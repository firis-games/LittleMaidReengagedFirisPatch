package mmmlibx.lib.multiModel.model.mc162;

import java.util.Map;

/**
 * 装備品用のクラス。
 *
 */
public class EquippedStabilizer {
	
	public ModelStabilizerBase stabilizer;
	public ModelRenderer equipPoint;
	public String equipPointName;
	public Map<String, Object> localValues;
	
/*	
	public EquippedStabilizer(String pEquipPointName, ModelStabilizerBase pStabilizer) {
		equipPointName = pEquipPointName;
		stabilizer = pStabilizer;
	}
*/	
	public boolean updateEquippedPoint(ModelBase pmodel) {
		// 取り付け位置をアップデート
		for (int li = 0; li < pmodel.boxList.size(); li++) {
			ModelRenderer lmr = pmodel.boxList.get(li);
			if (lmr.boxName != null && lmr.boxName.equalsIgnoreCase(equipPointName)) {
				equipPoint = lmr;
				return true;
			}
		}
		
		equipPoint = null;
		return false;
	}
	
	
}
