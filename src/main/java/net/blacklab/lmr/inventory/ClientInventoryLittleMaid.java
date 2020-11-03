package net.blacklab.lmr.inventory;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class ClientInventoryLittleMaid extends InventoryLittleMaid {
	
	/**
	 * Inventory保存領域
	 */
	protected final NonNullList<ItemStack> inventorySlotItemStack;
	
	/**
	 * Field保存領域
	 */
	protected final NonNullList<Integer> fieldList;
	
	protected final IInventory iinventory;
	
	public ClientInventoryLittleMaid(EntityLittleMaid littleMaid) {
		
		super(littleMaid);
		
		InventoryLittleMaid maidInventory = littleMaid.maidInventory;
		
		this.inventorySlotItemStack = NonNullList.<ItemStack>withSize(maidInventory.getSizeInventory(), ItemStack.EMPTY);
		
		this.fieldList = NonNullList.<Integer>withSize(maidInventory.getFieldCount(), 0);
		//初期値設定
		for (int i = 0; i < maidInventory.getFieldCount(); i++) {
			this.fieldList.set(i, maidInventory.getField(i));
		}
		
		this.iinventory = maidInventory;
	}


	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventorySlotItemStack.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(inventorySlotItemStack, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventorySlotItemStack, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventorySlotItemStack.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {		
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		entityLittleMaid.onGuiOpened();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		entityLittleMaid.onGuiClosed();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return this.iinventory.isItemValidForSlot(index, stack);
	}

	@Override
	public int getField(int id) {
		return this.fieldList.get(id);
	}

	@Override
	public void setField(int id, int value) {
		this.fieldList.set(id, value);
	}

	@Override
	public int getFieldCount() {
		return this.fieldList.size();
	}

	@Override
	public void clear() {
	}

}
