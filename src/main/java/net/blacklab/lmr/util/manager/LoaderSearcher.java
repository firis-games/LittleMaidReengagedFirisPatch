package net.blacklab.lmr.util.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;

/**
 * Searches for classes or resources.
 * Register the Handler to this, and they can handle classes.
 * @author Verclene
 *
 */
public class LoaderSearcher {
	
	public static LoaderSearcher INSTANCE = new LoaderSearcher();
	
	private List<LoaderHandler> handlers;
	private List<File> classPath;
	
	public LoaderSearcher() {
		handlers = new ArrayList<>();
	}
	
	public void startSearch() {
		// Initialize classpath
		classPath = new ArrayList<>();
		for (URL tUrl : ((URLClassLoader)LittleMaidReengaged.class.getClassLoader()).getURLs()) {
			try {
				classPath.add(new File(tUrl.toURI()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (File file : classPath) {
			if (file.isDirectory()) {
				searchDir(file, FileClassUtil.getLinuxAntiDotName(file.getAbsolutePath()));
			} else if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")) {
				searchZip(file);
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
				System.err.println("Z+" + tEntry.getName());
				String tSearchName = tEntry.getName();

				for (LoaderHandler tHandler : handlers) {
					if (tHandler.isHandled(tSearchName)) {
						tHandler.doHandle(zFile.getInputStream(tEntry), tSearchName);
					}
				}
			}
			zFile.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			System.err.println("F+" + tSearchName);
			
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
	
	public List<File> getClassPath() {
		return classPath;
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
