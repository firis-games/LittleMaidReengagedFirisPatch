package net.firis.lmt.client.renderer.layer;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.renderer.layer.LMLayerArmorBase;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * メイドさんの防具Layer
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class LayerArmorLMAvatar extends LMLayerArmorBase {

	public LayerArmorLMAvatar(RenderLivingBase<? extends EntityLivingBase> rendererIn) {
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
		EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
		if (player.inventory.armorItemInSlot(slot.getIndex()).isEmpty()) return false;
		
		return true;
	}
	
	@Override
	protected IModelCompound getArmorModelConfigCompound(EntityLivingBase entityLivingBaseIn, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale,
			EntityEquipmentSlot slot) {
		EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
		return PlayerModelManager.getModelConfigCompound(player);
	}
	
}
