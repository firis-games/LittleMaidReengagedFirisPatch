package net.firis.lmt.common.modelcaps;

import java.util.HashMap;
import java.util.Map;

import net.blacklab.lmr.entity.maidmodel.ModelConfigCompoundBase;
import net.blacklab.lmr.util.IModelCapsData;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.firis.lmt.config.FirisConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * PlayerAvatar用パラメータクラス
 * @author firis-games
 *
 */
public class PlayerModelConfigCompound extends ModelConfigCompoundBase {
	
	/**
	 * 内部保持用
	 * プレイヤー名をキーとして判断する
	 */
	private static Map<String, PlayerModelConfigCompound> modelConfigCompoundMap = new HashMap<>();
	
	/**
	 * コンストラクタ
	 * @param entity
	 * @param caps
	 */
	public PlayerModelConfigCompound(EntityLivingBase entity, IModelCapsData caps) {
		super(entity, caps);
	}
	
	/**
	 * EntityPlayerに紐づくModelConfigCompoundを取得する
	 * @return
	 */
	public static PlayerModelConfigCompound getModelConfigCompound(EntityPlayer player) {
		String key = player.getName();
		//存在していなければ初期化して作成する
		if (!modelConfigCompoundMap.containsKey(key)) {
			modelConfigCompoundMap.put(key, createModelConfigCompound(player));
		}
		return modelConfigCompoundMap.get(key); 
	}
	
	/**
	 * 設定ファイルの内容を反映する
	 */
	public static void syncConfig() {
		for (String key : modelConfigCompoundMap.keySet()) {
			refreshConfig(modelConfigCompoundMap.get(key));
		}
	}
	
	/**
	 * 設定から作成する
	 */
	private static PlayerModelConfigCompound createModelConfigCompound(EntityPlayer player) {
		PlayerModelConfigCompound playerModelConfig = new PlayerModelConfigCompound(player, new PlayerModelCaps(player));
		return refreshConfig(playerModelConfig);
	}
	
	/**
	 * 設定内容をオブジェクトに反映する
	 * @param playerModelConfig
	 */
	private static PlayerModelConfigCompound refreshConfig(PlayerModelConfigCompound playerModelConfig) {
		//モデル情報を設定する
		LMTextureBox maidBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_maid_model);
		LMTextureBox headBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_head);
		LMTextureBox bodyBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_body);
		LMTextureBox legBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_leg);
		LMTextureBox bootsBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_boots);
		
		playerModelConfig.setTextureBoxLittleMaid(maidBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.HEAD, headBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.CHEST, bodyBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.LEGS, legBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.FEET, bootsBox);
		
		playerModelConfig.setColor(FirisConfig.cfg_maid_color);
		playerModelConfig.setContract(true);
		
		return playerModelConfig;
	}

}
