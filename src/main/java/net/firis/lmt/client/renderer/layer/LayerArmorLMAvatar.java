package net.firis.lmt.client.renderer.layer;

import net.blacklab.lmr.client.renderer.layer.LayerArmorLittleMaidBase;
import net.blacklab.lmr.entity.maidmodel.IModelConfigCompound;
import net.firis.lmt.common.modelcaps.PlayerModelConfigCompound;
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
public class LayerArmorLMAvatar extends LayerArmorLittleMaidBase {

	public LayerArmorLMAvatar(RenderLivingBase<? extends EntityLivingBase> rendererIn) {
		super(rendererIn);
	}
	
	@Override
	protected IModelConfigCompound getArmorModelConfigCompound(EntityLivingBase entityLivingBaseIn, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale,
			EntityEquipmentSlot slot) {
		EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
		return PlayerModelConfigCompound.getModelConfigCompound(player);
	}
	
}
