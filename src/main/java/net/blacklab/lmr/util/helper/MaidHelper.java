package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.minecraft.item.ItemStack;

public class MaidHelper {

	/**
	 * メイドにアイテムを与える
	 */
	public static void giveItem(ItemStack stack, EntityLittleMaid maid) {
		int stacksize = stack.stackSize;
		
		for (int i=0; i<InventoryLittleMaid.maxInventorySize; i++) {
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
