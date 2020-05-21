package net.firis.lmt.common;

import java.util.Map;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.LittleMaidReengaged.LMItems;
import net.blacklab.lmr.config.LMRConfig;
import net.firis.lmt.client.event.KeyBindingHandler;
import net.firis.lmt.client.event.LittleMaidAvatarClientTickEventHandler;
import net.firis.lmt.client.renderer.RendererLMAvatar;
import net.firis.lmt.client.renderer.RendererLMVillager;
import net.firis.lmt.common.command.LMAvatarCommand;
import net.firis.lmt.common.item.LMItemPlayerMaidBook;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.common.manager.SyncPlayerModelClient;
import net.firis.lmt.common.manager.SyncPlayerModelServer;
import net.firis.lmt.config.ConfigChangedEventHandler;
import net.firis.lmt.config.FirisConfig;
import net.firis.lmt.config.custom.JConfigLMAvatarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * テスト用リトルメイド管理用
 * 
 * リトルメイドの実装テストのためのパッケージ群
 *
 */
public class LMTCore {
	
	/**
	 * テスト用リトルメイドモジュールを使用するかどうか
	 * @return
	 */
	public static boolean isLMTCore() {
		
		////開発環境のみ実行
		//if (!DevMode.DEVELOPMENT_DEBUG_MODE || !LMRConfig.cfg_developer_test_module) return false;
		
		if (!LMRConfig.cfg_lmabatar_maid_avatar) return false;
		
		return true;
	}
	
	/**
	 * init処理を記述
	 */
	public static int registerEntities(int entityId) {
		
		//開発環境のみ実行
		if (!isLMTCore()) return entityId;
		
		/*
		//テストメイドの登録
		EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaidtest"),
				EntityLittleMaidTest.class,
    			"littlemaidtest", entityId, LittleMaidReengaged.instance, 80, 1, true);
    	entityId++;
    	*/
		
		return entityId;
		
	}
	
	public static void rendererRegister() {

		//開発環境のみ実行
		if (!isLMTCore()) return;

		/*
		//テストメイドさん描画用クラス登録
    	RenderingRegistry.registerEntityRenderingHandler(
    			EntityLittleMaidTest.class, new IRenderFactory<EntityLittleMaidTest>() {
				@Override
				public Render<? super EntityLittleMaidTest> createRenderFor(RenderManager manager) {
					return new RendererLittleMaidTest(manager);
				}
    	});
    	*/
	}
	
	@SideOnly(Side.CLIENT)
	public static void initClientRendererEventRegister() {
		
		//開発環境のみ実行
		if (!isLMTCore()) return;

		////テスト用処理
		if (LMRConfig.cfg_developer_test_module) {
			////にわとりのrenderを独自renderへ差し替え
			Map<Class<? extends Entity>, Render<? extends Entity>> entityMap = Minecraft.getMinecraft().getRenderManager().entityRenderMap;
			////にわとりのRenderの差し替え
			//Render<?> renderer = entityMap.get(EntityChicken.class);
			//entityMap.put(EntityChicken.class, new RendererMaidChicken((RenderChicken) renderer));
			
			//村人のRenderの差し替え
			Render<?> renderVillager = entityMap.get(EntityVillager.class);
			entityMap.put(EntityVillager.class, new RendererLMVillager(renderVillager.getRenderManager()));
		}
		
		//Playerのスキン差し替え
		//Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().skinMap;
		Map<String, RenderPlayer> skinMap = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, 
				Minecraft.getMinecraft().getRenderManager(), 
				new String[] { "skinMap", "field_178636_l" });
		
		RenderPlayer renderPlayerDefault = skinMap.get("default");
		RenderPlayer renderPlayerSlim = skinMap.get("slim");
		//RendererMaidPlayer renderMaidPlayer = new RendererMaidPlayer(renderPlayer);
		RendererLMAvatar renderMaidPlayerDefault = new RendererLMAvatar(renderPlayerDefault);
		RendererLMAvatar renderMaidPlayerSlim = new RendererLMAvatar(renderPlayerSlim);
		
		
		//Minecraft.getMinecraft().getRenderManager().playerRenderer = renderMaidPlayer;
		ObfuscationReflectionHelper.setPrivateValue(RenderManager.class, 
				Minecraft.getMinecraft().getRenderManager(),
				renderMaidPlayerDefault, 
				new String[] { "playerRenderer", "field_178637_m" });
		
		skinMap.put("default", renderMaidPlayerDefault);
		skinMap.put("slim", renderMaidPlayerSlim);
		
		
		//GuiConfig更新イベント登録
		MinecraftForge.EVENT_BUS.register(ConfigChangedEventHandler.class);
		
		
		KeyBindingHandler.init();
		
		MinecraftForge.EVENT_BUS.register(KeyBindingHandler.class);
		MinecraftForge.EVENT_BUS.register(LittleMaidAvatarClientTickEventHandler.class);
		
		//LMアバター管理用イベント登録
        MinecraftForge.EVENT_BUS.register(new SyncPlayerModelClient());
	}
	
	/**
	 * テスト用アイテム登録
	 */
	public static void registerItems(RegistryEvent.Register<Item> event) {

		if (!isLMTCore()) return;

		//メイドさんになる本
    	event.getRegistry().register(new LMItemPlayerMaidBook()
    			.setRegistryName(LittleMaidReengaged.MODID, "player_maid_book")
    			.setUnlocalizedName("player_maid_book"));
    	
	}
	
	/**
	 * テスト用アイテムモデル登録
	 */
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		
		if (!isLMTCore()) return;
		
		// メイドさんになる本
		ModelLoader.setCustomModelResourceLocation(LMItems.PLAYER_MAID_BOOK, 0,
				new ModelResourceLocation(LMItems.PLAYER_MAID_BOOK.getRegistryName(), "inventory"));
	}
	
	/**
	 * テスト用preInit
	 * @param event
	 */
	public static void preInit(FMLPreInitializationEvent event) {
		
		if (!isLMTCore()) return;
		
		//設定読込
        FirisConfig.init(event.getModConfigurationDirectory());
        
        //カスタム設定読込
        JConfigLMAvatarManager.init();
        
        //LMアバター管理用イベント登録
        MinecraftForge.EVENT_BUS.register(new PlayerModelManager());
        MinecraftForge.EVENT_BUS.register(new SyncPlayerModelServer());
        
	}
	
	/**
	 * テスト用serverStatingEvent
	 */
	public static void serverStatingEvent(FMLServerStartingEvent event) {
		
		if (!isLMTCore()) return;
		
		event.registerServerCommand(new LMAvatarCommand());
		
	}
	
}
