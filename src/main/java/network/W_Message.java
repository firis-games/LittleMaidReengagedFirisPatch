package network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class W_Message implements IMessage
{
	/** チャネル番号 */
	public byte   ch;
	/** 実データ */
	public byte[] data;

	public W_Message(){}

	/**
	 * @param ch どこ宛かを示すチャネル番号。LMM宛か、MMMLib宛かを区別するために追加した
	 * @param sendData 送信する実データ
	 *  */
	public W_Message(int ch, byte[] sendData)
	{
		this.ch		= (byte)ch;
		this.data	= sendData;
	}

	/** IMessageのメソッド。ByteBufからデータを読み取る。
	 * data[0] ... 通信パケットに勝手につくMOD側から見ればゴミ
	 * data[1] ... チャネル番号
	 * data[2] ... 以降が実データ
	 * */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		try{
			int len = buf.array().length;
			//System.out.println("DEBUG INFO=READABLE BUF:"+buf.readableBytes());

			if(len > 2)
			{
				ByteBuf bbuf = buf.slice(0, buf.readableBytes());
				this.data = new byte[bbuf.readableBytes()-1];
				this.ch =  bbuf.getByte(0);
				bbuf.getBytes(1, this.data);
			}
			else
			{
				this.data = new byte[]{0};
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override//IMessageのメソッド。ByteBufにデータを書き込む。
	public void toBytes(ByteBuf buf)
	{
		buf.writeByte(this.ch);
		buf.writeBytes(this.data);
	}
}
