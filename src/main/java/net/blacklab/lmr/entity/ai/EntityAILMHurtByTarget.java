package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.mode.EntityModeBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class EntityAILMHurtByTarget extends EntityAIHurtByTarget {

	protected EntityLittleMaid theMaid;
	private boolean field_75303_a;
	private int field_75301_b;
	private int field_75302_c;


	public EntityAILMHurtByTarget(EntityLittleMaid par1EntityLiving, boolean par2) {
		super(par1EntityLiving, par2);
		
		theMaid = par1EntityLiving;
		field_75303_a = false;
		field_75301_b = 0;
		field_75302_c = 0;
	}

	@Override
	public boolean shouldExecute() {
		if(theMaid.isMaidWaitEx()) return false;
		if (theMaid.isContract() && !theMaid.isBlocking() && theMaid.getMaidMasterEntity() != null) {
			// フェンサー系は主に対する攻撃に反応
			EntityLivingBase lentity = theMaid.getMaidMasterEntity().getAITarget();
			if (isSuitableTarget(lentity, false)) {
				theMaid.setRevengeTarget(lentity);
				return true;
			}
		}
		return super.shouldExecute();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		String s1 = taskOwner.getAITarget() == null ? "Null" : taskOwner.getAITarget().getClass().toString();
		String s2 = taskOwner.getAttackTarget() == null ? "Null" : taskOwner.getAttackTarget().getClass().toString();
		System.out.println(String.format("ID:%d, target:%s, attack:%s", taskOwner.getEntityId(), s1, s2));
		
		// 殴られた仕返し
		EntityLivingBase leliving = taskOwner.getAITarget();
		if (leliving != null && leliving != taskOwner.getAttackTarget()) {
			taskOwner.setAttackTarget(null);
			System.out.println(String.format("ID:%d, ChangeTarget.", taskOwner.getEntityId()));
		}
		
	}
	
	@Override
	protected boolean isSuitableTarget(EntityLivingBase par1EntityLiving, boolean par2) {
		// LMM用にカスタム
		if (par1EntityLiving == null) {
			return false;
		}
		if (par1EntityLiving == taskOwner) {
			return false;
		}
		if (par1EntityLiving == theMaid.getMaidMasterEntity()) {
			return false;
		}
		if (!par1EntityLiving.isEntityAlive()) {
			return false;
		}
		
		EntityModeBase lailm = theMaid.getActiveModeClass(); 
		if (lailm != null && lailm.isSearchEntity()) {
			if (!lailm.checkEntity(theMaid.getMaidModeInt(), par1EntityLiving)) {
				return false;
			}
		} else {
			if (theMaid.getIFF(par1EntityLiving)) {
				return false;
			}
		}
		
		// 基点から一定距離離れている場合も攻撃しない
		if (!taskOwner.isWithinHomeDistanceCurrentPosition()) {
			return false;
		}
		
		// ターゲットが見えない
		if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(par1EntityLiving)) {
			return false;
		}
		
		// 攻撃中止判定？
		if (this.field_75303_a) {
			if (--this.field_75302_c <= 0) {
				this.field_75301_b = 0;
			}
			
			if (this.field_75301_b == 0) {
				this.field_75301_b = this.func_75295_a(par1EntityLiving) ? 1 : 2;
			}
			
			if (this.field_75301_b == 2) {
				return false;
			}
		}
		
		return true;
	}

	protected boolean func_75295_a(Entity par1EntityLiving) {
		this.field_75302_c = 10 + this.taskOwner.getRNG().nextInt(5);
		PathEntity var2 = taskOwner.getNavigator().getPathToXYZ(par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
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

}
