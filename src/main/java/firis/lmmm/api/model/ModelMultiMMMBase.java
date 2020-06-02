package firis.lmmm.api.model;

import firis.lmmm.api.caps.IModelCaps;

/**
 * MMMの実験コードを含む部分。
 * ModelMultiBaseに追加するに足りるかをここで実験。
 * このクラスにある機能は予告なく削除される恐れが有るためご留意下さい。
 */
public abstract class ModelMultiMMMBase extends ModelMultiBase {

//	public Map<String, EquippedStabilizer> stabiliser;

	/**
	 * 削除予定変数使わないで下さい。
	 */
//	@Deprecated
//	public float onGround;
	/**
	 * 削除予定変数使わないで下さい。
	 * Beverly7で使用しているので削除は見送り対応を考える
	 */
	@Deprecated
	protected float heldItemLeft = 0.0F;
	/**
	 * 削除予定変数使わないで下さい。
	 * Beverly7で使用しているので削除は見送り対応を考える
	 */
	@Deprecated
	protected float heldItemRight = 0.0F;


	public ModelMultiMMMBase() {
		super();
	}
	public ModelMultiMMMBase(float sizeAdjust) {
		super(sizeAdjust);
	}
	public ModelMultiMMMBase(float sizeAdjust, float yOffset, int textureWidth, int textureHeight) {
		super(sizeAdjust, yOffset, textureWidth, textureHeight);
	}

	/**
	 * mainFrameに全てぶら下がっているならば標準で描画する。
	 */
	@Override
	public void render(IModelCaps entityCaps, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale, boolean isRender) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityCaps);
		mainFrame.render(scale, isRender);
//		renderStabilizer(pEntityCaps, par2, par3, ticksExisted, pheadYaw, pheadPitch, par7);
	}

	/**
	 * 通常のレンダリング前に呼ばれる。
	 * @return falseを返すと通常のレンダリングをスキップする。
	 */
//	public boolean preRender(float par2, float par3,
//			float par4, float par5, float par6, float par7) {
//		return true;
//	}

	/**
	 * 通常のレンダリング後に呼ぶ。 基本的に装飾品などの自律運動しないパーツの描画用。
	 */
//	public void renderExtention(float par2, float par3,
//			float par4, float par5, float par6, float par7) {
//	}

//	/**
//	 * スタビライザーの描画。 自動では呼ばれないのでrender内で呼ぶ必要があります。
//	 */
//	protected void renderStabilizer(IModelCaps pEntityCaps, float par2, float par3,
//			float ticksExisted, float pheadYaw, float pheadPitch, float par7) {
//		// スタビライザーの描画、doRenderの方がいいか？
//		if (stabiliser == null || stabiliser.isEmpty() || render == null)
//			return;
//
//		GL11.glPushMatrix();
//		for (Entry<String, EquippedStabilizer> le : stabiliser.entrySet()) {
//			EquippedStabilizer les = le.getValue();
//			if (les != null && les.equipPoint != null) {
//				ModelStabilizerBase lsb = les.stabilizer;
//				if (lsb.isLoadAnotherTexture()) {
//					Minecraft.getMinecraft().getTextureManager().bindTexture(lsb.getTexture());
//				}
//				les.equipPoint.loadMatrix();
//				lsb.render(this, null, par2, par3, ticksExisted, pheadYaw, pheadPitch, par7);
//			}
//		}
//		GL11.glPopMatrix();
//	}

	/**
	 * モデル切替時に実行されるコード
	 * @param pEntityCaps
	 * Entityの値を操作するためのModelCaps。
	 */
//	public void changeModel(IModelCaps pEntityCaps) {
//		// カウンタ系の加算値、リミット値の設定など行う予定。
//	}

	/**
	 * 初期ロード時に実行
	 */
	public void buildTexture() {
		
	}

	public void setDefaultPause() {
	}

	public void setDefaultPause(float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		setDefaultPause();
	}

//	@Override
//	public boolean setCapsValue(int pIndex, Object... pArg) {
//		switch (pIndex) {
//		case caps_changeModel:
//			changeModel((IModelCaps)pArg[0]);
//			return true;
//		case caps_renderFace:
//			renderFace((IModelCaps)pArg[0], (Float)pArg[1], (Float)pArg[2], (Float)pArg[3],
//				(Float)pArg[4], (Float)pArg[5], (Float)pArg[6], (Boolean)pArg[7]);
//			return true;
//		case caps_renderBody:
//			renderBody((IModelCaps)pArg[0], (Float)pArg[1], (Float)pArg[2], (Float)pArg[3],
//				(Float)pArg[4], (Float)pArg[5], (Float)pArg[6], (Boolean)pArg[7]);
//			return true;
//		}
//		return super.setCapsValue(pIndex, pArg);
//	}

	@Override
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
//		case caps_setFaceTexture:
//			return setFaceTexture((Integer)pArg[0]);
		case caps_textureLightColor:
			return getTextureLightColor((IModelCaps)pArg[0]);
		}
		return super.getCapsValue(pIndex, pArg);
	}

//	// Actors実験区画
//	// このへん未だ未整理
//	public void renderFace(IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
//			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
//	}
//	public void renderBody(IModelCaps pEntityCaps, float par2, float par3, float ticksExisted,
//			float pheadYaw, float pheadPitch, float par7, boolean pIsRender) {
//	}
	/**
	 * 表情をテクスチャのUVマップを変えることで表現
	 * @param pIndex
	 */
//	public int setFaceTexture(int pIndex) {
//		// u = (int)(pIndex % 2) * 32 / 64
//		// v = (int)(pIndex / 2) * 32 / 32
//		GL11.glTranslatef(((pIndex & 0x01) * 32) / textureWidth, (((pIndex >>> 1) & 0x01) * 16) / textureHeight , 0F);
//		return pIndex / 4;
//	}

	public float[] getTextureLightColor(IModelCaps pEntityCaps) {
		return null;
	}

}
