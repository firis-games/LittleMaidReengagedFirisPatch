package net.blacklab.lmr.entity.maidmodel;

import net.minecraft.util.ResourceLocation;

/**
 * MMM_Texture仕様のテクスチャパック設定に対応しているEntityへ継承させる。
 */
public interface IModelEntity {

	/**
	 * Client用。
	 * TextureManagerがクライアント側のEntityへテクスチャ変更の通知を行う。
	 * @param pPackName
	 * 設定されるテクスチャパックの名称（TextureBoxClient）
	 */
	void setTexturePackName(TextureBox[] pTextureBox);
	
	/**
	 * 現在のEntityに色を設定する。
	 * @param pColor
	 */
	void setColor(byte pColor);

	/**
	 * 現在のEntityに設定されている色を返す。
	 * @return
	 */
	byte getColor();

	void setContract(boolean pContract);
	boolean isContract();

	void setTextureBox(TextureBoxBase[] pTextureBox);
	TextureBoxBase[] getTextureBox();

	void setTextures(int pIndex, ResourceLocation[] pNames);
	ResourceLocation[] getTextures(int pIndex);
	
	/**
	 * 仕様変更により、これ以外は必要無くなる予定。
	 * @return
	 */
	ModelConfigCompound getModelConfigCompound();


}
