package net.blacklab.lmr.entity.littlemaid.mode;

import firis.lmlib.api.constant.EnumSound;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
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

/**
 * ポーション調合メイド
 *
 */
public class EntityMode_Pharmacist extends EntityModeBlockBase {

	public static final String mmode_Pharmacist = "Pharmacist";

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
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Pharmacist:0x0022
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;

		owner.addMaidMode(mmode_Pharmacist, ltasks);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mmode_Pharmacist, litemstack) && owner.maidInventory.getInventorySlotContainItem(Items.BLAZE_POWDER) > 0) {
				owner.setMaidMode(mmode_Pharmacist);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.Pharmacist);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mmode_Pharmacist :
			owner.setBloodsuck(false);
//			owner.aiJumpTo.setEnable(false);
			owner.aiFollow.setEnable(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			inventryPos = 0;
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(String pMode) {
		int li;
		if ((li = super.getNextEquipItem(pMode)) >= 0) {
			return li;
		}

		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Pharmacist :
			litemstack = owner.getCurrentEquippedItem();
			if (!(inventryPos > 0 && !litemstack.isEmpty() && !PotionUtils.getEffectsFromStack(litemstack).isEmpty())) {
				for (li = 0; li < owner.maidInventory.getSizeInventory(); li++) {
					litemstack = owner.maidInventory.getStackInSlot(li);
					if (!litemstack.isEmpty()) {
						// 対象は水ポーション
						if (isTriggerItem(pMode, litemstack)) {
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
	protected boolean isTriggerItem(String pMode, ItemStack par1ItemStack) {
		if (par1ItemStack.isEmpty()) {
			return false;
		}
		return par1ItemStack.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(par1ItemStack);
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return false;
	}

	@Override
	public boolean isSearchBlock() {
		if (!super.isSearchBlock()) return false;

		if (!owner.getCurrentEquippedItem().isEmpty()) {
			fDistance = Double.MAX_VALUE;
			owner.jobController.clearTilePos();
			owner.setSneaking(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldBlock(String pMode) {
		// 実行中判定
		return owner.jobController.getTileEntity() instanceof TileEntityBrewingStand &&
				(((TileEntityBrewingStand)owner.jobController.getTileEntity()).getField(0) > 0 ||
						(!owner.getCurrentEquippedItem().isEmpty()) || inventryPos > 0);
	}

	@Override
	public boolean checkBlock(String pMode, int px, int py, int pz) {
		if (owner.getCurrentEquippedItem().isEmpty()) {
			return false;
		}
		TileEntity ltile = owner.getEntityWorld().getTileEntity(new BlockPos(px, py, pz));
		if (!(ltile instanceof TileEntityBrewingStand)) {
			return false;
		}

		// 世界のメイドから
		checkWorldMaid(ltile);
		// 使用していた蒸留器ならそこで終了
		if (owner.jobController.isUsingTile(ltile)) return true;

		double ldis = this.getDistanceTilePosSq(ltile);
		if (fDistance > ldis) {
			owner.jobController.setTilePos(ltile);
			fDistance = ldis;
		}

		return false;
	}

	@Override
	public boolean executeBlock(String pMode, int px, int py, int pz) {
		TileEntityBrewingStand ltile = (TileEntityBrewingStand)owner.jobController.getTileEntity();
		if (owner.getEntityWorld().getTileEntity(new BlockPos(px, py, pz)) != ltile) {
			return false;
		}

		ItemStack litemstack1;
		boolean lflag = false;
		SwingStatus lswing = owner.getSwingStatusDominant();

		// 蒸留待機
//    	isMaidChaseWait = true;
		if (!ltile.getStackInSlot(0).isEmpty() || !ltile.getStackInSlot(1).isEmpty() || !ltile.getStackInSlot(2).isEmpty() || !ltile.getStackInSlot(3).isEmpty() || !lswing.canAttack()) {
			// お仕事中
//			owner.setWorking(true);
			owner.jobController.setStartWorking();
		}

		int blaze_position = owner.maidInventory.getInventorySlotContainItem(Items.BLAZE_POWDER);
		if (lswing.canAttack()) {
			// Get fuel value
			// Blaze Power slot in stand
			ItemStack blazeStackB = ltile.getStackInSlot(4);
			if (ltile.getField(1) <= 0 && blazeStackB.isEmpty()) {
				if (blaze_position >= 0) {
					ItemStack blazeStackM = owner.maidInventory.getStackInSlot(blaze_position);
					ltile.setInventorySlotContents(4, new ItemStack(Items.BLAZE_POWDER));
					blazeStackM.shrink(1);
					if (blazeStackM.getCount() <= 0) {
						blazeStackM = ItemStack.EMPTY;
					}
					lflag = true;
				} else {
					return false;
				}
			}

			ItemStack lIngredientStack = ltile.getStackInSlot(3);

			if (!lIngredientStack.isEmpty() && lIngredientStack.getCount() > 0 && ltile.getField(0) <= 0) {
				// 蒸留不能なので回収
				LittleMaidReengaged.Debug("Impossible brewing.");
				if (py <= owner.posY) {
					owner.setSneaking(true);
				}
				lflag = true;
				if (owner.maidInventory.addItemStackToInventory(lIngredientStack)) {
					ltile.setInventorySlotContents(3, ItemStack.EMPTY);
					owner.playSound("entity.item.pickup");
					owner.setSwing(5, EnumSound.NULL, false);
				}
			}
			// 完成品
			if (!lflag && inventryPos >= owner.maidInventory.getSizeInventory()) {
				// ポーションの回収
				LittleMaidReengaged.Debug("Complete.");
				for (int li = 0; li < 3 && !lflag; li ++) {
					litemstack1 = ltile.getStackInSlot(li);
					if (!litemstack1.isEmpty() && owner.maidInventory.addItemStackToInventory(litemstack1)) {
						ltile.setInventorySlotContents(li, ItemStack.EMPTY);
						owner.playSound("entity.item.pickup");
						owner.setSwing(5, EnumSound.COOKING_OVER, false);
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
			if (!lflag && (!litemstack1.isEmpty() && litemstack1.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(litemstack1))) {
				// 水瓶をげっとれでぃ
				LittleMaidReengaged.Debug("Ready bottle.");
				int li = 0;
				for (li = 0; li < 3 && !lflag; li++) {
					if (ltile.getStackInSlot(li).isEmpty()) {
						ltile.setInventorySlotContents(li, litemstack1);
						owner.maidInventory.setInventoryCurrentSlotContents(ItemStack.EMPTY);
						owner.playSound("entity.item.pickup");
						owner.setSwing(5, EnumSound.COOKING_START, false);
						owner.getNextEquipItem();
						owner.addMaidExperience(0.25f);
						lflag = true;
					}
				}
			}
			if (!lflag && (!ltile.getStackInSlot(0).isEmpty() || !ltile.getStackInSlot(1).isEmpty() || !ltile.getStackInSlot(2).isEmpty())
					&& (owner.maidInventory.getCurrentItemIndex() == -1 || (!litemstack1.isEmpty() && litemstack1.getItem() instanceof ItemPotion && !CommonHelper.hasEffect(litemstack1)))) {
				// ポーション以外を検索
				LittleMaidReengaged.Debug("Search stuff.");
//				for (inventryPos = 0; inventryPos < owner.maidInventory.InventoryLittleMaid.maxInventorySize; inventryPos++) {
				for (; inventryPos < owner.maidInventory.getSizeInventory(); inventryPos++) {
					litemstack1 = owner.maidInventory.getStackInSlot(inventryPos);
					if (!litemstack1.isEmpty() && !(litemstack1.getItem() instanceof ItemPotion)) {
						LittleMaidReengaged.Debug("Select item %s", litemstack1.getItem().getRegistryName());
						owner.setEquipItem(inventryPos);
						lflag = true;
						break;
					}
				}
			}

			if (!lflag && lIngredientStack.isEmpty() && (!ltile.getStackInSlot(0).isEmpty() || !ltile.getStackInSlot(1).isEmpty() || !ltile.getStackInSlot(2).isEmpty())) {
				// 手持ちのアイテムをぽーい
				if (!litemstack1.isEmpty() && !(litemstack1.getItem() instanceof ItemPotion) && BrewingRecipeRegistry.isValidIngredient(litemstack1)) {
					LittleMaidReengaged.Debug("Set stuff %s", litemstack1.getItem().getRegistryName());
					ltile.setInventorySlotContents(3, litemstack1);
					owner.maidInventory.setInventorySlotContents(inventryPos, ItemStack.EMPTY);
					owner.playSound("entity.item.pickup");
					owner.setSwing(15, EnumSound.NULL, false);
					owner.addMaidExperience(3.5f);
					lflag = true;
				}
				else if (litemstack1.isEmpty() || (litemstack1.getItem() instanceof ItemPotion && CommonHelper.hasEffect(litemstack1)) || !PotionUtils.getEffectsFromStack(litemstack1).isEmpty()) {
					// 対象外アイテムを発見した時に終了
					inventryPos = owner.maidInventory.getSizeInventory();
					lflag = true;
				}
				inventryPos++;
//				owner.maidInventory.currentItem = maidSearchCount;
				owner.setEquipItem(inventryPos);

			}


			// 終了状態のキャンセル
			if (owner.getSwingStatusDominant().index == -1 && lIngredientStack.isEmpty()) {
				owner.getNextEquipItem();
			}
		} else {
			lflag = true;
		}
		if (ltile.getField(0) > 0 || inventryPos > 0) {
//			owner.setWorking(true);
			owner.jobController.setStartWorking();
			lflag = true;
		}
		return lflag;
	}

	@Override
	public void startBlock(String pMode) {
		inventryPos = 0;
	}

	@Override
	public void resetBlock(String pMode) {
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
