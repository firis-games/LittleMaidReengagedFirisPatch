package net.blacklab.lmr.entity.littlemaid.mode;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ai.motion.EntityAILMWildWatchClosest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 死亡から復活用モード（未使用に変更）
 *
 */
public class EntityMode_DeathWait extends EntityModeBase {
	
	public static final String mmode_DeathWait = "Fatal"; 

	public EntityMode_DeathWait(EntityLittleMaid pEntity) {
		super(pEntity);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int priority() {
		// TODO 自動生成されたメソッド・スタブ
		return -1;
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// TODO 自動生成されたメソッド・スタブ
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(null);
		ltasks[1] = new EntityAITasks(null);

		ltasks[0].addTask(1, new EntityAILMWildWatchClosest(owner, EntityLivingBase.class, 10F, 0.02F));
		ltasks[0].addTask(2, new EntityAILMWildWatchClosest(owner, EntityLittleMaid.class, 10F, 0.02F));
		ltasks[0].addTask(2, new EntityAILMWildWatchClosest(owner, EntityPlayer.class, 10F, 0.02F));
		ltasks[0].addTask(2, new EntityAILookIdle(owner));

		owner.addMaidMode(mmode_DeathWait, ltasks);
	}

}
