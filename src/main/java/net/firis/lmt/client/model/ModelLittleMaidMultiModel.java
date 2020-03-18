package net.firis.lmt.client.model;

import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.util.EntityCapsLiving;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * マルチモデルを扱うためのラッパーモデル
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class ModelLittleMaidMultiModel extends ModelBase {

	protected TextureBox textureBox;
	/**
	 * コンストラクタ
	 */
	public ModelLittleMaidMultiModel() {
		
		//テクスチャBox固定で初期化
		this.textureBox = ModelManager.instance.getTextureBox("MMM_Akari");
		
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		//Entity用ModelCapsでテスト
		EntityCapsLiving caps = new EntityCapsLiving((EntityLivingBase) entityIn);
		this.textureBox.models[0].render(caps, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
	}
	
}
