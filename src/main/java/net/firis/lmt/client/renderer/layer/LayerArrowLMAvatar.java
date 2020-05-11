package net.firis.lmt.client.renderer.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.entity.EntityLivingBase;

public class LayerArrowLMAvatar extends LayerArrow {

	public LayerArrowLMAvatar(RenderLivingBase<?> renderer) {
		super(renderer);
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		GlStateManager.pushMatrix();
		
		//メイドさん用に位置とサイズ調整する
		float eltrascale = 0.625F;
		GlStateManager.scale(eltrascale, eltrascale, eltrascale);
		GlStateManager.translate(0.05F, 0.25F, -0.15F);

		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.popMatrix();
		
	}
	
}
