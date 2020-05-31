package firis.lmlib.api.caps;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;

/**
 * マルチモデルのパラメータを管理する
 * @author firis-games
 *
 */
public interface IModelCapsData extends IModelCaps {

	/**
	 * ModelMultiBaseへ初期値を設定する
	 * @param model
	 * @param modelCaps
	 */
	public void setModelMultiFromModelCaps(ModelMultiBase model, float entityYaw, float partialTicks);
	
}
