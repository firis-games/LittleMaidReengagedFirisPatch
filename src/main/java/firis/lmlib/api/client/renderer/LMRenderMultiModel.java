package firis.lmlib.api.client.renderer;

import org.lwjgl.opengl.GL11;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.model.LMModelLittleMaid;
import firis.lmmm.api.caps.IModelCaps;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class LMRenderMultiModel<T extends EntityLiving> extends RenderLiving<T> {

	//メイド用モデル
	public LMModelLittleMaid modelMain;
	
	/**
	 * コンストラクタ
	 */
	public LMRenderMultiModel(RenderManager manager, float pShadowSize) {
		
		super(manager, null, pShadowSize);
		
		//メイド本体描画用モデル初期化
		this.modelMain = new LMModelLittleMaid();
		this.mainModel = this.modelMain;
		
	}
	
	/**
	 * メイドさんのサイズ変更を行う
	 * caps_ScaleFactorをもとに描画前に全体サイズを変更する
	 */
	@Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		Float lscale = this.modelMain.getMultiModelScaleFactor();
		if (lscale != null) {
			GL11.glScalef(lscale, lscale, lscale);
		}
    }
	
	/**
	 * 描画用マルチモデル情報をEntityから取得する
	 * @param entity
	 * @return
	 */
	abstract protected IModelCompound getModelConfigCompoundFromEntity(T entity);

	/**
	 * マルチモデルの描画パラメータの初期化処理
	 * @param entity
	 * @param x
	 * @param y
	 * @param z
	 * @param entityYaw
	 * @param partialTicks
	 */
	protected void setModelValues(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//マルチモデル関連情報を取得する
		IModelCompound modelConfig = this.getModelConfigCompoundFromEntity(entity);
		
		//パラメータの初期化
		modelMain.initModelParameter(modelConfig, entityYaw, partialTicks);
	}
	
	/**
	 * Rendererのメイン処理
	 */
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//パラメータの初期化
		this.setModelValues(entity, x, y, z, entityYaw, partialTicks);
		
		//描画処理
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
	}
	
	/**
	 * リードの描画処理
	 */
	@Override
	protected void renderLeash(T entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//縄の位置のオフセット
		float offset = this.modelMain.getLeashOffset();
		
		//リードの描画
		super.renderLeash(entityLivingIn, x, y - offset, z, entityYaw, partialTicks);
		
	}
	
	/**
	 * テクスチャのバインドはモデル側で行うため
	 * 使用しない
	 */
	@Override
	protected ResourceLocation getEntityTexture(T var1) {
		return null;
	}
	
	/**
	 * テクスチャのバインドはモデル側で行うため
	 * このタイミングでは何もしない
	 */
	protected boolean bindEntityTexture(T entity) {
		return true;
    }
	
	/**
	 * 向いている方向に合わせてモデルの位置を調整する
	 * 特定の場合のみ無効化する
	 */
	@Override
	protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		
		IModelCompound modelConfig = this.getModelConfigCompoundFromEntity(entityLiving);
		
		Float lookingRotation = (Float) modelConfig.getModelCaps().getCapsValue(IModelCaps.caps_looking_rotation);
		if (lookingRotation == null) {
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
		} else {
			GlStateManager.rotate(lookingRotation, 0.0F, 1.0F, 0.0F);
		}
	}
}
