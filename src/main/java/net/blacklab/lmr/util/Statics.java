package net.blacklab.lmr.util;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.EntityLittleMaidAvatarMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class Statics
{

	/** Absoption効果をクライアント側へ転送するのに使う */
	// TODO DataManagerは手探り
	public static final DataParameter<Float> dataWatch_Absoption		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.FLOAT);

	/** メイドカラー(byte) */
	public static final DataParameter<Integer> dataWatch_Color			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/**
	 * MSB|0x0000 0000|LSB<br>
	 *       |    |本体のテクスチャインデックス<br>
	 *       |アーマーのテクスチャインデックス<br>
	 */
	public static final DataParameter<Integer> dataWatch_Texture		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** モデルパーツの表示フラグ(Integer) */
	public static final DataParameter<Integer> dataWatch_Parts			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/**
	 * 各種フラグを一纏めにしたもの。
	 */
	// TODO VARって何
	public static final DataParameter<Integer> dataWatch_Flags			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	public static final int dataWatch_Flags_looksWithInterest		= 0x00000001;
	public static final int dataWatch_Flags_looksWithInterestAXIS	= 0x00000002;
	public static final int dataWatch_Flags_Aimebow					= 0x00000004;
	public static final int dataWatch_Flags_Freedom					= 0x00000008;
	public static final int dataWatch_Flags_Tracer					= 0x00000010;
	public static final int dataWatch_Flags_remainsContract			= 0x00000020;
	public static final int dataWatch_Flags_PlayingMode				= 0x00000040;
	public static final int dataWatch_Flags_Working					= 0x00000080;
	public static final int dataWatch_Flags_Wait					= 0x00000100;
	public static final int dataWatch_Flags_WaitEx					= 0x00000200;
	public static final int dataWatch_Flags_LooksSugar				= 0x00000400;
	public static final int dataWatch_Flags_Bloodsuck				= 0x00000800;
	public static final int dataWatch_Flags_OverDrive				= 0x00001000;
	public static final int dataWatch_Flags_Register				= 0x00002000;
	public static final int dataWatch_Flags_Swimming				= 0x00004000;

	/** 紐の持ち主のEntityID。 */
	public static final DataParameter<Integer> dataWatch_Gotcha			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);

	/** メイドモード(Short) */
	public static final DataParameter<Integer> dataWatch_Mode			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** 利き腕(Byte) */
	public static final DataParameter<Integer> dataWatch_DominamtArm	= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** アイテムの使用判定、腕毎(Integer) */
	public static final DataParameter<Integer> dataWatch_ItemUse		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** 保持経験値→メイド経験値で上書きな */
	public static final DataParameter<Float> dataWatch_MaidExpValue		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.FLOAT);

	// TODO この処遇は何とするか．EntityPlayer#ABSORPTIONはprivateだし
	/** EntityPlayer と EntityTameable で17番がかぶっているため、EntityPlayer側を28へ移動。 */
	public static final DataParameter<Float> dataWatch_AbsorptionAmount	= EntityDataManager.createKey(EntityLittleMaidAvatarMP.class, DataSerializers.FLOAT);

	/**
	 * 自由設定値。
	 */
	public static final DataParameter<Integer> dataWatch_Free			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);

	public static final int dataFlags_ForceUpdateInventory	= 0x80000000;

// NetWork

	/*
	 * 動作用定数、8bit目を立てるとEntity要求
	 */

	/*
	 * LMMPacetのフォーマット
	 * (Byte)
	 * 0	: 識別(1byte)
	 * 1 - 4: EntityID(4Byte)場合に寄っては省略
	 * 5 - 	: Data
	 *
	 */
	/**
	 * サーバー側へ対象のインベントリを送信するように指示する。
	 * スポーン時点ではインベントリ情報が無いため。
	 * [0]		: 0x00;
	 * [1..4]	: EntityID(int);
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
	/**
	 * クライアント側へ腕振りを指示する。
	 * 振った時の再生音声も指定する。
	 * [0]		: 0x81;
	 * [1..4]	: EntityID(int);
	 * [5]		: ArmIndex(byte);
	 * [6..9]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	/**
	 * サーバー側へ染料の使用を通知する。
	 * GUISelect用。
	 * [0]		: 0x02;
	 * [1]		: color(byte);
	 */
	public static final byte LMN_Server_DecDyePowder	= (byte)0x02;
	/**
	 * サーバーへIFFの設定値が変更されたことを通知する。
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 * [6..]	: TargetName(str);
	 */
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	/**
	 * クライアントへIFFの設定値を通知する。
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 */
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	/**
	 * サーバーへ現在のIFFの設定値を要求する。
	 * 要求時は一意な識別番号を付与すること。
	 * [0]		: 0x05;
	 * [1..4]	: Index(int);
	 * [5..]	: TargetName(str);
	 */
	public static final byte LMN_Server_GetIFFValue		= (byte)0x05;
	/**
	 * サーバーへIFFの設定値を保存するように指示する。
	 * [0]		: 0x06;
	 */
	public static final byte LMN_Server_SaveIFF			= (byte)0x06;
	/**
	 * クライアント側へ音声を発生させるように指示する。
	 * 音声の自体はクライアント側の登録音声を使用するため標準の再生手順だと音がでないため。
	 * [0]		: 0x89;
	 * [1..4]	: EntityID(int);
	 * [5..8]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_PlaySound		= (byte)0x89;

}
