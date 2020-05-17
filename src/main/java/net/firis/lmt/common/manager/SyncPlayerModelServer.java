package net.firis.lmt.common.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.blacklab.lmr.network.LMRMessage.EnumPacketMode;
import net.blacklab.lmr.network.LMRNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * サーバー側のパケット送信処理
 * 
 * キューにUUIDを貯めて一定時間ごとにパケットをまとめて投げる
 * 
 * @author firis-games
 *
 */
public class SyncPlayerModelServer {

	private final static Set<String> syncPacketQueue = new HashSet<>();
	
	/**
	 * モデルの同期を行う
	 */
	public static void syncModel(String key) {
		syncPacketQueue.add(key);
	}
	
	/**
	 * サーバーサイドのtick処理
	 * @param event
	 */
	@SubscribeEvent
	public void onWorldTickEvent(WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END){
			onWorldTickEventPost(event);
		}
	}
	
	/**
	 * Tickの最後で処理を行う
	 * @param event
	 */
	protected void onWorldTickEventPost(WorldTickEvent event) {
		
		Iterator<String> syncPacketIterator = syncPacketQueue.iterator();
		
		NBTTagList tagList = new NBTTagList();
		
		while (syncPacketIterator.hasNext()) {
			//NBT取得
			NBTTagCompound tagCompound = getAvatarModelNbt(syncPacketIterator.next());
			if (tagCompound != null) {
				tagList.appendTag(tagCompound);				
			}
			//削除
			syncPacketIterator.remove();
		}
		
		//条件に一致すればパケット送信
		sendPacketToClient(tagList);

	}
	
	/**
	 * LMアバターのNBTを取得する
	 * 対象が無い場合はnull
	 * @param tagCompound
	 * @return
	 */
	protected NBTTagCompound getAvatarModelNbt(String key) {
		if (!PlayerModelManager.serverModelNbtMap.containsKey(key)) {
			return null;
		}
		NBTTagCompound tagCompound = PlayerModelManager.serverModelNbtMap.get(key);
		return tagCompound;
	}
	
	/**
	 * クライアントへパケットを送信する
	 * @param uuid
	 */
	protected void sendPacketToClient(NBTTagList tagList) {
		
		//送信情報が0以上の場合はパケットを送信する
		if (tagList.tagCount() > 0) {
			NBTTagCompound send = new NBTTagCompound();
			send.setTag("avatar", tagList);
			
			//全クライアントへ送信する
			LMRNetwork.sendPacketToAllPlayer(EnumPacketMode.CLIENT_SYNC_SERVER_LMAVATAR, -1, send);
		}
		
	}
}
