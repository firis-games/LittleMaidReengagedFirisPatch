package net.blacklab.lmr.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.ModelBaseDuo;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

/**
 * 防具描画レイヤー
 * RenderLittleMaidから分離
 * 
 */
public class MMMLayerArmor extends LayerArmorBase<ModelBaseDuo> {

	protected static final float renderScale = 0.0625F;
	
	//レイヤーと化した防具描画
	protected final RenderLittleMaid renderer;
	
	//アーマーモデル
	protected final ModelBaseDuo mmodel;
	
	/*
	public float field_177184_f;
	public float field_177185_g;
	public float field_177192_h;
	public float field_177187_e;
	public boolean field_177193_i;
	*/
	
	protected EntityLittleMaid lmm;

	/**
	 * コンストラクタ
	 */
	public MMMLayerArmor(RenderLivingBase<?> rendererIn) {
		
		super(rendererIn);
		
		this.renderer = (RenderLittleMaid) rendererIn;
		
		this.mmodel = this.renderer.modelFATT;
		
	}

	@Override
	protected void initArmor() {
	}

	@Override
	protected void setModelSlotVisible(ModelBaseDuo paramModelBase, EntityEquipmentSlot paramInt) {
		ModelBaseDuo model = (ModelBaseDuo) paramModelBase;
		model.showArmorParts(paramInt.getIndex());
	}

	@Override
	public void doRenderLayer(EntityLivingBase par1EntityLiving, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		lmm = (EntityLittleMaid) par1EntityLiving;

		for (int i=0; i<4; i++) {
			if (!lmm.maidInventory.armorItemInSlot(i).isEmpty()) {
				render(par1EntityLiving, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, i);
			}
		}
	}

	/**
	 * アーマー描画処理
	 */
	public void render(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, int renderParts) {
//		boolean lri = (renderCount & 0x0f) == 0;
		//総合
		mmodel.showArmorParts(renderParts);

		//Inner
		INNER:{
			if(mmodel.textureInner!=null){
				ResourceLocation texInner = mmodel.textureInner[renderParts];
				if(texInner!=null&&lmm.isArmorVisible(0)) try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texInner);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					mmodel.modelInner.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, renderer.fcaps);
					mmodel.modelInner.setLivingAnimations(renderer.fcaps, limbSwing, limbSwingAmount, partialTicks);
					mmodel.modelInner.render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
				}catch(Exception e){ break INNER; }
			} else {
//				mmodel.modelInner.render(lmm.maidCaps, par2, par3, lmm.ticksExisted, par5, par6, renderScale, true);
			}
		}

		// 発光Inner
		INNERLIGHT: if (mmodel.modelInner!=null) {
			ResourceLocation texInnerLight = mmodel.textureInnerLight[renderParts];
			if (texInnerLight != null&&lmm.isArmorVisible(1)) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texInnerLight);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
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
					mmodel.modelInner.render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
					RendererHelper.setLightmapTextureCoords(mmodel.lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}catch(Exception e){ break INNERLIGHT; }
			}
		}

//		Minecraft.getMinecraft().getTextureManager().deleteTexture(lmm.getTextures(0)[renderParts]);
		//Outer
//		if(LittleMaidReengaged.cfg_isModelAlphaBlend) GL11.glEnable(GL11.GL_BLEND);
		OUTER:{
			if(mmodel.textureOuter!=null){
				ResourceLocation texOuter = mmodel.textureOuter[renderParts];
				if(texOuter!=null&&lmm.isArmorVisible(2)) try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuter);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					mmodel.modelOuter.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, renderer.fcaps);
					mmodel.modelOuter.setLivingAnimations(renderer.fcaps, limbSwing, limbSwingAmount, partialTicks);
					mmodel.modelOuter.render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
				}catch(Exception e){break OUTER;}
			}else{
//				mmodel.modelOuter.render(lmm.maidCaps, limbSwing, par3, lmm.ticksExisted, par5, par6, renderScale, true);
			}
		}

		// 発光Outer
		OUTERLIGHT: if (mmodel.modelOuter!=null) {
			ResourceLocation texOuterLight = mmodel.textureOuterLight[renderParts];
			if (texOuterLight != null&&lmm.isArmorVisible(3)) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuterLight);
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
					if(lmm.isArmorVisible(1)) mmodel.modelOuter.render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
					RendererHelper.setLightmapTextureCoords(mmodel.lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}catch(Exception e){ break OUTERLIGHT; }
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
	}
}
