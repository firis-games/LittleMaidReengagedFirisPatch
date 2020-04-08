package net.blacklab.lmr.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

/**
 * LittleMaidReengaged設定
 *
 */
public class LMRConfig {
	
	// @MLProp(info="Relative spawn weight. The lower the less common. 10=pigs. 0=off")
	public static int cfg_spawnWeight = 5;
	// @MLProp(info="Maximum spawn count in the World.")
	public static int cfg_spawnLimit = 20;
	// @MLProp(info="Minimum spawn group count.")
	public static int cfg_minGroupSize = 1;
	// @MLProp(info="Maximum spawn group count.")
	public static int cfg_maxGroupSize = 3;
	// @MLProp(info="It will despawn, if it lets things go. ")
	public static boolean cfg_canDespawn = false;

	// @MLProp(info="Print Debug Massages.")
	public static boolean cfg_PrintDebugMessage = false;
	// @MLProp(info="Print Death Massages.")
	public static boolean cfg_DeathMessage = true;
	// @MLProp(info="Spawn Anywhere.")
	public static boolean cfg_Dominant = false;
	// アルファブレンド
	public static boolean cfg_isModelAlphaBlend = false;
	
	// 野生テクスチャ
	public static boolean cfg_isFixedWildMaid = false;

	public static final float cfg_voiceRate = 0.2f;
	
	/** メイドの土産 */
	public static boolean cfg_isResurrection = true;
	
	/** 砂糖アイテムID */
	private static List<String> cfg_sugar_item_ids = null;
	
	/** 砂糖アイテムID */
	public static Map<String, Integer> cfg_sugar_item_ids_map = null;

	/** ケーキアイテムID */
	public static List<String> cfg_cake_item_ids = null;
	
	/** 原木ブロックID */
	public static List<String> cfg_lj_log_block_ids = null;

	/** 葉ブロックID */
	public static List<String> cfg_lj_leaf_block_ids = null;

	/** 苗アイテムID */
	public static List<String> cfg_lj_sapling_item_ids = null;
	
	/** デフォルトスポーン有効化設定 */
	public static boolean cfg_spawn_default_enable = true;
	
	/** カスタムスポーン バイオームID or バイオーム名 */
	public static List<String> cfg_spawn_biomes = null;
	
	/** メイドミルク */
	public static boolean cfg_custom_maid_milk = false;
	
	/** 騎乗高さ調整 */
	public static float cfg_custom_riding_height_adjustment = 0.0F;
	
	/** 弓（銃）アイテムID */
	public static List<String> cfg_ac_bow_item_ids = null;
	
	/** 矢（弾丸）アイテムID */
	public static List<String> cfg_ac_arrow_item_ids = null;
	
	/** かまどの料理対象外アイテム */
	public static List<String> cfg_cock_no_cooking_item_ids = null;
	
	/** アニマルメイド判定 */
	public static List<String> cfg_custom_animal_maid_mob_ids = null;
	
	/** hwyla連携 */
	public static boolean cfg_plugin_hwyla = true;
	
	/** trigger 判断用 アイテムID */
	public static List<String> cfg_trigger_item_ids = null;
	
	/** メイドミルクの特殊表示 */
	public static boolean cfg_secret_maid_milk = false;
	public static String cfg_secret_maid_milk_producer_default = "";
	public static String cfg_secret_maid_milk_producer_label = "";
	
	/** みんなのメイドさん */
	public static boolean cfg_cstm_everyones_maid = false;
	
	/** 開発用テストモジュールの有効化設定 */
	public static boolean cfg_developer_test_module = false;
	
	/** 試験中の追加機能 メイドアバター */
	public static boolean cfg_prottype_maid_avatar = false;
	
	/** ファイルローダー機能のキャッシュ機能のON/OFF設定 */
	public static boolean cfg_loader_is_cache = true;
	
	/**
	 * Config初期化
	 */
	public static void init(File configFile) {
		
		// Config
		Configuration cfg = new Configuration(configFile);
		cfg.load();

		cfg_canDespawn = cfg.getBoolean("canDespawn", "General", false,
				"Set whether non-contracted maids can despawn.");
		
		cfg_DeathMessage = cfg.getBoolean("deathMessage", "General", true,
				"Set whether prints death message of maids.");
		
		cfg_Dominant = cfg.getBoolean("Dominant", "Advanced", false,
				"Recommended to keep 'false'. If true, non-vanilla check is used for maid spawning.");
		
		cfg_maxGroupSize = cfg.getInt("maxGroupSize", "Advanced", 3, 1, 20,
				"Settings for maid spawning. Recommended to keep default.");
		
		cfg_minGroupSize = cfg.getInt("minGroupSize", "Advanced", 1, 1, 20,
				"Settings for maid spawning. Recommended to keep default.");
		
		cfg_spawnLimit = cfg.getInt("spawnLimit", "Advanced", 20, 1, 30,
				"Settings for maid spawning. Recommended to keep default.");
		
		cfg_spawnWeight = cfg.getInt("spawnWeight", "Advanced", 5, 1, 9,
				"Settings for maid spawning. Recommended to keep default.");
		
		cfg_PrintDebugMessage = cfg.getBoolean("PrintDebugMessage", "Advanced", false,
				"Print debug logs. Recommended to keep default.");
		
		cfg_isModelAlphaBlend = cfg.getBoolean("isModelAlphaBlend", "Advanced", true,
				"If your graphics SHOULD be too powerless to draw alpha-blend textures, turn this 'false'.");
		
		cfg_isFixedWildMaid = cfg.getBoolean("isFixedWildMaid", "General", false,
				"If 'true', only default-texture maid spawns. You can still change their textures after employing.");

		//メイドの土産設定
		cfg_isResurrection = cfg.getBoolean("isResurrection", "General", true,
				"If 'true', Drops a resurrection item when a maid dies.");
		
		//指定IDを砂糖として認識する
		String[] sugarItemIds = new String[] {"minecraft:sugar"};
		cfg_sugar_item_ids = Arrays.asList(cfg.getStringList("Sugar", "Custom", sugarItemIds, "Set the item ID to be treated the same as maid sugar."));
		
		//Map形式へ変換する
		cfg_sugar_item_ids_map = new HashMap<>();
		for (String sugarItem : cfg_sugar_item_ids) {
			String[] works = sugarItem.split("\\*");
			if (works.length == 1 || works.length == 2) {
				String item = works[0];
				Integer heal = 1;
				if (works.length == 2) {
					try {
						heal = Integer.parseInt(works[1]);
					} catch (Exception e) {
					}
				}
				cfg_sugar_item_ids_map.put(item, heal);
			}
		}
		
		
		//指定IDをケーキとして認識する
		String[] cakeItemIds = new String[] {"minecraft:cake"};
		cfg_cake_item_ids = Arrays.asList(cfg.getStringList("Cake", "Custom", cakeItemIds, "Set the item ID to be treated the same as maid cake."));
		
		//木こり設定
		initLumberjack(cfg);
		
		//メイドスポーン設定
		initSpawnBiome(cfg);
		
		//メイドミルクの設定
		cfg_custom_maid_milk = cfg.getBoolean("MaidMilk", "Custom", false,
				"Enable maid milk.");
		
		//騎乗メイドの高さ設定
		cfg_custom_riding_height_adjustment = cfg.getFloat("RidingHeightAdjustment", "Custom", 0.0F, -2.0F, 2.0F, 
				"Riding mode height adjustment.Standard setting of PFLM is -0.5.");
		
		//指定IDをアニマルメイドの変更対象として認識する
		String[] animalMaidMobIds = new String[] {"minecraft:rabbit"};
		cfg_custom_animal_maid_mob_ids = Arrays.asList(cfg.getStringList("AnimalMaidMobs", "Custom", animalMaidMobIds,
				"Set a Mob ID that can be transformed into an Animal Maid."));

		//アーチャー設定
		initArcher(cfg);
		
		//triggerの設定化
		String[] triggerItemIds = new String[] {
				"archer:minecraft:bow",
				"fencer:minecraft:iron_sword",
				"farmer:minecraft:iron_hoe",
				"lumberjack:minecraft:iron_axe"
		};
		cfg_trigger_item_ids = Arrays.asList(
				cfg.getStringList("ItemIds", "Trigger", triggerItemIds, "Set the item ID of the trigger used for maid mode judgment.ex [maidmode]:[modid]:[itemid]")
		);
		
		//みんなのメイドさん
		cfg_cstm_everyones_maid = cfg.getBoolean("EveryonesMaid", "Custom", false,
				"Maid listening to instructions other than the master.");
		
		//試験機能
		initTest(cfg);
		
		//Hwyla連携
		cfg_plugin_hwyla = cfg.getBoolean("Hwyla", "Plugin", true,
				"Enable Hwyla　integration.");
		
		//秘密機能
		initSecret(cfg);
		
		//開発者用テストモジュール有効化設定
		cfg_developer_test_module = cfg.getBoolean("TestModule", "Develop", false,
				"developer only.");
		
		//指定IDを調理しない
		String[] noCookingItemIds = new String[] {"minecraft:iron_sword", "minecraft:golden_sword"};
		cfg_cock_no_cooking_item_ids = Arrays.asList(cfg.getStringList("NotCookingItemIds", "Cock", noCookingItemIds,
				"Set the item ID that does not cook in the furnace."));
		
		
		//ファイルローダー機能のキャッシュ機能設定
		cfg_loader_is_cache = cfg.getBoolean("EnableFileLoaderCache", "Loader", true,
				"Enable FileLoader Caching.");
		
		cfg.save();
	}
	
	/**
	 * 木こり用設定
	 */
	public static void initLumberjack(Configuration cfg) {
		
		//指定IDを原木として認識する
		String[] logBlockIds = new String[] {"minecraft:log"};
		cfg_lj_log_block_ids = Arrays.asList(
				cfg.getStringList("Log", "Lumberjack", logBlockIds, "Set the block ID to be processed in the same way as the log.")
		);
		
		//指定IDを葉ブロックとして認識する
		String[] leafBlockIds = new String[] {"minecraft:leaves"};
		cfg_lj_leaf_block_ids = Arrays.asList(
				cfg.getStringList("Leaf", "Lumberjack", leafBlockIds, "Set the block ID to be processed in the same way as the leaf.")
		);
		
		//指定IDを苗木として認識する
		String[] saplingItemIds = new String[] {"minecraft:sapling"};
		cfg_lj_sapling_item_ids = Arrays.asList(
				cfg.getStringList("Sapling", "Lumberjack", saplingItemIds, "Set the item ID to be processed in the same way as the sapling.")
		);
	}
	
	/**
	 * カスタムスポーン設定
	 */
	public static void initSpawnBiome(Configuration cfg) {
		
		//カスタムスポーン有効設定
		cfg_spawn_default_enable = cfg.getBoolean("SpawnDefaultEnable", "Custom", true,
				"Enable the default spawn for Maid.");
		
		//スポーンバイオーム設定
		String[] biomeList = new String[] {
				"Plains",
				"minecraft:plains"};
		cfg_spawn_biomes = Arrays.asList(
				cfg.getStringList("SpawnBiomeList", "Custom", biomeList, 
						"Setting for Maid custom spawn Biome Name or Biome Id.")
		);
		
	}
	
	
	/**
	 * アーチャー用設定
	 */
	public static void initArcher(Configuration cfg) {
		
		//指定IDを弓として認識する
		String[] bowItemIds = new String[] {"minecraft:bow"};
		cfg_ac_bow_item_ids = Arrays.asList(
				cfg.getStringList("Bow", "Archer", bowItemIds, "Set the item ID to be processed in the same way as the Bow or Gun.")
		);
		
		//指定IDを矢として認識する
		String[] arrowItemIds = new String[] {"minecraft:arrow"};
		cfg_ac_arrow_item_ids = Arrays.asList(
				cfg.getStringList("Arrow", "Archer", arrowItemIds, "Set the item ID to be processed in the same way as the arrow or bullet.")
		);
		
	}
	
	
	/** 試験機能 ******************************/
	/** 水上歩行術 */
	public static boolean cfg_test_water_walking  = true;
	
	/**
	 * 試験的に実装した機能の設定
	 */
	public static void initTest(Configuration cfg) {
		
		//水上歩行術
		cfg_test_water_walking = cfg.getBoolean("MaidWaterWalking", "Test", false,
				"Enable Maid's water walking technique.");
		
	}
	
	/****************************************/
	
	/**
	 * メイドさんの秘密機能のConfig
	 * @param cfg
	 */
	public static void initSecret(Configuration cfg) {
		
		//メイドミルク表示設定
		cfg_secret_maid_milk = cfg.getBoolean("MaidMilkLabel", "Secret", false,
				"Enable producer labeling for maid milk.");

		//デフォルトメイドさんの表示
		cfg_secret_maid_milk_producer_default = cfg.getString("MaidMilkLabel_DefaultMaidName", "Secret", "メイドさん",
				"Maid milk default littlemaid name.");

		//メイドミルク表示のラベル設定
		cfg_secret_maid_milk_producer_label = cfg.getString("MaidMilkLabel_DisplayLabel", "Secret", "%s印のミルク",
				"Maid milk producer display label.");
		
		//プロトタイプ機能
		cfg_prottype_maid_avatar = cfg.getBoolean("PlayerMaidAvatar", "xProttype", false,
				"Player looks like a LittleMaid.This is a work in progress.");
	}
	
}
