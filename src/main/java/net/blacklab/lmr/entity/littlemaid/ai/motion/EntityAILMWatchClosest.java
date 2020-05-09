package net.blacklab.lmr.entity.littlemaid.ai.motion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

/**
 * メイドさんが近くのモブを見つめる
 * @author firis-games
 *
 */
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
	public boolean shouldContinueExecuting() {
		// サイレンサー
		try {
			return super.shouldContinueExecuting();
		} catch (NullPointerException exception) {}
		return false;
	}

}
