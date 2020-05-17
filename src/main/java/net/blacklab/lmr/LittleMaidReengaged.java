package net.blacklab.lmr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;

import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmc.common.command.LMCommand;
import net.blacklab.lmc.common.entity.LMEntityItemAntiDamage;
import net.blacklab.lmc.common.helper.ReflectionHelper;
import net.blacklab.lmc.common.item.LMItemMaidCarry;
import net.blacklab.lmc.common.item.LMItemMaidSouvenir;
import net.blacklab.lmc.common.item.LMItemMaidSpawnEgg;
import net.blacklab.lmc.common.item.LMItemMaidSugar;
import net.blacklab.lmc.common.villager.StructureVillagePiecesMaidBrokerHouse;
import net.blacklab.lmc.common.villager.VillagerProfessionMaidBroker;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.resource.OldZipTexturesWrapper;
import net.blacklab.lmr.client.resource.SoundResourcePack;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityMarkerDummy;
import net.blacklab.lmr.entity.maidmodel.api.LMMotionHandler;
import net.blacklab.lmr.entity.renderfactory.RenderFactoryLittleMaid;
import net.blacklab.lmr.entity.renderfactory.RenderFactoryMarkerDummy;
import net.blacklab.lmr.entity.renderfactory.RenderFactoryModelSelect;
import net.blacklab.lmr.event.EventHookLMRE;
import net.blacklab.lmr.item.ItemMaidPorter;
import net.blacklab.lmr.item.ItemMaidSpawnEgg;
import net.blacklab.lmr.item.ItemTriggerRegisterKey;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.network.ProxyCommon;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.loader.LMFileLoader;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.PluginManager;
import net.blacklab.lmr.util.manager.SoundManager;
import net.firis.lmt.common.LMTCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
//github.com/Verclene/LittleMaidReengaged.git
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryModifiable;

@Mod(
		modid = LittleMaidReengaged.MODID,
		name = LittleMaidReengaged.NAME,
		version = LittleMaidReengaged.VERSION,
		acceptedMinecraftVersions=LittleMaidReengaged.ACCEPTED_MCVERSION,
		dependencies = LittleMaidReengaged.DEPENDENCIES,
		guiFactory = "net.firis.lmt.config.FirisConfigGuiFactory"
		/*,
		updateJSON = "http://mc.el-blacklab.net/lmr-version.json"*/)
@EventBusSubscriber
public class LittleMaidReengaged {

	public static final String MODID = "lmreengaged";
	public static final String NAME = "LittleMaidReengaged";
	public static final String VERSION = "9.0.5.fp.039";
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

	/*
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
	*/

	@SidedProxy(clientSide = "net.blacklab.lmr.network.ProxyClient", serverSide = "net.blacklab.lmr.network.ProxyCommon")
	public static ProxyCommon proxy;

	@Instance(MODID)
	public static LittleMaidReengaged instance;

	// Item
	/*
	public static ItemMaidSpawnEgg spawnEgg;
	public static ItemTriggerRegisterKey registerKey;
	public static ItemMaidPorter maidPorter;
	*/
	
	/**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(LittleMaidReengaged.MODID)
    public static class LMItems {
    	public final static ItemMaidSpawnEgg SPAWN_LITTLEMAID_EGG = null;
    	public final static ItemTriggerRegisterKey REGISTERKEY = null;
    	public final static ItemMaidPorter MAIDPORTER = null;
    	public final static Item MAID_SOUVENIR = null;
    	public final static Item MAID_CARRY = null;
    	public final static Item MAID_SUGAR = null;
    	public final static Item MAID_SPAWN_EGG = null;
    	public final static Item MAID_CONTRACT = null;
    	public final static Item PLAYER_MAID_BOOK = null;
    }
    
    /**
     * 村人職業のインスタンス保持用
     */
	@ObjectHolder(LittleMaidReengaged.MODID + ":maid_broker")
	public final static VillagerProfession MAID_BROKER = null;

    /**
     * 開発用デバッグログ
     */
	public static void Debug(String pText, Object... pVals) {
		// デバッグメッセージ
		if (LMRConfig.cfg_PrintDebugMessage) {
			System.out.println(String.format("littleMaidMob-" + pText, pVals));
		}
	}
	
    /**
     * 開発用デバッグログ
     */
	public static void Debug(boolean isRemote, String format, Object... pVals) {
		Debug("Side=%s; ".concat(format), isRemote, pVals);
	}

	//public String getName() {
	//	return "LittleMaidReengaged";
	//}

	//public static Random randomSoundChance;
	
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
		
		// Config初期化
		LMRConfig.init(evt.getSuggestedConfigurationFile());
		
		// MMMLibからの引継ぎ
		// ClassLoaderを初期化
		/*
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
		*/
		
		//リトルメイドファイルローダー
		LMFileLoader.instance.load();
		
//		//マルチモデルセットアップ
//		ModelManager.instance.createLittleMaidModels();
		
		//テクスチャモデル初期化
		LMTextureBoxManager.instance.init();
		
		//サウンドパックセットアップ
		SoundManager.instance.createSounds();

//		StabilizerManager.init();

		// テクスチャパックの構築
//		ModelManager.instance.init();
//		ModelManager.instance.loadTextures();

//		// ロード
//		if (CommonHelper.isClient) {
//			// テクスチャパックの構築
////			MMM_TextureManager.loadTextures();
////			MMM_StabilizerManager.loadStabilizer();
//			// テクスチャインデックスの構築
//			Debug("Localmode: InitTextureList.");
//			ModelManager.instance.initTextureList(true);
//		} else {
//			ModelManager.instance.loadTextureServer();
//		}

		// FileManager.setSrcPath(evt.getSourceFile());
		// MMM_cfg_init();

		// MMMLibのRevisionチェック
		// MMM_Helper.checkRevision("6");
		// MMM_cfg_checkConfig(this.getClass());

//		randomSoundChance = new Random();

		/*
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
		*/

//		latestVersion = Version.getLatestVersion("http://mc.el-blacklab.net/lmmnxversion.txt", 10000);

		/*
		EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaid"),
				EntityLittleMaid.class,
    			"littlemaid", 0, instance, 80, 1, true);
    	*/
		
//		registerEntities();
		
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
		//EntityModeManager.init();
		
		//Pluginクラスをロードする登録処理
		PluginManager.preInitPluginLoad(evt);
		
		// アイテムスロット更新用のパケット
		LMRNetwork.init(MODID);

		//// Register model and renderer
		//proxy.rendererRegister();
		
		//音声ロード
		//proxy.loadSounds();
		
		//テスト用preInit
		LMTCore.preInit(evt);
		
		//メイドさんのモーション初期化
		LMMotionHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new EventHookLMRE());
		VEventBus.instance.registerListener(new EventHookLMRE());
		
		//描画イベント登録
		proxy.initClientRendererEventRegister();

		//Plugin初期化処理
		PluginManager.initRegisterPlugin(event);
		
		//村人構造物の初期化処理
		if (LMRConfig.cfg_general_villager_maid_broker) {
			StructureVillagePiecesMaidBrokerHouse.init();
		}
	}

	// public static ProxyClient.CountThread countThread;

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		// カンマ区切りのアイテム名のリストを配列にして設定
		// "aaa, bbb,ccc  " -> "aaa" "bbb" "ccc"

		// デフォルトモデルの設定
		// MMM_TextureManager.instance.setDefaultTexture(LMM_EntityLittleMaid.class,
		// MMM_TextureManager.instance.getTextureBox("default_Orign"));

		if (LMRConfig.cfg_spawnWeight > 0) {
			
			//メイドさんのスポーンバイオーム
			List<BiomeDictionary.Type> spawnBiomeList = new ArrayList<>();
			spawnBiomeList.add(BiomeDictionary.Type.WET);
			spawnBiomeList.add(BiomeDictionary.Type.DRY);
			spawnBiomeList.add(BiomeDictionary.Type.SAVANNA);
			spawnBiomeList.add(BiomeDictionary.Type.CONIFEROUS);
			spawnBiomeList.add(BiomeDictionary.Type.MUSHROOM);
			spawnBiomeList.add(BiomeDictionary.Type.FOREST);
			spawnBiomeList.add(BiomeDictionary.Type.PLAINS);
			spawnBiomeList.add(BiomeDictionary.Type.SANDY);
			spawnBiomeList.add(BiomeDictionary.Type.BEACH);
			
			Iterator<Biome> biomeIterator = Biome.REGISTRY.iterator();
			
			//バイオーム単位でスポーン設定を行う
			while(biomeIterator.hasNext()) {
				
				Biome biome = biomeIterator.next();
				if (biome == null) continue;
				
				boolean isSpawn = false;
				
				//デフォルトスポーン設定
				if (!isSpawn && !LMRConfig.cfg_custom_spawn) {
					//Biomeタイプが一致した場合にスポーン設定を行う
					for (BiomeDictionary.Type biomeType : spawnBiomeList) {
						if (BiomeDictionary.hasType(biome, biomeType)) {
							isSpawn = true;
							break;
						}
					}
				}
				
				//カスタムスポーン設定
				if (!isSpawn) {
					for(String spawnBiome : LMRConfig.cfg_spawn_biomes) {
						
						String biomeName = ReflectionHelper.getField(Biome.class, biome, "biomeName", "field_76791_y");
						
						//バイオーム名 or バイオームID
						if (spawnBiome.equals(biomeName)
								|| spawnBiome.equals(biome.getRegistryName().toString())) {
							isSpawn = true;
							break;
						}
					}
				}
				
				//スポーン対象の場合はスポーン設定
				if (isSpawn) {
					EntityRegistry.addSpawn(EntityLittleMaid.class, 
							LMRConfig.cfg_spawnWeight, 
							LMRConfig.cfg_minGroupSize, 
							LMRConfig.cfg_maxGroupSize, 
							EnumCreatureType.CREATURE, biome);
					Debug("Registering maids to spawn in " + biome);
				}
				
			}
		}

		// モードリストを構築
//		EntityModeManager.loadEntityMode();
//		EntityModeManager.showLoadedModes();
		//LoaderSearcher.INSTANCE.register(EntityModeHandler.class);
		//LoaderSearcher.INSTANCE.startSearch();

		// サウンドのロード
		// TODO ★ proxy.loadSounds();

		// IFFのロード
		IFF.loadIFFs();
		
		//お手製スポーンエッグのレシピ削除
		if (!LMRConfig.cfg_general_recipe_maid_spawn_egg) {
			((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES)
				.remove(new ResourceLocation(LittleMaidReengaged.MODID, "lmreengaged_recipe_0"));
			logger.info("delete recipe lmreengaged:maid_spawn_egg");
		}
	}
	
	
	@SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
		
//		//メイドさんスポーンエッグ
//		event.getRegistry().register(new ItemMaidSpawnEgg()
//   			.setRegistryName(MODID, "spawn_littlemaid_egg"));
		
//		event.getRegistry().register(new ItemTriggerRegisterKey()
//    			.setRegistryName(MODID, "registerkey"));
		
//		event.getRegistry().register(new ItemMaidPorter()
//   			.setRegistryName(MODID, "maidporter"));
		
		//メイドの土産
    	event.getRegistry().register(new LMItemMaidSouvenir()
    			.setRegistryName(MODID, "maid_souvenir")
    			.setUnlocalizedName("maid_souvenir"));
    	
    	//メイドキャリー
    	event.getRegistry().register(new LMItemMaidCarry()
    			.setRegistryName(MODID, "maid_carry")
    			.setUnlocalizedName("maid_carry"));
    	
    	//メイドシュガー
    	event.getRegistry().register(new LMItemMaidSugar()
    			.setRegistryName(MODID, "maid_sugar")
    			.setUnlocalizedName("maid_sugar"));
    	
		//お手製スポーンエッグ
		event.getRegistry().register(new LMItemMaidSpawnEgg(false)
    			.setRegistryName(MODID, "maid_spawn_egg")
    			.setUnlocalizedName("maid_spawn_egg"));
		
		//メイドさんの契約書
		event.getRegistry().register(new LMItemMaidSpawnEgg(true)
    			.setRegistryName(MODID, "maid_contract")
    			.setUnlocalizedName("maid_contract"));
    	
		//テスト用モジュール登録
		LMTCore.registerItems(event);
	}
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
//		// Register model and renderer
//		proxy.rendererRegister();
    	
    	// メイドの土産
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_SOUVENIR, 0,
				new ModelResourceLocation(LMItems.MAID_SOUVENIR.getRegistryName(), "inventory"));

		// メイドキャリー
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_CARRY, 0,
				new ModelResourceLocation(LMItems.MAID_CARRY.getRegistryName(), "inventory"));
		
		// メイドシュガー
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_SUGAR, 0,
				new ModelResourceLocation(LMItems.MAID_SUGAR.getRegistryName(), "inventory"));
		
    	// お手製スポーンエッグ
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_SPAWN_EGG, 0,
				new ModelResourceLocation(LMItems.MAID_SPAWN_EGG.getRegistryName(), "inventory"));

    	// メイドさんの契約書
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_CONTRACT, 0,
				new ModelResourceLocation(LMItems.MAID_CONTRACT.getRegistryName(), "inventory"));

		LMTCore.registerModels(event);
		
		//Entityの描画設定
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaid.class, new RenderFactoryLittleMaid());
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaidForTexSelect.class, new RenderFactoryModelSelect());
		RenderingRegistry.registerEntityRenderingHandler(EntityMarkerDummy.class, new RenderFactoryMarkerDummy());
		
		//リトルメイドテスト用モジュール
		LMTCore.rendererRegister();
		
    }
    
    
    /**
     * Entity登録イベント
     * 
     * @SubscribeEvent
     * public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
     */
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
    	
    	int entityId = 0;
    	
    	//Little Maid
    	EntityEntry littlemaidEntry = EntityEntryBuilder.create()
    			.entity(EntityLittleMaid.class)
    			.id(new ResourceLocation(LittleMaidReengaged.MODID, "littlemaid"), entityId++)
    			.name("littlemaid")
    			.egg(0xf8f8f8, 0xcc6600)
    			.tracker(80, 1, true)
    			.build();
    	event.getRegistry().register(littlemaidEntry);
    	
    	//AntiDamage EntityItem
    	EntityEntry antiDamageEntityItem = EntityEntryBuilder.create()
    			.entity(LMEntityItemAntiDamage.class)
    			.id(new ResourceLocation(LittleMaidReengaged.MODID, "entityitem_antidamage"), entityId++)
    			.name("Anti Dmage EntityItem")
    			.tracker(32, 5, true)
    			.build();
    	event.getRegistry().register(antiDamageEntityItem);
    	
    	//リトルメイドテスト用モジュール
    	LMTCore.registerEntities(entityId);
    }
    
    /**
     * サーバーサイドの登録処理
     * @param event
     */
	@EventHandler
	public void serverStatingEvent(FMLServerStartingEvent event) {
		
		//メイドさんコマンドの追加
		event.registerServerCommand(new LMCommand());
		
		//リトルメイドテスト用モジュール
    	LMTCore.serverStatingEvent(event);
	}
		
	/**
	 * 村人登録イベント
	 * @param event
	 */
    @SubscribeEvent
    public static void registerVillagerProfession(RegistryEvent.Register<VillagerProfession> event) {
    	
    	//メイド仲介人を登録
    	if (LMRConfig.cfg_general_villager_maid_broker) {
    		event.getRegistry().register(new VillagerProfessionMaidBroker());
    	}
    }

}
