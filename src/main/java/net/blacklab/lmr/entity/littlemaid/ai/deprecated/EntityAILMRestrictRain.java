package net.blacklab.lmr.entity.littlemaid.ai.deprecated;

import net.blacklab.lmr.entity.littlemaid.ai.IEntityAILM;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

@Deprecated
public class EntityAILMRestrictRain extends EntityAIBase implements
		IEntityAILM {

	protected EntityLiving theEntity;
	protected boolean isEnable;

	public EntityAILMRestrictRain(EntityLiving par1EntityLiving) {
		theEntity = par1EntityLiving;
		isEnable = false;
	}

	@Override
	public boolean shouldExecute() {
		return isEnable && theEntity.getEntityWorld().isRaining();
	}

	@Override
	public void startExecuting() {
		//theEntity.getNavigator().setAvoidSun(true);
	}

	@Override
	public void resetTask() {
		//theEntity.getNavigator().setAvoidSun(false);
	}

	// 実行可能フラグ
	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

}
