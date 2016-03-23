package net.blacklab.lmr.wrapper;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.passive.EntityTameable;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

public interface W_ICommon
{
	public void		setOwner(EntityTameable entity, UUID uuid);
	public UUID		getOwnerUUID(IEntityOwnable entity);
	public GameProfile newGameProfile(String UUIDid, String name);
	public void		notifyAdmins(ICommandSender sender, ICommand cmd, int p_152374_2_, String s, Object ... p_152374_4_);
}
