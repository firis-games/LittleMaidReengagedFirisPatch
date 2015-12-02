package net.blacklab.lib.classutil;

public class FileClassUtil {
	
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
	
	/**与えるパスはgetLinuxAntiDotNameで正規化したものでなくてはならない
	 * @param path
	 * @return
	 */
	public static String getParentDir(String path){
		if(path.endsWith("/.")) path=path.substring(0,path.length()-1);
		if(path.endsWith("/")) path=path.substring(0,path.length()-1);
		return path.substring(0,path.lastIndexOf("/"));
	}

}
