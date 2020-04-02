package net.firis.lmt.client.model;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * マルチモデルを扱うためのラッパーモデル
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class ModelLittleMaidMultiModelArmor extends ModelBase {

	private PlayerModelCaps playerCaps = null;
	private ModelMultiBase armorModel = null;
	private ModelMultiBase armorOuterModel = null;
	private EntityEquipmentSlot armorSlot = null;
	
	private static int ARMOR_INNER_ID = 0;
	private static int ARMOR_OUTER_ID = 1;
	
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidMultiModelArmor() {
	}
	
	/**
	 * アーマーモデルのセットアップ
	 * 
	 * doRenderのタイミングで必要な情報を内部変数へセットする
	 * ※重複処理を排除するため
	 */
	public void initArmorModel(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot) {
		
		EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		
		//アーマーモデルの準備
		List<ModelMultiBase> modelList = PlayerModelManager.getArmorModels(player, slot);
		armorModel = modelList.get(0);
		armorOuterModel = modelList.get(1);
		
		playerCaps = getModelCaps(armorModel, armorOuterModel, player);
		
		//モデルの表示設定
		armorModel.showArmorParts(slot.getIndex(), ARMOR_INNER_ID);
		armorOuterModel.showArmorParts(slot.getIndex(), ARMOR_OUTER_ID);
		
		//スロットの位置
		armorSlot = slot;
		
	}
	
	/**
	 * メイドさんの向きなどを設定
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		
		armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, playerCaps);
		armorOuterModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, playerCaps);
		
	}
	
	/**
	 * メイドさんのアニメーション設定
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

		armorModel.setLivingAnimations(playerCaps, limbSwing, limbSwingAmount, partialTickTime);
		armorOuterModel.setLivingAnimations(playerCaps, limbSwing, limbSwingAmount, partialTickTime);
	}
	
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		EntityPlayer player = (EntityPlayer) entityIn;

		//法線再計算
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		List<ResourceLocation> armorModelTextureList = PlayerModelManager.getArmorTexture(player, armorSlot);
		List<ResourceLocation> armorLightModelTextureList = PlayerModelManager.getArmorLightTexture(player, armorSlot);

		//innerモデル
		if (armorModelTextureList.get(0) != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(armorModelTextureList.get(0));
			GL11.glEnable(GL11.GL_NORMALIZE);
			armorModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
		}
		
		//outerモデル
		if (armorModelTextureList.get(1) != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(armorModelTextureList.get(1));
			armorOuterModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
		}
		
		//発光innerモデル
		if (armorLightModelTextureList.get(0) != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(armorLightModelTextureList.get(0));
			
			int lighting = player.getBrightnessForRender();
			
			float var4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			
			RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			
			armorModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
			
			RendererHelper.setLightmapTextureCoords(lighting);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(true);
		}
		
		//発光outerモデル
		if (armorLightModelTextureList.get(1) != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(armorLightModelTextureList.get(1));
			
			int lighting = player.getBrightnessForRender();
			
			float var4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			
			RendererHelper.setLightmapTextureCoords(0x00f000f0);//61680
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			
			armorOuterModel.render(playerCaps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
			
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
	protected PlayerModelCaps getModelCaps(ModelMultiBase modelArmor, ModelMultiBase modelOuterArmor, EntityPlayer player) {
		
		PlayerModelCaps caps = new PlayerModelCaps(player);
		
		//モデルにCaps情報を設定する
		caps.setModelMultiBaseCapsFromModelCaps(modelArmor);
		caps.setModelMultiBaseCapsFromModelCaps(modelOuterArmor);
		
		return caps;
	}
}
