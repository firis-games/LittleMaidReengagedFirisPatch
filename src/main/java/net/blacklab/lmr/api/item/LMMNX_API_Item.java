package net.blacklab.lmr.api.item;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LMMNX_API_Item {
	public static boolean isSugar(Item item){
		if(item==null) return false;
		return item==Items.sugar||item instanceof LMMNX_IItemSpecialSugar;
	}
	
	public static boolean hasSugar(EntityLittleMaid maid){
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
