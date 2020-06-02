package firis.lmmm.builtin.model;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.parts.ModelBox;
import firis.lmmm.api.model.parts.ModelPlate;
import firis.lmmm.api.renderer.ModelRenderer;

/**
 * サンプルとしてaddPartsを使用しています。
 */
public class ModelLittleMaid_Aug extends ModelLittleMaid_SR2 {
	
	public ModelRenderer shaggyB;
	public ModelRenderer shaggyR;
	public ModelRenderer shaggyL;
	
	public ModelRenderer SideTailR;
	public ModelRenderer SideTailL;

	public ModelRenderer sensor1;
	public ModelRenderer sensor2;
	public ModelRenderer sensor3;
	public ModelRenderer sensor4;

	
	public ModelLittleMaid_Aug() {
		super();
	}
	public ModelLittleMaid_Aug(float psize) {
		super(psize);
	}
	public ModelLittleMaid_Aug(float psize, float pyoffset, int pTextureWidth, int pTextureHeight) {
		super(psize, pyoffset, pTextureWidth, pTextureHeight);
	}


	@Override
	public void initModel(float size, float yOffset) {
		super.initModel(size, yOffset);
		
		// 再構成パーツ
		SideTailR = new ModelRenderer(this);
		SideTailR.setTextureOffset(46, 20).addParts(ModelBox.class, -1.5F, -0.5F, -1.0F, 2, 10, 2, size);
		SideTailR.setRotationPoint(-5F, -7.8F, 1.9F);
		SideTailL = new ModelRenderer(this);
		SideTailL.setTextureOffset(54, 20).addParts(ModelBox.class, 0.5F, -0.5F, -1.0F, 2, 10, 2, size);
		SideTailL.setRotationPoint(4F, -7.8F, 1.9F);
		
		
		// 増加パーツ
		shaggyB = new ModelRenderer(this, 24, 0);
		shaggyB.addParts(ModelPlate.class, -5.0F, 0.0F, 0.0F, 10, 4, 4, size);
		shaggyB.setRotationPoint(0.0F, -1.0F, 4.0F);
		shaggyB.setRotateAngleX(0.4F);
		shaggyR = new ModelRenderer(this, 34, 4);
		shaggyR.addParts(ModelPlate.class, 0.0F, 0.0F, -5.0F, 10, 4, 1, size);
		shaggyR.setRotationPoint(4.0F, -1.0F, 0.0F);
		shaggyR.setRotateAngleZ(-0.4F);
		shaggyL = new ModelRenderer(this, 24, 4);
		shaggyL.addParts(ModelPlate.class, 0.0F, 0.0F, -5.0F, 10, 4, 5, size);
		shaggyL.setRotationPoint(-4.0F, -1.0F, 0.0F);
		shaggyL.setRotateAngleZ(0.4F);
		
		sensor1 = new ModelRenderer(this, 0, 0);
		sensor1.addParts(ModelPlate.class, -8.0F, -4.0F, 0.0F, 8, 4, 0);
		sensor1.setRotationPoint(0.0F, -8.0F + yOffset, 0.0F);
		sensor2 = new ModelRenderer(this, 0, 4);
		sensor2.addParts(ModelPlate.class, 0.0F, -4.0F, 0.0F, 8, 4, 0);
		sensor2.setRotationPoint(0.0F, -8.0F + yOffset, 0.0F);
		sensor3 = new ModelRenderer(this, 44, 0);
		sensor3.addParts(ModelPlate.class, 0.0F, -7.0F, -4.0F, 4, 8, 1);
		sensor3.setRotationPoint(0.0F, -8.0F + yOffset, 0.0F);
		sensor4 = new ModelRenderer(this, 34, 0);
		sensor4.addParts(ModelPlate.class, 0.0F, -4.0F, -10.0F, 10, 4, 1);
		sensor4.setRotationPoint(0.0F, -8.0F + yOffset, 0.0F);
		
		
		// 変更パーツ
		bipedHead.clearCubeList();
		bipedHead.setMirror(false);
		bipedHead.setTextureOffset( 0,  0).addParts(ModelBox.class, -4F, -8F, -4F, 8, 8, 8, size);		// Head
		bipedHead.setTextureOffset( 0, 18).addParts(ModelBox.class, -5F, -8.5F, 0.2F, 1, 3, 3, size);	// ChignonR
		bipedHead.setTextureOffset(24, 18).addParts(ModelBox.class, 4F, -8.5F, 0.2F, 1, 3, 3, size);		// ChignonL
		bipedHead.setTextureOffset(52, 10).addParts(ModelBox.class, -7.5F, -9.5F, 0.9F, 4, 3, 2, size);	// sidetailUpperR
		bipedHead.setTextureOffset(52, 15).addParts(ModelBox.class, 3.5F, -9.5F, 0.9F, 4, 3, 2, size);	// sidetailUpperL
		bipedHead.setRotationPoint(0F, 0F, 0F);
		
		bipedHead.addChild(HeadMount);
		bipedHead.addChild(HeadTop);
		bipedHead.addChild(SideTailR);
		bipedHead.addChild(SideTailL);
		bipedHead.addChild(shaggyB);
		bipedHead.addChild(shaggyR);
		bipedHead.addChild(shaggyL);
		bipedHead.addChild(sensor1);
		bipedHead.addChild(sensor2);
		bipedHead.addChild(sensor3);
		bipedHead.addChild(sensor4);
		bipedHead.addChild(eyeR);
		bipedHead.addChild(eyeL);
		
	}

	@Override
	public void setLivingAnimations(IModelCaps entityCaps, float limbSwing, float limbSwingAmount, float partialTickTime) {
		super.setLivingAnimations(entityCaps, limbSwing, limbSwingAmount, partialTickTime);
		
		float f3 = entityTicksExisted + partialTickTime + entityIdFactor;
		float f4;
		if (ModelCapsHelper.getCapsValueBoolean(entityCaps, caps_isLookSuger)) {
			f3 *= 8.0F;
			f4 = -0.2F;
		} else {
			f4 = (1F - ModelCapsHelper.getCapsValueInt(entityCaps, caps_health) / 20F) * 0.5F;
		}
		float f5 = mh_sin(f3 * 0.067F) * 0.05F - f4;
		float f6 = 40.0F / 57.29578F;
		sensor1.setRotateAngle(f5, -f6, f5);
		sensor2.setRotateAngle(-f5, f6, -f5);
		sensor3.setRotateAngle(mh_sin(f3 * 0.067F) * 0.05F - 1.2F - f4, mh_sin(f3 * 0.09F) * 0.4F, mh_cos(f3 * 0.09F) * 0.2F);
		sensor4.setRotateAngle(mh_sin(f3 * 0.067F) * 0.05F + f4, mh_cos(f3 * 0.09F) * 0.5F, mh_sin(f3 * 0.09F) * 0.2F);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityCaps);
		
		SideTailR.setRotateAngleX(SideTailL.setRotateAngleX(bipedHead.getRotateAngleX() * -0.666666666F));
	}

}
