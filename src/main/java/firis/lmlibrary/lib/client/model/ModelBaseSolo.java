package firis.lmlibrary.lib.client.model;

import org.lwjgl.opengl.GL11;

import firis.lmlibrary.api.caps.IModelCaps;
import firis.lmlibrary.api.model.ModelLittleMaidBase;
import firis.lmlibrary.api.model.ModelMultiBase;
import firis.lmlibrary.lib.common.data.IModelConfigCompound;
import net.blacklab.lmr.config.LMRConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * マルチモデルのメイドさん用ModelBase
 * @author firis-games
 *
 */
public class ModelBaseSolo extends ModelBaseNihil {

	/**
	 * メイドさんモデル
	 */
	private ModelMultiBase maidModel;
	
	/**
	 * メイドさんテクスチャ
	 */
	private ResourceLocation maidTexture;

	/**
	 * メイドさんテクスチャ
	 */
	private ResourceLocation maidTextureLight;
	
	/**
	 * コンストラクタ
	 */
	public ModelBaseSolo() {}
	
	/**
	 * 描画用パラメータを設定する
	 * @param modelConfigCompound
	 */
	public void initModelParameter(IModelConfigCompound modelConfigCompound, float entityYaw, float partialTicks) {
		
		this.modelConfigCompound = modelConfigCompound;
//		EntityLiving entity = (EntityLiving) this.modelConfigCompound.getOwner();
		
		//内部変数リセット
		this.entityCaps = null;
		this.maidModel = null;
		this.maidTexture = null;
		this.maidTextureLight = null;
		this.textureLightColor = null;
		
		//設定対象がない場合は処理を中断
		if (this.modelConfigCompound == null) return;
		
		//各内部変数に設定する
		this.maidModel = this.modelConfigCompound.getModelLittleMaid();
		
		this.maidTexture = this.modelConfigCompound.getTextureLittleMaid();
		this.maidTextureLight = this.modelConfigCompound.getLightTextureLittleMaid();
		
		this.entityCaps = this.modelConfigCompound.getModelCaps();
		
		//マルチモデル初期化
		this.entityCaps.setModelMultiFromModelCaps(this.maidModel, entityYaw, partialTicks);
		
		//本体描画設定
		//透明の場合は本体を描画しない
		this.isRendering = !this.modelConfigCompound.isInvisible();
//		if (entity.isInvisible()) {
//			this.isRendering = false;
//		}
		
		//各パラメータの初期化
		this.showAllParts();
//		this.isAlphablend = true;
//		this.renderCount = 0;
		this.lighting = this.modelConfigCompound.getBrightnessForRender();
	}
	
	/**
	 * マルチモデルのsetRotationAngles
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		if (maidModel != null) {
			maidModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityCaps);
		}
	}
	
	/**
	 * マルチモデルのsetLivingAnimations
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		if (maidModel != null) {
			maidModel.setLivingAnimations(entityCaps, limbSwing, limbSwingAmount, partialTickTime);
		}
	}
	
	/**
	 * メイドさん描画処理
	 */
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		if (this.maidModel == null) return;
		
		//法線再計算
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//マルチモデル
		if (this.maidModel != null && this.maidTexture != null) {
			//透過設定
			if (LMRConfig.cfg_isModelAlphaBlend) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			} else {
				GL11.glDisable(GL11.GL_BLEND);
			}
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.maidTexture);
			this.maidModel.render(entityCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, isRendering);
		}
		
		//発光テクスチャ
		if (this.maidModel != null && this.maidTextureLight != null) {
			
			//発光パーツ
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.maidTextureLight);

			//発光テクスチャ用事前設定
			this.glLightTexturePre();

			maidModel.render(entityCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, isRendering);
			
			//発光テクスチャ用事後設定
			this.glLightTexturePost();
		}
	}
	
	/**
	 * メイドさんモデル表示設定
	 */
	@Override
	public void showAllParts() {
		if (maidModel != null) {
			maidModel.showAllParts();
		}
	}

	
	/**
	 * 腕の位置へずらす
	 */
	public void armPostRender(int arm, float scale) {
		this.maidModel.Arms[arm].postRender(scale);
	}
	
	/**
	 * 頭の位置へずらす
	 */
	public void headPostRender(float scale) {
		if (this.maidModel instanceof ModelLittleMaidBase) {
			ModelLittleMaidBase maidmodel = (ModelLittleMaidBase) this.maidModel;
			
			maidmodel.bipedHead.postRender(scale);
		}
	}
	
	/**
	 * 胴の位置へずらす
	 */
	public void bodyPostRender(float scale) {
		if (this.maidModel instanceof ModelLittleMaidBase) {
			ModelLittleMaidBase maidmodel = (ModelLittleMaidBase) this.maidModel;
			
			maidmodel.bipedBody.postRender(scale);
		}
	}
	
	/**
	 * メイドモデルのスケールを取得する
	 * @return
	 */
	public Float getMultiModelScaleFactor() {
		
		if (maidModel == null) return null;
		
		Float scale = (Float) maidModel.getCapsValue(IModelCaps.caps_ScaleFactor);
		
		return scale;
	}
	
	/**
	 * リードの位置調整用パラメータ
	 * @param pEntityCaps
	 * @return
	 */
	public float getLeashOffset() {
		
		if (this.maidModel == null) return 0.0F;
		
		//リードの位置調整パラメータを取得する
		return this.maidModel.getLeashOffset(this.entityCaps);
	}
	
	/**
	 * 一人称の手を描画する
	 * @param modelConfigCompound
	 */
	public void renderFirstPersonArm(IModelConfigCompound modelConfigCompound) {
		
		//プレイヤーモデルの準備
		this.initModelParameter(modelConfigCompound, 0, 0);
		
		//テクスチャバインド
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.maidTexture);
		
		//お手ての位置調整
		GlStateManager.translate(0.0F, 0.25F, 0.0F);

		//お手てを描画
		this.maidModel.renderFirstPersonHand(this.entityCaps);
	}
	
	/**
	 * メイドさん描画処理
	 */
/*
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
//		if (maidModel == null) {
//			isAlphablend = false;
//			return;
//		}
		
		if (this.maidModel == null) return;
		
//		isAlphablend = true;
		
		//法線の再計算
		//GlStateManager.enableRescaleNormal();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//透過設定
//		if (isAlphablend) {
		if (LMRConfig.cfg_isModelAlphaBlend) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL11.glDisable(GL11.GL_BLEND);
		}
//		}
//		if (textures.length > 2 && textures[2] != null) {
//			// Actors用
//			model.setRotationAngles(par2, par3, par4, par5, par6, par7, entityCaps);
//			// Face
//			// TODO テクスチャのロードはなんか考える。
//			Minecraft.getMinecraft().getTextureManager().bindTexture(textures[2]);
//			model.setCapsValue(caps_renderFace, entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
//			// Body
//			Minecraft.getMinecraft().getTextureManager().bindTexture(textures[0]);
//			model.setCapsValue(caps_renderBody, entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
//		} else {
		// 通常
		if (this.maidModel != null && this.maidTexture != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.maidTexture);
			this.maidModel.render(entityCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, isRendering);
		}
		
//		}
//		isAlphablend = false;
		if (this.maidModel != null && this.maidTextureLight != null) {
//				&& renderCount == 0) {
			
			//発光パーツ
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.maidTextureLight);
			
////			float var4 = 1.0F;
//			GL11.glEnable(GL11.GL_BLEND);
//			GL11.glEnable(GL11.GL_ALPHA_TEST);
//			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//			GL11.glDepthFunc(GL11.GL_LEQUAL);
//			RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			//発光テクスチャ用事前設定
			this.glLightTexturePre();

			maidModel.render(entityCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, isRendering);
			
			//発光テクスチャ用事後設定
			this.glLightTexturePre();
			
//			RendererHelper.setLightmapTextureCoords(lighting);
//			
////			GL11.glEnable(GL11.GL_LIGHTING);
//			GL11.glDisable(GL11.GL_BLEND);
//			//GL11.glDisable(GL11.GL_ALPHA_TEST);
//			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//			GL11.glDepthMask(true);
		}
//		textures = blanks;
//		renderCount++;
	}
*/
	
//	public ResourceLocation textures[];
//	private ModelConfigCompound modelConfigCompound;
	
//	protected ModelMultiBase getModel() {
//		return this.maidModel;
//	}
	
	
	
//	public static final ResourceLocation[] blanks = new ResourceLocation[0];

//	public ModelBaseSolo(RenderModelMulti<? extends EntityLiving> pRender) {
//	public ModelBaseSolo() {
//		rendererLivingEntity = pRender;
//	}



//	@Override
//	public TextureOffset getTextureOffset(String par1Str) {
//		return maidModel == null ? null : maidModel.getTextureOffset(par1Str);
//	}

	// IModelMMM追加分

//	@Override
//	public void renderItems(EntityLivingBase pEntity, Render pRender) {
//		if (model != null) {
//			model.renderItems(entityCaps);
//		}
//	}

//	@Override
//	public void showArmorParts(int pParts) {
//		if (model != null) {
//			model.showArmorParts(pParts, 0);
//		}
//	}

//	/**
//	 * Renderer辺でこの変数を設定する。
//	 * 設定値はIModelCapsを継承したEntitiyとかを想定。
//	 */
//	@Override
//	public void setEntityCaps(IModelCaps pEntityCaps) {
//		entityCaps = pEntityCaps;
//		if (capsLink != null) {
//			capsLink.setEntityCaps(pEntityCaps);
//		}
//	}

//	@Override
//	public void setRender(Render pRender) {
//		if (model != null) {
//			model.render = pRender;
//		}
//	}

//	@Override
//	public void setArmorRendering(boolean pFlag) {
//		isRendering = pFlag;
//	}


	// IModelCaps追加分

//	@Override
//	public Map<String, Integer> getModelCaps() {
//		return model == null ? null : model.getModelCaps();
//	}

//	@Override
//	public Object getCapsValue(int pIndex, Object ... pArg) {
//		return model == null ? null : model.getCapsValue(pIndex, pArg);
//	}

//	@Override
//	public boolean setCapsValue(int pIndex, Object... pArg) {
//		if (capsLink != null) {
//			capsLink.setCapsValue(pIndex, pArg);
//		}
//		if (model != null) {
//			model.setCapsValue(pIndex, pArg);
//		}
//		return false;
//	}
	
}
