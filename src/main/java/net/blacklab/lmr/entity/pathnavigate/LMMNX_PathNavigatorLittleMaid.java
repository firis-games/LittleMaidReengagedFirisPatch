package net.blacklab.lmr.entity.pathnavigate;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LMMNX_PathNavigatorLittleMaid extends PathNavigateGround {
	
	protected EntityLittleMaid theMaid;

	public LMMNX_PathNavigatorLittleMaid(EntityLiving entitylivingIn,
			World worldIn) {
		super(entitylivingIn, worldIn);
		if (theEntity instanceof EntityLittleMaid) theMaid = (EntityLittleMaid) theEntity;
	}

	@Override
	protected PathFinder getPathFinder() {
		nodeProcessor = new MaidMoveNodeProcessor();
		nodeProcessor.setCanEnterDoors(true);
		return new PathFinder(nodeProcessor);
	}

	@Override
	protected Vec3d getEntityPosition() {
		if (theMaid.isSwimmingEnabled() && theMaid.isInWater())
			return new Vec3d(theEntity.posX, theEntity.posY + (double)theEntity.height * 0.5D, theEntity.posZ);
		return super.getEntityPosition();
	}

	@Override
	protected void pathFollow() {
		if (theMaid.isSwimmingEnabled() && theMaid.isInWater()) {
			Vec3d vec3 = getEntityPosition();
			float f = theEntity.width * theEntity.width;
			int i = 6;

			if (vec3.squareDistanceTo(currentPath.getVectorFromIndex(theEntity, currentPath.getCurrentPathIndex())) < (double)f) {
				currentPath.incrementPathIndex();
			}

			for (int j = Math.min(currentPath.getCurrentPathIndex() + i, currentPath.getCurrentPathLength() - 1); j > currentPath.getCurrentPathIndex(); --j) {
				Vec3d vec31 = currentPath.getVectorFromIndex(theEntity, j);

				if (vec31.squareDistanceTo(vec3) <= 36.0D && isDirectPathBetweenPoints(vec3, vec31, 0, 0, 0)) {
					currentPath.setCurrentPathIndex(j);
					break;
				}
			}

			checkForStuck(vec3);
			return;
		}
		super.pathFollow();
	}

	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32,
			int sizeX, int sizeY, int sizeZ) {
		if (theMaid.isSwimmingEnabled()) {
			RayTraceResult movingobjectposition = worldObj.rayTraceBlocks(posVec31, new Vec3d(posVec32.xCoord, posVec32.yCoord + (double)theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
			return movingobjectposition == null || movingobjectposition.typeOfHit == RayTraceResult.Type.MISS;
		}
		return super.isDirectPathBetweenPoints(posVec31, posVec32, sizeX, sizeY, sizeZ);
	}

}
