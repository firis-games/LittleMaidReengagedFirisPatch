package net.blacklab.lmr.client.renderer.entity;

import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.layer.LayerArmorGui;
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
	public RenderEntitySelect(RenderManager manager, float pShadowSize) {
		super(manager, pShadowSize);
		addLayer(new LayerArmorGui(this));
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
