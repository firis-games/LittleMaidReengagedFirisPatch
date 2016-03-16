package mmmlibx.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mmmlibx.lib.multiModel.model.mc162.*;

public abstract class ModelBoxBase {

	public String textureName;
	protected int contractColor;
	public int wildColor;
	protected float modelHeight;
	protected float modelWidth;
	protected float modelYOffset;
	protected float modelMountedYOffset;
	protected boolean isUpdateSize;


	public void setModelSize(float pHeight, float pWidth, float pYOffset, float pMountedYOffset) {
		modelHeight = pHeight;
		modelWidth = pWidth;
		modelYOffset = pYOffset;
		modelMountedYOffset = pMountedYOffset;
	}

	protected int getRandomColor(int pColor, Random pRand) {
		List<Integer> llist = new ArrayList<Integer>();
		for (int li = 0; li < 16; li++) {
			if ((pColor & 0x01) > 0) {
				llist.add(li);
			}
			pColor = pColor >>> 1;
		}
		
		if (llist.size() > 0) {
			return llist.get(pRand.nextInt(llist.size()));
		}
		return -1;
	}

	/**
	 * 契約色の有無をビット配列にして返す
	 */
	public int getContractColorBits() {
		return contractColor;
	}

	/**
	 * 野生色の有無をビット配列にして返す
	 */
	public int getWildColorBits() {
		return wildColor;
	}

//	public boolean hasColor(int pIndex, boolean pContract) {
//		return (((pContract ? contractColor : wildColor) >>> pIndex) & 0x01) != 0;
//	}

	/**
	 * 野生のメイドの色をランダムで返す
	 */
	public int getRandomWildColor(Random pRand) {
		return getRandomColor(getWildColorBits(), pRand);
	}

	/**
	 * 契約のメイドの色をランダムで返す
	 */
	public int getRandomContractColor(Random pRand) {
		return getRandomColor(getContractColorBits(), pRand);
	}

	public float getHeight(IModelCaps pEntityCaps) {
		return modelHeight;
	}
	public float getHeight() {
		return getHeight(null);
	}

	public float getWidth(IModelCaps pEntityCaps) {
		return modelWidth;
	}
	public float getWidth() {
		return getWidth(null);
	}

	public float getYOffset(IModelCaps pEntityCaps) {
		return modelYOffset;
	}
	public float getYOffset() {
		return getYOffset(null);
	}

	public float getMountedYOffset(IModelCaps pEntityCaps) {
		return modelMountedYOffset;
	}
	public float getMountedYOffset() {
		return getMountedYOffset(null);
	}

}
