package net.blacklab.lib.minecraft.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemUtil {
	
	public static Item getItemByStringId(String id){
		String modid,name = "";
		if(name.indexOf(":")==-1){
			modid= "minecraft";
			name = id;
		}else{
			modid= id.split(":")[0];
			name = id.split(":")[1];
		}
		Item item = GameRegistry.findItem(modid, name);
		return item;
	}

	public static boolean isHelm(ItemStack stack){
		if(stack!=null){
			if(stack.getItem() instanceof ItemArmor){
				if(((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD){
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getFoodAmount(ItemStack pStack) {
		if (pStack == null) {
			return -1;
		}
		if (pStack.getItem() instanceof ItemFood) {
			return ((ItemFood) pStack.getItem()).getHealAmount(pStack);
		}
		return -1;
	}
}
