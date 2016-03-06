package littleMaidMobX;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class LMM_EntityAISwimming extends EntityAISwimming {

	protected EntityLiving theEntity;
	
	public LMM_EntityAISwimming(EntityLiving par1EntityLiving) {
		super(par1EntityLiving);
		theEntity = par1EntityLiving;
	}

	@Override
	public boolean shouldExecute() {
		if(theEntity instanceof LMM_EntityLittleMaid){
			if(theEntity.isInWater()) return true;
		}
		return ((theEntity.getNavigator().noPath() ?
				(theEntity.isInsideOfMaterial(Material.water)) : theEntity.isInWater())
				|| theEntity.isInLava());
	}

	@Override
	public void updateTask() {
		super.updateTask();
		double totalmotionY = 0d;
		if(theEntity instanceof LMM_EntityLittleMaid){
			if(theEntity.isInLava()){
//				theEntity.motionY+=1.0D;
				theEntity.getJumpHelper().setJumping();
				return;
			}
			LMM_EntityLittleMaid theMaid = (LMM_EntityLittleMaid) theEntity;
			if(theMaid.isInWater()){
				double xd = theEntity.posX;
				double yd = theEntity.getEntityBoundingBox().minY;
				double zd = theEntity.posZ;
				int x = MathHelper.floor_double(xd);
				int z = MathHelper.floor_double(zd);
				int y = MathHelper.floor_double(yd);
				totalmotionY+= 0.05D*MathHelper.cos(theEntity.ticksExisted/8f);
//				if(theEntity.worldObj.isAnyLiquid(new AxisAlignedBB(x, y, z, x, y+h+1, z))){
//					totalmotionY += 0.05D;
//				}
				
				PathPoint pathPoint = null;
				PathEntity pathEntity = theMaid.prevPathEntity;
				if(pathEntity!=null && (theMaid.swimmingEnabled||!theMaid.isContract())){
					pathPoint = pathEntity.getFinalPathPoint();
					theEntity.motionX = ((pathPoint.xCoord>x)?1:(pathPoint.xCoord<x)?-1:0) * theEntity.getAIMoveSpeed()/5d;
					theEntity.motionZ = ((pathPoint.zCoord>z)?1:(pathPoint.zCoord<z)?-1:0) * theEntity.getAIMoveSpeed()/5d;
					totalmotionY +=		((pathPoint.yCoord>=y)?1:-1) * theEntity.getAIMoveSpeed()/5d;
					
				}
				if(theMaid.swimmingEnabled && theMaid.isInWater()){
					if (pathPoint != null && Math.abs(pathPoint.yCoord - yd) < 3d && Math.pow(pathPoint.xCoord - xd, 2) + Math.pow(pathPoint.zCoord - zd, 2) < 9d &&
							theMaid.worldObj.getBlockState(new BlockPos(pathPoint.xCoord,pathPoint.yCoord,pathPoint.zCoord)).getBlock().getMaterial() != Material.water) {
						totalmotionY += 0.1D;
						theMaid.motionX *= 2d;
						theMaid.motionZ *= 2d;
					}
					theEntity.motionY = totalmotionY;
				}else{
					theEntity.motionY = 0.04D;
				}
				if (theMaid.getAir() == 0) {
					theEntity.motionY += 0.2D;
				}
			}
		}
	}

}
