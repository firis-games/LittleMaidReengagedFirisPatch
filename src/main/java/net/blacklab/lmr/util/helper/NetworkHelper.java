package net.blacklab.lmr.util.helper;

public class NetworkHelper {
	
	/*
	 * 送受信データ関連
	 */

	public static void setIntToPacket(byte[] pData, int pIndex, int pVal) {
		pData[pIndex + 3]	= (byte)(pVal & 0xff);
		pData[pIndex + 2]	= (byte)((pVal >>> 8) & 0xff);
		pData[pIndex + 1]	= (byte)((pVal >>> 16) & 0xff);
		pData[pIndex + 0]	= (byte)((pVal >>> 24) & 0xff);
	}

	public static int getIntFromPacket(byte[] pData, int pIndex) {
		return (pData[pIndex + 3] & 0xff) | ((pData[pIndex + 2] & 0xff) << 8) | ((pData[pIndex + 1] & 0xff) << 16) | ((pData[pIndex + 0] & 0xff) << 24);
	}

	public static void setFloatToPacket(byte[] pData, int pIndex, float pVal) {
		setIntToPacket(pData, pIndex, Float.floatToIntBits(pVal));
	}

	public static float getFloatFromPacket(byte[] pData, int pIndex) {
		return Float.intBitsToFloat(getIntFromPacket(pData, pIndex));
	}

	public static void setShortToPacket(byte[] pData, int pIndex, int pVal) {
		pData[pIndex++]	= (byte)(pVal & 0xff);
		pData[pIndex]	= (byte)((pVal >>> 8) & 0xff);
	}

	public static short getShortFromPacket(byte[] pData, int pIndex) {
		return (short)((pData[pIndex] & 0xff) | ((pData[pIndex + 1] & 0xff) << 8));
	}

	public static String getStrFromPacket(byte[] pData, int pIndex, int pLen) {
		String ls = new String(pData, pIndex, pLen);
		return ls;
	}

	public static String getStrFromPacket(byte[] pData, int pIndex) {
		return getStrFromPacket(pData, pIndex, pData.length - pIndex);
	}

	public static void setStrToPacket(byte[] pData, int pIndex, String pVal) {
		byte[] lb = pVal.getBytes();
		for (int li = pIndex; li < pData.length; li++) {
			pData[li] = lb[li - pIndex];
		}
	}

}
