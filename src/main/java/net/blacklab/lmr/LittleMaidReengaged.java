package net.blacklab.lmr;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.blacklab.lib.config.ConfigList;
import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.client.resource.OldZipTexturesWrapper;
import net.blacklab.lmr.client.resource.SoundResourcePack;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.event.EventHookLMRE;
import net.blacklab.lmr.item.ItemMaidPorter;
import net.blacklab.lmr.item.ItemMaidSpawnEgg;
import net.blacklab.lmr.item.ItemTriggerRegisterKey;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.network.ProxyCommon;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.FileList.CommonClassLoaderWrapper;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.manager.EntityModeManager;
import net.blacklab.lmr.util.manager.ModelManager;
import net.blacklab.lmr.util.manager.StabilizerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
//github.com/Verclene/LittleMaidReengaged.git
import net.minecraftforge.common.MinecraftForge;
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

@Mod(
		modid = LittleMaidReengaged.DOMAIN,
		name = "LittleMaidReengaged",
		version = LittleMaidReengaged.VERSION,
		acceptedMinecraftVersions=LittleMaidReengaged.ACCEPTED_MCVERSION,
		dependencies = LittleMaidReengaged.DEPENDENCIES,
		updateJSON = "http://mc.el-blacklab.net/lmr-version.json")
public class LittleMaidReengaged {

	public static final String DOMAIN = "lmreengaged";
	public static final String VERSION = "8.0.4.91";
	public static final String ACCEPTED_MCVERSION = "[1.9.4,1.10]";
	public static final int VERSION_CODE = 1;
	public static final String DEPENDENCIES = "required-after:Forge@[1.9-12.16.0.1819,);"
			+ "required-after:net.blacklab.lib@[5.2.0.3,)";

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

	@SidedProxy(clientSide = "net.blacklab.lmr.network.ProxyClient", serverSide = "net.blacklab.lmr.network.ProxyCommon")
	public static ProxyCommon proxy;

	@Instance(DOMAIN)
	public static LittleMaidReengaged instance;

	// Item
	public static ItemMaidSpawnEgg spawnEgg;
	public static ItemTriggerRegisterKey registerKey;
	public static ItemMaidPorter maidPorter;

	public static void Debug(String pText, Object... pVals) {
		// デバッグメッセージ
		if (cfg_PrintDebugMessage || DevMode.DEVELOPMENT_DEBUG_MODE) {
			System.out.println(String.format("littleMaidMob-" + pText, pVals));
		}
	}

	public static void Debug(boolean isRemote, String format, Object... pVals) {
		Debug("Side=%s; ".concat(format), isRemote, pVals);
	}

	public String getName() {
		return "LittleMaidReengaged";
	}

	public static Random randomSoundChance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		// MMMLibからの引継ぎ
		// ClassLoaderを初期化
		List<URL> urls = new ArrayList<URL>();
		try {
			urls.add(FileList.dirMods.toURI().toURL());
		} catch (MalformedURLException e1) {
		}
		if(DevMode.DEVMODE==DevMode.DEVMODE_ECLIPSE){
			for(File f:FileList.dirDevIncludeClasses){
				try {
					urls.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
				}
			}
		}
		FileList.COMMON_CLASS_LOADER = new CommonClassLoaderWrapper(urls.toArray(new URL[]{}), LittleMaidReengaged.class.getClassLoader());

		StabilizerManager.init();

		// テクスチャパックの構築
		ModelManager.instance.init();
		ModelManager.instance.loadTextures();
		// ロード
		if (CommonHelper.isClient) {
			// テクスチャパックの構築
//			MMM_TextureManager.loadTextures();
//			MMM_StabilizerManager.loadStabilizer();
			// テクスチャインデックスの構築
			Debug("Localmode: InitTextureList.");
			ModelManager.instance.initTextureList(true);
		} else {
			ModelManager.instance.loadTextureServer();
		}

		// FileManager.setSrcPath(evt.getSourceFile());
		// MMM_cfg_init();

		// MMMLibのRevisionチェック
		// MMM_Helper.checkRevision("6");
		// MMM_cfg_checkConfig(this.getClass());

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

		try {
			cfg.saveConfig(getName(), evt);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		latestVersion = Version.getLatestVersion("http://mc.el-blacklab.net/lmmnxversion.txt", 10000);

		EntityRegistry.registerModEntity(EntityLittleMaid.class,
				"LittleMaid", 0, instance, 80, 1, true);

		spawnEgg = new ItemMaidSpawnEgg();
		GameRegistry.<Item>register(spawnEgg, new ResourceLocation(DOMAIN, "spawn_littlemaid_egg"));
		if (cfg_enableSpawnEgg) {
			GameRegistry.addRecipe(
					new ItemStack(spawnEgg, 1),
					new Object[] { "scs", "sbs", " e ", Character.valueOf('s'),
							Items.SUGAR, Character.valueOf('c'),
							new ItemStack(Items.DYE, 1, 3),
							Character.valueOf('b'), Items.SLIME_BALL,
							Character.valueOf('e'), Items.EGG, });
		}

		registerKey = new ItemTriggerRegisterKey();
		GameRegistry.<Item>register(registerKey, new ResourceLocation(DOMAIN, "registerkey"));

		GameRegistry.addShapelessRecipe(new ItemStack(registerKey), Items.EGG,
				Items.SUGAR, Items.NETHER_WART);

		maidPorter = new ItemMaidPorter();
		GameRegistry.register(maidPorter, new ResourceLocation(DOMAIN, "maidporter"));

		// 実績追加
		AchievementsLMRE.initAchievements();

		// AIリストの追加
		EntityModeManager.init();

		// アイテムスロット更新用のパケット
		LMRNetwork.init(DOMAIN);

		// Register model and renderer
		proxy.rendererRegister();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.loadSounds();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		if (CommonHelper.isClient) {
			List<IResourcePack> defaultResourcePacks = ObfuscationReflectionHelper
					.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(),
							"defaultResourcePacks", "field_110449_ao");
			defaultResourcePacks.add(new SoundResourcePack());
			defaultResourcePacks.add(new OldZipTexturesWrapper());
		}

		MinecraftForge.EVENT_BUS.register(new EventHookLMRE());
		VEventBus.instance.registerListener(new EventHookLMRE());
	}

	// public static ProxyClient.CountThread countThread;

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// カンマ区切りのアイテム名のリストを配列にして設定
		// "aaa, bbb,ccc  " -> "aaa" "bbb" "ccc"

		// デフォルトモデルの設定
		// MMM_TextureManager.instance.setDefaultTexture(LMM_EntityLittleMaid.class,
		// MMM_TextureManager.instance.getTextureBox("default_Orign"));

		// Dominant
		Biome[] biomeList = null;

		if (cfg_spawnWeight > 0) {
			Iterator<Biome> biomeIterator = Biome.REGISTRY.iterator();
			while(biomeIterator.hasNext()) {
				Biome biome = biomeIterator.next();

				if(biome != null &&
						(
								(BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.HOT) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.COLD) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.WET) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DRY) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SAVANNA) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.CONIFEROUS) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.LUSH) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MUSHROOM) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.PLAINS) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SANDY) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SNOWY) ||
										BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.BEACH))
								)
						)
				{
					EntityRegistry.addSpawn(EntityLittleMaid.class, cfg_spawnWeight, cfg_minGroupSize, cfg_maxGroupSize, EnumCreatureType.CREATURE, biome);
					System.out.println("Registering spawn in " + biome.getBiomeName());
					Debug("Registering maids to spawn in " + biome.getBiomeName());
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
