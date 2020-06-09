package firis.lmlib.common.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import firis.lmlib.LMLibrary;
import firis.lmlib.common.config.LMLConfig;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

/**
 * メイドさんの外部読み込みファイルのLoader
 * ILMFileLoaderHandler実装クラスで読込対象のチェックと読込処理を行う
 * 
 * @author firis-games
 */
public class LMFileLoader {
	
	public static LMFileLoader instance = new LMFileLoader();
	
	private List<ILMFileLoaderHandler> loaderHandler = new ArrayList<>();
	
	/**
	 * 初期化処理
	 */
	private LMFileLoader() {
		
		//マルチモデルHandler
		loaderHandler.add(LMMultiModelHandler.instance);
		
		//テクスチャHandler
		loaderHandler.add(LMTextureHandler.instance);
		
		//サウンドHandler
		loaderHandler.add(LMSoundHandler.instance);
		
	}
	
	/**
	 * ファイルロード処理
	 * 
	 * メイドさん関連ファイルを読み込む共通処理を行う
	 */
	public void load() {
		
		LMLibrary.logger.info("LMFileLoader-load : start");
		
		//LoaderHandlerの初期化処理
		for (ILMFileLoaderHandler handler : loaderHandler) {
			handler.init();
		}
		
		//Loaderの動作チェック
		List<ILMFileLoaderHandler> procLoaderHandler = new ArrayList<>();
		for (ILMFileLoaderHandler handler : loaderHandler) {
			if (handler.isFileLoad()) {
				//処理対象のHandler登録
				procLoaderHandler.add(handler);
			}
		}
		
		//Loaderのファイル読込処理をスキップ
		if (procLoaderHandler.size() == 0) {
			LMLibrary.logger.info("LMFileLoader-load : cache-load end");
			return;
		};
		
		//ファイルパスリストを生成
		List<ModPath> filePathList = new ArrayList<>();
		
		//ファイルのリスト作成
		try {
			filePathList = getLoadPath();
		} catch (Exception e) {
			LMLibrary.logger.error("LMFileLoader-Exception : getLoadPath", e);
			return;
		}
		
		//パス走査
		for (ModPath loaderPath : filePathList) {
			try {
				//拡張子判定
				if (loaderPath.isExtension(".jar", ".zip")) {
					//zip or jarファイル
					ZipInputStream zipStream = new ZipInputStream(Files.newInputStream(loaderPath.path));
					ZipEntry zipEntry;
					Path zipPath = loaderPath.path;
					while ((zipEntry = zipStream.getNextEntry()) != null) {
						String zPath = zipEntry.getName();
						//handlerに処理を委譲
						for (ILMFileLoaderHandler handler : procLoaderHandler) {
							if (handler.isLoadHandler(zPath, zipPath)) {
								handler.loadHandler(zPath, zipPath, zipStream);
								break;
							}
						}
					}
				} else {
					//通常ファイル
					String lPath = loaderPath.getLoaderPath();
					//handlerに処理を委譲
					for (ILMFileLoaderHandler handler : procLoaderHandler) {
						if (handler.isLoadHandler(lPath, null)) {
							handler.loadHandler(lPath, null, null);
							break;
						}
					}
					
				}
			} catch (Exception e){
				LMLibrary.logger.error(String.format("LMFileLoader-LoadException : %s", loaderPath.path.toString()), e);
			}
		}
		
		//読込後処理を呼び出し
		for (ILMFileLoaderHandler handler : procLoaderHandler) {
			handler.postLoadHandler();
		}
		
		LMLibrary.logger.info("LMFileLoader-load : end");
	}
	
	/**
	 * ファイル読込対象のパスリストを生成する
	 * 
	 * 通常：modsフォルダ配下
	 * 開発：modsフォルダ配下・classloaderパス
	 * @return
	 */
	private List<ModPath> getLoadPath() throws IOException {
		
		List<ModPath> pathList = new ArrayList<>();
		
		//開発環境専用パス
		if (LMLConfig.DEVELOPER_MODE) {
			String classPath = System.getProperty("java.class.path");
			String separator = System.getProperty("path.separator");
			
			List<String> classPathList = Arrays.asList(classPath.split(separator));
			
			//対象パスからファイルを取得する
			for (String path : classPathList) {
				//Pathリストを追加
				Path basePath = Paths.get(path);
				if (Files.exists(basePath)) {
					pathList.addAll(
							Files.walk(basePath)
							.filter(p -> !Files.isDirectory(p))
							.map(p -> {
								return new ModPath(basePath, p);
							})
							.collect(Collectors.toList()));
				}
			}
		}
		
		//通常パス
		Path mcHomePath = getMinecraftHomePath();
		//ひとまずはmodsフォルダの下のみ
		Path modsPath = Paths.get(mcHomePath.toString(), "mods");
		//Pathリストを追加
		pathList.addAll(
				Files.walk(modsPath)
				.filter(p -> !Files.isDirectory(p))
				.map(p -> {
					return new ModPath(modsPath, p);
				})
				.collect(Collectors.toList()));
		
		
		return pathList;
	}
	
	/**
	 * MinecraftのHomeパスを取得する
	 * @return
	 */
	private Path getMinecraftHomePath() {
		//Minecraftのホームパスを取得
		File minecraftHome = (File) FMLInjectionData.data()[6];
		return Paths.get(minecraftHome.toURI());
	}
	
	/**
	 * FileLoader用パス管理クラス
	 *
	 */
	private static class ModPath {
		
		private Path basePath;
		private Path path;
		
		public ModPath(Path basePath, Path path) {
			this.basePath = basePath;
			this.path = path;
		}
		
		/**
		 * ClassLoaderアクセス用のパスに変換
		 */
		public String getLoaderPath() {
			String loaderPath = this.path.toString().replace(this.basePath.toString(), "");
			loaderPath = loaderPath.replace("\\", "/");
			loaderPath = loaderPath.startsWith("/") ? loaderPath.substring(1) : loaderPath;
			return loaderPath.replace("\\", "/");
		}
		
		/**
		 * 拡張子のチェック処理
		 * @return
		 */
		public boolean isExtension(String... exts) {
			for (String ext : exts) {
				if (this.path.toString().endsWith(ext)) return true;
			}
			return false;
		}
	}
	

}
