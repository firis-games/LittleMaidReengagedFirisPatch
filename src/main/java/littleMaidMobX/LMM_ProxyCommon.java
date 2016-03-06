package littleMaidMobX;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import network.W_Message;
public class LMM_ProxyCommon
{
	public int OFFSET_COUNT = 0;
	public void init() {}
	public void onItemPickup(EntityPlayer lmm_EntityLittleMaidAvatar,Entity entity, int i) {}
	public void onCriticalHit(EntityPlayer pAvatar, Entity par1Entity) {}
	public void onEnchantmentCritical(EntityPlayer pAvatar, Entity par1Entity) {}
	public void clientCustomPayload(W_Message var2) {}
	public EntityPlayer getClientPlayer(){ return null; }
	public void loadSounds(){}
	public void runCountThread() {}
	
	public boolean isSinglePlayer()
	{
		return MinecraftServer.getServer().isSinglePlayer();
	}
	public void playLittleMaidSound(World worldObj, double posX, double posY,
			double posZ, String s, float soundVolume, float lpitch, boolean b) {
		
	}
}
