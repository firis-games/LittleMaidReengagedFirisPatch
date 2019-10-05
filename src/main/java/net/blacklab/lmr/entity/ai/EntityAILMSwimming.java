package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAILMSwimming extends EntityAISwimming {

	protected EntityLiving theEntity;

	public EntityAILMSwimming(EntityLiving par1EntityLiving) {
		super(par1EntityLiving);
		theEntity = par1EntityLiving;
	}

	@Override
	public boolean shouldExecute() {
		return theEntity.isInWater() || theEntity.isInsideOfMaterial(Material.WATER) || theEntity.isInLava();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateTask() {
		super.updateTask();

		double totalmotionY = 0d;
		if(theEntity instanceof EntityLittleMaid){
			if(theEntity.isInLava()){
//				theEntity.motionY+=1.0D;
				theEntity.getJumpHelper().setJumping();
				return;
			}
			EntityLittleMaid theMaid = (EntityLittleMaid) theEntity;
			if(theMaid.isInWater()){
				double xd = theMaid.posX;
				double yd = theMaid.getEntityBoundingBox().minY;
				double zd = theMaid.posZ;
				int x = MathHelper.floor(xd);
				int z = MathHelper.floor(zd);
				int y = MathHelper.floor(yd);
				totalmotionY += 0.03D * MathHelper.cos(theMaid.ticksExisted/16f);
//				if(theMaid.worldObj.isAnyLiquid(new AxisAlignedBB(x, y, z, x, y+h+1, z))){
//					totalmotionY += 0.05D;
//				}

				PathPoint pathPoint = null;
				Path pathEntity = theMaid.getNavigator().getPath();

				// Main AI
				if(pathEntity!=null){
					pathPoint = pathEntity.getFinalPathPoint();
					theMaid.motionX = ((pathPoint.x>x)?1:(pathPoint.x<x)?-1:0) * theMaid.getAIMoveSpeed()/5d;
					theMaid.motionZ = ((pathPoint.z>z)?1:(pathPoint.z<z)?-1:0) * theMaid.getAIMoveSpeed()/5d;
					totalmotionY +=		((pathPoint.y>=y)?1:-1) * theMaid.getAIMoveSpeed()/3d;
				}

				// Breathing
				if (theMaid.getAir() <= 0) {
					theMaid.motionY += 0.1D;
				}
				
				if (MaidHelper.canStartFollow(theMaid)) {
					// Move 
					IBlockState iState;
					
					// Going ashore
					if (pathPoint != null && Math.abs(pathPoint.y - yd) < 3d && Math.pow(pathPoint.x - xd, 2) + Math.pow(pathPoint.z - zd, 2) < 9d &&
							(iState = theMaid.getEntityWorld().getBlockState(new BlockPos(pathPoint.x, pathPoint.y + 1, pathPoint.z)))
							.getBlock().getMaterial(iState) != Material.WATER) {
						totalmotionY += 0.05D;
					}
					theMaid.motionY = totalmotionY;
				}
			}
		}
	}

}
