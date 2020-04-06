package net.blacklab.lmr.util.loader;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;

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
	 * 対象ファイルがマルチモデルか判断する
	 * 
	 * ・拡張子が.class
	 *　・クラス名にexistsMultiModelNamesを含む
	 */
	@Override
	public boolean isLoader(String path, Path filePath) {
		
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
			LittleMaidReengaged.logger.error(String.format("LMMultiModelHandler-Exception : %s", path));
			if (LMRConfig.cfg_PrintDebugMessage) e.printStackTrace();
		}
	}
}
