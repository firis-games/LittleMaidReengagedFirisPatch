package net.blacklab.lmc.common.network;

import io.netty.buffer.ByteBuf;
import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * GuiOpen処理
 * @author computer
 *
 */
public class PacketSpawnParticleS2C implements IMessageHandler<PacketSpawnParticleS2C.MessageSpawnParticle, IMessage> {
	
	@Override
	public IMessage onMessage(MessageSpawnParticle message, MessageContext ctx) {
		
		LittleMaidReengaged.proxy.spawnParticle(message.pos, message.particleNo);
		
		return null;
	}
	
	/**
	 * Messageクラス
	 * @author computer
	 *
	 */
	public static class MessageSpawnParticle implements IMessage {
		
		public BlockPos pos;
		public int particleNo;
		
		public MessageSpawnParticle() {
		}

		public MessageSpawnParticle(BlockPos pos, int particleNo) {
			this.pos = pos;
			this.particleNo = particleNo;
		}
		
		/**
		 * byteからの復元
		 * @param buf
		 */
		@Override
		public void fromBytes(ByteBuf buf) {
			//書き込んだ順番で読み込み
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			this.pos = new BlockPos(x, y, z);
			this.particleNo = buf.readInt();
		}

		/**
		 * byteへ変換
		 * @param buf
		 */
		@Override
		public void toBytes(ByteBuf buf) {
			//intを書き込み
			buf.writeInt(this.pos.getX());
			buf.writeInt(this.pos.getY());
			buf.writeInt(this.pos.getZ());
			buf.writeInt(this.particleNo);
		}
	}
}