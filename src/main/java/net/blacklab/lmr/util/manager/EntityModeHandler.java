package net.blacklab.lmr.util.manager;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.mode.EntityModeBase;

public class EntityModeHandler extends LoaderHandler {
	
	private List<Class<? extends EntityModeBase>> modeClasses;
	private List<EntityModeBase> entityModes;
	
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
				System.err.println("ADD");
				modeClasses.add((Class<? extends EntityModeBase>) tClass);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<EntityModeBase> getModeList(EntityLittleMaid pMaid) {
		if (entityModes == null) {
			// Generate
			ArrayList<EntityModeBase> result = new ArrayList<>();
			for (Class<? extends EntityModeBase> tClass : modeClasses) {
				try {
					result.add(tClass.getConstructor(EntityLittleMaid.class).newInstance(pMaid));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
			entityModes = result;
		}
		return entityModes;
	}

}
