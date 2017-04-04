package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;

public class EntityAILMMoveTowardsRestriction extends EntityAIMoveTowardsRestriction implements IEntityAILM {

	private EntityLittleMaid theMaid;
	private boolean enabled = false;

	public EntityAILMMoveTowardsRestriction(EntityLittleMaid par1EntityLittleMaid, double pSpeed) {
		super(par1EntityLittleMaid, pSpeed);
		theMaid = par1EntityLittleMaid;
		enabled = false;
	}

	@Override
	public boolean shouldExecute() {
		return !theMaid.isMaidWait() && enabled && !theMaid.aiBegMove.shouldExecute() && super.shouldExecute();
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
