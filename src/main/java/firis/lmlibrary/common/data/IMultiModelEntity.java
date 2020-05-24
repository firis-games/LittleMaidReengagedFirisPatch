package firis.lmlibrary.common.data;

import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;

/**
 * MMM_Texture仕様のテクスチャパック設定に対応しているEntityへ継承させる。
 * 
 * マルチモデルテクスチャ対応のEntityへ継承させる
 * 
 */
public interface IMultiModelEntity {

	/**
	 * Client用。
	 * TextureManagerがクライアント側のEntityへテクスチャ変更の通知を行う。
	 * @param pPackName
	 * 設定されるテクスチャパックの名称（TextureBoxClient）
	 */
	//void setTexturePackName(TextureBox[] pTextureBox);
	
	/**
	 * 現在のEntityに色を設定する。
	 * @param pColor
	 */
	//void setColor(byte pColor);

	/**
	 * 現在のEntityに設定されている色を返す。
	 * @return
	 */
	//byte getColor();

	//void setContract(boolean pContract);
	//boolean isContract();

	//void setTextureBox(TextureBoxBase[] pTextureBox);
	//TextureBoxBase[] getTextureBox();

	//void setTextures(int pIndex, ResourceLocation[] pNames);
	//ResourceLocation[] getTextures(int pIndex);
	
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
