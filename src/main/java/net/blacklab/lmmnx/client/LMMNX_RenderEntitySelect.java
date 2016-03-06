package net.blacklab.lmmnx.client;

import littleMaidMobX.LMM_LittleMaidMobNX;
import mmmlibx.lib.Client;
import mmmlibx.lib.ITextureEntity;
import mmmlibx.lib.MMM_EntitySelect;
import mmmlibx.lib.multiModel.model.mc162.ModelBaseDuo;
import mmmlibx.lib.multiModel.model.mc162.RenderModelMulti;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LMMNX_RenderEntitySelect extends RenderModelMulti {

	public class MMMLayerArmor extends LayerArmorBase{

		public RendererLivingEntity p1;
		public ModelBaseDuo mmodel;
		public RendererLivingEntity field_177190_a;
		public float field_177184_f;
		public float field_177185_g;
		public float field_177192_h;
		public float field_177187_e;
		public boolean field_177193_i;
		public MMM_EntitySelect lmm;
		private int renderCount;

		public MMMLayerArmor(RendererLivingEntity p_i46125_1_) {
			super(p_i46125_1_);
			p1 = p_i46125_1_;
			mmodel = modelFATT;
			this.field_177189_c = mmodel;
			this.field_177186_d = mmodel;
		}

		@Override
		protected void initArmor() {
			this.field_177189_c = mmodel;
			this.field_177186_d = mmodel;
		}

		@Override
		protected void func_177179_a(ModelBase paramModelBase, int paramInt) {
			ModelBaseDuo model = (ModelBaseDuo) paramModelBase;
			model.showArmorParts(paramInt);
		}

		@Override
		public void doRenderLayer(EntityLivingBase par1EntityLiving,
				float par2, float par3, float par4,
				float par5, float par6, float par7,
				float par8) {
			lmm = (MMM_EntitySelect) par1EntityLiving;
			if(!lmm.modeArmor) return;
			// TODO もっと手っ取り早い方法ない？
			render(par1EntityLiving, par2, par3, par4, par6, par7, par8, 3);
			render(par1EntityLiving, par2, par3, par4, par6, par7, par8, 2);
			render(par1EntityLiving, par2, par3, par4, par6, par7, par8, 1);
			render(par1EntityLiving, par2, par3, par4, par6, par7, par8, 0);
		}

		public void setModelValues(EntityLivingBase par1EntityLiving) {
			if (par1EntityLiving instanceof ITextureEntity) {
				ITextureEntity ltentity = (ITextureEntity)par1EntityLiving;
				mmodel.modelInner = ltentity.getTextureData().textureModel[1];
				mmodel.modelOuter = ltentity.getTextureData().textureModel[2];
				mmodel.textureInner = ltentity.getTextures(1);
				mmodel.textureOuter = ltentity.getTextures(2);
				mmodel.textureInnerLight = ltentity.getTextures(3);
				mmodel.textureOuterLight = ltentity.getTextures(4);
//				mmodel.textureLightColor = (float[])modelFATT.getCapsValue(IModelCaps.caps_textureLightColor, pEntityCaps);
//				mmodel.entityCaps = lmm.maidCaps;
			}
//			mmodel.setEntityCaps(pEntityCaps);
			mmodel.setRender(LMMNX_RenderEntitySelect.this);
			mmodel.showAllParts();
			mmodel.isAlphablend = true;
		}

		public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7, int renderParts) {
			//初回のみ指定値設定
			/*if(renderCount==0) */this.setModelValues(lmm);

			//総合
			mmodel.showArmorParts(renderParts);

			//Inner
			INNER:{
				if(mmodel.modelInner==null) break INNER;
				ResourceLocation texInner = mmodel.textureInner[renderParts];
				if(texInner!=null) try{
					Client.setTexture(texInner);
				}catch(Exception e){}

//				mmodel.modelInner.setLivingAnimations(lmm.maidCaps, par2, par3, lmm.ticksExisted);
//				mmodel.modelInner.setRotationAngles(par2, par3, lmm.ticksExisted, par5, par6, 0.0625F, lmm.maidCaps);
				mmodel.modelInner.mainFrame.render(0.0625F);
				//mmodel.modelOuter.mainFrame.render(0.0625F, true);
			}

			// 発光Inner
			INNERLIGHT: if (renderCount == 0 && mmodel.modelInner!=null) {
				ResourceLocation texInnerLight = mmodel.textureInnerLight[renderParts];
				if (texInnerLight != null) {
					try{
						Client.setTexture(texInnerLight);
					}catch(Exception e){ break INNERLIGHT; }
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);

					Client.setLightmapTextureCoords(0x00f000f0);//61680
					if (mmodel.textureLightColor == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								mmodel.textureLightColor[0],
								mmodel.textureLightColor[1],
								mmodel.textureLightColor[2],
								mmodel.textureLightColor[3]);
					}
					mmodel.modelInner.mainFrame.render(0.0625F);
					Client.setLightmapTextureCoords(mmodel.lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
//					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}
			}

			//Outer
			if(LMM_LittleMaidMobNX.cfg_isModelAlphaBlend) GL11.glEnable(GL11.GL_BLEND);
			OUTER:{
				if(mmodel.modelOuter==null) break OUTER;
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				ResourceLocation texOuter = mmodel.textureOuter[renderParts];
				if(texOuter!=null) try{
					Client.setTexture(texOuter);
				}catch(Exception e){}

//				mmodel.modelOuter.setLivingAnimations(lmm.maidCaps, par2, par3, lmm.ticksExisted);
//				mmodel.modelOuter.setRotationAngles(par2, par3, lmm.ticksExisted, par5, par6, 0.0625F, lmm.maidCaps);
				mmodel.modelOuter.mainFrame.render(0.0625F);
				//mmodel.modelOuter.mainFrame.render(0.0625F, true);
			}

			// 発光Outer
			OUTERLIGHT: if (renderCount == 0 && mmodel.modelOuter!=null) {
				ResourceLocation texOuterLight = mmodel.textureOuterLight[renderParts];
				if (texOuterLight != null) {
					try{
						Client.setTexture(texOuterLight);
					}catch(Exception e){ break OUTERLIGHT; }
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);

					Client.setLightmapTextureCoords(0x00f000f0);//61680
					if (mmodel.textureLightColor == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								mmodel.textureLightColor[0],
								mmodel.textureLightColor[1],
								mmodel.textureLightColor[2],
								mmodel.textureLightColor[3]);
					}
					mmodel.modelOuter.mainFrame.render(0.0625F);
					Client.setLightmapTextureCoords(mmodel.lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
				}
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			//カウントインクリメント
//			renderCount++;
//			if(renderCount>=500) renderCount=0;
		}
	}


	public LMMNX_RenderEntitySelect(RenderManager manager, float pShadowSize) {
		super(manager, pShadowSize);
		addLayer(new MMMLayerArmor(this));
	}


	@Override
	public void doRender(EntityLiving par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9) {
		super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
	}

}
