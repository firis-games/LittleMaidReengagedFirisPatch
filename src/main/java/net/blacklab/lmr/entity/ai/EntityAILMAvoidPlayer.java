package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.Vec3d;

public class EntityAILMAvoidPlayer extends EntityAIBase implements
		IEntityAILM {

	/** The entity we are attached to */
	protected EntityLittleMaid theMaid;
	protected EntityPlayer theMaster;
	protected float speedNormal;
	protected Path avoidPath;
	/** The PathNavigate of our entity */
	protected PathNavigate entityPathNavigate;
	protected boolean isEnable;

	public boolean isActive;
	public int minDist;

	public EntityAILMAvoidPlayer(EntityLittleMaid pEntityLittleMaid,
			float pSpeed, int pMinDist) {
		theMaid = pEntityLittleMaid;
		speedNormal = pSpeed;
		entityPathNavigate = pEntityLittleMaid.getNavigator();
		isActive = false;
		isEnable = false;
		minDist = pMinDist;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!isEnable || !isActive || !theMaid.isContract()) {
			isActive = false;
			return false;
		}

		theMaster = theMaid.getMaidMasterEntity();

		// 不具合対策：プレイヤーがログアウトすると theMaster がNULLになって以降の処理でクラッシュ
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=180#p211806
		if(theMaster==null)
		{
			return false;
		}

		// 対象は見えるか？てかこれいらなくね？
		if (!theMaid.getEntitySenses().canSee(theMaster)) {
			return false;
		}

		// 移動先を策定
		Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
				theMaid, minDist, 7, new Vec3d(theMaster.posX,
						theMaster.posY, theMaster.posZ));

		// 移動先が無い
		if (vec3d == null) {
			return false;
		}
		// 移動先の距離が近い
		if (theMaster.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < theMaid.getDistanceSqToMaster()) {
			return false;
		}

		avoidPath = entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);

		if (avoidPath == null) {
			return false;
		}

		PathPoint pathpoint = avoidPath.getFinalPathPoint();
		return pathpoint == null ? false : pathpoint.xCoord == (int)vec3d.xCoord && pathpoint.zCoord == (int)vec3d.zCoord;
	}

	@Override
	public boolean continueExecuting() {
		if(theMaster==null) return false;
		return !entityPathNavigate.noPath() && theMaid.getDistanceSqToEntity(theMaster) < 144D;
	}

	@Override
	public void startExecuting() {
		entityPathNavigate.setPath(avoidPath, speedNormal);
	}

	@Override
	public void resetTask() {
		isActive = false;
	}

	public void setActive() {
		// 動作開始
		isActive = true;
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
