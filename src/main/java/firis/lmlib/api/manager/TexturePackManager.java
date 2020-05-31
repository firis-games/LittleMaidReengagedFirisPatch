package firis.lmlib.api.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import firis.lmlib.api.constant.EnumColor;
import firis.lmlib.api.resource.TexturePack;
import firis.lmlib.common.loader.LMTextureHandler;

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
		
		this.initBuiltInTexture();
		
	}
	
	/**
	 * 内蔵テクスチャのセットアップ
	 */
	public void initBuiltInTexture() {
		
		//Steavモデル
		TexturePack steve = new TexturePack("crafter_Steve", new ArrayList<>());
		steve.textureLittleMaid.put(EnumColor.BROWN, "assets/minecraft/textures/entity/lmsteve/steve.png");
		texturePackMap.put("crafter_Steve", steve);
		
		//Stefモデル
		TexturePack stef = new TexturePack("crafter_Stef", new ArrayList<>());
		stef.textureLittleMaid.put(EnumColor.BROWN, "assets/minecraft/textures/entity/lmsteve/steve.png");
		
		texturePackMap.put("crafter_Stef", stef);
		
		//防具モデル
		List<String> armorList = new ArrayList<>(Arrays.asList("leather", "chainmail", "iron", "diamond", "gold"));
		for (String armorId : armorList) {
			
			String armorKey = armorId + "Armor_Steve";
			TexturePack armorPack = new TexturePack(armorKey, new ArrayList<>());
			
			armorPack.textureInnerArmor.put(0, "assets/minecraft/textures/models/armor/" + armorId + "_layer_2.png");
			armorPack.textureOuterArmor.put(0, "assets/minecraft/textures/models/armor/" + armorId + "_layer_1.png");
			
			texturePackMap.put(armorKey, armorPack);
		}
	}
	
}
