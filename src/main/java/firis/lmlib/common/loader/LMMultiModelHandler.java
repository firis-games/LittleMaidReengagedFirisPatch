package firis.lmlib.common.loader;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import firis.lmlib.LMLibrary;
import firis.lmlib.common.config.LMLConfig;
import firis.lmlib.common.helper.ResourceFileHelper;
import firis.lmmm.api.model.ModelMultiBase;

/**
 * メイドさんのマルチモデルをロードする
 * 
 * @author firis-games
 *
 */
public class LMMultiModelHandler implements ILMFileLoaderHandler {

	public static LMMultiModelHandler instance = new LMMultiModelHandler();
	
	private LMMultiModelHandler() {}
	
	/**
	 * マルチモデルクラス保管用
	 */
	public static Map<String, Class<? extends ModelMultiBase>> multiModelClassMap = new HashMap<>();
	
	/**
	 * マルチモデルのクラス名に含まれる文字列
	 */
	private static List<String> existsMultiModelNames = Arrays.asList("ModelMulti_", "ModelLittleMaid_");
	
	/**
	 * キャッシュフラグ
	 */
	private boolean isCache = false;
	
	/**
	 * キャッシュファイル名
	 */
	private String cacheFileName = "cache_multimodelpack.json";
	
	/**
	 * Handlerの初期化処理
	 * キャッシュ確認しキャッシュがあれば読込する
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		
		//キャッシュ機能の利用可否
		if (!LMLConfig.cfg_loader_is_cache) return;
		
		//変換用キャッシュ
		Map<String, String> cachemultiModelClassMap = new HashMap<>();
		cachemultiModelClassMap = ResourceFileHelper.readFromJson(this.cacheFileName, Map.class);
		if (cachemultiModelClassMap == null) return;
		
		//内部設定へ変換する
		multiModelClassMap = new HashMap<>();
		
		for (String className : cachemultiModelClassMap.keySet()) {
			try {
				//クラスをロード
				Class<?> modelClass;
				modelClass = Class.forName(cachemultiModelClassMap.get(className));
				
				//クラスを登録
				multiModelClassMap.put(className, (Class<? extends ModelMultiBase>) modelClass);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		if (multiModelClassMap.size() > 0) {
			this.isCache = true;
		}
	}
	
	/**
	 * キャッシュがある場合は読み込み処理を行わない
	 */
	@Override
	public boolean isFileLoad() {
		return !this.isCache;
	}
	
	/**
	 * 対象ファイルがマルチモデルか判断する
	 * 
	 * ・拡張子が.class
	 *　・クラス名にexistsMultiModelNamesを含む
	 */
	@Override
	public boolean isLoadHandler(String path, Path filePath) {
		
		//.class判定
		if (path != null && path.endsWith(".class")) {
			
			String className = path.substring(path.lastIndexOf("/") + 1);
			
			//クラスファイル名チェック
			return existsMultiModelNames.stream()
					.anyMatch(n -> className.indexOf(n) > -1);
		}
		return false;
	}
	
	/**
	 * マルチモデルクラスをロード
	 * 
	 *　ModelMultiBaseを継承しているかはこのタイミングでチェックする
	 *　クラス名から識別子を削除した名称をモデルIDとして登録する
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void loadHandler(String path, Path filePath, InputStream inputstream) {
		
		try {
			//ClassLoader用パスへ変換
			String classpath = path.replace("/", ".");
			classpath = classpath.substring(0, path.lastIndexOf(".class"));
			
			//クラスを取得
			Class<?> modelClass;
			modelClass = Class.forName(classpath);
			
			//マルチモデル
			if (ModelMultiBase.class.isAssignableFrom(modelClass)) {
				
				//モデルID
				String className = classpath.substring(classpath.lastIndexOf(".") + 1);
				//識別子削除
				for (String mmName : existsMultiModelNames) {
					int idx = className.indexOf(mmName);
					if (idx > -1) {
						className = className.substring(idx + mmName.length());
						break;
					}
				}
				
				//クラスを登録
				multiModelClassMap.put(className, (Class<? extends ModelMultiBase>) modelClass);
			}
			
		} catch (Exception e) {
			LMLibrary.logger.error(String.format("LMMultiModelHandler-Exception : %s", path), e);
		}
	}
	
	/**
	 * ファイル読込後の処理
	 * キャッシュファイルを出力する
	 */
	@Override
	public void postLoadHandler() {
		
		//キャッシュファイルを出力する
		if (LMLConfig.cfg_loader_is_cache) {
			
			//キャッシュファイル出力用に変換する
			Map<String, String> cachemultiModelClassMap = new HashMap<>();
			for (String key : multiModelClassMap.keySet()) {
				cachemultiModelClassMap.put(key, multiModelClassMap.get(key).getName());
			}
			
			//キャッシュ出力
			ResourceFileHelper.writeToJson(this.cacheFileName, cachemultiModelClassMap);
		}
		
	}
}
