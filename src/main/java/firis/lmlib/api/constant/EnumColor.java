package firis.lmlib.api.constant;

/**
 * リトルメイドの色情報
 * 染料のメタデータと逆順となっている
 * @author firis-games
 *
 */
public enum EnumColor {
	
	WHITE(0),
	ORANGE(1),
	MAGENTA(2),
	LIGHT_BLUE(3),
	YELLOW(4),
	LIME(5),
	PINK(6),
	GRAY(7),
	LIGHT_GRAY(8),
	CYAN(9),
	PURPLE(10),
	BLUE(11),
	BROWN(12),
	GREEN(13),
	RED(14),
	BLACK(15);
	
	private EnumColor(int color) {
		this.color = color;
	}
	
	private final int color;
	
	public int getColor() {
		return this.color;
	}
	
	/**
	 * カラー番号からEnumColorを取得する
	 * @param color
	 * @return
	 */
	public static EnumColor getColorFromInt(int color) {
		for (EnumColor enumColor : EnumColor.values()) {
			if (enumColor.getColor() == color) return enumColor;
		}
		return EnumColor.WHITE;
	}
	
	/**
	 * カラー番号が含まれるかの確認
	 * @return
	 */
	public static boolean hasColor(int color) {
		for (EnumColor enumColor : EnumColor.values()) {
			if (enumColor.getColor() == color) return true;
		}
		return false;
	}

}
