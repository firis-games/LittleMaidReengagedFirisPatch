package net.blacklab.lmmnx.entity.ai;

import littleMaidMobX.LMM_EntityLittleMaid;
import littleMaidMobX.LMM_LittleMaidMobNX;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;

public class LMMNX_EntityAIOpenDoor extends EntityAIOpenDoor {
	
	protected LMM_EntityLittleMaid theMaid;

	public LMMNX_EntityAIOpenDoor(EntityLiving p_i1644_1_, boolean p_i1644_2_) {
		super(p_i1644_1_, p_i1644_2_);
		// TODO 自動生成されたコンストラクター・スタブ
		if(p_i1644_1_ instanceof LMM_EntityLittleMaid)
			theMaid = (LMM_EntityLittleMaid) p_i1644_1_;
	}

	@Override
	public boolean continueExecuting() {
		// TODO 自動生成されたメソッド・スタブ
		LMM_LittleMaidMobNX.Debug("DOOR CONTINUE");
		return super.continueExecuting();
	}

	@Override
	public void startExecuting() {
		// TODO 自動生成されたメソッド・スタブ
		LMM_LittleMaidMobNX.Debug("DOOR START");
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		// TODO 自動生成されたメソッド・スタブ
		LMM_LittleMaidMobNX.Debug("DOOR RESET");
		super.resetTask();
	}

	@Override
	public void updateTask() {
		// TODO 自動生成されたメソッド・スタブ
		LMM_LittleMaidMobNX.Debug("DOOR UPDATE");
		super.updateTask();
	}

	@Override
	public boolean shouldExecute() {
		if(theMaid==null) return false;
		if(theMaid.isSwimmingEnabled() && theMaid.isInWater()) return false;
		return super.shouldExecute();
	}
	
}
