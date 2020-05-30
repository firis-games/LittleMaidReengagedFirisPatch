package firis.lmlib.api.constant;

public enum EnumSound {

	death(0x100,			"Deid Voice. Null is no Voice",							"entity.ghast.death"),
	attack(0x110,			"Attack Voice. Null is no Voice",						"entity.ghast.shoot"),
	attack_bloodsuck(0x111, "Attack Bloodsucker Voice. Null is no Voice",			""),
	laughter(0x120,			"Laughter Voice. Null is no Voice",						""),
	shoot(0x130,			"shoot Voice. Null is no Voice",						"entity.ghast.shoot"),
	shoot_burst(0x131,		"burst shoot Voice. Null is no Voice",					"entity.ghast.shoot"),
	sighting(0x140,			"Adopt a fire Voice. Null is no Voice",					""),
	healing(0x150,			"Healing Voice. Null is no Voice",						""),
	healing_potion(0x151, 	"Healing with potion Voice. Null is no Voice",			""),
	tnt_d(0x160, 			"Enable TNT-D Voice. Null is no Voice",					""),
	eatSugar(0x200, 		"Eat Sugar Voice. Null is no Voice",					""),
	eatSugar_MaxPower(0x201,"Eat Sugar to MAX healing Voice. Null is no Voice",		""),
	getCake(0x210, 			"Get Cake Voice. Null is no Voice",						""),
	Recontract(0x211,		"Recontract Voice. Null is no Voice",					""),
	addFuel(0x220,			"Add Fuel Voice. Null is no Voice",						""),
	cookingStart(0x221,		"Cooking Start Voice. Null is no Voice",				""),
	cookingOver(0x222,		"Cooking Over Voice. Null is no Voice",					""),
	installation(0x230,		"Installation Voice. Null is no Voice",					""),
	farmer_farm(0x240,		"Farming ground Voice. Null is no Voice",				""),
	farmer_plant(0x241,		"Planting seed Voice. Null is no Voice",				""),
	farmer_harvest(0x242,	"Harvesting crops Voice. Null is no Voice",				""),
	collect_snow(0x250,		"Collecting snow Voice. Null is no Voice",				""),

	hurt(0x300,				"Dameged Voice. Null is no Voice",						"entity.ghast.scream"),
	hurt_snow(0x301,		"Dameged Voice from snowball. Null is no Voice",		""),
	hurt_fire(0x302,		"Dameged Voice from fire. Null is no Voice",			""),
	hurt_guard(0x303,		"Dameged Voice on Guard. Null is no Voice",				"entity.blaze.hurt"),
	hurt_fall(0x304,		"Dameged Voice from Fall. Null is no Voice",			""),
	hurt_nodamege(0x309,	"No Dameged Voice. Null is no Voice",					"entity.blaze.hurt"),

	findTarget_N(0x400,		"Find target Normal Voice. Null is no Voice",			""),
	findTarget_B(0x401,		"Find target Bloodsuck Voice. Null is no Voice",		""),
	findTarget_I(0x402,		"Find target Item Voice. Null is no Voice",				""),
	findTarget_D(0x403,		"Find target Darkness Voice. Null is no Voice",			""),

	living_daytime(0x500,	"Living Voice(Default) in Daytime. Null is no Voice",	"entity.ghast.ambient"),
	living_morning(0x501,	"Living Voice in Mornig. Null is no Voice",				""),
	living_night(0x502,		"Living Voice in Night. Null is no Voice",				""),
	living_whine(0x503,		"Living Voice at Whine. Null is no Voice",				""),
	living_rain(0x504,		"Living Voice at Rain. Null is no Voice",				""),
	living_snow(0x505,		"Living Voice at Snow. Null is no Voice",				""),
	living_cold(0x506,		"Living Voice at Cold. Null is no Voice",				""),
	living_hot(0x507,		"Living Voice at Hot. Null is no Voice",				""),
	goodmorning(0x551,		"Goodmorning Voice. Null is no Voice",					"entity.wolf.howl"),
	goodnight(0x561,		"Goodnight Voice. Null is no Voice",					"entity.ghast.warn"),
	
	Null(0, "", null);
	
	public final int index;
	public final String info;
	public final String DefaultValue;
	
	private EnumSound(int findex, String finfo, String fdefault) {
		index = findex;
		info = finfo;
		DefaultValue = fdefault;
	}

	/**
	 * 指定されたインデックスのEnumSoundを返す。
	 */
	public static EnumSound getEnumSound(int pindex) {
		for (EnumSound le : EnumSound.values()) {
			if (le.index == pindex) {
				return le;
			}
		}
		return Null;
	}
}
