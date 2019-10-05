package net.blacklab.lib.minecraft.vector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VectorUtil {

	/**
	 * do1:当たり判定のチェック
	 * do2:常時ブロク判定、透過判定も当たり判定も無視。
	 */
	public static boolean canBlockBeSeen(Entity entity,int pX, int pY, int pZ, boolean toTop, boolean do1, boolean do2) {
		// ブロックの可視判定
		World worldObj = entity.getEntityWorld();
		BlockPos pos = new BlockPos(pX, pY, pZ);
		IBlockState state = worldObj.getBlockState(pos);
		Block lblock = state.getBlock();
		if (lblock == null) {
			return false;
		}
//		lblock.setBlockBoundsBasedOnState(worldObj, new BlockPos(pX, pY, pZ));
		AxisAlignedBB boundingBox = state.getBoundingBox(worldObj, pos);
		
		Vec3d vec3do = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3d vec3dt = new Vec3d(pX + 0.5D, pY + ((boundingBox.maxY + boundingBox.minY) * (toTop ? 0.9D : 0.5D)), pZ + 0.5D);
		RayTraceResult movingobjectposition = worldObj.rayTraceBlocks(vec3do, vec3dt, do1, do2, false);
		
		if (movingobjectposition != null && movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
			// 接触ブロックが指定したものならば
			if (movingobjectposition.getBlockPos().getX() == pX && 
					movingobjectposition.getBlockPos().getY() == pY &&
					movingobjectposition.getBlockPos().getZ() == pZ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 基本的にcanBlockBeSeenに同じ。違いは足元基準で「通れるか」を判断するもの
	 */
	public static boolean canMoveThrough(Entity pEntity, double fixHeight, double pX, double pY, double pZ, boolean toTop, boolean do1, boolean do2){
		Block lblock = pEntity.getEntityWorld().getBlockState(new BlockPos(pX, pY, pZ)).getBlock();
		if (lblock == null) {
			return false;
		}
//		lblock.setBlockBoundsBasedOnState(pEntity.getEntityWorld(), new BlockPos(pX, pY, pZ));
		
		Vec3d vec3do = new Vec3d(pEntity.posX, pEntity.posY+fixHeight, pEntity.posZ);
		Vec3d vec3dt = new Vec3d(pX, pY, pZ);
		RayTraceResult movingobjectposition = pEntity.getEntityWorld().rayTraceBlocks(vec3do, vec3dt, do1, do2, false);
		
		if (movingobjectposition != null && movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
			if (movingobjectposition.getBlockPos().getX() == (int)pX && 
					movingobjectposition.getBlockPos().getY() == (int)pY &&
					movingobjectposition.getBlockPos().getZ() == (int)pZ) {
				return true;
			}
			return false;
		}
		return true;
	}

}
