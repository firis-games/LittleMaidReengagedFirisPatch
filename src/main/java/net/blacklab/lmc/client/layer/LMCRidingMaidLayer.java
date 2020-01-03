package net.blacklab.lmc.client.layer;

import net.blacklab.lmc.common.helper.LittleMaidHelper;
import net.blacklab.lmc.common.item.LMItemMaidCarry;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * メイド騎乗用
 * @author computer
 *
 */
public final class LMCRidingMaidLayer implements LayerRenderer<EntityPlayer> {
	
	protected EntityLittleMaid maid = null;
	
	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		if (!this.isRender(player)) {
			maid = null;
			return;
		}
		
		//リトルメイド取得
		if (maid == null) {
			ItemStack stack = player.inventory.getStackInSlot(9);
			Entity entity = LittleMaidHelper.spawnEntityFromItemStack(stack, player.getEntityWorld(), 0, 0, 0);
			if (entity != null && entity instanceof EntityLittleMaid) {
				maid = (EntityLittleMaid) entity;
				
				maid.setMaidWait(false);
				maid.setClientRidingRender(true);
			}
		}
		
		if (maid == null) return;
		
		GlStateManager.pushMatrix();
		
		//ブロックを描画
		//========================================
		//スニーク位置調整
		rotateSneaking(player);
		
		//GlStateManager.scale(0.75F, 0.75F, 0.75F);
		//GlStateManager.rotate(90, 0, 1, 0);
		
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.translate(0, -0.5F, -0.35F);
		
		
		//GlStateManager.translate(-1F, -0.75F, 0.5F);
		
		float f = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);
		Minecraft.getMinecraft().getRenderManager().renderEntity(maid, 0.0D, 0.0D, 0.0D, f, 0.0F, true);
		//========================================
		
		GlStateManager.popMatrix();
	}
	
	/**
	 * 描画するかの判断を行う
	 * @param player
	 * @return
	 */
	private boolean isRender(EntityPlayer player) {
		
		boolean ret = false;
		
		//左上のスロットをチェックする
		ItemStack stack = player.inventory.getStackInSlot(9);
		if (stack.getItem() instanceof LMItemMaidCarry
				&& stack.hasTagCompound()) {
			ret = true;
		}
		
		return ret;
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
	/**
	 * スニーク時の角度調整
	 * @param player
	 */
	private void rotateSneaking(EntityPlayer player) {
		
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
			GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}
	}

}
