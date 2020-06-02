package firis.lmlib.api.client.model;

import org.lwjgl.opengl.GL11;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

/**
 * アーマーの二重描画用クラス。
 * 必ずInner側にはモデルを設定すること。
 * 通常のRendererで描画するためのクラスなので、Renderをちゃんと記述するならいらないクラスです。
 */
public class LMModelArmor extends LMModelBase {

	/**
	 * 内部定数
	 */
	private static final int ARMOR_INNER_ID = 0;
	private static final int ARMOR_OUTER_ID = 1;
	private static final float RENDER_SCALE = 0.0625F;
	
	/**
	 * インナー防具モデル
	 */
	private ModelMultiBase modelInner;
	
	/**
	 * アウター防具モデル
	 */
	private ModelMultiBase modelOuter;
	
	/**
	 * インナー防具テクスチャ
	 */
	private ResourceLocation textureInner;
	
	/**
	 * 発光インナー防具テクスチャ
	 */
	private ResourceLocation textureInnerLight;
	
	/**
	 * インナー防具テクスチャ
	 */
	private ResourceLocation textureOuter;
	
	/**
	 * 発光インナー防具テクスチャ
	 */
	private ResourceLocation textureOuterLight;

	/**
	 * コンストラクタ
	 */
	public LMModelArmor() {}
	
	/**
	 * 描画用パラメータを設定する
	 * @param modelConfigCompound
	 * 
	 * 描画用のモデル、テクスチャパスを内部変数へ展開する
	 * 
	 */
	public void initModelParameter(IModelCompound modelConfigCompound, float entityYaw, float partialTicks, EntityEquipmentSlot slot) {
		
		this.modelConfigCompound = modelConfigCompound;
		
		//内部変数リセット
		this.modelInner = null;
		this.modelOuter = null;
		this.textureInner = null;
		this.textureInnerLight = null;
		this.textureOuter = null;
		this.textureOuterLight = null;
		this.textureLightColor = null;
		
		//設定対象がない場合は処理を中断
		if (this.modelConfigCompound == null) return;
		
		//各内部変数に設定する
		//防具モデル
		this.modelInner = this.modelConfigCompound.getModelInnerArmor(slot);
		this.modelOuter = this.modelConfigCompound.getModelOuterArmor(slot);
		
		//テクスチャ
		this.textureInner = this.modelConfigCompound.getTextureInnerArmor(slot);
		this.textureInnerLight = this.modelConfigCompound.getLightTextureInnerArmor(slot);
		this.textureOuter = this.modelConfigCompound.getTextureOuterArmor(slot);
		this.textureOuterLight = this.modelConfigCompound.getLightTextureOuterArmor(slot);
		
		//ModelCapsData設定
		this.entityCaps = this.modelConfigCompound.getModelCaps();
		
		//マルチモデル情報の初期化
		this.entityCaps.initModelMultiBase(this.modelInner, entityYaw, partialTicks);
		this.entityCaps.initModelMultiBase(this.modelOuter, entityYaw, partialTicks);
		
		//モデルの表示設定
		if (this.modelInner != null) {
			this.modelInner.showArmorParts(slot.getIndex(), ARMOR_INNER_ID);
		}
		if (this.modelOuter != null) {
			this.modelOuter.showArmorParts(slot.getIndex(), ARMOR_OUTER_ID);
		}
		
		//色設定
		//インナー防具モデルより取得
		this.textureLightColor = null;
		if (this.modelInner != null) {
			this.textureLightColor = (float[])this.modelInner.getCapsValue(IModelCaps.caps_textureLightColor, this.entityCaps);
		}
		
		//ライティング設定
		this.lighting = this.modelConfigCompound.getBrightnessForRender();
		
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
//		isAlphablend = true;
	}

	/**
	 * アーマー描画処理
	 */
	public void render(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, int renderParts) {
		
		//法線再計算
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//OpenGL設定
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//インナー
		INNER:{
			if(this.modelInner != null 
					&& this.textureInner != null 
					&& this.modelConfigCompound.isArmorTypeVisible(0)){
				try {
					
					//テクスチャバインド
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureInner);
					
					//モデル描画
					this.modelInner.render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, RENDER_SCALE, true);
					
				} catch(Exception e) {
					break INNER;
				}
			}
		}
		
		//発光インナー
		INNER_LIGHT:{
			if(this.modelInner != null 
					&& this.textureInnerLight != null 
					&& this.modelConfigCompound.isArmorTypeVisible(1)){
				try {
					
					//テクスチャバインド
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureInnerLight);
					
					//発光テクスチャ用事前設定
					this.glLightTexturePre();
					
					//モデル描画
					this.modelInner.render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, RENDER_SCALE, true);
					
					//発光テクスチャの事後設定
					this.glLightTexturePost();
					
				} catch(Exception e) {
					break INNER_LIGHT;
				}
			}
		}

		//インナー
		OUTER:{
			if(this.modelOuter != null 
					&& this.textureOuter != null 
					&& this.modelConfigCompound.isArmorTypeVisible(2)){
				try {
					
					//テクスチャバインド
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureOuter);
					
					//モデル描画
					this.modelOuter.render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, RENDER_SCALE, true);
					
				} catch(Exception e) {
					break OUTER;
				}
			}
		}
		
		//発光アウター
		OUTER_LIGHT:{
			if(this.modelOuter != null 
					&& this.textureOuterLight != null 
					&& this.modelConfigCompound.isArmorTypeVisible(3)){
				try {
					
					//テクスチャバインド
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.textureOuterLight);
					
					//発光テクスチャ用事前設定
					this.glLightTexturePre();
					
					//モデル描画
					this.modelOuter.render(this.modelConfigCompound.getModelCaps(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, RENDER_SCALE, true);
					
					//発光テクスチャの事後設定
					this.glLightTexturePost();
					
				} catch (Exception e) {
					break OUTER_LIGHT;
				}
			}
		}
		
		//OpenGL設定解除
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * アーマーモデルの全部位表示設定
	 */
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
