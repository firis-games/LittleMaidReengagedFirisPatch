package net.firis.lmt.client.renderer.layer;

import net.firis.lmt.client.model.ModelLittleMaidMultiModel.EnumMultiModelPartsType;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * メイドさん用のエリトラ描画
 * @author firis-games
 *
 */
public class LayerElytraLittleMaid extends LayerElytra {

	private RendererMaidPlayerMultiModel renderer;
	
	public LayerElytraLittleMaid(RenderLivingBase<?> renderer) {
		super(renderer);
		
		this.renderer = (RendererMaidPlayerMultiModel) renderer;
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		
		GlStateManager.pushMatrix();
		
		//メイドさん用に位置とサイズ調整する
		float eltrascale = 0.625F;
		GlStateManager.scale(eltrascale, eltrascale, eltrascale);
		
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
