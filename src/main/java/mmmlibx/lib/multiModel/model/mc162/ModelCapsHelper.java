package mmmlibx.lib.multiModel.model.mc162;

/**
 * ModelCapsの補助関数群
 */
public class ModelCapsHelper {

	public static Object getCapsValue(IModelCaps pOwner, int pCapsIndex, Object ...pArg) {
		return pOwner == null ? null : pOwner.getCapsValue(pCapsIndex, pArg);
	}

	public static Object getCapsValue(IModelCaps pOwner, String pCapsName, Object ...pArg) {
		return pOwner == null ? null : pOwner.getCapsValue(pOwner.getModelCaps().get(pCapsName), pArg);
	}

	public static int getCapsValueInt(IModelCaps pOwner, int pIndex, Object ...pArg) {
		if (pOwner == null) return 0;
		Integer li = (Integer)pOwner.getCapsValue(pIndex, pArg);
		return li == null ? 0 : li;
	}

	public static float getCapsValueFloat(IModelCaps pOwner, int pIndex, Object ...pArg) {
		if (pOwner == null) return 0F;
		Float lf = (Float)pOwner.getCapsValue(pIndex, pArg);
		return lf == null ? 0F : lf;
	}

	public static double getCapsValueDouble(IModelCaps pOwner, int pIndex, Object ...pArg) {
		if (pOwner == null) return 0D;
		Double ld = (Double)pOwner.getCapsValue(pIndex, pArg);
		return ld == null ? 0D : ld;
	}

	public static boolean getCapsValueBoolean(IModelCaps pOwner, int pIndex, Object ...pArg) {
		if (pOwner == null) return false;
		Boolean lb = (Boolean)pOwner.getCapsValue(pIndex, pArg);
		return lb == null ? false : lb;
	}

	public static boolean setCapsValue(IModelCaps pOwner, String pCapsName, Object... pArg) {
		return pOwner == null ? false : pOwner.setCapsValue(pOwner.getModelCaps().get(pCapsName), pArg);
	}

}
