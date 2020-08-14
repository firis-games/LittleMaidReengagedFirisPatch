package net.blacklab.lmr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import firis.lmlib.api.LMLibraryAPI;
import net.blacklab.lmc.common.block.LMBlockSugarBox;
import net.blacklab.lmc.common.command.LMCommand;
import net.blacklab.lmc.common.entity.LMEntityItemAntiDamage;
import net.blacklab.lmc.common.helper.ReflectionHelper;
import net.blacklab.lmc.common.item.LMItemMaidCarry;
import net.blacklab.lmc.common.item.LMItemMaidSouvenir;
import net.blacklab.lmc.common.item.LMItemMaidSpawnEgg;
import net.blacklab.lmc.common.item.LMItemMaidStick;
import net.blacklab.lmc.common.item.LMItemMaidSugar;
import net.blacklab.lmc.common.tileentity.LMTileSugarBox;
import net.blacklab.lmc.common.villager.StructureVillagePiecesMaidBrokerHouse;
import net.blacklab.lmc.common.villager.VillagerProfessionMaidBroker;
import net.blacklab.lmr.client.renderer.entity.RenderEntityMarkerDummy;
import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityMarkerDummy;
import net.blacklab.lmr.entity.maidmodel.motion.LMMotionItemCarryer;
import net.blacklab.lmr.event.EventHookLMRE;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.network.ProxyCommon;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.manager.PluginManager;
import net.firis.lmt.common.LMTCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
//github.com/Verclene/LittleMaidReengaged.git
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
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
		dependencies = LittleMaidReengaged.DEPENDENCIES
		/*,
		updateJSON = "http://mc.el-blacklab.net/lmr-version.json"*/)
@EventBusSubscriber(modid=LittleMaidReengaged.MODID)
public class LittleMaidReengaged {

	public static final String MODID = "lmreengaged";
	public static final String NAME = "LittleMaidReengaged";
	public static final String VERSION = "9.2.3.fp.047";
	public static final String ACCEPTED_MCVERSION = "[1.12.2]";
	public static final String DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);"
			+ "required-after:lmlibrary@[1.0.7,);"
			+ "before:gvclib@[1.12.2,);"
			+ "before:generalguns@[1.,);";
	
	public static Logger logger = LogManager.getLogger(MODID);;

	@SidedProxy(clientSide = "net.blacklab.lmr.network.ProxyClient", serverSide = "net.blacklab.lmr.network.ProxyCommon")
	public static ProxyCommon proxy;

	@Instance(MODID)
	public static LittleMaidReengaged instance;
	
	/**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(LittleMaidReengaged.MODID)
    public static class LMItems {
    	public final static Item MAID_SOUVENIR = null;
    	public final static Item MAID_CARRY = null;
    	public final static Item MAID_SUGAR = null;
    	public final static Item MAID_SPAWN_EGG = null;
    	public final static Item MAID_CONTRACT = null;
    	public final static Item MAID_STICK = null;
    }
    
    /**
     * ブロックインスタンス保持用
     */
    @ObjectHolder(LittleMaidReengaged.MODID)
    public static class LMBlocks {
    	public final static Block SUGAR_BOX = null;
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
			logger.debug(String.format("littleMaidMob-" + pText, pVals));
		}
	}
	
    /**
     * 開発用デバッグログ
     */
	public static void Debug(boolean isRemote, String format, Object... pVals) {
		Debug("Side=%s; ".concat(format), isRemote, pVals);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		
		//logger = evt.getModLog();
		
		// Config初期化
		LMRConfig.init(evt.getSuggestedConfigurationFile());
		
		//Pluginクラスをロードする登録処理
		PluginManager.preInitPluginLoad(evt);
		
		// アイテムスロット更新用のパケット
		LMRNetwork.init(MODID);

		//テスト用preInit
		LMTCore.preInit(evt);
		
		//TileEntity登録
		GameRegistry.registerTileEntity(LMTileSugarBox.class, 
        		new ResourceLocation(MODID, "te_sugar_box"));
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new EventHookLMRE());
		
		//描画イベント登録
		proxy.initClientRendererEventRegister();

		//Plugin初期化処理
		PluginManager.initRegisterPlugin(event);
		
		//村人構造物の初期化処理
		if (LMRConfig.cfg_general_villager_maid_broker) {
			StructureVillagePiecesMaidBrokerHouse.init();
		}
		
		//追加モーション設定
		LMLibraryAPI.instance().registerLittleMaidMotion(new LMMotionItemCarryer());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {

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
		
		// IFFのロード
		IFF.loadIFFs();
		
		//お手製スポーンエッグのレシピ削除
		if (!LMRConfig.cfg_general_recipe_maid_spawn_egg) {
			((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES)
				.remove(new ResourceLocation(LittleMaidReengaged.MODID, "lmreengaged_recipe_0"));
			logger.info("delete recipe lmreengaged:maid_spawn_egg");
		}
		
		//メイドさんの契約書のレシピ削除
		if (!LMRConfig.cfg_general_recipe_maid_contract) {
			((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES)
				.remove(new ResourceLocation(LittleMaidReengaged.MODID, "lmreengaged_recipe_5"));
			logger.info("delete recipe lmreengaged:maid_contract");
		}
	}
	
	@SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event) {
		
		//シュガーボックス
        event.getRegistry().register(
                new LMBlockSugarBox()
                .setRegistryName(MODID, "sugar_box")
                .setUnlocalizedName("sugar_box")
        );
		
	}
	
	@SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
		
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
		
		//メイド棒
		event.getRegistry().register(new LMItemMaidStick()
    			.setRegistryName(MODID, "maid_stick")
    			.setUnlocalizedName("maid_stick"));
		
		//シュガーボックス
    	event.getRegistry().register(new ItemBlock(LMBlocks.SUGAR_BOX)
    			.setRegistryName(MODID, "sugar_box"));
    	
		//テスト用モジュール登録
		LMTCore.registerItems(event);
	}
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
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
		
		// メイド棒
		ModelLoader.setCustomModelResourceLocation(LMItems.MAID_STICK, 0,
				new ModelResourceLocation(LMItems.MAID_STICK.getRegistryName(), "inventory"));

		// シュガーボックス
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LMBlocks.SUGAR_BOX), 0,
				new ModelResourceLocation(LMBlocks.SUGAR_BOX.getRegistryName(), "inventory"));
		
		LMTCore.registerModels(event);
		
		//Entityの描画設定
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaid.class, new IRenderFactory<EntityLittleMaid>() {
			@Override
			public Render<? super EntityLittleMaid> createRenderFor(RenderManager manager) {
				return new RenderLittleMaid(manager);
			}
		});
//		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaidGui.class, new IRenderFactory<EntityLittleMaidGui>() {
//			@Override
//			public Render<? super EntityLittleMaidGui> createRenderFor(RenderManager manager) {
//				return new RenderEntityLittleMaidGui(manager);
//			}
//		});
		RenderingRegistry.registerEntityRenderingHandler(EntityMarkerDummy.class, new IRenderFactory<EntityMarkerDummy>() {
			@Override
			public Render<? super EntityMarkerDummy> createRenderFor(RenderManager manager) {
				return new RenderEntityMarkerDummy();
			}
		});
		
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
