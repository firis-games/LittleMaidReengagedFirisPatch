package firis.lmmm.api.model.parts;

import firis.lmmm.api.renderer.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;

public class ModelPlate extends ModelBoxBase {

	// 互換用パラメーター
	@Deprecated
	public static final int planeXY		= 0;
	@Deprecated
	public static final int planeZY		= 1;
	@Deprecated
	public static final int planeXZ		= 2;
	@Deprecated
	public static final int planeXYInv	= 4;
	@Deprecated
	public static final int planeZYInv	= 5;
	@Deprecated
	public static final int planeXZInv	= 6;

	/*
	 * こちらを使用して下さい。
	 * ボックスを作った時と同じマッピングになるように調整してます。
	 * マッピング的に左右の面は自力で移動しなければ反転されません、前後が逆になります。
	 */
	public static final int planeXYFront	= 0x10;
	public static final int planeXYBack		= 0x14;
	public static final int planeZYRight	= 0x11;
	public static final int planeZYLeft		= 0x15;
	public static final int planeXZTop		= 0x12;
	public static final int planeXZBottom	= 0x16;



	/**
	 * @param pMRenderer
	 * @param pArg
	 * textureX, textureY, posX, posY, posZ, width, height, facePlane, sizeAdjust
	 */
	public ModelPlate(ModelRenderer pMRenderer, Object... pArg) {
		super(pMRenderer, pArg);
		init(pMRenderer, (Integer)pArg[0], (Integer)pArg[1],
				(Float)pArg[2], (Float)pArg[3], (Float)pArg[4],
				(Integer)pArg[5], (Integer)pArg[6], (Integer)pArg[7],
				pArg.length < 9 ? 0.0F : (Float)pArg[8]);
	}

	private void init(ModelRenderer modelrenderer, int pTextureX, int pTextureY,
			float pX, float pY, float pZ, int pWidth, int pHeight, int pPlane, float pZoom) {
		float lx;
		float ly;
		float lz;
		boolean lotherplane = (pPlane & 0x04) > 0;
		int lPlane = pPlane & 0x03;
		
		// i1 は平面の生成位置
		switch (lPlane) {
		case planeXY:
			// xy
			posX1 = pX;
			posY1 = pY;
			posZ1 = pZ;
			posX2 = lx = pX + pWidth;
			posY2 = ly = pY + pHeight;
			posZ2 = lz = pZ;
			pX -= pZoom;
			pY -= pZoom;
			lx += pZoom;
			ly += pZoom;
			if (lotherplane) {
				pZ += pZoom;
				lz += pZoom;
			} else {
				pZ -= pZoom;
				lz -= pZoom;
			}
			break;
		case planeZY:
			// zy
			posX1 = pX;
			posY1 = pY;
			posZ1 = pZ;
			posX2 = lx = pX;
			posY2 = ly = pY + pHeight;
			posZ2 = lz = pZ + pWidth;
			pY -= pZoom;
			pZ -= pZoom;
			ly += pZoom;
			lz += pZoom;
			if (lotherplane) {
				pX += pZoom;
				lx += pZoom;
			} else {
				pX -= pZoom;
				lx -= pZoom;
			}
			break;
		case planeXZ:
		default:
			// xz
			posX1 = pX;
			posY1 = pY;
			posZ1 = pZ;
			posX2 = lx = pX + pWidth;
			posY2 = ly = pY;
			posZ2 = lz = pZ + pHeight;
			pX -= pZoom;
			pZ -= pZoom;
			lx += pZoom;
			lz += pZoom;
			if (lotherplane) {
				pY += pZoom;
				ly += pZoom;
			} else {
				pY -= pZoom;
				ly -= pZoom;
			}
			break;
		}
		
		quadList = new TexturedQuad[1];
		// 面の法面を反転する
		if (modelrenderer.mirror) {
			if (lPlane == 0) {
				// xy
				float f7 = lx;
				lx = pX;
				pX = f7;
			} else if (lPlane == 1) {
				// zy
				float f7 = lz;
				lz = pZ;
				pZ = f7;
			} else {
				// xz
				float f7 = lx;
				lx = pX;
				pX = f7;
			}
		}
		
		switch (pPlane) {
		case planeXYFront:
		case planeZYRight:
			vertexPositions = new PositionTextureVertex[] {
					new PositionTextureVertex(pX, pY, lz, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY)),
					new PositionTextureVertex(pX, ly, lz, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(lx, ly, pZ, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(lx, pY, pZ, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY))
			};
			lotherplane = false;
			break;
		case planeXYBack:
		case planeZYLeft:
			vertexPositions = new PositionTextureVertex[] {
					new PositionTextureVertex(lx, pY, pZ, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY)),
					new PositionTextureVertex(lx, ly, pZ, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(pX, ly, lz, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(pX, pY, lz, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY))
			};
			lotherplane = false;
			break;
		case planeXZTop:
			vertexPositions = new PositionTextureVertex[] {
					new PositionTextureVertex(pX, pY, lz, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY)),
					new PositionTextureVertex(pX, ly, pZ, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(lx, ly, pZ, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(lx, pY, lz, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY))
			};
			lotherplane = false;
			break;
		case planeXZBottom:
			vertexPositions = new PositionTextureVertex[] {
					new PositionTextureVertex(lx, pY, lz, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY)),
					new PositionTextureVertex(lx, ly, pZ, getU(modelrenderer, pTextureX + pWidth), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(pX, ly, pZ, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY + pHeight)),
					new PositionTextureVertex(pX, pY, lz, getU(modelrenderer, pTextureX), getV(modelrenderer, pTextureY))
			};
			lotherplane = false;
			break;
		case planeXY:
		case planeZY:
		case planeXZ:
		case planeXYInv:
		case planeZYInv:
		case planeXZInv:
		default:
			vertexPositions = new PositionTextureVertex[] {
					new PositionTextureVertex(pX, pY, pZ, 0.0F, 0.0F),
					new PositionTextureVertex(lx, pY, lz, 0.0F, 8F),
					new PositionTextureVertex(lx, ly, lz, 8F, 8F),
					new PositionTextureVertex(pX, ly, pZ, 8F, 0.0F)
			};
			break;
		}
		
		if ((pPlane & 0x0010) > 0) {
			quadList[0] = new TexturedQuad(
					new PositionTextureVertex[] {
							vertexPositions[0],
							vertexPositions[1],
							vertexPositions[2],
							vertexPositions[3] });
			if (modelrenderer.mirror) {
				quadList[0].flipFace();
			}
		} else {
			if (lotherplane) {
				// 逆周り
				quadList[0] = new TexturedQuad(
						new PositionTextureVertex[] {
								vertexPositions[0],
								vertexPositions[1],
								vertexPositions[2],
								vertexPositions[3] },
						pTextureX, pTextureY, pTextureX + pWidth, pTextureY + pHeight,
						modelrenderer.textureWidth,
						modelrenderer.textureHeight);
			} else {
				quadList[0] = new TexturedQuad(
						new PositionTextureVertex[] {
								vertexPositions[1],
								vertexPositions[0],
								vertexPositions[3],
								vertexPositions[2] },
						pTextureX, pTextureY, pTextureX + pWidth, pTextureY + pHeight,
						modelrenderer.textureWidth,
						modelrenderer.textureHeight);
			}
		}
	}

	public float getU(ModelRenderer pRender, int pU) {
		float lf = pU / pRender.textureWidth;
		return lf;
	}

	public float getV(ModelRenderer pRender, int pV) {
		float lf = pV / pRender.textureHeight;
		return lf;
	}

}
