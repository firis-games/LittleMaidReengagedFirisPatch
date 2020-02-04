package net.firis.lmt.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
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
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		//全体をまとめて描画する
		this.mainFrame.render(scale);
		
	}
	
	/**
	 * 各パーツの位置調整を行う
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		
	}
}
