package net.blacklab.lmr.proxy;

import static net.blacklab.lmr.util.Statics.LMN_Client_PlaySound;
import static net.blacklab.lmr.util.Statics.LMN_Client_SetIFFValue;
import static net.blacklab.lmr.util.Statics.LMN_Client_SwingArm;

import mmmlibx.lib.MMM_EntityDummy;
import mmmlibx.lib.MMM_EntitySelect;
import mmmlibx.lib.MMM_Helper;
import mmmlibx.lib.MMM_RenderDummy;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.renderer.RenderLittleMaid;
import net.blacklab.lmr.client.renderer.entity.LMMNX_RenderEntitySelect;
import net.blacklab.lmr.client.sound.LMMNX_SoundLoader;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.network.LMMNX_NetSync;
import net.blacklab.lmr.network.LMRMessage;
import net.blacklab.lmr.network.NetworkSync;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.IFF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.MCPDummyContainer;

/**
 * クライアント専用処理。
 * マルチ用に分離。
 * 分離しとかないとNoSuchMethodで落ちる。
 */
public class ProxyClient extends ProxyCommon
{
	public void init() {
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaid.class,new RenderLittleMaid(Minecraft.getMinecraft().getRenderManager(),0.3F));
		RenderingRegistry.registerEntityRenderingHandler(MMM_EntitySelect.class,	new LMMNX_RenderEntitySelect(Minecraft.getMinecraft().getRenderManager(), 0.0F));
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
		MMM_Helper.mc.effectRenderer.addEffect(new EntityPickupFX(MMM_Helper.mc.theWorld, entity, pAvatar, 0.1F));
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

	
// Network

	@SuppressWarnings("null")
	public void clientCustomPayload(LMRMessage pPayload) {
		// クライアント側の特殊パケット受信動作
		byte lmode = pPayload.data[0];
		int leid = 0;
		EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(pPayload.data, 1);
			lemaid =NetworkSync.getLittleMaid(pPayload.data, 1, MMM_Helper.mc.theWorld);
			if (lemaid == null) return;
		}
		LittleMaidReengaged.Debug(String.format("LMM|Upd Clt Call[%2x:%d].", lmode, leid));
		
		switch (lmode) {
		case LMN_Client_SwingArm : 
			// 腕振り
			byte larm = pPayload.data[5];
			EnumSound lsound = EnumSound.getEnumSound(MMM_Helper.getInt(pPayload.data, 6));
			lemaid.setSwinging(larm, lsound, MMM_Helper.getInt(pPayload.data, 10)==1);
//			mod_LMM_littleMaidMob.Debug(String.format("SwingSound:%s", lsound.name()));
			break;
			
		case LMN_Client_SetIFFValue :
			// IFFの設定値を受信
			int lval = pPayload.data[1];
			int lindex = MMM_Helper.getInt(pPayload.data, 2);
			String lname = (String)IFF.DefaultIFF.keySet().toArray()[lindex];
			LittleMaidReengaged.Debug("setIFF-CL %s(%d)=%d", lname, lindex, lval);
			IFF.setIFFValue(null, lname, lval);
			break;
			
		case LMN_Client_PlaySound : 
			// 音声再生
			EnumSound lsound9 = EnumSound.getEnumSound(MMM_Helper.getInt(pPayload.data, 5));
			LittleMaidReengaged.Debug(String.format("playSound:%s", lsound9.name()));
			lemaid.playSound(lsound9, true);
			break;
		case LMMNX_NetSync.LMMNX_Sync:
			LMMNX_NetSync.onPayLoad(lemaid, pPayload.data);
			break;
		}
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
		LMMNX_SoundLoader.load();
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
