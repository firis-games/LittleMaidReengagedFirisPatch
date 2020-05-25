package net.firis.lmt.client.renderer;

import org.lwjgl.opengl.GL11;

import firis.lmlib.client.model.ModelBaseSolo;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.firis.lmt.client.event.ClientEventLMAvatar;
import net.firis.lmt.client.renderer.layer.LayerArmorLMAvatar;
import net.firis.lmt.client.renderer.layer.LayerArrowLMAvatar;
import net.firis.lmt.client.renderer.layer.LayerCustomHeadLMAvatar;
import net.firis.lmt.client.renderer.layer.LayerElytraLMAvatar;
import net.firis.lmt.client.renderer.layer.LayerEntityOnShoulderLMAvatar;
import net.firis.lmt.client.renderer.layer.LayerHeldItemLMAvatar;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.common.modelcaps.PlayerModelConfigCompound;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

/**
 * マルチモデルでプレイヤーモデル描画
 * @author firis-games
 *
 */
public class RendererLMAvatar extends RenderPlayer {
	
	protected static ModelBase dummyMainModel = new ModelPlayer(0.0F, false);
	
	/**
	 * 既存Render
	 */
	private final RenderPlayer renderPlayer;
	
	/**
	 * addLayer制御用
	 * 初期化中だけロード中に設定しAddLayerを有効化する
	 */
	private boolean isLayerLoading = false;
	
	private boolean isDummyModel = true;
	
	/**
	 * コンストラクタ
	 * @param renderManagerIn
	 * @param modelBaseIn
	 * @param shadowSizeIn
	 */
	public RendererLMAvatar(RenderPlayer renderPlayer) {
		
		super(renderPlayer.getRenderManager());
		
		//既存のものを保存
		this.renderPlayer = renderPlayer;
		
		//初期化
		this.layerRenderers.clear();
		this.mainModel = new ModelBaseSolo();
		this.shadowSize = 0.5F;

		//Layerロード開始
		this.isLayerLoading = true;
		
		//layer追加
		this.addLayer(new LayerHeldItemLMAvatar(this));
		this.addLayer(new LayerArmorLMAvatar(this));
		
		//Player用のlayerを一部改造
        this.addLayer(new LayerElytraLMAvatar(this));
        this.addLayer(new LayerCustomHeadLMAvatar(this));
        this.addLayer(new LayerEntityOnShoulderLMAvatar(renderManager, this));
        this.addLayer(new LayerArrowLMAvatar(this));
        
        //Layer登録用イベント
   		MinecraftForge.EVENT_BUS.post(new ClientEventLMAvatar.RendererAvatarAddLayerEvent(this));
        
		//Layerロード完了
		this.isLayerLoading = false;
	}
	
	/**
	 * レイヤー登録処理
	 * isInitLayerLoaded=trueの場合はロード済みとしてLayerの登録処理を行わない
	 */
	@Override
	public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer)
    {
		//通常Layer登録
		//親クラスの初期化で呼ばれるタイミングはnullのためそこは考慮する
		if (this.renderPlayer != null && !this.isLayerLoading) {
			this.renderPlayer.addLayer(layer);
		}
		
		//LMアバター
		if (!this.isLayerLoading) {
			if (LMRConfig.cfg_lmavatar_include_layer.stream()
				.anyMatch(p -> layer.getClass().toString().indexOf(p) > -1)) {
				LittleMaidReengaged.logger.info("LittleMaidAvatar include layer : " + layer.getClass().toString());
			} else {
				LittleMaidReengaged.logger.info("LittleMaidAvatar exclude layer : " + layer.getClass().toString());
				return true;
			}
		}
		super.addLayer(layer);
		
		return true; 
    }
	
	/**
	 * バインド用のテクスチャ
	 * 
	 * Avatarの場合も通常テクスチャを返却する
	 * 実際の描画時には使用しない
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		
		//通常スキン
		if (!isRenderLMAvatar(entity)) {
			return this.renderPlayer.getEntityTexture(entity);
		}
		
		//EntityPlayerからテクスチャを取得する
		//メイドモデルの実装だとこの部分はnullを返却する
		return this.renderPlayer.getEntityTexture(entity);
	}
	
	/**
	 * PlayerModelをダミーモデルとして返却する
	 */
	@Override
	public ModelPlayer getMainModel()
    {
		if (this.renderPlayer != null && !this.isDummyModel) {
			return this.renderPlayer.getMainModel();
		}
        return (ModelPlayer) dummyMainModel;
    }
	
	/**
	 * メイドさんのサイズ変更を行う
	 * caps_ScaleFactorをもとに描画前に全体サイズを変更する
	 */
	@Override
	protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime) {
		Float lscale = this.getLittleMaidMultiModel().getMultiModelScaleFactor();
		if (lscale != null) {
			GL11.glScalef(lscale, lscale, lscale);
		}
    }
	
	/**
	 * プレイヤーモデルの初期化をしてから描画処理を行う
	 */
	@Override
	public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
	
		//通常スキン
		if (!isRenderLMAvatar(entity)) {
			this.renderPlayer.doRender(entity, x, y, z, entityYaw, partialTicks);
			return;
		}
		
		
		//パラメータの初期化
		this.setModelValues(entity, x, y, z, entityYaw, partialTicks);
		
		//描画処理
		//setModelVisibilitiesを書き換えできないので
		//フラグを使って疑似的にgetMainModelを制御
		this.isDummyModel = true;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		this.isDummyModel = false;
	}
	
	@Override
	public void renderRightArm(AbstractClientPlayer clientPlayer) {
		
		//通常スキン
		if (!isRenderLMAvatar(clientPlayer)) {
			this.renderPlayer.renderRightArm(clientPlayer);
			return;
		}
		
		this.renderFirstPersonArm(clientPlayer);
	}
	
	@Override
	public void renderLeftArm(AbstractClientPlayer clientPlayer) {
		
		//通常スキン
		if (!isRenderLMAvatar(clientPlayer)) {
			this.renderPlayer.renderLeftArm(clientPlayer);
			return;
		}
		
		this.renderFirstPersonArm(clientPlayer);		
	}
	
	
	/**
	 * model情報を取得する
	 * @return
	 */
	public ModelBaseSolo getLittleMaidMultiModel() {
		return (ModelBaseSolo) this.mainModel;
	}
	
	/**
	 * 一人称視点の手を描画する
	 */
	protected void renderFirstPersonArm(AbstractClientPlayer clientPlayer) {	
		this.getLittleMaidMultiModel().renderFirstPersonArm(PlayerModelManager.getModelConfigCompound(clientPlayer));
	}
	
	/**
	 * LMアバターが有効化どうか
	 * @return
	 */
	protected boolean isRenderLMAvatar(AbstractClientPlayer player) {
		//初期化のタイミングはLMアバター扱い
		if (this.renderPlayer == null) return true;
		return PlayerModelManager.getModelConfigCompound(player).getEnableLMAvatar();
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
	public void setModelValues(AbstractClientPlayer entity, 
			double x, double y, double z, float entityYaw, float partialTicks) {
		
		//モデルパラメータ取得
		PlayerModelConfigCompound modelConfigCompound = PlayerModelManager.getModelConfigCompound(entity);
		
		//パラメータの初期化
		this.getLittleMaidMultiModel().initModelParameter(modelConfigCompound, entityYaw, partialTicks);
		
	}
	
	/**
	 * テクスチャのバインドはモデル側で行うため
	 * このタイミングでは何もしない
	 */
	protected boolean bindEntityTexture(AbstractClientPlayer entity) {
		return true;
    }
	
}
