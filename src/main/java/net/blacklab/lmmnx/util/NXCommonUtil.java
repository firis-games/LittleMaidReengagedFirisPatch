package net.blacklab.lmmnx.util;

import littleMaidMobX.LMM_EntityLittleMaid;
import littleMaidMobX.LMM_LittleMaidMobNX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.language;

public class NXCommonUtil {
	
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
		LMM_LittleMaidMobNX.Debug("GETCLASS %s - %s", path, root);
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
	
	/**
	 * メイドにアイテムを与える
	 */
	public static void giveItem(ItemStack stack, LMM_EntityLittleMaid maid) {
		int stacksize = stack.stackSize;
		
		for (int i=0; i<maid.maidInventory.mainInventory.length; i++) {
			ItemStack stack1 = maid.maidInventory.mainInventory[i];

			if (stack1 != null && stack != null) {
				// スタックが空でない場合は合成を行う．
				if (stack.getItem() == stack1.getItem() && stack.getItemDamage() == stack1.getItemDamage()) {
					int totalsize = stack1.stackSize + stacksize;
					int diffsize = totalsize - stack1.getItem().getItemStackLimit(stack1);
					if (diffsize<=0) {
						stack1.stackSize = totalsize;
						stack = null;
					} else {
						stack1.stackSize = stack1.getItem().getItemStackLimit(stack1);
						stack.stackSize = diffsize;
					}
				}
			} else if (stack != null) {
				// スタックが空の場合は投入
				maid.maidInventory.mainInventory[i] = stack.copy();
				stack = null;
			} else {
				// 処理対象がなくなったらその時点でループを抜ける．
				break;
			}
		}
		
		// それでも残ってしまったらドロップ
		if (stack!=null) {
			maid.entityDropItem(stack, 0);
		}
	}

}
