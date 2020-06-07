package firis.lmavatar.client.renderer.layer;

import firis.lmavatar.common.modelcaps.PlayerModelCaps;
import firis.lmlib.api.client.model.LMModelLittleMaid;
import firis.lmlib.api.client.renderer.layer.LMLayerHeldItemBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

/**
 * LMAvatarの手持ちアイテムLayer
 * @author firis-games
 *
 */
public class LayerHeldItemLMAvatar extends LMLayerHeldItemBase {

	/**
	 * コンストラクタ
	 * @param rendererIn
	 * @param rendererModel
	 */
	public LayerHeldItemLMAvatar(RenderLivingBase<?> rendererIn, LMModelLittleMaid rendererModel) {
		super(rendererIn, rendererModel);
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
	 * 盾持ち時の位置調整
	 */
	@Override
	protected void translateAdjustment(EntityLivingBase entitylivingbaseIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide) {
		
		boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
		
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
	}
}
