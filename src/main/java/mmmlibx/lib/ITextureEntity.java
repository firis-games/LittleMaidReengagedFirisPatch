package mmmlibx.lib;

import net.minecraft.util.ResourceLocation;

/**
 * MMM_Texture仕様のテクスチャパック設定に対応しているEntityへ継承させる。
 */
public interface ITextureEntity {

	/**
	 * Server用。
	 * TextureManagerがサーバー側のEntityへテクスチャ変更の通知を行う。
	 * @param pIndex
	 * 設定されるテクスチャパックのインデックス（TextureBoxServer）
	 */
	public void setTexturePackIndex(int pColor, int[] pIndex);

	/**
	 * Client用。
	 * TextureManagerがクライアント側のEntityへテクスチャ変更の通知を行う。
	 * @param pPackName
	 * 設定されるテクスチャパックの名称（TextureBoxClient）
	 */
	public void setTexturePackName(MMM_TextureBox[] pTextureBox);

	/**
	 * 現在のEntityに色を設定する。
	 * @param pColor
	 */
	public void setColor(int pColor);

	/**
	 * 現在のEntityに設定されている色を返す。
	 * @return
	 */
	public int getColor();

	public void setContract(boolean pContract);
	public boolean isContract();

	public void setTextureBox(MMM_TextureBoxBase[] pTextureBox);
	public MMM_TextureBoxBase[] getTextureBox();

	public void setTextureIndex(int[] pTextureIndex);
	public int[] getTextureIndex();

	public void setTextures(int pIndex, ResourceLocation[] pNames);
	public ResourceLocation[] getTextures(int pIndex);
	
	/**
	 * 仕様変更により、これ以外は必要無くなる予定。
	 * @return
	 */
	public MMM_TextureData getTextureData();


}
