package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.List;
import java.util.UUID;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ai.target.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.littlemaid.ai.target.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTriggerRegisterHelper;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.Counter;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

/**
 * 剣士とオノ
 * 独自基準としてモード定数は0x0080は平常、0x00c0は血まみれモードと区別。
 */
public class EntityMode_Fencer extends EntityModeBase {

	public static final String mmode_Fencer			= "Fencer";
	public static final String mmode_Bloodsucker	= "Bloodsucker";

	public static final String mtrigger_Sword 	= "Fencer:Sword";
	public static final String mtrigger_Axe = "Bloodsucker:Axe";
	
	// Charging timer
	protected Counter ticksCharge;
	protected static final UUID CHARGING_BOOST_UUID = UUID.nameUUIDFromBytes(LittleMaidReengaged.MODID.concat(":fencer_charge_boost").getBytes());
	protected static final AttributeModifier CHARGING_BOOST_MODIFIER = new AttributeModifier(CHARGING_BOOST_UUID, LittleMaidReengaged.MODID.concat(":fencer_charge_boost"), 0.2d, 0);

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
		ModeTriggerRegisterHelper.register(mmode_Fencer, mtrigger_Sword);
		ModeTriggerRegisterHelper.register(mmode_Bloodsucker, mtrigger_Axe);
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
		ltasks[1].addTask(4, new EntityAILMNearestAttackableTarget<EntityLivingBase>(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Fencer, ltasks);


		// Bloodsucker:0x00c0
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);

		ltasks2[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget<EntityLivingBase>(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Bloodsucker, ltasks2);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mmode_Fencer, litemstack)) {
				owner.setMaidMode(mmode_Fencer);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.Fencer);
				if (litemstack.getItem() instanceof ItemSpade && pentityplayer != null) {
					AchievementsLMRE.grantAC(pentityplayer, AC.Buster);
				}
				return true;
			} else  if (isTriggerItem(mmode_Bloodsucker, litemstack)) {
				owner.setMaidMode(mmode_Bloodsucker);
				AchievementsLMRE.grantAC(pentityplayer, AC.RandomKiller);
				if (litemstack.getItem() instanceof ItemSpade && pentityplayer != null) {
					AchievementsLMRE.grantAC(pentityplayer, AC.Buster);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
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
	public int getNextEquipItem(String pMode) {
		if (isTriggerItem(pMode, owner.getHandSlotForModeChange())) {
			return InventoryLittleMaid.handInventoryOffset;
		}

		int li;
		//int ll = -1;
		double ld = 0;
		double lld;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Fencer :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;

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
					//ll = li;
					ld = lld;
				}
			}
			break;
		case mmode_Bloodsucker :
			for (li = 0; li < owner.maidInventory.getSizeInventory(); li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;

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
					//ll = li;
					ld = lld;
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

		switch (pMode) {
		case mmode_Fencer:
			return owner.getModeTrigger().isTriggerable(mtrigger_Sword, par1ItemStack, ItemSword.class);
		case mmode_Bloodsucker:
			boolean ret1 = owner.getModeTrigger().isTriggerable(mtrigger_Axe, par1ItemStack, ItemAxe.class);
			boolean ret2 = owner.getModeTrigger().isTriggerable(mtrigger_Axe, owner.getHeldItemOffhand(), ItemAxe.class);
			return ret1 && ret2;
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
		return owner.getMaidModeString().equals(mmode_Fencer);
	}
	
	@Override
	public boolean checkEntity(String pMode, Entity pEntity) {
		// Distance from master
		if (!owner.isFreedom() && owner.getMaidMasterEntity() != null &&
				owner.getMaidMasterEntity().getDistanceSq(pEntity) >= getLimitRangeSqOnFollow()) {
			return false;
		}

		if (pEntity instanceof EntityCreeper) {
			if (owner.getMaidMasterEntity() == null ? true : !owner.getMaidMasterEntity().equals(((EntityCreeper) pEntity).getAttackTarget())) {
				return false;
			}
		}
		return !owner.getIFF(pEntity);
	}
	
	@Override
	public void updateAITick(String pMode) {
		super.updateAITick(pMode);
		if (pMode.equals(mmode_Fencer)|| pMode.equals(mmode_Bloodsucker)) {
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
			return 18d;
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
	
	/**
	 * 被ダメ時の処理
	 */
	@Override
	public float attackEntityFrom(DamageSource damageSource, float amount) {
		
		//Fencer以外はスキップ
		if (!mmode_Fencer.equals(owner.getMaidModeString())) return 0;
		
		//Mobからのダメージ以外はスキップ
		if (!"mob".equals(damageSource.getDamageType())) return 0;
		
		//ブースト状態でないかつ
		//ダメージが2以上かつ
		//現HP- ダメージが10以下の場合
		//mobからのダメージの場合
		if (!owner.getMaidOverDriveTime().isEnable() 
				&& 2.0F <= amount
				&& owner.getHealth() - amount <= 10.0F) {
			
			//ブースト発動
			owner.getMaidOverDriveTime().setValue(100);
			owner.playSound("mob.zombie.infect");
			
			//周辺の敵を吹き飛ばし
			List<EntityLiving> entityList = owner.world.getEntitiesWithinAABB(EntityLiving.class, (new AxisAlignedBB(owner.getPosition()).grow(3.0D)));
			for (EntityLiving entity : entityList) {
				if (entity.getAttackTarget() instanceof EntityLittleMaid
						|| entity.getRevengeTarget() instanceof EntityLittleMaid) {
					entity.knockBack(owner, 1.5F, 
							(double)MathHelper.sin(owner.rotationYaw * 0.017453292F), 
							(double)(-MathHelper.cos(owner.rotationYaw * 0.017453292F)));
					entity.setAttackTarget(null);
					entity.setRevengeTarget(null);
				}
			}
		} else if (!owner.getMaidOverDriveTime().isEnable()
				&& owner.world.rand.nextInt(100) < 10) {
			//ブースト発動
			owner.getMaidOverDriveTime().setValue(60);
			owner.playSound("mob.zombie.infect");
		}
		
		return 0;
	}
}
