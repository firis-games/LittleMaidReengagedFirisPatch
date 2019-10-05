package net.blacklab.lmr;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmr.client.resource.OldZipTexturesWrapper;
import net.blacklab.lmr.client.resource.SoundResourcePack;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.event.EventHookLMRE;
import net.blacklab.lmr.item.ItemMaidPorter;
import net.blacklab.lmr.item.ItemMaidSpawnEgg;
import net.blacklab.lmr.item.ItemTriggerRegisterKey;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.network.ProxyCommon;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.manager.EntityModeHandler;
import net.blacklab.lmr.util.manager.EntityModeManager;
import net.blacklab.lmr.util.manager.LoaderSearcher;
import net.blacklab.lmr.util.manager.ModelManager;
import net.blacklab.lmr.util.manager.StabilizerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeDictionary;
//github.com/Verclene/LittleMaidReengaged.git
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
		modid = LittleMaidReengaged.DOMAIN,
		name = "LittleMaidReengaged",
		version = LittleMaidReengaged.VERSION,
		acceptedMinecraftVersions=LittleMaidReengaged.ACCEPTED_MCVERSION,
		dependencies = LittleMaidReengaged.DEPENDENCIES/*,
		updateJSON = "http://mc.el-blacklab.net/lmr-version.json"*/)
@EventBusSubscriber
public class LittleMaidReengaged {

	public static final String DOMAIN = "lmreengaged";
	public static final String VERSION = "8.1.6.141";
	public static final String ACCEPTED_MCVERSION = "[1.12.2]";
	public static final String DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);";

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
	
	public static Logger logger;

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

	//public String getName() {
	//	return "LittleMaidReengaged";
	//}

	public static Random randomSoundChance;
	
	@SuppressWarnings("unchecked")
	public LittleMaidReengaged() {
		if (CommonHelper.isClient) {
			List<IResourcePack> defaultResourcePacks = (List<IResourcePack>)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), new String[] { "defaultResourcePacks", "field_110449_ao" });

			defaultResourcePacks.add(new SoundResourcePack());
			defaultResourcePacks.add(new OldZipTexturesWrapper());
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		logger = evt.getModLog();
		
		// MMMLibからの引継ぎ
		// ClassLoaderを初期化

		// Find classpath dir
		String classpath = System.getProperty("java.class.path");
		String separator = System.getProperty("path.separator");

		for (String path :
				classpath.split(separator)) {
			File pathFile = new File(path);
			if (pathFile.isDirectory()) {
				FileList.dirClasspath.add(pathFile);
			}
		}

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
		Configuration cfg = new Configuration(evt.getSuggestedConfigurationFile());
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

		cfg.save();

//		latestVersion = Version.getLatestVersion("http://mc.el-blacklab.net/lmmnxversion.txt", 10000);

		EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaid"),
				EntityLittleMaid.class,
    			"littlemaid", 0, instance, 80, 1, true);

		/*
		spawnEgg = new ItemMaidSpawnEgg();
		GameRegistry.<Item>register(spawnEgg, new ResourceLocation(DOMAIN, "spawn_littlemaid_egg"));
		
		GameRegistry.addRecipe(
				new ItemStack(spawnEgg, 1),
				"scs", "sbs", " e ", Character.valueOf('s'),
				Items.SUGAR, Character.valueOf('c'),
				new ItemStack(Items.DYE, 1, 3),
				Character.valueOf('b'), Items.SLIME_BALL,
				Character.valueOf('e'), Items.EGG);

		registerKey = new ItemTriggerRegisterKey();
		GameRegistry.<Item>register(registerKey, new ResourceLocation(DOMAIN, "registerkey"));

		GameRegistry.addShapelessRecipe(new ItemStack(registerKey), Items.EGG,
				Items.SUGAR, Items.NETHER_WART);

		maidPorter = new ItemMaidPorter();
		GameRegistry.register(maidPorter, new ResourceLocation(DOMAIN, "maidporter"));
		*/

		//// 実績追加
		//AchievementsLMRE.initAchievements();

		// AIリストの追加
		EntityModeManager.init();

		// アイテムスロット更新用のパケット
		LMRNetwork.init(DOMAIN);

		//// Register model and renderer
		//proxy.rendererRegister();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.loadSounds();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

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

		if (cfg_spawnWeight > 0) {
			Iterator<Biome> biomeIterator = Biome.REGISTRY.iterator();
			while(biomeIterator.hasNext()) {
				Biome biome = biomeIterator.next();

				if(biome != null &&
						(
								(BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) ||
//										BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.WET) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS) ||
//										BiomeDictionary.hasType(biome, BiomeDictionary.Type.LUSH) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) ||
//										BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY) ||
										BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH))
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
//		EntityModeManager.loadEntityMode();
//		EntityModeManager.showLoadedModes();
		LoaderSearcher.INSTANCE.register(EntityModeHandler.class);
		LoaderSearcher.INSTANCE.startSearch();

		// サウンドのロード
		// TODO ★ proxy.loadSounds();

		// IFFのロード
		IFF.loadIFFs();

	}
	
	
	@SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
		
		spawnEgg = new ItemMaidSpawnEgg();
		//メイドさんスポーンエッグ
		event.getRegistry().register(spawnEgg
    			.setRegistryName(DOMAIN, "spawn_littlemaid_egg"));
		
		
		registerKey = new ItemTriggerRegisterKey();
		event.getRegistry().register(registerKey
    			.setRegistryName(DOMAIN, "registerkey"));
		
		maidPorter = new ItemMaidPorter();
		event.getRegistry().register(maidPorter
    			.setRegistryName(DOMAIN, "maidporter"));
	}
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
    	// Register model and renderer
    	proxy.rendererRegister();
    }

}
