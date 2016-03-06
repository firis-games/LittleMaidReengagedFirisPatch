package littleMaidMobX;

import java.util.Iterator;
import java.util.List;

import mmmlibx.lib.MMM_Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;

public class LMM_InventoryLittleMaid extends InventoryPlayer {

	/**
	 * 最大インベントリ数
	 */
	public static final int maxInventorySize = 18;
	/**
	 * オーナー
	 */
	public LMM_EntityLittleMaid entityLittleMaid;
	/**
	 * スロット変更チェック用
	 */
	public ItemStack prevItems[];

	public LMM_InventoryLittleMaid(LMM_EntityLittleMaid par1EntityLittleMaid) {
		super(par1EntityLittleMaid.maidAvatar);

		entityLittleMaid = par1EntityLittleMaid;
		mainInventory = new ItemStack[maxInventorySize];
		prevItems = new ItemStack[mainInventory.length + armorInventory.length];
	}

	@Override
	public void readFromNBT(NBTTagList par1nbtTagList) {
		mainInventory = new ItemStack[maxInventorySize];
		armorInventory = new ItemStack[4];

		for (int i = 0; i < par1nbtTagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = par1nbtTagList.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

			if (itemstack == null) {
				continue;
			}

			if (j >= 0 && j < mainInventory.length) {
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
		return mainInventory.length + armorInventory.length;
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
	public void decrementAnimations() {
		for (int li = 0; li < this.mainInventory.length; ++li) {
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

	@Override
	public void damageArmor(float pDamage) {
		// 装備アーマーに対するダメージ
		// 頭部は除外
		//ItemStack lis = armorInventory[3];
		//armorInventory[3] = null;
		super.damageArmor(pDamage);
		//armorInventory[3] = lis;

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

	@Override
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

	@Override
	public ItemStack getCurrentItem() {
		if (currentItem >= 0 && currentItem < mainInventory.length) {
			return mainInventory[currentItem];
		}
		return null;
	}

	@Override
	public boolean addItemStackToInventory(ItemStack par1ItemStack) {
		markDirty();
		return super.addItemStackToInventory(par1ItemStack);
	}

	/**
	 * 頭部の追加アイテムを返す。
	 */
	public ItemStack getHeadMount() {
		return mainInventory[mainInventory.length - 1];
	}

	public void setInventoryCurrentSlotContents(ItemStack itemstack) {
		if (currentItem > -1) {
			setInventorySlotContents(currentItem, itemstack);
		}
	}

	protected int getInventorySlotContainItem(Item item) {
		// 指定されたアイテムIDの物を持っていれば返す
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].getItem() == item) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItem(Class itemClass) {
		// 指定されたアイテムクラスの物を持っていれば返す
		for (int j = 0; j < mainInventory.length; j++) {
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
		for (int i = 0; i < mainInventory.length; i++) {
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
		for (int j = 0; j < mainInventory.length; j++) {
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
		for (int i = 0; i < mainInventory.length; i++) {
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
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null
					&& mainInventory[j].getItem() instanceof ItemPotion) {
				ItemStack is = mainInventory[j];
				List list = ((ItemPotion) is.getItem()).getEffects(is);
				nextPotion: if (list != null) {
					PotionEffect potioneffect;
					for (Iterator iterator = list.iterator(); iterator
							.hasNext();) {
						potioneffect = (PotionEffect) iterator.next();
						if (potioneffect.getPotionID() == potionID) break;
						if (potioneffect.getPotionID() == Potion.heal.id) {
							if ((!flag && isUndead) || (flag && !isUndead)) {
								break nextPotion;
							}
						} else if (potioneffect.getPotionID() == Potion.harm.id) {
							if ((flag && isUndead) || (!flag && !isUndead)) {
								break nextPotion;
							}
						} else if (Potion.potionTypes[potioneffect.getPotionID()].isBadEffect() != flag) {
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
		for (int i = 0; i < mainInventory.length; i++) {
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
		return (pItemstack != null && MMM_Helper.getSmeltingResult(pItemstack) != null);
	}

	public boolean isItemExplord(int index) {
		// 爆発物？
		return (index >= 0) && isItemExplord(getStackInSlot(index));
	}

	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack == null)
			return false;
		Item li = pItemstack.getItem();
		return (pItemstack != null && li instanceof ItemBlock && Block.getBlockFromItem(li).getMaterial() == Material.tnt);
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
}
