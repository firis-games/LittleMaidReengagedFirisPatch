package net.blacklab.lmr.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAILMWatchClosest extends EntityAIWatchClosest {

	public EntityAILMWatchClosest(EntityLiving entitylivingIn,
			Class<? extends Entity> watchTargetClass, float maxDistance,
			float chanceIn) {
		super(entitylivingIn, watchTargetClass, maxDistance, chanceIn);
	}

	public EntityAILMWatchClosest(EntityLiving entitylivingIn,
			Class<? extends Entity> watchTargetClass, float maxDistance) {
		super(entitylivingIn, watchTargetClass, maxDistance);
	}

	@Override
	public boolean continueExecuting() {
		// サイレンサー
		try {
			return super.continueExecuting();
		} catch (NullPointerException exception) {}
		return false;
	}

}
