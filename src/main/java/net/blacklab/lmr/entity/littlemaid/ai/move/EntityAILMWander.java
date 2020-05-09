package net.blacklab.lmr.entity.littlemaid.ai.move;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ai.IEntityAILM;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;

/**
 * 自由行動時にいろんなところに向かって歩くメイドさん
 * @author firis-games
 *
 */
public class EntityAILMWander extends EntityAIWander implements IEntityAILM {

	protected EntityLittleMaid theMaid;
	protected boolean isEnable;
	
	public EntityAILMWander(EntityCreature par1EntityCreature, float par2) {
		super(par1EntityCreature, par2);
		theMaid = (EntityLittleMaid) par1EntityCreature;
		isEnable = false;
	}

	@Override
	public boolean shouldExecute() {
		return isEnable && super.shouldExecute() && !theMaid.isMaidWaitEx();
	}
	
	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}
	
}
