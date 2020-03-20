package net.firis.lmt.client.renderer;

import net.firis.lmt.client.model.ModelLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerArmorLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerHeldItemLittleMaidMultiModel;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * マルチモデルでプレイヤーモデル描画
 * @author firis-games
 *
 */
public class RendererMaidPlayerMultiModel extends RenderPlayer {
	
	protected static ModelBase dummyMainModel = new ModelPlayer(0.0F, false);
	
	/**
	 * コンストラクタ
	 * @param renderManagerIn
	 * @param modelBaseIn
	 * @param shadowSizeIn
	 */
	public RendererMaidPlayerMultiModel(RenderPlayer renderPlayer) {
		
		super(renderPlayer.getRenderManager());
		
		//初期化
		this.layerRenderers.clear();
		this.mainModel = new ModelLittleMaidMultiModel();
		this.shadowSize = 0.5F;
		
		//layer追加
		this.addLayer(new LayerHeldItemLittleMaidMultiModel(this));
		this.addLayer(new LayerArmorLittleMaidMultiModel(this));
		
	}

	/**
	 * バインド用のテクスチャ
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		//EntityPlayerからテクスチャを取得する
		//メイドモデルの実装だとこの部分はnullを返却する
		return PlayerModelManager.getPlayerTexture(entity);
	}
	
	/**
	 * 試作段階のため通常のPlayerModelをダミーモデルとして返却する
	 */
	@Override
	public ModelPlayer getMainModel()
    {
        return (ModelPlayer) dummyMainModel;
    }
	
	/**
	 * model情報を取得する
	 * @return
	 */
	public ModelLittleMaidMultiModel getLittleMaidMultiModel() {
		return (ModelLittleMaidMultiModel) this.mainModel;
	}
	
	/**
	 * プレイヤーモデルの初期化をしてから描画処理を行う
	 */
	@Override
	public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
	
		//パラメータを初期化
		((ModelLittleMaidMultiModel) this.mainModel).initPlayerModel(entity, x, y, z, entityYaw, partialTicks);
		
		//描画処理
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
	}
}
