package net.blacklab.lmr.client.renderer.layer;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import firis.lmlib.api.client.renderer.layer.LMLayerArmorBase;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.IMultiModelEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * メイドさんの防具Layer
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class LayerArmorLittleMaid extends LMLayerArmorBase {

	public LayerArmorLittleMaid(LMRenderMultiModel<? extends EntityLiving> rendererIn) {
		super(rendererIn);
	}

	/**
	 * 防具の描画判定
	 * @param entityLivingBaseIn
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param partialTicks
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 * @param scale
	 * @param slot
	 * @return
	 */
	@Override
	protected boolean isRenderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot) {
		//対象スロットの描画対象チェック
		EntityLittleMaid maid = (EntityLittleMaid) entityLivingBaseIn;
		if (maid.maidInventory.armorItemInSlot(slot.getIndex()).isEmpty()) return false;
		
		return true;
	}
	
	@Override
	protected IModelCompound getArmorModelConfigCompound(EntityLivingBase entityLivingBaseIn, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale,
			EntityEquipmentSlot slot) {
		IMultiModelEntity modelEntity = (IMultiModelEntity) entityLivingBaseIn;
		IModelCompound modelConfigCompound = modelEntity.getModelConfigCompound();
		return modelConfigCompound;
	}
	
}
