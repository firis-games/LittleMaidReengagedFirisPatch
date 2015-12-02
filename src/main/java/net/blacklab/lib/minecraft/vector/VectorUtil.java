package net.blacklab.lib.minecraft.vector;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class VectorUtil {

	/**
	 * do1:当たり判定のチェック
	 * do2:常時ブロク判定、透過判定も当たり判定も無視。
	 */
	public static boolean canBlockBeSeen(Entity entity,int pX, int pY, int pZ, boolean toTop, boolean do1, boolean do2) {
		// ブロックの可視判定
		World worldObj = entity.worldObj;
		Block lblock = worldObj.getBlockState(new BlockPos(pX, pY, pZ)).getBlock();
		if (lblock == null) {
			return false;
		}
		lblock.setBlockBoundsBasedOnState(worldObj, new BlockPos(pX, pY, pZ));
		
		Vec3 vec3do = new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3 vec3dt = new Vec3(pX + 0.5D, pY + ((lblock.getBlockBoundsMaxY() + lblock.getBlockBoundsMinY()) * (toTop ? 0.9D : 0.5D)), pZ + 0.5D);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3do, vec3dt, do1, do2, false);
		
		if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
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
		Block lblock = pEntity.worldObj.getBlockState(new BlockPos(pX, pY, pZ)).getBlock();
		if (lblock == null) {
			return false;
		}
//		lblock.setBlockBoundsBasedOnState(pEntity.worldObj, new BlockPos(pX, pY, pZ));
		
		Vec3 vec3do = new Vec3(pEntity.posX, pEntity.posY+fixHeight, pEntity.posZ);
		Vec3 vec3dt = new Vec3(pX, pY, pZ);
		MovingObjectPosition movingobjectposition = pEntity.worldObj.rayTraceBlocks(vec3do, vec3dt, do1, do2, false);
		
		if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
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
