package net.blacklab.lmr.util;

/**
 * メイドさんの固定モーション設定
 */
public enum EnumMaidMotion {
	NONE(0),
	DEFAULT(1),
	WAIT(2),
	LOOKSUGAR(3),
	SNEAK(4),
	SIT(5),
	BOW(6);
	
	private EnumMaidMotion(int id) {
		this.id = id;
	}
	
	private int id;
	public int getId() {
		return this.id;
	}
	
	//次のモーションを取得する
	public EnumMaidMotion next() {
		EnumMaidMotion rtn = EnumMaidMotion.NONE;
		boolean isNext = false;
		for (EnumMaidMotion value : EnumMaidMotion.values()) {
			if (isNext) {
				rtn = value;
				break;
			}
			if(value.getId() == this.getId()) {
				isNext = true;
				continue;
			}
		}
		return rtn;
	}
	
	//IdからMaidMotion取得
	public static EnumMaidMotion getMaidMotionFromId(int id) {
		EnumMaidMotion ret = EnumMaidMotion.NONE;
		for (EnumMaidMotion value : EnumMaidMotion.values()) {
			if(value.getId() == id) {
				ret = value;
				break;
			}
		}
		return ret;
	}
}