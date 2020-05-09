package net.blacklab.lmr.entity.littlemaid.ai.move;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ai.IEntityAILM;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import scala.util.Random;

/**
 * メイドさんがご主人さまから離れたらテレポート
 * @author firis-games
 *
 */
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
		
		//20tickに1回判断を行う
		if (theMaid.ticksExisted % 20 != 0) return false; 
		
		if (theMaid.jobController.getActiveModeClass() == null || theMaid.getMaidMasterEntity() == null ||
//				theMaid.isFreedom() || theMaid.isMaidWait() || theMaid.isSitting() || theMaid.isWorkingDelay()) {
				theMaid.isFreedom() || theMaid.isMaidWait() || theMaid.isSitting() || theMaid.jobController.isWorkingDelay()) {
			return false;
		}
		
		// If this maid gets too far from her master:
		if (theMaid.getDistanceSq(theMaid.getMaidMasterEntity()) >= theMaid.jobController.getActiveModeClass().getLimitRangeSqOnFollow()) {
			return true;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		theMaid.getNavigator().clearPath();
	}
	
	@Override
	public void updateTask() {
		// Select safe place
		
		EntityPlayer lMaster = theMaid.getMaidMasterEntity();
		if (lMaster != null) {
			
			BlockPos lMasterPos = lMaster.getPosition();
			System.out.println("MASTER=" + lMasterPos.toString());
			
			List<BlockPos> posList = new ArrayList<>();
			
			//5x5のプレイヤーの範囲パスを取得
			for (BlockPos addPos : BlockPos.getAllInBox(-2, -1, -2, 2, 2, 2)) {
				
				BlockPos tPos = lMasterPos.add(addPos);

				//player座標は無視
				if (tPos.equals(lMasterPos)) {
					continue;
				}
				
				//テレポートブロック判定
				if (isTeleportArea(tPos)) {
					posList.add(tPos);
				}
			}
			
			//テレポート
			if (posList.size() > 0) {
				BlockPos tDest = posList.get(new Random().nextInt(posList.size()));
				theMaid.setPosition(
						(double)(tDest.getX()) + 0.5D, 
						tDest.getY(), 
						((double)tDest.getZ()) + 0.5D);
				
				resetTask();
			}
		}
	}
	
	@Override
	public void resetTask() {
		theMaid.getNavigator().clearPath();
		theMaid.setAttackTarget(null);
//		theMaid.getWorkingCount().setValue(0);
		theMaid.jobController.setEndWorking();
	}
	
	
	/**
	 * 対象の座標がテレポート可能か判断する
	 * @param pos
	 * @return
	 */
	private boolean isTeleportArea(BlockPos pos) {
		
		//基準座標から3×3の範囲も同条件を満たしているかを判断する
		for (BlockPos cPos : BlockPos.getAllInBox(-1, 0, -1, 1, 0, 1)) {
			if (!isTeleportBlockPos(pos.add(cPos))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * isTeleportAreaチェック用
	 * BlockPos単位で判断を行う
	 * @param pos
	 * @return
	 */
	private boolean isTeleportBlockPos(BlockPos pos) {
		
		IBlockState tGround = theMaid.getEntityWorld().getBlockState(pos.add(0, -1, 0));
		IBlockState tFeet = theMaid.getEntityWorld().getBlockState(pos);
		IBlockState tHead = theMaid.getEntityWorld().getBlockState(pos.add(0, 1, 0));
		
		//下のブロックが固体ブロックか水の場合だけ許可
		//足元が置き換え可能なブロックか
		//頭の部分が空気ブロックか
		if ((tGround.getMaterial().isSolid() || tGround.getBlock() == Blocks.WATER)
				&& tFeet.getMaterial().isReplaceable()
				&& tHead.getMaterial() == Material.AIR) {
			return true;
		}
		
		return false;
	}
	
}
