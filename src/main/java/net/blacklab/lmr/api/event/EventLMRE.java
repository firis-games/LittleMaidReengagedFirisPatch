package net.blacklab.lmr.api.event;

import net.blacklab.lib.vevent.IVEventCancelable;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class EventLMRE implements IVEventCancelable {
	
	private boolean canceled = false;
	
	public EntityLittleMaid maid;

	public static class MaidLevelUpEvent extends EventLMRE {
		
		public int level = 0;
		
		public MaidLevelUpEvent(EntityLittleMaid maid, int level) {
			super(maid);
			this.level = level;
		}
	}

	@Cancelable
	public static class ItemPutChestEvent extends EventLMRE{
		
		public IInventory target;
		public ItemStack stack;
		public int maidStackIndex;
	
		public ItemPutChestEvent(EntityLittleMaid maid, IInventory target, ItemStack stack, int maidStackIndex){
			super(maid);
			this.target = target;
			this.stack = stack;
			this.maidStackIndex = maidStackIndex;
		}
		
	}

	public EventLMRE(EntityLittleMaid living) {
		maid = living;
	}
	
	@Override
	public void setCanceled(boolean flag) {
		if (!canceled) canceled = flag; 
	}
	
	@Override
	public boolean isCanceled() {
		return canceled;
	}
	
	@Override
	public Result getResult() {
		return Result.PASS;
	}

	@Override
	public void setResult(Result result) {
	}
	
}
