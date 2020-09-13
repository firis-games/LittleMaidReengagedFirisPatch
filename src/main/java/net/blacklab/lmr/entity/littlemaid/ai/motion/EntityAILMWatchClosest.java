package net.blacklab.lmr.entity.littlemaid.ai.motion;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;

/**
 * メイドさんが近くのモブを見つめる
 * @author firis-games
 *
 */
public class EntityAILMWatchClosest extends EntityAIWatchClosest {

	protected EntityLittleMaid littleMaid;
	
	public EntityAILMWatchClosest(EntityLiving entitylivingIn,
			Class<? extends Entity> watchTargetClass, float maxDistance,
			float chanceIn) {
		super(entitylivingIn, watchTargetClass, maxDistance, chanceIn);
	}

	public EntityAILMWatchClosest(EntityLittleMaid entitylivingIn,
			Class<? extends Entity> watchTargetClass, float maxDistance) {
		super(entitylivingIn, watchTargetClass, maxDistance);
		littleMaid = entitylivingIn;
	}
	
	@Override
	public boolean shouldExecute() {
		//睡眠中は無効化
		if (littleMaid.isSleep()) {
			return false;
		}
		return super.shouldExecute();
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
