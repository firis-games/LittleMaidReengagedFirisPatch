package net.blacklab.lmmnx.api.item;

import littleMaidMobX.LMM_EntityLittleMaid;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LMMNX_API_Item {
	public static boolean isSugar(Item item){
		if(item==null) return false;
		return item==Items.sugar||item instanceof LMMNX_IItemSpecialSugar;
	}
	
	public static boolean hasSugar(LMM_EntityLittleMaid maid){
		boolean flag = false;
		for(ItemStack stack:maid.maidInventory.mainInventory){
			if(stack==null) continue;
			if(isSugar(stack.getItem())){
				flag = false;
				break;
			}
		}
		return flag;
	}

}
