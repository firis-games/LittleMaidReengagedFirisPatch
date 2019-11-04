package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.api.item.IItemSpecialSugar;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid.EnumConsumeSugar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemHelper {
	
	/**
	 * 対象アイテムが砂糖系アイテムか判断する
	 * @param item
	 * @return
	 */
	public static boolean isSugar(ItemStack sugar) {
		
		//砂糖判断
		if (!sugar.isEmpty() && sugar.getItem() == Items.SUGAR) return true;
		
		//IItemSpecialSugar判断
		if (!sugar.isEmpty() && sugar.getItem() instanceof IItemSpecialSugar) return true;
		
		//設定から判断
		if (!sugar.isEmpty()) {
			if (LMRConfig.cfg_sugar_item_ids.contains(sugar.getItem().getRegistryName().toString())) return true;
		}
		
		return false;
	}
	
	/**
	 * IItemSpecialSugar.onSugarInteractのhelper
	 * 砂糖として使用できるかを判断するAPI
	 * @param maid
	 * @param givePlayer
	 * @param sugar
	 * @return
	 */
	public static boolean onSugarInteract(EntityLittleMaid maid, EntityPlayer givePlayer, ItemStack sugar) {
		boolean cmode = true;
		if(sugar.getItem() instanceof IItemSpecialSugar){
			cmode = ((IItemSpecialSugar)sugar.getItem()).onSugarInteract(maid.getEntityWorld(), givePlayer, sugar, maid);
		}
		return cmode;
	}
	
	/**
	 * IItemSpecialSugar.onSugarEatenのhelper
	 * 砂糖として食べることができるかを判断するAPI
	 * メイドに対して回復したりポーション効果を付与したりなどをやることを想定
	 * @param maid
	 * @param mode
	 * @param sugar
	 * @return
	 */
	public static boolean onSugarEaten(EntityLittleMaid maid, EnumConsumeSugar mode, ItemStack sugar) {
		boolean ret = true;
		if(sugar.getItem() instanceof IItemSpecialSugar){
			ret = ((IItemSpecialSugar)sugar.getItem()).onSugarEaten(maid, mode, sugar);
		}
		return ret;
	}

	/**
	 * メイドが砂糖を持っているか判断する
	 * @param maid
	 * @return
	 */
	public static boolean hasSugar(EntityLittleMaid maid){
		boolean flag = false;
		for(ItemStack stack: maid.maidInventory.mainInventory){
			if(stack.isEmpty()) continue;
			if(isSugar(stack)){
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 契約アイテム（ケーキ）かの判断を行う
	 * @param item
	 * @return
	 */
	public static boolean isCake(ItemStack cake) {
		
		//ケーキ判断
		if (!cake.isEmpty() && cake.getItem() == Items.CAKE) return true;
		
		//設定から判断
		if (!cake.isEmpty()) {
			if (LMRConfig.cfg_cake_item_ids.contains(cake.getItem().getRegistryName().toString())) return true;
		}
		return false;
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
