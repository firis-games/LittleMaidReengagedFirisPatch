package littleMaidMobX;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class LMM_EntityAIBegMove extends EntityAIBase {

	private LMM_EntityLittleMaid theMaid;
	private EntityPlayer thePlayer;
	private float moveSpeed;
	
	public LMM_EntityAIBegMove(LMM_EntityLittleMaid pEntityLittleMaid, float pmoveSpeed) {
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
	public boolean continueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void updateTask() {
		// 不具合対応。
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=220
		// 這い寄れ！
		if (theMaid.aiBeg.getDistanceSq() < 3.5D || thePlayer==null) {
			theMaid.getNavigator().clearPathEntity();
		} else {
			theMaid.getNavigator().tryMoveToEntityLiving(thePlayer, moveSpeed);
		}
	}
}
