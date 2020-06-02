package firis.lmlib.api.client.model;

import org.lwjgl.opengl.GL11;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.ModelMultiBase;
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
public class LMModelLittleMaid extends LMModelBase {

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
	public LMModelLittleMaid() {}
	
	/**
	 * 描画用パラメータを設定する
	 * @param modelConfigCompound
	 */
	public void initModelParameter(IModelCompound modelConfigCompound, float entityYaw, float partialTicks) {
		
		this.modelConfigCompound = modelConfigCompound;
		
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
		this.entityCaps.initModelMultiBase(this.maidModel, entityYaw, partialTicks);
		
		//本体描画設定
		//透明の場合は本体を描画しない
		this.isRendering = !this.modelConfigCompound.isInvisible();
		
		//各パラメータの初期化
		this.showAllParts();
		
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
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
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
	public void renderFirstPersonArm(IModelCompound modelConfigCompound) {
		
		//プレイヤーモデルの準備
		this.initModelParameter(modelConfigCompound, 0, 0);
		
		//テクスチャバインド
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.maidTexture);
		
		//お手ての位置調整
		GlStateManager.translate(0.0F, 0.25F, 0.0F);

		//お手てを描画
		this.maidModel.renderFirstPersonHand(this.entityCaps);
	}
	
}
