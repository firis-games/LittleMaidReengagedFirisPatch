package net.blacklab.lmr.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTextureModelHandler implements IMessageHandler<PacketTextureModel, IMessage> {

	@Override
	public IMessage onMessage(PacketTextureModel message, MessageContext ctx) {
		return null;
	}

}
