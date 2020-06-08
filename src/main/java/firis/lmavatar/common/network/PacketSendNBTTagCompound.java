package firis.lmavatar.common.network;

import firis.lmavatar.common.command.LMAvatarCommandClient;
import firis.lmavatar.common.manager.PlayerModelManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * NBT形式のパケット送信用クラス
 * @author firis-games
 *
 */
public class PacketSendNBTTagCompound implements IMessageHandler<MessageSendNBTTagCompound, IMessage> {

	/** クライアントの情報をサーバーへ送信 */
	public static int SERVER_SYNC_CLIENT_LMAVATAR = 1;
	
	/** サーバーの情報をクライアントへ送信 */
	public static int CLIENT_SYNC_SERVER_LMAVATAR = 2;
	
	/** コマンド同期 */
	public static int CLIENT_COMMAND_EXECUTE = 3;
	
	@Override
	public IMessage onMessage(MessageSendNBTTagCompound message, MessageContext ctx) {
		
		//Client to Server
		if (message.type == SERVER_SYNC_CLIENT_LMAVATAR) {
			PlayerModelManager.reciveLMAvatarDataFromClient(message.nbt);
			
		//Server to Client
		} else if (message.type == CLIENT_SYNC_SERVER_LMAVATAR) {
			PlayerModelManager.receiveLMAvatarDataFromServer(message.nbt);
			
		//Comand sync
		} else if (message.type == CLIENT_COMMAND_EXECUTE) {
			LMAvatarCommandClient.execute(message.nbt.getString("command"), message.nbt.getString("param"));
		}
		
		return null;
	}

}
