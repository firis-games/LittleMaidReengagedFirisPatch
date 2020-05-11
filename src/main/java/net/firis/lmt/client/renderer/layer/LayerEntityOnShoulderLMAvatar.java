package net.firis.lmt.client.renderer.layer;

import net.firis.lmt.client.renderer.RendererLMAvatar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.player.EntityPlayer;

public class LayerEntityOnShoulderLMAvatar extends LayerEntityOnShoulder {

	private RendererLMAvatar renderer;
	
	public LayerEntityOnShoulderLMAvatar(RenderManager manager, RendererLMAvatar renderer) {
		
		super(manager);
		
		this.renderer = renderer;
	}
	
	@Override
	public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		EntityPlayer player = entitylivingbaseIn;
		
		GlStateManager.pushMatrix();
		
		//PostRender
		renderer.getLittleMaidMultiModel().bodyPostRender(scale);
		
		//微調整
		if (!player.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		
		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.popMatrix();
	}

}
