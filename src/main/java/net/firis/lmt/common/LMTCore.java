package net.firis.lmt.common;

import java.util.Map;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.DevMode;
import net.firis.lmt.client.renderer.RendererLittleMaidTest;
import net.firis.lmt.client.renderer.RendererMaidChicken;
import net.firis.lmt.common.entity.EntityLittleMaidTest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

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
		if (!DevMode.DEVELOPMENT_DEBUG_MODE) return entityId;
		
		//テストメイドの登録
		EntityRegistry.registerModEntity(new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaidtest"),
				EntityLittleMaidTest.class,
    			"littlemaidtest", entityId, LittleMaidReengaged.instance, 80, 1, true);
    	entityId++;
		
		
		return entityId;
		
	}
	
	public static void rendererRegister() {

		//開発環境のみ実行
		if (!DevMode.DEVELOPMENT_DEBUG_MODE) return;

		//テストメイドさん描画用クラス登録
    	RenderingRegistry.registerEntityRenderingHandler(
    			EntityLittleMaidTest.class, new IRenderFactory<EntityLittleMaidTest>() {
				@Override
				public Render<? super EntityLittleMaidTest> createRenderFor(RenderManager manager) {
					return new RendererLittleMaidTest(manager);
				}
    	});
	}

	
	public static void initClientRendererEventRegister() {
		
		//開発環境のみ実行
		if (!DevMode.DEVELOPMENT_DEBUG_MODE) return;

		//テスト用処理
		//にわとりのrenderを独自renderへ差し替え
		Map<Class<? extends Entity>, Render<? extends Entity>> entityMap = Minecraft.getMinecraft().getRenderManager().entityRenderMap;
		Render<?> renderer = entityMap.get(EntityChicken.class);
		
		entityMap.put(EntityChicken.class, new RendererMaidChicken((RenderChicken) renderer));
		
	}

}
