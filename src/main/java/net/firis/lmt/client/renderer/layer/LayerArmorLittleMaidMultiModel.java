package net.firis.lmt.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import net.firis.lmt.client.model.ModelLittleMaidMultiModelArmor;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerArmorLittleMaidMultiModel extends LayerArmorBase<ModelLittleMaidMultiModelArmor> {

	private ModelLittleMaidMultiModelArmor armorModel;
	
	public LayerArmorLittleMaidMultiModel(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
		
		this.armorModel = new ModelLittleMaidMultiModelArmor();
	}

	@Override
	protected void initArmor() {}

	@Override
	protected void setModelSlotVisible(ModelLittleMaidMultiModelArmor modelIn, EntityEquipmentSlot slotIn) {}
	
	/**
	 * Layerの描画
	 */
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
    }

	/**
	 * 防具を描画する
	 */
	private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot)
    {
		//対象スロットの描画対象チェック
		EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
		if (player.inventory.armorItemInSlot(slot.getIndex()).isEmpty()) return;
		
		//テクスチャバインド
		Minecraft.getMinecraft().getTextureManager().bindTexture(PlayerModelManager.getArmorTexture(player, slot));
		
		//透過設定
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//防具モデルの準備
		this.armorModel.initArmorModel(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, slot);

		//防具モデル描画調整
		//setRotationAnglesはLayerArmorBaseで呼ばれていないようなのでコメントアウト
		//this.armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityLivingBaseIn);
		this.armorModel.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);

		//防具モデル描画
		this.armorModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
    }

}
