package firis.lmavatar.client.renderer.layer;

import firis.lmavatar.client.renderer.RendererLMAvatar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * メイドさん用のエリトラ描画
 * @author firis-games
 *
 */
public class LayerElytraLMAvatar extends LayerElytra {

	private RendererLMAvatar renderer;
	
	public LayerElytraLMAvatar(RendererLMAvatar renderer) {
		super(renderer);
		
		this.renderer = renderer;
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		
		GlStateManager.pushMatrix();
		
		//メイドさん用に位置とサイズ調整する
		float eltrascale = 0.625F;
		GlStateManager.scale(eltrascale, eltrascale, eltrascale);
		
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
