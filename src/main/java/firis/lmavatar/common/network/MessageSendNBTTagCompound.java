package firis.lmavatar.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * NBT形式のパケット送信用クラス
 * @author firis-games
 *
 */
public class MessageSendNBTTagCompound implements IMessage {

	public int type = 0;
	public NBTTagCompound nbt = null;
	
	public MessageSendNBTTagCompound() {}
	
	public MessageSendNBTTagCompound(int sendType, NBTTagCompound tagCompound) {
		this.type = sendType;
		this.nbt = tagCompound;
	}
	
	/**
	 * パケットからでコードする
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		this.type = buf.readInt();
		this.nbt = ByteBufUtils.readTag(buf);
	}

	/**
	 * パケットへエンコードする
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.type);
		ByteBufUtils.writeTag(buf, this.nbt);
	}

}
