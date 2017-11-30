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

	public static File dirMinecraft;
	public static File dirMods;
	public static List<File> filesMods;
	public static List<File> dirClasspath = new ArrayList<>();

	public static String dirMinecraftPath	= "";

	static {
		Object[] injectionData = FMLInjectionData.data();
		dirMinecraft = (File) FMLInjectionData.data()[6];
		dirMinecraftPath = FileClassUtil.getLinuxAntiDotName(dirMinecraft.getAbsolutePath());
		if (dirMinecraftPath.endsWith("/")) {
			dirMinecraftPath = dirMinecraftPath.substring(0, dirMinecraftPath.lastIndexOf("/"));
		}
		dirMods = new File(dirMinecraft, "mods");

		// Check if version directory exists
		File dirModsVersion = new File(dirMods, (String)injectionData[4]);
		if (dirModsVersion.isDirectory()) {
			dirMods = dirModsVersion;
		}

		// List 'files' up in mods
		filesMods = new ArrayList<>();
		filesMods.addAll(Arrays.asList(dirMods.listFiles()));

		for (File child :
				new ArrayList<>(filesMods)) {
			if (!child.isFile()) {
				filesMods.remove(child);
			}
		}

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
