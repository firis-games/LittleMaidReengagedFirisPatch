package net.blacklab.lmr.entity.maidmodel;

import org.lwjgl.opengl.GL11;

/**
 * LMM用に最適化
 */
public abstract class ModelLittleMaidBase extends ModelMultiMMMBase {

	//fields
	public ModelRenderer bipedTorso;
	public ModelRenderer bipedNeck;
	public ModelRenderer bipedHead;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedPelvic;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;
	public ModelRenderer Skirt;


	/**
	 * コンストラクタは全て継承させること
	 */
	public ModelLittleMaidBase() {
		super();
	}
	/**
	 * コンストラクタは全て継承させること
	 */
	public ModelLittleMaidBase(float psize) {
		super(psize);
	}
	/**
	 * コンストラクタは全て継承させること
	 */
	public ModelLittleMaidBase(float psize, float pyoffset, int pTextureWidth, int pTextureHeight) {
		super(psize, pyoffset, pTextureWidth, pTextureHeight);
	}


	@Override
	public void initModel(float psize, float pyoffset) {
		// 標準型
		// 手持ち
		Arms[0] = new ModelRenderer(this);
		Arms[0].setRotationPoint(-1F, 5F, -1F);
		Arms[1] = new ModelRenderer(this);
		Arms[1].setRotationPoint(1F, 5F, -1F);
		Arms[1].isInvertX = true;

		HeadMount.setRotationPoint(0F, -4F, 0F);
		HeadTop.setRotationPoint(0F, -13F, 0F);


		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setTextureOffset( 0,  0).addBox(-4F, -8F, -4F, 8, 8, 8, psize);		// Head
		bipedHead.setTextureOffset(24,  0).addBox(-4F, 0F, 1F, 8, 4, 3, psize);			// Hire
		bipedHead.setTextureOffset(24, 18).addBox(-4.995F, -7F, 0.2F, 1, 3, 3, psize);		// ChignonR
		bipedHead.setTextureOffset(24, 18).addBox(3.995F, -7F, 0.2F, 1, 3, 3, psize);		// ChignonL
		bipedHead.setTextureOffset(52, 10).addBox(-2F, -7.2F, 4F, 4, 4, 2, psize);		// ChignonB
		bipedHead.setTextureOffset(46, 20).addBox(-1.5F, -6.8F, 4F, 3, 9, 3, psize);	// Tail
		bipedHead.setTextureOffset(58, 21).addBox(-5.5F, -6.8F, 0.9F, 1, 8, 2, psize);	// SideTailR
		bipedHead.setMirror(true);
		bipedHead.setTextureOffset(58, 21).addBox(4.5F, -6.8F, 0.9F, 1, 8, 2, psize);	// SideTailL
		bipedHead.setRotationPoint(0F, 0F, 0F);

		bipedRightArm = new ModelRenderer(this, 48, 0);
		bipedRightArm.addBox(-2.0F, -1F, -1F, 2, 8, 2, psize);
		bipedRightArm.setRotationPoint(-3.0F, 1.5F, 0F);

		bipedLeftArm = new ModelRenderer(this, 56, 0);
		bipedLeftArm.addBox(0.0F, -1F, -1F, 2, 8, 2, psize);
		bipedLeftArm.setRotationPoint(3.0F, 1.5F, 0F);

		bipedRightLeg = new ModelRenderer(this, 32, 19);
		bipedRightLeg.addBox(-2F, 0F, -2F, 3, 9, 4, psize);
		bipedRightLeg.setRotationPoint(-1F, 0F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 32, 19);
		bipedLeftLeg.setMirror(true);
		bipedLeftLeg.addBox(-1F, 0F, -2F, 3, 9, 4, psize);
		bipedLeftLeg.setRotationPoint(1F, 0F, 0F);

		Skirt = new ModelRenderer(this, 0, 16);
		Skirt.addBox(-4F, -2F, -4F, 8, 8, 8, psize);
		Skirt.setRotationPoint(0F, 0F, 0F);

		bipedBody = new ModelRenderer(this, 32, 8);
		bipedBody.addBox(-3F, 0F, -2F, 6, 7, 4, psize);
		bipedBody.setRotationPoint(0F, 0F, 0F);

		bipedTorso = new ModelRenderer(this);
		bipedNeck = new ModelRenderer(this);
		bipedPelvic = new ModelRenderer(this);
		bipedPelvic.setRotationPoint(0F, 7F, 0F);
		mainFrame = new ModelRenderer(this, 0, 0);
		mainFrame.setRotationPoint(0F, 0F + pyoffset + 8F, 0F);
		mainFrame.addChild(bipedTorso);
		bipedTorso.addChild(bipedNeck);
		bipedTorso.addChild(bipedBody);
		bipedTorso.addChild(bipedPelvic);
		bipedNeck.addChild(bipedHead);
		bipedNeck.addChild(bipedRightArm);
		bipedNeck.addChild(bipedLeftArm);
		bipedHead.addChild(HeadMount);
		bipedHead.addChild(HeadTop);
		bipedRightArm.addChild(Arms[0]);
		bipedLeftArm.addChild(Arms[1]);
		bipedPelvic.addChild(bipedRightLeg);
		bipedPelvic.addChild(bipedLeftLeg);
		bipedPelvic.addChild(Skirt);
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
		return getHeight() * 0.9F;
	}

	@Override
	public float getMountedYOffset() {
		return 0.35F;
	}

	@Override
	public void setLivingAnimations(IModelCaps pEntityCaps, float par2, float par3, float pRenderPartialTicks) {
		float angle = ModelCapsHelper.getCapsValueFloat(pEntityCaps, caps_interestedAngle, (Float)pRenderPartialTicks);
		bipedHead.setRotateAngleZ(angle);
	}

	/**
	 * 姿勢制御用
	 * 独自追加分
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, IModelCaps pEntityCaps) {
		setDefaultPause(par1, par2, pTicksExisted, pHeadYaw, pHeadPitch, par6, pEntityCaps);

		if (isRiding) {
			// 乗り物に乗っている
			bipedRightArm.addRotateAngleX(-0.6283185F);
			bipedLeftArm.addRotateAngleX(-0.6283185F);
			bipedRightLeg.setRotateAngleX(-1.256637F);
			bipedLeftLeg.setRotateAngleX(-1.256637F);
			bipedRightLeg.setRotateAngleY(0.3141593F);
			bipedLeftLeg.setRotateAngleY(-0.3141593F);
//			mainFrame.rotationPointY += 5.00F;
		}
		
		//カスタム設定
		//お座りモーションの場合はモデル側で位置を調整する
		if (motionSitting) {
			mainFrame.rotationPointY += 5.00F;
		}

		// アイテム持ってるときの腕振りを抑える+表示角オフセット
		if (heldItem[1] != 0) {
			bipedLeftArm.setRotateAngleX(bipedLeftArm.getRotateAngleX() * 0.5F);
			bipedLeftArm.addRotateAngleDegX(-18F * heldItem[1]);
		}
		if (heldItem[0] != 0) {
			bipedRightArm.setRotateAngleX(bipedRightArm.getRotateAngleX() * 0.5F);
			bipedRightArm.addRotateAngleDegX(-18F * heldItem[0]);
		}

//		bipedRightArm.setRotateAngleY(0.0F);
//		bipedLeftArm.setRotateAngleY(0.0F);

		if ((onGrounds[0] > -9990F || onGrounds[1] > -9990F) && !aimedBow) {
			// 腕振り
			float f6, f7, f8;
			f6 = mh_sin(mh_sqrt(onGrounds[0]) * (float)Math.PI * 2.0F);
			f7 = mh_sin(mh_sqrt(onGrounds[1]) * (float)Math.PI * 2.0F);
			bipedTorso.setRotateAngleY((f6 - f7) * 0.2F);
			Skirt.addRotateAngleY(bipedTorso.rotateAngleY);
			bipedRightArm.addRotateAngleY(bipedTorso.rotateAngleY);
			bipedLeftArm.addRotateAngleY(bipedTorso.rotateAngleY);
			bipedPelvic.addRotateAngleY(-bipedTorso.rotateAngleY);
			bipedHead.addRotateAngleY(-bipedTorso.rotateAngleY);
			// R
			if (onGrounds[0] > 0F) {
				f6 = 1.0F - onGrounds[0];
				f6 *= f6;
				f6 *= f6;
				f6 = 1.0F - f6;
				f7 = mh_sin(f6 * (float)Math.PI);
				f8 = mh_sin(onGrounds[0] * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				bipedRightArm.addRotateAngleX(-f7 * 1.2F - f8);
				bipedRightArm.addRotateAngleY(bipedTorso.rotateAngleY * 2.0F);
				bipedRightArm.setRotateAngleZ(mh_sin(onGrounds[0] * 3.141593F) * -0.4F);
			} else {
				bipedRightArm.addRotateAngleX(bipedTorso.rotateAngleY);
			}
			// L
			if (onGrounds[1] > 0F) {
				f6 = 1.0F - onGrounds[1];
				f6 *= f6;
				f6 *= f6;
				f6 = 1.0F - f6;
				f7 = mh_sin(f6 * (float)Math.PI);
				f8 = mh_sin(onGrounds[1] * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				bipedLeftArm.rotateAngleX -= f7 * 1.2D + f8;
				bipedLeftArm.rotateAngleY += bipedTorso.rotateAngleY * 2.0F;
				bipedLeftArm.setRotateAngleZ(mh_sin(onGrounds[1] * 3.141593F) * 0.4F);
			} else {
				bipedLeftArm.rotateAngleX += bipedTorso.rotateAngleY;
			}
		}
		if(isSneak) {
			// しゃがみ
			bipedTorso.rotateAngleX += 0.5F;
			bipedNeck.rotateAngleX -= 0.5F;
			bipedRightArm.rotateAngleX += 0.2F;
			bipedLeftArm.rotateAngleX += 0.2F;

			bipedPelvic.addRotationPointY(-0.5F);
			bipedPelvic.addRotationPointZ(-0.6F);
			bipedPelvic.addRotateAngleX(-0.5F);
			bipedHead.rotationPointY = 1.0F;
//			Skirt.setRotationPoint(0.0F, 5.8F, 2.7F);
			Skirt.rotationPointY -= 0.25F;
			Skirt.rotationPointZ += 0.00F;
			Skirt.addRotateAngleX(0.2F);
			bipedTorso.rotationPointY += 1.00F;
		} else {
			// 通常立ち
		}
		if (isWait) {
			//待機状態の特別表示
			float lx = mh_sin(pTicksExisted * 0.067F) * 0.05F -0.7F;
			bipedRightArm.setRotateAngle(lx, 0.0F, -0.4F);
			bipedLeftArm.setRotateAngle(lx, 0.0F, 0.4F);
		} else {
			float la, lb, lc;
			if (aimedBow) {
				// 弓構え
				float lonGround = onGrounds[dominantArm];
				float f6 = mh_sin(lonGround * 3.141593F);
				float f7 = mh_sin((1.0F - (1.0F - lonGround) * (1.0F - lonGround)) * 3.141593F);
				la = 0.1F - f6 * 0.6F;
				bipedRightArm.setRotateAngle(-1.470796F, -la, 0.0F);
				bipedLeftArm.setRotateAngle(-1.470796F, la, 0.0F);
				la = bipedHead.rotateAngleX;
				lb = mh_sin(pTicksExisted * 0.067F) * 0.05F;
				lc = f6 * 1.2F - f7 * 0.4F;
				bipedRightArm.addRotateAngleX(la + lb - lc);
				bipedLeftArm.addRotateAngleX(la - lb - lc);
				la = bipedHead.rotateAngleY;
				bipedRightArm.addRotateAngleY(la);
				bipedLeftArm.addRotateAngleY(la);
				la = mh_cos(pTicksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.addRotateAngleZ(la);
				bipedLeftArm.addRotateAngleZ(-la);
			} else {
				// 通常
				la = mh_sin(pTicksExisted * 0.067F) * 0.05F;
				lc = 0.5F + mh_cos(pTicksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.addRotateAngleX(la);
				bipedLeftArm.addRotateAngleX(-la);
				bipedRightArm.addRotateAngleZ(lc);
				bipedLeftArm.addRotateAngleZ(-lc);
			}
		}
	}

	@Override
	public void setDefaultPause() {
	}

	@Override
	public void setDefaultPause(float par1, float par2, float pTicksExisted,
			float pHeadYaw, float pHeadPitch, float par6, IModelCaps pEntityCaps) {
		super.setDefaultPause(par1, par2, pTicksExisted, pHeadYaw, pHeadPitch, par6, pEntityCaps);
		mainFrame.setRotationPoint(0F, 8F, 0F);
		bipedTorso.setRotationPoint(0F, 0F, 0F).setRotateAngle(0F, 0F, 0F);
		bipedNeck.setRotationPoint(0F, 0F, 0F).setRotateAngle(0F, 0F, 0F);
		bipedPelvic.setRotationPoint(0F, 7F, 0F).setRotateAngle(0F, 0F, 0F);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.setRotateAngleDegY(pHeadYaw);
		bipedHead.setRotateAngleDegX(pHeadPitch);
		bipedBody.setRotationPoint(0F, 0F, 0F).setRotateAngle(0F, 0F, 0F);
		bipedRightArm.setRotationPoint(-3.0F, 1.6F, 0F);
		bipedRightArm.setRotateAngle(mh_cos(par1 * 0.6662F + 3.141593F) * 2.0F * par2 * 0.5F, 0F, 0F);
		bipedLeftArm.setRotationPoint(3.0F, 1.6F, 0F);
		bipedLeftArm.setRotateAngle(mh_cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F, 0F, 0F);
		bipedRightLeg.setRotationPoint(-1F, 0F, 0F);
		bipedRightLeg.setRotateAngle(mh_cos(par1 * 0.6662F) * 1.4F * par2, 0F, 0F);
		bipedLeftLeg.setRotationPoint(1F, 0F, 0F);
		bipedLeftLeg.setRotateAngle(mh_cos(par1 * 0.6662F + 3.141593F) * 1.4F * par2, 0F, 0F);

		Skirt.setRotationPoint(0F, 0F, 0F).setRotateAngle(0F, 0F, 0F);

	}

	@Override
	public void showAllParts() {
		// 表示制限を解除してすべての部品を表示
		bipedHead.setVisible(true);
		bipedBody.setVisible(true);
		bipedRightArm.setVisible(true);
		bipedLeftArm.setVisible(true);
		Skirt.setVisible(true);
		bipedRightLeg.setVisible(true);
		bipedLeftLeg.setVisible(true);
	}

	@Override
	public int showArmorParts(int parts, int index) {
		// 鎧の表示用
		boolean f;
		// 兜
		f = parts == 3 ? true : false;
		bipedHead.setVisible(f);
		// 鎧
		f = parts == 2 ? true : false;
		bipedBody.setVisible(f);
		bipedRightArm.setVisible(f);
		bipedLeftArm.setVisible(f);
		// 脚甲
		f = parts == 1 ? true : false;
		Skirt.setVisible(f);
		// 臑当
		f = parts == 0 ? true : false;
		bipedRightLeg.setVisible(f);
		bipedLeftLeg.setVisible(f);

		return -1;
	}

	@Override
	public void renderItems(IModelCaps pEntityCaps) {
		// 手持ちの表示
		GL11.glPushMatrix();
		// R
		Arms[0].loadMatrix();
		GL11.glTranslatef(0F, 0.05F, -0.05F);
		Arms[0].renderItems(this, pEntityCaps, false, 0);
		// L
		Arms[1].loadMatrix();
		GL11.glTranslatef(0F, 0.05F, -0.05F);
		Arms[1].renderItems(this, pEntityCaps, false, 1);
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
		GL11.glPopMatrix();
	}

	@Override
	public void renderFirstPersonHand(IModelCaps pEntityCaps) {
		float var2 = 1.0F;
		GL11.glColor3f(var2, var2, var2);
		onGrounds[0] = onGrounds[1] = 0.0F;
		setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, pEntityCaps);
		bipedRightArm.render(0.0625F);
	}

}
