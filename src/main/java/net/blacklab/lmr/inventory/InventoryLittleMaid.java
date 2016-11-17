package net.blacklab.lmr.inventory;

import java.util.Iterator;
import java.util.List;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryLittleMaid extends InventoryPlayer {

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
	public final ItemStack mainHandInventory[] = new ItemStack[1];

	/**
	 * オーナー
	 */
	public EntityLittleMaid entityLittleMaid;

	/**
	 * スロット変更チェック用
	 */
	public ItemStack prevItems[];

	public InventoryLittleMaid(EntityLittleMaid par1EntityLittleMaid) {
		super(par1EntityLittleMaid.maidAvatar);

		entityLittleMaid = par1EntityLittleMaid;
//		player = entityLittleMaid.maidAvatar;
		// TODO InventoryPlayer.mainInventory became 'final'. S**t
//		mainInventory = new ItemStack[maxInventorySize];
		prevItems = new ItemStack[getSizeInventory()];
	}

	@Override
	public void readFromNBT(NBTTagList par1nbtTagList) {
		for (int i = 0; i < par1nbtTagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = par1nbtTagList.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

			if (itemstack == null) {
				continue;
			}

			if (j >= 0 && j < InventoryLittleMaid.maxInventorySize) {
				mainInventory[j] = itemstack;
			}

			if (j >= 100 && j < armorInventory.length + 100) {
				armorInventory[j - 100] = itemstack;
			}

			if (j >= 150 && j < armorInventory.length + 150) {
				offHandInventory[j - 150] = itemstack;
			}

			if (j >= 200 && j < mainHandInventory.length + 200) {
				mainHandInventory[j - 200] = itemstack;
			}
		}
	}

	@Override
	public NBTTagList writeToNBT(NBTTagList nbtTagListIn) {
		NBTTagList result = super.writeToNBT(nbtTagListIn);

		for (int k = 0; k < mainHandInventory.length; ++k) {
			if (this.mainHandInventory[k] != null) {
				NBTTagCompound nbttagcompound2 = new NBTTagCompound();
				nbttagcompound2.setByte("Slot", (byte)(k + 200));
				mainHandInventory[k].writeToNBT(nbttagcompound2);
				result.appendTag(nbttagcompound2);
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return "InsideSkirt";
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
			if (getStackInSlot(li) != null) {
				try {
					getStackInSlot(li).updateAnimation(this.player.worldObj,
							entityLittleMaid, li, this.currentItem == li);
				} catch (ClassCastException e) {
					getStackInSlot(li).updateAnimation(this.player.worldObj,
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

		for (int i = 0; i < armorInventory.length; ++i) {
			if (armorInventory[i] != null && armorInventory[i].getItem() instanceof ItemArmor) {
				armorInventory[i].damageItem((int)pDamage, player);

				if (armorInventory[i].stackSize == 0) {
					armorInventory[i] = null;
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
			lexp = new Explosion(entityLittleMaid.worldObj, entityLittleMaid,
					entityLittleMaid.posX, entityLittleMaid.posY, entityLittleMaid.posZ, 3F, false, entityLittleMaid.worldObj.getGameRules().getBoolean("mobGriefing"));
		}

//		armorInventory[3] = null;
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack it = getStackInSlot(i);
			if (it != null) {
				if (detonator && isItemExplord(i)) {
					Item j = it.getItem();
					for (int l = 0; l < it.stackSize; l++) {
						// 爆薬ぶちまけ
						((BlockTNT)Block.getBlockFromItem(j)).onBlockDestroyedByExplosion(
								entityLittleMaid.worldObj,
								new BlockPos(
									MathHelper.floor_double(entityLittleMaid.posX)
									+ entityLittleMaid.getRNG().nextInt(7) - 3,
									MathHelper.floor_double(entityLittleMaid.posY)
									+ entityLittleMaid.getRNG().nextInt(7) - 3,
									MathHelper.floor_double(entityLittleMaid.posZ)
									+ entityLittleMaid.getRNG().nextInt(7) - 3
								), lexp);
					}
				} else {
					entityLittleMaid.entityDropItem(it, 0F);
				}
			}
			setInventorySlotContents(i, null);
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
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (entityLittleMaid.isDead) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(entityLittleMaid) <= 64D;
	}

	public ItemStack getCurrentItem() {
		if (currentItem >= handInventoryOffset + 1) {
			return offHandInventory[currentItem - (handInventoryOffset + 1)];
		}
		if (currentItem >= handInventoryOffset) {
			return mainHandInventory[currentItem - handInventoryOffset];
		}
		if (currentItem >= 0 && currentItem < InventoryLittleMaid.maxInventorySize) {
			return mainInventory[currentItem];
		}
		return null;
	}

	@Override
	public boolean addItemStackToInventory(final ItemStack par1ItemStack) {
		if (par1ItemStack == null) {
			return false;
		}

		int empty = getFirstEmptyStack();

		// Picking up items
		ItemStack buffer = par1ItemStack;
		int originalStackSize = buffer.stackSize;

		// Can be merged to dedicated slots
		for (int i=0; i < getSizeInventory(); i++) {
			ItemStack targetStack = getStackInSlot(i);

			if (targetStack != null && targetStack.getItem() == buffer.getItem()) {
				int maxStackSize = targetStack.getItem().getItemStackLimit(targetStack);
				if (targetStack.stackSize == maxStackSize) continue;

				// Check item damage and NBT
				boolean flag = true;
				flag &= targetStack.getItemDamage() == buffer.getItemDamage();
				flag &= buffer.getTagCompound() == null ?
						targetStack.getTagCompound() == null :
							buffer.getTagCompound().equals(targetStack.getTagCompound());
				if (!flag) continue;

				// Merge stack
				int floorSize = targetStack.stackSize + buffer.stackSize - maxStackSize;
				if (floorSize > 0) {
					targetStack.stackSize = maxStackSize;
					targetStack.animationsToGo = 5;
					buffer.stackSize = floorSize;
				} else {
					targetStack.stackSize = floorSize + maxStackSize;
					targetStack.animationsToGo = 5;
					buffer.stackSize = 0;
					break;
				}
			}
		}

		if (buffer.stackSize > 0 && empty >= 0) {
			setInventorySlotContents(empty, ItemStack.copyItemStack(buffer));
			buffer.stackSize = 0;
		}

		if (buffer.stackSize < originalStackSize) {
			markDirty();
			return buffer.stackSize <= 0;
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
			if (getStackInSlot(j) != null && getStackInSlot(j).getItem() == item) {
				return j;
			}
		}

		return -1;
	}

	public int getInventorySlotContainItem(Class<? extends Item> itemClass) {
		// 指定されたアイテムクラスの物を持っていれば返す
		for (int j = 0; j < getSizeInventory(); j++) {
			// if (mainInventory[j] != null &&
			// mainInventory[j].getItem().getClass().isAssignableFrom(itemClass))
			// {
			if (getStackInSlot(j) != null
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
			if (getStackInSlot(i) != null && getStackInSlot(i).getItem() == item
					&& getStackInSlot(i).getItemDamage() == damege) {
				return i;
			}
		}

		return -1;
	}

	protected ItemStack getInventorySlotContainItemStack(Item item) {
		// いらんかも？
		int j = getInventorySlotContainItem(item);
		return j > -1 ? mainInventory[j] : null;
	}

	protected ItemStack getInventorySlotContainItemStackAndDamege(Item item, int damege) {
		// いらんかも？
		int j = getInventorySlotContainItemAndDamage(item, damege);
		return j > -1 ? mainInventory[j] : null;
	}

	public int getInventorySlotContainItemFood() {
		// インベントリの最初の食料を返す
		for (int j = 0; j < getSizeInventory(); j++) {
			ItemStack mi = getStackInSlot(j);
			if (mi != null && mi.getItem() instanceof ItemFood) {
				if (((ItemFood) mi.getItem()).getHealAmount(mi) > 0) {
					return j;
				}
			}
		}
		return -1;
	}

	public int getSmeltingItem() {
		// 調理可能アイテムを返す
		for (int i = 0; i < entityLittleMaid.maidInventory.getSizeInventory(); i++) {
			if (isItemSmelting(i) && i != currentItem) {
				ItemStack mi = mainInventory[i];
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
			if (mainInventory[j] != null
					&& mainInventory[j].getItem() instanceof ItemPotion) {
				ItemStack is = mainInventory[j];
				List list = PotionUtils.getEffectsFromStack(is);
				nextPotion: if (list != null) {
					PotionEffect potioneffect;
					for (Iterator iterator = list.iterator(); iterator
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
			if (mainInventory[i] == null) {
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
		return !ItemStack.areItemStacksEqual(lis, prevItems[pIndex]);
		// return (lis == null || prevItems[pIndex] == null) ?
		// (prevItems[pIndex] != lis) : !ItemStack.areItemStacksEqual(lis,
		// prevItems[pIndex]);
		// return prevItems[pIndex] != getStackInSlot(pIndex);
	}

	public void setChanged(int pIndex) {
		if (pIndex >= getSizeInventory()) {
			return;
		}
		prevItems[pIndex] = new ItemStack(Items.SUGAR);
	}

	public void resetChanged(int pIndex) {
		// 処理済みのチェック
		ItemStack lis = getStackInSlot(pIndex);
		prevItems[pIndex] = (lis == null ? null : lis.copy());
	}

	public void clearChanged() {
		// 強制リロード用、ダミーを登録して強制的に一周させる
		ItemStack lis = new ItemStack(Items.SUGAR);
		for (int li = 0; li < prevItems.length; li++) {
			prevItems[li] = lis;
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
		ItemStack pItemStack;
		if (index >= handInventoryOffset + 1) {
			pItemStack = offHandInventory[index - (handInventoryOffset + 1)];
		} else if (index >= handInventoryOffset) {
			pItemStack = mainHandInventory[index - handInventoryOffset];
		} else if (index >= maxInventorySize) {
			pItemStack = armorInventory[index-maxInventorySize];
		} else {
			pItemStack = mainInventory[index];
		}

		if (pItemStack != null && pItemStack.stackSize <= 0) {
			setInventorySlotContents(index, null);
			pItemStack = null;
		}

		return pItemStack;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack target, returned = null;
		if ((target = getStackInSlot(index)) != null) {
			returned = target.splitStack(count);
			if (target.stackSize == 0) {
				setInventorySlotContents(index, null);
			}
			return returned;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		markDirty();
		ItemStack aStack;
		if (index >= handInventoryOffset + 1) {
			aStack = ItemStack.copyItemStack(offHandInventory[index - (handInventoryOffset + 1)]);
			offHandInventory[index - (handInventoryOffset + 1)] = null;
		}
		if (index >= handInventoryOffset) {
			aStack = ItemStack.copyItemStack(mainHandInventory[index - handInventoryOffset]);
			mainHandInventory[index - handInventoryOffset] = null;
		}
		if (index >= maxInventorySize) {
			aStack = ItemStack.copyItemStack(armorInventory[index - maxInventorySize]);
			armorInventory[index - maxInventorySize] = null;
		} else {
			aStack = ItemStack.copyItemStack(mainInventory[index]);
			mainInventory[index] = null;
		}
		return aStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (isItemValidForSlot(index, stack)) {
			if (index >= handInventoryOffset + 1) {
				offHandInventory[index - (handInventoryOffset + 1)] = stack;
			} else if (index >= handInventoryOffset) {
				mainHandInventory[index - handInventoryOffset] = stack;
			} else if (index >= maxInventorySize) {
				armorInventory[index - maxInventorySize] = stack;
			} else {
				mainInventory[index] = stack;
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack != null && index >= maxInventorySize && index < handInventoryOffset) {
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
			setInventorySlotContents(i, null);
		}
	}

}
