package mmmlibx.lib;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import mmmlibx.lib.multiModel.model.mc162.*;

/**
 * 追加パーツたるスタビライザーを管理する
 */
public class MMM_StabilizerManager extends MMM_ManagerBase {

	public static final String preFix = "ModelStabilizer";
	public static Map<String, ModelStabilizerBase> stabilizerList = new TreeMap<String, ModelStabilizerBase>();
	
	
	public static void init() {
		// 特定名称をプリフィックスに持つmodファイをを獲得
		FileManager.getModFile("Stabilizer", preFix);
	}

	public static void loadStabilizer() {
		(new MMM_StabilizerManager()).load();
	}

	@Override
	protected String getPreFix() {
		return preFix;
	}

	@Override
	protected boolean append(Class pclass) {
		if (!(ModelStabilizerBase.class).isAssignableFrom(pclass)) {
			return false;
		}
		
		try {
			ModelStabilizerBase lms = (ModelStabilizerBase)pclass.newInstance();
			stabilizerList.put(lms.getName(), lms);
			return true;
		} catch (Exception e) {
		}
		
		return false;
	}

	/**
	 * 指定された名称のスタビライザーモデルを返す。
	 */
	public static EquippedStabilizer getStabilizer(String pname, String pequippoint) {
		if (!stabilizerList.containsKey(pname)) {
			return null;
		}
		
		EquippedStabilizer lequip = new EquippedStabilizer();
		lequip.stabilizer = stabilizerList.get(pname);
		lequip.stabilizer.init(lequip);
		lequip.equipPointName = pequippoint;
		
		return lequip;
	}

	/**
	 * 実装場所のアップデート
	 */
	public static void updateEquippedPoint(Map<String, EquippedStabilizer> pMap, ModelBase pModel) {
		for (Entry<String, EquippedStabilizer> le : pMap.entrySet()) {
			le.getValue().updateEquippedPoint(pModel);
		}
	}

}
