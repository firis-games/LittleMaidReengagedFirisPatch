package firis.lmmm.builtin.model;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.renderer.ModelRenderer;

/**
 * 瞬き付き基本形
 */
public class ModelLittleMaid_SR2 extends ModelLittleMaidBase {

	public ModelRenderer eyeR;
	public ModelRenderer eyeL;


	public ModelLittleMaid_SR2() {
		super();
	}
	public ModelLittleMaid_SR2(float psize) {
		super(psize);
	}
	public ModelLittleMaid_SR2(float psize, float pyoffset, int pTextureWidth, int pTextureHeight) {
		super(psize, pyoffset, pTextureWidth, pTextureHeight);
	}


	@Override
	public void initModel(float psize, float pyoffset) {
		super.initModel(psize, pyoffset);
		
		// 追加パーツ
		eyeR = new ModelRenderer(this, 32, 19);
		eyeR.addPlate(-4.0F, -5.0F, -4.001F, 4, 4, 0, psize);
		eyeR.setRotationPoint(0.0F, 0.0F, 0.0F);
		eyeL = new ModelRenderer(this, 42, 19);
		eyeL.addPlate(0.0F, -5.0F, -4.001F, 4, 4, 0, psize);
		eyeL.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(eyeR);
		bipedHead.addChild(eyeL);
	}

	@Override
	public void setLivingAnimations(IModelCaps pEntityCaps, float par2, float par3, float pRenderPartialTicks) {
		super.setLivingAnimations(pEntityCaps, par2, par3, pRenderPartialTicks);
		
		float f3 = entityTicksExisted + pRenderPartialTicks + entityIdFactor;
		// 目パチ
		if( 0 > mh_sin(f3 * 0.05F) + mh_sin(f3 * 0.13F) + mh_sin(f3 * 0.7F) + 2.55F) { 
			eyeR.setVisible(true);
			eyeL.setVisible(true);
		} else { 
			eyeR.setVisible(false);
			eyeL.setVisible(false);
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityCaps);
		if (aimedBow) {
			if (ModelCapsHelper.getCapsValueInt(entityCaps, caps_dominantArm) == 0) {
				eyeL.setVisible(true);
			} else {
				eyeR.setVisible(true);
			}
		}
	}

	@Override
	public String getUsingTexture() {
		return null;
	}

}
