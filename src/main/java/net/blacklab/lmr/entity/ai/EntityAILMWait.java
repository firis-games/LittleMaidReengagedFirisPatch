package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAISit;

public class EntityAILMWait extends EntityAISit {

	public EntityLittleMaid theMaid;


	public EntityAILMWait(EntityLittleMaid pEntity) {
		super(pEntity);
		this.setMutexBits(5);

		theMaid = pEntity;
	}

	@Override
	public boolean shouldExecute() {
		return theMaid.isMaidWaitEx() || (!theMaid.isFreedom() && theMaid.getMaidMasterEntity() == null);
	}

}
