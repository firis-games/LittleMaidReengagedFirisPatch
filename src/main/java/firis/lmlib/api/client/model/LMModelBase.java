package firis.lmlib.api.client.model;

import org.lwjgl.opengl.GL11;

import firis.lmlib.api.caps.IModelCapsEntity;
import firis.lmlib.api.caps.IModelCompound;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;

/**
 * マルチモデル用ModelBaseの基底クラス
 * @author firis-games
 *
 */
public abstract class LMModelBase extends ModelBase {

	/**
	 * デフォルト発光色
	 */
	protected static final float[] TEXTURE_LIGHT_COLOR_DEFAULT = new float[] {1.0F, 1.0F, 1.0F, 1.0F};

	/**
	 * 描画に必要なマルチモデル関連のパラメータ
	 */
	protected IModelCompound modelConfigCompound;
	protected IModelCapsEntity entityCaps;
	
	/**
	 * 描画に必要な個別パラメータ
	 */
	protected int lighting;
	protected boolean isRendering;
	protected float[] textureLightColor;
	
	/**
	 * パーツ表示メソッド
	 */
	abstract public void showAllParts();
	
	/**
	 * 発光テクスチャの事前設定
	 */
	protected void glLightTexturePre() {
		
		//発光テクスチャ用設定
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		//発光色調整
		this.setLightmapTextureCoords(0x00f000f0);//61680
		
		//発光色を調整
		float[] lightColor = this.getTextureLightColor();
		GL11.glColor4f(
				lightColor[0],
				lightColor[1],
				lightColor[2],
				lightColor[3]);		
	}
	
	/**
	 * 発光テクスチャの事後設定
	 */
	protected void glLightTexturePost() {
		
		//発光色リセット
		this.setLightmapTextureCoords(this.lighting);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		//発光テクスチャリセット
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(true);
	}
	
	/**
	 * 発光色設定
	 * @return
	 */
	protected float[] getTextureLightColor() {
		if (this.textureLightColor == null) {
			return TEXTURE_LIGHT_COLOR_DEFAULT;
		}
		return this.textureLightColor;
	}

	/**
	 * 互換用メソッド
	 * @param pValue
	 */
	public void setLightmapTextureCoords(int pValue) {
//		int ls = pValue % 65536;
//		int lt = pValue / 65536;
		int ls = pValue & 0xffff;
		int lt = pValue >>> 16;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ls, lt);
	}
	
}
