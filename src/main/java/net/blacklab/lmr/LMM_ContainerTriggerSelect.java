package net.blacklab.lmr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mmmlibx.lib.ContainerCreative;
import mmmlibx.lib.MMM_Helper;
import net.blacklab.lmr.util.TriggerSelect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LMM_ContainerTriggerSelect extends ContainerCreative {

	public List<ItemStack> weaponSelect = new ArrayList<ItemStack>();
	public String weaponSelectName;
	public List<Item> weaponSelectList;
	public int weaponOffset;

	public LMM_ContainerTriggerSelect(EntityPlayer entityplayer) {
		super(entityplayer);

		inventorySlots.clear();
		for (int l2 = 0; l2 < 5; l2++) {
			for (int j3 = 0; j3 < 8; j3++) {
				addSlotToContainer(new Slot(LMM_GuiTriggerSelect.getInventory1(),
						j3 + l2 * 8, 8 + j3 * 18, 18 + l2 * 18));
			}
		}
		
		for (int l2 = 0; l2 < 4; l2++) {
			for (int j3 = 0; j3 < 8; j3++) {
				addSlotToContainer(new Slot(LMM_GuiTriggerSelect.getInventory2(),
						j3 + l2 * 8, 8 + j3 * 18, 121 + l2 * 18));
			}
			
		}
		
		setWeaponSelect(MMM_Helper.getPlayerName(entityplayer), TriggerSelect.selector.get(0));
		
		initAllSelections();
		scrollTo(0.0F);
		setWeaponlist(0.0F);
	}
	
	private void initAllSelections() {
		// コンテナ表示用アイテムの設定
		this.itemList.clear();

		for (Object o : Item.itemRegistry.getKeys())
		{
			Item item = (Item)Item.itemRegistry.getObject((ResourceLocation) o);
			
			if (item != null && item.getCreativeTab() != null) {
				item.getSubItems(item, (CreativeTabs) null, (List<ItemStack>) this.itemList);
			}
		}

		// List 生成 (ソート用)
		Comparator cmp = new Comparator<ItemStack>()
			{
				public int compare(ItemStack i1, ItemStack i2)
				{
					Item item1 = i1.getItem();
					Item item2 = i2.getItem();
					CreativeTabs ct1 = item1.getCreativeTab();
					CreativeTabs ct2 = item2.getCreativeTab();
					if(ct1!=null && ct2!=null)
					{
						if(ct1.getTabIndex() != ct2.getTabIndex())
						{
							return ct1.getTabIndex() < ct2.getTabIndex()? -1: 1;
						}
					}
					if(item1 == item2)
					{
						System.out.println(i1.getDisplayName() + " : " + i2.getDisplayName());
						return i1.getItemDamage() < i2.getItemDamage()? -1: 1;
					}
					
					return (item1.getUnlocalizedName()).compareTo(item2.getUnlocalizedName());
				}
			};
		Collections.sort(this.itemList, cmp);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void scrollTo(float f) {
		// スクロールポジション
		int i = (itemList.size() / 8 - 5) + 1;
		int j = (int) (f * i + 0.5D);
		if (j < 0) {
			j = 0;
		}
		for (int k = 0; k < 5; k++) {
			for (int l = 0; l < 8; l++) {
				int i1 = l + (k + j) * 8;
				if (i1 >= 0 && i1 < itemList.size()) {
					LMM_GuiTriggerSelect.getInventory1().setInventorySlotContents(l + k * 8, (ItemStack) itemList.get(i1));
				} else {
					LMM_GuiTriggerSelect.getInventory1().setInventorySlotContents(l + k * 8, null);
				}
			}

		}

	}

	@Override
	public ItemStack slotClick(int i, int j, int flag, EntityPlayer entityplayer) {
		if (i >= 40) {
			// セットされたアイテムを定義
			int lk = (i - 40) + weaponOffset * 8;
			for (; weaponSelect.size() <= lk + 7;) {
				weaponSelect.add(null);
			}
			weaponSelect.set(lk, entityplayer.inventory.getItemStack());
		}
		
		if (i == -999) {
			entityplayer.inventory.setItemStack(null);
		}
		ItemStack is = super.slotClick(i, j, flag, entityplayer);

		return is;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		// Shiftクリック時の反応
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			if (i < 40) {
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				mergeItemStack(itemstack1, 40, 72, false);
			} else {
				slot.putStack(null);
			}
		}
		return itemstack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag) {
		// itemstack以外は無効
		boolean flag1 = false;
		int k = 0;
		
		// 同じのがあったときは追加しない
		while (itemstack.stackSize > 0 && k < weaponSelect.size()) {
			ItemStack itemstack1 = weaponSelect.get(k);
			if (itemstack1 != null) {
				if (itemstack1.isItemEqual(itemstack)) {
					// 同一アイテムである
					flag1 = true;
					break;
				}
			} else {
				weaponSelect.set(k, itemstack);
				flag1 = true;
				break;
			}
			k++;
		}
		if (!flag1) {
			weaponSelect.add(itemstack);
			setWeaponlist(1.0F);
		} else {
			int m = (weaponSelect.size() / 8 - 4) + 1;
			int n = k / 8;
			float f = (float) n / (float) m;
			if (f < 0.0F)
				f = 0.0F;
			if (f > 1.0F)
				f = 1.0F;
			setWeaponlist(f);
		}
		
		return flag1;
	}

	public void setWeaponlist(float f) {
		// スクロールポジション
		int i = (weaponSelect.size() / 8 - 4) + 1;
		weaponOffset = (int) (f * i + 0.5D);
		if (weaponOffset < 0) {
			weaponOffset = 0;
		}
		for (int k = 0; k < 4; k++) {
			for (int l = 0; l < 8; l++) {
				int i1 = l + (k + weaponOffset) * 8;
				if (i1 >= 0 && i1 < weaponSelect.size()) {
					LMM_GuiTriggerSelect.getInventory2().setInventorySlotContents(k * 8 + l, weaponSelect.get(i1));
				} else {
					LMM_GuiTriggerSelect.getInventory2().setInventorySlotContents(k * 8 + l, null);
				}
			}
		}
	}

	public void setWeaponSelect(String pUsername, String pName) {
		weaponSelect.clear();
		weaponSelectName = pName;
		weaponSelectList = TriggerSelect.getuserTriggerList(pUsername, pName);

		for (Item li : weaponSelectList) {
			if (li != null)
			{
				weaponSelect.add(new ItemStack(li));
			}
		}
	}

	public List getItemList() {
		return weaponSelectList;
	}

	@Override
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
		return false;
	}

	@Override
	public boolean canDragIntoSlot(Slot par1Slot) {
		return false;
	}


}
