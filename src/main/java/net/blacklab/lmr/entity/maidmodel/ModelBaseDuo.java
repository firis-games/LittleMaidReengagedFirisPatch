package net.blacklab.lmr.entity.maidmodel;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * アーマーの二重描画用クラス。
 * 必ずInner側にはモデルを設定すること。
 * 通常のRendererで描画するためのクラスなので、Renderをちゃんと記述するならいらないクラスです。
 */
public class ModelBaseDuo extends ModelBaseNihil implements IModelBaseMMM {

	public ModelMultiBase modelOuter;
	public ModelMultiBase modelInner;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 外側。
	 */
	public ResourceLocation[] textureOuter;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 内側。
	 */
	public ResourceLocation[] textureInner;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 外側・発光。
	 */
	public ResourceLocation[] textureOuterLight;
	/**
	 * 部位毎のアーマーテクスチャの指定。
	 * 内側・発光。
	 */
	public ResourceLocation[] textureInnerLight;
	/**
	 * 描画されるアーマーの部位。
	 * shouldRenderPassとかで指定する。
	 */
	public int renderParts;

	public float[] textureLightColor;

	public ModelBaseDuo(RenderLivingBase pRender) {
		rendererLivingEntity = pRender;
		renderParts = 0;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
		if (modelInner != null) {
			modelInner.setLivingAnimations(entityCaps, par2, par3, par4);
		}
		if (modelOuter != null) {
			modelOuter.setLivingAnimations(entityCaps, par2, par3, par4);
		}
		isAlphablend = true;
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		boolean lri = (renderCount & 0x0f) == 0;
		
		//法線の再計算
		//GlStateManager.enableRescaleNormal();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		if (modelInner != null) {
			if (textureInner != null && lri) {
				if (textureInner[renderParts] != null) {
					// 通常パーツ
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(textureInner[renderParts]);
						modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
					}catch(Exception e){
					}
				}
			} else {
				// ほぼエンチャントエフェクト用
				modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
			}
			if (textureInnerLight != null && renderCount == 0) {
				// 発光テクスチャ表示処理
				if (textureInnerLight[renderParts] != null) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(textureInnerLight[renderParts]);
					}catch(Exception e){
					}
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
					
					RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
					if (textureLightColor == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								textureLightColor[0],
								textureLightColor[1],
								textureLightColor[2],
								textureLightColor[3]);
					}
					modelInner.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
					RendererHelper.setLightmapTextureCoords(lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
				}
			}
		}
		GL11.glEnable(GL11.GL_BLEND);
		if (modelOuter != null) {
			if (textureOuter != null && lri) {
				// 通常パーツ
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				if (textureOuter[renderParts] != null) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(textureOuter[renderParts]);
						modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
					}catch(Exception e){
					}
				}
			} else {
				// ほぼエンチャントエフェクト用
				modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
			}
			if (textureOuterLight != null && renderCount == 0) {
				// 発光テクスチャ表示処理
				if (textureOuterLight[renderParts] != null) {
					try{
						Minecraft.getMinecraft().getTextureManager().bindTexture(textureOuterLight[renderParts]);
					}catch(Exception e){
					}
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
					
					RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
					if (textureLightColor == null) {
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					} else {
						//発光色を調整
						GL11.glColor4f(
								textureLightColor[0],
								textureLightColor[1],
								textureLightColor[2],
								textureLightColor[3]);
					}
					modelOuter.render(entityCaps, par2, par3, par4, par5, par6, par7, isRendering);
					RendererHelper.setLightmapTextureCoords(lighting);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
				}
			}
		}
//		isAlphablend = false;
		renderCount++;
	}

	@Override
	public TextureOffset getTextureOffset(String par1Str) {
		return modelInner == null ? null : modelInner.getTextureOffset(par1Str);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity par7Entity) {
		if (modelInner != null) {
			modelInner.setRotationAngles(par1, par2, par3, par4, par5, par6, entityCaps);
		}
		if (modelOuter != null) {
			modelOuter.setRotationAngles(par1, par2, par3, par4, par5, par6, entityCaps);
		}
	}


	// IModelMMM追加分

	@Override
	public void renderItems(EntityLivingBase pEntity, Render pRender) {
		if (modelInner != null) {
			modelInner.renderItems(entityCaps);
		}
	}

	@Override
	public void showArmorParts(int pParts) {
		if (modelInner != null) {
			modelInner.showArmorParts(pParts, 0);
		}
		if (modelOuter != null) {
			modelOuter.showArmorParts(pParts, 1);
		}
	}

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

	@Override
	public void setRender(Render pRender) {
		if (modelInner != null) {
			modelInner.render = pRender;
		}
		if (modelOuter != null) {
			modelOuter.render = pRender;
		}
	}

	@Override
	public void setArmorRendering(boolean pFlag) {
		isRendering = pFlag;
	}


	// IModelCaps追加分

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

}
