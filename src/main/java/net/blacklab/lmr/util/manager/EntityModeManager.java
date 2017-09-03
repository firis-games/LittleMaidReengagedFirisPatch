package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.mode.EntityModeBase;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.coremod.Transformer;

public class EntityModeManager  {

	public static final String prefix = "EntityMode";

	public static void init() {
		// 特定名称をプリフィックスに持つmodファイをを獲得
		FileList.getModFile("EntityMode", prefix);
	}
	
	/**
	 * AI追加用のリストを獲得。 
	 */
	public static List<EntityModeBase> getModeList(EntityLittleMaid pentity) {
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

}
