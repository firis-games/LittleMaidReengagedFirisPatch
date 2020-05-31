package net.firis.lmt.client.renderer;

import firis.lmlib.client.renderer.RenderModelMulti;
import firis.lmlib.common.data.IModelConfigCompound;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityVillager;

public class RendererLMVillager extends RenderModelMulti<EntityVillager> {

	public RendererLMVillager(RenderManager renderManagerIn) {
		super(renderManagerIn, 0.3F);
	}
	
	/**
	 * 村人用マルチモデル設定を生成する
	 */
	@Override
	protected IModelConfigCompound getModelConfigCompoundFromEntity(EntityVillager entity) {
		return new VillagerModelConfigCompound(entity);
	}

}
