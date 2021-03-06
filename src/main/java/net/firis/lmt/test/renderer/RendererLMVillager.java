package net.firis.lmt.test.renderer;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityVillager;

public class RendererLMVillager extends LMRenderMultiModel<EntityVillager> {

	public RendererLMVillager(RenderManager renderManagerIn) {
		super(renderManagerIn, 0.3F);
	}
	
	/**
	 * 村人用マルチモデル設定を生成する
	 */
	@Override
	protected IModelCompound getModelConfigCompoundFromEntity(EntityVillager entity) {
		return new VillagerModelConfigCompound(entity);
	}

}
