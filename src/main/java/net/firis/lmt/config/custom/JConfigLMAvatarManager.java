package net.firis.lmt.config.custom;

import firis.lmlibrary.loader.pack.ResourceFileHelper;

/**
 * 独自設定の管理用クラス
 * @author firis-games
 *
 */
public class JConfigLMAvatarManager {

	public static JConfigLMAvatarModelMap lmAvatarModels = new JConfigLMAvatarModelMap();
	
	/**
	 * キャッシュファイル名
	 */
	private static String configFileName = "lmrfp_avatar_list.json";
	
	/**
	 * Configの読み込み
	 */
	public static void init() {
		lmAvatarModels = ResourceFileHelper.readFromJson(configFileName, JConfigLMAvatarModelMap.class);
		//ない場合は初期化
		if (lmAvatarModels == null) {
			lmAvatarModels = new JConfigLMAvatarModelMap();
			JConfigLMAvatarModel model = new JConfigLMAvatarModel();
			lmAvatarModels.put("default", model);
		}
		ResourceFileHelper.writeToJson(configFileName, lmAvatarModels);
	}
	
	/**
	 * Configを保存する
	 */
	public static void save(String name, JConfigLMAvatarModel model) {
		
		lmAvatarModels.put(name, model);

		//保存
		ResourceFileHelper.writeToJson(configFileName, lmAvatarModels);
	}
}
