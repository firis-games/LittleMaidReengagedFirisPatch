package net.firis.lmt.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 解析用メイドテストモデル
 * ModelLittleMaidBaseをMCモデルとして書き換え対応
 *
 */
@SideOnly(Side.CLIENT)
public class ModelLittleMaidTest extends ModelBase {
	
	//マルチモデル全体
	//人型以外を含むメイドさんの全体オブジェクト
	//このオブジェクトをベースに位置調整や描画を行っている
	public ModelRenderer mainFrame;
	
	//メイド全体
	//デフォルトメイドさんの全体
	public ModelRenderer bipedTorso;
	
	//首のグループ（頭と両手）
	public ModelRenderer bipedNeck;
	
	//下半身グループ
	public ModelRenderer bipedPelvic;
	
	//身体の部位
	//**********
	//頭
	public ModelRenderer bipedHead;
	//右腕
	public ModelRenderer bipedRightArm;
	//左腕
	public ModelRenderer bipedLeftArm;
	//身体
	public ModelRenderer bipedBody;
	//右足
	public ModelRenderer bipedRightLeg;
	//左足
	public ModelRenderer bipedLeftLeg;
	//スカート
	public ModelRenderer Skirt;
	
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidTest()
	{
		//補正値
		float psize = 0.0F;
		float pyoffset = 0.0F;
		
		//モデルグループの初期化
		mainFrame = new ModelRenderer(this);
		//全体の位置を下に調整
		mainFrame.setRotationPoint(0F, 0F + pyoffset + 8F, 0F);
		
		bipedTorso = new ModelRenderer(this);
		bipedNeck = new ModelRenderer(this);
		bipedPelvic = new ModelRenderer(this);
		//下半身の位置を下に調整
		bipedPelvic.setRotationPoint(0F, 7F, 0F);
		
		//頭の初期化
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.setTextureOffset( 0,  0).addBox(-4F, -8F, -4F, 8, 8, 8, psize);		// Head
		bipedHead.setTextureOffset(24,  0).addBox(-4F, 0F, 1F, 8, 4, 3, psize);			// Hire
		bipedHead.setTextureOffset(24, 18).addBox(-4.995F, -7F, 0.2F, 1, 3, 3, psize);		// ChignonR
		bipedHead.setTextureOffset(24, 18).addBox(3.995F, -7F, 0.2F, 1, 3, 3, psize);		// ChignonL
		bipedHead.setTextureOffset(52, 10).addBox(-2F, -7.2F, 4F, 4, 4, 2, psize);		// ChignonB
		bipedHead.setTextureOffset(46, 20).addBox(-1.5F, -6.8F, 4F, 3, 9, 3, psize);	// Tail
		bipedHead.setTextureOffset(58, 21).addBox(-5.5F, -6.8F, 0.9F, 1, 8, 2, psize);	// SideTailR
		bipedHead.setTextureOffset(58, 21).addBox(4.5F, -6.8F, 0.9F, 1, 8, 2, psize);	// SideTailL
		bipedHead.setRotationPoint(0F, 0F, 0F);
		
		//右腕初期化
		bipedRightArm = new ModelRenderer(this, 48, 0);
		bipedRightArm.addBox(-2.0F, -1F, -1F, 2, 8, 2, psize);
		bipedRightArm.setRotationPoint(-3.0F, 1.5F, 0F);
		
		//左腕初期化
		bipedLeftArm = new ModelRenderer(this, 56, 0);
		bipedLeftArm.addBox(0.0F, -1F, -1F, 2, 8, 2, psize);
		bipedLeftArm.setRotationPoint(3.0F, 1.5F, 0F);

		//右足初期化
		bipedRightLeg = new ModelRenderer(this, 32, 19);
		bipedRightLeg.addBox(-2F, 0F, -2F, 3, 9, 4, psize);
		bipedRightLeg.setRotationPoint(-1F, 0F, 0F);

		//左足初期化
		bipedLeftLeg = new ModelRenderer(this, 32, 19);
		bipedLeftLeg.addBox(-1F, 0F, -2F, 3, 9, 4, psize);
		bipedLeftLeg.setRotationPoint(1F, 0F, 0F);
		
		//身体初期化
		bipedBody = new ModelRenderer(this, 32, 8);
		bipedBody.addBox(-3F, 0F, -2F, 6, 7, 4, psize);
		bipedBody.setRotationPoint(0F, 0F, 0F);
		
		//スカートパーツ初期化
		Skirt = new ModelRenderer(this, 0, 16);
		Skirt.addBox(-4F, -2F, -4F, 8, 8, 8, psize);
		Skirt.setRotationPoint(0F, 0F, 0F);

		//モデル親子関係設定
		mainFrame.addChild(bipedTorso);
		
		//メイドさん本体
		bipedTorso.addChild(bipedNeck);
		bipedTorso.addChild(bipedBody);
		bipedTorso.addChild(bipedPelvic);
		
		//首グループ
		bipedNeck.addChild(bipedHead);
		bipedNeck.addChild(bipedRightArm);
		bipedNeck.addChild(bipedLeftArm);

		//下半身グループ
		bipedPelvic.addChild(bipedRightLeg);
		bipedPelvic.addChild(bipedLeftLeg);
		bipedPelvic.addChild(Skirt);
	}
	
	/**
	 * メイドの描画を行う
	 */
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		//デフォルトモーション設定
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		//腕振りモーション設定
		this.setAnimationSwingArms(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		//スニークモーション設定
		this.setAnimationSneak(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		
		//全体をまとめて描画する
		this.mainFrame.render(scale);
		
	}
	
	/**
	 * 各パーツの位置調整を行う
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.setAnimationDefault(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
				
		//待機モーションテスト
		/*
		if (false) {
			float lx = MathHelper.sin(ageInTicks * 0.067F) * 0.05F -0.7F;
			ModelHelper.setRotateAngle(bipedRightArm, lx, 0.0F, -0.4F);
			ModelHelper.setRotateAngle(bipedLeftArm, lx, 0.0F, 0.4F);			
		}
		*/
		
	}
	
	public void setDefaultPause() {
		
	}
	
	/**
	 * 初期モーションの設定
	 * 腕振り足振りの制御とかもここに含まれている
	 * 
	 */
	public void setAnimationDefault(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		mainFrame.setRotationPoint(0F, 8F, 0F);
		
		bipedTorso.setRotationPoint(0F, 0F, 0F);
		ModelHelper.setRotateAngle(bipedTorso, 0F, 0F, 0F);
		
		bipedNeck.setRotationPoint(0F, 0F, 0F);
		ModelHelper.setRotateAngle(bipedNeck, 0F, 0F, 0F);
		
		bipedPelvic.setRotationPoint(0F, 7F, 0F);
		ModelHelper.setRotateAngle(bipedPelvic, 0F, 0F, 0F);

		bipedHead.setRotationPoint(0F, 0F, 0F);
		//bipedHead.setRotateAngleDegY(netHeadYaw);
		//bipedHead.setRotateAngleDegX(headPitch);
		bipedHead.rotateAngleY = netHeadYaw * ModelHelper.degFactor;
		bipedHead.rotateAngleX = headPitch * ModelHelper.degFactor;
		
		bipedBody.setRotationPoint(0F, 0F, 0F);
		ModelHelper.setRotateAngle(bipedBody, 0F, 0F, 0F);

		bipedRightArm.setRotationPoint(-3.0F, 1.6F, 0F);
		ModelHelper.setRotateAngle(bipedRightArm, MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 2.0F * limbSwingAmount * 0.5F, 0F, 0F);
		bipedLeftArm.setRotationPoint(3.0F, 1.6F, 0F);
		ModelHelper.setRotateAngle(bipedLeftArm, MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F, 0F, 0F);
		bipedRightLeg.setRotationPoint(-1F, 0F, 0F);
		ModelHelper.setRotateAngle(bipedRightLeg, MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, 0F, 0F);
		bipedLeftLeg.setRotationPoint(1F, 0F, 0F);
		ModelHelper.setRotateAngle(bipedLeftLeg, MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount, 0F, 0F);

		Skirt.setRotationPoint(0F, 0F, 0F);
		ModelHelper.setRotateAngle(Skirt, 0F, 0F, 0F);
		
		
		
		//腕振りとかの制御？
		
		/*
		// 腕ふり、腿上げ
		float lf1 = MathHelper.cos(limbSwing * 0.6662F);
		float lf2 = MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI));
		this.bipedRightArm.rotateAngleX = lf2 * 2.0F * limbSwingAmount * 0.5F;
		this.bipedLeftArm.rotateAngleX = lf1 * 2.0F * limbSwingAmount * 0.5F;
		this.bipedRightLeg.rotateAngleX = lf1 * 1.4F * limbSwingAmount;
		this.bipedLeftLeg.rotateAngleX = lf2 * 1.4F * limbSwingAmount;
		*/
		
		//腕を横方向にする制御
		// 通常
		float la, lc;
		la = MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		lc = 0.5F + MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += la;
		bipedLeftArm.rotateAngleX += -la;
		bipedRightArm.rotateAngleZ += lc;
		bipedLeftArm.rotateAngleZ += -lc;
	}
	
	
	/**
	 * 腕振り制御
	 * onGroundsの数値をもとに腕振りを制御する
	 */
	public void setAnimationSwingArms(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		float onGrounds[] = new float[] {0.0F, 0.0F};
		
		EntityPlayer player = (EntityPlayer) entityIn;
		
		//利き腕取得
		EnumHandSide dominantHand = player.getPrimaryHand();
		boolean isDominantRight = dominantHand == EnumHandSide.RIGHT;
		
		//右か左かの判断
		boolean isMainHand = EnumHand.MAIN_HAND == player.swingingHand;
		
		//左利きの場合は左右逆転する
		if (!isDominantRight) {
			isMainHand = !isMainHand;
		}
		
		//腕振り
		/*　tick単位での腕振り制御位置
		腕を振った時にplayer.swingProgressにこの値が設定される
		0.16666667
		0.16666667
		0.16666667
		0.33333334
		0.33333334
		0.33333334
		0.5
		0.5
		0.5
		0.6666667
		0.6666667
		0.6666667
		0.8333333
		0.8333333
		0.8333333
		*/
		if (isMainHand) {
			//右振り
			onGrounds[0] = player.swingProgress;
			onGrounds[1] = 0.0F;
		} else {
			//左振り
			onGrounds[0] = 0.0F;
			onGrounds[1] = player.swingProgress;
		}
		
		
		if ((onGrounds[0] > -9990F || onGrounds[1] > -9990F)) {
			
			// 腕振り
			float f6, f7, f8;
			f6 = MathHelper.sin(MathHelper.sqrt(onGrounds[0]) * (float)Math.PI * 2.0F);
			f7 = MathHelper.sin(MathHelper.sqrt(onGrounds[1]) * (float)Math.PI * 2.0F);
			
			bipedTorso.rotateAngleY = (f6 - f7) * 0.2F;
			Skirt.rotateAngleY += bipedTorso.rotateAngleY;
			bipedRightArm.rotateAngleY += bipedTorso.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedTorso.rotateAngleY;
			bipedPelvic.rotateAngleY += bipedTorso.rotateAngleY;
			bipedHead.rotateAngleY += -bipedTorso.rotateAngleY;
			Skirt.rotateAngleY += -bipedTorso.rotateAngleY;
			
			// R
			if (onGrounds[0] > 0F) {
				f6 = 1.0F - onGrounds[0];
				f6 *= f6;
				f6 *= f6;
				f6 = 1.0F - f6;
				f7 = MathHelper.sin(f6 * (float)Math.PI);
				f8 = MathHelper.sin(onGrounds[0] * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				
				bipedRightArm.rotateAngleX += -f7 * 1.2F - f8;
				bipedRightArm.rotateAngleY += bipedTorso.rotateAngleY * 2.0F;
				bipedRightArm.rotateAngleZ += MathHelper.sin(onGrounds[0] * 3.141593F) * -0.4F;
			} else {
				bipedRightArm.rotateAngleX += bipedTorso.rotateAngleY;
			}
			// L
			if (onGrounds[1] > 0F) {
				f6 = 1.0F - onGrounds[1];
				f6 *= f6;
				f6 *= f6;
				f6 = 1.0F - f6;
				f7 = MathHelper.sin(f6 * (float)Math.PI);
				f8 = MathHelper.sin(onGrounds[1] * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				
				bipedLeftArm.rotateAngleX -= f7 * 1.2F - f8;
				bipedLeftArm.rotateAngleY += bipedTorso.rotateAngleY * 2.0F;
				bipedLeftArm.rotateAngleZ += MathHelper.sin(onGrounds[1] * 3.141593F) * 0.4F;
				
			} else {
				bipedLeftArm.rotateAngleX += bipedTorso.rotateAngleY;
			}
		}
	}
	
	/**
	 * スニークモーションを設定する
	 */
	public void setAnimationSneak(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		EntityPlayer player = (EntityPlayer) entityIn;
		
		if (!player.isSneaking()) return;
		
		// しゃがみ
		bipedTorso.rotateAngleX += 0.5F;
		bipedNeck.rotateAngleX -= 0.5F;
		bipedRightArm.rotateAngleX += 0.2F;
		bipedLeftArm.rotateAngleX += 0.2F;

		bipedPelvic.rotationPointY += -0.5F;
		bipedPelvic.rotationPointZ += -0.6F;
		bipedPelvic.rotateAngleX += -0.5F;
		
		bipedHead.rotationPointY = 1.0F;

		Skirt.rotationPointY -= 0.25F;
		Skirt.rotationPointZ += 0.00F;
		Skirt.rotateAngleX += 0.2F;
		
		bipedTorso.rotationPointY += 1.00F;
					
	}
}
