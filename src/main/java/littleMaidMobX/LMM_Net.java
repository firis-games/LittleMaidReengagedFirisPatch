package littleMaidMobX;

import static littleMaidMobX.LMM_Statics.LMN_Client_SetIFFValue;
import static littleMaidMobX.LMM_Statics.LMN_Server_DecDyePowder;
import static littleMaidMobX.LMM_Statics.LMN_Server_GetIFFValue;
import static littleMaidMobX.LMM_Statics.LMN_Server_SaveIFF;
import static littleMaidMobX.LMM_Statics.LMN_Server_SetIFFValue;
import static littleMaidMobX.LMM_Statics.LMN_Server_UpdateSlots;
import mmmlibx.lib.MMM_Helper;
import net.blacklab.lmmnx.sync.LMMNX_NetSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import network.W_Message;
import network.W_Network;

public class LMM_Net {
	
	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して全てのクライアントへ送信
	 */
	public static void sendToAllEClient(LMM_EntityLittleMaid pEntity, byte[] pData)
	{
		MMM_Helper.setInt(pData, 1, pEntity.getEntityId());
		
		EntityTracker et = ((WorldServer)pEntity.worldObj).getEntityTracker();
		for(EntityPlayer player : et.getTrackingPlayers(pEntity))
		{
			W_Network.sendPacketToPlayer(2, player, pData);
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
		W_Network.sendPacketToPlayer(2, player, pData);
//		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭にEntityIDを付与してサーバーへ送信。
	 * 0:Mode, 1-4:EntityID, 5-:Data
	 */
	public static void sendToEServer(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.getEntityId());
		W_Network.sendPacketToServer(2,pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LMM_LittleMaidMobNX.Debug(String.format("LMM|Upd:send:%2x:%d", pData[0], pEntity.getEntityId()));
	}

	public static void sendToServer(byte[] pData) {
		W_Network.sendPacketToServer(2, pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LMM_LittleMaidMobNX.Debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
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
	public static LMM_EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld)
	{
		Entity lentity = MMM_Helper.getEntity(pData, pIndex, pWorld);
		if (lentity instanceof LMM_EntityLittleMaid)
		{
			return (LMM_EntityLittleMaid)lentity;
		}
		return null;
	}

	// 受信パケットの処理
	
	@SuppressWarnings("null")
	public static void serverCustomPayload(EntityPlayer playerEntity, W_Message pPayload)
	{
		// サーバ側の動作
		byte lmode = pPayload.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(pPayload.data, 1);
			lemaid = getLittleMaid(pPayload.data, 1, playerEntity.worldObj);
			if (lemaid == null) return;
		}
		LMM_LittleMaidMobNX.Debug(String.format("LMM|Upd Srv Call[%2x:%d].", lmode, leid));
		int lindex;
		int lval;
		String lname;
		
		switch (lmode) {
		case LMN_Server_UpdateSlots : 
			// 初回更新とか
			// インベントリの更新
			lemaid.maidInventory.clearChanged();
			for (LMM_SwingStatus lswing : lemaid.mstatSwingStatus) {
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
							MMM_Helper.decPlayerInventory(playerEntity, li, 1);
						}
					}
				}
			}
			break;
			
		case LMN_Server_SetIFFValue :
			// IFFの設定値を受信
			lval = pPayload.data[1];
			lindex = MMM_Helper.getInt(pPayload.data, 2);
			lname = MMM_Helper.getStr(pPayload.data, 6);
			LMM_LittleMaidMobNX.Debug("setIFF-SV user:%s %s(%d)=%d", MMM_Helper.getPlayerName(playerEntity), lname, lindex, lval);
			LMM_IFF.setIFFValue(MMM_Helper.getPlayerName(playerEntity), lname, lval);
			sendIFFValue(playerEntity, lval, lindex);
			break;
		case LMN_Server_GetIFFValue :
			// IFFGUI open
			lindex = MMM_Helper.getInt(pPayload.data, 1);
			lname = MMM_Helper.getStr(pPayload.data, 5);
			lval = LMM_IFF.getIFF(MMM_Helper.getPlayerName(playerEntity), lname, playerEntity.worldObj);
			LMM_LittleMaidMobNX.Debug("getIFF-SV user:%s %s(%d)=%d", MMM_Helper.getPlayerName(playerEntity), lname, lindex, lval);
			sendIFFValue(playerEntity, lval, lindex);
			break;
		case LMN_Server_SaveIFF :
			// IFFファイルの保存
			LMM_IFF.saveIFF(MMM_Helper.getPlayerName(playerEntity));
			if (!playerEntity.worldObj.isRemote) {
				LMM_IFF.saveIFF("");
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
		MMM_Helper.setInt(ldata, 2, pIndex);
		sendToClient(player, ldata);
	}
}
