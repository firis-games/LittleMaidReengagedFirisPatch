package firis.lmlib.api.caps;

import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;

/**
 * MMM_Texture仕様のテクスチャパック設定に対応しているEntityへ継承させる。
 * 
 * マルチモデルテクスチャ対応のEntityへ継承させる
 * 
 */
public interface IMultiModelEntity {
	
	/**
	 * 仕様変更により、これ以外は必要無くなる予定。
	 * @return
	 */
	public ModelConfigCompound getModelConfigCompound();

	/**
	 * Entityのサイズを再設定する
	 */
	public void setSizeMultiModel(float width, float height);

}
