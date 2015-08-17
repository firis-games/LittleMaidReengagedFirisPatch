package net.blacklab.lib;

import net.minecraft.item.Item;
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

}
