package net.firis.lmt.client.renderer;

import net.firis.lmt.client.model.ModelLittleMaidTest;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

public class RendererMaidChicken extends RenderLiving<EntityChicken> {

	private static final ResourceLocation MAID_TEXTURES = new ResourceLocation("textures/entity/littlemaid/mob_littlemaid.png");
	
	/**
	 * メイドさんに書き換え実験
	 * @param renderChicken
	 */
	public RendererMaidChicken(RenderChicken renderChicken) {
		
		super(renderChicken.getRenderManager(), new ModelLittleMaidTest(), 0.3F);		
		
	}

	/**
	 * バインド用のテクスチャ
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityChicken entity) {
		return MAID_TEXTURES;
	}
}
