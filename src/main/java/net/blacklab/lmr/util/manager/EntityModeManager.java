package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.util.FileList;

public class EntityModeManager extends ManagerBase {

	public static final String prefix = "EntityMode";
	public static List<EntityModeBase> maidModeList = new ArrayList<EntityModeBase>();


	public static void init() {
		// 特定名称をプリフィックスに持つmodファイをを獲得
	}
	
	public static void loadEntityMode() {
		(new EntityModeManager()).load();
	}

	@Override
	protected String getPreFix() {
		return prefix;
	}

	@Override
	protected boolean append(Class pclass) {
		// プライオリティー順に追加
		// ソーター使う？
		if (!EntityModeBase.class.isAssignableFrom(pclass)) {
			return false;
		}
		
		try {
			EntityModeBase lemb = null;
			lemb = (EntityModeBase)pclass.getConstructor(EntityLittleMaid.class).newInstance((EntityLittleMaid)null);
			lemb.init();
			
			//既存
			if(maidModeList.contains(lemb)) return false;
			
			if (maidModeList.isEmpty() || lemb.priority() >= maidModeList.get(maidModeList.size() - 1).priority()) {
				maidModeList.add(lemb);
			} else {
				for (int li = 0; li < maidModeList.size(); li++) {
					if (lemb.priority() < maidModeList.get(li).priority()) {
						maidModeList.add(li, lemb);
						break;
					}
				}
			}

			return true;
		} catch (Exception e) {
		} catch (Error e) {
		}

		return false;
	}

	/**
	 * AI追加用のリストを獲得。 
	 */
	public static List<EntityModeBase> getModeList(EntityLittleMaid pentity) {
		List<EntityModeBase> llist = new ArrayList<EntityModeBase>();
		for (EntityModeBase lmode : maidModeList) {
			try {
				llist.add(lmode.getClass().getConstructor(EntityLittleMaid.class).newInstance(pentity));
			} catch (Exception e) {
			} catch (Error e) {
			}
		}
		
		return llist;
	}

	/**
	 * ロードされているモードリストを表示する。
	 */
	public static void showLoadedModes() {
		LittleMaidReengaged.Debug("Loaded Mode lists(%d)", maidModeList.size());
		for (EntityModeBase lem : maidModeList) {
			LittleMaidReengaged.Debug("%04d : %s", lem.priority(), lem.getClass().getSimpleName());
		}
	}

}
