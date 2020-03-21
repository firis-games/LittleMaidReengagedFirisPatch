package net.firis.lmt.common;

import java.util.Map;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.LittleMaidReengaged.LMItems;
import net.blacklab.lmr.config.LMRConfig;
import net.firis.lmt.client.renderer.RendererLittleMaidTest;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.firis.lmt.common.entity.EntityLittleMaidTest;
import net.firis.lmt.common.item.LMItemPlayerMaidBook;
import net.firis.lmt.config.ConfigChangedEventHandler;
import net.firis.lmt.config.FirisConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
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
		
		if (!LMRConfig.cfg_prottype_maid_avatar) return false;
		
		return true;
	}
	
	/**
	 * init処理を記述
	 */
	public static int registerEntities(int entityId) {
		
		//開発環境のみ実行
		if (!isLMTCore()) return entityId;
		
		//テストメイドの登録
		EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaidtest"),
				EntityLittleMaidTest.class,
    			"littlemaidtest", entityId, LittleMaidReengaged.instance, 80, 1, true);
    	entityId++;
		
		
		return entityId;
		
	}
	
	public static void rendererRegister() {

		//開発環境のみ実行
		if (!isLMTCore()) return;

		//テストメイドさん描画用クラス登録
    	RenderingRegistry.registerEntityRenderingHandler(
    			EntityLittleMaidTest.class, new IRenderFactory<EntityLittleMaidTest>() {
				@Override
				public Render<? super EntityLittleMaidTest> createRenderFor(RenderManager manager) {
					return new RendererLittleMaidTest(manager);
				}
    	});
	}
	
	@SideOnly(Side.CLIENT)
	public static void initClientRendererEventRegister() {
		
		//開発環境のみ実行
		if (!isLMTCore()) return;

		////テスト用処理
		////にわとりのrenderを独自renderへ差し替え
		//Map<Class<? extends Entity>, Render<? extends Entity>> entityMap = Minecraft.getMinecraft().getRenderManager().entityRenderMap;
		//Render<?> renderer = entityMap.get(EntityChicken.class);
		
		//entityMap.put(EntityChicken.class, new RendererMaidChicken((RenderChicken) renderer));
		
		
		//Playerのスキンも差し替え
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().skinMap;
		
		RenderPlayer renderPlayer = skinMap.get("default");
		//RendererMaidPlayer renderMaidPlayer = new RendererMaidPlayer(renderPlayer);
		RendererMaidPlayerMultiModel renderMaidPlayer = new RendererMaidPlayerMultiModel(renderPlayer);
		
		
		Minecraft.getMinecraft().getRenderManager().playerRenderer = renderMaidPlayer;
		skinMap.put("default", renderMaidPlayer);
		skinMap.put("slim", renderMaidPlayer);
		
		
		//GuiConfig更新イベント登録
		MinecraftForge.EVENT_BUS.register(ConfigChangedEventHandler.class);
	}
	
	/**
	 * テスト用アイテム登録
	 */
	public static void registerItems(RegistryEvent.Register<Item> event) {

		if (!isLMTCore()) return;

		//メイドさんになる本
    	event.getRegistry().register(new LMItemPlayerMaidBook()
    			.setRegistryName(LittleMaidReengaged.DOMAIN, "player_maid_book")
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
        
	}
	
}
