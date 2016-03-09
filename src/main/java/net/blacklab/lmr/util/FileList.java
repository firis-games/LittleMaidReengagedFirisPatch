package net.blacklab.lmr.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mmmlibx.lib.MMMLib;
import net.blacklab.lib.classutil.FileClassUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class FileList {
	
	public static class CommonClassLoaderWrapper extends URLClassLoader{

		public CommonClassLoaderWrapper(URL[] urls, ClassLoader parent) {
			super(urls, parent);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public void addURL(URL url) {
			// 可視化
			if (new ArrayList(Arrays.asList(getURLs())).contains(url)) return;
			super.addURL(url);
		}
		
	}

	public static File dirMinecraft;
	public static File dirMods;
	public static File dirModsVersion;
	public static File dirDevClasses;
	public static File dirDevClassAssets;
	public static List<File> dirDevIncludeClasses = new ArrayList<File>();
//	public static File[] dirDevIncludeAssets = new File[]{};
	
	public static List<File> files;
	public static String minecraftDir	= "";
//	public static File   minecraftJar	= null;	// minecraft.jarを見に行くのは昔の仕様？
	public static String assetsDir		= "";	// mods/LittleMaidX/assets
	public static boolean isDevdir;
	public static Map<String,List<File>>    fileList = new HashMap<String, List<File>>();

	public static CommonClassLoaderWrapper COMMON_CLASS_LOADER;

	static {
		Object[] injectionData = FMLInjectionData.data();
		dirMinecraft = (File) FMLInjectionData.data()[6];
		minecraftDir = dirMinecraft.getPath();
		dirMods = new File(dirMinecraft, "mods");
		//開発モード
		if(DevMode.DEVMODE != DevMode.NOT_IN_DEV){
			//Linux準拠の形式に変更
			String path = FileClassUtil.getLinuxAntiDotName(dirMods.getAbsolutePath());
			String pathd = path;
			String patha;
			String tail = "/eclipse/mods";
			String tail2 = "/eclipse/server/mods";
			boolean serverFlag = false;
			if(path.endsWith(tail2)){
				path = path.substring(0, path.indexOf(tail2))+tail;
				serverFlag = true;
			}
			if(path.endsWith(tail)){
				if(DevMode.DEVMODE == DevMode.DEVMODE_ECLIPSE){
					pathd = path.substring(0, path.indexOf(tail))+"/bin";
				}else if(DevMode.DEVMODE == DevMode.DEVMODE_NO_IDE){
					pathd = path.substring(0, path.indexOf(tail))+"/build/classes/main";
					patha = path.substring(0, path.indexOf(tail))+"/build/resources/main";
					dirDevClassAssets = new File(patha);
				}
				dirDevClasses = new File(pathd);
				if(!dirDevClasses.exists()||!dirDevClasses.isDirectory())
					throw new IllegalStateException("Could not get dev class path: Maybe your source codes are out of src/main/java?");
				
				for(int i=0;i<DevMode.INCLUDEPROJECT.length&&!serverFlag;i++){
					if(DevMode.DEVMODE == DevMode.DEVMODE_ECLIPSE){
						String c = FileClassUtil.getParentDir(path.substring(0, path.indexOf(tail)))+"/"+DevMode.INCLUDEPROJECT[i]+"/bin";
						dirDevIncludeClasses.add(new File(c));
					}else if(DevMode.DEVMODE == DevMode.DEVMODE_NO_IDE){
					}
				}
			}else{
				throw new IllegalStateException("Run Directory is incorrect: You must run at \"<PROJECT>/eclipse\"!");
			}
		}
		dirModsVersion = new File(dirMods, (String)injectionData[4]);
		MMMLib.Debug("init FileManager.");
	}
	
	// TODO 今後使用しなさそう
	/*
	public static void setSrcPath(File file)
	{
		assetsDir = file.getPath() + "/assets";
		MMMLib.Debug("mods path =" + dirMods.getAbsolutePath());

		// eclipseの環境の場合、eclipseフォルダ配下のmodsを見に行く
		isDevdir = file.getName().equalsIgnoreCase("bin");
		if(isDevdir)
		{
			dirMods = new File(file.getParent()+"/eclipse/mods");
		}
		else
		{
			dirMods = new File(file.getParent());
		}
	}
	*/

	/**
	 * modsディレクトリに含まれるファイルを全て返す。<br>
	 * バージョンごとの物も含む。
	 * @return
	 */
	/*
	public static List<File> getAllmodsFiles() {
		List<File> llist = new ArrayList<File>();
		if (dirMods.exists()) {
			for (File lf : dirMods.listFiles()) {
				llist.add(lf);
			}
		}
		if (dirModsVersion.exists()) {
			for (File lf : dirModsVersion.listFiles()) {
				llist.add(lf);
			}
		}
		files = llist;
		return llist;
	}
	public static List<File> getAllmodsFiles(ClassLoader pClassLoader) {
		List<File> llist = new ArrayList<File>();
		if (pClassLoader instanceof URLClassLoader ) {
			for (URL lurl : ((URLClassLoader)pClassLoader).getURLs()) {
				try {
					String ls = lurl.toString();
					if (ls.endsWith("/bin/") || ls.indexOf("/mods/") > -1) {
						llist.add(new File(lurl.toURI()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		files = llist;
		return llist;
	}
	*/
	public static List<File> getAllmodsFiles(ClassLoader pClassLoader, boolean pFlag) {
		List<File> llist = new ArrayList<File>();
		if (pClassLoader instanceof URLClassLoader ) {
			for (URL lurl : ((URLClassLoader)pClassLoader).getURLs()) {
				try {
					String ls = lurl.toString();
					if (ls.endsWith("/bin/") || ls.indexOf("/mods/") > -1) {
						llist.add(new File(lurl.toURI()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (pFlag) {
			if (dirMods.exists()) {
				for (File lf : dirMods.listFiles()) {
					addList(llist, lf);
				}
			}
			if (dirModsVersion.exists()) {
				for (File lf : dirModsVersion.listFiles()) {
					addList(llist, lf);
				}
			}
		}
		files = llist;
		return llist;
	}

	protected static boolean addList(List<File> pList, File pFile) {
		for (File lf : pList) {
			try {
				if (pFile.getCanonicalPath().compareTo(lf.getCanonicalPath()) == 0) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pList.add(pFile);
		return true;
	}



	/**
	 * MODディレクトリに含まれる対象ファイルのオブジェクトを取得。
	 * @param pname 検索リスト名称、getFileList()で使う。
	 * @param pprefix この文字列の含まれるファイルを列挙する。
	 * @return 列挙されたファイルのリスト。
	 */
	public static List<File> getModFile(String pname, String pprefix) {
		// 検索済みかどうかの判定
		List<File> llist;
		if (fileList.containsKey(pname)) {
			llist = fileList.get(pname);
		} else {
			llist = new ArrayList<File>();
			fileList.put(pname, llist);
		}
		
		MMMLib.Debug("getModFile:[%s]:%s", pname, dirMods.getAbsolutePath());
		// ファイル・ディレクトリを検索
		if(DevMode.DEVMODE != DevMode.NOT_IN_DEV){
			//開発モード時はそちらを優先
			llist.add(dirDevClasses);
			if(DevMode.DEVMODE == DevMode.DEVMODE_NO_IDE) llist.add(dirDevClassAssets);
			if(DevMode.DEVMODE == DevMode.DEVMODE_ECLIPSE){
				for(File f:dirDevIncludeClasses){
					llist.add(f);
				}
			}
		}
		try {
			if (dirMods.isDirectory()) {
				MMMLib.Debug("getModFile-get:%d.", dirMods.list().length);
				for (File t : dirMods.listFiles()) {
					if (t.getName().indexOf(pprefix) != -1) {
						if (t.getName().endsWith(".zip") || t.getName().endsWith(".jar")) {
							llist.add(t);
							MMMLib.Debug("getModFile-file:%s", t.getName());
						} else if (t.isDirectory()) {
							llist.add(t);
							MMMLib.Debug("getModFile-file:%s", t.getName());
						}
					}
				}
				MMMLib.Debug("getModFile-files:%d", llist.size());
			} else {
				// まずありえない
				MMMLib.Debug("getModFile-fail.");
			}
		}
		catch (Exception exception) {
			MMMLib.Debug("getModFile-Exception.");
		}
		return llist;

	}
	public static void debugPrintAllFileList()
	{
		for(String key : fileList.keySet())
		{
			List<File> list = fileList.get(key);
			for(File f : list)
			{
				System.out.println("MMMLib-AllFileList ### " + key + " : " + f.getPath());
			}
		}
	}
	
	public static List<File> getFileList(String pname)
	{
		return fileList.get(pname);
	}
}
