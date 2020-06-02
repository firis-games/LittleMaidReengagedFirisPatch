package firis.lmlib.api.client.renderer.layer;

import org.lwjgl.opengl.GL11;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.model.LMModelArmor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * リトルメイドの防具用レイヤー
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public abstract class LMLayerArmorBase extends LayerArmorBase<LMModelArmor> {

	private LMModelArmor armorModel;
	
	public LMLayerArmorBase(RenderLivingBase<? extends EntityLivingBase> rendererIn) {
		
		super(rendererIn);
		
		//モデルの初期化
		this.armorModel = new LMModelArmor();
	}

	@Override
	protected void initArmor() {}

	@Override
	protected void setModelSlotVisible(LMModelArmor modelIn, EntityEquipmentSlot slotIn) {}
	
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
		if (!this.isRenderArmorLayer(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, slot)) return;
		
		//テクスチャバインド
		//法線の再計算
		//GlStateManager.enableRescaleNormal();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//透過設定
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//防具モデルの準備
		IModelCompound modelConfigCompound = this.getArmorModelConfigCompound(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, slot);
		this.armorModel.initModelParameter(modelConfigCompound, netHeadYaw, partialTicks, slot);

		//防具モデル描画調整
		//setRotationAnglesはLayerArmorBaseで呼ばれていないようなのでコメントアウト
		//this.armorModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityLivingBaseIn);
		this.armorModel.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);

		//防具モデル描画
		this.armorModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, slot.getIndex());
		
    }
	
	/**
	 * 防具の描画判定
	 * @param entityLivingBaseIn
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param partialTicks
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 * @param scale
	 * @param slot
	 * @return
	 */
	protected boolean isRenderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot) {
		return true;
	}

	/**
	 * 描画用パラメータを取得する
	 * @param entityLivingBaseIn
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param partialTicks
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 * @param scale
	 * @param slot
	 * @return
	 */
	abstract protected IModelCompound getArmorModelConfigCompound(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot);

}
