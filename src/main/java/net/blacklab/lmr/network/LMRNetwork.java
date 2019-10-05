package net.blacklab.lmr.network;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.SwingStatus;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

	public static void sendPacketToServer(LMRMessage.EnumPacketMode mode, Integer id, NBTTagCompound tagCompound)
	{
		INSTANCE.sendToServer(new LMRMessage(mode, id, tagCompound));
	}

	public static void sendPacketToPlayer(LMRMessage.EnumPacketMode mode, Integer id, NBTTagCompound tagCompound, EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			INSTANCE.sendTo(new LMRMessage(mode, id, tagCompound), (EntityPlayerMP)player);
		}
	}

	public static void sendPacketToAllPlayer(LMRMessage.EnumPacketMode mode, Integer id, NBTTagCompound tagCompound)
	{
		INSTANCE.sendToAll(new LMRMessage(mode, id, tagCompound));
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

	/**
	 * サーバーへIFFのセーブをリクエスト
	 */
	public static void requestSavingIFF() {
		sendPacketToServer(LMRMessage.EnumPacketMode.SERVER_SAVE_IFF, 0, null);
	}

	public static void onServerCustomPayload(EntityPlayer sender, LMRMessage pPayload) {
		// Turn true if the packet is sent from server
		LMRMessage.EnumPacketMode lmode = pPayload.getMode();

		if (lmode == null) return;

		LittleMaidReengaged.Debug("MODE: %s", lmode.toString());

		Entity lemaid = null;
		if (lmode.withEntity) {
			lemaid = sender.getEntityWorld().getEntityByID(pPayload.getEntityId());
			if (!(lemaid instanceof EntityLittleMaid)) return;

			LittleMaidReengaged.Debug("Check Debug-%d/%s/%s",
					lemaid.getEntityId(),
					((EntityLittleMaid) lemaid).isRemainsContract(), ((EntityLittleMaid) lemaid).isFreedom());
			syncPayLoad(lmode, (EntityLittleMaid) lemaid, pPayload.getTag());
		}
		serverPayLoad(lmode, sender, (EntityLittleMaid) lemaid, pPayload.getTag());
	}

	private static void serverPayLoad(LMRMessage.EnumPacketMode pMode, EntityPlayer sender, EntityLittleMaid lemaid, NBTTagCompound tagCompound) {
		//int lindex;
		//int lval;
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
			byte color = tagCompound.getByte("Color");
			// Synchronizing
			lemaid.setColor(color);
			break;

		case SERVER_CHANGE_IFF :
			// IFFの設定値を受信
			byte value = tagCompound.getByte("Value");
			lname = tagCompound.getString("Name");

			LittleMaidReengaged.Debug("setIFF-SV user:%s %s=%d", CommonHelper.getPlayerUUID(sender), lname,  value);

			IFF.setIFFValue(CommonHelper.getPlayerUUID(sender), lname, value);
//			sendIFFValue(sender, value, lindex);
			IFF.saveIFF(CommonHelper.getPlayerUUID(sender));
			break;

		case SERVER_REQUEST_IFF :
			// IFFGUI open
			lname = tagCompound.getString("Name");
			value = IFF.getIFF(CommonHelper.getPlayerUUID(sender), lname, sender.getEntityWorld());

			sendIFFValue(sender, value, lname);

			LittleMaidReengaged.Debug("getIFF-SV user:%s %s=%d", CommonHelper.getPlayerUUID(sender), lname, value);
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
			lemaid.setFreedom(tagCompound.getBoolean("Freedom"));
			break;

		case SERVER_REQUEST_MODEL :
			lemaid.syncModelNames();
			lemaid.syncMaidArmorVisible();
			break;

		case REQUEST_CURRENT_ITEM:
			ItemStack stack = lemaid.getHeldItemMainhand();
			if (stack != null) {
				NBTTagCompound returnTag = new NBTTagCompound();
				returnTag.setInteger("Index", lemaid.maidInventory.currentItem);

				NBTTagCompound stackTag = new NBTTagCompound();
				stack.writeToNBT(stackTag);

				returnTag.setTag("Stack", stackTag);

				sendPacketToPlayer(LMRMessage.EnumPacketMode.CLIENT_CURRENT_ITEM, lemaid.getEntityId(), returnTag, sender);
			}
			break;

		default:
			break;
		}
	}

	protected static void syncPayLoad(LMRMessage.EnumPacketMode pMode, EntityLittleMaid pMaid, NBTTagCompound tagCompound) {
		switch (pMode) {
		case SYNC_ARMORFLAG:
			pMaid.setMaidArmorVisible(tagCompound.getInteger("Visible"));
			break;

		case SYNC_EXPBOOST:
			pMaid.setExpBooster(tagCompound.getInteger("Booster"));
			break;

		case SYNC_MODEL :
//			LittleMaidReengaged.Debug("CLIENT=%5s, INDEX:%d, name=%s", pMaid.worldObj.isRemote, contents[0], NetworkHelper.getStrFromPacket(contents, 1));

			pMaid.setTextureNameMain(tagCompound.getString("Main"));
			pMaid.setTextureNameArmor(tagCompound.getString("Armor"));
			break;
		default:
			break;
		}
	}

	protected static void sendIFFValue(EntityPlayer player, byte pValue, String name) {
		NBTTagCompound sendTag = new NBTTagCompound();

		sendTag.setByte("Value", pValue);
		sendTag.setString("Name", name);

		sendPacketToPlayer(LMRMessage.EnumPacketMode.CLIENT_RESPOND_IFF, null, sendTag, player);
	}

}