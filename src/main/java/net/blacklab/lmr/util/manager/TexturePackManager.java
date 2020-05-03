package net.blacklab.lmr.util.manager;

import java.util.LinkedHashMap;
import java.util.Map;

import net.blacklab.lmr.util.loader.LMTextureHandler;
import net.blacklab.lmr.util.manager.pack.TexturePack;

/**
 * マルチモデルのテクスチャパックを管理する
 * @author firis-games
 *
 */
public class TexturePackManager {

	public static TexturePackManager instance = new TexturePackManager();
	
	/**
	 * モデルパックの一覧
	 */
	public Map<String, TexturePack> texturePackMap = new LinkedHashMap<>();
	
	/**
	 * 初期化
	 * 
	 * テクスチャ情報を元にテクスチャパックを内部処理用形式へ変更する
	 * 
	 */
	public void init() {
		
		//テクスチャを生成
		for (String key : LMTextureHandler.textureMap.keySet()) {
			
			//TexturePack生成
			TexturePack texturePack = new TexturePack(key, LMTextureHandler.textureMap.get(key));
			
			//TexturePack設定
			texturePackMap.put(key, texturePack);
			
		}
		
	}
	
	
	
}
