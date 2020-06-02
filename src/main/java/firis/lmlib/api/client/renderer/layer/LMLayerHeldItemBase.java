package firis.lmlib.api.client.renderer.layer;

import firis.lmlib.api.client.model.LMModelLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * リトルメイドの手持ちアイテム用Layer
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public abstract class LMLayerHeldItemBase extends LayerHeldItem {

	private LMModelLittleMaid rendererModel;
	
	/**
	 * コンストラクタ
	 * @param rendererIn
	 */
	public LMLayerHeldItemBase(RenderLivingBase<?> rendererIn, LMModelLittleMaid rendererModel) {
		super(rendererIn);
		this.rendererModel = rendererModel;
	}
	
	/**
	 * プレイヤーメイドモデルの手に持ったアイテムを描画する
	 */
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		//右手と左手のアイテムを取得
        ItemStack itemstackR = getRightHandItemStack(entitylivingbaseIn);
        ItemStack itemstackL = getLeftHandItemStack(entitylivingbaseIn);

        if (!itemstackR.isEmpty() || !itemstackL.isEmpty()) {
            GlStateManager.pushMatrix();
            
            //右手アイテム描画
            this.renderHeldItem(entitylivingbaseIn, itemstackR, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            
            //左手アイテム描画
            this.renderHeldItem(entitylivingbaseIn, itemstackL, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            
            GlStateManager.popMatrix();
        }
    }

	/**
	 * 手に持ったアイテムを描画する
	 * @param entitylivingbaseIn
	 * @param stackIn
	 * @param transformType
	 * @param handSide
	 */
    private void renderHeldItem(EntityLivingBase entitylivingbaseIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide) {
    	
        if (!stackIn.isEmpty() && isRenderHeldItem(entitylivingbaseIn, stackIn, transformType)) {
            GlStateManager.pushMatrix();
            
            //手の位置へアイテム描画位置を調整する
            this.translateToHand(handSide);
            
            //向き調整
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            
            //手の位置の微調整
            boolean flag = handSide == EnumHandSide.LEFT;
            /* 初期モデル構成で
			 * x: 手の甲に垂直な方向(-で向かって右に移動)
			 * y: 体の面に垂直な方向(-で向かって背面方向に移動)
			 * z: 腕に平行な方向(-で向かって手の先方向に移動)
			 */
            //GlStateManager.translate((flag ? -1.0F : 1.0F) * 0.05F, 0.06f, -0.5f);
            GlStateManager.translate(flag ? -0.0125F : 0.0125F, 0.05f, -0.15f);
            
            //スニーク調整
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate((flag ? -1.0F : 1.0F) * 0F, 0.0f, 0.02f);
            }
            
            //条件に応じて位置を調整する
            this.translateAdjustment(entitylivingbaseIn, stackIn, transformType, handSide);
            
            //アイテム描画
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entitylivingbaseIn, stackIn, transformType, flag);
            
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * 腕の位置へ調整する
     */
    @Override
    protected void translateToHand(EnumHandSide handSide)
    {
    	float scale = 0.0625F;
    	int hand = EnumHandSide.RIGHT == handSide ? 0 : 1;
    	rendererModel.armPostRender(hand, scale);
    }
    
	/**
	 * 右手のアイテムを取得する
	 * @param entitylivingbase
	 * @return
	 */
	abstract protected ItemStack getRightHandItemStack(EntityLivingBase entitylivingbaseIn);
	
	/**
	 * 左手のアイテムを取得する
	 */
	abstract protected ItemStack getLeftHandItemStack(EntityLivingBase entitylivingbaseIn);
	
	/**
     * Layerの描画判定
     * @param entitylivingbaseIn
     * @param stackIn
     * @param transformType
     * @return
     */
    protected boolean isRenderHeldItem(EntityLivingBase entitylivingbaseIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformType) {
    	return true;
    }
    
	/**
	 * アイテムの位置を調整する
	 */
	protected void translateAdjustment(EntityLivingBase entitylivingbaseIn, ItemStack stackIn, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide) {}
	
}
