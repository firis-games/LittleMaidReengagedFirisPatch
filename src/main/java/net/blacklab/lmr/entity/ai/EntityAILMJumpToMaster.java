package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class EntityAILMJumpToMaster extends EntityAIBase implements IEntityAI {

	protected EntityLittleMaid theMaid;
	protected EntityLivingBase theOwner;
	protected World theWorld;
	protected boolean isEnable;
	private boolean jumpTarget;
	protected AxisAlignedBB boundingBox;

	public EntityAILMJumpToMaster(EntityLittleMaid pEntityLittleMaid) {
		super();
		
		theMaid = pEntityLittleMaid;
		theWorld = pEntityLittleMaid.worldObj;
		isEnable = true;
		boundingBox = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	public boolean shouldExecute() {
		if (!isEnable || !theMaid.isContractEX() || theMaid.isMaidWaitEx()) {
			// 契約個体のみが跳ぶ
			return false;
		}
		if (theMaid.getLeashed()) {
			// 括られているなら跳ばない
			return false;
		}
		if (theMaid.isFreedom()) {
			// 自由行動の子は基点へジャンプ
			if (theMaid.homeWorld != theMaid.dimension) {
				LittleMaidReengaged.Debug(String.format("ID:%d, %d -> %d, Change HomeWorld. reset HomePosition.",
						theMaid.getEntityId(),theMaid.homeWorld, theMaid.worldObj.provider.getDimensionId()));
//				theMaid.func_110171_b(
				theMaid.setHomePosAndDistance(theMaid.getPosition(), (int) theMaid.getMaximumHomeDistance());
				return false;
			}
			
			if (!theMaid.isWithinHomeDistanceCurrentPosition()) {
				jumpTarget = false;
				LittleMaidReengaged.Debug(String.format(
						"ID:%d(%s) Jump To Home.", theMaid.getEntityId(),
						theMaid.worldObj.isRemote ? "C" : "W"));
				return true;
			}
			
		} else {
			jumpTarget = true;
			theOwner = theMaid.getMaidMasterEntity();
			if (theMaid.getAttackTarget() == null) {
				if (theMaid.getDistanceSqToMaster() < 144D) {
					return false;
				}
				//theMaid.setPosition(theMaid.getMaidMasterEntity().posX, theMaid.getMaidMasterEntity().posY, theMaid.getMaidMasterEntity().posZ);
			} else {
				// ターゲティング中は距離が伸びる
				if (theMaid.getDistanceSqToMaster() < (theMaid.isBloodsuck() ? 1024D : 256D)) {
					return false;
				}
			}
			LittleMaidReengaged.Debug(
					"ID:%d(%s) Jump To Master.",
					theMaid.getEntityId(), theMaid.worldObj.isRemote ? "C" : "W");
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		if (jumpTarget) {
			int i = theOwner.getPosition().getX() - 2;
			int j = theOwner.getPosition().getZ() - 2;
			int k = MathHelper.floor_double(theOwner.getEntityBoundingBox().minY);
			
			for (int l = 0; l <= 4; l++) {
				for (int i1 = 0; i1 <= 4; i1++) {
					if ((l < 1 || i1 < 1 || l > 3 || i1 > 3)
							&& theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).getBlock().isNormalCube()
							&& !theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getBlock().isNormalCube()
							&& !theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getBlock().isNormalCube()) {
						// 主の前に跳ばない
						double dd = theOwner.getDistanceSq(
								i + l + 0.5D + MathHelper.sin(theOwner.rotationYaw * 0.01745329252F) * 2.0D,
								k,
								j + i1 - MathHelper.cos(theOwner.rotationYaw * 0.01745329252F) * 2.0D);
						if (dd > 8D) {
//							theMaid.setTarget(null);
//							theMaid.setRevengeTarget(null);
//							theMaid.setAttackTarget(null);
//							theMaid.getNavigator().clearPathEntity();
							theMaid.setLocationAndAngles(
									i + l + 0.5F, k, j + i1 + 0.5F,
									theMaid.rotationYaw, theMaid.rotationPitch);
							theMaid.onWarp();
							return;
						}
					}
				}
			}
		} else {
			// ホームポジションエリア外で転移
			int lx = theMaid.getHomePosition().getX();
			int ly = theMaid.getHomePosition().getY();
			int lz = theMaid.getHomePosition().getZ();
			if (!(isCanJump(lx, ly, lz))) {
				// ホームポジション消失
				LittleMaidReengaged.Debug(String.format(
						"ID:%d(%s) home lost.",
						theMaid.getEntityId(), theMaid.worldObj.isRemote ? "C" : "W"));
				int a;
				int b;
				// int c;
				boolean f = false;
				// ｙ座標で地面を検出
				for (a = 1; a < 6 && !f; a++) {
					if (isCanJump(lx, ly + a, lz)) {
						f = true;
						ly += a;
						break;
					}
				}
				for (a = -1; a > -6 && !f; a--) {
					if (isCanJump(lx, ly + a, lz)) {
						f = true;
						ly += a;
						break;
					}
				}

				// CW方向に検索領域を広げる
				loop_search: for (a = 2; a < 18 && !f; a += 2) {
					lx--;
					lz--;
					for (int c = 0; c < 4; c++) {
						for (b = 0; b <= a; b++) {
							// N
							if (isCanJump(lx, ly + a, lz)) {
								f = true;
								break loop_search;
							}
							if (c == 0)
								lx++;
							else if (c == 1)
								lz++;
							else if (c == 2)
								lx--;
							else if (c == 3)
								lz--;
						}
					}
				}
				if (f) {
//					theMaid.func_110171_b(lx, ly, lz, (int) theMaid.func_110174_bM());
//					theMaid.setHomePosAndDistance(new BlockPos(lx, ly, lz), (int) theMaid.getMaximumHomeDistance());
					LittleMaidReengaged.Debug(String.format(
							"Find new position:%d, %d, %d.", lx, ly, lz));
				} else {
					if (isCanJump(lx, ly - 6, lz)) {
						ly -= 6;
					}
					LittleMaidReengaged.Debug(String.format(
							"loss new position:%d, %d, %d.", lx, ly, lz));
				}
			} else {
				LittleMaidReengaged.Debug(String.format(
						"ID:%d(%s) home solid.",
						theMaid.getEntityId(), theMaid.worldObj.isRemote ? "C" : "W"));
			}
			
//			theMaid.setTarget(null);
//			theMaid.setAttackTarget(null);
//			theMaid.getNavigator().clearPathEntity();
			theMaid.setLocationAndAngles(lx + 0.5D, ly, lz + 0.5D,
					theMaid.rotationYaw, theMaid.rotationPitch);
			theMaid.onWarp();
			return;
		}
		
		//theMaid.setTarget(null);
		theMaid.setAttackTarget(null);
		theMaid.setRevengeTarget(null);
		theMaid.getNavigator().clearPathEntity();
		LittleMaidReengaged.Debug(String.format("ID:%d(%s) Jump Fail.",
				theMaid.getEntityId(), theMaid.worldObj.isRemote ? "C" : "W"));
	}

	/**
	 * 転移先のチェック
	 */
	protected boolean isCanJump(double px, double py, double pz) {
		return theWorld.getBlockState(new BlockPos(px, py - 1, pz)).getBlock().getMaterial().isSolid()/*
				&& theWorld.func_147461_a(boundingBox).isEmpty()*/;
	}

	@Override
	public boolean continueExecuting() {
		return false;
	}

	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

	@Override
	public boolean isInterruptible() {
		return true;
	}

}
