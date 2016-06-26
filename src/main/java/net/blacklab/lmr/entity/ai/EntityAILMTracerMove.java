package net.blacklab.lmr.entity.ai;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.EntityMarkerDummy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAILMTracerMove extends EntityAIBase implements IEntityAI {

	protected EntityLittleMaid theMaid;
	protected World world;
	protected boolean isEnable;
	protected int tileX;
	protected int tileY;
	protected int tileZ;


	public EntityAILMTracerMove(EntityLittleMaid pEntityLittleMaid) {
		theMaid = pEntityLittleMaid;
		world = pEntityLittleMaid.worldObj;
		isEnable = false;

		setMutexBits(1);
	}

	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public void updateTask() {
		doExecute();
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

	protected boolean canUpdate() {
		return isEnable && theMaid.isTracer() && theMaid.isFreedom();
	}

	@Override
	public boolean shouldExecute() {
		return canUpdate() && !theMaid.isMaidWaitEx() && theMaid.getNavigator().noPath();

	}

	@Override
	public boolean continueExecuting() {
		return shouldExecute() || !theMaid.getNavigator().noPath();
	}

	protected void doExecute() {
		// ルート策定
		// ターゲットをサーチ
		int ox = MathHelper.floor_double(theMaid.posX);
		int oy = MathHelper.floor_double(theMaid.posY);
		int oz = MathHelper.floor_double(theMaid.posZ);
		int vt = MathHelper.floor_float(((theMaid.rotationYawHead * 4F) / 360F) + 2.5F) & 3;
		int xx = ox;
		int yy = oy;
		int zz = oz;
		double lrange = Double.MAX_VALUE;

		// TODO:Dummy
		EntityMarkerDummy.clearDummyEntity(theMaid);
		boolean flagdammy = false;

		// CW方向に検索領域を広げる
		for (int d = 0; d < 4; d++) {
			for (int a = 2; a < 14; a += 2) {
				int del = a / 2;
				if (vt == 0) {
					xx = ox - del;
					zz = oz - del;
				}
				else if (vt == 1) {
					xx = ox + del;
					zz = oz - del;
				}
				else if (vt == 2) {
					xx = ox + del;
					zz = oz + del;
				}
				else if (vt == 3) {
					xx = ox - del;
					zz = oz + del;
				}
				// TODO:Dummay
				if (!flagdammy) {
					EntityMarkerDummy.setDummyEntity(theMaid, 0x00ff4f4f, xx, oy, zz);
					flagdammy = true;
				}
				int b = 0;
				do {
					for (int c = 0; c < 3; c++) {
						yy = oy + (c == 2 ? -1 : c);
						if (checkBlock(xx, yy, zz)) {
							// 最も近いポイントの判定
							double lr = theMaid.getDistanceSq(xx, yy, zz);
							if (lr < lrange) {
								if (doFindBlock(xx, yy, zz)) {
									lrange = lr;
									tileX = xx;
									tileY = yy;
									tileZ = zz;
//									theMaid.func_110171_b(xx, yy, zz, 16);
									theMaid.setHomePosAndDistance(new BlockPos(xx, yy, zz), -1);
									// TODO:Dummy
									EntityMarkerDummy.setDummyEntity(theMaid, 0x004f4fff, xx, yy, zz);
									flagdammy = true;
									return;
								}
							}
							// TODO:Dummay
							EntityMarkerDummy.setDummyEntity(theMaid, 0x004fff4f, xx, yy, zz);
							flagdammy = true;
						}
					}
					// TODO:Dummay
					if (!flagdammy) {
						EntityMarkerDummy.setDummyEntity(theMaid, 0x00ffffcf, xx, oy, zz);
						flagdammy = true;
					}
					// TODO:dammy
					flagdammy = false;

					if (vt == 0) {
						xx++;
					}
					else if (vt == 1) {
						zz++;
					}
					else if (vt == 2) {
						xx--;
					}
					else if (vt == 3) {
						zz--;
					}

				} while(++b < a);
			}
			vt = (vt + 1) & 3;
		}
	}

	/**
	 * 指定座標のブロックは探しているものか？
	 */
	protected boolean checkBlock(int px, int py, int pz) {
		IBlockState iState = world.getBlockState(new BlockPos(px, py + 1, pz));
		return world.isBlockPowered(new BlockPos(px,py,pz)) && (iState.getBlock().getMaterial(iState) == Material.AIR);
	}

	/**
	 * 見つけたブロックに対する動作。
	 * trueを返すとループ終了。
	 */
	protected boolean doFindBlock(int px, int py, int pz) {
		return theMaid.getNavigator().tryMoveToXYZ(px, py, pz, 1.0F);
//		return theMaid.getNavigator().tryMoveToXYZ(px, py, pz, theMaid.getAIMoveSpeed());
	}

}
