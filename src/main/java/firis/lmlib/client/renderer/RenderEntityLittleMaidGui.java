package firis.lmlib.client.renderer;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import firis.lmlib.client.entity.EntityLittleMaidGui;
import firis.lmlib.client.renderer.layer.LayerArmorLittleMaidGui;
import net.minecraft.client.renderer.entity.RenderManager;


/**
 * GUIに表示するメイドさん
 *
 */
public class RenderEntityLittleMaidGui extends LMRenderMultiModel<EntityLittleMaidGui> {

	/**
	 * コンストラクタ
	 * @param manager
	 * @param pShadowSize
	 */
	public RenderEntityLittleMaidGui(RenderManager manager) {
		super(manager, 0.0F);
		addLayer(new LayerArmorLittleMaidGui(this));
	}
	
	/**
	 * 描画用のマルチモデル情報を取得する
	 */
	@Override
	protected IModelCompound getModelConfigCompoundFromEntity(EntityLittleMaidGui entity) {
//		IMultiModelEntity modelEntity = (IMultiModelEntity) entity;
//		ModelConfigCompound modelConfigCompound = modelEntity.getModelConfigCompound();
		return entity.getModelCompoundEntity();
	}
	
}
