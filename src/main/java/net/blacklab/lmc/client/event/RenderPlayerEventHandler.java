package net.blacklab.lmc.client.event;

import net.blacklab.lmc.common.helper.LittleMaidHelper;
import net.blacklab.lmc.common.item.LMItemMaidCarry;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderPlayerEventHandler {
	
	/**
	 * プレイヤー描画
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderPlayerEventPost(RenderPlayerEvent.Post event) {
		
		if (event.getEntityPlayer() == null) return;
		
		this.doRender(event.getEntityPlayer(), event.getPartialRenderTick());
		
	}

	protected EntityLittleMaid maid = null;
	
	//左上スロット
	protected static final int cfgPlayerSlotIndex = 9;
	
	public void doRender(EntityPlayer player, float partialticks) {
		
		if (!this.isRender(player) || player.isRiding()) {
			maid = null;
			return;
		}
		
		//リトルメイド取得
		if (maid == null) {
			ItemStack stack = player.inventory.getStackInSlot(cfgPlayerSlotIndex);
			Entity entity = LittleMaidHelper.spawnEntityFromItemStack(stack, player.getEntityWorld(), 0, 0, 0);
			if (entity != null && entity instanceof EntityLittleMaid) {
				maid = (EntityLittleMaid) entity;
				
				//擬似騎乗状態を設定
				maid.setMaidWait(false);
				maid.setClientRidingRender(true);
				maid.setClinetRidingEntity(player);
			}
		}
		
		if (maid == null) return;
		
		//アニメーション（目パチ用）
		maid.ticksExisted = player.ticksExisted;
		
		GlStateManager.pushMatrix();
		
		//描画処理
		//========================================
		//方向をプレイヤーに合わせる
		float rotation = 0.0F;
		rotation = -(player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialticks);
		GlStateManager.rotate(rotation, 0.0F, 1.0f, 0.0F);
		
		//メイドさん位置調整
		GlStateManager.translate(0, 0.9F, -0.35F);
		
		
		//微調整 PFLMの標準設定は0.5F
		//GlStateManager.translate(0, -0.5F, 0);
		float heightAdjustment = LMRConfig.cfg_custom_riding_height_adjustment;
		if (LMRConfig.cfg_lmabatar_maid_avatar) heightAdjustment -= 0.5F;
		GlStateManager.translate(0, heightAdjustment, 0);
		
		
		//スニーク位置調整
		rotateSneaking(player);
		
		//メイドさん描画
		float f = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);
		Minecraft.getMinecraft().getRenderManager().renderEntity(maid, 0.0D, 0.0D, 0.0D, f, 0.0F, true);
		Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);
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
		
		//メイドキャリーのチェック
		ItemStack stack = player.inventory.getStackInSlot(cfgPlayerSlotIndex);
		if (stack.getItem() instanceof LMItemMaidCarry
				&& stack.hasTagCompound()) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * スニーク時の角度調整
	 * @param player
	 */
	private void rotateSneaking(EntityPlayer player) {
		
		if (player.isSneaking()) {
			GlStateManager.translate(0F, -0.15F, -0.2F);
			GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}
	}
	
}
