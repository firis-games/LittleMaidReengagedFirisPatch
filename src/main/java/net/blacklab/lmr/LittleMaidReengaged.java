package net.blacklab.lmr;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import mmmlibx.lib.MMM_TextureManager;
import net.blacklab.lib.config.ConfigList;
import net.blacklab.lib.version.Version;
import net.blacklab.lib.version.Version.VersionData;
import net.blacklab.lmr.achievements.LMMNX_Achievements;
import net.blacklab.lmr.api.mode.LMMNX_API_Farmer;
import net.blacklab.lmr.client.resource.LMMNX_OldZipTexturesLoader;
import net.blacklab.lmr.client.resource.LMM_SoundResourcePack;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.event.EventHook;
import net.blacklab.lmr.item.ItemRegisterKey;
import net.blacklab.lmr.item.ItemSpawnEgg;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.proxy.ProxyCommon;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.CommonHelper;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.manager.EntityModeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
		modid = LittleMaidReengaged.DOMAIN,
		name = "LittleMaidMobNX",
		version = LittleMaidReengaged.VERSION,
		acceptedMinecraftVersions=LittleMaidReengaged.ACCEPTED_MCVERSION)
public class LittleMaidReengaged {

	public static final String DOMAIN = "lmreengaged";
	public static final String VERSION = "6.0.6";
	public static final String ACCEPTED_MCVERSION = "1.8.9";
	public static final int VERSION_CODE = 1;

	public static final VersionData currentVersion = new VersionData(VERSION_CODE, VERSION, VERSION);
	public static VersionData latestVersion = new VersionData(1, "6.0.1", "6.0.1");

	/*
	 * public static String[] cfg_comment = {
	 * "spawnWeight = Relative spawn weight. The lower the less common. 10=pigs. 0=off"
	 * , "spawnLimit = Maximum spawn count in the World.",
	 * "minGroupSize = Minimum spawn group count.",
	 * "maxGroupSize = Maximum spawn group count.",
	 * "canDespawn = It will despawn, if it lets things go. ",
	 * "checkOwnerName = At local, make sure the name of the owner. ",
	 * "antiDoppelganger = Not to survive the doppelganger. ",
	 * "enableSpawnEgg = Enable LMM SpawnEgg Recipe. ",
	 * "VoiceDistortion = LittleMaid Voice distortion.",
	 * "defaultTexture = Default selected Texture Packege. Null is Random",
	 * "DebugMessage = Print Debug Massages.",
	 * "DeathMessage = Print Death Massages.", "Dominant = Spawn Anywhere.",
	 * "Aggressive = true: Will be hostile, false: Is a pacifist",
	 * "IgnoreItemList = aaa, bbb, ccc: Items little maid to ignore",
	 * "AchievementID = used Achievement index.(0 = Disable)",
	 * "UniqueEntityId = UniqueEntityId(0 is AutoAssigned. max 255)" };
	 */

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
	// @MLProp(info="At local, make sure the name of the owner. ")
	public static boolean cfg_checkOwnerName = false;
	// @MLProp(info="Not to survive the doppelganger. ")
	public static boolean cfg_antiDoppelganger = true;
	// @MLProp(info="Enable LMM SpawnEgg Recipe. ")
	public static boolean cfg_enableSpawnEgg = true;

	// @MLProp(info="LittleMaid Voice distortion.")
	public static boolean cfg_VoiceDistortion = false;

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
	
	// LivingSoundRate
	public static float cfg_voiceRate = 0.1f;

	// @MLProp(info="true: AlphaBlend(request power), false: AlphaTest(more fast)")
	// public static boolean AlphaBlend = true;
	// @MLProp(info="true: Will be hostile, false: Is a pacifist")
	public static boolean cfg_Aggressive = true;
	public static int cfg_maidOverdriveDelay = 64;

	@SidedProxy(clientSide = "net.blacklab.lmr.proxy.ProxyClient", serverSide = "net.blacklab.lmr.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	@Instance(DOMAIN)
	public static LittleMaidReengaged instance;

	public static ItemSpawnEgg spawnEgg;

	public static ItemRegisterKey registerKey;

	public static void Debug(String pText, Object... pVals) {
		// デバッグメッセージ
		if (cfg_PrintDebugMessage || DevMode.DEVELOPMENT_DEBUG_MODE) {
			System.out.println(String.format("littleMaidMob-" + pText, pVals));
		}
	}

	public String getName() {
		return "littleMaidMobNX";
	}
/*
	public String getPriorities() {
		// MMMLibを要求
		return "required-after:mod_MMM_MMMLib";
	}

	public String getVersion() {
		return "1.8";
	}
*/
	public static Random randomSoundChance;

	@EventHandler
	public void PreInit(FMLPreInitializationEvent evt) {
		// FileManager.setSrcPath(evt.getSourceFile());
		// MMM_Config.init();

		// MMMLibのRevisionチェック
		// MMM_Helper.checkRevision("6");
		// MMM_Config.checkConfig(this.getClass());

		randomSoundChance = new Random();

		// Config
		// エラーチェックのため試験的にimportしない形にしてみる
		ConfigList cfg = new ConfigList();
		try {
			cfg.loadConfig(getName(), evt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg_Aggressive = cfg.getBoolean("Aggressive", true);
		cfg_antiDoppelganger = cfg.getBoolean("antiDoppelganger", true);
		cfg.setComment("canDespawn", "Whether a LittleMaid(no-contract) can despawn.");
		cfg_canDespawn = cfg.getBoolean("canDespawn", false);
		cfg.setComment("checkOwnerName", "Recommended to keep 'true'. If 'true', on SMP, each player can tame his/her own maids.");
		cfg_checkOwnerName = cfg.getBoolean("checkOwnerName", true);
		cfg.setComment("DeathMessage", "Print chat message when your maid dies.");
		cfg_DeathMessage = cfg.getBoolean("DeathMessage", true);
		cfg.setComment("VoiceDistortion", "If 'true', voices distorts like as vanila mobs.");
		cfg_VoiceDistortion = cfg.getBoolean("VoiceDistortion", false);
		cfg_Dominant = cfg.getBoolean("Dominant", false);
		cfg.setComment("enableSpawnEgg", "If 'true', you can use a recipe of LittleMaid SpawnEgg.");
		cfg_enableSpawnEgg = cfg.getBoolean("enableSpawnEgg", true);
		cfg.setComment("maxGroupSize", "This config adjusts LittleMaids spawning.");
		cfg_maxGroupSize = cfg.getInt("maxGroupSize", 3);
		cfg.setComment("minGroupSize", "This config adjusts LittleMaids spawning.");
		cfg_minGroupSize = cfg.getInt("minGroupSize", 1);
		cfg.setComment("spawnLimit", "This config adjusts LittleMaids spawning.");
		cfg_spawnLimit = cfg.getInt("spawnLimit", 20);
		cfg.setComment("spawnWeight", "This config adjusts LittleMaids spawning.");
		cfg_spawnWeight = cfg.getInt("spawnWeight", 5);
		cfg.setComment("PrintDebugMessage", "Output messages for debugging to log. Usually this should be 'false'.");
		cfg_PrintDebugMessage = cfg.getBoolean("PrintDebugMessage", false);
		cfg.setComment("isModelAlphaBlend", "If 'false', alpha-blend of textures is disabled.");
		cfg_isModelAlphaBlend = cfg.getBoolean("isModelAlphaBlend", true);
		cfg.setComment("isFixedWildMaid", "If 'true', additional textures of LittleMaid(no-contract) will never used.");
		cfg_isFixedWildMaid = cfg.getBoolean("isFixedWildMaid", false);
		cfg_voiceRate = cfg.getFloat("voiceRate", 0.2f);
		cfg.setComment("voiceRate", "Ratio of playing non-force sound");

		cfg_maidOverdriveDelay = cfg.getInt("maidOverdriveDelay", 32);
		if (cfg_maidOverdriveDelay < 1) {
			cfg_maidOverdriveDelay = 1;
		} else if (cfg_maidOverdriveDelay > 128) {
			cfg_maidOverdriveDelay = 128;
		}

		// 配列
		String seedItemsOrgStr = cfg.getString("seedItems",
				"wheat_seeds, carrot, potato");
		for (String s : seedItemsOrgStr.split(" *, *")) {
			LMMNX_API_Farmer.addItemsForSeed(s);
		}

		String cropItemsOrgStr = cfg.getString("cropItems",
				"wheat, carrot, potato");
		for (String s : cropItemsOrgStr.split(" *, *")) {
			LMMNX_API_Farmer.addItemsForCrop(s);
		}

		try {
			cfg.saveConfig(getName(), evt);
		} catch (IOException e) {
			e.printStackTrace();
		}

		latestVersion = Version.getLatestVersion("http://mc.el-blacklab.net/lmmnxversion.txt", 10000);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance,
				new GuiHandler());

		EntityRegistry.registerModEntity(EntityLittleMaid.class,
				"LittleMaidX", 0, instance, 80, 3, true);

		spawnEgg = new ItemSpawnEgg();
		spawnEgg.setUnlocalizedName(DOMAIN + ":spawn_littlemaid_egg");
		GameRegistry.registerItem(spawnEgg, "spawn_littlemaid_egg");
		if (cfg_enableSpawnEgg) {
			GameRegistry.addRecipe(
					new ItemStack(spawnEgg, 1),
					new Object[] { "scs", "sbs", " e ", Character.valueOf('s'),
							Items.sugar, Character.valueOf('c'),
							new ItemStack(Items.dye, 1, 3),
							Character.valueOf('b'), Items.slime_ball,
							Character.valueOf('e'), Items.egg, });
		}

		registerKey = new ItemRegisterKey();
		GameRegistry.registerItem(registerKey, "registerkey");
		GameRegistry.addShapelessRecipe(new ItemStack(registerKey), Items.egg,
				Items.sugar, Items.nether_wart);

		// 実績追加
		LMMNX_Achievements.initAchievements();

		// AIリストの追加
		EntityModeManager.init();

		// アイテムスロット更新用のパケット
		LMRNetwork.init(DOMAIN);

		// Model
		if (evt.getSide() == Side.CLIENT) {
			ModelLoader.setCustomModelResourceLocation(
					LittleMaidReengaged.spawnEgg, 0, new ModelResourceLocation(
							DOMAIN+":spawn_littlemaid_egg", "inventory"));
			ModelLoader.setCustomModelResourceLocation(registerKey, 0,
					new ModelResourceLocation(DOMAIN+":registerkey",
							"inventory"));
			ModelLoader.setCustomModelResourceLocation(registerKey, 1,
					new ModelResourceLocation(DOMAIN+":registerkey",
							"inventory"));
		}

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.loadSounds();

		if (CommonHelper.isClient) {
			List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper
					.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(),
							"defaultResourcePacks", "field_110449_ao");
			defaultResourcePacks.add(new LMM_SoundResourcePack());
			defaultResourcePacks.add(new LMMNX_OldZipTexturesLoader());

			// デフォルトモデルの設定
			proxy.init();
		}

	}

	// public static ProxyClient.CountThread countThread;

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// カンマ区切りのアイテム名のリストを配列にして設定
		// "aaa, bbb,ccc  " -> "aaa" "bbb" "ccc"
		MinecraftForge.EVENT_BUS.register(new EventHook());
		FMLCommonHandler.instance().bus().register(new EventHook());

		// デフォルトモデルの設定
		// MMM_TextureManager.instance.setDefaultTexture(LMM_EntityLittleMaid.class,
		// MMM_TextureManager.instance.getTextureBox("default_Orign"));

		// Dominant
		BiomeGenBase[] biomeList = null;
		if (cfg_spawnWeight > 0) {
			if (cfg_Dominant) {
				biomeList = BiomeGenBase.getBiomeGenArray();
			} else {
				biomeList = new BiomeGenBase[] { BiomeGenBase.desert,
						BiomeGenBase.plains, BiomeGenBase.savanna,
						BiomeGenBase.mushroomIsland, BiomeGenBase.forest,
						BiomeGenBase.birchForest, BiomeGenBase.swampland,
						BiomeGenBase.taiga, BiomeGenBase.icePlains };
			}
			for (BiomeGenBase biome : biomeList) {
				if (biome != null) {
					EntityRegistry.addSpawn(EntityLittleMaid.class,
							cfg_spawnWeight, cfg_minGroupSize,
							cfg_maxGroupSize, EnumCreatureType.CREATURE, biome);
				}
			}
		}

		// モードリストを構築
		EntityModeManager.loadEntityMode();
		EntityModeManager.showLoadedModes();

		// サウンドのロード
		// TODO ★ proxy.loadSounds();

		// IFFのロード
		IFF.loadIFFs();
	}

}
