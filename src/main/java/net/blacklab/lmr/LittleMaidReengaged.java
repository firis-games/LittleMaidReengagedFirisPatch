package net.blacklab.lmr;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmc.common.entity.LMEntityItemAntiDamage;
import net.blacklab.lmc.common.helper.ReflectionHelper;
import net.blacklab.lmc.common.item.LMItemMaidCarry;
import net.blacklab.lmc.common.item.LMItemMaidSouvenir;
import net.blacklab.lmc.common.item.LMItemMaidSugar;
import net.blacklab.lmr.client.resource.OldZipTexturesWrapper;
import net.blacklab.lmr.client.resource.SoundResourcePack;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.event.EventHookLMRE;
import net.blacklab.lmr.item.ItemMaidPorter;
import net.blacklab.lmr.item.ItemMaidSpawnEgg;
import net.blacklab.lmr.item.ItemTriggerRegisterKey;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.network.ProxyCommon;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.manager.EntityModeHandler;
import net.blacklab.lmr.util.manager.EntityModeManager;
import net.blacklab.lmr.util.manager.LoaderSearcher;
import net.blacklab.lmr.util.manager.ModelManager;
import net.blacklab.lmr.util.manager.StabilizerManager;
import net.firis.lmt.common.LMTCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
//github.com/Verclene/LittleMaidReengaged.git
import net.minecraftforge.common.MinecraftForge;
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
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
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
	public static final String VERSION = "8.1.6.141.fp.019";
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

	@Instance(DOMAIN)
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
    @ObjectHolder(LittleMaidReengaged.DOMAIN)
    public static class LMItems{
    	public final static ItemMaidSpawnEgg SPAWN_LITTLEMAID_EGG = null;
    	public final static ItemTriggerRegisterKey REGISTERKEY = null;
    	public final static ItemMaidPorter MAIDPORTER = null;
    	public final static Item MAID_SOUVENIR = null;
    	public final static Item MAID_CARRY = null;
    	public final static Item MAID_SUGAR = null;
    }

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

		// Config初期化
		LMRConfig.init(evt.getSuggestedConfigurationFile());
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
		registerEntities();
		
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
		
		//音声ロード
		proxy.loadSounds();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new EventHookLMRE());
		VEventBus.instance.registerListener(new EventHookLMRE());
		
		//描画イベント登録
		proxy.initClientRendererEventRegister();
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
				if (!isSpawn && LMRConfig.cfg_spawn_default_enable) {
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
		LoaderSearcher.INSTANCE.register(EntityModeHandler.class);
		LoaderSearcher.INSTANCE.startSearch();

		// サウンドのロード
		// TODO ★ proxy.loadSounds();

		// IFFのロード
		IFF.loadIFFs();

	}
	
	
	@SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
		
		//メイドさんスポーンエッグ
		event.getRegistry().register(new ItemMaidSpawnEgg()
    			.setRegistryName(DOMAIN, "spawn_littlemaid_egg"));
		
		event.getRegistry().register(new ItemTriggerRegisterKey()
    			.setRegistryName(DOMAIN, "registerkey"));
		
		event.getRegistry().register(new ItemMaidPorter()
    			.setRegistryName(DOMAIN, "maidporter"));
		
		//メイドの土産
    	event.getRegistry().register(new LMItemMaidSouvenir()
    			.setRegistryName(DOMAIN, "maid_souvenir")
    			.setUnlocalizedName("maid_souvenir"));
    	
    	//メイドキャリー
    	event.getRegistry().register(new LMItemMaidCarry()
    			.setRegistryName(DOMAIN, "maid_carry")
    			.setUnlocalizedName("maid_carry"));
    	
    	//メイドシュガー
    	event.getRegistry().register(new LMItemMaidSugar()
    			.setRegistryName(DOMAIN, "maid_sugar")
    			.setUnlocalizedName("maid_sugar"));
    	
	}
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
    	// Register model and renderer
    	proxy.rendererRegister();
    	
    	// メイドの土産
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_SOUVENIR, 0,
				new ModelResourceLocation(LMItems.MAID_SOUVENIR.getRegistryName(), "inventory"));

		// メイドキャリー
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_CARRY, 0,
				new ModelResourceLocation(LMItems.MAID_CARRY.getRegistryName(), "inventory"));
		
		// メイドシュガー
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_SUGAR, 0,
				new ModelResourceLocation(LMItems.MAID_SUGAR.getRegistryName(), "inventory"));
		
    }
    
    
    /**
     * Entity登録イベント
     * 互換のためイベントでの挙動はさせない
     * @SubscribeEvent
     * public void registerEntities(RegistryEvent.Register<EntityEntry> event)
     */
    public void registerEntities() {
    	int entityId = 0;
    	
    	//Little Maid
    	EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaid"),
				EntityLittleMaid.class,
    			"littlemaid", entityId, instance, 80, 1, true);
    	entityId++;
    	
    	//AntiDamage EntityItem
    	EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "entityitem_antidamage"), 
    			LMEntityItemAntiDamage.class, 
    			"Anti Dmage EntityItem", 
    			entityId, 
    			instance, 32, 5, true);
    	entityId++;
    	
    	//リトルメイドテスト用モジュール
    	LMTCore.registerEntities(entityId);
    }

}
