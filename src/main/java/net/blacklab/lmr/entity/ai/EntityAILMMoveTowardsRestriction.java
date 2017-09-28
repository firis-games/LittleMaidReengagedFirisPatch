package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;

public class EntityAILMMoveTowardsRestriction extends EntityAIMoveTowardsRestriction implements IEntityAILM {

	private EntityLittleMaid theMaid;
	private boolean enabled = false;

	public EntityAILMMoveTowardsRestriction(EntityLittleMaid par1EntityLittleMaid, double pSpeed) {
		super(par1EntityLittleMaid, pSpeed);
		theMaid = par1EntityLittleMaid;
		enabled = true;
	}

	@Override
	public boolean shouldExecute() {
		if (theMaid == null) {
			return false;
		}
		if (!theMaid.isTamed() || theMaid.isMaidWait() || theMaid.isSitting() || !theMaid.isFreedom()) {
			return false;
		}
		if (theMaid.isPlaying() && !theMaid.getNavigator().noPath()) {
			return false;
		}
		return !theMaid.aiBegMove.shouldExecute() && super.shouldExecute();
	}

	@Override
	public void setEnable(boolean pFlag) {
		enabled = pFlag;
	}

	@Override
	public boolean getEnable() {
		return enabled;
	}

}
