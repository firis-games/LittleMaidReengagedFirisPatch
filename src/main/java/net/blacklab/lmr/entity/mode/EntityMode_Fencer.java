package net.blacklab.lmr.entity.mode;

import java.util.UUID;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.ai.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.ai.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.Counter;
import net.blacklab.lmr.util.TriggerSelect;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * 独自基準としてモード定数は0x0080は平常、0x00c0は血まみれモードと区別。
 */
public class EntityMode_Fencer extends EntityModeBase {

	public static final int mmode_Fencer		= 0x0080;
	public static final int mmode_Bloodsucker	= 0x00c0;

	// Charging timer
	protected Counter ticksCharge;
	protected static final UUID CHARGING_BOOST_UUID = UUID.nameUUIDFromBytes(LittleMaidReengaged.DOMAIN.concat(":fencer_charge_boost").getBytes());
	protected static final AttributeModifier CHARGING_BOOST_MODIFIER = new AttributeModifier(CHARGING_BOOST_UUID, LittleMaidReengaged.DOMAIN.concat(":fencer_charge_boost"), 0.2d, 0);

	protected static final int CHARGE_COUNTER_MAX_VALUE = 60;

	public EntityMode_Fencer(EntityLittleMaid pEntity) {
		super(pEntity);
		isAnytimeUpdate = true;
		ticksCharge = new Counter(-20*30, CHARGE_COUNTER_MAX_VALUE, -20*30);
	}

	@Override
	public int priority() {
		return 3000;
	}

	@Override
	public void init() {
		// 登録モードの名称追加
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "ja_JP", "護衛剣士");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "F-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "ja_JP", "自由剣士");
		ModLoader.addLocalization("littleMaidMob.mode.T-Fencer", "T-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.D-Fencer", "D-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "ja_JP", "血に飢えた冥土");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "F-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "ja_JP", "通魔冥土");
		ModLoader.addLocalization("littleMaidMob.mode.T-Bloodsucker", "T-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.D-Bloodsucker", "D-Bloodsucker");
		*/
		TriggerSelect.appendTriggerItem(null, "Sword", "");
		TriggerSelect.appendTriggerItem(null, "Axe", "");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Fencer:0x0080
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

//		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
//		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new EntityAILMHurtByTarget(owner, true));
		ltasks[1].addTask(4, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(ltasks, "Fencer", mmode_Fencer);


		// Bloodsucker:0x00c0
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);

		ltasks2[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(ltasks2, "Bloodsucker", mmode_Bloodsucker);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (litemstack != null) {
			if (isTriggerItem(mmode_Fencer, litemstack)) {
				owner.setMaidMode("Fencer");
				if (pentityplayer != null) {
					pentityplayer.addStat(AchievementsLMRE.ac_Fencer);
				}
				if (litemstack.getItem() instanceof ItemSpade && pentityplayer != null) {
					pentityplayer.addStat(AchievementsLMRE.ac_Buster);
				}
				return true;
			} else  if (isTriggerItem(mmode_Bloodsucker, litemstack)) {
				owner.setMaidMode("Bloodsucker");
				if (pentityplayer != null) {
					pentityplayer.addStat(AchievementsLMRE.ac_RandomKiller);
				}
				if (litemstack.getItem() instanceof ItemSpade && pentityplayer != null) {
					pentityplayer.addStat(AchievementsLMRE.ac_Buster);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Fencer :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(false);
			owner.aiAttack.isGuard = true;
			return true;
		case mmode_Bloodsucker :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(true);
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		if (isTriggerItem(pMode, owner.getHandSlotForModeChange())) {
			return InventoryLittleMaid.handInventoryOffset;
		}

		int li;
		int ll = -1;
		double ld = 0;
		double lld;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Fencer :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 剣
				if (isTriggerItem(pMode, litemstack)) {
					return li;
				}

				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = CommonHelper.getAttackVSEntity(litemstack);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		case mmode_Bloodsucker :
			for (li = 0; li < owner.maidInventory.getSizeInventory(); li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 斧
				if (isTriggerItem(pMode, litemstack)) {
					return li;
				}

				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = CommonHelper.getAttackVSEntity(litemstack);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	protected boolean isTriggerItem(int pMode, ItemStack par1ItemStack) {
		if (par1ItemStack == null) {
			return false;
		}
		switch (pMode) {
		case mmode_Fencer:
			return par1ItemStack.getItem() instanceof ItemSword || TriggerSelect.checkTrigger(owner.getMaidMasterUUID(), "Sword", par1ItemStack.getItem());
		case mmode_Bloodsucker:
			return par1ItemStack.getItem() instanceof ItemAxe || TriggerSelect.checkTrigger(owner.getMaidMasterUUID(), "Axe", par1ItemStack.getItem());
		}
		return super.isTriggerItem(pMode, par1ItemStack);
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		// 装備アイテムを回収
		return pItemStack.getItem() instanceof ItemSword || pItemStack.getItem() instanceof ItemAxe;
	}

	@Override
	public boolean isSearchEntity() {
		return true;
	}

	@Override
	public boolean checkEntity(int pMode, Entity pEntity) {
		if (!MaidHelper.isTargetReachable(owner, pEntity, 0)) return false;

		return !owner.getIFF(pEntity);
	}

	@Override
	public void updateAITick(int pMode) {
		super.updateAITick(pMode);
		if (pMode == mmode_Fencer || pMode == mmode_Bloodsucker) {
			// Charge(boost moving speed)
			ticksCharge.onUpdate();
			EntityLivingBase targetEntity = owner.getAttackTarget();
			if (targetEntity != null && !targetEntity.isDead) {
				if (!ticksCharge.isDelay()) {
					// Reset counter
					ticksCharge.setValue(CHARGE_COUNTER_MAX_VALUE);
				}
				if (ticksCharge.isEnable()) {
					// Keep boosting speed
					IAttributeInstance maidAttribute;
					if (!(maidAttribute = owner.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)).hasModifier(CHARGING_BOOST_MODIFIER)) {
						maidAttribute.applyModifier(CHARGING_BOOST_MODIFIER);
					}
				} else {
					// Reset speed
					resetSpeed();
				}
			} else {
				// no target or target died
				resetSpeed();
				if (ticksCharge.isEnable()) {
					ticksCharge.setValue(0);
				}
			}
		}
	}
	/**
	 * Reset AI speed once.
	 */
	protected void resetSpeed() {
		IAttributeInstance maidAttribute;
		if ((maidAttribute = owner.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)).hasModifier(CHARGING_BOOST_MODIFIER)) {
			maidAttribute.removeModifier(CHARGING_BOOST_UUID);
		}
	}

	@Override
	public double getDistanceToSearchTargets() {
		if (owner.isFreedom()) {
			return 21d;
		}
		return super.getDistanceToSearchTargets();
	}

	@Override
	public double getLimitRangeSqOnFollow() {
		return 18 * 18;
	}

	@Override
	public double getFreedomTrackingRangeSq() {
		return 25 * 25;
	}
}
