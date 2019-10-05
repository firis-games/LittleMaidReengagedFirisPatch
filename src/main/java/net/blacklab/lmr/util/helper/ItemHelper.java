package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.api.item.IItemSpecialSugar;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemHelper {
	public static boolean isSugar(Item item){
		if(item==null) return false;
		return item==Items.SUGAR || item instanceof IItemSpecialSugar;
	}

	public static boolean hasSugar(EntityLittleMaid maid){
		boolean flag = false;
		for(ItemStack stack: maid.maidInventory.mainInventory){
			if(stack.isEmpty()) continue;
			if(isSugar(stack.getItem())){
				flag = false;
				break;
			}
		}
		return flag;
	}

	public static boolean isItemBurned(ItemStack pItemstack) {
		return (!pItemstack.isEmpty() &&
				TileEntityFurnace.getItemBurnTime(pItemstack) > 0);
	}

	public static boolean isItemSmelting(ItemStack pItemstack) {
		return (!pItemstack.isEmpty() && FurnaceRecipes.instance().getSmeltingResult(pItemstack) != null);
	}

	@SuppressWarnings("deprecation")
	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack.isEmpty())
			return false;
		Item li = pItemstack.getItem();
		return (!pItemstack.isEmpty() && li instanceof ItemBlock && Block.getBlockFromItem(li).getMaterial(Block.getBlockFromItem(li).getDefaultState()) == Material.TNT);
	}

}
