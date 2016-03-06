package network;

import littleMaidMobX.LMM_LittleMaidMobNX;
import littleMaidMobX.LMM_Net;
import mmmlibx.lib.MMMLib;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class W_MessageHandler implements IMessageHandler<W_Message, IMessage>
{
	@Override//IMessageHandlerのメソッド
	public IMessage onMessage(W_Message message, MessageContext ctx)
	{
		if(message.data != null)
		{
			if(ctx.side.isClient())
			{
				LMM_LittleMaidMobNX.proxy.clientCustomPayload(message);
			}
			else
			{
				if(message.ch == 1)
				{
					MMMLib.serverCustomPayload(ctx.getServerHandler().playerEntity, message);
				}
				if(message.ch == 2)
				{
					LMM_Net.serverCustomPayload(ctx.getServerHandler().playerEntity, message);
				}
			}
		}
		return null;//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
	}
}
