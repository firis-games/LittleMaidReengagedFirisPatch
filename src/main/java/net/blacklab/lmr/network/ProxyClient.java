package net.blacklab.lmr.network;

import net.blacklab.lmc.client.event.RenderPlayerEventHandler;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.LittleMaidReengaged.LMItems;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.gui.GuiIFF;
import net.blacklab.lmr.client.gui.inventory.GuiMaidInventory;
import net.blacklab.lmr.client.sound.SoundLoader;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityMarkerDummy;
import net.blacklab.lmr.entity.renderfactory.RenderFactoryLittleMaid;
import net.blacklab.lmr.entity.renderfactory.RenderFactoryMarkerDummy;
import net.blacklab.lmr.entity.renderfactory.RenderFactoryModelSelect;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.firis.lmt.common.LMTCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * クライアント専用処理。
 * マルチ用に分離。
 * 分離しとかないとNoSuchMethodで落ちる。
 */
public class ProxyClient extends ProxyCommon
{
	public void rendererRegister() {
		
		ModelLoader.setCustomModelResourceLocation(
				LMItems.SPAWN_LITTLEMAID_EGG, 0, new ModelResourceLocation(
						LittleMaidReengaged.DOMAIN+":spawn_littlemaid_egg", "inventory"));
		ModelLoader.setCustomModelResourceLocation(LMItems.REGISTERKEY, 0,
				new ModelResourceLocation(LittleMaidReengaged.DOMAIN+":registerkey",
						"inventory"));
		ModelLoader.setCustomModelResourceLocation(LMItems.REGISTERKEY, 1,
				new ModelResourceLocation(LittleMaidReengaged.DOMAIN+":registerkey",
						"inventory"));
		String porter_modelName_A = LittleMaidReengaged.DOMAIN + ":maidporter_0";
		String porter_modelName_B = LittleMaidReengaged.DOMAIN + ":maidporter_1";
//		ModelLoader.addVariantName(LittleMaidReengaged.maidPorter, LittleMaidReengaged.DOMAIN + ":maidporter_0", LittleMaidReengaged.DOMAIN + ":maidporter_1");
		ModelBakery.registerItemVariants(LMItems.MAIDPORTER, new ResourceLocation(porter_modelName_A), new ResourceLocation(porter_modelName_B));
		ModelLoader.setCustomModelResourceLocation(LMItems.MAIDPORTER, 0, new ModelResourceLocation(porter_modelName_A, "inventory"));
		ModelLoader.setCustomModelResourceLocation(LMItems.MAIDPORTER, 1, new ModelResourceLocation(porter_modelName_B, "inventory"));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaid.class, new RenderFactoryLittleMaid());
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaidForTexSelect.class, new RenderFactoryModelSelect());
		RenderingRegistry.registerEntityRenderingHandler(EntityMarkerDummy.class, new RenderFactoryMarkerDummy());
		
		//リトルメイドテスト用モジュール
		LMTCore.rendererRegister();
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
		CommonHelper.mc.effectRenderer.addEffect(new ParticleItemPickup(CommonHelper.mc.world, entity, pAvatar, 0.1F));
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
		return Minecraft.getMinecraft().player;
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

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		Object o = null;
		switch(ID) {
			case GuiHandler.GUI_ID_INVVENTORY:
				if(GuiHandler.maidClient!=null)
				{
					o = new GuiMaidInventory(player, GuiHandler.maidClient);
					GuiHandler.maidClient = null;
				}
				break;

			case GuiHandler.GUI_ID_IFF:
				o = new GuiIFF(world, player, GuiHandler.maidClient);
				break;
		}
		return o;
	}

	@Override
	public void onClientCustomPayLoad(LMRMessage pPayload) {
		LMRMessage.EnumPacketMode lmode = pPayload.getMode();
		if (lmode == null) return;

		LittleMaidReengaged.Debug("MODE: %s", lmode.toString());

		Entity lemaid = null;
		if (lmode.withEntity) {
			lemaid = Minecraft.getMinecraft().world.getEntityByID(pPayload.getEntityId());
			if (!(lemaid instanceof EntityLittleMaid)) return;

			LMRNetwork.syncPayLoad(lmode, (EntityLittleMaid)lemaid, pPayload.getTag());
		}
		clientPayLoad(lmode, (EntityLittleMaid) lemaid, pPayload.getTag());
	}

	private static void clientPayLoad(LMRMessage.EnumPacketMode pMode, EntityLittleMaid lemaid, NBTTagCompound tagCompound) {
		switch (pMode) {
		case CLIENT_SWINGARM :
			// 腕振り
			byte larm = tagCompound.getByte("Arm");
			EnumSound lsound = EnumSound.getEnumSound(tagCompound.getInteger("Sound"));
			lemaid.setSwinging(larm, lsound, tagCompound.getBoolean("Force"));
			break;

		case CLIENT_RESPOND_IFF :
			// IFFの設定値を受信
			byte lval = tagCompound.getByte("Value");
			String lname = tagCompound.getString("Name");

			LittleMaidReengaged.Debug("setIFF-CL %s=%d", lname, lval);
			IFF.setIFFValue(Minecraft.getMinecraft().player.getUniqueID(), lname, lval);
			break;

		case CLIENT_PLAY_SOUND :
			// 音声再生
			EnumSound sound = EnumSound.getEnumSound(tagCompound.getInteger("Sound"));
			//lemaid.playSound(sound, tagCompound.getBoolean("Force"));
			lemaid.playLittleMaidVoiceSound(sound, false);
			LittleMaidReengaged.Debug(String.format("playSound:%s", sound.name()));
			break;

		case CLIENT_ONDEATH :
			lemaid.manualOnDeath();
			break;

		case CLIENT_CURRENT_ITEM:
			lemaid.maidInventory.currentItem = tagCompound.getInteger("Index");
			lemaid.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(tagCompound.getCompoundTag("Stack")));
			
		case SERVER_LITTLE_MAID_INVENTORY:
			//インベントリの同期処理
			lemaid.maidInventory.readFromNBT(tagCompound.getTagList("Inventory", 10));

		default:
			break;
		}
	}

	public int soundCount = 0;

	/*
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
	*/
	
	
	@Override
	public void spawnParticle(BlockPos pos, int spawnType) {
		World world = Minecraft.getMinecraft().world;
		
		//爆発エフェクト
		if (world.isRemote) {
			for (int k = 0; k < 10; ++k) {
                double d2 = world.rand.nextGaussian() * 0.02D;
                double d0 = world.rand.nextGaussian() * 0.02D;
                double d1 = world.rand.nextGaussian() * 0.02D;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 
                		(pos.getX() - 1.0) + world.rand.nextFloat() * 2, 
                		pos.getY() + world.rand.nextFloat(), 
                		(pos.getZ() - 1.0) + world.rand.nextFloat() * 2,
                		d2, d0, d1);
            }
		}
	}
	
	/**
	 * 描画イベントの登録
	 */
	@Override
	public void initClientRendererEventRegister() {
		
		//擬似騎乗描画用
		MinecraftForge.EVENT_BUS.register(new RenderPlayerEventHandler());
		
		//リトルメイドテスト用モジュール
		LMTCore.initClientRendererEventRegister();
		
	}
}
