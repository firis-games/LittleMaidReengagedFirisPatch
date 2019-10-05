package net.blacklab.lmr.entity.pathnavigate;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class MaidMoveNodeProcessor extends WalkNodeProcessor {

	protected boolean canSwim;

	@Override
	public void setCanSwim(boolean canSwimIn) {
		super.setCanSwim(canSwimIn);
		canSwim = canSwimIn;
	}

	@Override
	public PathPoint getStart() {
		if (canSwim && entity.isInWater()) {
			return this.openPoint(MathHelper.floor(entity.getEntityBoundingBox().minX), MathHelper.floor(entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(entity.getEntityBoundingBox().minZ));
		}
		return super.getStart();
	}
	
	@Override
	public PathNodeType getPathNodeType(IBlockAccess x, int y, int z, int p_186330_4_) {
		PathNodeType pathNodeType1 = super.getPathNodeType(x, y, z, p_186330_4_);
		if (pathNodeType1 == PathNodeType.RAIL) {
			pathNodeType1 = PathNodeType.WALKABLE;
		}
		return pathNodeType1;
	}

	@Override
	public PathPoint getPathPointToCoords(double x, double y, double z) {
		if (canSwim && entity.isInWater()) {
			return this.openPoint(MathHelper.floor(x - (double)(entity.width / 2.0F)), MathHelper.floor(y + 0.5D), MathHelper.floor(z - (double)(entity.width / 2.0F)));
		}
		return super.getPathPointToCoords(x, y, z);
	}

	@Override
	public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint,
			float maxDistance) {
		if (canSwim && entity.isInWater()) {
			int i = 0;

			for (EnumFacing enumfacing : EnumFacing.values()) {
				PathPoint pathpoint = this.getSafePoint(entity, currentPoint.x + enumfacing.getFrontOffsetX(), currentPoint.y + enumfacing.getFrontOffsetY(), currentPoint.z + enumfacing.getFrontOffsetZ());

				if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
					pathOptions[i++] = pathpoint;
				}
			}

			return i;
		}
		return super.findPathOptions(pathOptions, currentPoint, targetPoint, maxDistance);
	}

	/**
	 * Returns a point that the entity can safely move to
	 */
	private PathPoint getSafePoint(Entity entityIn, int x, int y, int z) {
		int i = -1;//this.func_176186_b(entityIn, x, y, z);
		return i == -1 ? this.openPoint(x, y, z) : null;
	}

/*
	private int func_176186_b(Entity entityIn, int x, int y, int z) {

		for (int i = x; i < x + this.field_176168_c; ++i) {
			for (int j = y; j < y + this.field_176165_d; ++j) {
				for (int k = z; k < z + this.field_176166_e; ++k) {
					Block block = entityIn.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();

					if (block.getMaterial() != Material.water) {
						return 0;
					}
				}
			}
		}

		return -1;
	}
*/
}
