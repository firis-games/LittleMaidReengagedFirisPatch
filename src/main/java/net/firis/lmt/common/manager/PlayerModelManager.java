package net.firis.lmt.common.manager;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.texture.TextureBox;
import net.blacklab.lmr.util.manager.ModelManager;
import net.firis.lmt.config.FirisConfig;
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

	/*
	//あかりちゃんの設定
	private static String testTexure = "MMM_Akari";
	private static Integer testTexureColorIndex = 8;
	*/
	
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
	private static TextureBox getPlayerTexureBox(EntityPlayer player, String textureName) {
		
		/*
		//個別設定がある場合はこっち
		NBTTagCompound nbt = player.getEntityData();
		if (nbt.hasKey("maidModel")) {
			return ModelManager.instance.getTextureBox(nbt.getString("maidModel"));
		}
		
		if (cacheTextureBox == null) {
			cacheTextureBox = ModelManager.instance.getTextureBox(testTexure);
		}
		*/
		
		//設定から取得するように変更
		cacheTextureBox = ModelManager.instance.getTextureBox(textureName);
		if (cacheTextureBox == null) {
			cacheTextureBox = ModelManager.instance.getTextureBox(FirisConfig.DEFAULT_MAID_MODEL);			
		}
		return cacheTextureBox;
	}
	
	
	/**
	 * メイドさんモデル取得
	 */
	public static ModelMultiBase getPlayerModel(EntityPlayer player) {
		
		TextureBox textureBox = getPlayerTexureBox(player, FirisConfig.cfg_maid_model);
		
		//メイドさん本体のモデルを返却する
		return textureBox.models[0];
	}
	
	/**
	 * メイドさんモデルのテクスチャを取得
	 * 
	 * 暫定対応としてカラーコードは固定
	 */
	public static ResourceLocation getPlayerTexture(EntityPlayer player) {
		
		/*
		//個別設定がある場合はこっち
		NBTTagCompound nbt = player.getEntityData();
		if (nbt.hasKey("maidTexture")) {
			return new ResourceLocation(nbt.getString("maidTexture"));
		}
		TextureBox textureBox = getPlayerTexureBox(player);
		return textureBox.getTextureName(testTexureColorIndex);
		*/
		
		TextureBox textureBox = getPlayerTexureBox(player, FirisConfig.cfg_maid_model);
		ResourceLocation rlTexture = textureBox.getTextureName(FirisConfig.cfg_maid_color);
		if (rlTexture == null) {
			rlTexture = textureBox.getTextureNameDefault();
		}
		//メイドさん本体のテクスチャを返却する
		return rlTexture;
	}
	
	public static ResourceLocation getPlayerTextureLight(EntityPlayer player) {
		
		TextureBox textureBox = getPlayerTexureBox(player, FirisConfig.cfg_maid_model);
		ResourceLocation rlTexture = textureBox.getTextureName(19);
		
		return rlTexture;
	}
	
	
	/**
	 * メイドさんアーマーモデル取得
	 * 
	 * innerModel = 1
	 * outerModel = 2
	 */
	public static List<ModelMultiBase> getArmorModels(EntityPlayer player, EntityEquipmentSlot slot) {
		
		//特別処理
		/*
		if (slot == EntityEquipmentSlot.HEAD) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("Accessories.Normal.PartySet1_ACUL");
			return textureBox.models[1];
		}
		if (slot == EntityEquipmentSlot.CHEST) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("littlePorters.Blue_LP");
			return textureBox.models[1];
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("Accessories.Normal.ExpeditionSet1_ACUL");
			return textureBox.models[1];
		}
		*/
		
		String textureName = "";
		switch (slot) {
		case HEAD:
			textureName = FirisConfig.cfg_armor_model_head;
			break;
		case CHEST:
			textureName = FirisConfig.cfg_armor_model_body;
			break;
		case LEGS:
			textureName = FirisConfig.cfg_armor_model_leg;
			break;
		case FEET:
			textureName = FirisConfig.cfg_armor_model_boots;
			break;
		default:
		}
		
		TextureBox textureBox = getPlayerTexureBox(player, textureName);
		
		List<ModelMultiBase> modelList = new ArrayList<>();
		
		//メイドさんのアーマーモデルを返却する
		//innerモデルとouterモデルを取得する
		modelList.add(textureBox.models[1]);
		modelList.add(textureBox.models[2]);

		return modelList;
	}
	
	/**
	 * メイドさんモデルのテクスチャを取得
	 * 
	 * 暫定対応としていろいろ設定を固定化
	 */
	public static List<ResourceLocation> getArmorTexture(EntityPlayer player, EntityEquipmentSlot slot) {
		
		//特別処理
		/*
		if (slot == EntityEquipmentSlot.HEAD) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("Accessories.Normal.PartySet1_ACUL");
			return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.CHEST) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("littlePorters.Blue_LP");
			return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("Accessories.Normal.ExpeditionSet1_ACUL");
			return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		*/
		
		String textureName = "";
		switch (slot) {
		case HEAD:
			textureName = FirisConfig.cfg_armor_model_head;
			break;
		case CHEST:
			textureName = FirisConfig.cfg_armor_model_body;
			break;
		case LEGS:
			textureName = FirisConfig.cfg_armor_model_leg;
			break;
		case FEET:
			textureName = FirisConfig.cfg_armor_model_boots;
			break;
		default:
		}
		
		TextureBox textureBox = getPlayerTexureBox(player, textureName);
		
		List<ResourceLocation> texturelList = new ArrayList<>();
		
		texturelList.add(textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0));
		texturelList.add(textureBox.getArmorTextureName(ModelManager.tx_armor2, "leather", 0));
		
		//メイドさんのアーマーテクスチャを返却する
		return texturelList;
	}
	
	/**
	 * メイドさんモデルのテクスチャを取得
	 * 
	 * 暫定対応としていろいろ設定を固定化
	 */
	public static List<ResourceLocation> getArmorLightTexture(EntityPlayer player, EntityEquipmentSlot slot) {
		
		//特別処理
		/*
		if (slot == EntityEquipmentSlot.HEAD) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("Accessories.Normal.PartySet1_ACUL");
			return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.CHEST) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("littlePorters.Blue_LP");
			return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			TextureBox textureBox = ModelManager.instance.getTextureBox("Accessories.Normal.ExpeditionSet1_ACUL");
			return textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		*/
		
		String textureName = "";
		switch (slot) {
		case HEAD:
			textureName = FirisConfig.cfg_armor_model_head;
			break;
		case CHEST:
			textureName = FirisConfig.cfg_armor_model_body;
			break;
		case LEGS:
			textureName = FirisConfig.cfg_armor_model_leg;
			break;
		case FEET:
			textureName = FirisConfig.cfg_armor_model_boots;
			break;
		default:
		}
		
		TextureBox textureBox = getPlayerTexureBox(player, textureName);
		
		List<ResourceLocation> texturelList = new ArrayList<>();
		
		texturelList.add(textureBox.getArmorTextureName(ModelManager.tx_armor1light, "leather", 0));
		texturelList.add(textureBox.getArmorTextureName(ModelManager.tx_armor2light, "leather", 0));
		
		//メイドさんのアーマーテクスチャを返却する
		return texturelList;
	}
}
