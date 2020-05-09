package net.blacklab.lmr.entity.littlemaid.ai.motion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

/**
 * 野生のメイドさんが近くのモブを見つめる
 * @author firis-games
 *
 *　野生のメイドさんは止まっている時だけ見つめるのでsetMutexBitsが通常メイドさんと違う設定になっている
 */
public class EntityAILMWildWatchClosest extends EntityAILMWatchClosest {

	public EntityAILMWildWatchClosest(EntityLiving entitylivingIn,
			Class<? extends Entity> watchTargetClass, float maxDistance, float f) {
		super(entitylivingIn, watchTargetClass, maxDistance);
		setMutexBits(3);
	}

}
