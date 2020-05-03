package net.blacklab.lmr.client.renderer.layer;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.ModelBaseDuo;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * 防具描画レイヤー
 * RenderLittleMaidから分離
 * 
 */
@Deprecated
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
		
//		this.mmodel = this.renderer.modelFATT;
		this.mmodel = null;
		
	}

	@Override
	protected void initArmor() {
	}

	@Override
	protected void setModelSlotVisible(ModelBaseDuo paramModelBase, EntityEquipmentSlot paramInt) {
//		ModelBaseDuo model = (ModelBaseDuo) paramModelBase;
//		model.showArmorParts(paramInt.getIndex());
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
	/*
		//総合
		mmodel.showArmorParts(renderParts);

		//Inner
		INNER:{
			if(mmodel.getTextureInner(renderParts) != null){
				ResourceLocation texInner = mmodel.getTextureInner(renderParts);
				if(texInner!=null && lmm.isArmorVisible(0)) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(texInner);
						
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						
						mmodel.getModelInner(renderParts).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, renderer.fcaps);
						mmodel.getModelInner(renderParts).setLivingAnimations(renderer.fcaps, limbSwing, limbSwingAmount, partialTicks);
						mmodel.getModelInner(renderParts).render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
						
					} catch(Exception e) {
						break INNER;
					}
				}
			} else {
//				mmodel.modelInner.render(lmm.maidCaps, par2, par3, lmm.ticksExisted, par5, par6, renderScale, true);
			}
		}

		// 発光Inner
		INNERLIGHT: if (mmodel.getModelInner(renderParts) != null) {
			ResourceLocation texInnerLight = mmodel.getLightTextureInner(renderParts);
			if (texInnerLight != null && lmm.isArmorVisible(1)) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texInnerLight);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);

					RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
					if (mmodel.getTextureLightColor() == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								mmodel.getTextureLightColor()[0],
								mmodel.getTextureLightColor()[1],
								mmodel.getTextureLightColor()[2],
								mmodel.getTextureLightColor()[3]);
					}
					mmodel.getModelInner(renderParts).render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
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
			if(mmodel.getTextureOuter(renderParts) != null){
				ResourceLocation texOuter = mmodel.getTextureOuter(renderParts);
				if(texOuter!=null && lmm.isArmorVisible(2)) try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuter);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					mmodel.getModelOuter(renderParts).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, renderer.fcaps);
					mmodel.getModelOuter(renderParts).setLivingAnimations(renderer.fcaps, limbSwing, limbSwingAmount, partialTicks);
					mmodel.getModelOuter(renderParts).render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
				}catch(Exception e){break OUTER;}
			}else{
//				mmodel.modelOuter.render(lmm.maidCaps, limbSwing, par3, lmm.ticksExisted, par5, par6, renderScale, true);
			}
		}

		// 発光Outer
		OUTERLIGHT: if (mmodel.getModelOuter(renderParts) != null) {
			ResourceLocation texOuterLight = mmodel.getLightTextureOuter(renderParts);
			if (texOuterLight != null&&lmm.isArmorVisible(3)) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuterLight);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);

					RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
					if (mmodel.getTextureLightColor() == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								mmodel.getTextureLightColor()[0],
								mmodel.getTextureLightColor()[1],
								mmodel.getTextureLightColor()[2],
								mmodel.getTextureLightColor()[3]);
					}
					if(lmm.isArmorVisible(1)) mmodel.getModelOuter(renderParts).render(renderer.fcaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
					RendererHelper.setLightmapTextureCoords(mmodel.lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}catch(Exception e){ break OUTERLIGHT; }
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
*/
	}
}
