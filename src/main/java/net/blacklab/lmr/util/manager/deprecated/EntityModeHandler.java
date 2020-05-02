package net.blacklab.lmr.util.manager.deprecated;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;

@Deprecated
public class EntityModeHandler extends LoaderHandler {
	
	private List<Class<? extends EntityModeBase>> modeClasses;
	
	public EntityModeHandler() {
		modeClasses = new ArrayList<>();
	}

	@Override
	public boolean isHandled(String pName) {
		// The classes starts with "EntityMode"
		if (pName.endsWith(".class") && FileClassUtil.getFileName(pName).startsWith("EntityMode")) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doHandle(InputStream pStream, String pSearchName) {
		String tClassName = FileClassUtil.getClassName(pSearchName, null);
		System.err.println(tClassName);
		try {
			Class<?> tClass = LittleMaidReengaged.class.getClassLoader().loadClass(tClassName);
			if (EntityModeBase.class.isAssignableFrom(tClass)) {
				modeClasses.add((Class<? extends EntityModeBase>) tClass);
				
				// Call init() (semi-static)
				EntityModeBase tBase =
						(EntityModeBase) tClass.getConstructor(EntityLittleMaid.class).newInstance((EntityLittleMaid)null);
				tBase.init();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public List<EntityModeBase> getModeList(EntityLittleMaid pMaid) {
		// Generate
		ArrayList<EntityModeBase> result = new ArrayList<>();
		for (Class<? extends EntityModeBase> tClass : modeClasses) {
			try {
				// TODO Dup loop?
				EntityModeBase tModeAdd = tClass.getConstructor(EntityLittleMaid.class).newInstance(pMaid);
				if (!result.isEmpty()) {
					// Check priority
					int startSize = result.size();
					for (int i = 0; i <= startSize; i++) {
						if (i < startSize) {
							EntityModeBase tModePres = result.get(i);
							if (tModePres.priority() >= tModeAdd.priority()) {
								result.add(i, tModeAdd);
								break;
							}
						} else {
							result.add(tModeAdd);
						}
					}
				} else {
					result.add(tModeAdd);
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
			}
		}
		return result;
	}

}
