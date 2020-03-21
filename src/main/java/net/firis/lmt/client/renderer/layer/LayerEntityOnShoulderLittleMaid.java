package net.firis.lmt.client.renderer.layer;

import net.firis.lmt.client.model.ModelLittleMaidMultiModel.EnumMultiModelPartsType;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.player.EntityPlayer;

public class LayerEntityOnShoulderLittleMaid extends LayerEntityOnShoulder {

	private RendererMaidPlayerMultiModel renderer;
	
	public LayerEntityOnShoulderLittleMaid(RenderManager manager, RendererMaidPlayerMultiModel renderer) {
		
		super(manager);
		
		this.renderer = (RendererMaidPlayerMultiModel) renderer;
	}
	
	@Override
	public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		EntityPlayer player = entitylivingbaseIn;
		
		GlStateManager.pushMatrix();
		
		//PostRender
		renderer.getLittleMaidMultiModel().modelPostRender(
				EnumMultiModelPartsType.BODY, 
				player, scale);
		
		//微調整
		if (!player.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		
		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.popMatrix();
	}

}
