package net.blacklab.lmr.entity.maidmodel.lmmodel;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiMMMBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.blacklab.lmr.entity.maidmodel.renderer.ModelRenderer;

/**
 * スタビライザー搭載機
 */
@Deprecated
public class ModelLittleMaid_AC extends ModelMultiMMMBase {

	public ModelRenderer bipedHead;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;
	public ModelRenderer Skirt;

	public ModelRenderer ChignonR;
	public ModelRenderer ChignonL;
	public ModelRenderer ChignonB;
	public ModelRenderer Tail;
	public ModelRenderer SideTailR;
	public ModelRenderer SideTailL;

	public ModelLittleMaid_AC() {
		super();
	}

	public ModelLittleMaid_AC(float psize) {
		super(psize);
	}

	public ModelLittleMaid_AC(float psize, float pyoffset) {
		super(psize, pyoffset, 64, 32);
	}

	@Override
	public void initModel(float psize, float pyoffset) {
		/*
		Arms = new ModelRenderer[18];
		// バイプロダクトエフェクター
		Arms[2] = new ModelRenderer(this, 0, 0);
		Arms[2].setRotationPoint(-3F, 9F, 6F);
		Arms[2].setRotateAngleDeg(45F, 0F, 0F);
		Arms[3] = new ModelRenderer(this, 0, 0);
		Arms[3].setRotationPoint(3F, 9F, 6F);
		Arms[3].setRotateAngleDeg(45F, 0F, 0F);
		Arms[3].isInvertX = true;
		// テールソード
		Arms[4] = new ModelRenderer(this, 0, 0);
		Arms[4].setRotationPoint(-2F, 0F, 0F);
		Arms[4].setRotateAngleDeg(180F, 0F, 0F);
		Arms[5] = new ModelRenderer(this, 0, 0);
		Arms[5].setRotationPoint(2F, 0F, 0F);
		Arms[5].setRotateAngleDeg(180F, 0F, 0F);
		*/
		//
//		Arms[2].setRotateAngle(-0.78539816339744830961566084581988F - bipedRightArm.getRotateAngleX(), 0F, 0F);
//		Arms[3].setRotateAngle(-0.78539816339744830961566084581988F - bipedLeftArm.getRotateAngleX(), 0F, 0F);

	}

	@Override
	public float[] getArmorModelsSize() {
		return new float[] {0.1F, 0.5F};
	}

	@Override
	public float getHeight() {
		return 1.35F;
	}

	@Override
	public float getWidth() {
		return 0.5F;
	}
	
	@Override
	public float getyOffset() {
		return 1.215F;
	}
	
	@Override
	public float getMountedYOffset() {
		return 0.35F;
	}
	
//	@Override
//	public void renderItems(IModelCaps pEntityCaps) {
//	}

	@Override
	public void renderFirstPersonHand(IModelCaps pEntityCaps) {
		// TODO Auto-generated method stub
		
	}

}
