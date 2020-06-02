package firis.lmlib.multimodel;

import org.lwjgl.opengl.GL11;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.ModelMultiBase;
import firis.lmmm.api.renderer.ModelRenderer;

public class ModelMulti_Steve extends ModelMultiBase {

	public ModelRenderer bipedHead;
	public ModelRenderer bipedHeadwear;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;
	public ModelRenderer bipedEars;
	public ModelRenderer bipedCloak;
	public ModelRenderer bipedTorso;
	public ModelRenderer bipedNeck;
	public ModelRenderer bipedPelvic;
	
	public ModelRenderer eyeR;
	public ModelRenderer eyeL;


	public ModelMulti_Steve() {
		super();
	}
	public ModelMulti_Steve(float psize) {
		super(psize);
	}
	public ModelMulti_Steve(float psize, float pyoffset, int pTextureWidth, int pTextureHeight) {
		super(psize, pyoffset, pTextureWidth, pTextureHeight);
	}

	@Override
	public void initModel(float size, float yOffset) {
		bipedCloak = new ModelRenderer(this, 0, 0);
		bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, size);
		bipedEars = new ModelRenderer(this, 24, 0);
		bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, size);
		
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, size);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, size + 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
		eyeL = new ModelRenderer(this, 0, 0);
		eyeL.addBox(0.0F, -5F, -4.001F, 4, 4, 0, size);
		eyeL.setRotationPoint(0.0F, 0.0F, 0.0F);
		eyeR = new ModelRenderer(this, 0, 4);
		eyeR.addBox(-4F, -5F, -4.001F, 4, 4, 0, size);
		eyeR.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		bipedBody = new ModelRenderer(this, 16, 16);
		bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, size);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

		bipedRightArm = new ModelRenderer(this, 40, 16);
		bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, size);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedLeftArm = new ModelRenderer(this, 40, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, size);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		
		HeadMount.setRotationPoint(0.0F, -4.0F, 0.0F);
		HeadTop.setRotationPoint(0.0F, -12.0F, 0.0F);
		Arms[0] = new ModelRenderer(this);
		Arms[0].setRotationPoint(-1.5F, 7.2F, -1F);
		Arms[1] = new ModelRenderer(this);
		Arms[1].setRotationPoint(1.5F, 7.2F, -1F);
		
		bipedTorso = new ModelRenderer(this);
		bipedNeck = new ModelRenderer(this);
		bipedPelvic = new ModelRenderer(this);
		
		mainFrame = new ModelRenderer(this);
		mainFrame.setRotationPoint(0F, yOffset, 0F);
		mainFrame.addChild(bipedTorso);
		bipedTorso.addChild(bipedNeck);
		bipedTorso.addChild(bipedPelvic);
		bipedTorso.addChild(bipedBody);
		bipedNeck.addChild(bipedHead);
		bipedHead.addChild(bipedHeadwear);
		bipedHead.addChild(bipedEars);
		bipedHead.addChild(HeadMount);
		bipedHead.addChild(HeadTop);
		bipedHead.addChild(eyeL);
		bipedHead.addChild(eyeR);
		bipedNeck.addChild(bipedRightArm);
		bipedNeck.addChild(bipedLeftArm);
		bipedPelvic.addChild(bipedRightLeg);
		bipedPelvic.addChild(bipedLeftLeg);
		bipedRightArm.addChild(Arms[0]);
		bipedLeftArm.addChild(Arms[1]);
		bipedBody.addChild(bipedCloak);
		
		bipedEars.showModel = false;
		bipedCloak.showModel = false;
		
	}

	@Override
	public void render(IModelCaps entityCaps, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale, boolean isRender) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityCaps);
		mainFrame.render(scale, isRender);
	}

	public void setDefaultPause(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, IModelCaps pEntityCaps) {
		// 初期姿勢
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F).setRotateAngleDeg(pHeadPitch, pHeadYaw, 0.0F);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedTorso.setRotationPoint(0.0F, 0.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedNeck.setRotationPoint(0.0F, 0.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
		bipedPelvic.setRotationPoint(0.0F, 0.0F, 0.0F).setRotateAngle(0.0F, 0.0F, 0.0F);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		setDefaultPause(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityCaps);
		
		// 腕ふり、腿上げ
		float lf1 = mh_cos(limbSwing * 0.6662F);
		float lf2 = mh_cos(limbSwing * 0.6662F + PI);
		this.bipedRightArm.rotateAngleX = lf2 * 2.0F * limbSwingAmount * 0.5F;
		this.bipedLeftArm.rotateAngleX = lf1 * 2.0F * limbSwingAmount * 0.5F;
		this.bipedRightLeg.rotateAngleX = lf1 * 1.4F * limbSwingAmount;
		this.bipedLeftLeg.rotateAngleX = lf2 * 1.4F * limbSwingAmount;
		
		
		if (isRiding) {
			bipedRightArm.addRotateAngleDegX(-36.0F);
			bipedLeftArm.addRotateAngleDegX(-36.0F);
			bipedRightLeg.addRotateAngleDegX(-72.0F);
			bipedLeftLeg.addRotateAngleDegX(-72.0F);
			bipedRightLeg.addRotateAngleDegY(18.0F);
			bipedLeftLeg.addRotateAngleDegY(-18.0F);
		}
		
		if (heldItem[0] > 0) {
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F;
			bipedRightArm.addRotateAngleDegX(-18.0F * heldItem[0]);
		}
		if (heldItem[1] > 0) {
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F;
			bipedLeftArm.addRotateAngleDegX(-18.0F * heldItem[1]);
		}
		
		float lf;
		if ((onGrounds[0] > -9990F || onGrounds[1] > -9990F) && !aimedBow) {
			// 腕振り
			lf = (float)Math.PI * 2.0F;
			lf1 = mh_sin(mh_sqrt(onGrounds[0]) * lf);
			lf2 = mh_sin(mh_sqrt(onGrounds[1]) * lf);
			bipedTorso.rotateAngleY = (lf1 - lf2) * 0.2F;
			
			// R
			if (onGrounds[0] > 0F) {
				lf = 1.0F - onGrounds[0];
				lf *= lf;
				lf *= lf;
				lf = 1.0F - lf;
				lf1 = mh_sin(lf * (float)Math.PI);
				lf2 = mh_sin(onGrounds[0] * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				bipedRightArm.addRotateAngleX(-lf1 * 1.2F - lf2);
				bipedRightArm.addRotateAngleY(bipedTorso.rotateAngleY * 2.0F);
				bipedRightArm.setRotateAngleZ(mh_sin(onGrounds[0] * 3.141593F) * -0.4F);
			} else {
				bipedRightArm.rotateAngleX += bipedTorso.rotateAngleY;
			}
			// L
			if (onGrounds[1] > 0F) {
				lf = 1.0F - onGrounds[1];
				lf *= lf;
				lf *= lf;
				lf = 1.0F - lf;
				lf1 = mh_sin(lf * (float)Math.PI);
				lf2 = mh_sin(onGrounds[1] * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				bipedLeftArm.addRotateAngleX(-lf1 * 1.2F - lf2);
				bipedLeftArm.addRotateAngleY(bipedTorso.rotateAngleY * 2.0F);
				bipedLeftArm.setRotateAngleZ(mh_sin(onGrounds[1] * 3.141593F) * 0.4F);
			} else {
				bipedLeftArm.rotateAngleX += bipedTorso.rotateAngleY;
			}
		}
		
		if (isSneak) {
			// しゃがみ
			bipedBody.rotationPointY = 2.0F;
			bipedTorso.rotateAngleX += 0.5F;
			bipedHead.rotationPointY += 1.0F;
			bipedNeck.rotateAngleX -= 0.5F;
			bipedRightArm.rotateAngleX += 0.4F;
			bipedLeftArm.rotateAngleX += 0.4F;
			bipedRightLeg.rotateAngleX -= 0.5F;
			bipedLeftLeg.rotateAngleX -= 0.5F;
			bipedRightLeg.setRotationPoint(-1.9F, 9.8F, -0.8F);
			bipedLeftLeg.setRotationPoint(1.9F, 9.8F, -0.8F);
			// 高さ調整
			bipedTorso.rotationPointY += 1.2F;
		}
		
		if (aimedBow) {
			lf1 = 0.0F;
			lf2 = 0.0F;
			bipedRightArm.rotateAngleZ = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.0F;
			lf = 0.1F - lf1 * 0.6F;
			bipedRightArm.rotateAngleY = -lf + bipedHead.rotateAngleY;
			bipedLeftArm.rotateAngleY = lf + bipedHead.rotateAngleY;
			lf = (float)Math.PI * 0.5F;
			bipedRightArm.rotateAngleX = -lf + bipedHead.rotateAngleX;
			bipedLeftArm.rotateAngleX = -lf + bipedHead.rotateAngleX;
			bipedRightArm.rotateAngleX -= lf1 * 1.2F - lf2 * 0.4F;
			bipedLeftArm.rotateAngleX -= lf1 * 1.2F - lf2 * 0.4F;
			if (ModelCapsHelper.getCapsValueInt(entityCaps, caps_dominantArm) == 0) {
				bipedLeftArm.rotateAngleY += 0.4F;
			} else {
				bipedRightArm.rotateAngleY += 0.4F;
			}
		}
		
		// 腕の揺らぎ
		lf = mh_cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleZ += lf;
		this.bipedLeftArm.rotateAngleZ -= lf;
		lf = mh_sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedRightArm.rotateAngleX += lf;
		this.bipedLeftArm.rotateAngleX -= lf;
		
	}

//	@Override
//	public void renderItems(IModelCaps pEntityCaps) {
//		// 手持ちの表示
//		GL11.glPushMatrix();
//		
//		// R
//		Arms[0].loadMatrix();
////		GL11.glTranslatef(0F, 0.05F, -0.05F);
//		Arms[0].renderItems(this, pEntityCaps, false, 0);
//		// L
//		Arms[1].loadMatrix();
////		GL11.glTranslatef(0F, 0.05F, -0.05F);
//		Arms[1].renderItems(this, pEntityCaps, false, 1);
//		// 頭部装飾品
//		boolean lplanter = ModelCapsHelper.getCapsValueBoolean(pEntityCaps, caps_isPlanter);
//		if (ModelCapsHelper.getCapsValueBoolean(pEntityCaps, caps_isCamouflage) || lplanter) {
//			if (lplanter) {
//				HeadTop.loadMatrix().renderItemsHead(this, pEntityCaps);
//			} else {
//				HeadMount.loadMatrix().renderItemsHead(this, pEntityCaps);
//			}
//		}
//		GL11.glPopMatrix();
//	}

	@Override
	public void renderFirstPersonHand(IModelCaps pEntityCaps) {
		// お手手の描画
		float var2 = 1.0F;
		GL11.glColor3f(var2, var2, var2);
		onGrounds[0] = onGrounds[1] = 0.0F;
		setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, pEntityCaps);
		bipedRightArm.render(0.0625F);
	}

	@Override
	public float[] getArmorModelsSize() {
		return new float[] {0.5F, 1.0F};
	}

	@Override
	public float getHeight() {
		return 1.8F;
	}

	@Override
	public float getWidth() {
		return 0.6F;
	}

	@Override
	public float getyOffset() {
		return 1.62F;
	}

	@Override
	public float getMountedYOffset() {
		return getHeight() * 0.75F;
	}

	@Override
	public boolean isItemHolder() {
		return true;
	}

	@Override
	public float getLeashOffset(IModelCaps pEntityCaps) {
		// TODO Auto-generated method stub
		return 0.2F;
	}

	@Override
	public int showArmorParts(int parts, int index) {
		if (index == 0) {
			bipedHead.isRendering = parts == 3;
			bipedHeadwear.isRendering = parts == 3;
			bipedBody.isRendering = parts == 1;
			bipedRightArm.isRendering = parts == 2;
			bipedLeftArm.isRendering = parts == 2;
			bipedRightLeg.isRendering = parts == 1;
			bipedLeftLeg.isRendering = parts == 1;
		} else {
			bipedHead.isRendering = parts == 3;
			bipedHeadwear.isRendering = parts == 3;
			bipedBody.isRendering = parts == 2;
			bipedRightArm.isRendering = parts == 2;
			bipedLeftArm.isRendering = parts == 2;
			bipedRightLeg.isRendering = parts == 0;
			bipedLeftLeg.isRendering = parts == 0;
		}
		return -1;
	}

}
