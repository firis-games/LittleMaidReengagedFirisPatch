package net.blacklab.lmr.entity.maidmodel;

import org.lwjgl.opengl.GL11;

/**
 * 旧型モデル互換のベースモデル。
 * 関節リンクしていない
 */
public class ModelLittleMaid_Archetype extends ModelLittleMaidBase {

	// fields
	public ModelRenderer bipedHeadwear;
	public ModelRenderer ChignonR;
	public ModelRenderer ChignonL;
	public ModelRenderer ChignonB;
	public ModelRenderer Tail;
	public ModelRenderer SideTailR;
	public ModelRenderer SideTailL;

	public ModelLittleMaid_Archetype() {
		super();
	}

	public ModelLittleMaid_Archetype(float f) {
		super(f);
	}

	public ModelLittleMaid_Archetype(float f, float f1, int pTextureWidth, int pTextureHeight) {
		super(f, f1, pTextureWidth, pTextureHeight);
	}

	@Override
	public void initModel(float psize, float pyoffset) {
		pyoffset += 8F;

		// 装備位置
		Arms = new ModelRenderer[1];
		Arms[0] = new ModelRenderer(this, 0, 0);
		Arms[0].setRotationPoint(-1F, 5F, -1F);
		HeadMount.setRotationPoint(0F, -4F, 0F);
		HeadTop.setRotationPoint(0F, -13F, 0F);

		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, psize);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(HeadMount);
		bipedHead.addChild(HeadTop);

		bipedHeadwear = new ModelRenderer(this, 24, 0);
		bipedHeadwear.addBox(-4F, 0F, 1F, 8, 4, 3, psize);
		bipedHeadwear.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(bipedHeadwear);

		bipedBody = new ModelRenderer(this, 32, 8);
		bipedBody.addBox(-3F, 0F, -2F, 6, 7, 4, psize);
		bipedBody.setRotationPoint(0F, 0F, 0F);

		bipedRightArm = new ModelRenderer(this, 48, 0);
		bipedRightArm.addBox(-2.0F, -1F, -1F, 2, 8, 2, psize);
		bipedRightArm.setRotationPoint(-3.0F, 1.5F, 0F);
		bipedRightArm.addChild(Arms[0]);

		bipedLeftArm = new ModelRenderer(this, 56, 0);
		bipedLeftArm.addBox(0.0F, -1F, -1F, 2, 8, 2, psize);
		bipedLeftArm.setRotationPoint(3.0F, 1.5F, 0F);

		bipedRightLeg = new ModelRenderer(this, 32, 19);
		bipedRightLeg.addBox(-2F, 0F, -2F, 3, 9, 4, psize);
		bipedRightLeg.setRotationPoint(-1F, 7F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 32, 19);
		bipedLeftLeg.setMirror(true);
		bipedLeftLeg.addBox(-1F, 0F, -2F, 3, 9, 4, psize);
		bipedLeftLeg.setRotationPoint(1F, 7F, 0F);

		Skirt = new ModelRenderer(this, 0, 16);
		Skirt.addBox(-4F, -2F, -4F, 8, 8, 8, psize);
		Skirt.setRotationPoint(0F, 7F, 0F);

		ChignonR = new ModelRenderer(this, 24, 18);
		ChignonR.addBox(-5F, -7F, 0.2F, 1, 3, 3, psize);
		ChignonR.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(ChignonR);

		ChignonL = new ModelRenderer(this, 24, 18);
		ChignonL.addBox(4F, -7F, 0.2F, 1, 3, 3, psize);
		ChignonL.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(ChignonL);

		ChignonB = new ModelRenderer(this, 52, 10);
		ChignonB.addBox(-2F, -7.2F, 4F, 4, 4, 2, psize);
		ChignonB.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(ChignonB);

		Tail = new ModelRenderer(this, 46, 20);
		Tail.addBox(-1.5F, -6.8F, 4F, 3, 9, 3, psize);
		Tail.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(Tail);

		SideTailR = new ModelRenderer(this, 58, 21);
		SideTailR.addBox(-5.5F, -6.8F, 0.9F, 1, 8, 2, psize);
		SideTailR.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(SideTailR);

		SideTailL = new ModelRenderer(this, 58, 21);
		SideTailL.setMirror(true);
		SideTailL.addBox(4.5F, -6.8F, 0.9F, 1, 8, 2, psize);
		SideTailL.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(SideTailL);

		mainFrame = new ModelRenderer(this, 0, 0);
		mainFrame.setRotationPoint(0F, 0F + pyoffset, 0F);
		mainFrame.addChild(bipedHead);
		mainFrame.addChild(bipedBody);
		mainFrame.addChild(bipedRightArm);
		mainFrame.addChild(bipedLeftArm);
		mainFrame.addChild(bipedRightLeg);
		mainFrame.addChild(bipedLeftLeg);
		mainFrame.addChild(Skirt);

	}

	@Override
	public String getUsingTexture() {
		return null;
	}

	public float getHeight() {
		// 身長
		return 1.35F;
	}

	public float getWidth() {
		// 横幅
		return 0.5F;
	}

	public void equippedBlockPosition() {
		// 手持ちブロックの表示位置
		GL11.glTranslatef(0.0F, 0.1275F, -0.3125F);
	}

	public void equippedItemPosition3D() {
		// 手持ち３Dアイテムの表示位置
		GL11.glTranslatef(0.02F, 0.1300F, 0.0F);
	}

	public void equippedItemPosition() {
		// 手持ちアイテムの表示位置
		GL11.glTranslatef(0.20F, 0.0800F, -0.0875F);
	}

	public void equippedHeadItemPosition() {
		// 頭部着装アイテムの表示位置
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
	}

	public void equippedItemBow() {
		// 手持ち弓の表示位置
		// GL11.glTranslatef(-0.07F, 0.005F, 0.3F);
		equippedItemPosition3D();
		// GL11.glTranslatef(-0.09F, -0.125F, 0.3F);
		GL11.glTranslatef(-0.05F, -0.075F, 0.1F);
	}

	@SuppressWarnings("deprecation")
	@Deprecated
	public boolean isItemHolder() {
		// アイテムを持っているときに手を前に出すかどうか。
		return false;
	}

	@Override
	public void setLivingAnimations(IModelCaps pEntityCaps, float par2, float par3, float pRenderPartialTicks) {
		super.setLivingAnimations(pEntityCaps, par2, par3, pRenderPartialTicks);
		float f3 = ModelCapsHelper.getCapsValueFloat(pEntityCaps, caps_interestedAngle, pRenderPartialTicks);
		bipedHead.rotateAngleZ = f3;
//		bipedHeadwear.rotateAngleZ = f3;
	}

	@Override
	public void setRotationAngles(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, IModelCaps pEntityCaps) {
//		super.setRotationAnglesMM(par1, par2, pTicksExisted, pHeadYaw, pHeadPitch, par6);
		
		bipedHead.rotateAngleY = pHeadYaw / 57.29578F;
		bipedHead.rotateAngleX = pHeadPitch / 57.29578F;
//		bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
		bipedHeadwear.rotateAngleX = 0F;
		bipedRightArm.rotateAngleX = mh_cos(par1 * 0.6662F + 3.141593F) * 2.0F * par2 * 0.5F;
		bipedLeftArm.rotateAngleX = mh_cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightLeg.rotateAngleX = mh_cos(par1 * 0.6662F) * 1.4F * par2;
		bipedLeftLeg.rotateAngleX = mh_cos(par1 * 0.6662F + 3.141593F) * 1.4F * par2;
		bipedRightLeg.rotateAngleY = 0.0F;
		bipedLeftLeg.rotateAngleY = 0.0F;

		if (isRiding) {
			// 乗り物に乗っている
			bipedRightArm.rotateAngleX += -0.6283185F;
			bipedLeftArm.rotateAngleX += -0.6283185F;
			bipedRightLeg.rotateAngleX = -1.256637F;
			bipedLeftLeg.rotateAngleX = -1.256637F;
			bipedRightLeg.rotateAngleY = 0.3141593F;
			bipedLeftLeg.rotateAngleY = -0.3141593F;
		}
		// アイテム持ってるときの腕振りを抑える
		if (heldItem[1] != 0) {
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F
					- 0.3141593F * heldItem[1];
		}
		if (heldItem[0] != 0) {
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F
					- 0.3141593F * heldItem[0];
		}
		
		bipedRightArm.rotateAngleY = 0.0F;
		bipedLeftArm.rotateAngleY = 0.0F;
		float lonGround = onGrounds[dominantArm];
		if (lonGround > -9990F && !aimedBow) {
			// 腕振り
			float f6 = lonGround;
			bipedBody.rotateAngleY = mh_sin(mh_sqrt(f6) * 3.141593F * 2.0F) * 0.2F;
			Skirt.rotateAngleY = bipedBody.rotateAngleY;
			bipedRightArm.rotationPointZ = mh_sin(bipedBody.rotateAngleY) * 4F;
			bipedRightArm.rotationPointX = -mh_cos(bipedBody.rotateAngleY) * 4F + 1.0F;
			bipedLeftArm.rotationPointZ = -mh_sin(bipedBody.rotateAngleY) * 4F;
			bipedLeftArm.rotationPointX = mh_cos(bipedBody.rotateAngleY) * 4F - 1.0F;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
			f6 = 1.0F - lonGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			float f7 = mh_sin(f6 * 3.141593F);
			float f8 = mh_sin(lonGround * 3.141593F)
					* -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
			bipedRightArm.rotateAngleX -= f7 * 1.2D + f8;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
			bipedRightArm.rotateAngleZ = mh_sin(lonGround * 3.141593F) * -0.4F;
		}
		if (isSneak) {
			// しゃがみ
			bipedBody.rotateAngleX = 0.5F;
			bipedRightLeg.rotateAngleX -= 0.0F;
			bipedLeftLeg.rotateAngleX -= 0.0F;
			bipedRightArm.rotateAngleX += 0.4F;
			bipedLeftArm.rotateAngleX += 0.4F;
			bipedRightLeg.rotationPointZ = 3F;
			bipedLeftLeg.rotationPointZ = 3F;
			bipedRightLeg.rotationPointY = 6F;
			bipedLeftLeg.rotationPointY = 6F;
			bipedHead.rotationPointY = 1.0F;
			bipedHeadwear.rotationPointY = 1.0F;
			bipedHeadwear.rotateAngleX += 0.5F;
			Skirt.rotationPointY = 5.8F;
			Skirt.rotationPointZ = 2.7F;
			Skirt.rotateAngleX = 0.2F;
		} else {
			// 通常立ち
			bipedBody.rotateAngleX = 0.0F;
			bipedRightLeg.rotationPointZ = 0.0F;
			bipedLeftLeg.rotationPointZ = 0.0F;
			bipedRightLeg.rotationPointY = 7F;
			bipedLeftLeg.rotationPointY = 7F;
			bipedHead.rotationPointY = 0.0F;
			bipedHeadwear.rotationPointY = 0.0F;
			Skirt.rotationPointY = 7.0F;
			Skirt.rotationPointZ = 0.0F;
			Skirt.rotateAngleX = 0.0F;
		}
		if (isWait) {
			// 待機状態の特別表示
			bipedRightArm.rotateAngleX = mh_sin(pTicksExisted * 0.067F) * 0.05F - 0.7F;
			bipedRightArm.rotateAngleY = 0.0F;
			bipedRightArm.rotateAngleZ = -0.4F;
			bipedLeftArm.rotateAngleX = mh_sin(pTicksExisted * 0.067F) * 0.05F - 0.7F;
			bipedLeftArm.rotateAngleY = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.4F;
		} else {
			if (aimedBow) {
				// 弓構え
				float f6 = mh_sin(lonGround * 3.141593F);
				float f7 = mh_sin((1.0F - (1.0F - lonGround)
						* (1.0F - lonGround)) * 3.141593F);
				bipedRightArm.rotateAngleZ = 0.0F;
				bipedLeftArm.rotateAngleZ = 0.0F;
				bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
				bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
				// bipedRightArm.rotateAngleX = -1.570796F;
				// bipedLeftArm.rotateAngleX = -1.570796F;
				bipedRightArm.rotateAngleX = -1.470796F;
				bipedLeftArm.rotateAngleX = -1.470796F;
				bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedRightArm.rotateAngleZ += mh_cos(pTicksExisted * 0.09F) * 0.05F + 0.05F;
				bipedLeftArm.rotateAngleZ -= mh_cos(pTicksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.rotateAngleX += mh_sin(pTicksExisted * 0.067F) * 0.05F;
				bipedLeftArm.rotateAngleX -= mh_sin(pTicksExisted * 0.067F) * 0.05F;
				bipedRightArm.rotateAngleX += bipedHead.rotateAngleX;
				bipedLeftArm.rotateAngleX += bipedHead.rotateAngleX;
				bipedRightArm.rotateAngleY += bipedHead.rotateAngleY;
				bipedLeftArm.rotateAngleY += bipedHead.rotateAngleY;
			} else {
				// 通常
				bipedRightArm.rotateAngleZ += 0.5F;
				bipedLeftArm.rotateAngleZ -= 0.5F;
				bipedRightArm.rotateAngleZ += mh_cos(pTicksExisted * 0.09F) * 0.05F + 0.05F;
				bipedLeftArm.rotateAngleZ -= mh_cos(pTicksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.rotateAngleX += mh_sin(pTicksExisted * 0.067F) * 0.05F;
				bipedLeftArm.rotateAngleX -= mh_sin(pTicksExisted * 0.067F) * 0.05F;
			}
		}

	}

	@Override
	public void renderItems(IModelCaps pEntityCaps) {
		// 手持ちの表示
		GL11.glPushMatrix();
		if (pEntityCaps != null) {
			int ldominant = ModelCapsHelper.getCapsValueInt(pEntityCaps, caps_dominantArm);
			Arms[0].loadMatrix().renderItems(this, pEntityCaps, false, ldominant);
			// 頭部装飾品
			boolean lplanter = ModelCapsHelper.getCapsValueBoolean(pEntityCaps, caps_isPlanter);
			if (ModelCapsHelper.getCapsValueBoolean(pEntityCaps, caps_isCamouflage) || lplanter) {
				HeadMount.loadMatrix();
				if (lplanter) {
					HeadTop.loadMatrix().renderItemsHead(this, pEntityCaps);
				} else {
					HeadMount.loadMatrix().renderItemsHead(this, pEntityCaps);
				}
			}
		}
		GL11.glPopMatrix();
	}

}
