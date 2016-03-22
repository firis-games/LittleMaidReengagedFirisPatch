package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAILMAttackOnCollide extends EntityAIBase implements IEntityAI {

	protected boolean fEnable;

	protected World worldObj;
	protected EntityLittleMaid theMaid;
	protected Entity entityTarget;
	protected float moveSpeed;
	protected boolean isReroute;
	protected PathEntity pathToTarget;
	protected int rerouteTimer;
	protected double attackRange;

	public boolean isGuard;


	public EntityAILMAttackOnCollide(EntityLittleMaid par1EntityLittleMaid, float par2, boolean par3) {
		theMaid = par1EntityLittleMaid;
		worldObj = par1EntityLittleMaid.worldObj;
		moveSpeed = par2;
		isReroute = par3;
		isGuard = false;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		Entity lentity = theMaid.getAttackTarget();
		if (!fEnable||theMaid.isMaidWait()) {
			return false;
		}
		if (lentity == null) {
			return false;
		}
		
		lentity = theMaid.getAttackTarget();
		if(lentity==null) return false;
		
		entityTarget = lentity;

		pathToTarget = theMaid.getNavigator().getPathToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ);
//		pathToTarget = theMaid.getNavigator().getPathToEntityLiving(entityTarget);
		attackRange = (double)theMaid.width + (double)entityTarget.width + 0.4D;
		attackRange *= attackRange;
		
		if (theMaid.isFreedom() && !theMaid.isWithinHomeDistanceFromPosition(new BlockPos(MathHelper.floor_double(entityTarget.posX), MathHelper.floor_double(entityTarget.posY), MathHelper.floor_double(entityTarget.posZ)))) {
			return false;
		}
		
		if ((pathToTarget != null) || (theMaid.getDistanceSq(entityTarget.posX, entityTarget.getEntityBoundingBox().minY, entityTarget.posZ) <= attackRange)) {
			return true;
		}
		theMaid.setAttackTarget(null);
		theMaid.setRevengeTarget(null);
//		theMaid.getNavigator().clearPathEntity();
		return false;
		
	}
	
	@Override
	public void startExecuting() {
		Entity lentity = theMaid.getAttackTarget();
		/*
		if(theMaid.getMaidModeInt() == LMM_EntityMode_Fencer.mmode_Fencer && lentity instanceof EntityCreeper){
			if(theMaid.getMaidMasterEntity()==null||((EntityCreeper) lentity).getAttackTarget()==null){
			}else if(!((EntityCreeper) lentity).getAttackTarget().equals(theMaid.getMaidMasterEntity())){
			}else{
				theMaid.playLittleMaidSound(theMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, true);
			}
		}else if(!lentity.isDead){
			theMaid.playLittleMaidSound(theMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, true);
		}
		*/
		if(!lentity.isDead){
			theMaid.playLittleMaidSound(theMaid.isBloodsuck() ? EnumSound.findTarget_B : EnumSound.findTarget_N, false);
		}
		theMaid.getNavigator().setPath(pathToTarget, moveSpeed);
		rerouteTimer = 0;
		theMaid.maidAvatar.stopActiveHand();
	}

	@Override
	public boolean continueExecuting() {
		Entity lentity = theMaid.getAttackTarget();
		if (lentity == null || entityTarget != lentity) {
			return false;
		}
		
		if (entityTarget.isDead) {
			theMaid.setAttackTarget(null);
			theMaid.setRevengeTarget(null);
			theMaid.getNavigator().clearPathEntity();
			return false;
		}
		
		if (!entityTarget.isEntityAlive()) {
			return false;
		}
		
		if (!isReroute) {
			return !theMaid.getNavigator().noPath();
		}
		
		return true;
	}

	@Override
	public void resetTask() {
		entityTarget = null;
//		theMaid.getNavigator().clearPathEntity();
		theMaid.maidAvatar.stopActiveHand();
	}

	@Override
	public void updateTask() {
		theMaid.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
		
//		if ((isReroute || theMaid.getEntitySenses().canSee(entityTarget)) && --rerouteTimer <= 0) {
//			// リルート
//			rerouteTimer = 4 + theMaid.getRNG().nextInt(7);
//			theMaid.getNavigator().tryMoveToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ, moveSpeed);
//		}
		if (--rerouteTimer <= 0) {
			if (isReroute) {
				// リルート
				rerouteTimer = 4 + theMaid.getRNG().nextInt(7);
				theMaid.getNavigator().tryMoveToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ, moveSpeed);
			}
			if (theMaid.getEntitySenses().canSee(entityTarget)) {
				// リルート
				rerouteTimer = 4 + theMaid.getRNG().nextInt(7);
				theMaid.getNavigator().tryMoveToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ, moveSpeed);
			} else {
				theMaid.setAttackTarget(null);
				theMaid.setRevengeTarget(null);
			}
		}
		
		boolean lguard = false;
		if (theMaid.getDistanceSq(entityTarget.posX, entityTarget.getEntityBoundingBox().minY, entityTarget.posZ) > attackRange) {
			if (isGuard && theMaid.isMaskedMaid()) {
				EntityLivingBase lel = null;
				if (entityTarget instanceof EntityCreature) {
					lel = ((EntityCreature)entityTarget).getAttackTarget();
				}
				else if (entityTarget instanceof EntityLivingBase) {
					lel = ((EntityLivingBase)entityTarget).getAITarget();
				}
				if (lel == theMaid) {
					ItemStack li = theMaid.getCurrentEquippedItem();
					if (li != null && li.getItemUseAction() == EnumAction.BLOCK) {
						li.useItemRightClick(worldObj, theMaid.maidAvatar, EnumHand.MAIN_HAND);
						lguard = true;
					}
				}
			}
			return;
		}
		if (theMaid.maidAvatar.isHandActive() && !lguard) {
			theMaid.maidAvatar.stopActiveHand();
		}
		
		if (!theMaid.getSwingStatusDominant().canAttack()) {
			return;
		}
		// 正面から110度方向が攻撃範囲
		double tdx = entityTarget.posX - theMaid.posX;
		double tdz = entityTarget.posZ - theMaid.posZ;
		double vdx = -Math.sin(theMaid.renderYawOffset * 3.1415926535897932384626433832795F / 180F);
		double vdz = Math.cos(theMaid.renderYawOffset * 3.1415926535897932384626433832795F / 180F);
		double ld = (tdx * vdx + tdz * vdz) / (Math.sqrt(tdx * tdx + tdz * tdz) * Math.sqrt(vdx * vdx + vdz * vdz));
//	        System.out.println(theMaid.renderYawOffset + ", " + ld);
		if (ld < -0.35D) {
			return;
		}
		
		// 攻撃
		theMaid.attackEntityAsMob(entityTarget);
		//theMaid.moveback();
		if (theMaid.getActiveModeClass().isChangeTartget(entityTarget)) {
			// 対象を再設定させる
			theMaid.setAttackTarget(null);
			theMaid.setRevengeTarget(null);
			theMaid.getNavigator().clearPathEntity();
		}
		return;
	}

	@Override
	public void setEnable(boolean pFlag) {
		fEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return fEnable;
	}
	
}
