package net.blacklab.lmr.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LMRMessageHandler implements IMessageHandler<LMRMessage, IMessage>
{
	@Override//IMessageHandlerのメソッド
	public IMessage onMessage(LMRMessage message, MessageContext ctx)
	{
		if(message.data != null) {
			LMRNetwork.onCustomPayload(ctx.side.isServer() ? ctx.getServerHandler().playerEntity : null, message);
		}
		return null;//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
	}
}
