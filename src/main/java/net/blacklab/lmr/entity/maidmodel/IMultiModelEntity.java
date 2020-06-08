package net.blacklab.lmr.entity.maidmodel;

import firis.lmlib.api.caps.IModelCompoundEntity;
import firis.lmlib.api.entity.ILMModelEntity;

/**
 * MMM_Texture仕様のテクスチャパック設定に対応しているEntityへ継承させる。
 * 
 * マルチモデルテクスチャ対応のEntityへ継承させる
 * 
 */
public interface IMultiModelEntity extends ILMModelEntity {
	
	/**
	 * 仕様変更により、これ以外は必要無くなる予定。
	 * @return
	 */
	public ModelConfigCompound getModelConfigCompound();

	/**
	 * Entityのサイズを再設定する
	 */
	public void setSizeMultiModel(float width, float height);
	
	/**
	 * APIが利用するインターフェース
	 */
	@Override
	default public IModelCompoundEntity getModelCompoundEntity() {
		return getModelConfigCompound();	
	}

}
