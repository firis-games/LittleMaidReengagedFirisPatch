package net.blacklab.lmr.client.renderer.layer;

import java.util.Iterator;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * 手持ちアイテムレイヤー
 * RenderLittleMaidから分離
 */
public class MMMLayerHeldItem extends LayerHeldItem {

	//レイヤーと化したアイテム描画
	protected final RenderLittleMaid renderer;
	
	/**
	 * コンストラクタ
	 * @param rendererIn
	 */
	public MMMLayerHeldItem(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
		renderer = (RenderLittleMaid) rendererIn;
	}

	/**
	 * アイテムを描画する
	 */
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
	
		EntityLittleMaid lmm = (EntityLittleMaid) entitylivingbaseIn;

		if(!lmm.isMaidWait()){
			
			Iterator<ItemStack> heldItemIterator = lmm.getHeldEquipment().iterator();
			int i = 0, handindexes[] = {lmm.getDominantArm(), lmm.getDominantArm() == 1 ? 0 : 1};

			while (heldItemIterator.hasNext()) {
				ItemStack itemstack = (ItemStack) heldItemIterator.next();

				if (!itemstack.isEmpty())
				{
					GlStateManager.pushMatrix();

					// Use dominant arm as mainhand.
					this.renderer.modelMain.model.Arms[handindexes[i]].postRender(0.0625F);

					if (lmm.isSneaking()) {
						GlStateManager.translate(0.0F, 0.2F, 0.0F);
					}
					boolean flag = handindexes[i] == 1;

					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
					/* 初期モデル構成で
					 * x: 手の甲に垂直な方向(-で向かって右に移動)
					 * y: 体の面に垂直な方向(-で向かって背面方向に移動)
					 * z: 腕に平行な方向(-で向かって手の先方向に移動)
					 */
					GlStateManager.translate(flag ? -0.0125F : 0.0125F, 0.05f, -0.15f);
					Minecraft.getMinecraft().getItemRenderer().renderItemSide(lmm, itemstack,
							flag ?
									ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND :
									ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
							flag);
					GlStateManager.popMatrix();
				}

				i++;
			}
		}	
	}
}