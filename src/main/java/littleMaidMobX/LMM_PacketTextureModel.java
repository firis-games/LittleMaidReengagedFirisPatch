package littleMaidMobX;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class LMM_PacketTextureModel implements IMessage {

	/**
	 * 制御情報を格納するbyteです。値のセットにはなるべくコンストラクタを使用してください。
	 */
	public byte mode;
	/**
	 * データの中身をStringで指定します。値のセットにはなるべくコンストラクタを利用してください。
	 */
	public String text;
	
	/**
	 * 内部処理が利用するコンストラクタ無しのメソッドです。通常は使用しません。
	 */
	public LMM_PacketTextureModel(){
		mode = (byte)0;
		text = "";
	}
	
	/**
	 * 文字列データを送受信する新しいSimplePacketStringインスタンスを生成します。
	 * @param pmode モード値
	 * @param text データ
	 */
	public LMM_PacketTextureModel(byte pmode, String ptext){
		mode = pmode;
		text = ptext;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mode = buf.getByte(0);
		ByteBuf bbuf = buf.slice(1, buf.readableBytes()-1);
		text = bbuf.toString(Charset.forName("UTF-8"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(mode);
		buf.writeBytes(text.getBytes(Charset.forName("UTF-8")));
	}
}
