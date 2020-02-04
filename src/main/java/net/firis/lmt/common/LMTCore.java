package net.firis.lmt.common;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.DevMode;
import net.firis.lmt.client.renderer.RendererLittleMaidTest;
import net.firis.lmt.common.entity.EntityLittleMaidTest;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
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

}
