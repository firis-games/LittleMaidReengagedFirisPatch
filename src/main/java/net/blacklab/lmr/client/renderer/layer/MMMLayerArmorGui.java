package net.blacklab.lmr.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.entity.RenderEntitySelect;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.maidmodel.IModelEntity;
import net.blacklab.lmr.entity.maidmodel.ModelBaseDuo;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

/**
 * 防具描画レイヤー
 * RenderEntitySelectから分離
 * 
 */
public class MMMLayerArmorGui extends LayerArmorBase<ModelBaseDuo> {

	/*
	public RenderLivingBase p1;
	public ModelBaseDuo mmodel;
	*/
	
	protected static final float renderScale = 0.0625F;
	
	//レイヤーと化した防具描画
	protected final RenderEntitySelect renderer;
	
	//アーマーモデル
	protected final ModelBaseDuo mmodel;
	
	/*
	public float field_177184_f;
	public float field_177185_g;
	public float field_177192_h;
	public float field_177187_e;
	public boolean field_177193_i;
	*/
	
	protected EntityLittleMaidForTexSelect lmm;
	
	private int renderCount;

	/**
	 * コンストラクタ
	 */
	public MMMLayerArmorGui(RenderLivingBase<?> rendererIn) {
		
		super(rendererIn);
		
		this.renderer = (RenderEntitySelect) rendererIn;
		
		this.mmodel = this.renderer.modelFATT;
		
	}

	@Override
	protected void initArmor() {
		this.modelLeggings = this.mmodel;
		this.modelArmor = this.mmodel;
	}

	@Override
	protected void setModelSlotVisible(ModelBaseDuo paramModelBase, EntityEquipmentSlot paramInt) {
		ModelBaseDuo model = (ModelBaseDuo) paramModelBase;
		model.showArmorParts(paramInt.getIndex());
	}
	
	@Override
	public void doRenderLayer(EntityLivingBase par1EntityLiving, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		lmm = (EntityLittleMaidForTexSelect) par1EntityLiving;
		
		if(!lmm.modeArmor) return;

		//各パーツを描画する
		render(par1EntityLiving, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, 3);
		render(par1EntityLiving, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, 2);
		render(par1EntityLiving, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, 1);
		render(par1EntityLiving, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, 0);
	}

	/**
	 * アーマー描画処理
	 */
	public void render(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, int renderParts) {
		
		//描画用パラメータを初期化
		//初回のみ指定値設定
		/*if(renderCount==0) */this.setModelValues(lmm);

		//総合
		mmodel.showArmorParts(renderParts);

		//Inner
		INNER:{
			if(mmodel.modelInner==null) break INNER;
			
			ResourceLocation texInner = mmodel.textureInner[renderParts];
			if(texInner!=null) try{
				Minecraft.getMinecraft().getTextureManager().bindTexture(texInner);
			}catch(Exception e){}

			//透過設定
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
//			mmodel.modelInner.setLivingAnimations(lmm.maidCaps, par2, par3, lmm.ticksExisted);
//			mmodel.modelInner.setRotationAngles(par2, par3, lmm.ticksExisted, par5, par6, renderScale, lmm.maidCaps);
			mmodel.modelInner.mainFrame.render(renderScale);
			//mmodel.modelOuter.mainFrame.render(renderScale, true);
		}

		// 発光Inner
		INNERLIGHT: if (renderCount == 0 && mmodel.modelInner!=null) {
			ResourceLocation texInnerLight = mmodel.textureInnerLight[renderParts];
			if (texInnerLight != null) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texInnerLight);
				}catch(Exception e){ break INNERLIGHT; }
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				GL11.glDepthFunc(GL11.GL_LEQUAL);

				RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
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
				mmodel.modelInner.mainFrame.render(renderScale);
				RendererHelper.setLightmapTextureCoords(mmodel.lighting);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
//				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
		}

		//Outer
		if(LMRConfig.cfg_isModelAlphaBlend) GL11.glEnable(GL11.GL_BLEND);
		OUTER:{
			if(mmodel.modelOuter==null) break OUTER;
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			ResourceLocation texOuter = mmodel.textureOuter[renderParts];
			if(texOuter!=null) try{
				Minecraft.getMinecraft().getTextureManager().bindTexture(texOuter);
	//			mmodel.modelOuter.setLivingAnimations(lmm.maidCaps, par2, par3, lmm.ticksExisted);
	//			mmodel.modelOuter.setRotationAngles(par2, par3, lmm.ticksExisted, par5, par6, renderScale, lmm.maidCaps);
				mmodel.modelOuter.mainFrame.render(renderScale);
				//mmodel.modelOuter.mainFrame.render(renderScale, true);
			}catch(Exception e){}
		}

		// 発光Outer
		OUTERLIGHT: if (renderCount == 0 && mmodel.modelOuter!=null) {
			ResourceLocation texOuterLight = mmodel.textureOuterLight[renderParts];
			if (texOuterLight != null) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuterLight);
				}catch(Exception e){ break OUTERLIGHT; }
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				GL11.glDepthFunc(GL11.GL_LEQUAL);

				RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
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
				mmodel.modelOuter.mainFrame.render(renderScale);
				RendererHelper.setLightmapTextureCoords(mmodel.lighting);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
			}
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}

		//カウントインクリメント
//		renderCount++;
//		if(renderCount>=500) renderCount=0;
	}
	
	
	/**
	 * 描画用のパラメータを初期化する
	 */
	public void setModelValues(EntityLivingBase par1EntityLiving) {
		if (par1EntityLiving instanceof IModelEntity) {
			IModelEntity ltentity = (IModelEntity)par1EntityLiving;
			mmodel.modelInner = ltentity.getModelConfigCompound().getModelInnerArmor();
			mmodel.modelOuter = ltentity.getModelConfigCompound().getModelOuterArmor();
			mmodel.textureInner = ltentity.getTextures(1);
			mmodel.textureOuter = ltentity.getTextures(2);
			mmodel.textureInnerLight = ltentity.getTextures(3);
			mmodel.textureOuterLight = ltentity.getTextures(4);
//			mmodel.textureLightColor = (float[])modelFATT.getCapsValue(IModelCaps.caps_textureLightColor, pEntityCaps);
//			mmodel.entityCaps = lmm.maidCaps;
		}
//		mmodel.setEntityCaps(pEntityCaps);
		mmodel.setRender(this.renderer);
		mmodel.showAllParts();
		mmodel.isAlphablend = true;
	}
}
