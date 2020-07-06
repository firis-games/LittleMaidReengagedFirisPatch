package net.blacklab.lmr.client.renderer.entity;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import firis.lmlib.client.entity.EntityLittleMaidGui;
import net.blacklab.lmr.client.renderer.layer.LayerArmorLittleMaidGui;
import net.blacklab.lmr.entity.maidmodel.IMultiModelEntity;
import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;
import net.minecraft.client.renderer.entity.RenderManager;


/**
 * GUIに表示するメイドさん
 *
 */
public class RenderEntitySelect extends LMRenderMultiModel<EntityLittleMaidGui> {

	/**
	 * コンストラクタ
	 * @param manager
	 * @param pShadowSize
	 */
	public RenderEntitySelect(RenderManager manager) {
		super(manager, 0.0F);
		addLayer(new LayerArmorLittleMaidGui(this));
	}
	
	/**
	 * 描画用のマルチモデル情報を取得する
	 */
	@Override
	protected IModelCompound getModelConfigCompoundFromEntity(EntityLittleMaidGui entity) {
		IMultiModelEntity modelEntity = (IMultiModelEntity) entity;
		ModelConfigCompound modelConfigCompound = modelEntity.getModelConfigCompound();
		return modelConfigCompound;
	}
	
}
