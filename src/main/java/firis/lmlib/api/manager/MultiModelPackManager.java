package firis.lmlib.api.manager;

import java.util.LinkedHashMap;
import java.util.Map;

import firis.lmlib.LMLibrary;
import firis.lmlib.api.resource.MultiModelPack;
import firis.lmlib.common.loader.LMMultiModelHandler;
import firis.lmmm.api.model.ModelMultiBase;

/**
 * マルチモデルパックを管理する
 * @author firis-games
 *
 */
public class MultiModelPackManager {
	
	public static MultiModelPackManager instance = new MultiModelPackManager();
	
	/**
	 * モデルパックの一覧
	 */
	protected Map<String, MultiModelPack> multiModelPackMap = new LinkedHashMap<>();
	
	/**
	 * デフォルトモデルパック名
	 */
	public static final String defaultMultiModelName = "Orign";
	
	/**
	 * 初期化
	 * 
	 * クラス情報を元にマルチモデルをインスタンス化する
	 * 
	 */
	public void init() {
		
		//マルチモデル生成
		for (String key : LMMultiModelHandler.multiModelClassMap.keySet()) {
			
			Class<? extends ModelMultiBase> clazz;
			try {
				
				//マルチモデルクラスを準備
				clazz = LMMultiModelHandler.multiModelClassMap.get(key);
				
				MultiModelPack mmodelPack = new MultiModelPack(key, clazz);

				//登録
				multiModelPackMap.put(key, mmodelPack);
				
				//モデルロードメッセージ
				LMLibrary.logger.info(String.format("MultiModelPackManager-Load-MultiModel : %s", key));

			} catch (Exception e) {
				LMLibrary.logger.error(String.format("MultiModelPackManager-MultiModelInstanceException : %s", ""), e);
			}
		}
	}
	
	/**
	 * テクスチャパックに紐づくマルチモデルを取得する
	 * @return
	 */
	public MultiModelPack getMultiModelPackWithTexturePack(String multiModelPackName) {
		
		//設定がない場合はデフォルトマルチモデルを返す
		if (multiModelPackName.equals("")) {
			return multiModelPackMap.get(defaultMultiModelName);
		}
		
		for (String mmpackName : multiModelPackMap.keySet()) {
			
			//大文字小文字の違いは無視する
			if (multiModelPackName.toLowerCase().equals(mmpackName.toLowerCase())) {
				return multiModelPackMap.get(mmpackName);
			}
		}
		//対象がない場合は無視
		return null;
	}
}
