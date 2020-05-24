package firis.lmlibrary.lib.client.model;

import org.lwjgl.opengl.GL11;

import firis.lmlibrary.lib.common.data.IModelCapsData;
import firis.lmlibrary.lib.common.data.IModelConfigCompound;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.minecraft.client.model.ModelBase;

/**
 * マルチモデル用ModelBaseの基底クラス
 * @author firis-games
 *
 */
public abstract class ModelBaseNihil extends ModelBase {

	/**
	 * デフォルト発光色
	 */
	protected static final float[] TEXTURE_LIGHT_COLOR_DEFAULT = new float[] {1.0F, 1.0F, 1.0F, 1.0F};

	/**
	 * 描画に必要なマルチモデル関連のパラメータ
	 */
	protected IModelConfigCompound modelConfigCompound;
	protected IModelCapsData entityCaps;
	
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
		RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
		
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
		RendererHelper.setLightmapTextureCoords(this.lighting);
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

//	/**
//	 * レンダリングが実行された回数。
//	 * ダメージ時などの対策。
//	 */
//	protected int renderCount;


//	@Override
//	public ModelRenderer getRandomModelBox(Random par1Random) {
//		return modelArmorInner.getRandomModelBox(par1Random);
//	}
	
//	protected RenderModelMulti<? extends EntityLiving> rendererLivingEntity;

//	protected boolean isAlphablend;
//	public boolean isModelAlphablend;
//	public IModelBaseMMM capsLink;
	
//	@Override
//	public void render(Entity par1Entity, float par2, float par3, float par4,
//			float par5, float par6, float par7) {
//		renderCount++;
//	}
	
}
