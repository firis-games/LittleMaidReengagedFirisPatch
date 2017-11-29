package net.blacklab.lmr.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blacklab.lib.classutil.FileClassUtil;
import net.blacklab.lmr.LittleMaidReengaged;
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
	public static List<File> dirClasspath = new ArrayList<File>();
//	public static File[] dirDevIncludeAssets = new File[]{};

	public static String dirMinecraftPath	= "";

	static {
		Object[] injectionData = FMLInjectionData.data();
		dirMinecraft = (File) FMLInjectionData.data()[6];
		dirMinecraftPath = FileClassUtil.getLinuxAntiDotName(dirMinecraft.getAbsolutePath());
		if (dirMinecraftPath.endsWith("/")) {
			dirMinecraftPath = dirMinecraftPath.substring(0, dirMinecraftPath.lastIndexOf("/"));
		}
		dirMods = new File(dirMinecraft, "mods");

		// 開発モード
		dirModsVersion = new File(dirMods, (String)injectionData[4]);
		LittleMaidReengaged.Debug("init FileManager.");
	}

	// TODO 今後使用しなさそう
	/*
	public static void setSrcPath(File file)
	{
		assetsDir = file.getPath() + "/assets";
		LittleMaidReengaged.Debug("mods path =" + dirMods.getAbsolutePath());

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


}
