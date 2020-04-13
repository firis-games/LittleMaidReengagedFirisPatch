package net.firis.lmt.client.renderer;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.firis.lmt.client.event.ClientEventLMAvatar;
import net.firis.lmt.client.model.ModelLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerArmorLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerArrowLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerCustomHeadLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerElytraLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerEntityOnShoulderLittleMaid;
import net.firis.lmt.client.renderer.layer.LayerHeldItemLittleMaidMultiModel;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.config.FirisConfig;
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
public class RendererMaidPlayerMultiModel extends RenderPlayer {
	
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
	
	/**
	 * コンストラクタ
	 * @param renderManagerIn
	 * @param modelBaseIn
	 * @param shadowSizeIn
	 */
	public RendererMaidPlayerMultiModel(RenderPlayer renderPlayer) {
		
		super(renderPlayer.getRenderManager());
		
		//既存のものを保存
		this.renderPlayer = renderPlayer;
		
		//初期化
		this.layerRenderers.clear();
		this.mainModel = new ModelLittleMaidMultiModel();
		this.shadowSize = 0.5F;

		//Layerロード開始
		this.isLayerLoading = true;
		
		//layer追加
		this.addLayer(new LayerHeldItemLittleMaidMultiModel(this));
		this.addLayer(new LayerArmorLittleMaidMultiModel(this));
		
		//Player用のlayerを一部改造
        this.addLayer(new LayerElytraLittleMaid(this));
        this.addLayer(new LayerCustomHeadLittleMaid(this));
        this.addLayer(new LayerEntityOnShoulderLittleMaid(renderManager, this));
        this.addLayer(new LayerArrowLittleMaid(this));
        
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
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		
		//通常スキン
		if (!isRenderLMAvatar()) {
			return this.renderPlayer.getEntityTexture(entity);
		}
		
		//EntityPlayerからテクスチャを取得する
		//メイドモデルの実装だとこの部分はnullを返却する
		return PlayerModelManager.getPlayerTexture(entity);
	}
	
	/**
	 * PlayerModelをダミーモデルとして返却する
	 */
	@Override
	public ModelPlayer getMainModel()
    {
		if (!isRenderLMAvatar()) {
			return this.renderPlayer.getMainModel();
		}
        return (ModelPlayer) dummyMainModel;
    }
	
	/**
	 * プレイヤーモデルの初期化をしてから描画処理を行う
	 */
	@Override
	public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
	
		//通常スキン
		if (!isRenderLMAvatar()) {
			this.renderPlayer.doRender(entity, x, y, z, entityYaw, partialTicks);
			return;
		}
		
		//パラメータを初期化
		this.getLittleMaidMultiModel().initPlayerModel(entity, x, y, z, entityYaw, partialTicks);
		
		//法線の再計算
		//GlStateManager.enableRescaleNormal();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		//描画処理
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
	}
	
	@Override
	public void renderRightArm(AbstractClientPlayer clientPlayer) {
		
		//通常スキン
		if (!isRenderLMAvatar()) {
			this.renderPlayer.renderRightArm(clientPlayer);
			return;
		}
		
		this.renderFirstPersonArm(clientPlayer);
	}
	
	@Override
	public void renderLeftArm(AbstractClientPlayer clientPlayer) {
		
		//通常スキン
		if (!isRenderLMAvatar()) {
			this.renderPlayer.renderLeftArm(clientPlayer);
			return;
		}
		
		this.renderFirstPersonArm(clientPlayer);		
	}
	
	/**
	 * model情報を取得する
	 * @return
	 */
	public ModelLittleMaidMultiModel getLittleMaidMultiModel() {
		return (ModelLittleMaidMultiModel) this.mainModel;
	}
	
	/**
	 * 一人称視点の手を描画する
	 */
	protected void renderFirstPersonArm(AbstractClientPlayer clientPlayer) {
		this.getLittleMaidMultiModel().renderFirstPersonArm(clientPlayer);
	}
	
	/**
	 * LMアバターが有効化どうか
	 * @return
	 */
	protected boolean isRenderLMAvatar() {
		//初期化のタイミングはLMアバター扱い
		if (this.renderPlayer == null) return true;
		return FirisConfig.cfg_enable_lmavatar;
	}
	
}
