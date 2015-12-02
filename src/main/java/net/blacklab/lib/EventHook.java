package net.blacklab.lib;

import net.blacklab.lib.version.Version;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHook {

	public class RunThread extends Thread{
		public PlayerEvent.PlayerLoggedInEvent e;
		
		public RunThread(PlayerEvent.PlayerLoggedInEvent ev){
			e = ev;
		}
		
		public void run(){
			Version.VersionData v = Version.getLatestVersion("http://mc.el-blacklab.net/eblibversion.txt");
			if(EBLib.VERSION_CODE < v.code){
				//バージョンが古い
				// TODO これメイドのAvatarキャッチしない？
				try{
					//別スレッドから使えるんかい
					e.player.addChatMessage(new ChatComponentText("[EBLib]New Version Avaliable : "+v.name));
					e.player.addChatMessage(new ChatComponentText("[EBLib]Go to : http://el-blacklab.net/"));
				}catch(Exception e){}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event){
		new RunThread(event).run();
	}
}
