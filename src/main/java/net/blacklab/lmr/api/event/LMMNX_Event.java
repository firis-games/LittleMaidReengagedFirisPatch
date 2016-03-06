package net.blacklab.lmr.api.event;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LMMNX_Event extends Event {
	
	public static class LMMNX_MaidLevelUpEvent extends LMMNX_Event {
		
		public int level = 0;

		public LMMNX_MaidLevelUpEvent(EntityLittleMaid maid, int level) {
			super(maid);
			this.level = level;
		}
	}

	public LMMNX_Event(EntityLittleMaid maid) {
		this.maid = maid;
	}
	
	@Cancelable
	public static class LMMNX_ItemPutChestEvent extends LMMNX_Event{
		
		public IInventory target;
		public ItemStack stack;
		public int maidStackIndex;
		
		public LMMNX_ItemPutChestEvent(EntityLittleMaid maid, IInventory target, ItemStack stack, int maidStackIndex){
			super(maid);
			this.target = target;
			this.stack = stack;
			this.maidStackIndex = maidStackIndex;
			this.setCanceled(false);
		}
		
	}

	public EntityLittleMaid maid;
	
}
