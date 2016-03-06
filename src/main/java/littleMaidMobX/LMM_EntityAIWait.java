package littleMaidMobX;

import net.minecraft.entity.ai.EntityAISit;

public class LMM_EntityAIWait extends EntityAISit {

	public LMM_EntityLittleMaid theMaid;


	public LMM_EntityAIWait(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		this.setMutexBits(5);

		theMaid = pEntity;
	}

	@Override
	public boolean shouldExecute() {
		return theMaid.isMaidWaitEx() || (!theMaid.isFreedom() && theMaid.mstatMasterEntity == null);
	}

}
