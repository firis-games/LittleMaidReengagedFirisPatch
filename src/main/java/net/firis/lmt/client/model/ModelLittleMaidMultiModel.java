package net.firis.lmt.client.model;

import net.blacklab.lmr.entity.maidmodel.IModelCaps;
import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.util.EntityCapsLiving;
import net.blacklab.lmr.util.manager.ModelManager;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * マルチモデルを扱うためのラッパーモデル
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class ModelLittleMaidMultiModel extends ModelBase {

	protected TextureBox textureBox;
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidMultiModel() {
		
		//テクスチャBox固定で初期化
		this.textureBox = ModelManager.instance.getTextureBox(RendererMaidPlayerMultiModel.testTexure);
		
	}
	
	/**
	 * メイドさんの向きなどを設定
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		EntityCapsLiving caps = getModelCaps((EntityPlayer) entityIn);

		ModelMultiBase modelMain = this.textureBox.models[0];
		
		modelMain.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, caps);
		
	}
	
	/**
	 * メイドさんのアニメーション設定
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

		EntityCapsLiving caps = getModelCaps((EntityPlayer) entitylivingbaseIn);

		ModelMultiBase modelMain = this.textureBox.models[0];
		
		modelMain.setLivingAnimations(caps, limbSwing, limbSwingAmount, partialTickTime);
	}
	
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		//Entity用ModelCapsでテスト
		EntityCapsLiving caps = getModelCaps((EntityPlayer) entityIn);
		
		ModelMultiBase modelMain = this.textureBox.models[0];
		modelMain.render(caps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
	}
	
	/**
	 * マルチモデル描画用ModelCapsをセットアップ
	 * 
	 * マルチモデル側のsetCapsValueを呼び出して最低限必要な情報を設定する
	 * 内部変数にセットする
	 * @param player
	 * @return
	 */
	protected EntityCapsLiving getModelCaps(EntityPlayer player) {
		
		EntityCapsLiving caps = new EntityCapsLiving(player);
		
		//腕振り情報
		float onGrounds[] = getOnGrounds(player);
		
		//パラメータを渡す
		ModelMultiBase modelMain = this.textureBox.models[0];
		
		modelMain.setCapsValue(IModelCaps.caps_heldItemLeft, (Integer)0);
		modelMain.setCapsValue(IModelCaps.caps_heldItemRight, (Integer)0);
		modelMain.setCapsValue(IModelCaps.caps_onGround,
				onGrounds[0],
				onGrounds[1]);
		modelMain.setCapsValue(IModelCaps.caps_isRiding, caps.getCapsValue(IModelCaps.caps_isRiding));
		modelMain.setCapsValue(IModelCaps.caps_isSneak, caps.getCapsValue(IModelCaps.caps_isSneak));
		modelMain.setCapsValue(IModelCaps.caps_aimedBow, false);
		modelMain.setCapsValue(IModelCaps.caps_isWait, false);
		modelMain.setCapsValue(IModelCaps.caps_isChild, false);
		modelMain.setCapsValue(IModelCaps.caps_entityIdFactor, 0.0F);
		modelMain.setCapsValue(IModelCaps.caps_ticksExisted, player.ticksExisted);
		modelMain.setCapsValue(IModelCaps.caps_dominantArm, 0);
		
		return caps;
	}
	
	/**
	 * メイドさんのmstatSwingStatusを仮想で再現
	 * @param player
	 * @return
	 */
	protected float[] getOnGrounds(EntityPlayer player) {
		
		float onGrounds[] = new float[] {0.0F, 0.0F};
		
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
		
		return onGrounds;
	}
	
}
