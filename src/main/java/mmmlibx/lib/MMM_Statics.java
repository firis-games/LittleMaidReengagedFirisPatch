package mmmlibx.lib;

public class MMM_Statics {

	/**
	 * サーバーへテクスチャインデックスを送信する。
	 * [0]		: 0x80;
	 * [1..4]	: EntityID;
	 * [5]		: Color(byte);
	 * [6..7]	: TexturePackIndex0(short);
	 * [8..9]	: TexturePackIndex1(short);
	 * [10..11]	: TexturePackIndex2(short);
	 * ..
	 */
	public static final byte Server_SetTexturePackIndex = (byte)0x80;
	/**
	 * サーバーへテクスチャインデックスを問い合わせる。
	 * [0]		: 0x01;
	 * [1]		: BufIndex;
	 * [2..3]	: contColorBits
	 * [4..5]	: wildColorBits
	 * [6..9]	: height
	 * [10..13]	: width
	 * [14..17]	: yoffset
	 * [18..21]	: mountedYOffset
	 * [22..]	: TexturePackName;
	 */
	public static final byte Server_GetTextureIndex = (byte)0x01;
	/**
	 * サーバーへテクスチャインデックスを問い合わせる。
	 * [0]		: 0x01;
	 * [1]		: BufIndex;
	 * [2..3]	: TexturePackIndex;
	 */
	public static final byte Client_SetTextureIndex = (byte)0x01;

	/**
	 * サーバーへテクスチャパックの情報を問い合わせる。
	 * [0]		: 0x02;
	 * [1..2]	: TexturePackIndex;
	 */
	public static final byte Server_GetTexturePackName = (byte)0x02;
	/**
	 * クライアントへテクスチャパックの情報を設定する。
	 * [0]		: 0x02;
	 * [1..2]	: TexturePackIndex;
	 * [3..4]	: contColorBits
	 * [5..6]	: wildColorBits
	 * [7..10]	: height
	 * [11..14]	: width
	 * [15..18]	: yoffset
	 * [19..22]	: mountedYOffset
	 * [23..]	: TexturePackName;
	 */
	public static final byte Client_SetTexturePackName = (byte)0x02;
	
}
