package net.blacklab.lmr.entity.ai;

import java.util.Random;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAILMFleeRain extends EntityAIBase implements IEntityAILM {

	protected EntityCreature theCreature;
	protected EntityLittleMaid theMaid;
	protected double shelterX;
	protected double shelterY;
	protected double shelterZ;
	protected float movespeed;
	protected World theWorld;
	protected boolean isEnable;

	public EntityAILMFleeRain(EntityCreature par1EntityCreature, float pMoveSpeed) {
		theCreature = par1EntityCreature;
		if (theCreature instanceof EntityLittleMaid) {
			theMaid = (EntityLittleMaid) theCreature;
		}
		movespeed = pMoveSpeed;
		theWorld = par1EntityCreature.getEntityWorld();
		isEnable = false;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!isEnable || !theWorld.isRaining()) {
			return false;
		}

		if (!theMaid.isWet()) {
			return false;
		}
		
		if (theMaid.isTracer()) {
			return false;
		}

		if (!theWorld.canBlockSeeSky(theMaid.getPosition())) {
			return false;
		}

		Vec3d vec3d = findPossibleShelter();

		if (vec3d == null) {
			return false;
		}
		shelterX = vec3d.x;
		shelterY = vec3d.y;
		shelterZ = vec3d.z;
		LittleMaidReengaged.Debug("SHELTER FOUND %04.2f,%04.2f,%04.2f", shelterX, shelterY, shelterZ);
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return theMaid.getNavigator().noPath() ? false :
			theWorld.canBlockSeeSky(theMaid.getPosition());
	}

	@Override
	public void startExecuting() {
		LittleMaidReengaged.Debug("EXECUTE %04.2f,%04.2f,%04.2f", shelterX, shelterY, shelterZ);
		theMaid.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, movespeed);
	}

	private Vec3d findPossibleShelter() {
		Random random = theMaid.getRNG();
		
		for (int i = 0; i < 10; i++) {
			int j = MathHelper.floor((theMaid.posX + (i-5)));
			int k = MathHelper.floor((theMaid.getEntityBoundingBox().minY +
					random.nextInt(4)) - 2D);
			int l = MathHelper.floor((theMaid.posZ + (i-5)));
			
			//離れすぎている
			if(theMaid.isFreedom() && !MaidHelper.isTargetReachable(theMaid, new Vec3d(j, k, l), 0)){
				continue;
			}
			
			if (!theWorld.canBlockSeeSky(new BlockPos(j, k, l))/*
					&& theMaid.getBlockPathWeight(j, k, l) > -0.5F*/) {
				return new Vec3d(j, k, l);
			}
		}
		
		return null;
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
