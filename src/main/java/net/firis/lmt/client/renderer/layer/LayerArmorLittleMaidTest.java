package net.firis.lmt.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import net.firis.lmt.client.model.ModelLittleMaidTest;
import net.firis.lmt.client.renderer.RendererMaidPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerArmorLittleMaidTest extends LayerArmorBase<ModelLittleMaidTest> {

	private RendererMaidPlayer renderer;
	
	private static final ResourceLocation MAID_ARMOR_TEXTURES = new ResourceLocation("textures/entity/playermaid/player_littlemaid_armor_00.png");
	
	public LayerArmorLittleMaidTest(RenderLivingBase<?> rendererIn) {
		super(rendererIn);
		
		this.renderer = (RendererMaidPlayer) rendererIn;
	}

	@Override
	protected void initArmor() {
		
	}

	@Override
	protected void setModelSlotVisible(ModelLittleMaidTest modelIn, EntityEquipmentSlot slotIn) {
		
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
		Minecraft.getMinecraft().getTextureManager().bindTexture(MAID_ARMOR_TEXTURES);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//アーマー描画
		this.renderer.getLittleMaidModel().renderArmor(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, slotIn);
    }

}
