package net.blacklab.lmr.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.client.renderer.entity.RenderModelMulti;
import net.blacklab.lmr.entity.maidmodel.ModelBaseDuo;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerArmorGui extends LayerArmorBase<ModelBaseDuo> {

	private ModelBaseDuo armorModel;
	
	public LayerArmorGui(RenderModelMulti<? extends EntityLiving> rendererIn) {
		
		super(rendererIn);
		
		//モデルの初期化
		this.armorModel = new ModelBaseDuo();
	}

	@Override
	protected void initArmor() {}

	@Override
	protected void setModelSlotVisible(ModelBaseDuo modelIn, EntityEquipmentSlot slotIn) {}
	
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
		//テクスチャバインド
		//法線の再計算
		//GlStateManager.enableRescaleNormal();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//透過設定
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//防具モデルの準備
		this.armorModel.setModelConfigCompound((EntityLiving) entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, slot);

		//防具モデル描画調整
		//setRotationAnglesはLayerArmorBaseで呼ばれていないようなのでコメントアウト
		//this.armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityLivingBaseIn);
		this.armorModel.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);

		//防具モデル描画
		this.armorModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, slot.getIndex());
		
    }
	
}
