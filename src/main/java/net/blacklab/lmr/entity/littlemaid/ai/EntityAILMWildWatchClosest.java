package net.blacklab.lmr.entity.littlemaid.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntityAILMWildWatchClosest extends EntityAILMWatchClosest {

	public EntityAILMWildWatchClosest(EntityLiving entitylivingIn,
			Class<? extends Entity> watchTargetClass, float maxDistance, float f) {
		super(entitylivingIn, watchTargetClass, maxDistance);
		setMutexBits(3);
	}

}
