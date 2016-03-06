package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;

public class LMMNX_EntityAIRestrictOpenDoor extends EntityAIRestrictOpenDoor {
	
	protected EntityLittleMaid theMaid;

	public LMMNX_EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_) {
		super(p_i1651_1_);
		// TODO 自動生成されたコンストラクター・スタブ
		if(p_i1651_1_ instanceof EntityLittleMaid)
			theMaid = (EntityLittleMaid) p_i1651_1_;
	}

	@Override
	public void startExecuting() {
		// TODO 自動生成されたメソッド・スタブ
		if(theMaid==null) return;
		if(theMaid.isSwimmingEnabled()||theMaid.isInWater()) return;
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		// TODO 自動生成されたメソッド・スタブ
		if(theMaid==null) return;
		if(theMaid.isSwimmingEnabled()||theMaid.isInWater()) return;
		super.resetTask();
	}

}
