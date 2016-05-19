package net.blacklab.lmr.util.helper;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.Entity;
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
	
	public static boolean isTargetReachable(EntityLittleMaid pMaid, Entity pTarget, double expandRangeSq) {
		return isTargetReachable(pMaid, pTarget.getPositionVector(), expandRangeSq);
	}
	
	public static boolean isTargetReachable(EntityLittleMaid pMaid, Vec3d pTarget, double expandRangeSq) {
		return pMaid.isFreedom() ?
				pMaid.getHomePosition().distanceSq(pTarget.xCoord, pTarget.yCoord, pTarget.zCoord) <= pMaid.getActiveModeClass().getFreedomTrackingRangeSq() + expandRangeSq :
				(pMaid.getMaidMasterEntity() == null ? true : pMaid.getMaidMasterEntity().getPositionVector().squareDistanceTo(pTarget) <= pMaid.getActiveModeClass().getLimitRangeSqOnFollow() + expandRangeSq);
	}

}
