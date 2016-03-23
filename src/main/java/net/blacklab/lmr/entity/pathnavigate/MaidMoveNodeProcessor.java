package net.blacklab.lmr.entity.pathnavigate;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class MaidMoveNodeProcessor extends WalkNodeProcessor {
	
	protected boolean canSwim;
	
	@Override
	public void setCanSwim(boolean canSwimIn) {
		super.setCanSwim(canSwimIn);
		canSwim = canSwimIn;
	}
	
	@Override
	public PathPoint func_186318_b() {
		if (canSwim) {
			return this.openPoint(MathHelper.floor_double(field_186326_b.getEntityBoundingBox().minX), MathHelper.floor_double(field_186326_b.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(field_186326_b.getEntityBoundingBox().minZ));
		}
		return super.func_186318_b();
	}

	@Override
	public PathPoint func_186325_a(double x, double y, double target) {
		if (canSwim) {
			return this.openPoint(MathHelper.floor_double(x - (double)(field_186326_b.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(target - (double)(field_186326_b.width / 2.0F)));
		}
		return super.func_186325_a(x, y, target);
	}
	
	@Override
	public int func_186320_a(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
		if (canSwim && field_186326_b.isInWater()) {
			int i = 0;

			for (EnumFacing enumfacing : EnumFacing.values()) {
				PathPoint pathpoint = this.getSafePoint(field_186326_b, currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());

				if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
					pathOptions[i++] = pathpoint;
				}
			}

			return i;
		}
		return super.func_186320_a(pathOptions, currentPoint, targetPoint, maxDistance);
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
