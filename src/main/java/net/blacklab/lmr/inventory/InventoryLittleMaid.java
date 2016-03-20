package net.blacklab.lmr.inventory;

import java.util.Iterator;
import java.util.List;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;

public class InventoryLittleMaid extends InventoryPlayer {

	/**
	 * 最大インベントリ数
	 */
	public static final int maxInventorySize = 18;

	/**
	 * Inventory "inside skirt"
	 */
//	public ItemStack mainInventory[] = new ItemStack[maxInventorySize];
	
	/**
	 * Armor Inventory
	 */
//	private ItemStack armorInventory[] = new ItemStack[4];
	
	/**
	 * オーナー
	 */
	public EntityLittleMaid entityLittleMaid;
	
	/**
	 * Owner's Avatar
	 */
	private EntityPlayer player;
	
	/**
	 * Current Item Index
	 */
	private int currentItem;

	/**
	 * スロット変更チェック用
	 */
	public ItemStack prevItems[];

	public InventoryLittleMaid(EntityLittleMaid par1EntityLittleMaid) {
		super(par1EntityLittleMaid.maidAvatar);

		entityLittleMaid = par1EntityLittleMaid;
		// TODO InventoryPlayer.mainInventory became 'final'. S**t 
//		mainInventory = new ItemStack[maxInventorySize];
		prevItems = new ItemStack[maxInventorySize + armorInventory.length];
	}

	public void readFromNBT(NBTTagList par1nbtTagList) {
//		mainInventory = new ItemStack[maxInventorySize];
//		armorInventory = new ItemStack[4];

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
		}
	}

	@Override
	public String getName() {
		return "InsideSkirt";
	}

	@Override
	public int getSizeInventory() {
		// 一応
		return maxInventorySize + armorInventory.length;
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
		for (int li = 0; li < maxInventorySize; ++li) {
			if (this.mainInventory[li] != null) {
				try {
					this.mainInventory[li].updateAnimation(this.player.worldObj,
							entityLittleMaid, li, this.currentItem == li);
				} catch (ClassCastException e) {
					this.mainInventory[li].updateAnimation(this.player.worldObj,
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
	@SuppressWarnings("null")
	public void dropAllItems(boolean detonator) {
		// インベントリをブチマケロ！
		Explosion lexp = null;
		if (detonator) {
			// Mobによる破壊の是非
			lexp = new Explosion(entityLittleMaid.worldObj, entityLittleMaid,
					entityLittleMaid.posX, entityLittleMaid.posY, entityLittleMaid.posZ, 3F, false, entityLittleMaid.worldObj.getGameRules().getBoolean("mobGriefing"));
		}

		armorInventory[3] = null;
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
		if (currentItem >= 0 && currentItem < InventoryLittleMaid.maxInventorySize) {
			return mainInventory[currentItem];
		}
		return null;
	}

	public boolean addItemStackToInventory(ItemStack par1ItemStack) {
		if (par1ItemStack == null) {
			return false;
		}
		markDirty();
		ItemStack bufferStack = par1ItemStack;
		if (bufferStack.isItemDamaged()) {
			int empty = getFirstEmptyStack();
			if (empty >= 0) {
				mainInventory[empty] = ItemStack.copyItemStack(bufferStack);
			}
		} else {
			for (int i=0; i<InventoryLittleMaid.maxInventorySize; i++) {
				if (mainInventory[i] == null) {
					mainInventory[i] = ItemStack.copyItemStack(bufferStack);
					bufferStack = null;
					break;
				}
				if (mainInventory[i].getItem()==bufferStack.getItem() && !mainInventory[i].isItemDamaged()) {
					int maxSize = mainInventory[i].getItem().getItemStackLimit(mainInventory[i]);
					mainInventory[i].stackSize += bufferStack.stackSize;
					if (mainInventory[i].stackSize > maxSize) {
						bufferStack.stackSize = maxSize - mainInventory[i].stackSize;
						mainInventory[i].stackSize = maxSize;
					}
				}
				if (bufferStack.stackSize == 0) {
					bufferStack = null;
					break;
				}
			}
			if (bufferStack != null && bufferStack.stackSize > 0) {
				entityLittleMaid.entityDropItem(bufferStack, 0);
			}
		}
		return true;
	}

	public void setInventoryCurrentSlotContents(ItemStack itemstack) {
		if (currentItem > -1) {
			setInventorySlotContents(currentItem, itemstack);
		}
	}

	public int getInventorySlotContainItem(Item item) {
		// 指定されたアイテムIDの物を持っていれば返す
		for (int j = 0; j < InventoryLittleMaid.maxInventorySize; j++) {
			if (mainInventory[j] != null && mainInventory[j].getItem() == item) {
				return j;
			}
		}

		return -1;
	}

	public int getInventorySlotContainItem(Class<Item> itemClass) {
		// 指定されたアイテムクラスの物を持っていれば返す
		for (int j = 0; j < InventoryLittleMaid.maxInventorySize; j++) {
			// if (mainInventory[j] != null &&
			// mainInventory[j].getItem().getClass().isAssignableFrom(itemClass))
			// {
			if (mainInventory[j] != null
					&& itemClass.isAssignableFrom(mainInventory[j].getItem().getClass())) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItemAndDamage(Item item, int damege) {
		// とダメージ値
		for (int i = 0; i < InventoryLittleMaid.maxInventorySize; i++) {
			if (mainInventory[i] != null && mainInventory[i].getItem() == item
					&& mainInventory[i].getItemDamage() == damege) {
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
		for (int j = 0; j < InventoryLittleMaid.maxInventorySize; j++) {
			ItemStack mi = mainInventory[j];
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
		for (int i = 0; i < InventoryLittleMaid.maxInventorySize; i++) {
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
		for (int j = 0; j < InventoryLittleMaid.maxInventorySize; j++) {
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
		return index > -1 && isItemBurned(getStackInSlot(index));
	}

	public static boolean isItemBurned(ItemStack pItemstack) {
		return (pItemstack != null &&
				TileEntityFurnace.getItemBurnTime(pItemstack) > 0);
	}

	public boolean isItemSmelting(int index) {
		// 燃えるアイテムか?
		return isItemSmelting(getStackInSlot(index));
	}

	public static boolean isItemSmelting(ItemStack pItemstack) {
		return (pItemstack != null && FurnaceRecipes.instance().getSmeltingResult(pItemstack) != null);
	}

	public boolean isItemExplord(int index) {
		// 爆発物？
		return (index >= 0) && isItemExplord(getStackInSlot(index));
	}

	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack == null)
			return false;
		Item li = pItemstack.getItem();
		return (pItemstack != null && li instanceof ItemBlock && Block.getBlockFromItem(li).getMaterial(Block.getBlockFromItem(li).getDefaultState()) == Material.tnt);
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
		prevItems[pIndex] = new ItemStack(Items.sugar);
	}

	public void resetChanged(int pIndex) {
		// 処理済みのチェック
		ItemStack lis = getStackInSlot(pIndex);
		prevItems[pIndex] = (lis == null ? null : lis.copy());
	}

	public void clearChanged() {
		// 強制リロード用、ダミーを登録して強制的に一周させる
		ItemStack lis = new ItemStack(Items.sugar);
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
		if (index >= maxInventorySize) {
			return armorInventory[index-maxInventorySize];
		}
		return mainInventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack returned = null;
		if (mainInventory[index] != null) {
			returned = mainInventory[index].splitStack(count);
			if (mainInventory[index].stackSize == 0) {
				mainInventory[index] = null;
			}
			return returned;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack aStack = ItemStack.copyItemStack(mainInventory[index]);
		mainInventory[index] = null;
		return aStack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (isItemValidForSlot(index, stack)) {
			if (index >= maxInventorySize) {
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
	public void markDirty() {
		// TODO Currently there's no task
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack != null && index >= maxInventorySize && index < getSizeInventory()) {
			int armorSlotIndex = index - maxInventorySize;
			for (EntityEquipmentSlot slot: EntityEquipmentSlot.values()) {
				if (slot.getSlotType()==EntityEquipmentSlot.Type.ARMOR && slot.getIndex()==armorSlotIndex) {
					if (stack != null && stack.getItem().isValidArmor(stack, slot, entityLittleMaid)) {
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
