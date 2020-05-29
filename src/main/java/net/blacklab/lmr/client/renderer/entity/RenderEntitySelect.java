package net.blacklab.lmr.client.renderer.entity;

import firis.lmlib.client.renderer.RenderModelMulti;
import firis.lmlib.common.data.IMultiModelEntity;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.layer.LayerArmorLittleMaidGui;
import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;
import net.minecraft.client.renderer.entity.RenderManager;


/**
 * GUIに表示するメイドさん
 *
 */
public class RenderEntitySelect extends RenderModelMulti<EntityLittleMaidForTexSelect> {

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
	 * マルチモデルの描画パラメータの初期化処理
	 * @param entity
	 * @param x
	 * @param y
	 * @param z
	 * @param entityYaw
	 * @param partialTicks
	 */
	@Override
	public void setModelValues(EntityLittleMaidForTexSelect entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//モデルパラメータ取得
		IMultiModelEntity modelEntity = (IMultiModelEntity) entity;
		ModelConfigCompound modelConfigCompound = modelEntity.getModelConfigCompound();
		
		//パラメータの初期化
		modelMain.initModelParameter(modelConfigCompound, entityYaw, partialTicks);
		
	}

//	/**
//	 * Rendererのメイン処理
//	 */
//	@Override
//	public void doRender(EntityLittleMaidForTexSelect entity, double x, double y, double z, float entityYaw, float partialTicks) {
//		
//		//モデルパラメータセット
//		fcaps = entity.getModelConfigCompound().getModelCaps();
//		
//		//マルチモデルの描画
//		doRenderMultiModel(entity, x, y, z, entityYaw, partialTicks, fcaps);
//		
//	}
	
}
