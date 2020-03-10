package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;

/**
 * メイドさんのモード管理クラス
 * プラグインのregisterLittleMaidModeよりclassの登録を行う
 * @author computer
 *
 */
public class MaidModeManager {
	
	public static MaidModeManager instance = new MaidModeManager();
	
	private List<Class<? extends EntityModeBase>> maidModeClassList = new ArrayList<>();
	
	/**
	 * メイドモードクラスの登録
	 * @param maidModeClass
	 */
	public void register(Class<? extends EntityModeBase> maidModeClass) {
		
		maidModeClassList.add(maidModeClass);
		
	}
	
	/**
	 * メイドモードの初期化処理を行う
	 */
	public void init() {
		
		for (Class<? extends EntityModeBase> maidModeClass : maidModeClassList) {
			try {
				EntityModeBase maidMode = maidModeClass.getConstructor(EntityLittleMaid.class).newInstance((EntityLittleMaid) null);
				maidMode.init();
			} catch (Exception e) {
				LittleMaidReengaged.logger.error("MaidModeManager.init.error:", maidModeClass);
			}
		}
	}
	
	/**
	 * メイドさん用のメイドモードリストを生成する
	 * @param pentity
	 * @return
	 */
	public List<EntityModeBase> getModeList(EntityLittleMaid pentity) {
		
		List<EntityModeBase> result = new ArrayList<>();
		for (Class<? extends EntityModeBase> maidModeClass : maidModeClassList) {
			try {
				EntityModeBase maidMode = maidModeClass.getConstructor(EntityLittleMaid.class).newInstance(pentity);
				
				//priorityを考慮して設定する
				if (!result.isEmpty()) {
					int startSize = result.size();
					for (int i = 0; i <= startSize; i++) {
						if (i < startSize) {
							EntityModeBase tModePres = result.get(i);
							if (tModePres.priority() >= maidMode.priority()) {
								result.add(i, maidMode);
								break;
							}
						} else {
							result.add(maidMode);
						}
					}
				} else {
					result.add(maidMode);
				}
			} catch (Exception e) {
				LittleMaidReengaged.logger.error("MaidModeManager.getModeList.error:", maidModeClass);
			}
		}
		return result;
	} 
	

}
