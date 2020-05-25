package net.blacklab.lmr.client.renderer.layer;

import firis.lmlib.client.renderer.RenderModelMulti;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;

/**
 * メイドさんの手持ちLayer
 * @author firis-games
 *
 */
public class LayerHeldChestLittleMaid extends LayerHeldItem {
	
	protected final RenderModelMulti<? extends EntityLiving> renderer;
	
	/**
	 * コンストラクタ
	 * @param rendererIn
	 */
	public LayerHeldChestLittleMaid(RenderModelMulti<? extends EntityLiving> rendererIn) {
		
		super(rendererIn);
		
		this.renderer = rendererIn;
		
	}

	/**
	 * プレイヤーメイドモデルの手に持ったアイテムを描画する
	 */
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		GlStateManager.pushMatrix();
	    
		//チェストの描画
	    this.renderHeldBlock(entitylivingbaseIn, Blocks.CHEST.getDefaultState());

	    GlStateManager.popMatrix();
        
    }

	/**
	 * 手に持ったブロックを描画する
	 * @param entitylivingbaseIn
	 * @param stackIn
	 * @param transformType
	 * @param handSide
	 */
    private void renderHeldBlock(EntityLivingBase entitylivingbaseIn, IBlockState state)
    {
    	EntityLittleMaid maid = (EntityLittleMaid) entitylivingbaseIn;
    	IBlockState iblockstate = Blocks.CHEST.getDefaultState();
    	
    	String job = maid.jobController.getMaidModeString();
    	boolean idDisplay = false;
    	if (EntityMode_Basic.mmode_FarmPorter.equals(job) 
    			|| EntityMode_Basic.mmode_LumberjackPorter.equals(job)
    			|| EntityMode_Basic.mmode_RipperPorter.equals(job)
    			|| EntityMode_Basic.mmode_SugarCanePorter.equals(job)
    			|| EntityMode_Basic.mmode_AnglerPorter.equals(job)) {
    		idDisplay = true;
    	}
    	
        if (!maid.isMaidWait() && idDisplay)
        {
            GlStateManager.pushMatrix();
            
            //手の位置へアイテム描画位置を調整する
            this.translateToBody();
            
            //スニーク調整
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(1.0F, 0.0f, 0.02f);
            }
            
            //位置とサイズを調整
            float f = 0.5F;
            GlStateManager.scale(-f, -f, f);
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.5F, -0.8F, 0.5F);

            //ブロックの描画
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
            
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * 胴の位置へ調整する
     */
    protected void translateToBody()
    {
    	float scale = 0.0625F;
    	this.renderer.modelMain.bodyPostRender(scale);
    }
}