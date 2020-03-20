package net.firis.lmt.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.util.manager.ModelManager;
import net.firis.lmt.client.model.ModelLittleMaidMultiModelArmor;
import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
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

	private RendererMaidPlayerMultiModel renderer;
	
	public LayerArmorLittleMaidMultiModel(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
		
		this.renderer = (RendererMaidPlayerMultiModel) rendererIn;
	}

	@Override
	protected void initArmor() {
		
	}

	@Override
	protected void setModelSlotVisible(ModelLittleMaidMultiModelArmor modelIn, EntityEquipmentSlot slotIn) {
		//アーマーの表示設定
		modelIn.showArmorParts(slotIn.getIndex());
	}
	
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
	 * @param entityLivingBaseIn
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param partialTicks
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 * @param scale
	 * @param slotIn
	 */
	private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
    {
		EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
		if (player.inventory.armorItemInSlot(slotIn.getIndex()).isEmpty()) return;
		
		//テクスチャバインド
		Minecraft.getMinecraft().getTextureManager().bindTexture(
				this.renderer.textureBox.getArmorTextureName(ModelManager.tx_armor1, "leather", 0));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//アーマー描画
		PlayerModelCaps caps = new PlayerModelCaps(player);
		//モデルにCaps情報を設定する
		caps.setModelMultiBaseCapsFromModelCaps(this.renderer.textureBox.models[2]);
		this.renderer.textureBox.models[2].showArmorParts(slotIn.getIndex(), 0);
		this.renderer.textureBox.models[2].render(caps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
    }

}
