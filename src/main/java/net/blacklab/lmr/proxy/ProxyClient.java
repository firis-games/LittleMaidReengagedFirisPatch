package net.blacklab.lmr.proxy;

import mmmlibx.lib.MMM_EntityDummy;
import mmmlibx.lib.MMM_RenderDummy;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.entity.LMMNX_RenderEntitySelect;
import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.client.sound.SoundLoader;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * クライアント専用処理。
 * マルチ用に分離。
 * 分離しとかないとNoSuchMethodで落ちる。
 */
public class ProxyClient extends ProxyCommon
{
	public void init() {
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaid.class,new RenderLittleMaid(Minecraft.getMinecraft().getRenderManager(),0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaidForTexSelect.class,	new LMMNX_RenderEntitySelect(Minecraft.getMinecraft().getRenderManager(), 0.0F));
		RenderingRegistry.registerEntityRenderingHandler(MMM_EntityDummy.class,		new MMM_RenderDummy());
	}

	/* 呼び出し箇所なし
	public GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2,
			int var3, int var4, int var5) {
		Entity lentity = var1.worldObj.getEntityByID(var3);
		if (lentity instanceof LMM_EntityLittleMaid) {
			LMM_GuiInventory lgui = new LMM_GuiInventory(var1, (LMM_EntityLittleMaid)lentity);
//			var1.openContainer = lgui.inventorySlots;
			return lgui;
		} else {
			return null;
		}
	}
	*/

// Avatarr
	
	public void onItemPickup(EntityPlayer pAvatar, Entity entity, int i) {
		// アイテム回収のエフェクト
		// TODO:こっちを使うか？
//		mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, avatar, -0.5F));
		CommonHelper.mc.effectRenderer.addEffect(new EntityPickupFX(CommonHelper.mc.theWorld, entity, pAvatar, 0.1F));
	}

	// TODO いらん？
	public void onCriticalHit(EntityPlayer pAvatar, Entity par1Entity) {
		//1.8後回し
//		MMM_Helper.mc.effectRenderer.addEffect(new EntityCrit2FX(MMM_Helper.mc.theWorld, par1Entity));
	}

	public void onEnchantmentCritical(EntityPlayer pAvatar, Entity par1Entity) {
		//1.8後回し
//		EntityCrit2FX entitycrit2fx = new EntityCrit2FX(MMM_Helper.mc.theWorld, par1Entity, "magicCrit");
//		MMM_Helper.mc.effectRenderer.addEffect(entitycrit2fx);
	}

	
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	/* 呼び出し箇所なし
	public static void setAchievement() {
// MinecraftクラスからstatFileWriterが消えてる
//		MMM_Helper.mc.statFileWriter.readStat(mod_LMM_littleMaidMob.ac_Contract, 1);
	}
	*/

	public void loadSounds()
	{
		// 音声の解析
//		LMM_SoundManager.instance.init();
		// サウンドパック
//		LMM_SoundManager.instance.loadDefaultSoundPack();
		SoundLoader.load();
	}

	public boolean isSinglePlayer()
	{
		return Minecraft.getMinecraft().isSingleplayer();
	}
	
	@Override
	public void runCountThread(){
	}
	
	@Override
	public void playLittleMaidSound(World par1World, double x, double y, double z, String s, float v, float p, boolean b) {
/*
		if(!par1World.isRemote) return;
//		if(LMM_LittleMaidMobNX.proxy.OFFSET_COUNT==0){
//			LMM_LittleMaidMobNX.proxy.OFFSET_COUNT=2;
			if(soundCount<=1){
				soundCount++;
				try{
					par1World.playSound(x, y, z, s, v, p, b);
				}catch(Exception exception){
					Minecraft.getMinecraft().getSoundHandler().update();
				}
			}
//		}
*/
	}
	
	public int soundCount = 0;
	
	public static class CountThread extends Thread{

		public boolean isRunning = true;

		@Override
		public void run() {
			while(isRunning){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				if(((ProxyClient)LittleMaidReengaged.proxy).soundCount>0){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					((ProxyClient)LittleMaidReengaged.proxy).soundCount--;
				}
			}
		}
		
		public void cancel(){
			isRunning = false;
		}
	}
}
