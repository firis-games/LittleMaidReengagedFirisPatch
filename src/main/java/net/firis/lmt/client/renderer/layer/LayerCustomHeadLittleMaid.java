package net.firis.lmt.client.renderer.layer;

import net.firis.lmt.client.model.ModelLittleMaidMultiModel.EnumMultiModelPartsType;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * かぼちゃ頭用
 * net.minecraft.client.renderer.entity.layers.LayerCustomHeadをベースに作成する
 * @author firis-games
 *
 */
public class LayerCustomHeadLittleMaid extends LayerCustomHead
{
    private final RendererMaidPlayerMultiModel renderer;
    
    //無名クラス定義
    private static ModelRenderer dummyModelRenderer = new ModelRenderer(new ModelBase() {});

    public LayerCustomHeadLittleMaid(RendererMaidPlayerMultiModel renderer)
    {
    	super(dummyModelRenderer);
        this.renderer = renderer;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		
		GlStateManager.pushMatrix();
		
		//PostRender
		renderer.getLittleMaidMultiModel().modelPostRender(
				EnumMultiModelPartsType.HEAD, 
				player, scale);
		
		//微調整
		if (player.isSneaking()) {
			GlStateManager.translate(0.0F, -0.2F, 0.0F);
		}
		
		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.popMatrix();
    }

}