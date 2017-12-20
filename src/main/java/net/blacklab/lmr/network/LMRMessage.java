package net.blacklab.lmr.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class LMRMessage implements IMessage
{
	/**
	 * 通信モード
	 */
	private EnumPacketMode mode;

	/**
	 * 対象メイドID
	 */
	private Integer entityId;

	/**
	 * 通信内容NBT
	 */
	private NBTTagCompound tag;

	public LMRMessage(){
		// Keep empty
	}

	public LMRMessage(EnumPacketMode packetMode, Integer id, NBTTagCompound tagCompound)
	{
		mode = packetMode;
		entityId = id;
		tag = tagCompound;
	}

	/** IMessageのメソッド。ByteBufからデータを読み取る。
	 * data[0] ... 通信パケットに勝手につくMOD側から見ればゴミ
	 * data[1] ... チャネル番号
	 * data[2] ... 以降が実データ
	 * */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		try {
			mode = EnumPacketMode.getEnumPacketMode(buf.readByte());
			tag = ByteBufUtils.readTag(buf);
			entityId = buf.readInt();
		} catch (Exception e) {
			// Prevent to send fragmented data
			buf.clear();
		}
	}

	@Override//IMessageのメソッド。ByteBufにデータを書き込む。
	public void toBytes(ByteBuf buf)
	{
		buf.writeByte(mode.modeByte);
		if (tag != null)
			ByteBufUtils.writeTag(buf, tag);
		if (entityId != null)
			buf.writeInt(entityId);
	}

	public EnumPacketMode getMode() {
		return mode;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public NBTTagCompound getTag() {
		return tag;
	}
}
