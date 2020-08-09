package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.api.item.IItemSpecialSugar;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid.EnumConsumeSugar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
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
			if (LMRConfig.cfg_sugar_item_ids_map.containsKey(sugar.getItem().getRegistryName().toString())) return true;
		}
		
		return false;
	}
	
	/**
	 * 対象の砂糖系アイテムの回復量を取得する
	 * @param sugar
	 * @return
	 */
	public static int isSugarHeal(ItemStack sugar) {

		//砂糖判断
		if (!sugar.isEmpty() && sugar.getItem() == Items.SUGAR) return 1;
		
		//IItemSpecialSugar判断
		if (!sugar.isEmpty() && sugar.getItem() instanceof IItemSpecialSugar) return 1;
		
		//設定から判断
		if (!sugar.isEmpty()) {
			if (LMRConfig.cfg_sugar_item_ids_map.containsKey(sugar.getItem().getRegistryName().toString())) {
				return LMRConfig.cfg_sugar_item_ids_map.get(sugar.getItem().getRegistryName().toString());
			}
		}
		
		//対象外の場合は0とする
		return 0;
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
		
		//砂糖による回復量
		int heal = isSugarHeal(sugar);
		if (heal <= 0) {
			ret = false;
		} else if (heal >= 2) {
			maid.heal(heal - 1);
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
	 * メイドさんが持っている砂糖の数を取得する
	 * @param maid
	 * @return
	 */
	public static int getSugarCount(EntityLittleMaid maid) {
		int sugar = 0;
		for (int i = 0; i < maid.maidInventory.getSizeInventory(); i++) {
			ItemStack stack = maid.maidInventory.getStackInSlot(i);
			if (ItemHelper.isSugar(stack)) {
				sugar += stack.getCount();
			}
		}
		return sugar;
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
		
		//料理対象外の場合は
		if (!pItemstack.isEmpty()) {
			if (LMRConfig.cfg_cock_no_cooking_item_ids.contains(pItemstack.getItem().getRegistryName().toString())) {
				//料理対象外の場合はfalseを返す
				return false;
			}
		}
		
		return (!pItemstack.isEmpty() 
				&& !FurnaceRecipes.instance().getSmeltingResult(pItemstack).isEmpty());
	}

	@SuppressWarnings("deprecation")
	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack.isEmpty())
			return false;
		Item li = pItemstack.getItem();
		return (!pItemstack.isEmpty() && li instanceof ItemBlock && Block.getBlockFromItem(li).getMaterial(Block.getBlockFromItem(li).getDefaultState()) == Material.TNT);
	}
	
	
	/**
	 * net.blacklab.lib.minecraft.item.ItemUtilから移動
	 * @param pStack
	 * @return
	 */
	public static int getFoodAmount(ItemStack pStack) {
		if (pStack.isEmpty()) {
			return -1;
		}
		if (pStack.getItem() instanceof ItemFood) {
			return ((ItemFood) pStack.getItem()).getHealAmount(pStack);
		}
		return -1;
	}
	
	/**
	 * net.blacklab.lib.minecraft.item.ItemUtilから移動
	 * @param stack
	 * @return
	 */
	public static boolean isHelm(ItemStack stack){
		if(!stack.isEmpty()){
			if(stack.getItem() instanceof ItemArmor){
				if(((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD){
					return true;
				}
			}
		}
		return false;
	}

}
