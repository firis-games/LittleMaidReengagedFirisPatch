package net.blacklab.lmr.network;

public enum EnumPacketMode {
	// Be sent to server
	/*
	 * 0x0*	:	Server-side general configures
	 * 0x1*	:	Server-side maid operating
	 * 0x2*	:	Client-side maid configures
	 * 0x3*	:	Server-side operating without maid
	 */
	/** Notify server of changing IFF **/
	SERVER_CHANGE_IFF		(0x00, false),
	/** Request IFF values to server **/
	SERVER_REQUEST_IFF		(0x01, false),
	/** Ask server to save IFF **/
	SERVER_SAVE_IFF			(0x02, false),
	/** Respond to SERVER_REQUEST_IFF. Tell IFF values. **/
	CLIENT_RESPOND_IFF		(0x03, false),

	/** Request first updating of inventory to server. **/
	SERVER_UPDATE_SLOTS		(0x10,  true),
	/** Request ExpBoost value **/
	SERVER_REQUEST_BOOST	(0x11,  true),
	/** Notify onDeath to Client **/
	CLIENT_ONDEATH			(0x12,  true),

	/** Ask server to tell model name **/
	SERVER_REQUEST_MODEL	(0x20,  true),
	/** Make a maid swing arms **/
	CLIENT_SWINGARM			(0x21,  true),
	/** Make all clients play sound **/
	CLIENT_PLAY_SOUND		(0x22,  true),

	/** Synchronize model name **/
	SYNC_MODEL				(0x23,  true),
	SYNC_ARMORFLAG			(0x24,  true),
	SYNC_EXPBOOST			(0x25,  true),

	/** Notify server of using dye. Decrements dye powder. **/
	SERVER_DECREMENT_DYE	(0x30, false);
	
	public byte modeByte;
	public boolean withEntity;
	
	private EnumPacketMode(int pByte, boolean pBool) {
		modeByte = (byte) pByte;
		withEntity = pBool;
	}
	
	public static EnumPacketMode getEnumPacketMode(byte pByte) {
		for (EnumPacketMode sMode : values()) {
			if (sMode.modeByte == pByte) {
				return sMode;
			}
		}
		return null;
	}

}
