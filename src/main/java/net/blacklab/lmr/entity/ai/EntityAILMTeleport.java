package net.blacklab.lmr.entity.ai;

import net.blacklab.lib.minecraft.vector.VectorUtil;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import scala.util.Random;

public class EntityAILMTeleport extends EntityAIBase implements IEntityAILM {
	
	private EntityLittleMaid theMaid;
	private boolean enabled = true;
	
	public EntityAILMTeleport(EntityLittleMaid pMaid) {
		theMaid = pMaid;
	}

	@Override
	public void setEnable(boolean pFlag) {
		enabled = pFlag;
	}

	@Override
	public boolean getEnable() {
		return enabled;
	}

	@Override
	public boolean shouldExecute() {
		if (theMaid == null) {
			return false;
		}
		if (theMaid.getActiveModeClass() == null || theMaid.getMaidMasterEntity() == null ||
				theMaid.isFreedom() || theMaid.isMaidWait() || theMaid.isSitting() || theMaid.isWorkingDelay()) {
			return false;
		}
		
		// If this maid gets too far from her master:
		if (theMaid.getDistanceSqToEntity(theMaid.getMaidMasterEntity()) >= theMaid.getActiveModeClass().getLimitRangeSqOnFollow()) {
			return true;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		theMaid.getNavigator().clearPathEntity();
	}
	
	@Override
	public void updateTask() {
		// Select safe place
		
		EntityPlayer lMaster = theMaid.getMaidMasterEntity();
		if (lMaster != null) {
			BlockPos lMasterPos = lMaster.getPosition();
			System.out.println("MASTER=" + lMasterPos.toString());

			int x, y, z, i = 0;
			BlockPos[] lCoordinates = new BlockPos[3];
			for (z = -2; z <= 2 && i < 3; z++) {
				for (x = -2; x <= 2 && i < 3; x++) {
					for (y = -1; y <= 1 && i < 3; y++) {
						if (x == 0 && y == 0 && z == 0) {
							continue;
						}
						BlockPos tPos = lMasterPos.add(x, y, z);
						IBlockState tGround = theMaid.worldObj.getBlockState(tPos.add(0,-1,0)),
								tFeet = theMaid.worldObj.getBlockState(tPos),
								tHead = theMaid.worldObj.getBlockState(tPos.add(0, 1, 0));
						if (tGround.getMaterial().isSolid() &&
								tFeet.getMaterial().isReplaceable() &&
								!tHead.getMaterial().isOpaque() &&
								VectorUtil.canBlockBeSeen(lMaster, tPos.getX(), tPos.getY(), tPos.getZ(), false, true, false)) {
							lCoordinates[i++] = tPos;
						}
					}
				}
			}
			if (i > 0) {
				BlockPos tDest = lCoordinates[new Random().nextInt(i)];
				theMaid.setPosition(tDest.getX(), tDest.getY(), tDest.getZ());
			}
		}
	}
	
	@Override
	public void resetTask() {
		theMaid.getNavigator().clearPathEntity();
	}

}
