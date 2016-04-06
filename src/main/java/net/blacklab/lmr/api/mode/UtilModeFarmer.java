package net.blacklab.lmr.api.mode;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lib.minecraft.item.ItemUtil;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.TriggerSelect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class UtilModeFarmer {

	private static List<String> api_seedItems = new ArrayList<String>();
	private static List<String> api_cropItems = new ArrayList<String>();
	/**
	 * 渡されたItemが登録されている「種」であるかどうか
	 */
	public static boolean isSeed(Item pItem){
		for(String fname:api_seedItems){
			Item item = ItemUtil.getItemByStringId(fname);
			if(pItem==item) return true;
		}
		return false;
	}
	/**
	 * 渡されたItemが登録されている「穀物」であるかどうか
	 */
	public static boolean isCrop(Item pItem){
		for(String fname:api_cropItems){
			Item item = ItemUtil.getItemByStringId(fname);
			if(pItem==item) return true;
		}
		return false;
	}
	
	/**
	 * 「種」としてアイテムを登録
	 * @param string 文字列アイテムID
	 */
	public static void addItemsForSeed(String string){
		api_seedItems.add(string);
	}
	
	/**
	 * 「穀物」としてアイテムを登録
	 * @param string 文字列アイテムID
	 */
	public static void addItemsForCrop(String string){
		api_cropItems.add(string);
	}
	
	/**
	 * 登録されている「種」を取得
	 * @return 登録アイテムの文字列IDが列挙されたArrayList
	 */
	public static ArrayList<String> getItemsListForSeed(){
		return new ArrayList<String>(api_seedItems);
	}
	
	/**
	 * 登録されている「穀物」を取得
	 * @return 登録アイテムの文字列IDが列挙されたArrayList
	 */
	public static ArrayList<String> getItemsListForCrop(){
		return new ArrayList<String>(api_cropItems);
	}
	/**
	 * 渡されたItemStackがクワのものかどうか
	 */
	public static boolean isHoe(EntityLittleMaid owner, ItemStack pItemStack){
		if(pItemStack==null) return false;
		if(pItemStack.getItem()==null) return false;
		return pItemStack.getItem() instanceof ItemHoe ||
				TriggerSelect.checkWeapon(owner.getMaidMasterUUID(), "Hoe", pItemStack);
	}

}
