package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class MaidHelper {

	/**
	 * メイドにアイテムを与える
	 */
	public static void giveItem(ItemStack stack, EntityLittleMaid maid) {
		if (!maid.maidInventory.addItemStackToInventory(stack)) {
			maid.entityDropItem(stack, 0);
		}
	}

	/**
	 * Returns if the maid can walk or swim. Used for move-AI.
	 * @param pMaid
	 * @return
	 */
	public static boolean canStartWalk(EntityLittleMaid pMaid) {
		if (pMaid.isFreedom()) {
			return true;
		}
		EntityPlayer lMaster = pMaid.getMaidMasterEntity();
		return lMaster == null ? false : canStartFollow(pMaid, lMaster, 0);
	}

	/**
	 * Returns if the maid can follow her master. If the maid is freedom mode, this method returns false.
	 * @param pMaid
	 * @param pTarget
	 * @param expandIgnoreRangeSq
	 * @return
	 */
	public static boolean canStartFollow(EntityLittleMaid pMaid, Entity pTarget, double expandIgnoreRangeSq) {
		if (!pMaid.isFreedom()) {
			return pMaid.getDistanceSqToEntity(pTarget) > pMaid.getActiveModeClass().getDistanceSqToStartFollow() + expandIgnoreRangeSq;
		}
		return false;
	}

	public static boolean isOutSideHome(EntityLittleMaid pMaid) {
		if (pMaid.isFreedom()) {
			return pMaid.getDistanceSqToCenter(pMaid.getHomePosition()) > pMaid.getActiveModeClass().getFreedomTrackingRangeSq();
		}
		return false;
	}

	public static boolean isTargetReachable(EntityLittleMaid pMaid, Entity pTarget, double expandRangeSq) {
		return isTargetReachable(pMaid, pTarget.getPositionVector(), expandRangeSq);
	}

	/** Can maid reach target? **/
	public static boolean isTargetReachable(EntityLittleMaid pMaid, Vec3d pTarget, double expandRangeSq) {
		expandRangeSq -= 1D;
		return pMaid.isFreedom() ?
				pMaid.getHomePosition().distanceSq(pTarget.xCoord, pTarget.yCoord, pTarget.zCoord) <= pMaid.getActiveModeClass().getFreedomTrackingRangeSq() + expandRangeSq :
				(pMaid.getMaidMasterEntity() == null ? true : pMaid.getMaidMasterEntity().getPositionVector().squareDistanceTo(pTarget) <= pMaid.getActiveModeClass().getLimitRangeSqOnFollow() + expandRangeSq);
	}

}
