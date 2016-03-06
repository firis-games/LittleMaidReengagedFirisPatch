package littleMaidMobX;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class LMM_EntityAIFollowOwner extends EntityAIBase implements LMM_IEntityAI {

	private LMM_EntityLittleMaid theMaid;
	private Entity theOwner;
	private float moveSpeed;
	private PathNavigate petPathfinder;
	private int field_48310_h;
	protected double maxDist;
	protected double minDist;
	protected double sprintDist;
	protected double toDistance;
	protected boolean isEnable;

	public LMM_EntityAIFollowOwner(LMM_EntityLittleMaid par1EntityLittleMaid,
			float pSpeed, double pMin, double pMax, double pSprintDistSQ) {
		theMaid = par1EntityLittleMaid;
		moveSpeed = pSpeed;
		petPathfinder = par1EntityLittleMaid.getNavigator();
		minDist = pMin;
		maxDist = pMax;
		sprintDist = pSprintDistSQ;
		isEnable = true;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!isEnable)
			return false;

		Entity entityliving = theMaid.getOwner();
		if (entityliving == null) {
			return false;
		}

		if (theMaid.isSitting()||theMaid.isMaidWait()) {
			return false;
		}

		toDistance = theMaid.getDistanceSqToEntity(entityliving);
		if (toDistance < minDist && !theMaid.isInWater()) {
			return false;
		}
		theOwner = entityliving;
		return true;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		toDistance = theMaid.getDistanceSqToEntity(theOwner);
		if(theMaid.handleWaterMovement())
			return !theMaid.isMaidWait()&&!theMaid.isSitting();
		return !theMaid.getNavigator().noPath()
				&&(toDistance > maxDist)
				&& !theMaid.isSitting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		field_48310_h = 0;
		//lastAvoidWater = petPathfinder.getAvoidsWater();
		//petPathfinder.setAvoidsWater(false);
//		if(!theMaid.isInWater()) ((PathNavigateGround)this.theMaid.getNavigator()).setAvoidsWater(false);
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		theMaid.setSprinting(false);
		theOwner = null;
//		if(!theMaid.isInWater()) ((PathNavigateGround)this.theMaid.getNavigator()).setAvoidsWater(true);
		petPathfinder.clearPathEntity();
		//petPathfinder.setAvoidsWater(lastAvoidWater);
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		theMaid.getLookHelper().setLookPositionWithEntity(theOwner, 10F,
				theMaid.getVerticalFaceSpeed());

		if (theMaid.isSitting()) {
			return;
		}
		// 指定距離以上ならダッシュ
		if(!theMaid.isInWater()){
			theMaid.setSprinting(toDistance > sprintDist);
			if (--field_48310_h > 0) {
				return;
			}
		}

		field_48310_h = 10;

		PathEntity entity = theMaid.getNavigator().getPathToEntityLiving(theOwner);
		/*
		if(entity==null){
			if(theMaid.isInWater()&&theMaid.swimmingEnabled){
				int x = MathHelper.floor_double(theOwner.posX);
				int z = MathHelper.floor_double(theOwner.posZ);
				int y = MathHelper.floor_double(theOwner.posY);
				LMM_LittleMaidMobNX.Debug("TARGET POS %d,%d,%d", x,y,z);
				if(theMaid.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock().getMaterial()!=Material.water){
					if(theMaid.worldObj.getBlockState(new BlockPos(x, y-1, z)).getBlock().getMaterial()==Material.water)
						entity = theMaid.getNavigator().getPathToXYZ(theOwner.posX, theOwner.posY-1, theOwner.posZ);
					else {
						theMaid.setLocationAndAngles(x, y+1, z, theMaid.rotationYaw, theMaid.rotationPitch);
					}
				}
			}
			return;
		}
		*/
		theMaid.getNavigator().setPath(entity, moveSpeed);
	}

	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

}
