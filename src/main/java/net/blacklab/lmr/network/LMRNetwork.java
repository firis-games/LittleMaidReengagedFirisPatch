package net.blacklab.lmr.network;

import static net.blacklab.lmr.util.Statics.*;

import java.util.Arrays;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.actionsp.SwingStatus;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.blacklab.lmr.util.helper.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LMRNetwork
{
	//このMOD用のSimpleNetworkWrapperを生成。チャンネルの文字列は固有であれば何でも良い。MODIDの利用を推奨。
	private static SimpleNetworkWrapper INSTANCE;

	public static void init(String ch)
	{
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ch);

		INSTANCE.registerMessage(LMRMessageHandler.class, LMRMessage.class, 0, Side.SERVER);
		INSTANCE.registerMessage(LMRMessageHandler.class, LMRMessage.class, 0, Side.CLIENT);
	}

	private static void sendPacketToServer(int ch, byte[] data)
	{
		INSTANCE.sendToServer(new LMRMessage(ch, data));
	}

	private static void sendPacketToPlayer(int ch, EntityPlayer player, byte[] data)
	{
		if(player instanceof EntityPlayerMP)
		{
			INSTANCE.sendTo(new LMRMessage(ch, data), (EntityPlayerMP)player);
		}
	}

	private static void sendPacketToAllPlayer(int ch, byte[] data)
	{
		INSTANCE.sendToAll(new LMRMessage(ch, data));
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

	private static void sendToClient(EntityPlayer player, byte[] pData) {
		sendPacketToPlayer(2, player, pData);
//		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	public static void sendToClient(EnumPacketMode pMode, EntityPlayer player, byte[] contents) {
		byte dst[] = new byte[contents.length+1];
		dst[0] = pMode.modeByte;
		System.arraycopy(contents, 0, dst, 1, contents.length);
		sendPacketToPlayer(2, player, dst);
	}

	public static void sendToAllClient(EnumPacketMode pMode, byte[] contents) {
		byte[] dst = new byte[contents.length + 1];
		dst[0] = pMode.modeByte;
		System.arraycopy(contents, 0, dst, 1, contents.length);
		sendPacketToAllPlayer(2, dst);
	}

	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して全てのクライアントへ送信
	 */
	public static void sendToAllClientWithEntityID(EnumPacketMode pMode, EntityLittleMaid pMaid, byte[] contents) {
		byte[] dst = new byte[contents.length + 4];
		System.arraycopy(contents, 0, dst, 4, contents.length);
		NetworkHelper.setIntToPacket(dst, 0, pMaid.getEntityId());
		sendToAllClient(pMode, dst);
	}

	private static void sendToServer(byte[] pData) {
		sendPacketToServer(2, pData);
//		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		LittleMaidReengaged.Debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
	}

	public static void sendToServer(EnumPacketMode pMode, byte[] contents) {
		byte[] dst = new byte[contents.length + 1];
		dst[0] = pMode.modeByte;
		System.arraycopy(contents, 0, dst, 1, contents.length);
		sendToServer(dst);
	}

	public static void sendToServerWithEntityID(EnumPacketMode pMode, EntityLittleMaid pMaid, byte[] contents) {
		byte[] dst = new byte[contents.length + 4];
		System.arraycopy(contents, 0, dst, 4, contents.length);
		NetworkHelper.setIntToPacket(dst, 0, pMaid.getEntityId());
		sendToServer(pMode, dst);
	}

	/**
	 * サーバーへIFFのセーブをリクエスト
	 */
	public static void requestSavingIFF() {
		sendToServer(new byte[] {LMN_Server_SaveIFF});
	}

	/**
	 * littleMaidのEntityを返す。
	 */
	public static EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld)
	{
		Entity lentity = LMRNetwork.getEntityFromPacket(pData, pIndex, pWorld);
		if (lentity instanceof EntityLittleMaid)
		{
			return (EntityLittleMaid)lentity;
		}
		return null;
	}

	public static void onServerCustomPayload(EntityPlayer sender, LMRMessage pPayload) {
		// Turn true if the packet is sent from server
		EnumPacketMode lmode = EnumPacketMode.getEnumPacketMode(pPayload.data[0]);
		if (lmode == null) return;
		LittleMaidReengaged.Debug("MODE: %s", lmode.toString());
		EntityLittleMaid lemaid = null;
		if (lmode.withEntity) {
			lemaid = getLittleMaid(pPayload.data, 1, sender.worldObj);
			if (lemaid == null) return;
			syncPayLoad(lmode, lemaid, Arrays.copyOfRange(pPayload.data, 5, pPayload.data.length));
		}
		serverPayLoad(lmode, sender, lemaid, Arrays.copyOfRange(pPayload.data, lmode.withEntity?5:1, pPayload.data.length));
	}

	private static void serverPayLoad(EnumPacketMode pMode, EntityPlayer sender, EntityLittleMaid lemaid, byte[] contents) {
		int lindex;
		int lval;
		String lname;

		switch (pMode) {
		case SERVER_UPDATE_SLOTS :
			// 初回更新とか
			// インベントリの更新
			lemaid.maidInventory.clearChanged();
			for (SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;

		case SERVER_DECREMENT_DYE :
			// カラー番号をクライアントから受け取る
			// インベントリから染料を減らす。
			int lcolor2 = contents[0];
			// Synchronizing
			lemaid.setColor(lcolor2);
			if (!sender.capabilities.isCreativeMode) {
				for (int li = 0; li < sender.inventory.mainInventory.length; li++) {
					ItemStack lis = sender.inventory.mainInventory[li];
					if (lis != null && lis.getItem() == Items.dye) {
						if (lis.getItemDamage() == (15 - lcolor2)) {
							MaidHelper.decPlayerInventory(sender, li, 1);
						}
					}
				}
			}
			break;

		case SERVER_CHANGE_IFF :
			// IFFの設定値を受信
			lval = contents[0];
			lindex = NetworkHelper.getIntFromPacket(contents, 1);
			lname = NetworkHelper.getStrFromPacket(contents, 5);
			LittleMaidReengaged.Debug("setIFF-SV user:%s %s(%d)=%d", CommonHelper.getPlayerUUID(sender), lname, lindex, lval);
			IFF.setIFFValue(CommonHelper.getPlayerUUID(sender), lname, lval);
			sendIFFValue(sender, lval, lindex);
			break;
		case SERVER_REQUEST_IFF :
			// IFFGUI open
			lindex = NetworkHelper.getIntFromPacket(contents, 0);
			lname = NetworkHelper.getStrFromPacket(contents, 4);
			lval = IFF.getIFF(CommonHelper.getPlayerUUID(sender), lname, sender.worldObj);
			LittleMaidReengaged.Debug("getIFF-SV user:%s %s(%d)=%d", CommonHelper.getPlayerUUID(sender), lname, lindex, lval);
			sendIFFValue(sender, lval, lindex);
			break;
		case SERVER_SAVE_IFF :
			// IFFファイルの保存
			IFF.saveIFF(CommonHelper.getPlayerUUID(sender));
//			if (!sender.worldObj.isRemote) {
//				IFF.saveIFF("");
//			}
			break;
		case SERVER_REQUEST_BOOST :
			lemaid.requestExpBoost();
			break;
		case SERVER_CHAMGE_FREEDOM :
			lemaid.setFreedom(contents[0]==1);
			break;
		case SERVER_CHANGE_SWIMMING :
			lemaid.setSwimming(contents[0]==1);
			break;
		case SERVER_REQUEST_MODEL :
			lemaid.syncModelNames();
			lemaid.syncMaidArmorVisible();
			break;
		default:
			break;
		}
	}

	protected static void syncPayLoad(EnumPacketMode pMode, EntityLittleMaid pMaid, byte[] contents) {
		switch (pMode) {
		case SYNC_ARMORFLAG:
			pMaid.setMaidArmorVisible(contents[0]);
			break;
		case SYNC_EXPBOOST:
			pMaid.setExpBooster(NetworkHelper.getIntFromPacket(contents, 0));
			break;
		case SYNC_MODEL :
			LittleMaidReengaged.Debug("CLIENT=%5s, INDEX:%d, name=%s", pMaid.worldObj.isRemote, contents[0], NetworkHelper.getStrFromPacket(contents, 1));
			if (contents[0] == 0) {
				// main
				pMaid.setTextureNameMain(NetworkHelper.getStrFromPacket(contents, 1));
			} else {
				// armor
				pMaid.setTextureNameArmor(NetworkHelper.getStrFromPacket(contents, 1));
			}
		default:
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

	/**
	 * Entityを返す。
	 */
	public static Entity getEntityFromPacket(byte[] pData, int pIndex, World pWorld) {
		return pWorld.getEntityByID(NetworkHelper.getIntFromPacket(pData, pIndex));
	}
}