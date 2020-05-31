package net.blacklab.lmr.client.renderer.entity;

import firis.lmlib.client.renderer.RenderModelMulti;
import firis.lmlib.common.data.IMultiModelEntity;
import net.blacklab.lmr.api.client.event.ClientEventLMRE;
import net.blacklab.lmr.client.renderer.layer.LayerArmorLittleMaid;
import net.blacklab.lmr.client.renderer.layer.LayerHeldChestLittleMaid;
import net.blacklab.lmr.client.renderer.layer.LayerHeldItemLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * リトルメイドRenderer
 * 
 * メイドさんを描画するRenderer
 */
@SideOnly(Side.CLIENT)
public class RenderLittleMaid extends RenderModelMulti<EntityLittleMaid> {
	
	/**
	 * コンストラクタ
	 */
	public RenderLittleMaid(RenderManager manager) {
		
		super(manager, 0.3F);
		
		//描画用Layer登録
		this.addLayer(new LayerHeldItemLittleMaid(this));
		this.addLayer(new LayerArmorLittleMaid(this));
		this.addLayer(new LayerHeldChestLittleMaid(this));
		
		//Layer登録用イベント
		MinecraftForge.EVENT_BUS.post(new ClientEventLMRE.RendererLittleMaidAddLayerEvent(this));
	}
	
	/**
	 * メイドさんからModelConfigCompoundを取得する
	 */
	@Override
	protected ModelConfigCompound getModelConfigCompoundFromEntity(EntityLittleMaid entity) {
		IMultiModelEntity modelEntity = (IMultiModelEntity) entity;
		ModelConfigCompound modelConfigCompound = modelEntity.getModelConfigCompound();
		return modelConfigCompound;
	}
	
	/**
	 * メイドモードごとの特殊描画を行う
	 * 
	 * ※現在は使用していないので扱いをどうするかは考慮する
	 */
	@Override
	protected void renderLivingAt(EntityLittleMaid entityLivingBaseIn, double x, double y, double z) {
		
		super.renderLivingAt(entityLivingBaseIn, x, y, z);
		
//		// 追加分
//		for (int li = 0; li < entityLivingBaseIn.maidEntityModeList.size(); li++) {
//			entityLivingBaseIn.maidEntityModeList.get(li).showSpecial(this, x, y, z);
//		}
		//職業アイテム描画
		entityLivingBaseIn.jobController.showSpecial(this, x, y, z);
	}

	/**
	 * カラー設定を行う
	 */
	@Override
	protected int getColorMultiplier(EntityLittleMaid entityLivingBaseIn, float lightBrightness, float partialTickTime) {
		
		return entityLivingBaseIn.colorMultiplier(lightBrightness, partialTickTime);
		
	}
	
}
