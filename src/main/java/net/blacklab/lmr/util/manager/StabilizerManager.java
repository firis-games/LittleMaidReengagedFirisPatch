package net.blacklab.lmr.util.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.blacklab.lmr.entity.maidmodel.base.ModelBase;
import net.blacklab.lmr.entity.maidmodel.deprecated.EquippedStabilizer;
import net.blacklab.lmr.entity.maidmodel.deprecated.ModelStabilizerBase;

/**
 * 追加パーツたるスタビライザーを管理する
 */
@Deprecated
public class StabilizerManager extends ManagerBase {

	public static final String preFix = "ModelStabilizer";
	public static Map<String, ModelStabilizerBase> stabilizerList = new TreeMap<String, ModelStabilizerBase>();
	
	
	public static void init() {
		// 特定名称をプリフィックスに持つmodファイをを獲得
	}

	public static void loadStabilizer() {
		(new StabilizerManager()).load();
	}

	@Override
	protected String getPreFix() {
		return preFix;
	}

	@Override
	@SuppressWarnings("rawtypes")
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
