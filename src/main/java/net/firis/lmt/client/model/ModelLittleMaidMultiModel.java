package net.firis.lmt.client.model;

import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.util.manager.ModelManager;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
		
		//Player用ModelCaps
		PlayerModelCaps caps = getModelCaps((EntityPlayer) entityIn);

		ModelMultiBase modelMain = this.textureBox.models[0];
		
		modelMain.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, caps);
		
	}
	
	/**
	 * メイドさんのアニメーション設定
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

		//Player用ModelCaps
		PlayerModelCaps caps = getModelCaps((EntityPlayer) entitylivingbaseIn);

		ModelMultiBase modelMain = this.textureBox.models[0];
		
		modelMain.setLivingAnimations(caps, limbSwing, limbSwingAmount, partialTickTime);
	}
	
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		//Player用ModelCaps
		PlayerModelCaps caps = getModelCaps((EntityPlayer) entityIn);
		
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
	protected PlayerModelCaps getModelCaps(EntityPlayer player) {
		
		PlayerModelCaps caps = new PlayerModelCaps(player);
		
		//モデルにCaps情報を設定する
		caps.setModelMultiBaseCapsFromModelCaps(this.textureBox.models[0]);
		
		return caps;
	}
	
}
