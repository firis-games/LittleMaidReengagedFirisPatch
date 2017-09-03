package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.mode.EntityModeBase;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.coremod.Transformer;

public class EntityModeManager extends ManagerBase {

	public static final String prefix = "EntityMode";
	private List<EntityModeBase> maidModeList = new ArrayList<EntityModeBase>();

	private static EntityModeManager instance = new EntityModeManager();
	
	public static void init() {
		// 特定名称をプリフィックスに持つmodファイをを獲得
		FileList.getModFile("EntityMode", prefix);
	}
	
	public EntityModeManager() {
		maidModeList = new ArrayList<>();
	}
	
	public static EntityModeManager getInstance() {
		return instance;
	}
	
	public void loadEntityMode() {
		getInstance().load();
	}
	
	@Override
	protected String getPreFix() {
		return prefix;
	}

	@Deprecated
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
	public List<EntityModeBase> getModeList(EntityLittleMaid pentity) {
		List<EntityModeBase> llist = new ArrayList<EntityModeBase>();
		for (String lmode : Transformer.getEntityModeList()) {
			try {
				Class<?> clazz = Class.forName(lmode);
				if (EntityModeBase.class.isAssignableFrom(clazz)) {
					llist.add((EntityModeBase) clazz.getConstructor(EntityLittleMaid.class).newInstance(pentity));
				}
			} catch (Exception e) {
			} catch (Error e) {
			}
		}
		
		return llist;
	}

	/**
	 * ロードされているモードリストを表示する。
	 */
	@Deprecated
	public void showLoadedModes() {
		LittleMaidReengaged.Debug("Loaded Mode lists(%d)", maidModeList.size());
		for (EntityModeBase lem : maidModeList) {
			LittleMaidReengaged.Debug("%04d : %s", lem.priority(), lem.getClass().getSimpleName());
		}
	}

}
