package net.blacklab.lmr.util;

public enum EnumArmorRenderParts {
	INNER_HEAD(0x10),
	INNER_CHESTPLATE(0x11),
	INNER_LEGGINS(0x12),
	INNER_BOOTS(0x13),
	OUTER_HEAD(0x20),
	OUTER_CHESTPLATE(0x21),
	OUTER_LEGGINS(0x22),
	OUTER_BOOTS(0x23);
	
	public Integer value;
	public Integer inventoryIndex;
	public Integer layerIndex;
	public Integer textureIndex;
	
	EnumArmorRenderParts(int texIndex) {
		value = texIndex;
		textureIndex = texIndex & 0x0f;
		layerIndex = (textureIndex & 0xf0) / 0x10;
		inventoryIndex = 4 - textureIndex;
	}
	
	public static EnumArmorRenderParts getEnumArmor(int layer, int tex) {
		if (layer <= 0) {
			return null;
		}
		for (EnumArmorRenderParts ea : values()) {
			if (ea.layerIndex == layer && ea.textureIndex == tex) {
				return ea;
			}
		}
		return null;
	}
}
