package net.blacklab.lmr.client.renderer.entity;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.maidmodel.IModelCaps;
import net.blacklab.lmr.entity.maidmodel.IModelEntity;
import net.blacklab.lmr.entity.maidmodel.ModelBaseDuo;
import net.blacklab.lmr.entity.maidmodel.ModelBaseSolo;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderModelMulti<T extends EntityLiving> extends RenderLiving<T> {

	//メイド用モデル
	public ModelBaseSolo modelMain;
	//メイド防具用モデル
	public ModelBaseDuo modelFATT;
	//メイドモデル用パラメータ管理クラス
	public IModelCaps fcaps;

	/**
	 * コンストラクタ
	 */
	public RenderModelMulti(RenderManager manager, float pShadowSize) {
		
		super(manager, null, pShadowSize);
		
		//アーマー描画用モデル初期化
		modelFATT = new ModelBaseDuo(this);
		modelFATT.isModelAlphablend = LMRConfig.cfg_isModelAlphaBlend;
		modelFATT.isRendering = true;
		
		//メイド本体描画用モデル初期化
		modelMain = new ModelBaseSolo(this);
		modelMain.isModelAlphablend = LMRConfig.cfg_isModelAlphaBlend;
		modelMain.capsLink = modelFATT;
		mainModel = modelMain;
		//setRenderPassModel(modelFATT);
	}
	
	protected int showArmorParts(T par1EntityLiving, int par2, float par3) {
		// アーマーの表示設定
		modelFATT.renderParts = par2;
		modelFATT.renderCount = 0;
		ItemStack is = ((List<ItemStack>)par1EntityLiving.getArmorInventoryList()).get(par2);
		if (!is.isEmpty() && is.getCount() > 0) {
			modelFATT.showArmorParts(par2);
			return is.isItemEnchanted() ? 15 : 1;
		}

		return -1;
	}

	/**
	 * caps_ScaleFactorをもとに描画前に全体サイズを変更する
	 */
	@Override
	protected void preRenderCallback(EntityLiving entitylivingbaseIn, float partialTickTime) {
		
		//スケール設定を取得
		Float lscale = (Float)modelMain.getCapsValue(IModelCaps.caps_ScaleFactor);
		
		if (lscale != null) {
			GL11.glScalef(lscale, lscale, lscale);
		}
	}

	public void setModelValues(T par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
		
		if (par1EntityLiving instanceof IModelEntity) {
			IModelEntity ltentity = (IModelEntity)par1EntityLiving;
			modelMain.model = ltentity.getModelConfigCompound().textureModel[0];
			modelFATT.modelInner = ltentity.getModelConfigCompound().textureModel[1];
			modelFATT.modelOuter = ltentity.getModelConfigCompound().textureModel[2];
//			modelMain.model = ((TextureBox)ltentity.getTextureBox()[0]).models[0];
			modelMain.textures = ltentity.getTextures(0);
//			modelFATT.modelInner = ((TextureBox)ltentity.getTextureBox()[1]).models[1];
//			modelFATT.modelOuter = ((TextureBox)ltentity.getTextureBox()[1]).models[2];
			modelFATT.textureInner = ltentity.getTextures(1);
			modelFATT.textureOuter = ltentity.getTextures(2);
			modelFATT.textureInnerLight = ltentity.getTextures(3);
			modelFATT.textureOuterLight = ltentity.getTextures(4);
			modelFATT.textureLightColor = (float[])modelFATT.getCapsValue(IModelCaps.caps_textureLightColor, pEntityCaps);
		}
		modelMain.setEntityCaps(pEntityCaps);
		modelFATT.setEntityCaps(pEntityCaps);
		modelMain.setRender(this);
		modelFATT.setRender(this);
		modelMain.showAllParts();
		modelFATT.showAllParts();
		modelMain.isAlphablend = true;
		modelFATT.isAlphablend = true;
		modelMain.renderCount = 0;
		modelFATT.renderCount = 0;
		modelMain.lighting = modelFATT.lighting = par1EntityLiving.getBrightnessForRender();

		modelMain.setCapsValue(IModelCaps.caps_heldItemLeft, 0);
		modelMain.setCapsValue(IModelCaps.caps_heldItemRight, 0);
//		modelMain.setCapsValue(IModelCaps.caps_onGround, getSwingProgress(par1EntityLiving, par9));
		modelMain.setCapsValue(IModelCaps.caps_isRiding, par1EntityLiving.isRiding());
		modelMain.setCapsValue(IModelCaps.caps_isSneak, par1EntityLiving.isSneaking());
		modelMain.setCapsValue(IModelCaps.caps_aimedBow, false);
		modelMain.setCapsValue(IModelCaps.caps_isWait, false);
		modelMain.setCapsValue(IModelCaps.caps_isChild, par1EntityLiving.isChild());
		modelMain.setCapsValue(IModelCaps.caps_entityIdFactor, 0F);
		modelMain.setCapsValue(IModelCaps.caps_ticksExisted, par1EntityLiving.ticksExisted);
		//カスタム設定
		modelMain.setCapsValue(IModelCaps.caps_motionSitting, false);
	}

//	public void renderModelMulti(EntityLivingBase par1EntityLiving, double par2,
	public void renderModelMulti(T par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
		setModelValues(par1EntityLiving, par2, par4, par6, par8, par9, pEntityCaps);
		super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	/*
	@Override
	public void doRender(T par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9) {
		fcaps = (IModelCaps)par1EntityLiving;
		renderModelMulti(par1EntityLiving, par2, par4, par6, par8, par9, fcaps);
	}
	*/
	
	/**
	 * Rendererのメイン処理
	 */
	@Override
	abstract public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks);
	

	@Override
	protected void renderLeash(T par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9) {
		// 縄の位置のオフセット
		float lf = 0F;
		if (modelMain.model != null && fcaps != null) {
			lf = modelMain.model.getLeashOffset(fcaps);
		}
		super.renderLeash(par1EntityLiving, par2, par4 - lf, par6, par8, par9);
	}

	@Override
	protected void renderModel(T par1EntityLiving, float par2,
			float par3, float par4, float par5, float par6, float par7) {
		if (!par1EntityLiving.isInvisible()) {
			modelMain.setArmorRendering(true);
		} else {
			modelMain.setArmorRendering(false);
		}
		// アイテムのレンダリング位置を獲得するためrenderを呼ぶ必要がある
		mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
	}

	// TODO いらん？
	/*
	@Override
	protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2) {
		// ハードポイントの描画
		modelMain.renderItems(par1EntityLiving, this);
		renderArrowsStuckInEntity(par1EntityLiving, par2);
	}


	@Override
	protected void renderArrowsStuckInEntity(EntityLivingBase par1EntityLiving, float par2) {
		Client.renderArrowsStuckInEntity(par1EntityLiving, par2, this, modelMain.model);
	}



	@Override
	protected int getColorMultiplier(EntityLivingBase par1EntityLivingBase,
			float par2, float par3) {
		modelMain.renderCount = 16;
		return super.getColorMultiplier(par1EntityLivingBase, par2, par3);
	}

	*/

	@Override
	protected ResourceLocation getEntityTexture(T var1) {
		// テクスチャリソースを返すところだけれど、基本的に使用しない。
		return null;
	}


}
