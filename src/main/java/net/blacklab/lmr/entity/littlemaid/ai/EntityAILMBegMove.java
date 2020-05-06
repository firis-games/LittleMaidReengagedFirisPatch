package net.blacklab.lmr.entity.littlemaid.ai;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILMBegMove extends EntityAIBase {

	private EntityLittleMaid theMaid;
	private EntityPlayer thePlayer;
	private float moveSpeed;

	public EntityAILMBegMove(EntityLittleMaid pEntityLittleMaid, float pmoveSpeed) {
		theMaid = pEntityLittleMaid;
		moveSpeed = pmoveSpeed;

		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		return theMaid.isLookSuger() && !theMaid.isMaidWait();
	}

	@Override
	public void startExecuting() {
		thePlayer = theMaid.aiBeg.getPlayer();
	}

	@Override
	public void resetTask() {
		thePlayer = null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}

	@Override
	public void updateTask() {
		// 不具合対応。
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=220
		// 這い寄れ！
		if (theMaid.aiBeg.getDistanceSq() < 3.5D || thePlayer==null) {
			theMaid.getNavigator().clearPath();
		} else {
			theMaid.getNavigator().tryMoveToEntityLiving(thePlayer, moveSpeed);
		}
	}
}
