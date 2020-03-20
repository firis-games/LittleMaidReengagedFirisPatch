package net.firis.lmt.client.model;

import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * マルチモデルを扱うためのラッパーモデル
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class ModelLittleMaidMultiModelArmor extends ModelBase {

	private PlayerModelCaps playerCaps = null;
	private ModelMultiBase armorModel = null;
	
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidMultiModelArmor() {
	}
	
	/**
	 * アーマーモデルのセットアップ
	 * 
	 * doRenderのタイミングで必要な情報を内部変数へセットする
	 * ※重複処理を排除するため
	 */
	public void initArmorModel(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot) {
		
		EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		
		//アーマーモデルの準備
		armorModel = PlayerModelManager.getArmorModel(player, slot);
		playerCaps = getModelCaps(armorModel, player);
		
		//モデルの表示設定
		armorModel.showArmorParts(slot.getIndex(), 0);
		
	}
	
	/**
	 * メイドさんの向きなどを設定
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, playerCaps);
		
	}
	
	/**
	 * メイドさんのアニメーション設定
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

		armorModel.setLivingAnimations(playerCaps, limbSwing, limbSwingAmount, partialTickTime);
	}
	
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		armorModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
	}
	
	/**
	 * マルチモデル描画用ModelCapsをセットアップ
	 * 
	 * マルチモデル側のsetCapsValueを呼び出して最低限必要な情報を設定する
	 * 内部変数にセットする
	 * @param player
	 * @return
	 */
	protected PlayerModelCaps getModelCaps(ModelMultiBase modelMain, EntityPlayer player) {
		
		PlayerModelCaps caps = new PlayerModelCaps(player);
		
		//モデルにCaps情報を設定する
		caps.setModelMultiBaseCapsFromModelCaps(modelMain);
		
		return caps;
	}
}
