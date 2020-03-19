package net.firis.lmt.client.renderer;

import net.blacklab.lmr.entity.maidmodel.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.util.manager.ModelManager;
import net.firis.lmt.client.model.ModelLittleMaidMultiModel;
import net.firis.lmt.client.renderer.layer.LayerHeldItemLittleMaidMultiModel;
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
	
	//あかりちゃんの設定
	public static String testTexure = "MMM_Akari";
	public static Integer testTexureColorIndex = 8;
	
	//マルチモデルテクスチャBox
	protected TextureBox textureBox;
	
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
		
		//テクスチャBox固定で初期化
		this.textureBox = ModelManager.instance.getTextureBox(testTexure);
		
		//layer追加
		this.addLayer(new LayerHeldItemLittleMaidMultiModel(this));
		
	}

	/**
	 * バインド用のテクスチャ
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		//あかりちゃんのcolorindex
		return textureBox.getTextureName(testTexureColorIndex);
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
	public ModelMultiBase getLittleMaidMultiModel() {
		return (ModelMultiBase) this.textureBox.models[0];
	}
}
