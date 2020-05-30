package firis.lmlib.client.renderer;

import org.lwjgl.opengl.GL11;

import firis.lmlib.client.model.ModelBaseSolo;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderModelMulti<T extends EntityLiving> extends RenderLiving<T> {

	//メイド用モデル
	public ModelBaseSolo modelMain;
	
	/**
	 * コンストラクタ
	 */
	public RenderModelMulti(RenderManager manager, float pShadowSize) {
		
		super(manager, null, pShadowSize);
		
		//メイド本体描画用モデル初期化
		this.modelMain = new ModelBaseSolo();
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
	 * マルチモデルの描画パラメータの初期化処理
	 * @param entity
	 * @param x
	 * @param y
	 * @param z
	 * @param entityYaw
	 * @param partialTicks
	 */
	abstract public void setModelValues(T entity, double x, double y, double z, float entityYaw, float partialTicks);
	
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
	
}
