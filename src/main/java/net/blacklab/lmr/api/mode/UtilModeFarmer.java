package net.blacklab.lmr.api.mode;

import java.util.UUID;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.TriggerSelect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

public class UtilModeFarmer {

	/**
	 * 渡されたItemが「種」であるかどうか
	 */
	public static boolean isSeed(UUID pUuid, Item pItem){
		return pItem instanceof IPlantable || TriggerSelect.checkTrigger(pUuid, "Seed", pItem);
	}

	/**
	 * 渡されたItemStackがクワのものかどうか
	 */
	public static boolean isHoe(EntityLittleMaid owner, ItemStack pItemStack){
		if(pItemStack==null) return false;
		if(pItemStack.getItem()==null) return false;
		return pItemStack.getItem() instanceof ItemHoe ||
				TriggerSelect.checkTrigger(owner.getMaidMasterUUID(), "Hoe", pItemStack.getItem());
	}

}
