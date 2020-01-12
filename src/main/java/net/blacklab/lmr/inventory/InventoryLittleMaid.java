package net.blacklab.lmr.inventory;

import java.util.Iterator;
import java.util.List;

import net.blacklab.lib.minecraft.item.ItemUtil;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryLittleMaid extends InventoryPlayer {
	
	public boolean inventoryChanged = false;
	
	@Override
	public void markDirty() {
		super.markDirty();
        this.inventoryChanged = true;
    }
	
	/**
	 * 最大インベントリ数
	 */
	public static final int maxInventorySize = 18;
	/**
	 * Offset of mainHand slot's index
	 */
	public static final int handInventoryOffset = maxInventorySize + 4;

	/**
	 * Inventory "inside skirt"
	 */
//	public ItemStack mainInventory[] = new ItemStack[maxInventorySize];

	/**
	 * Armor Inventory
	 */
//	private ItemStack armorInventory[] = new ItemStack[4];

	/**
	 * Hand Slots
	 */
	public final NonNullList<ItemStack> mainHandInventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

	/**
	 * オーナー
	 */
	public EntityLittleMaid entityLittleMaid;

	/**
	 * スロット変更チェック用
	 */
	public final NonNullList<ItemStack> prevItems;

	public InventoryLittleMaid(EntityLittleMaid par1EntityLittleMaid) {
		super(par1EntityLittleMaid.maidAvatar);

		entityLittleMaid = par1EntityLittleMaid;
//		player = entityLittleMaid.maidAvatar;
		// TODO InventoryPlayer.mainInventory became 'final'. S**t
//		mainInventory = new ItemStack[maxInventorySize];
		prevItems = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(NBTTagList par1nbtTagList) {
		for (int i = 0; i < par1nbtTagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = par1nbtTagList.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = new ItemStack(nbttagcompound);

			if (itemstack.isEmpty()) {
				continue;
			}

			if (j >= 0 && j < InventoryLittleMaid.maxInventorySize) {
				mainInventory.set(j, itemstack);
			}

			if (j >= 100 && j < armorInventory.size() + 100) {
				armorInventory.set(j - 100, itemstack);
			}

			if (j >= 150 && j < armorInventory.size() + 150) {
				offHandInventory.set(j - 150, itemstack);
			}

			if (j >= 200 && j < mainHandInventory.size() + 200) {
				mainHandInventory.set(j - 200, itemstack);
			}
		}
	}

	@Override
	public NBTTagList writeToNBT(NBTTagList nbtTagListIn) {
		NBTTagList result = super.writeToNBT(nbtTagListIn);

		for (int k = 0; k < mainHandInventory.size(); ++k) {
			if (!this.mainHandInventory.get(k).isEmpty()) {
				NBTTagCompound nbttagcompound2 = new NBTTagCompound();
				nbttagcompound2.setByte("Slot", (byte)(k + 200));
				this.mainHandInventory.get(k).writeToNBT(nbttagcompound2);
				result.appendTag(nbttagcompound2);
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return "LOSE";
	}

	@Override
	public int getSizeInventory() {
		// 一応
		return handInventoryOffset + 2;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		entityLittleMaid.onGuiOpened();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		entityLittleMaid.onGuiClosed();
	}

	public void decrementAnimations() {
		for (int li = 0; li < getSizeInventory(); ++li) {
			if (!getStackInSlot(li).isEmpty()) {
				try {
					getStackInSlot(li).updateAnimation(this.player.getEntityWorld(),
							entityLittleMaid, li, this.currentItem == li);
				} catch (ClassCastException e) {
					getStackInSlot(li).updateAnimation(this.player.getEntityWorld(),
							entityLittleMaid.maidAvatar, li, this.currentItem == li);
				}
			}
		}
	}

/*
	@Override
	public int getTotalArmorValue() {
		// 身に着けているアーマーの防御力の合算
		// 頭部以外
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		// int li = super.getTotalArmorValue() * 20 / 17;
		int li = super.getTotalArmorValue();
		// 兜分の補正
		for (int lj = 0; lj < armorInventory.length; lj++) {
			if (armorInventory[lj] != null
					&& armorInventory[lj].getItem() instanceof ItemArmor) {
				li++;
			}
		}
		armorInventory[3] = lis;
		return li;
	}
*/

	// From InventoryPlayer
	public void damageArmor(float pDamage) {
		pDamage = Math.max(pDamage/4, 1);

		for (int i = 0; i < armorInventory.size(); ++i) {
			if (!armorInventory.get(i).isEmpty() && armorInventory.get(i).getItem() instanceof ItemArmor) {
				armorInventory.get(i).damageItem((int)pDamage, player);

				if (armorInventory.get(i).getCount() == 0) {
					armorInventory.set(i, ItemStack.EMPTY);
				}
			}
		}
	}
/*
	@Override
	public int getDamageVsEntity(Entity entity) {
		return getDamageVsEntity(entity, currentItem);
	}

	public int getDamageVsEntity(Entity entity, int index) {
		if (index < 0 || index >= getSizeInventory()) return 1;
		ItemStack itemstack = getStackInSlot(index);
		if (itemstack != null) {
			if (itemstack.getItem() instanceof ItemAxe) {
				// アックスの攻撃力を補正
				return itemstack.getDamageVsEntity(entity) * 3 / 2 + 1;

			} else {
				return itemstack.getDamageVsEntity(entity);
			}
		} else {
			return 1;
		}
	}
*/
	public void dropAllItems(boolean detonator) {
		// インベントリをブチマケロ！
		Explosion lexp = null;
		if (detonator) {
			// Mobによる破壊の是非
			lexp = new Explosion(entityLittleMaid.getEntityWorld(), entityLittleMaid,
					entityLittleMaid.posX, entityLittleMaid.posY, entityLittleMaid.posZ, 3F, false, entityLittleMaid.getEntityWorld().getGameRules().getBoolean("mobGriefing"));
		}

//		armorInventory[3] = null;
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack it = getStackInSlot(i);
			if (!it.isEmpty()) {
				if (detonator && isItemExplord(i)) {
					Item j = it.getItem();
					for (int l = 0; l < it.getCount(); l++) {
						// 爆薬ぶちまけ
						((BlockTNT)Block.getBlockFromItem(j)).onBlockDestroyedByExplosion(
								entityLittleMaid.getEntityWorld(),
								new BlockPos(
									MathHelper.floor(entityLittleMaid.posX)
									+ entityLittleMaid.getRNG().nextInt(7) - 3,
									MathHelper.floor(entityLittleMaid.posY)
									+ entityLittleMaid.getRNG().nextInt(7) - 3,
									MathHelper.floor(entityLittleMaid.posZ)
									+ entityLittleMaid.getRNG().nextInt(7) - 3
								), lexp);
					}
				} else {
					entityLittleMaid.entityDropItem(it, 0F);
				}
			}
			setInventorySlotContents(i, ItemStack.EMPTY);
		}
		if (detonator) {
			lexp.doExplosionA();
			lexp.doExplosionB(true);
		}
	}

	public void dropAllItems() {
		dropAllItems(false);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (entityLittleMaid.isDead) {
			return false;
		}
		return player.getDistanceSq(entityLittleMaid) <= 64D;
	}

	public ItemStack getCurrentItem() {
		if (currentItem >= handInventoryOffset + 1) {
			return offHandInventory.get(currentItem - (handInventoryOffset + 1));
		}
		if (currentItem >= handInventoryOffset) {
			return mainHandInventory.get(currentItem - handInventoryOffset);
		}
		if (currentItem >= 0 && currentItem < InventoryLittleMaid.maxInventorySize) {
			return mainInventory.get(currentItem);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean addItemStackToInventory(final ItemStack par1ItemStack) {
		if (par1ItemStack.isEmpty()) {
			return false;
		}

		int empty = getFirstEmptyStack();

		// Picking up items
		ItemStack buffer = par1ItemStack;
		int originalStackSize = buffer.getCount();

		// Can be merged to dedicated slots
		for (int i=0; i < getSizeInventory(); i++) {
			ItemStack targetStack = getStackInSlot(i);

			if (!targetStack.isEmpty() && targetStack.getItem() == buffer.getItem()) {
				int maxStackSize = targetStack.getItem().getItemStackLimit(targetStack);
				if (targetStack.getCount() == maxStackSize) continue;

				// Check item damage and NBT
				boolean flag = true;
				flag &= targetStack.getItemDamage() == buffer.getItemDamage();
				flag &= buffer.getTagCompound() == null ?
						targetStack.getTagCompound() == null :
							buffer.getTagCompound().equals(targetStack.getTagCompound());
				if (!flag) continue;

				// Merge stack
				int floorSize = targetStack.getCount() + buffer.getCount() - maxStackSize;
				if (floorSize > 0) {
					targetStack.setCount(maxStackSize);
					targetStack.setAnimationsToGo(5);
					buffer.setCount(floorSize);
				} else {
					targetStack.setCount(floorSize + maxStackSize);
					targetStack.setAnimationsToGo(5);
					buffer.setCount(0);
					break;
				}
			}
		}

		if (buffer.getCount() > 0 && empty >= 0) {
			setInventorySlotContents(empty, buffer.copy());
			buffer.setCount(0);
		}

		if (buffer.getCount() < originalStackSize) {
			markDirty();
			return buffer.getCount() <= 0;
		}

		return false;
	}

	public void setInventoryCurrentSlotContents(ItemStack itemstack) {
		if (currentItem > -1) {
			setInventorySlotContents(currentItem, itemstack);
		}
	}

	public int getInventorySlotContainItem(Item item) {
		// 指定されたアイテムIDの物を持っていれば返す
		for (int j = 0; j < getSizeInventory(); j++) {
			if (!getStackInSlot(j).isEmpty() && getStackInSlot(j).getItem() == item) {
				return j;
			}
		}

		return -1;
	}

	public int getInventorySlotContainItem(Class<? extends Item> itemClass) {
		// 指定されたアイテムクラスの物を持っていれば返す
		for (int j = 0; j < getSizeInventory(); j++) {
			// if (mainInventory.get(j) != null &&
			// mainInventory.get(j).getItem().getClass().isAssignableFrom(itemClass))
			// {
			if (!getStackInSlot(j).isEmpty()
					&& itemClass.isAssignableFrom(getStackInSlot(j).getItem().getClass())) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItemAndDamage(Item item, int damege) {
		if (damege == OreDictionary.WILDCARD_VALUE) {
			return getInventorySlotContainItem(item);
		}

		for (int i = 0; i < getSizeInventory(); i++) {
			if (!getStackInSlot(i).isEmpty() && getStackInSlot(i).getItem() == item
					&& getStackInSlot(i).getItemDamage() == damege) {
				return i;
			}
		}

		return -1;
	}

	protected ItemStack getInventorySlotContainItemStack(Item item) {
		// いらんかも？
		int j = getInventorySlotContainItem(item);
		return j > -1 ? mainInventory.get(j) : ItemStack.EMPTY;
	}

	protected ItemStack getInventorySlotContainItemStackAndDamege(Item item, int damege) {
		// いらんかも？
		int j = getInventorySlotContainItemAndDamage(item, damege);
		return j > -1 ? mainInventory.get(j) : ItemStack.EMPTY;
	}

	public int getInventorySlotContainItemFood() {
		// インベントリの最初の食料を返す
		if (ItemUtil.getFoodAmount(mainHandInventory.get(0)) > 0) {
			return getSizeInventory() - 2;
		}
		if (ItemUtil.getFoodAmount(offHandInventory.get(0)) > 0) {
			return getSizeInventory() - 1;
		}
		for (int j = 0; j < getSizeInventory(); j++) {
			ItemStack mi = getStackInSlot(j);
			if (ItemUtil.getFoodAmount(mi) > 0) {
				return j;
			}
		}
		return -1;
	}
	
	public int getInventorySlotContainItemId(String itemId) {
		// 指定されたアイテムクラスの物を持っていれば返す
		for (int j = 0; j < getSizeInventory(); j++) {
			if (!getStackInSlot(j).isEmpty()
					&& getStackInSlot(j).getItem().getRegistryName().toString().equals(itemId)) {
				return j;
			}
		}
		return -1;
	}

	public int getSmeltingItem() {
		// 調理可能アイテムを返す
		for (int i = 0; i < entityLittleMaid.maidInventory.getSizeInventory(); i++) {
			if (isItemSmelting(i) && i != currentItem) {
				ItemStack mi = mainInventory.get(i);
				if (mi.getMaxDamage() > 0 && mi.getItemDamage() == 0) {
					// 修復レシピ対策
					continue;
				}
				// レシピ対応品
				return i;
			}
		}
		return -1;
	}

	public int getInventorySlotContainItemPotion(boolean flag, int potionID, boolean isUndead) {
		// インベントリの最初のポーションを返す
		// flag = true: 攻撃・デバフ系、 false: 回復・補助系
		// potionID: 要求ポーションのID
		for (int j = 0; j < getSizeInventory(); j++) {
			if (!mainInventory.get(j).isEmpty()
					&& mainInventory.get(j).getItem() instanceof ItemPotion) {
				ItemStack is = mainInventory.get(j);
				List<PotionEffect> list = PotionUtils.getEffectsFromStack(is);
				nextPotion: if (list != null) {
					PotionEffect potioneffect;
					for (Iterator<PotionEffect> iterator = list.iterator(); iterator
							.hasNext();) {
						potioneffect = (PotionEffect) iterator.next();
						if (Potion.getIdFromPotion(potioneffect.getPotion()) == potionID) break;
						if (Potion.getIdFromPotion(potioneffect.getPotion()) == 6) {
							if ((!flag && isUndead) || (flag && !isUndead)) {
								break nextPotion;
							}
						} else if (Potion.getIdFromPotion(potioneffect.getPotion()) == 7) {
							if ((flag && isUndead) || (!flag && !isUndead)) {
								break nextPotion;
							}
						} else if (potioneffect.getPotion().isBadEffect() != flag) {
							break nextPotion;
						}
					}
					return j;
				}
			}
		}
		return -1;
	}

	public int getFirstEmptyStack() {
		for (int i = 0; i < InventoryLittleMaid.maxInventorySize; i++) {
			if (mainInventory.get(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}

	public boolean isItemBurned(int index) {
		// 燃えるアイテムか?
		return index > -1 && ItemHelper.isItemBurned(getStackInSlot(index));
	}

	public boolean isItemSmelting(int index) {
		// 燃えるアイテムか?
		return ItemHelper.isItemSmelting(getStackInSlot(index));
	}

	public boolean isItemExplord(int index) {
		// 爆発物？
		return (index >= 0) && ItemHelper.isItemExplord(getStackInSlot(index));
	}

	// インベントリの転送関連
	public boolean isChanged(int pIndex) {
		// 変化があったかの判定
		ItemStack lis = getStackInSlot(pIndex);
		return !ItemStack.areItemStacksEqual(lis, prevItems.get(pIndex));
		// return (lis == null || prevItems[pIndex] == null) ?
		// (prevItems[pIndex] != lis) : !ItemStack.areItemStacksEqual(lis,
		// prevItems[pIndex]);
		// return prevItems[pIndex] != getStackInSlot(pIndex);
	}

	public void setChanged(int pIndex) {
		if (pIndex >= getSizeInventory()) {
			return;
		}
		prevItems.set(pIndex, new ItemStack(Items.SUGAR));
	}

	public void resetChanged(int pIndex) {
		// 処理済みのチェック
		ItemStack lis = getStackInSlot(pIndex);
		prevItems.set(pIndex, (lis.isEmpty() ? ItemStack.EMPTY : lis.copy()));
	}

	public void clearChanged() {
		// 強制リロード用、ダミーを登録して強制的に一周させる
		ItemStack lis = new ItemStack(Items.SUGAR);
		for (int li = 0; li < prevItems.size(); li++) {
			prevItems.set(li, lis);
		}
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString("MaidInventory");
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		ItemStack pItemStack = ItemStack.EMPTY;
		if (index == handInventoryOffset + 1) {
			pItemStack = offHandInventory.get(0);
		} else if (index == handInventoryOffset) {
			pItemStack = mainHandInventory.get(0);
		} else if (index >= maxInventorySize && index < handInventoryOffset) {
			pItemStack = armorInventory.get(index-maxInventorySize);
		} else if (index < maxInventorySize) {
			pItemStack = mainInventory.get(index);
		}

		if (!pItemStack.isEmpty() && pItemStack.getCount() <= 0) {
			setInventorySlotContents(index, ItemStack.EMPTY);
			pItemStack = ItemStack.EMPTY;
		}

		return pItemStack;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack target, returned = ItemStack.EMPTY;
		if (!(target = getStackInSlot(index)).isEmpty()) {
			returned = target.splitStack(count);
			if (target.getCount() == 0) {
				setInventorySlotContents(index, ItemStack.EMPTY);
			}
			return returned;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		markDirty();
		ItemStack aStack;
		if (index >= handInventoryOffset + 1) {
			aStack = offHandInventory.get(index - (handInventoryOffset + 1)).copy();
			offHandInventory.set(index - (handInventoryOffset + 1), ItemStack.EMPTY);
		}
		if (index >= handInventoryOffset) {
			aStack = mainHandInventory.get(index - handInventoryOffset).copy();
			mainHandInventory.set(index - handInventoryOffset, ItemStack.EMPTY);
		}
		if (index >= maxInventorySize) {
			aStack = armorInventory.get(index - maxInventorySize).copy();
			armorInventory.set(index - maxInventorySize, ItemStack.EMPTY);
		} else {
			aStack = mainInventory.get(index).copy();
			mainInventory.set(index, ItemStack.EMPTY);
		}
		return aStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (isItemValidForSlot(index, stack)) {
			if (index >= handInventoryOffset + 1) {
				offHandInventory.set(index - (handInventoryOffset + 1), stack);
			} else if (index >= handInventoryOffset) {
				mainHandInventory.set(index - handInventoryOffset, stack);
			} else if (index >= maxInventorySize) {
				armorInventory.set(index - maxInventorySize, stack);
			} else {
				mainInventory.set(index, stack);
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (!stack.isEmpty() && index >= maxInventorySize && index < handInventoryOffset) {
			int armorSlotIndex = index - maxInventorySize;
			for (EntityEquipmentSlot slot: EntityEquipmentSlot.values()) {
				if (slot.getSlotType()==EntityEquipmentSlot.Type.ARMOR && slot.getIndex() == armorSlotIndex) {
					if (stack.getItem().isValidArmor(stack, slot, entityLittleMaid)) {
						return true;
					}
				}
			}
		} else if (index >= 0 && index < getSizeInventory()) {
			return true;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		for (int i=0; i<getSizeInventory(); i++) {
			setInventorySlotContents(i, ItemStack.EMPTY);
		}
	}

}
