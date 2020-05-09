package net.blacklab.lmr.entity.littlemaid.ai;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAISit;

/**
 * メイドさんの待機
 * @author firis-games
 *
 */
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
