package net.blacklab.lmr.config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

/**
 * LittleMaidReengaged設定
 *
 */
public class LMRConfig {
	
	public static int cfg_spawnWeight = 5;
	public static int cfg_spawnLimit = 20;
	public static int cfg_minGroupSize = 1;
	public static int cfg_maxGroupSize = 3;
	public static boolean cfg_canDespawn = false;

	public static boolean cfg_PrintDebugMessage = false;
	public static boolean cfg_DeathMessage = true;
	public static boolean cfg_Dominant = false;

	// アルファブレンド
	public static boolean cfg_isModelAlphaBlend = false;
	
	// 野生テクスチャ
	public static boolean cfg_isFixedWildMaid = false;

	// VoiceRate
	public static float cfg_voiceRate = 0.2f;
	
	// デフォルトボイス
	public static boolean cfg_default_voice = true;
	
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
	public static boolean cfg_custom_spawn = false;
	
	/** カスタムスポーン バイオームID or バイオーム名 */
	public static List<String> cfg_spawn_biomes = null;
	
	/** メイドミルク */
	public static boolean cfg_custom_maid_milk = false;
	
	/** 騎乗高さ調整 */
	public static float cfg_custom_riding_height_adjustment = 0.0F;
	
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
	
	///** メイドアバター */
	//public static boolean cfg_lmabatar_maid_avatar = false;
	
	///** ファイルローダー機能のキャッシュ機能のON/OFF設定 */
	//public static boolean cfg_loader_is_cache = true;
	
	///** sounds.jsonファイルの出力設定 */
	//public static boolean cfg_loader_output_sounds_json = false;
	
	///** テクスチャのリソースパックロードモード設定 */
	//public static boolean cfg_loader_texture_load_from_resoucepack = false;
	
	///** LittleMaidAvatarに登録するLayer設定 */
	//public static List<String> cfg_lmavatar_include_layer = null;
	
	/** メイドスポーンエッグのレシピ */
	public static boolean cfg_general_recipe_maid_spawn_egg = true;
	
	/** 契約書のレシピ */
	public static boolean cfg_general_recipe_maid_contract = false;
	
	/** 仲介人村人 */
	public static boolean cfg_general_villager_maid_broker = true;
	
	/** 村人の交換レート */
	public static int cfg_general_villager_trade_rate = 1;
	
	/** シュガーボックス範囲 */
	public static int cfg_general_sugar_box_range = 3;
	
	/** メイド指揮棒範囲 */
	public static int cfg_general_maid_stick_range = 10;
	
	/** 染料によるメイドさんの色変更機能 */
	public static boolean cfg_lm_change_maid_color_dye = false;
	
	/** ボーナスチェストへのアイテム追加 */
	public static boolean cfg_general_bonus_chest_add_item = true;
	
	/** 試験機能 ******************************/
	/** 水上歩行術 */
	public static boolean cfg_test_water_walking  = true;
	
	/**
	 * Config初期化
	 */
	public static void init(File configFile) {
		
		// Config
		Configuration cfg = new Configuration(configFile);
		cfg.load();
		
		//各設定の初期化
		initGeneral(cfg);
		initLittleMaid(cfg);
		initJob(cfg);
		initSpawn(cfg);
//		initLMAvatar(cfg);
		initDecoration(cfg);
		initCollaboration(cfg);
//		initLoader(cfg);
		initDevelop(cfg);
		
		cfg.save();
	}
	
	protected static final String GROUP_GENERAL = "00_GENERAL";
	
	protected static final String GROUP_LITTLE_MAID = "01_LITTLE_MAID";
	
	protected static final String GROUP_JOB = "02_JOB";
	
	protected static final String GROUP_SPAWN = "03_SPAWN";
	
	protected static final String GROUP_DECORATION = "04_DECORATION";
	
	protected static final String GROUP_AVATAR = "05_LittleMaidAvatar";

	protected static final String GROUP_COLLABORATION = "06_ModCollaboration";
	
	//protected static final String GROUP_LOADER = "ex_LOADER";
	
	protected static final String GROUP_DEVELOPER = "ex_DEVELOPER";
	
	/**
	 * Mod本体の設定
	 * @param cfg
	 */
	protected static void initGeneral(Configuration cfg) {
		
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_GENERAL, "Modに含まれる各機能の設定ができます。");
		
		//スポーンエッグレシピ
		cfg_general_recipe_maid_spawn_egg = cfg.getBoolean("Recipe.SpawnEgg", GROUP_GENERAL, true,
				"お手製スポーンエッグのレシピを有効化できます。");
		
		//契約書レシピ
		cfg_general_recipe_maid_contract = cfg.getBoolean("Recipe.MaidContract", GROUP_GENERAL, false,
				"メイドさんの契約書のレシピを有効化できます。");
		
		//村人追加
		cfg_general_villager_maid_broker = cfg.getBoolean("Villager.MaidBroker", GROUP_GENERAL, true,
				"仲介人村人を有効化できます。");
		
		//トレードレート
		cfg_general_villager_trade_rate = cfg.getInt("Villager.MaidContractTradeRate", GROUP_GENERAL, 2, 1, 3,
				"メイドさんの契約書の取引レートを変更します。1:easy[4-12], 2:normal[8-32] 3:hard[24-64]");
		
		//シュガーボックス範囲
		cfg_general_sugar_box_range = cfg.getInt("Item.SugarBox.Range", GROUP_GENERAL, 3, 1, 10,
				"シュガーボックスの有効範囲チャンクの半径を設定します。例：3を設定した場合はシュガーボックスを中心に7×7チャンクの範囲");
		
		//メイド指揮棒
		cfg_general_maid_stick_range = cfg.getInt("Item.MaidStick.Range", GROUP_GENERAL, 10, 1, 30,
				"ご主人様を中心の有効範囲ブロック数の半径を設定します。");
		
		//ボーナスチェストへのアイテム追加
		cfg_general_bonus_chest_add_item = cfg.getBoolean("BounusChest.Add.LMRItem", GROUP_GENERAL, true,
				"ボーナスチェストへLMRアイテムの追加を設定します。");
		
	}
	
	/**
	 * メイドさん本体に関連する設定
	 */
	protected static void initLittleMaid(Configuration cfg) {
		
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_LITTLE_MAID, "メイドさんに関連する各機能の設定ができます。");
		
		//指定IDを砂糖として認識する
		String[] sugarItemIds = new String[] {"minecraft:sugar*1"};
		cfg_sugar_item_ids = Arrays.asList(cfg.getStringList("Favorite.Sugar", GROUP_LITTLE_MAID, sugarItemIds, 
				"砂糖と同じ扱いとなるアイテムIDを設定できます。[アイテムID]*[数値]で回復量を設定できます。"));
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
		cfg_cake_item_ids = Arrays.asList(cfg.getStringList("Favorite.Cake", GROUP_LITTLE_MAID, cakeItemIds, 
				"ケーキと同じ扱いとなるアイテムIDを設定できます。"));
		
		//メイドミルクの設定
		cfg_custom_maid_milk = cfg.getBoolean("MaidMilk", GROUP_LITTLE_MAID, false,
				"メイドミルク機能を有効化できます。");
		
		//メイドミルク表示設定
		cfg_secret_maid_milk = cfg.getBoolean("MaidMilk.Label", GROUP_LITTLE_MAID, false,
				"ミルクバケツのラベル機能を有効化できます。");

		//デフォルトメイドさんの表示
		cfg_secret_maid_milk_producer_default = cfg.getString("MaidMilk.Label_DefaultMaidName", GROUP_LITTLE_MAID, 
				"メイドさん",
				"名無しメイドさんのミルクバケツのラベルを設定できます。");

		//メイドミルク表示のラベル設定
		cfg_secret_maid_milk_producer_label = cfg.getString("MaidMilk.Label_DisplayLabel", GROUP_LITTLE_MAID, 
				"%s印のミルク",
				"デフォルトメイドさんのミルクバケツのラベルを設定できます。");
		
		//メイドの土産設定
		cfg_isResurrection = cfg.getBoolean("isResurrection", GROUP_LITTLE_MAID, true,
				"デッドロスト回避機能を有効化できます。有効化するとメイドさん死亡時にメイドの土産をドロップします。");
		
		//騎乗メイドの高さ設定
		cfg_custom_riding_height_adjustment = cfg.getFloat("RidingMode.HeightAdjustment", GROUP_LITTLE_MAID, 0.0F, -2.0F, 2.0F, 
				"疑似騎乗モードのメイドさんの描画位置（高さ）を設定します。LMアバターが有効化されている場合は-0.5されます。");
		
		//指定IDをアニマルメイドの変更対象として認識する
		String[] animalMaidMobIds = new String[] {"minecraft:rabbit"};
		cfg_custom_animal_maid_mob_ids = Arrays.asList(cfg.getStringList("AnimalMaidMobs", GROUP_LITTLE_MAID, animalMaidMobIds,
				"アニマルメイドさんの変身対象となる動物のエンティティIDを設定できます。"));
		
		//死亡時のメッセージ表示
		cfg_DeathMessage = cfg.getBoolean("deathMessage", GROUP_LITTLE_MAID, true,
				"メイドさん死亡時のメッセージ表示を設定できます。");
		
		//染料によるメイドさんの色変更機能
		cfg_lm_change_maid_color_dye = cfg.getBoolean("ChangeMaidColorDye", GROUP_LITTLE_MAID, false,
				"染料を与えるとメイドさんの色を変更できる機能を有効化できます。");
	}
	
	/**
	 * メイドさんのお仕事に関連する設定
	 * @param cfg
	 */
	protected static void initJob(Configuration cfg) {
		
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_JOB, "メイドさんにお仕事に関連する各機能の設定ができます。");
		
		//指定IDを矢として認識する
		String[] arrowItemIds = new String[] {"minecraft:arrow"};
		cfg_ac_arrow_item_ids = Arrays.asList(
				cfg.getStringList("Archer.Arrow", GROUP_JOB, arrowItemIds, 
						"弓兵メイドさんが矢と同じ扱いをするアイテムIDを設定できます。")
		);
		
		//指定IDを原木として認識する
		String[] logBlockIds = new String[] {"minecraft:log"};
		cfg_lj_log_block_ids = Arrays.asList(
				cfg.getStringList("Lumberjack.Log", GROUP_JOB, logBlockIds, 
						"木こりメイドさんが原木と同じ扱いをするアイテムIDを設定できます。")
		);
		
		//指定IDを葉ブロックとして認識する
		String[] leafBlockIds = new String[] {"minecraft:leaves"};
		cfg_lj_leaf_block_ids = Arrays.asList(
				cfg.getStringList("Lumberjack.Leaf", GROUP_JOB, leafBlockIds, 
						"木こりメイドさんが葉ブロックと同じ扱いをするアイテムIDを設定できます。")
		);
		
		//指定IDを苗木として認識する
		String[] saplingItemIds = new String[] {"minecraft:sapling"};
		cfg_lj_sapling_item_ids = Arrays.asList(
				cfg.getStringList("Lumberjack.Sapling", GROUP_JOB, saplingItemIds, 
						"木こりメイドさんが苗木ブロックと同じ扱いをするアイテムIDを設定できます。")
		);
		
		//指定IDを調理しない
		String[] noCookingItemIds = new String[] {"minecraft:iron_sword", "minecraft:golden_sword"};
		cfg_cock_no_cooking_item_ids = Arrays.asList(cfg.getStringList("Cook.NotCookingItemIds", GROUP_JOB, noCookingItemIds,
				"コックメイドさんが料理対象としないアイテムIDを設定できます。"));
		
		//triggerの設定化
		String[] triggerItemIds = new String[] {
				"archer:minecraft:bow",
				"fencer:minecraft:iron_sword",
				"farmer:minecraft:iron_hoe",
				"lumberjack:minecraft:iron_axe"
		};
		cfg_trigger_item_ids = Arrays.asList(
				cfg.getStringList("Trigger.ItemIds", GROUP_JOB, triggerItemIds, 
						"メイドさんの転職用のトリガーアイテムを設定できます。 [maidmode]:[modid]:[itemid]")
		);
		
	}
	
	/**
	 * メイドさんのスポーンに関連する設定
	 * @param cfg
	 */
	protected static void initSpawn(Configuration cfg) {
		
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_SPAWN, "メイドさんにスポーン条件に関連する設定ができます。");
		
		//デスポーン設定
		cfg_canDespawn = cfg.getBoolean("WildLittleMaidDespawn", GROUP_SPAWN, false,
				"野生のメイドさんがデスポーンするかを設定できます。");
		
		//スポーンする群れの最大サイズ
		cfg_maxGroupSize = cfg.getInt("SpawnMaxGroupSize", GROUP_SPAWN, 2, 1, 20,
				"自然スポーン時の最大沸き数を設定できます。");
		
		//スポーンする群れの最小サイズ
		cfg_minGroupSize = cfg.getInt("SpawnMinGroupSize", GROUP_SPAWN, 1, 1, 20,
				"自然スポーン時の最小沸き数を設定できます。");
		
		//自然スポーンのリミット
		cfg_spawnLimit = cfg.getInt("SpawnLimit", GROUP_SPAWN, 20, 1, 30,
				"自然スポーン時のスポーン上限を設定できます。読込範囲に指定数以上メイドさんが存在する場合に自然スポーンが発生しなくなります。");
		
		//スポーンのウェイト
		cfg_spawnWeight = cfg.getInt("SpawnWeight", GROUP_SPAWN, 5, 0, 10,
				"自然スポーン時の沸きやすさを設定できます。0の場合は自然スポーンが停止します。");
		
		//カスタムスポーン有効設定
		cfg_custom_spawn = cfg.getBoolean("CustomBiomeSpwan", GROUP_SPAWN, false,
				"カスタムスポーンを有効化します。カスタムスポーンを有効化するとデフォルトのバイオームでスポーンしなくなります。");
		
		//スポーンバイオーム設定
		String[] biomeList = new String[] {
				"Plains",
				"minecraft:plains"};
		cfg_spawn_biomes = Arrays.asList(
				cfg.getStringList("CustomBiomeSpwan.BiomeList", GROUP_SPAWN, biomeList, 
						"カスタムスポーン有効時にスポーンするバイオーム名 or バイオームIDを設定できます。")
		);
	}
	
	/**
	 * メイドさんの見た目に関連する設定
	 * @param cfg
	 */
	protected static void initDecoration(Configuration cfg) {
		
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_DECORATION, "メイドさんの見た目に関連する設定ができます。");
		
		//野生メイドさんのスポーン設定
		cfg_isFixedWildMaid = cfg.getBoolean("WildLittleMaid.DefaultOnly", GROUP_DECORATION, false,
				"スポーン時の野生メイドさんのテクスチャをデフォルトのみにするか設定できます。");
		
		//透過設定
		cfg_isModelAlphaBlend = cfg.getBoolean("isModelAlphaBlend", GROUP_DECORATION, true,
				"グラフィックが無力すぎてアルファブレンドテクスチャを描画できない場合は、「false」にしてください。");
		
		//メイドさんのランダムVoiceRate
		cfg_voiceRate = cfg.getFloat("VoiceRate", GROUP_DECORATION, 0.2F, 0.0F, 1.0F,
				"メイドさんの通常おしゃべりのレートを設定できます。[1.0 = 100%]");
		
		//メイドさんのデフォルトボイス設定
		cfg_default_voice = cfg.getBoolean("DefaultVoice", GROUP_DECORATION, true,
				"メイドさんのデフォルトボイスを有効化します。有効化するとガストの啼き声で啼きます。");
		
	}
	
	/**
	 * LMアバターの設定
	 * @param cfg
	 */
//	protected static void initLMAvatar(Configuration cfg) {
//		
//		//グループコメント
//		cfg.addCustomCategoryComment(GROUP_AVATAR, "リトルメイドアバターに関連する設定ができます。");
//
//		//メイドアバター機能
//		cfg_lmabatar_maid_avatar = cfg.getBoolean("LittleMaidAvatar", GROUP_AVATAR, false,
//				"プレイヤーの見た目をメイドさんにするLittleMaidAvatar機能を有効化します。");
//		
//		//指定されたIDのLayerは登録する
//		String[] lma_include_layer = new String[] {"LayerSlashBlade"};
//		cfg_lmavatar_include_layer = Arrays.asList(cfg.getStringList("LittleMaidAvatar.IncludeLayer", GROUP_AVATAR, lma_include_layer, 
//				"指定された文字を含むLayerクラスをLittleMaidAvatarに追加します。"));
//		
//	}
	
	/**
	 * Mod連携
	 * @param cfg
	 */
	protected static void initCollaboration(Configuration cfg) {
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_COLLABORATION, "Mod連携");
		
		//Hwyla連携
		cfg_plugin_hwyla = cfg.getBoolean("Hwyla", GROUP_COLLABORATION, true,
				"Hwyla連携を有効化します。");
	}
	
	
//	/**
//	 * メイドさんの見た目に関連する設定
//	 * @param cfg
//	 */
//	protected static void initLoader(Configuration cfg) {
//		//グループコメント
//		cfg.addCustomCategoryComment(GROUP_LOADER, "LittleMaidLoader Setting");
//		
//		//ファイルローダー機能のキャッシュ機能設定
//		cfg_loader_is_cache = cfg.getBoolean("EnableFileLoaderCache", GROUP_LOADER, false,
//				"Enable FileLoader Caching.");
//		
//		//sounds.jsonファイルの出力設定
//		cfg_loader_output_sounds_json = cfg.getBoolean("OutputSoundsJson", GROUP_LOADER, false,
//				"Output sounds.json.");
//		
//		//テクスチャのリソースパックロードの設定
//		cfg_loader_texture_load_from_resoucepack = cfg.getBoolean("EnableTextureLoadResourcepack", GROUP_LOADER, false,
//				"Developer mode setting. Reads a texture from a resourcepack.");
//		
//	}
	
	/**
	 * テスト用機能の設定
	 */
	protected static void initDevelop(Configuration cfg) {
		
		//グループコメント
		cfg.addCustomCategoryComment(GROUP_DEVELOPER, "Developer Mode Setting");
		
		cfg_PrintDebugMessage = cfg.getBoolean("PrintDebugMessage", GROUP_DEVELOPER, false,
				"Print debug logs. Recommended to keep default.");
		
		//みんなのメイドさん
		cfg_cstm_everyones_maid = cfg.getBoolean("Test.EveryonesMaid", GROUP_DEVELOPER, false,
				"Maid listening to instructions other than the master.");
		
		//水上歩行術
		cfg_test_water_walking = cfg.getBoolean("Test.MaidWaterWalking", GROUP_DEVELOPER, false,
				"Enable Maid's water walking technique.");
		
		//開発者用テストモジュール有効化設定
		cfg_developer_test_module = cfg.getBoolean("Test.Module", GROUP_DEVELOPER, false,
				"developer only.");
	}
	
	/**
	 * 現在未使用の設定
	 * 一定期間後に削除する
	 * @param cfg
	 */
	protected static void initDelete(Configuration cfg) {
		cfg_Dominant = cfg.getBoolean("Dominant", "Advanced", false,
				"Recommended to keep 'false'. If true, non-vanilla check is used for maid spawning.");		
	}
	
	/**
	 * LMアバターロード判定
	 */
	private static Boolean loadedlittleMaidAvatar = null;
	public static boolean isLoadedLittleMaidAvatar() {
		if (loadedlittleMaidAvatar == null) {
			loadedlittleMaidAvatar = Loader.isModLoaded("lmavatar");
		}
		return loadedlittleMaidAvatar;
	}
	
}
