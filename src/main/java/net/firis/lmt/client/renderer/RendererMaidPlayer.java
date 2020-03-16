package net.firis.lmt.client.renderer;

import net.firis.lmt.client.model.ModelLittleMaidTest;
import net.firis.lmt.client.renderer.layer.LayerArmorLittleMaidTest;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RendererMaidPlayer extends RenderPlayer {
	
	private static final ResourceLocation MAID_TEXTURES = new ResourceLocation("textures/entity/playermaid/player_littlemaid_00.png");
	
	
	protected static ModelBase dummyMainModel = new ModelPlayer(0.0F, false);
	
	/**
	 * コンストラクタ
	 * @param renderManagerIn
	 * @param modelBaseIn
	 * @param shadowSizeIn
	 */
	public RendererMaidPlayer(RenderPlayer renderPlayer) {
		
		super(renderPlayer.getRenderManager());
		
		//初期化
		this.layerRenderers.clear();
		this.mainModel = new ModelLittleMaidTest();
		this.shadowSize = 0.5F;
		
		//layer追加
		this.addLayer(new LayerArmorLittleMaidTest(this));
		
	}

	/**
	 * バインド用のテクスチャ
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		return MAID_TEXTURES;
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
	public ModelLittleMaidTest getLittleMaidModel() {
		return (ModelLittleMaidTest) this.mainModel;
	}
}
