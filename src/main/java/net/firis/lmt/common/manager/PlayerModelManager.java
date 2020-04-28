package net.firis.lmt.common.manager;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.firis.lmt.config.FirisConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * プレイヤーモデル用の操作用クラス
 * 
 * LMTextureBoxから取得するモデル・テクスチャの取得する
 * @author firis-games
 *
 */
public class PlayerModelManager {

	/*
	//あかりちゃんの設定
	private static String testTexure = "MMM_Akari";
	private static Integer testTexureColorIndex = 8;
	*/
	
	//内部保持用LMTextureBox
	private static LMTextureBox cacheLMTextureBox = null;

	/**
	 * EntityPlayerからLMTextureBoxを取得する
	 * 
	 * 暫定対応として固定のLMTextureBoxを返却する
	 * 
	 * @param player
	 * @return
	 */
	private static LMTextureBox getPlayerTexureBox(EntityPlayer player, String textureName) {
		
		/*
		//個別設定がある場合はこっち
		NBTTagCompound nbt = player.getEntityData();
		if (nbt.hasKey("maidModel")) {
			return ModelManager.instance.getLMTextureBox(nbt.getString("maidModel"));
		}
		
		if (cacheLMTextureBox == null) {
			cacheLMTextureBox = ModelManager.instance.getLMTextureBox(testTexure);
		}
		*/
		
		//設定から取得するように変更
		cacheLMTextureBox = LMTextureBoxManager.instance.getLMTextureBox(textureName);
		if (cacheLMTextureBox == null) {
			cacheLMTextureBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.DEFAULT_MAID_MODEL);			
		}
		return cacheLMTextureBox;
	}
	
	
	/**
	 * メイドさんモデル取得
	 */
	public static ModelMultiBase getPlayerModel(EntityPlayer player) {
		
		LMTextureBox lmTextureBox = getPlayerTexureBox(player, FirisConfig.cfg_maid_model);
		
		//メイドさん本体のモデルを返却する
		return lmTextureBox.getModelLittleMaid();
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
		LMTextureBox LMTextureBox = getPlayerTexureBox(player);
		return LMTextureBox.getTextureName(testTexureColorIndex);
		*/
		
		LMTextureBox lmTextureBox = getPlayerTexureBox(player, FirisConfig.cfg_maid_model);
		ResourceLocation rlTexture = lmTextureBox.getTextureLittleMaid(FirisConfig.cfg_maid_color);
		if (rlTexture == null) {
			rlTexture = lmTextureBox.getTextureLittleMaidDefault();
		}
		//メイドさん本体のテクスチャを返却する
		return rlTexture;
	}
	
	/**
	 * プレイヤーの発光テクスチャ取得
	 * @param player
	 * @return
	 */
	public static ResourceLocation getPlayerTextureLight(EntityPlayer player) {
		
		LMTextureBox lmTextureBox = getPlayerTexureBox(player, FirisConfig.cfg_maid_model);
		ResourceLocation rlTexture = lmTextureBox.getLightTextureLittleMaid(FirisConfig.cfg_maid_color);
		
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
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("Accessories.Normal.PartySet1_ACUL");
			return LMTextureBox.models[1];
		}
		if (slot == EntityEquipmentSlot.CHEST) {
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("littlePorters.Blue_LP");
			return LMTextureBox.models[1];
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("Accessories.Normal.ExpeditionSet1_ACUL");
			return LMTextureBox.models[1];
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
		
		LMTextureBox lmTextureBox = getPlayerTexureBox(player, textureName);
		
		List<ModelMultiBase> modelList = new ArrayList<>();
		
		//メイドさんのアーマーモデルを返却する
		//innerモデルとouterモデルを取得する
		modelList.add(lmTextureBox.getModelInnerArmor());
		modelList.add(lmTextureBox.getModelOuterArmor());

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
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("Accessories.Normal.PartySet1_ACUL");
			return LMTextureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.CHEST) {
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("littlePorters.Blue_LP");
			return LMTextureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("Accessories.Normal.ExpeditionSet1_ACUL");
			return LMTextureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
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
		
		//アーマーアイテムを取得する
		ItemStack armorStack = player.inventory.armorInventory.get(slot.getIndex());
		
		LMTextureBox lmTextureBox = getPlayerTexureBox(player, textureName);
		
		List<ResourceLocation> texturelList = new ArrayList<>();
		
		texturelList.add(lmTextureBox.getTextureInnerArmor(armorStack));
		texturelList.add(lmTextureBox.getTextureOuterArmor(armorStack));
		
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
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("Accessories.Normal.PartySet1_ACUL");
			return LMTextureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.CHEST) {
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("littlePorters.Blue_LP");
			return LMTextureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			LMTextureBox LMTextureBox = ModelManager.instance.getLMTextureBox("Accessories.Normal.ExpeditionSet1_ACUL");
			return LMTextureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0);
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
		
		//アーマーアイテムを取得する
		ItemStack armorStack = player.inventory.armorInventory.get(slot.getIndex());
		
		LMTextureBox lmTextureBox = getPlayerTexureBox(player, textureName);
		
		List<ResourceLocation> texturelList = new ArrayList<>();
		
		texturelList.add(lmTextureBox.getLightTextureInnerArmor(armorStack));
		texturelList.add(lmTextureBox.getLightTextureOuterArmor(armorStack));
		
		//メイドさんのアーマーテクスチャを返却する
		return texturelList;
	}
}
