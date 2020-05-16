package net.firis.lmt.common.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import net.blacklab.lmr.network.LMRMessage.EnumPacketMode;
import net.blacklab.lmr.network.LMRNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * クライアント側のパケット送信処理
 * 
 * キューにUUIDを貯めて一定時間ごとにパケットをまとめて投げる
 * サーバー側と同じ仕組みになっているがClient側はMinecraft.getMinecraft().playerしか動かない
 * 
 * @author firis-games
 *
 */
public class SyncPlayerModelClient {

	private final static Set<UUID> syncPacketQueue = new HashSet<>();
	
	/**
	 * モデルの同期を行う
	 */
	public static void syncModel() {
		syncPacketQueue.add(Minecraft.getMinecraft().player.getUniqueID());
	}
	
	/**
	 * クライアントサイドのtick処理
	 * @param event
	 */
	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().player != null){
			onClientTickEventPost(event);
		}
	}
	
	/**
	 * Tickの最後で処理を行う
	 * @param event
	 */
	protected void onClientTickEventPost(ClientTickEvent event) {
		
		Iterator<UUID> syncPacketIterator = syncPacketQueue.iterator();
		
		NBTTagCompound tagCompound = null;
		
		while (syncPacketIterator.hasNext()) {
			//NBT取得
			tagCompound = getAvatarModelNbt(syncPacketIterator.next());
			//削除
			syncPacketIterator.remove();
			break;
		}
		
		//条件に一致すればパケット送信
		sendPacketToServer(tagCompound);
	}
	
	/**
	 * LMアバターのNBTを取得する
	 * 対象が無い場合はnull
	 * @param tagCompound
	 * @return
	 */
	protected NBTTagCompound getAvatarModelNbt(UUID uuid) {
		if (!PlayerModelManager.clientModelNbtMap.containsKey(uuid)) {
			return null;
		}
		NBTTagCompound tagCompound = PlayerModelManager.clientModelNbtMap.get(uuid);
		return tagCompound;
	}
	
	/**
	 * サーバーへパケットを送信する
	 * @param uuid
	 */
	protected void sendPacketToServer(NBTTagCompound nbt) {
		if (nbt != null) {
			//サーバーへ送信する
			LMRNetwork.sendPacketToServer(EnumPacketMode.SERVER_SYNC_CLIENT_LMAVATAR, -1, nbt);
		}
	}
}
