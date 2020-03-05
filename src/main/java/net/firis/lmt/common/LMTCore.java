package net.firis.lmt.common;

import java.util.Map;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.util.DevMode;
import net.firis.lmt.client.renderer.RendererLittleMaidTest;
import net.firis.lmt.client.renderer.RendererMaidChicken;
import net.firis.lmt.client.renderer.RendererMaidPlayer;
import net.firis.lmt.common.entity.EntityLittleMaidTest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
	 * init処理を記述
	 */
	public static int registerEntities(int entityId) {
		
		//開発環境のみ実行
		if (!DevMode.DEVELOPMENT_DEBUG_MODE || !LMRConfig.cfg_developer_test_module) return entityId;
		
		//テストメイドの登録
		EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaidtest"),
				EntityLittleMaidTest.class,
    			"littlemaidtest", entityId, LittleMaidReengaged.instance, 80, 1, true);
    	entityId++;
		
		
		return entityId;
		
	}
	
	public static void rendererRegister() {

		//開発環境のみ実行
		if (!DevMode.DEVELOPMENT_DEBUG_MODE || !LMRConfig.cfg_developer_test_module) return;

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
		if (!DevMode.DEVELOPMENT_DEBUG_MODE || !LMRConfig.cfg_developer_test_module) return;

		//テスト用処理
		//にわとりのrenderを独自renderへ差し替え
		Map<Class<? extends Entity>, Render<? extends Entity>> entityMap = Minecraft.getMinecraft().getRenderManager().entityRenderMap;
		Render<?> renderer = entityMap.get(EntityChicken.class);
		
		entityMap.put(EntityChicken.class, new RendererMaidChicken((RenderChicken) renderer));
		
		
		//Playerのスキンも差し替え
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().skinMap;
		
		RenderPlayer renderPlayer = skinMap.get("default");
		RendererMaidPlayer renderMaidPlayer = new RendererMaidPlayer(renderPlayer);
		
		Minecraft.getMinecraft().getRenderManager().playerRenderer = renderMaidPlayer;
		skinMap.put("default", renderMaidPlayer);
		skinMap.put("slim", renderMaidPlayer);
	}

}
