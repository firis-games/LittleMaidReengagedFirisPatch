package firis.lmlib.api.entity;

import firis.lmlib.api.caps.IModelCompoundEntity;

/**
 * LittleMaidModelに対応したEntityを表すインターフェース
 * @author firis-games
 *
 */
public interface ILMModelEntity {

	/**
	 * MultiModel情報を取得する
	 * @return
	 */
	public IModelCompoundEntity getModelCompoundEntity();
	
}
