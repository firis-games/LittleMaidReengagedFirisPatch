package net.firis.lmt.test.renderer;

import net.firis.lmt.test.entity.EntityLittleMaidTest;
import net.firis.lmt.test.model.ModelLittleMaidTest;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * テストメイド用描画クラス
 */
@SideOnly(Side.CLIENT)
public class RendererLittleMaidTest extends RenderLiving<EntityLittleMaidTest> {

	private static final ResourceLocation MAID_TEXTURES = new ResourceLocation("textures/entity/littlemaid/mob_littlemaid.png");
	
	/**
	 * コンストラクタ
	 */
	public RendererLittleMaidTest(RenderManager rendermanagerIn) {
		
		super(rendermanagerIn, new ModelLittleMaidTest(), 0.3F);
		
	}
	
	/**
	 * バインド用のテクスチャ
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityLittleMaidTest entity) {
		return MAID_TEXTURES;
	}
	
}
