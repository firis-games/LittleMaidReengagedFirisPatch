package littleMaidMobX;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;

public class LMM_EntityAIWander extends EntityAIWander implements LMM_IEntityAI {

	protected LMM_EntityLittleMaid theMaid;
	protected boolean isEnable;
	
	public LMM_EntityAIWander(EntityCreature par1EntityCreature, float par2) {
		super(par1EntityCreature, par2);
		theMaid = (LMM_EntityLittleMaid) par1EntityCreature;
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
