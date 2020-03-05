package net.blacklab.lmr.client.renderer.entity;

import net.blacklab.lmr.api.client.event.ClientEventLMRE;
import net.blacklab.lmr.client.renderer.layer.MMMLayerArmor;
import net.blacklab.lmr.client.renderer.layer.MMMLayerHeldItem;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.IModelCaps;
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
	public RenderLittleMaid(RenderManager manager, float f) {
		
		super(manager, f);
		
		//描画用Layer登録
		this.addLayer(new MMMLayerHeldItem(this));
		this.addLayer(new MMMLayerArmor(this));
		
		//Layer登録用イベント
		MinecraftForge.EVENT_BUS.post(new ClientEventLMRE.RendererLittleMaidAddLayerEvent(this));
	}
	
	@Override
	public void setModelValues(EntityLittleMaid par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
		EntityLittleMaid lmaid = par1EntityLiving;
		
		super.setModelValues(par1EntityLiving, par2, par4, par6, par8, par9, pEntityCaps);

		//カスタムモーション
		if (this.setCustomMotion(par1EntityLiving, par2, par4, par6, par8, par9, pEntityCaps)) {
			return;
		}
		
//		modelMain.setRender(this);
//		modelMain.setEntityCaps(pEntityCaps);
//		modelMain.showAllParts();
//		modelMain.isAlphablend = true;
//		modelFATT.isAlphablend = true;

		modelMain.setCapsValue(IModelCaps.caps_heldItemLeft, (Integer)0);
		modelMain.setCapsValue(IModelCaps.caps_heldItemRight, (Integer)0);
//		modelMain.setCapsValue(IModelCaps.caps_onGround, renderSwingProgress(lmaid, par9));
		modelMain.setCapsValue(IModelCaps.caps_onGround,
				lmaid.mstatSwingStatus[0].getSwingProgress(par9),
				lmaid.mstatSwingStatus[1].getSwingProgress(par9));
		modelMain.setCapsValue(IModelCaps.caps_isRiding, lmaid.isRidingRender());
		modelMain.setCapsValue(IModelCaps.caps_isSneak, lmaid.isSneaking());
		modelMain.setCapsValue(IModelCaps.caps_aimedBow, lmaid.isAimebow());
		modelMain.setCapsValue(IModelCaps.caps_isWait, lmaid.isMaidWait());
		modelMain.setCapsValue(IModelCaps.caps_isChild, lmaid.isChild());
		modelMain.setCapsValue(IModelCaps.caps_entityIdFactor, lmaid.entityIdFactor);
		modelMain.setCapsValue(IModelCaps.caps_ticksExisted, lmaid.ticksExisted);
		modelMain.setCapsValue(IModelCaps.caps_dominantArm, lmaid.getDominantArm());

		//カスタム設定
		modelMain.setCapsValue(IModelCaps.caps_motionSitting, lmaid.isMotionSitting());

		modelFATT.setModelAttributes(mainModel);
		// だが無意味だ
//		plittleMaid.textureModel0.isChild = plittleMaid.textureModel1.isChild = plittleMaid.textureModel2.isChild = plittleMaid.isChild();
	}

/*
	@SuppressWarnings("unused")
	protected void renderString(LMM_EntityLittleMaid plittleMaid, double px, double py, double pz, float f, float f1) {
		// ひも
		// TODO 傍目にみた表示がおかしい
		if(plittleMaid.mstatgotcha != null && plittleMaid.mstatgotcha instanceof EntityLivingBase) {
			EntityLivingBase lel = (EntityLivingBase)plittleMaid.mstatgotcha;
			py -= 0.5D;
			Tessellator tessellator = Tessellator.getInstance();
			float f9 = ((lel.prevRotationYaw + (lel.rotationYaw - lel.prevRotationYaw) * f1 * 0.5F) * 3.141593F) / 180F;
			float f3 = ((lel.prevRotationPitch + (lel.rotationPitch - lel.prevRotationPitch) * f1 * 0.5F) * 3.141593F) / 180F;
			double d3 = MathHelper.sin(f9);
			double d5 = MathHelper.cos(f9);
			float f11 = lel.getSwingProgress(f1);
			float f12 = MathHelper.sin(MathHelper.sqrt_float(f11) * 3.141593F);
			Vec3 vec3d = new Vec3d(-0.5D, 0.029999999999999999D, 0.55D);

			vec3d.rotatePitch((-(lel.prevRotationPitch + (lel.rotationPitch - lel.prevRotationPitch) * f1) * 3.141593F) / 180F);
			vec3d.rotateYaw((-(lel.prevRotationYaw + (lel.rotationYaw - lel.prevRotationYaw) * f1) * 3.141593F) / 180F);
			//vec3d.rotateAroundY(f12 * 0.5F);
			//vec3d.rotateAroundX(-f12 * 0.7F);

			double d7 = lel.prevPosX + (lel.posX - lel.prevPosX) * f1 + vec3d.xCoord;
			double d8 = lel.prevPosY + (lel.posY - lel.prevPosY) * f1 + vec3d.yCoord;
			double d9 = lel.prevPosZ + (lel.posZ - lel.prevPosZ) * f1 + vec3d.zCoord;
			if(renderManager.options.thirdPersonView > 0) {
				float f4 = ((lel.prevRenderYawOffset + (lel.renderYawOffset - lel.prevRenderYawOffset) * f1) * 3.141593F) / 180F;
				double d11 = MathHelper.sin(f4);
				double d13 = MathHelper.cos(f4);
				d7 = (lel.prevPosX + (lel.posX - lel.prevPosX) * f1) - d13 * 0.34999999999999998D - d11 * 0.54999999999999998D;
				d8 = (lel.prevPosY + (lel.posY - lel.prevPosY) * f1) - 0.45000000000000001D;
				d9 = ((lel.prevPosZ + (lel.posZ - lel.prevPosZ) * f1) - d11 * 0.34999999999999998D) + d13 * 0.54999999999999998D;
			}
			double d10 = plittleMaid.prevPosX + (plittleMaid.posX - plittleMaid.prevPosX) * f1;
			double d12 = plittleMaid.prevPosY + (plittleMaid.posY - plittleMaid.prevPosY) * f1 + 0.25D - 0.5D;//+ 0.75D;
			double d14 = plittleMaid.prevPosZ + (plittleMaid.posZ - plittleMaid.prevPosZ) * f1;
			double d15 = (float)(d7 - d10);
			double d16 = (float)(d8 - d12);
			double d17 = (float)(d9 - d14);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			//tessellator.startDrawing(3);
			try {
				tessellator.getWorldRenderer().func_181668_a(3, new VertexFormat()
					.func_181721_a(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.NORMAL, 16)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//tessellator.setColorOpaque_I(0);
			GlStateManager.color(1f, 1f, 1f);
			tessellator.getWorldRenderer().putPositionData(new int[]{1, 1, 1, 12, 24});
			int i = 16;
			for(int j = 0; j <= i; j++)
			{
				float f5 = (float)j / (float)i;
				try {
					tessellator.getWorldRenderer().putPosition(px + d15 * f5, py + d16 * (f5 * f5 + f5) * 0.5D + (((float)i - (float)j) / (i * 0.75F) + 0.125F), pz + d17 * f5);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

			tessellator.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
*/
/*
	public void doRenderLitlleMaid(LMM_EntityLittleMaid plittleMaid, double px, double py, double pz, float f, float f1) {
		// いくつか重複してるのであとで確認
		// 姿勢による高さ調整

		// ここは本来的には要らない。
		if (plittleMaid.worldObj instanceof WorldServer) {
			// RSHUD-ACV用
			MMM_TextureBox ltbox0 = ((MMM_TextureBoxServer)plittleMaid.textureData.textureBox[0]).localBox;
			MMM_TextureBox ltbox1 = ((MMM_TextureBoxServer)plittleMaid.textureData.textureBox[1]).localBox;
			modelMain.model = ltbox0.models[0];
			modelFATT.modelInner = ltbox1.models[1];
			modelFATT.modelOuter = ltbox1.models[2];
			plittleMaid.textureData.setTextureNamesServer();
			modelMain.textures = plittleMaid.textureData.getTextures(0);
			modelFATT.textureInner = plittleMaid.textureData.getTextures(1);
			modelFATT.textureOuter = plittleMaid.textureData.getTextures(2);
			modelFATT.textureInnerLight = plittleMaid.textureData.getTextures(3);
			modelFATT.textureOuterLight = plittleMaid.textureData.getTextures(4);
		} else {
			modelMain.model = ((MMM_TextureBox)plittleMaid.textureData.textureBox[0]).models[0];
			modelFATT.modelInner = ((MMM_TextureBox)plittleMaid.textureData.textureBox[1]).models[1];
			modelFATT.modelOuter = ((MMM_TextureBox)plittleMaid.textureData.textureBox[1]).models[2];
			modelMain.textures = plittleMaid.textureData.getTextures(0);
			modelFATT.textureInner = plittleMaid.textureData.getTextures(1);
			modelFATT.textureOuter = plittleMaid.textureData.getTextures(2);
			modelFATT.textureInnerLight = plittleMaid.textureData.getTextures(3);
			modelFATT.textureOuterLight = plittleMaid.textureData.getTextures(4);
		}

//		doRenderLiving(plittleMaid, px, py, pz, f, f1);
		renderModelMulti(plittleMaid, px, py, pz, f, f1, plittleMaid.maidCaps);
		renderString(plittleMaid, px, py, pz, f, f1);
	}
*/
	/**
	パラメータ名を書き直し
	@Override
	public void doRender(EntityLittleMaid par1EntityLiving,
			double par2, double par4, double par6, float par8, float par9) {

		EntityLittleMaid lmm = par1EntityLiving;

		fcaps = lmm.maidCaps;

		renderModelMulti(lmm, par2, par4, par6, par8, par9, fcaps);

	}
	*/
	
	/**
	 * Rendererのメイン処理
	 */
	@Override
	public void doRender(EntityLittleMaid entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		//モデルパラメータセット
		fcaps = entity.maidCaps;
		
		//マルチモデルの描画
		renderModelMulti(entity, x, y, z, entityYaw, partialTicks, fcaps);
		
	}

	@Override
	public void renderLivingAt(EntityLittleMaid par1EntityLiving, double par2, double par4, double par6) {
		super.renderLivingAt(par1EntityLiving, par2, par4, par6);

		EntityLittleMaid llmm = par1EntityLiving;
		// 追加分
		for (int li = 0; li < llmm.maidEntityModeList.size(); li++) {
			llmm.maidEntityModeList.get(li).showSpecial(this, par2, par4, par6);
		}
	}

	@Override
	protected int getColorMultiplier(EntityLittleMaid par1EntityLiving, float par2, float par3) {
		return par1EntityLiving.colorMultiplier(par2, par3);
	}
	
	
	/**
	 * LMRFP独自処理
	 * 
	 * モーション固定Rendererを制御する
	 * 歩行や首の傾きは別部分で制御しているようなのでここでは制御できない
	 * 
	 */
	protected boolean setCustomMotion(EntityLittleMaid par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9, IModelCaps pEntityCaps) {
		
		MaidMotion motion = par1EntityLiving.getMaidMotion();
		
		switch (motion) {
			case NONE:
				return false;
			
			//標準状態
			case DEFAULT:
				break;

			//待機
			case WAIT:
				modelMain.setCapsValue(IModelCaps.caps_isLookSuger, true);
				modelMain.setCapsValue(IModelCaps.caps_isWait, true);
				break;
			
			//砂糖らぶ
			case LOOKSUGAR:
				break;

			//スニーク
			case SNEAK:
				modelMain.setCapsValue(IModelCaps.caps_isSneak, true);
				break;
				
			//おすわり（LMRFPのお座りは待機と複合）
			case SIT:
				modelMain.setCapsValue(IModelCaps.caps_isWait, true);
				modelMain.setCapsValue(IModelCaps.caps_isRiding, true);
				modelMain.setCapsValue(IModelCaps.caps_motionSitting, true);
				break;
			
			//弓構え
			case BOW:
				modelMain.setCapsValue(IModelCaps.caps_aimedBow, true);
				break;
		}
		
		//共通設定
		//腕降り制御に使われている
		modelMain.setCapsValue(IModelCaps.caps_onGround,
				par1EntityLiving.mstatSwingStatus[0].getSwingProgress(par9),
				par1EntityLiving.mstatSwingStatus[1].getSwingProgress(par9));
		
		return true;
	}
	
	
	/**
	 * メイドさんの固定モーション設定
	 */
	public enum MaidMotion {
		NONE(0),
		DEFAULT(1),
		WAIT(2),
		LOOKSUGAR(3),
		SNEAK(4),
		SIT(5),
		BOW(6);
		
		private MaidMotion(int id) {
			this.id = id;
		}
		
		private int id;
		public int getId() {
			return this.id;
		}
		
		//次のモーションを取得する
		public MaidMotion next() {
			MaidMotion rtn = MaidMotion.NONE;
			boolean isNext = false;
			for (MaidMotion value : MaidMotion.values()) {
				if (isNext) {
					rtn = value;
					break;
				}
				if(value.getId() == this.getId()) {
					isNext = true;
					continue;
				}
			}
			return rtn;
		}
		
		//IdからMaidMotion取得
		public static MaidMotion getMaidMotionFromId(int id) {
			MaidMotion ret = MaidMotion.NONE;
			for (MaidMotion value : MaidMotion.values()) {
				if(value.getId() == id) {
					ret = value;
					break;
				}
			}
			return ret;
		}
	}
	
}
