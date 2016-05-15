package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.item.ItemStack;

public class MaidHelper {

	/**
	 * メイドにアイテムを与える
	 */
	public static void giveItem(ItemStack stack, EntityLittleMaid maid) {
		if (!maid.maidInventory.addItemStackToInventory(stack)) {
			maid.entityDropItem(stack, 0);
		}
	}

}
