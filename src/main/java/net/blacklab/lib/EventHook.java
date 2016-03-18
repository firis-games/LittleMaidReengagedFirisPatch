package net.blacklab.lib;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHook {

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent e){
		if(EBLib.currentVersion.compareVersion(EBLib.latestVersion) > 0){
			e.player.addChatMessage(new TextComponentString(
					"[EBLib]New Version Avaliable : "+EBLib.latestVersion.name));
			e.player.addChatMessage(new TextComponentString("[EBLib]Go to : http://el-blacklab.net/"));
		}
	}
}
