package net.blacklab.lmr.api.event;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * LMRFP用イベント
 * @author firis-games
 *
 */
public class EventLMRE extends Event {
	
	/**
	 * チェスト格納イベント
	 * @author firis-games
	 *
	 */
	@Cancelable
	public static class ItemPutChestEvent extends EventLMRE {
		
		public EntityLittleMaid maid;
		public IInventory target;
		public ItemStack stack;
		public int maidStackIndex;
	
		public ItemPutChestEvent(EntityLittleMaid maid, IInventory target, ItemStack stack, int maidStackIndex) {
			this.maid = maid;
			this.target = target;
			this.stack = stack;
			this.maidStackIndex = maidStackIndex;
		}
	}
	
	/**
	 * レベルが上がったタイミングのイベント
	 * @author firis-games
	 *
	 */
	@Cancelable
	public static class MaidLevelUpEvent extends EventLMRE {
		
		public EntityLittleMaid maid;
		public int level = 0;
		
		public MaidLevelUpEvent(EntityLittleMaid maid, int level) {
			this.maid = maid;
			this.level = level;
		}
	}
}
