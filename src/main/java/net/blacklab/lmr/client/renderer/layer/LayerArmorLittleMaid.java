package net.blacklab.lmr.client.renderer.layer;

import net.blacklab.lmr.client.renderer.entity.RenderModelMulti;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
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
public class LayerArmorLittleMaid extends LayerArmorLittleMaidBase {

	public LayerArmorLittleMaid(RenderModelMulti<? extends EntityLiving> rendererIn) {
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
	
}
