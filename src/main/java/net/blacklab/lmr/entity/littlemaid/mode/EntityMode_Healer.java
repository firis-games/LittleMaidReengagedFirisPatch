package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.Iterator;
import java.util.List;

import firis.lmlib.api.constant.EnumSound;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

/**
 * ヒーラーメイド
 *
 */
public class EntityMode_Healer extends EntityModeBase {

	public static final String mmode_Healer		= "Healer";


	public EntityMode_Healer(EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 3300;
	}

	@Override
	public void init() {
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Healer:0x0082
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

		// 索敵系
		ltasks[1].addTask(1, new EntityAIHurtByTarget(owner, true));
		owner.addMaidMode(mmode_Healer, ltasks);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mmode_Healer, litemstack)) {
				owner.setMaidMode(mmode_Healer);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.Healer);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mmode_Healer :
			owner.setBloodsuck(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(String pMode) {
		if (isTriggerItem(pMode, owner.getHandSlotForModeChange())) {
			return InventoryLittleMaid.handInventoryOffset;
		}

		switch (pMode) {
		case mmode_Healer:
			// Healer
			for (int i = 0; i < owner.maidInventory.getSizeInventory(); i++) {
				ItemStack is = owner.maidInventory.getStackInSlot(i);
				if (is.isEmpty()) continue;
				// 対象は食料かポーション
				if (isTriggerItem(pMode, is)) {
					return i;
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
		return par1ItemStack.getItem() instanceof ItemFood || (par1ItemStack.getItem() instanceof ItemPotion && CommonHelper.hasEffect(par1ItemStack));
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return pItemStack.getItem() instanceof ItemFood || pItemStack.getItem() instanceof ItemPotion;
	}

	@Override
	public void onUpdate(String pMode) {
		// TODO 自動生成されたメソッド・スタブ
		if(owner.isMaidWait()) return;
		super.onUpdate(pMode);
		updateAITick(pMode);
	}

	@Override
	public void updateAITick(String pMode) {
		if (pMode.equals(mmode_Healer)) {
			// 近接した主に食物を突っ込む
			if (owner.getSwingStatusDominant().canAttack()) {
				// 主の回復
				if (owner.isContractEX() && owner.getDistanceSqToMaster() < 16D
						&& owner.getMaidMasterEntity() != null && owner.getMaidMasterEntity().isEntityAlive()
						&& owner.getMaidMasterEntity() != null
						&& owner.canEntityBeSeen(owner.getMaidMasterEntity())) {
					EntityPlayer lmaster = owner.getMaidMasterEntity();
					int h = lmaster.getFoodStats().getFoodLevel();

					while (owner.isMaskedMaid()) {
						// 主の状態に合わせてアイテムを選択
						if (lmaster.getHealth() < 9F) {
							// HPが減っているときはポーションを使う
							int j = owner.maidInventory.getInventorySlotContainItemPotion(false, Potion.getIdFromPotion(Potion.getPotionFromResourceLocation("instant_health")), lmaster.isEntityUndead());
							if (j > -1) {
								owner.setEquipItem(j);
								break;
							}
						}
						if (h < 18) {
							// 自然回復できない腹具合なら食料
							int j = owner.maidInventory.getInventorySlotContainItemFood();
							if (j > -1) {
								owner.setEquipItem(j);
								break;
							}
						}
						break;
					}

					ItemStack itemstack1 = owner.getCurrentEquippedItem();
					if (!itemstack1.isEmpty()) {
						if (itemstack1.getItem() instanceof ItemFood) {
							// 食料を突っ込む
							if (h < 18) {
								owner.setSwing(10, EnumSound.healing, true);
								itemstack1 = ((ItemFood)itemstack1.getItem()).onItemUseFinish(itemstack1, owner.getEntityWorld(), lmaster);
//	                        	owner.getEntityWorld().playSoundAtEntity(lmaster, lmaster.getHurtSound(), 0.5F, (owner.rand.nextFloat() - owner.rand.nextFloat()) * 0.2F + 1.0F);
								if (itemstack1.getCount() <= 0) {
									itemstack1 = ItemStack.EMPTY;
								}
								owner.maidInventory.setInventoryCurrentSlotContents(itemstack1);
								owner.getNextEquipItem();
								owner.addMaidExperience(2.4f);
							}
						}
						else if (itemstack1.getItem() instanceof ItemPotion) {
							boolean lswing = true;
							// ポーションの効果が重複しないように使う
							List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack1);
							if (list != null) {
								PotionEffect potioneffect;
								for(Iterator<PotionEffect> iterator = list.iterator(); iterator.hasNext();) {
									potioneffect = (PotionEffect)iterator.next();
									if (potioneffect.getPotion().equals(Potion.getPotionFromResourceLocation("instant_health"))) {
										if ((6 << potioneffect.getAmplifier()) <= (lmaster.getMaxHealth() - lmaster.getHealth())) {
//	                                    	mod_littleMaidMob.Debug(String.format("%d <= %d", (6 << potioneffect.getAmplifier()), (masterEntity.func_40117_c() - masterEntity.health)));
											lswing = true;
										} else {
											lswing = false;
										}
										break;
									}
									if (potioneffect.getPotion().isBadEffect()
											|| lmaster.isPotionActive(potioneffect.getPotion())) {
										lswing = false;
										break;
									}
								}
							}

							if (lswing) {
								owner.setSwing(10, EnumSound.healing_potion, true);
								owner.usePotionTotarget(lmaster);
//	                        	owner.getEntityWorld().playSoundAtEntity(lmaster, lmaster.getHurtSound(), 0.5F, (owner.rand.nextFloat() - owner.rand.nextFloat()) * 0.2F + 1.0F);
								owner.getNextEquipItem();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public double getDistanceSqToStartFollow() {
		return 3 * 3;
	}

	@Override
	public double getLimitRangeSqOnFollow() {
		return 12 * 12;
	}

}
