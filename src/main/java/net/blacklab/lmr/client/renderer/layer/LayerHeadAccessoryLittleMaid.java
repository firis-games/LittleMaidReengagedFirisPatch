package net.blacklab.lmr.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * 頭装飾用のLayer
 * 
 * @author firis-games
 *
 */
public class LayerHeadAccessoryLittleMaid implements LayerRenderer<EntityLivingBase> {

	protected final LMRenderMultiModel<? extends EntityLiving> renderer;

	/**
	 * コンストラクタ
	 * @param renderer
	 */
	public LayerHeadAccessoryLittleMaid(LMRenderMultiModel<? extends EntityLiving> renderer) {
		this.renderer = renderer;
	}

	/**
	 * 描画処理
	 */
	@SuppressWarnings("deprecation")
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		//無効化判定
		if (LMRConfig.cfg_deco_head_accessory == -1) return;
		
		EntityLittleMaid maid = (EntityLittleMaid) entitylivingbaseIn;

		//一番目のアイテムを取得する
		ItemStack stack = maid.maidInventory.getStackInSlot(LMRConfig.cfg_deco_head_accessory);
		if (stack.isEmpty()) return;
		
		//Blockが存在しない場合は無視
		Block renderBlock = Block.getBlockFromItem(stack.getItem());
		if (renderBlock == Blocks.AIR) return;
		
		IBlockState renderState = null;
		boolean isKaburimono = false;
		
		//お花や苗木系
		if (renderBlock instanceof BlockBush) {
			renderState = renderBlock.getStateFromMeta(stack.getMetadata());
		//カボチャ系
		} else if (renderBlock.getRegistryName().toString().equals("minecraft:pumpkin")
				|| renderBlock.getRegistryName().toString().equals("minecraft:lit_pumpkin")) {
			renderState  = renderBlock.getDefaultState();
			isKaburimono = true;
		}
		
		//描画対象が無い場合は何もしない
		if (renderState == null) return;
		
		//描画調整
		GlStateManager.pushMatrix();

		// PostRender
		this.renderer.modelMain.headPostRender(scale);

		// スニーク調整
		if (maid.isSneaking()) {
			GlStateManager.translate(0.0F, -0.2F, 0.0F);
		}

		//描画用設定調整
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		//サイズを調整
		if (!isKaburimono) {
			//通常の植物系ブロック
			GlStateManager.scale(0.52F, 0.52F, 0.52F);
		} else {
			//被り物
			GlStateManager.scale(0.56F, 0.56F, 0.56F);
		}
		
		//位置調整
		GlStateManager.rotate(180, 1.0F, 0.0F, 1.0F);
		GlStateManager.translate(0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, 0.0F, 0.5F);
		
		//位置の調整
		GlStateManager.translate(0.0F, -0.05F, 0.0F);
		if (isKaburimono) {
			//被り物
			GlStateManager.translate(0.0F, -1.0F, 0.0F);
		}

		// 苗木や花系のクロスブロックの場合にテクスチャ両面描画に設定しないとテクスチャがちらつく
		// テクスチャ両面描画
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		// テクスチャバインド
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		// ブロック描画
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(renderState, 1.0F);
		
		//テクスチャの両面描画解除
		GL11.glDisable(GL11.GL_CULL_FACE);

		GlStateManager.popMatrix();
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}