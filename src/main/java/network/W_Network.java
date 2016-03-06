package network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class W_Network
{
	//このMOD用のSimpleNetworkWrapperを生成。チャンネルの文字列は固有であれば何でも良い。MODIDの利用を推奨。
	private static SimpleNetworkWrapper INSTANCE;

	public static void init(String ch)
	{
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ch);

		/*IMesssageHandlerクラスとMessageクラスの登録。
		*第三引数：MessageクラスのMOD内での登録ID。256個登録できる
		*第四引数：送り先指定。クライアントかサーバーか、Side.CLIENT Side.SERVER*/
		// どうせまた変わるだろうから最低限のみ登録。メッセージの振り分けはMOD側で行う。
		INSTANCE.registerMessage(W_MessageHandler.class, W_Message.class, 0, Side.SERVER);
		INSTANCE.registerMessage(W_MessageHandler.class, W_Message.class, 0, Side.CLIENT);
	}

	public static void sendPacketToServer(int ch, byte[] data)
	{
		INSTANCE.sendToServer(new W_Message(ch, data));
	}

	public static void sendPacketToPlayer(int ch, EntityPlayer player, byte[] data)
	{
		if(player instanceof EntityPlayerMP)
		{
			INSTANCE.sendTo(new W_Message(ch, data), (EntityPlayerMP)player);
		}
	}

	public static void sendPacketToAllPlayer(int ch, byte[] data)
	{
		INSTANCE.sendToAll(new W_Message(ch, data));
	}
}