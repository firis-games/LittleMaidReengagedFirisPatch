package firis.lmlib.common.loader;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import firis.lmlib.LMLibrary;
import firis.lmlib.common.config.LMLConfig;
import firis.lmlib.common.helper.ResourceFileHelper;

/**
 * メイドさんのテクスチャをロードする
 * 
 * @author firis-games
 *
 */
public class LMTextureHandler implements ILMFileLoaderHandler {
	
	public static LMTextureHandler instance = new LMTextureHandler();
	
	private LMTextureHandler() {}
	
	/**
	 * テクスチャ保管用
	 * 大文字小文字を区別せずにソートする
	 */
	public static Map<String, List<String>> textureMap = new TreeMap<>(new Comparator<String>() {
		@Override
	    public int compare(String o1, String o2) {
	        return o1.toLowerCase().compareTo(o2.toLowerCase());
	    }
	});
	
	/**
	 * テクスチャが存在する階層設定
	 */
	private static List<String> lmTexturePath = Arrays.asList(
			"assets/minecraft/textures/entity/ModelMulti/", 
			"assets/minecraft/textures/entity/littleMaid/",
			"mob/ModelMulti/",
			"mob/littleMaid/");
	
	
	/**
	 * キャッシュフラグ
	 */
	private boolean isCache = false;
	
	/**
	 * キャッシュファイル名
	 */
	private String cacheFileName = "cache_texturepack.json";
	
	/**
	 * Handlerの初期化処理
	 * キャッシュ確認しキャッシュがあれば読込する
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		
		//キャッシュ機能の利用可否
		if (!LMLConfig.cfg_loader_is_cache) return;
		
		//キャッシュファイルの読み込み
		textureMap = (Map<String, List<String>>) ResourceFileHelper.readFromJson(this.cacheFileName, Map.class);
		
		if (textureMap != null) {
			this.isCache = true;
		} else {
			//初期化
			textureMap = new TreeMap<>();
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
	 * 対象ファイルがテクスチャか判断する
	 * 
	 * ・拡張子が.png
	 *　・pathをテクスチャ名に変換可
	 */
	@Override
	public boolean isLoadHandler(String path, Path filePath) {
		//.png判定
		if (path != null && path.endsWith(".png")) {
			//テクスチャ名に変換できない場合はメイドさんテクスチャではない
			return !"".equals(getTextureName(path));
		}
		return false;
	}
	
	/**
	 * テクスチャをロード
	 * 
	 *　ModelMultiBaseを継承しているかはこのタイミングでチェックする
	 *　クラス名から識別子を削除した名称をモデルIDとして登録する
	 */
	@Override		
	public void loadHandler(String path, Path filePath, InputStream inputstream) {
		
		//テクスチャ名生成
		//[パッケージ].[テクスチャ名]_[モデル名]
		String textureName = getTextureName(path);
		
		//色番号取得
		int colorIndex = getColorIndex(path);
		
		//テクスチャ保存
		if (colorIndex != -1) {
			if (!textureMap.containsKey(textureName)) {
				//初期化
				textureMap.put(textureName, new ArrayList<>());
			}
			textureMap.get(textureName).add(path);
		} else {
			//カラー番号が不正な場合はエラーとして扱う
			LMLibrary.logger.error(String.format("LMTextureHandler-ColorIndexException : %s", path));
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
			ResourceFileHelper.writeToJson(this.cacheFileName, textureMap);
		}
		
	}
	
	/**
	 * テクスチャパスからテクスチャ名を取得する
	 * 大文字小文字は無視する
	 * @return
	 */
	private String getTextureName(String path) {
		
		String textureName = "";
		for (String workPath : lmTexturePath) {
			int idx = path.toLowerCase().indexOf(workPath.toLowerCase());
			if (0 <= idx) {
				//ルートパスを削除
				idx += workPath.length();
				textureName = path.substring(idx);
				break;
			}
		}
		if (!"".equals(textureName)) {
			//テクスチャファイル名を削除
			int idx = textureName.lastIndexOf("/");
			if (idx == -1) {
				//モデル名が存在しない場合
				textureName = "";
			} else {
				textureName = textureName.substring(0, idx);					
				
				//テクスチャ名変換
				textureName = textureName.replace("/", ".");
			}
		}
		return textureName;
	}
	
	//colorインデックス設定
	private static final int tx_oldwild		= 0x10; //16;
	private static final int tx_oldarmor1	= 0x11; //17;
	private static final int tx_oldarmor2	= 0x12; //18;
	
	public static final int tx_gui			= 0x20; //32;
	public static final int tx_wild			= 0x30; //48;
	public static final int tx_armor1		= 0x40; //64;
	public static final int tx_armor2		= 0x50; //80;
	public static final int tx_eye			= 0x60; //96;
	public static final int tx_eyecontract	= 0x60; //96;
	public static final int tx_eyewild		= 0x70; //112;
	public static final int tx_armor1light	= 0x80; //128;
	public static final int tx_armor2light	= 0x90; //144;
	
	/**
	 * 旧タイプのファイル名
	 */
	protected static String defNames[] = {
		"mob_littlemaid0.png", "mob_littlemaid1.png",
		"mob_littlemaid2.png", "mob_littlemaid3.png",
		"mob_littlemaid4.png", "mob_littlemaid5.png",
		"mob_littlemaid6.png", "mob_littlemaid7.png",
		"mob_littlemaid8.png", "mob_littlemaid9.png",
		"mob_littlemaida.png", "mob_littlemaidb.png",
		"mob_littlemaidc.png", "mob_littlemaidd.png",
		"mob_littlemaide.png", "mob_littlemaidf.png",
		"mob_littlemaidw.png",
		"mob_littlemaid_a00.png", "mob_littlemaid_a01.png"
	};
	
	/**
	 * テクスチャファイルからcolor番号を取得する
	 */
	public static int getColorIndex(String name) {
		
		int colorIdx = -1;
		// 旧タイプのファイル名からindexを取得する
		for (int i = 0; i < defNames.length; i++) {
			if (name.endsWith(defNames[i])) {
				colorIdx = i;
				break;
			}
		}
		
		//　末尾16進数からカラーindexを取得する
		if (colorIdx == -1) {
			Pattern p = Pattern.compile("_([0-9a-f]+).png");
			Matcher m = p.matcher(name);
			if (m.find()) {
				colorIdx = Integer.decode("0x" + m.group(1));
			}
		}
		
		// colorIndexの変換
		if (colorIdx == tx_oldarmor1) {
			colorIdx = tx_armor1;
		}
		if (colorIdx == tx_oldarmor2) {
			colorIdx = tx_armor2;
		}
		if (colorIdx == tx_oldwild) {
			colorIdx = tx_wild + 12;
		}
		
		return colorIdx;
	}
}
