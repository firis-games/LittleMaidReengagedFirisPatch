package net.blacklab.lmr.client.renderer.layer;

import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import firis.lmlib.api.client.renderer.layer.LMLayerHeldItemBase;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

/**
 * メイドさんの手持ちLayer
 * @author firis-games
 *
 */
public class LayerHeldItemLittleMaid extends LMLayerHeldItemBase {
	
	/**
	 * コンストラクタ
	 * @param rendererIn
	 */
	public LayerHeldItemLittleMaid(LMRenderMultiModel<? extends EntityLiving> rendererIn) {
		
		super(rendererIn, rendererIn.modelMain);
		
	}

	/**
	 * 右手のアイテムを取得する
	 */
	@Override
	protected ItemStack getRightHandItemStack(EntityLivingBase entitylivingbaseIn) {
		boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
		return flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
	}

	/**
	 * 左手のアイテムを取得する
	 */
	@Override
	protected ItemStack getLeftHandItemStack(EntityLivingBase entitylivingbaseIn) {
		boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
		return flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
	}
	
	/**
	 * 手持ちアイテムの描画判定
	 */
	@Override
	protected boolean isRenderHeldItem(EntityLivingBase entitylivingbaseIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformType) {
		EntityLittleMaid maid = (EntityLittleMaid) entitylivingbaseIn;
    	return !maid.isMaidWait();
    }
}