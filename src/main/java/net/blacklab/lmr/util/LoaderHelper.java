package net.blacklab.lmr.util;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.item.Item;
import scala.language;

public class LoaderHelper {
	
	/**
	 * ファイルパスをLinux区切りに変換し、間に挟まった"."を除去します。
	 * @param par1
	 * @return
	 */
	public static String getLinuxAntiDotName(String par1){
		par1 = par1.replace("\\", "/").replace("/./", "/");
		if(par1.endsWith("/.")) par1.substring(0, par1.lastIndexOf("/."));
		return par1;
	}
	
	/**
	 * ファイルからクラスを読み取る時用。root以下にあるpathについてクラス名に変換する。
	 * @param path
	 * @param root
	 * @return
	 */
	public static String getClassName(String path, String root){
		LittleMaidReengaged.Debug("GETCLASS %s - %s", path, root);
		if(!path.endsWith(".class")) return null; 
		
		if(root!=null){
			if(!root.isEmpty()&&path.startsWith(root)){
				path = path.substring(root.length());
			}
		}
		if(path.startsWith("/")) path = path.substring(1);
		if(path.endsWith(".class")) path = path.substring(0,path.lastIndexOf(".class"));
		return path.replace("/", ".");
	}

}
