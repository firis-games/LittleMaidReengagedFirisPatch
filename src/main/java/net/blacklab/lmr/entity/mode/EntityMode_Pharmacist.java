package net.blacklab.lmr.entity.mode;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMR;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.SwingStatus;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class EntityMode_Pharmacist extends EntityModeBlockBase {

	public static final int mmode_Pharmacist = 0x0022;

	protected int inventryPos;


	public EntityMode_Pharmacist(EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 6100;
	}

	@Override
	public void init() {
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Pharmacist", "Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.T-Pharmacist", "T-Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.F-Pharmacist", "F-Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.F-Pharmacist", "D-Pharmacist");
		*/
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Pharmacist:0x0022
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;

		owner.addMaidMode(ltasks, "Pharmacist", mmode_Pharmacist);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(litemstack) && owner.maidInventory.getInventorySlotContainItem(Items.blaze_powder) > 0) {
				owner.setMaidMode("Pharmacist");
				if (AchievementsLMR.ac_Pharmacist != null) {
					pentityplayer.addStat(AchievementsLMR.ac_Pharmacist);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Pharmacist :
			owner.setBloodsuck(false);
			owner.aiJumpTo.setEnable(false);
			owner.aiFollow.setEnable(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			inventryPos = 0;
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Pharmacist :
			litemstack = owner.getCurrentEquippedItem();
			if (!(inventryPos > 0 && litemstack != null && !PotionUtils.getEffectsFromStack(litemstack).isEmpty())) {
				for (li = 0; li < InventoryLittleMaid.maxInventorySize; li++) {
					litemstack = owner.maidInventory.getStackInSlot(li);
					if (litemstack != null) {
						// 対象は水ポーション
						if (litemstack.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(litemstack)) {
							return li;
						}
					}
				}
			}
			break;
		}

		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return false;
	}

	@Override
	public boolean isSearchBlock() {
		if (!super.isSearchBlock()) return false;

		if (owner.getCurrentEquippedItem() != null) {
			fDistance = Double.MAX_VALUE;
			owner.clearTilePos();
			owner.setSneaking(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldBlock(int pMode) {
		// 実行中判定
		return owner.maidTileEntity instanceof TileEntityBrewingStand &&
				(((TileEntityBrewingStand)owner.maidTileEntity).getField(0) > 0 ||
						(owner.getCurrentEquippedItem() != null) || inventryPos > 0);
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		if (owner.getCurrentEquippedItem() == null) {
			return false;
		}
		TileEntity ltile = owner.worldObj.getTileEntity(new BlockPos(px, py, pz));
		if (!(ltile instanceof TileEntityBrewingStand)) {
			return false;
		}

		// 世界のメイドから
		checkWorldMaid(ltile);
		// 使用していた蒸留器ならそこで終了
		if (owner.isUsingTile(ltile)) return true;

		double ldis = owner.getDistanceTilePosSq(ltile);
		if (fDistance > ldis) {
			owner.setTilePos(ltile);
			fDistance = ldis;
		}

		return false;
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		TileEntityBrewingStand ltile = (TileEntityBrewingStand)owner.maidTileEntity;
		if (owner.worldObj.getTileEntity(new BlockPos(px, py, pz)) != ltile) {
			return false;
		}

		ItemStack litemstack1;
		boolean lflag = false;
		SwingStatus lswing = owner.getSwingStatusDominant();

		// 蒸留待機
//    	isMaidChaseWait = true;
		if (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null || ltile.getStackInSlot(3) != null || !lswing.canAttack()) {
			// お仕事中
			owner.setWorking(true);
		}

		int blaze_position = owner.maidInventory.getInventorySlotContainItem(Items.blaze_powder);
		if (lswing.canAttack()) {
			// Get fuel value
			// Blaze Power slot in stand
			ItemStack blazeStackB = ltile.getStackInSlot(4);
			if (ltile.getField(1) <= 0 && blazeStackB == null) {
				if (blaze_position >= 0) {
					ItemStack blazeStackM = owner.maidInventory.getStackInSlot(blaze_position);
					ltile.setInventorySlotContents(4, new ItemStack(Items.blaze_powder));
					blazeStackM.stackSize--;
					if (blazeStackM.stackSize <= 0) {
						blazeStackM = null;
					}
					lflag = true;
				} else {
					return false;
				}
			}

			ItemStack lIngredientStack = ltile.getStackInSlot(3);

			if (lIngredientStack != null && lIngredientStack.stackSize > 0 && ltile.getField(0) <= 0) {
				// 蒸留不能なので回収
				LittleMaidReengaged.Debug("Impossible brewing.");
				if (py <= owner.posY) {
					owner.setSneaking(true);
				}
				lflag = true;
				if (owner.maidInventory.addItemStackToInventory(lIngredientStack)) {
					ltile.setInventorySlotContents(3, null);
					owner.playSound("entity.item.pickup");
					owner.setSwing(5, EnumSound.Null, false);
				}
			}
			// 完成品
			if (!lflag && inventryPos > InventoryLittleMaid.maxInventorySize) {
				// ポーションの回収
				LittleMaidReengaged.Debug("Complete.");
				for (int li = 0; li < 3 && !lflag; li ++) {
					litemstack1 = ltile.getStackInSlot(li);
					if (litemstack1 != null && owner.maidInventory.addItemStackToInventory(litemstack1)) {
						ltile.setInventorySlotContents(li, null);
						owner.playSound("entity.item.pickup");
						owner.setSwing(5, EnumSound.cookingOver, false);
						owner.addMaidExperience(0.25f);
						lflag = true;
					}
				}
				if (!lflag) {
					inventryPos = 0;
					owner.getNextEquipItem();
					lflag = true;
				}
			}

			litemstack1 = owner.maidInventory.getCurrentItem();
			if (!lflag && (litemstack1 != null && litemstack1.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(litemstack1))) {
				// 水瓶をげっとれでぃ
				LittleMaidReengaged.Debug("Ready bottle.");
				int li = 0;
				for (li = 0; li < 3 && !lflag; li++) {
					if (ltile.getStackInSlot(li) == null) {
						ltile.setInventorySlotContents(li, litemstack1);
						owner.maidInventory.setInventoryCurrentSlotContents(null);
						owner.playSound("entity.item.pickup");
						owner.setSwing(5, EnumSound.cookingStart, false);
						owner.getNextEquipItem();
						owner.addMaidExperience(0.25f);
						lflag = true;
					}
				}
			}
			if (!lflag && (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null)
					&& (owner.maidInventory.currentItem == -1 || (litemstack1 != null && litemstack1.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(litemstack1)))) {
				// ポーション以外を検索
				LittleMaidReengaged.Debug("Search stuff.");
//				for (inventryPos = 0; inventryPos < owner.maidInventory.InventoryLittleMaid.maxInventorySize; inventryPos++) {
				for (; inventryPos < InventoryLittleMaid.maxInventorySize; inventryPos++) {
					litemstack1 = owner.maidInventory.getStackInSlot(inventryPos);
					if (litemstack1 != null && !(litemstack1.getItem() instanceof ItemPotion)) {
						LittleMaidReengaged.Debug("Select item %s", litemstack1.getItem().getRegistryName());
						owner.setEquipItem(inventryPos);
						lflag = true;
						break;
					}
				}
			}

			if (!lflag && lIngredientStack == null && (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null)) {
				// 手持ちのアイテムをぽーい
				if (litemstack1 != null && !(litemstack1.getItem() instanceof ItemPotion) && BrewingRecipeRegistry.isValidIngredient(litemstack1)) {
					LittleMaidReengaged.Debug("Set stuff %s", litemstack1.getItem().getRegistryName());
					ltile.setInventorySlotContents(3, litemstack1);
					owner.maidInventory.setInventorySlotContents(inventryPos, null);
					owner.playSound("entity.item.pickup");
					owner.setSwing(15, EnumSound.Null, false);
					owner.addMaidExperience(3.5f);
					lflag = true;
				}
				else if (litemstack1 == null || (litemstack1.getItem() instanceof ItemPotion && CommonHelper.hasEffect(litemstack1)) || !PotionUtils.getEffectsFromStack(litemstack1).isEmpty()) {
					// 対象外アイテムを発見した時に終了
					inventryPos = InventoryLittleMaid.maxInventorySize;
					lflag = true;
				}
				inventryPos++;
//				owner.maidInventory.currentItem = maidSearchCount;
				owner.setEquipItem(inventryPos);
			}


			// 終了状態のキャンセル
			if (owner.getSwingStatusDominant().index == -1 && lIngredientStack == null) {
				owner.getNextEquipItem();
			}
		} else {
			lflag = true;
		}
		if (ltile.getField(0) > 0 || inventryPos > 0) {
			owner.setWorking(true);
			lflag = true;
		}
		return lflag;
	}

	@Override
	public void startBlock(int pMode) {
		inventryPos = 0;
	}

	@Override
	public void resetBlock(int pMode) {
		owner.setSneaking(false);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		inventryPos = par1nbtTagCompound.getInteger("InventryPos");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		par1nbtTagCompound.setInteger("InventryPos", inventryPos);
	}

}
