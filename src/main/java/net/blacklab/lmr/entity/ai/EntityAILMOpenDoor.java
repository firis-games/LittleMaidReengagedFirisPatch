package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;

public class EntityAILMOpenDoor extends EntityAIOpenDoor {

	protected EntityLittleMaid theMaid;

	public EntityAILMOpenDoor(EntityLiving p_i1644_1_, boolean p_i1644_2_) {
		super(p_i1644_1_, p_i1644_2_);
		// TODO 自動生成されたコンストラクター・スタブ
		if(p_i1644_1_ instanceof EntityLittleMaid)
			theMaid = (EntityLittleMaid) p_i1644_1_;
	}

	@Override
	public boolean continueExecuting() {
		// TODO 自動生成されたメソッド・スタブ
		LittleMaidReengaged.Debug("DOOR CONTINUE");
		return super.continueExecuting();
	}

	@Override
	public void startExecuting() {
		// TODO 自動生成されたメソッド・スタブ
		LittleMaidReengaged.Debug("DOOR START");
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		// TODO 自動生成されたメソッド・スタブ
		LittleMaidReengaged.Debug("DOOR RESET");
		super.resetTask();
	}

	@Override
	public void updateTask() {
		// TODO 自動生成されたメソッド・スタブ
		LittleMaidReengaged.Debug("DOOR UPDATE");
		super.updateTask();
	}

	@Override
	public boolean shouldExecute() {
		if(theMaid==null) return false;
		if(theMaid.isInWater()) return false;
		return super.shouldExecute();
	}

}
