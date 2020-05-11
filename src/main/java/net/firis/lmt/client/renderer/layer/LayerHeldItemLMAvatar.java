package net.firis.lmt.client.renderer.layer;

import net.firis.lmt.client.renderer.RendererLMAvatar;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class LayerHeldItemLMAvatar extends LayerHeldItem {

	private RendererLMAvatar renderer;
	
	public LayerHeldItemLMAvatar(RendererLMAvatar rendererIn) {
		super(rendererIn);
		
		this.renderer = rendererIn;
		
	}

	/**
	 * プレイヤーメイドモデルの手に持ったアイテムを描画する
	 */
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		//利き腕を確認し右手と左手のアイテムをそれぞれ描画する
		boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
        {
            GlStateManager.pushMatrix();
            //右手アイテム描画
            this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            //左手アイテム描画
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
        
    }

	/**
	 * 手に持ったアイテムを描画する
	 * @param entitylivingbaseIn
	 * @param stackIn
	 * @param transformType
	 * @param handSide
	 */
    private void renderHeldItem(EntityLivingBase entitylivingbaseIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide)
    {
        if (!stackIn.isEmpty())
        {
            GlStateManager.pushMatrix();
            
            //手の位置へアイテム描画位置を調整する
            this.translateToHand(handSide);
            
            //向き調整
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            
            //手の位置の微調整
            boolean flag = handSide == EnumHandSide.LEFT;
            /* 初期モデル構成で
			 * x: 手の甲に垂直な方向(-で向かって右に移動)
			 * y: 体の面に垂直な方向(-で向かって背面方向に移動)
			 * z: 腕に平行な方向(-で向かって手の先方向に移動)
			 */
            //GlStateManager.translate((flag ? -1.0F : 1.0F) * 0.05F, 0.06f, -0.5f);
            GlStateManager.translate(flag ? -0.0125F : 0.0125F, 0.05f, -0.15f);
            
            //Block系アイテムの位置調整
            if (EnumAction.BLOCK == PlayerModelCaps.getPlayerAction((EntityPlayer) entitylivingbaseIn, handSide)) {
            	if (!flag) {
            		//右手調整
            		GlStateManager.rotate(55, 0.0F, 1.0F, 0.0F);
                	GlStateManager.translate(-0.15F, 0.2F, 0.0F);
            	} else {
            		//左手調整
            		GlStateManager.rotate(55, 0.0F, -1.0F, 0.0F);
                	GlStateManager.translate(0.04F, 0.2F, 0.0F);
            	}
            }
            
            //スニーク調整
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate((flag ? -1.0F : 1.0F) * 0F, 0.0f, 0.02f);
            }
            
            //アイテム描画
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entitylivingbaseIn, stackIn, transformType, flag);
            
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * 腕の位置へ調整する
     */
    @Override
    protected void translateToHand(EnumHandSide handSide)
    {
    	float scale = 0.0625F;
    	int hand = EnumHandSide.RIGHT == handSide ? 0 : 1;
    	this.renderer.getLittleMaidMultiModel().armPostRender(hand, scale);
    }
}
