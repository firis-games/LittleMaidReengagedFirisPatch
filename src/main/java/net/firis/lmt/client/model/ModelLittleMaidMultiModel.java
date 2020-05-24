package net.firis.lmt.client.model;

import org.lwjgl.opengl.GL11;

import firis.lmlibrary.api.model.ModelLittleMaidBase;
import firis.lmlibrary.api.model.ModelMultiBase;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.firis.lmt.common.manager.OldPlayerModelManager;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * マルチモデルを扱うためのラッパーモデル
 * @author firis-games
 *
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class ModelLittleMaidMultiModel extends ModelBase {

	private PlayerModelCaps playerCaps = null;
	private ModelMultiBase playerModel = null;
	
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidMultiModel() {
	}
	
	/**
	 * アーマーモデルのセットアップ
	 * 
	 * doRenderのタイミングで必要な情報を内部変数へセットする
	 * ※重複処理を排除するため
	 */
	public void initPlayerModel(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		this.initPlayerModel((EntityPlayer) entity, false);
		
	}
	
	/**
	 * モデルを利用するための設定を行う
	 * @param player
	 */
	public void initPlayerModel(EntityPlayer player, boolean isFirstPerson) {
		
		//プレイヤーモデルの準備
		playerModel = OldPlayerModelManager.getPlayerModel(player);
		playerCaps = getModelCaps(playerModel, player, isFirstPerson);
		
		playerModel.showAllParts();
		
	}
	
	/**
	 * メイドさんの向きなどを設定
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		//setRotationAngles		
		playerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, playerCaps);
		
	}
	
	/**
	 * メイドさんのアニメーション設定
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		
		//setLivingAnimations
		playerModel.setLivingAnimations(playerCaps, limbSwing, limbSwingAmount, partialTickTime);
	}
	
	/**
	 * メイドさん本体の描画
	 */
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		EntityPlayer player = (EntityPlayer) entityIn;
		
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(OldPlayerModelManager.getPlayerTexture(player));
		
		//描画する
		playerModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
		
		
		//発光テクスチャ
		ResourceLocation lightTexture = OldPlayerModelManager.getPlayerTextureLight(player);
		if (lightTexture != null) {
			
			
			int lighting = player.getBrightnessForRender();
			
			//発光テクスチャの描画
			Minecraft.getMinecraft().getTextureManager().bindTexture(lightTexture);
			//Minecraft.getMinecraft().getTextureManager().bindTexture(PlayerModelManager.getPlayerTexture(player));
			float var4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			
			RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			
			playerModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
			
			RendererHelper.setLightmapTextureCoords(lighting);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(true);
			
		}
		
		
	}
	
	/**
	 * マルチモデル描画用ModelCapsをセットアップ
	 * 
	 * マルチモデル側のsetCapsValueを呼び出して最低限必要な情報を設定する
	 * 内部変数にセットする
	 * @param player
	 * @return
	 */
	protected PlayerModelCaps getModelCaps(ModelMultiBase modelMain, EntityPlayer player, boolean isFirstPerson) {
		
		PlayerModelCaps caps = new PlayerModelCaps(player);
		
		//caps.setFirstPerson(isFirstPerson);
		
		//モデルにCaps情報を設定する
		caps.setModelMultiBaseCapsFromModelCaps(modelMain);
		
		return caps;
	}
	
	public void armsPostRenderer(EnumHandSide handSide, float scale) {
		
		if (EnumHandSide.RIGHT == handSide) {
    		this.playerModel.Arms[0].postRender(scale);
     	} else if(EnumHandSide.LEFT == handSide) {
    		this.playerModel.Arms[1].postRender(scale);   		
    	}
	}
	
	/**
	 * 一人称視点の手を描画する
	 * @param player
	 * @param handSide
	 */
	public void renderFirstPersonArm(EntityPlayer player) {
		
		//プレイヤーモデルの準備
		this.initPlayerModel(player, true);
		
		//テクスチャバインド
		Minecraft.getMinecraft().getTextureManager().bindTexture(OldPlayerModelManager.getPlayerTexture(player));
		
		//お手ての位置調整
		GlStateManager.translate(0.0F, 0.25F, 0.0F);

		//お手てを描画
		playerModel.renderFirstPersonHand(playerCaps);
	}
	
	/**
	 * マルチモデルのpostRenderを呼び出す
	 * 
	 * 描画位置の調整
	 */
	public void modelPostRender(EnumMultiModelPartsType modelType, EntityPlayer player, float scale) {
		
		//プレイヤーモデルの準備
		this.initPlayerModel(player, false);
		
		
		//メイドベースの場合のみ調整する
		if (!(this.playerModel instanceof ModelLittleMaidBase)) return;
		
		ModelLittleMaidBase maidModel = (ModelLittleMaidBase) this.playerModel;
		
		//位置調整
		switch (modelType) {
		case HEAD:
			if (maidModel.bipedHead == null) return;
			maidModel.bipedHead.postRender(scale);
			break;
		case BODY:
			if (maidModel.bipedBody == null) return;
			maidModel.bipedBody.postRender(scale);
			break;
		}
	}
	
	/**
	 * postRender用のパーツ定義
	 * @author firis-games
	 *
	 */
	public enum EnumMultiModelPartsType {
		HEAD,
		BODY;
	}
	
}
