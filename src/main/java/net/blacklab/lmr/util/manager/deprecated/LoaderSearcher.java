package net.blacklab.lmr.util.manager.deprecated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.FileList;

/**
 * Searches for classes or resources.
 * Register the Handler to this, and they can handle classes.
 * @author Verclene
 *
 */
@Deprecated
public class LoaderSearcher {
	
	public static LoaderSearcher INSTANCE = new LoaderSearcher();
	
	private List<LoaderHandler> handlers;

	public LoaderSearcher() {
		handlers = new ArrayList<>();
	}
	
	public void startSearch() {
		// Initialize classpath
		for (File pack :
				FileList.filesMods) {
			searchZip(pack);
		}

		for (File classpathDir :
				FileList.dirClasspath) {
			searchDir(classpathDir, FileClassUtil.getLinuxAntiDotName(classpathDir.getAbsolutePath()));
		}
		
		// 開発専用処理
		if (FileList.developIncludeDirMods != null) {
			for (File pack : FileList.developIncludeDirMods.listFiles()) {
				searchZip(pack);
			}
			
		}
	}
	
	/**
	 * Search in zip.
	 * @param file
	 */
	private void searchZip(File file) {
		try {
			ZipFile zFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> zEntries = zFile.entries();
			while (zEntries.hasMoreElements()) {
				ZipEntry tEntry = zEntries.nextElement();
				String tSearchName = tEntry.getName();

				for (LoaderHandler tHandler : handlers) {
					if (tHandler.isHandled(tSearchName)) {
						tHandler.doHandle(zFile.getInputStream(tEntry), tSearchName);
					}
				}
			}
			zFile.close();
		} catch (IOException e) {
			LittleMaidReengaged.logger.error("Cannot %s as zip package.", file.getName());
			if (DevMode.DEVELOPMENT_DEBUG_MODE) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * Search in directory.
	 * @param file
	 * @param root
	 */
	private void searchDir(File file, String root) {
		for (File tFile : file.listFiles()) {
			if (tFile.isDirectory()) {
				searchDir(tFile, root);
			}
			// The name exclude the root 
			String tFileName = FileClassUtil.getLinuxAntiDotName(tFile.getAbsolutePath());
			String tSearchName = tFileName.substring(root.length());
			if (tSearchName.startsWith("/")) {
				tSearchName = tSearchName.substring(1);
			}
			
			for (LoaderHandler tHandler : handlers) {
				if (tHandler.isHandled(tSearchName)) {
					try {
						tHandler.doHandle(new FileInputStream(tFile), tSearchName);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void register(Class<? extends LoaderHandler> pClass) {
		for (LoaderHandler tHandler : handlers) {
			if (tHandler.getClass().equals(pClass)) {
				throw new RuntimeException("Handler is already registered : " + pClass.getName());
			}
		}

		try {
			Constructor<? extends LoaderHandler> tConstructor = pClass.getConstructor();
			LoaderHandler tHandler = tConstructor.newInstance();
			handlers.add(tHandler);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		
	}
	
	public LoaderHandler getInstanceOfHandler(Class<? extends LoaderHandler> pClass) {
		for (LoaderHandler tHandler : handlers) {
			if (tHandler.getClass().equals(pClass)) {
				return tHandler;
			}
		}
		throw new RuntimeException(new ClassNotFoundException("The handler is not registered : " + pClass.getName()));
	}

}
