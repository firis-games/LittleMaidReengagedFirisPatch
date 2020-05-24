package net.firis.lmt.client.renderer;

import firis.lmlibrary.client.renderer.RenderModelMulti;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityVillager;

public class RendererLMVillager extends RenderModelMulti<EntityVillager> {

	public RendererLMVillager(RenderManager renderManagerIn) {
		super(renderManagerIn, 0.3F);
	}

	/**
	 * パラメータ設定
	 */
	@Override
	public void setModelValues(EntityVillager entity, double x, double y, double z, float entityYaw,	float partialTicks) {
		
		VillagerModelConfigCompound modelConfig = new VillagerModelConfigCompound(entity);
		//パラメータの初期化
		modelMain.initModelParameter(modelConfig, entityYaw, partialTicks);
		
	}

}
