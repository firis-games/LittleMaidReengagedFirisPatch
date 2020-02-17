package net.blacklab.lmr.client.renderer.entity;

import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.layer.MMMLayerArmorGui;
import net.blacklab.lmr.entity.maidmodel.IModelCaps;
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
		addLayer(new MMMLayerArmorGui(this));
	}


	/**
	 * Rendererのメイン処理
	 */
	@Override
	public void doRender(EntityLittleMaidForTexSelect entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//モデルパラメータセット
		fcaps = (IModelCaps)entity;
		
		//マルチモデルの描画
		renderModelMulti(entity, x, y, z, entityYaw, partialTicks, fcaps);
		
	}
	
}
