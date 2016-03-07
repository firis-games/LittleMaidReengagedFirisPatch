package net.blacklab.lmr.network;

import static net.blacklab.lmr.util.Statics.LMN_Client_SetIFFValue;
import static net.blacklab.lmr.util.Statics.LMN_Server_DecDyePowder;
import static net.blacklab.lmr.util.Statics.LMN_Server_GetIFFValue;
import static net.blacklab.lmr.util.Statics.LMN_Server_SaveIFF;
import static net.blacklab.lmr.util.Statics.LMN_Server_SetIFFValue;
import static net.blacklab.lmr.util.Statics.LMN_Server_UpdateSlots;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.actionsp.SwingStatus;
import net.blacklab.lmr.util.CommonHelper;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.MaidHelper;
import net.blacklab.lmr.util.NetworkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class NetworkSync {
	
	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して全てのクライアントへ送信
	 */
	public static void sendToAllEClient(EntityLittleMaid pEntity, byte[] pData)
	{
		NetworkHelper.setIntToPacket(pData, 1, pEntity.getEntityId());
		
		EntityTracker et = ((WorldServer)pEntity.worldObj).getEntityTracker();
		for(EntityPlayer player : et.getTrackingPlayers(pEntity))
		{
			LMRNetwork.sendPacketToPlayer(2, player, pData);
		}
//		((WorldServer)pEntity.worldObj).getEntityTracker().func_151248_b(pEntity, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して特定ののクライアントへ送信
	 *
	public static void sendToEClient(EntityPlayer player, LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.getEntityId());
		W_Network.sendPacketToPlayer(player, pData);
//		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}
	*/

	public static void sendToClient(EntityPlayer player, byte[] pData) {
		LMRNetwork.sendPacketToPlayer(2, player, pData);
//		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭にEntityIDを付与してサーバーへ送信。
	 * 0:Mode, 1-4:EntityID, 5-:Data
	 */
	public static void sendToEServer(EntityLittleMaid pEntity, byte[] pData) {
		NetworkHelper.setIntToPacket(pData, 1, pEntity.getEntityId());
		LMRNetwork.sendPacketToServer(2,pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LittleMaidReengaged.Debug(String.format("LMM|Upd:send:%2x:%d", pData[0], pEntity.getEntityId()));
	}

	public static void sendToServer(byte[] pData) {
		LMRNetwork.sendPacketToServer(2, pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LittleMaidReengaged.Debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
	}

	/**
	 * サーバーへIFFのセーブをリクエスト
	 */
	public static void saveIFF() {
		sendToServer(new byte[] {LMN_Server_SaveIFF});
	}

	/**
	 * littleMaidのEntityを返す。
	 */
	public static EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld)
	{
		Entity lentity = CommonHelper.getEntity(pData, pIndex, pWorld);
		if (lentity instanceof EntityLittleMaid)
		{
			return (EntityLittleMaid)lentity;
		}
		return null;
	}

	// 受信パケットの処理
	
	@SuppressWarnings("null")
	public static void serverCustomPayload(EntityPlayer playerEntity, LMRMessage pPayload)
	{
		// サーバ側の動作
		byte lmode = pPayload.data[0];
		int leid = 0;
		EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = NetworkHelper.getIntFromPacket(pPayload.data, 1);
			lemaid = getLittleMaid(pPayload.data, 1, playerEntity.worldObj);
			if (lemaid == null) return;
		}
		LittleMaidReengaged.Debug(String.format("LMM|Upd Srv Call[%2x:%d].", lmode, leid));
		int lindex;
		int lval;
		String lname;
		
		switch (lmode) {
		case LMN_Server_UpdateSlots : 
			// 初回更新とか
			// インベントリの更新
			lemaid.maidInventory.clearChanged();
			for (SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;
			
		case LMN_Server_DecDyePowder :
			// カラー番号をクライアントから受け取る
			// インベントリから染料を減らす。
			int lcolor2 = pPayload.data[1];
			if (!playerEntity.capabilities.isCreativeMode) {
				for (int li = 0; li < playerEntity.inventory.mainInventory.length; li++) {
					ItemStack lis = playerEntity.inventory.mainInventory[li];
					if (lis != null && lis.getItem() == Items.dye) {
						if (lis.getItemDamage() == (15 - lcolor2)) {
							MaidHelper.decPlayerInventory(playerEntity, li, 1);
						}
					}
				}
			}
			break;
			
		case LMN_Server_SetIFFValue :
			// IFFの設定値を受信
			lval = pPayload.data[1];
			lindex = NetworkHelper.getIntFromPacket(pPayload.data, 2);
			lname = NetworkHelper.getStrFromPacket(pPayload.data, 6);
			LittleMaidReengaged.Debug("setIFF-SV user:%s %s(%d)=%d", CommonHelper.getPlayerName(playerEntity), lname, lindex, lval);
			IFF.setIFFValue(CommonHelper.getPlayerName(playerEntity), lname, lval);
			sendIFFValue(playerEntity, lval, lindex);
			break;
		case LMN_Server_GetIFFValue :
			// IFFGUI open
			lindex = NetworkHelper.getIntFromPacket(pPayload.data, 1);
			lname = NetworkHelper.getStrFromPacket(pPayload.data, 5);
			lval = IFF.getIFF(CommonHelper.getPlayerName(playerEntity), lname, playerEntity.worldObj);
			LittleMaidReengaged.Debug("getIFF-SV user:%s %s(%d)=%d", CommonHelper.getPlayerName(playerEntity), lname, lindex, lval);
			sendIFFValue(playerEntity, lval, lindex);
			break;
		case LMN_Server_SaveIFF :
			// IFFファイルの保存
			IFF.saveIFF(CommonHelper.getPlayerName(playerEntity));
			if (!playerEntity.worldObj.isRemote) {
				IFF.saveIFF("");
			}
			break;
		case LMMNX_NetSync.LMMNX_Sync:
			LMMNX_NetSync.onPayLoad(lemaid, pPayload.data);
			break;
		}
	}

	/**
	 * クライアントへIFFの設定値を通知する。
	 * @param pNetHandler
	 * @param pValue
	 * @param pIndex
	 */
	protected static void sendIFFValue(EntityPlayer player, int pValue, int pIndex) {
		byte ldata[] = new byte[] {
				LMN_Client_SetIFFValue,
				0,
				0, 0, 0, 0
		};
		ldata[1] = (byte)pValue;
		NetworkHelper.setIntToPacket(ldata, 2, pIndex);
		sendToClient(player, ldata);
	}
}
