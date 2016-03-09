package net.blacklab.lmr.network;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.helper.NetworkHelper;

public class LMMNX_NetSync {

	public static final byte LMMNX_Sync = (byte) 0x84;

	public static final byte LMMNX_Sync_UB_Armor   = (byte) 0x00;
	public static final byte LMMNX_Sync_UB_Swim    = (byte) 0x01;
	public static final byte LMMNX_Sync_UB_Freedom = (byte) 0x02;
	//クライアントのみ
	public static final byte LMMNX_Sync_UB_RequestParamRecall = (byte) 0x03;

	// メイド経験値ブースト
	public static final byte LMMNX_Sync_UB_RequestExpBoost = (byte)0x04;			// C->S
	public static final byte LMMNX_Sync_Integer_SetExpBoost = (byte)0x10;

	// 手動onDeathUpdate
	public static final byte LMMNX_Sync_UB_ManualOnDeath = (byte) 0x05;

	// サーバがテクスチャ設定を受信(C->S)
	public static final byte LMMNX_Sync_String_MT_RequestChangeRender   = (byte) 0x20;
	public static final byte LMMNX_Sync_String_AT_RequestChangeRender   = (byte) 0x21;
	// サーバから保存されたテクスチャ情報を返す(S->C)
	public static final byte LMMNX_Sync_String_MT_RecallParam = (byte) 0x22;
	public static final byte LMMNX_Sync_String_AT_RecallParam = (byte) 0x23;

	public static void onPayLoad(EntityLittleMaid pMaid, byte[] pData){
		if(pData==null) return;
		if((pData[5] & 0xf0) == 0x20){
			// String
			onPayLoad(pMaid, pData[5], NetworkHelper.getStrFromPacket(pData, 6));
			return;
		}
		if ((pData[5] & 0xf0) == 0x10) {
			// Integer
			onPayLoad(pMaid, pData[5], NetworkHelper.getIntFromPacket(pData, 6));
			return;
		}
		if((pData[5] & 0xf0)==0x00){
			// byte
//			if(pData.length!=7) throw new IndexOutOfBoundsException("Data has wrong size");
			onPayLoad(pMaid, pData[5], pData[6]);
		}
	}

	public static void onPayLoad(EntityLittleMaid pMaid, byte pMode, byte pData){
		switch (pMode) {
		case LMMNX_Sync_UB_Armor:
			LittleMaidReengaged.Debug("SYNC ARMOR");
			pMaid.setMaidArmorVisible(pData);
			break;
		case LMMNX_Sync_UB_Swim:
			pMaid.setSwimming(pData==(byte)1);
			break;
		case LMMNX_Sync_UB_Freedom:
			pMaid.setFreedom(pData==(byte)1);
			break;
		case LMMNX_Sync_UB_RequestParamRecall :
			pMaid.syncMaidArmorVisible();
			pMaid.recallRenderParamTextureName(pMaid.textureModelNameForClient, pMaid.textureArmorNameForClient);
			break;
		case LMMNX_Sync_UB_RequestExpBoost :
			pMaid.requestExpBoost();
			break;
		case LMMNX_Sync_UB_ManualOnDeath :
			pMaid.manualOnDeath();
			break;
		}
	}

	public static void onPayLoad(EntityLittleMaid pMaid, byte pMode, Integer pInteger) {
		switch (pMode) {
		case LMMNX_Sync_Integer_SetExpBoost:
			pMaid.setExpBooster(pInteger);
			break;
		}
	}

	public static void onPayLoad(EntityLittleMaid pMaid, byte pMode, String pString){
		switch (pMode) {
		case LMMNX_Sync_String_MT_RequestChangeRender:
			pMaid.recallRenderParamTextureName(pString, pMaid.textureArmorNameForClient);
			break;
		case LMMNX_Sync_String_AT_RequestChangeRender:
			pMaid.recallRenderParamTextureName(pMaid.textureModelNameForClient, pString);
			break;
		case LMMNX_Sync_String_MT_RecallParam:
			pMaid.returnedRecallParam(pString, pMaid.textureArmorNameForClient);
			break;
		case LMMNX_Sync_String_AT_RecallParam:
			pMaid.returnedRecallParam(pMaid.textureModelNameForClient, pString);
			break;
		}
	}

}
