package net.firis.lmt.common.manager;

import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

/**
 * プレイヤーモデル用の操作用クラス
 * 
 * TextureBoxから取得するモデル・テクスチャの取得する
 * @author firis-games
 *
 */
public class PlayerModelManager {

	//あかりちゃんの設定
	private static String testTexure = "MMM_Akari";
	private static Integer testTexureColorIndex = 8;
	
	//内部保持用textureBox
	private static TextureBox cacheTextureBox = null;

	/**
	 * EntityPlayerからTextureBoxを取得する
	 * 
	 * 暫定対応として固定のTextureBoxを返却する
	 * 
	 * @param player
	 * @return
	 */
	private static TextureBox getPlayerTexureBox(EntityPlayer player) {
		
		if (cacheTextureBox == null) {
			cacheTextureBox = ModelManager.instance.getTextureBox(testTexure);
		}
		return cacheTextureBox;
	}
	
	
	/**
	 * メイドさんモデル取得
	 */
	public static ModelMultiBase getPlayerModel(EntityPlayer player) {
		
		TextureBox textureBox = getPlayerTexureBox(player);
		
		//メイドさん本体のモデルを返却する
		return textureBox.models[0];
	}
	
	/**
	 * メイドさんモデルのテクスチャを取得
	 * 
	 * 暫定対応としてカラーコードは固定
	 */
	public static ResourceLocation getPlayerTexture(EntityPlayer player) {
		
		TextureBox textureBox = getPlayerTexureBox(player);
		
		//メイドさん本体のテクスチャを返却する
		return textureBox.getTextureName(testTexureColorIndex);
	}
	
	
	/**
	 * メイドさんアーマーモデル取得
	 * 
	 * innerModel = 1
	 * outerModel = 2
	 */
	public static ModelMultiBase getArmorModel(EntityPlayer player, EntityEquipmentSlot slot) {
		
		TextureBox textureBox = getPlayerTexureBox(player);
		
		//メイドさんのアーマーモデルを返却する
		return textureBox.models[1];
	}
	
	/**
	 * メイドさんモデルのテクスチャを取得
	 * 
	 * 暫定対応としていろいろ設定を固定化
	 */
	public static ResourceLocation getArmorTexture(EntityPlayer player, EntityEquipmentSlot slot) {
		
		TextureBox textureBox = getPlayerTexureBox(player);
		
		//メイドさんのアーマーテクスチャを返却する
		return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
	}
}
