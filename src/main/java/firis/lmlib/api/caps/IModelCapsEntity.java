package firis.lmlib.api.caps;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;

/**
 * Entity用マルチモデルのパラメータの管理クラス
 * @author firis-games
 * 
 * ModelMultiBaseへの初期値設定処理定義を追加
 *
 */
public interface IModelCapsEntity extends IModelCaps {

	/**
	 * ModelMultiBaseへ初期値を設定する
	 * @param model
	 * @param modelCaps
	 */
	public void initModelMultiBase(ModelMultiBase model, float entityYaw, float partialTicks);
	
}
