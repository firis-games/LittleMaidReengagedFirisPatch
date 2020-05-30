package firis.lmlib.api.constant;

/**
 * リトルメイドのボイス一覧
 * @author firis-games
 *
 */
public enum EnumSound {

	//Deid Voice. Null is no Voice
	DEATH(0x100,			"death",							"entity.ghast.death"),
	//Attack Voice. Null is no Voice
	ATTACK(0x110,			"attack",						"entity.ghast.shoot"),
	//Attack Bloodsucker Voice. Null is no Voice
	ATTACK_BLOODSUCK(0x111, "attack_bloodsuck",			""),
	//Laughter Voice. Null is no Voice
	LAUGHTER(0x120,			"laughter",						""),
	//shoot Voice. Null is no Voice
	SHOOT(0x130,			"shoot",						"entity.ghast.shoot"),
	//burst shoot Voice. Null is no Voice
	SHOOT_BURST(0x131,		"shoot_burst",					"entity.ghast.shoot"),
	//Adopt a fire Voice. Null is no Voice
	SIGHTING(0x140,			"sighting",					""),
	//Healing Voice. Null is no Voice
	HEALING(0x150,			"healing",						""),
	//Healing with potion Voice. Null is no Voice
	HEALING_POTION(0x151, 	"healing_potion",			""),
	//Enable TNT-D Voice. Null is no Voice
	TNT_D(0x160, 			"TNT_D",					""),
	//Eat Sugar Voice. Null is no Voice
	EAT_SUGAR(0x200, 		"eatSugar",					""),
	//Eat Sugar to MAX healing Voice. Null is no Voice
	EAT_SUGAR_MAXPOWER(0x201,"eatSugar_MaxPower",	""),
	//Get Cake Voice. Null is no Voice
	CONTRACT(0x210,			"getCake",						""),
	//Recontract Voice. Null is no Voice
	RECONTRACT(0x211,		"Recontract",					""),
	//Add Fuel Voice. Null is no Voice
	ADD_FUEL(0x220,			"addFuel",						""),
	//Cooking Start Voice. Null is no Voice
	COOKING_START(0x221,	"cookingStart",				""),
	//Cooking Over Voice. Null is no Voice
	COOKING_OVER(0x222,		"cookingOver",					""),
	//Installation Voice. Null is no Voice
	INSTALLATION(0x230,		"installation",					""),
	//Farming ground Voice. Null is no Voice
	FARMER_FARM(0x240,		"farmer_farm",				""),
	//Planting seed Voice. Null is no Voice
	FARMER_PLANT(0x241,		"farmer_plant",				""),
	//Harvesting crops Voice. Null is no Voice
	FARMER_HARVEST(0x242,	"farmer_harvest",				""),
	//Collecting snow Voice. Null is no Voice
	COLLECT_SNOW(0x250,		"collect_snow",				""),
	
	//Dameged Voice. Null is no Voice
	HURT(0x300,				"hurt",						"entity.ghast.scream"),
	//Dameged Voice from snowball. Null is no Voice
	HURT_SNOW(0x301,		"hurt_snow",		""),
	//Dameged Voice from fire. Null is no Voice
	HURT_FIRE(0x302,		"hurt_fire",			""),
	//Dameged Voice on Guard. Null is no Voice
	HURT_GUARD(0x303,		"hurt_guard",				"entity.blaze.hurt"),
	//Dameged Voice from Fall. Null is no Voice
	HURT_FALL(0x304,		"hurt_fall",			""),
	//No Dameged Voice. Null is no Voice
	HURT_NODAMEGE(0x309,	"hurt_nodamege",					"entity.blaze.hurt"),

	//Find target Normal Voice. Null is no Voice
	FIND_TARGET_N(0x400,	"findTarget_N",			""),
	//Find target Bloodsuck Voice. Null is no Voice
	FIND_TARGET_B(0x401,	"findTarget_B",		""),
	//Find target Item Voice. Null is no Voice
	FIND_TARGET_I(0x402,	"findTarget_I",				""),
	//Find target Darkness Voice. Null is no Voice
	FIND_TARGET_D(0x403,	"findTarget_D",			""),

	//Living Voice(Default) in Daytime. Null is no Voice
	LIVING_DAYTIME(0x500,	"living_daytime",	"entity.ghast.ambient"),
	//Living Voice in Mornig. Null is no Voice
	LIVING_MORNING(0x501,	"living_morning",				""),
	//Living Voice in Night. Null is no Voice
	LIVING_NIGHT(0x502,		"living_night",				""),
	//Living Voice at Whine. Null is no Voice
	LIVING_WHINE(0x503,		"living_whine",				""),
	//Living Voice at Rain. Null is no Voice
	LIVING_RAIN(0x504,		"living_rain",				""),
	//Living Voice at Snow. Null is no Voice
	LIVING_SNOW(0x505,		"living_snow",				""),
	//Living Voice at Cold. Null is no Voice
	LIVING_COLD(0x506,		"living_cold",				""),
	//Living Voice at Hot. Null is no Voice
	LIVING_HOT(0x507,		"living_hot",				""),
	
	//Goodmorning Voice. Null is no Voice
	GOODMORNING(0x551,		"goodmorning",					"entity.wolf.howl"),
	//Goodnight Voice. Null is no Voice
	GOODNIGHT(0x561,		"goodnight",					"entity.ghast.warn"),
	
	NULL(0x000, "Null", null);
	
	private final int id;
	private final String voiceId;
	private final String defaultVoice;
	
	private EnumSound(int id, String voiceId, String defaultVoice) {
		this.id = id;
		this.voiceId = voiceId;
		this.defaultVoice = defaultVoice;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getVoiceId() {
		return this.voiceId;
	}
	
	public String getDefaultVoice() {
		return this.defaultVoice;
	}

	/**
	 * 指定されたインデックスのEnumSoundを返す。
	 */
	public static EnumSound getEnumSound(int id) {
		for (EnumSound le : EnumSound.values()) {
			if (le.id == id) {
				return le;
			}
		}
		return NULL;
	}
}
