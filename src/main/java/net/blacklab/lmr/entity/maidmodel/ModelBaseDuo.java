package net.blacklab.lmr.entity.maidmodel;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.client.renderer.entity.RenderModelMulti;
import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.blacklab.lmr.util.IModelCapsData;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * アーマーの二重描画用クラス。
 * 必ずInner側にはモデルを設定すること。
 * 通常のRendererで描画するためのクラスなので、Renderをちゃんと記述するならいらないクラスです。
 */
public class ModelBaseDuo extends ModelBaseNihil implements IModelBaseMMM {

	private ModelMultiBase modelOuter;
	private ModelMultiBase modelInner;
	
	//内部定数
	private static final int ARMOR_INNER_ID = 0;
	private static final int ARMOR_OUTER_ID = 1;
	private static final float renderScale = 0.0625F;
	
	public ModelMultiBase getModelOuter(int slot) {
		return this.modelOuter;
	}
	public ModelMultiBase getModelInner(int slot) {
		return this.modelInner;
	}
	
	protected ModelConfigCompound modelConfigCompound;
	
	/**
	 * 描画用パラメータを設定する
	 * @param modelConfigCompound
	 */
	public void setModelConfigCompound(EntityLiving entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot) {
		
		IModelEntity modelEntity = (IModelEntity) entity;
		
		this.modelConfigCompound = modelEntity.getModelConfigCompound();
		
		this.modelOuter = null;
		this.modelInner = null;
		this.textureLightColor = null;
		
		//モデル初期化
		if (this.modelConfigCompound != null) {
			
			//モデル初期化
			this.modelOuter = this.modelConfigCompound.getModelOuterArmor();
			this.modelInner = this.modelConfigCompound.getModelInnerArmor();
			
			IModelCapsData caps = modelConfigCompound.getModelCaps();
			
			//色設定
			this.textureLightColor = (float[])this.getCapsValue(IModelCaps.caps_textureLightColor, caps);
			
			//モデル情報の初期化
			caps.setModelValues(this.modelInner, entity, 0, 0, 0, netHeadYaw, partialTicks);
			caps.setModelValues(this.modelOuter, entity, 0, 0, 0, netHeadYaw, partialTicks);
			
			//モデルの表示設定
			if (this.modelInner != null) {
				this.modelInner.showArmorParts(slot.getIndex(), ARMOR_INNER_ID);
			}
			if (this.modelInner != null) {
				this.modelOuter.showArmorParts(slot.getIndex(), ARMOR_OUTER_ID);
			}
			
			this.entityCaps = modelConfigCompound.getModelCaps();
			
		}
	}
	
	/**
	 * インナー防具テクスチャ取得
	 * @param Slot
	 * @return
	 */
	public ResourceLocation getTextureInner(int slot) {
		if (this.modelConfigCompound != null) {
			return this.modelConfigCompound.getTextureBoxArmor().getTextureInnerArmor(ItemStack.EMPTY);
		}
		return null;
	}
	
	/**
	 * 発光インナー防具テクスチャ取得
	 * @param Slot
	 * @return
	 */
	public ResourceLocation getLightTextureInner(int slot) {
		if (this.modelConfigCompound != null && this.modelConfigCompound.getTextureBoxArmor() != null) {
			return this.modelConfigCompound.getTextureBoxArmor().getLightTextureInnerArmor(ItemStack.EMPTY);
		}
		return null;
	}
	
	/**
	 * アウター防具テクスチャ取得
	 * @param Slot
	 * @return
	 */
	public ResourceLocation getTextureOuter(int slot) {
		if (this.modelConfigCompound != null) {
			return this.modelConfigCompound.getTextureBoxArmor().getTextureOuterArmor(ItemStack.EMPTY);
		}
		return null;
	}
	
	/**
	 * 発光アウター防具テクスチャ取得
	 * @param Slot
	 * @return
	 */
	public ResourceLocation getLightTextureOuter(int slot) {
		if (this.modelConfigCompound != null && this.modelConfigCompound.getTextureBoxArmor() != null) {
			return this.modelConfigCompound.getTextureBoxArmor().getLightTextureOuterArmor(ItemStack.EMPTY);
		}
		return null;
	}
	
	
	
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 外側。
	 */
//	public ResourceLocation[] textureOuter;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 内側。
	 */
//	public ResourceLocation[] textureInner;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 外側・発光。
	 */
//	public ResourceLocation[] textureOuterLight;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 内側・発光。
	 */
//	public ResourceLocation[] textureInnerLight;
	/**
	 * 描画されるアーマーの部位。
	 * shouldRenderPassとかで指定する。
	 */
	public int renderParts;

	private float[] textureLightColor;
	public float[] getTextureLightColor() {
		return textureLightColor;
	}

	public ModelBaseDuo(RenderModelMulti<? extends EntityLiving> pRender) {
		rendererLivingEntity = pRender;
		renderParts = 0;
	}
	
	/**
	 * 防具モデルでは呼び出されていない
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
	}

	/**
	 * 防具モデルのsetLivingAnimations
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		
		if (modelInner != null) {
			modelInner.setLivingAnimations(entityCaps, limbSwing, limbSwingAmount, partialTickTime);
		}
		if (modelOuter != null) {
			modelOuter.setLivingAnimations(entityCaps, limbSwing, limbSwingAmount, partialTickTime);
		}
		
		isAlphablend = true;
	}

	/**
	 * アーマー描画処理
	 */
	public void render(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, int renderParts) {
//		boolean lri = (renderCount & 0x0f) == 0;
		//総合
		this.showArmorParts(renderParts);

		//Inner
		INNER:{
			if(this.getModelInner(renderParts) != null && this.getTextureInner(renderParts) != null){
				ResourceLocation texInner = this.getTextureInner(renderParts);
				if(texInner!=null && this.modelConfigCompound.isArmorVisible(0)) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(texInner);
						
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						
						this.getModelInner(renderParts).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, this.modelConfigCompound.getModelCaps());
						this.getModelInner(renderParts).setLivingAnimations(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, partialTicks);
						this.getModelInner(renderParts).render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
						
					} catch(Exception e) {
						break INNER;
					}
				}
			} else {
//				this.modelInner.render(lmm.maidCaps, par2, par3, lmm.ticksExisted, par5, par6, renderScale, true);
			}
		}

		// 発光Inner
		INNERLIGHT: if (this.getModelInner(renderParts) != null) {
			ResourceLocation texInnerLight = this.getLightTextureInner(renderParts);
			if (texInnerLight != null && this.modelConfigCompound.isArmorVisible(1)) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texInnerLight);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);

					RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
					if (this.getTextureLightColor() == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								this.getTextureLightColor()[0],
								this.getTextureLightColor()[1],
								this.getTextureLightColor()[2],
								this.getTextureLightColor()[3]);
					}
					this.getModelInner(renderParts).render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
					RendererHelper.setLightmapTextureCoords(this.lighting);
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
			if(this.getModelOuter(renderParts) != null && this.getTextureOuter(renderParts) != null){
				ResourceLocation texOuter = this.getTextureOuter(renderParts);
				if(texOuter!=null && this.modelConfigCompound.isArmorVisible(2)) try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuter);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					this.getModelOuter(renderParts).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, this.modelConfigCompound.getModelCaps());
					this.getModelOuter(renderParts).setLivingAnimations(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, partialTicks);
					this.getModelOuter(renderParts).render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
				}catch(Exception e){break OUTER;}
			}else{
//				this.modelOuter.render(lmm.maidCaps, limbSwing, par3, lmm.ticksExisted, par5, par6, renderScale, true);
			}
		}

		// 発光Outer
		OUTERLIGHT: if (this.getModelOuter(renderParts) != null) {
			ResourceLocation texOuterLight = this.getLightTextureOuter(renderParts);
			if (texOuterLight != null && this.modelConfigCompound.isArmorVisible(3)) {
				try{
					Minecraft.getMinecraft().getTextureManager().bindTexture(texOuterLight);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);

					RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
					if (this.getTextureLightColor() == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								this.getTextureLightColor()[0],
								this.getTextureLightColor()[1],
								this.getTextureLightColor()[2],
								this.getTextureLightColor()[3]);
					}
					if(this.modelConfigCompound.isArmorVisible(1)) this.getModelOuter(renderParts).render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderScale, true);
					RendererHelper.setLightmapTextureCoords(this.lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}catch(Exception e){ break OUTERLIGHT; }
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
	}
	
//	@Override
//	public TextureOffset getTextureOffset(String par1Str) {
//		return modelInner == null ? null : modelInner.getTextureOffset(par1Str);
//	}

//	@Override
//	public void setRotationAngles(float par1, float par2, float par3,
//			float par4, float par5, float par6, Entity par7Entity) {
//		if (modelInner != null) {
//			modelInner.setRotationAngles(par1, par2, par3, par4, par5, par6, entityCaps);
//		}
//		if (modelOuter != null) {
//			modelOuter.setRotationAngles(par1, par2, par3, par4, par5, par6, entityCaps);
//		}
//	}


	// IModelMMM追加分

//	@Override
//	public void renderItems(EntityLivingBase pEntity, Render pRender) {
//		if (modelInner != null) {
//			modelInner.renderItems(entityCaps);
//		}
//	}

//	@Override
//	public void showArmorParts(int pParts) {
//		if (modelInner != null) {
//			modelInner.showArmorParts(pParts, 0);
//		}
//		if (modelOuter != null) {
//			modelOuter.showArmorParts(pParts, 1);
//		}
//	}

	/**
	 * Renderer辺でこの変数を設定する。
	 * 設定値はIModelCapsを継承したEntitiyとかを想定。
	 */
	@Override
	public void setEntityCaps(IModelCaps pEntityCaps) {
		entityCaps = pEntityCaps;
		if (capsLink != null) {
			capsLink.setEntityCaps(pEntityCaps);
		}
	}

//	@Override
//	public void setRender(RenderModelMulti<? extends EntityLiving> pRender) {
//		if (modelInner != null) {
//			modelInner.render = pRender;
//		}
//		if (modelOuter != null) {
//			modelOuter.render = pRender;
//		}
//	}

	@Override
	public void setArmorRendering(boolean pFlag) {
		isRendering = pFlag;
	}


	// IModelBaseMMM追加分

	@Override
	public Map<String, Integer> getModelCaps() {
		return modelInner == null ? null : modelInner.getModelCaps();
	}

	@Override
	public Object getCapsValue(int pIndex, Object ... pArg) {
		return modelInner == null ? null : modelInner.getCapsValue(pIndex, pArg);
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		if (capsLink != null) {
			capsLink.setCapsValue(pIndex, pArg);
		}
		if (modelOuter != null) {
			modelOuter.setCapsValue(pIndex, pArg);
		}
		if (modelInner != null) {
			return modelInner.setCapsValue(pIndex, pArg);
		}
		return false;
	}

	@Override
	public void showAllParts() {
		if (modelInner != null) {
			modelInner.showAllParts();
		}
		if (modelOuter != null) {
			modelOuter.showAllParts();
		}
	}
	@Override
	public void showArmorParts(int pParts) {
	}

}
