package wrapper.mc18;

import java.util.UUID;

import net.minecraft.command.*;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.passive.EntityTameable;
import wrapper.W_ICommon;

import com.mojang.authlib.GameProfile;

public class W_CCommon implements W_ICommon
{
	public void setOwner(EntityTameable entity, String name)
	{
		entity.setOwnerId(name);
	}
	
	public String getOwnerName(IEntityOwnable entity)
	{
		return entity.getOwnerId();
	}
	
	public GameProfile newGameProfile(String UUIDid, String name)
	{
		return new GameProfile(UUID.randomUUID(), name);
	}
	
	public void notifyAdmins(ICommandSender p_152374_0_, ICommand p_152374_1_, int p_152374_2_, String p_152374_3_, Object ... p_152374_4_)
	{
		CommandBase.notifyOperators(p_152374_0_, p_152374_1_, p_152374_2_, p_152374_3_, p_152374_4_);
	}
}
