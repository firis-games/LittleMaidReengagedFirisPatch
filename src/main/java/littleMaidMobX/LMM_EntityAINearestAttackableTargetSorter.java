package littleMaidMobX;

import java.util.Comparator;

import net.minecraft.entity.Entity;

public class LMM_EntityAINearestAttackableTargetSorter implements Comparator {

	private Entity theEntity;

	public LMM_EntityAINearestAttackableTargetSorter(Entity par1Entity) {
		this.theEntity = par1Entity;
	}

	public int compareDistanceSq(Entity par1Entity, Entity par2Entity) {
		double var3 = this.theEntity.getDistanceSqToEntity(par1Entity);
		double var5 = this.theEntity.getDistanceSqToEntity(par2Entity);
		return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
	}

	public int compare(Object par1Obj, Object par2Obj) {
		return this.compareDistanceSq((Entity) par1Obj, (Entity) par2Obj);
	}

	public void setEntity(Entity pEntity) {
		theEntity = pEntity;
	}

}
