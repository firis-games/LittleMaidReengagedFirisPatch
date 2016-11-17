package net.blacklab.lmr.entity.ai;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.mode.EntityModeBase;
import net.blacklab.lmr.entity.mode.EntityMode_Fencer;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;

public class EntityAILMNearestAttackableTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

	protected EntityLittleMaid theMaid;
	protected int targetChance;
	protected EntityAILMNearestAttackableTargetSorter<?> theNearestAttackableTargetSorter;

	private boolean fretarget;
	private int fcanAttack;
	private int fretryCounter;

	public EntityAILMNearestAttackableTarget(EntityLittleMaid par1EntityLiving, Class<T> par2Class, int par4, boolean par5) {
		this(par1EntityLiving, par2Class, par4, par5, false);
	}

	public EntityAILMNearestAttackableTarget(EntityLittleMaid par1, Class<T> par2, int par4, boolean par5, boolean par6) {
		super(par1, par2, par4, par5, par6, null);
//		targetClass = par2;
		targetChance = par4;
		theNearestAttackableTargetSorter = new EntityAILMNearestAttackableTargetSorter<T>(par1);
		fretarget = par6;
		theMaid = par1;

		setMutexBits(1);
	}


	@Override
	public boolean shouldExecute() {
		if (targetEntity != null && targetEntity.isEntityAlive() && taskOwner.getAttackTarget() == targetEntity) {
			return true;
		}

		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
//		} else if (theMaid.getAttackTarget() != null) {
//			return true;
		}

		double lfollowRange;
		if (!(
				taskOwner instanceof EntityLittleMaid && ((EntityLittleMaid) taskOwner).getActiveModeClass() != null &&
				(lfollowRange = ((EntityLittleMaid) taskOwner).getActiveModeClass().getDistanceToSearchTargets()) > 0
				)) {
			lfollowRange = getTargetDistance();
		}

		List<T> llist = this.taskOwner.worldObj.getEntitiesWithinAABB(targetClass, taskOwner.getEntityBoundingBox().expand(lfollowRange, 8.0D, lfollowRange));

		if (theMaid.getMaidMasterEntity() != null && !theMaid.isBloodsuck()) {
			// ソーターを主中心へ
			theNearestAttackableTargetSorter.setEntity(theMaid.getMaidMasterEntity());
		} else {
			// 自分中心にソート
			theNearestAttackableTargetSorter.setEntity(theMaid);
		}
		Collections.sort(llist, theNearestAttackableTargetSorter);
		Iterator<T> nearEntityCollectionsIterator = llist.iterator();
		while (nearEntityCollectionsIterator.hasNext()) {
			T lentity = (T)nearEntityCollectionsIterator.next();
			if (lentity == theMaid.getAttackTarget()) {
				return true;
			}
			if (lentity.isEntityAlive() && this.isSuitableTargetLM(lentity, false)) {
				this.targetEntity = lentity;
				return true;
			}
		}

		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		theMaid.setAttackTarget((EntityLivingBase)targetEntity);

		fcanAttack = 0;
		fretryCounter = 0;
	}

	@Override
	public boolean continueExecuting() {
		/*
		if (theMaid.getActiveModeClass() != null && theMaid.getActiveModeClass().isSearchEntity()) {
			if (!theMaid.getActiveModeClass().checkEntity(theMaid.getMaidModeInt(), targetEntity)) {
				return false;
			}
		}
		*/
		return super.continueExecuting();
	}

//	@Override
	protected boolean isSuitableTargetLM(Entity pTarget, boolean par2) {
		// LMM用にカスタム
		// 非生物も対象のため別クラス
		if (pTarget == null) {
			return false;
		}

		if (pTarget == taskOwner) {
			return false;
		}
		if (pTarget == theMaid.getMaidMasterEntity()) {
			return false;
		}

		if (!pTarget.isEntityAlive()) {
			return false;
		}

		EntityModeBase lailm = theMaid.getActiveModeClass();
		if (lailm != null && lailm.isSearchEntity()) {
			if (!lailm.checkEntity(theMaid.getMaidModeInt(), pTarget)) {
				return false;
			}
		} else {
			if (theMaid.getIFF(pTarget)) {
				return false;
			}
			// Can't reach target
			if (!MaidHelper.isTargetReachable(theMaid, pTarget, 0)) {
				return false;
			}
		}

		// ターゲットが見えない
		if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(pTarget)) {
			return false;
		}

		// 攻撃中止判定？
		if (this.fretarget) {
			if (--this.fretryCounter <= 0) {
				this.fcanAttack = 0;
			}

			if (this.fcanAttack == 0) {
				this.fcanAttack = this.func_75295_a(pTarget) ? 1 : 2;
			}

			if (this.fcanAttack == 2) {
				return false;
			}
		}

		return true;
	}

	// 最終位置が攻撃の間合いでなければ失敗
	protected boolean func_75295_a(Entity par1EntityLiving) {
		this.fretryCounter = 10 + this.taskOwner.getRNG().nextInt(5);
		Path var2 = taskOwner.getNavigator().getPathToXYZ(par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
//		PathEntity var2 = this.taskOwner.getNavigator().getPathToEntityLiving(par1EntityLiving);

		if (var2 == null) {
			return false;
		}
		PathPoint var3 = var2.getFinalPathPoint();

		if (var3 == null) {
			return false;
		}
		int var4 = var3.xCoord - MathHelper.floor_double(par1EntityLiving.posX);
		int var5 = var3.zCoord - MathHelper.floor_double(par1EntityLiving.posZ);
		return var4 * var4 + var5 * var5 <= 2.25D;
	}

	@Override
	protected double getTargetDistance() {
		double targetd = 0;
		if (theMaid.getActiveModeClass() != null && (targetd = theMaid.getActiveModeClass().getDistanceToSearchTargets()) > 0) {
			return targetd;
		}
		return super.getTargetDistance();
	}

}
