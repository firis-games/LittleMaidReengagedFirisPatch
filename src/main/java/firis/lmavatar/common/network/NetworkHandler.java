package firis.lmavatar.common.network;

import firis.lmavatar.LittleMaidAvatar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 通信関連の管理クラス
 * @author firis-games
 *
 */
public class NetworkHandler {
	

	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(LittleMaidAvatar.MODID);
	
	/**
	 * パケット初期化
	 */
	public static void preInit() {
		
		int idx = 1;
		
		//Server to Client
		network.registerMessage(PacketSendNBTTagCompound.class, MessageSendNBTTagCompound.class, idx++, Side.CLIENT);
		
		//Client to Server
		network.registerMessage(PacketSendNBTTagCompound.class, MessageSendNBTTagCompound.class, idx++, Side.SERVER);
		
	}
	
	/**
	 * サーバーへパケットを送信する
	 */
	public static void sendPacketToServer(int sendType, NBTTagCompound nbt) {
		network.sendToServer(new MessageSendNBTTagCompound(sendType, nbt));
	}
	
	/**
	 * すべてのプレイヤーにパケットを送信する
	 * @param sendType
	 * @param nbt
	 */
	public static void sendPacketToClientAll(int sendType, NBTTagCompound nbt) {
		network.sendToAll(new MessageSendNBTTagCompound(sendType, nbt));
	}
	
	/**
	 * 指定したプレイヤーにパケットを送信する
	 * @param sendType
	 * @param nbt
	 * @param player
	 */
	public static void sendPacketToClientPlayer(int sendType, NBTTagCompound nbt, EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			network.sendTo(new MessageSendNBTTagCompound(sendType, nbt), (EntityPlayerMP) player);
		}
	}

}
