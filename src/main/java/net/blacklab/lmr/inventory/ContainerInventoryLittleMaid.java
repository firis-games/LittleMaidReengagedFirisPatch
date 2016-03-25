package net.blacklab.lmr.inventory;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.LMMNX_Achievements;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerInventoryLittleMaid extends ContainerPlayer {

	protected final InventoryLittleMaid littlemaidInventory;
	protected final int numRows;
	protected final EntityLittleMaid owner;

	public ContainerInventoryLittleMaid(IInventory playerInventory, EntityLittleMaid pEntity) {
		// >
//		super();
		super(pEntity.maidInventory, !pEntity.worldObj.isRemote, pEntity.maidAvatar);
		inventorySlots.clear();
		inventoryItemStacks.clear();
		// <

		InventoryLittleMaid maidInventory = pEntity.maidInventory;
		owner = pEntity;
		numRows = maidInventory.getSizeInventory() / 9;
		littlemaidInventory = maidInventory;
		littlemaidInventory.openInventory(owner.maidAvatar);

		for (int ly = 0; ly < numRows; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(maidInventory, lx + ly * 9, 8 + lx * 18, 76 + ly * 18));
			}
		}

		int lyoffset = (numRows - 4) * 18 + 59;
		for (int ly = 0; ly < 3; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(playerInventory, lx + ly * 9 + 9, 8 + lx * 18, 103 + ly * 18 + lyoffset));
			}
		}

		for (int lx = 0; lx < 9; lx++) {
			addSlotToContainer(new Slot(playerInventory, lx, 8 + lx * 18, 161 + lyoffset));
		}

		for (int j = 0; j < 4; j++) {
//			int j1 = j + 1;
//			addSlotToContainer(new SlotArmor(this, linventory, linventory.getSizeInventory() - 2 - j, 8, 8 + j * 18, j1));

			final int armorIndex = j; // ヘルメットはないと思っていたのか！
			this.addSlotToContainer(new Slot(maidInventory, InventoryLittleMaid.maxInventorySize+armorIndex, 8 + 72*((3-j)/2), 8 + ((3-j)%2)*36)
			{
				/**
				 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
				 * in the case of armor slots)
				 */
				public int getSlotStackLimit()
				{
					return 1;
				}
				/**
				 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
				 */
				public boolean isItemValid(ItemStack par1ItemStack)
				{
					if (par1ItemStack == null) return false;
					boolean flag = littlemaidInventory.isItemValidForSlot(littlemaidInventory.maxInventorySize+armorIndex, par1ItemStack);
					LittleMaidReengaged.Debug("SLOT-INDEX: %d; VALID? %s", getSlotIndex(), flag);
					return flag;
				}
				/**
				 * Returns the icon index on items.png that is used as background image of the slot.
				 */
				/*
				@SideOnly(Side.CLIENT)
				public IIcon getBackgroundIconIndex()
				{
					return ItemArmor.func_94602_b(armorIndex);
				}
				*/
				@SideOnly(Side.CLIENT)
				public String getSlotTexture()
				{
					return ItemArmor.EMPTY_SLOT_NAMES[armorIndex];
				}
			});
		}
	}

	@Override
	public void putStackInSlot(int p_75141_1_, ItemStack p_75141_2_) {
		super.putStackInSlot(p_75141_1_, p_75141_2_);
		checkAchievements();
	}

	@Override
	public void putStacksInSlots(ItemStack[] p_75131_1_) {
		super.putStacksInSlots(p_75131_1_);
		checkAchievements();
	}

	protected void checkAchievements() {
		boolean flag = true;
		Slot slot;
		Item item;
		flag &= (slot = getSlot(54)).getHasStack() && (item = slot.getStack().getItem()) instanceof ItemArmor &&
				((ItemArmor)item).getArmorMaterial() == ArmorMaterial.DIAMOND;
		flag &= (slot = getSlot(55)).getHasStack() && (item = slot.getStack().getItem()) instanceof ItemArmor &&
				((ItemArmor)item).getArmorMaterial() == ArmorMaterial.DIAMOND;
		flag &= (slot = getSlot(56)).getHasStack() && (item = slot.getStack().getItem()) instanceof ItemArmor &&
				((ItemArmor)item).getArmorMaterial() == ArmorMaterial.DIAMOND;

		if (flag && !owner.worldObj.isRemote)
			owner.getMaidMasterEntity().addStat(LMMNX_Achievements.ac_Overprtct);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// 開けるかどうかの判定
		EntityLittleMaid entitylittlemaid = littlemaidInventory.entityLittleMaid;
		if(entitylittlemaid.isDead) {
//		if(entitylittlemaid.isDead || entitylittlemaid.isOpenInventory()) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(entitylittlemaid) <= 64D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int pIndex) {
		ItemStack litemstack = null;
		Slot slot = (Slot)inventorySlots.get(pIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();

			litemstack = itemstack1.copy();
			int lline = numRows * 9;
			if (pIndex < lline) {
				if (!this.mergeItemStack(itemstack1, lline, lline + 36, true)) {
					return null;
				}
			} else if (pIndex >= lline && pIndex < lline + 36) {
				if (!this.mergeItemStack(itemstack1, 0, lline, false)) {
					return null;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, lline + 36, false)) {
					return null;
				}
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return litemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		littlemaidInventory.closeInventory(owner.maidAvatar);
	}

	@Override
	public boolean canMergeSlot(ItemStack par1ItemStack, Slot par2Slot) {
		return true;
	}

}
