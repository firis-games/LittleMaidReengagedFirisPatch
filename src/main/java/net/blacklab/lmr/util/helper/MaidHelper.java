package net.blacklab.lmr.util.helper;

import java.util.List;

import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class MaidHelper {

	/**
	 * メイドにアイテムを与える
	 */
	public static void giveItem(ItemStack stack, EntityLittleMaid maid) {
		int stacksize = stack.stackSize;
		
		for (int i=0; i<maid.maidInventory.mainInventory.length; i++) {
			ItemStack stack1 = maid.maidInventory.mainInventory[i];
	
			if (stack1 != null && stack != null) {
				// スタックが空でない場合は合成を行う．
				if (stack.getItem() == stack1.getItem() && stack.getItemDamage() == stack1.getItemDamage()) {
					int totalsize = stack1.stackSize + stacksize;
					int diffsize = totalsize - stack1.getItem().getItemStackLimit(stack1);
					if (diffsize<=0) {
						stack1.stackSize = totalsize;
						stack = null;
					} else {
						stack1.stackSize = stack1.getItem().getItemStackLimit(stack1);
						stack.stackSize = diffsize;
					}
				}
			} else if (stack != null) {
				// スタックが空の場合は投入
				maid.maidInventory.mainInventory[i] = stack.copy();
				stack = null;
			} else {
				// 処理対象がなくなったらその時点でループを抜ける．
				break;
			}
		}
		
		// それでも残ってしまったらドロップ
		if (stack!=null) {
			maid.entityDropItem(stack, 0);
		}
	}

	public static boolean setPathToTile(EntityLiving pEntity, TileEntity pTarget, boolean flag) {
		// Tileまでのパスを作る
		PathNavigate lpn = pEntity.getNavigator();
		float lspeed = 1.0F;
		// 向きに合わせて距離を調整
		int i = (pTarget.getPos().getY() == MathHelper.floor_double(pEntity.posY) && flag) ? 2 : 1;
		switch (pEntity.worldObj.getBlockState(new BlockPos(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ())).getValue(BlockDirectional.FACING)) {
		case SOUTH:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ() + i, lspeed);
		case NORTH:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		case EAST:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX() + 1, pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		case WEST:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX() - i, pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		default:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		}
	}

	/**
	 * プレーヤのインベントリからアイテムを減らす
	 */
	public static ItemStack decPlayerInventory(EntityPlayer par1EntityPlayer, int par2Index, int par3DecCount) {
		if (par1EntityPlayer == null) {
			return null;
		}
		
		if (par2Index == -1) {
			par2Index = par1EntityPlayer.inventory.currentItem;
		}
		ItemStack itemstack1 = par1EntityPlayer.inventory.getStackInSlot(par2Index);
		if (itemstack1 == null) {
			return null;
		}
		
		if (!par1EntityPlayer.capabilities.isCreativeMode) {
			// クリエイティブだと減らない
			itemstack1.stackSize -= par3DecCount;
		}
		
		if (itemstack1.getItem() instanceof ItemPotion) {
			if(itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle, par3DecCount));
				return null;
			}
			par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle, par3DecCount));
		} else {
			if (itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par2Index, null);
				return null;
			}
		}
		
		return itemstack1;
	}

	/**
		 * 視線の先にいる最初のEntityを返す
		 * @param pEntity
		 * 視点
		 * @param pRange
		 * 視線の有効距離
		 * @param pDelta
		 * 時刻補正
		 * @param pExpand
		 * 検知領域の拡大範囲
		 * @return
		 */
		public static Entity getRayTraceEntity(EntityLivingBase pEntity, double pRange, float pDelta, float pExpand) {
			Vec3 lvpos = new Vec3(
					pEntity.posX, pEntity.posY + pEntity.getEyeHeight(), pEntity.posZ);
	//		Vec3 lvpos = pEntity.getPosition(pDelta).addVector(0D, pEntity.getEyeHeight(), 0D);
			Vec3 lvlook = pEntity.getLook(pDelta);
			Vec3 lvview = lvpos.addVector(lvlook.xCoord * pRange, lvlook.yCoord * pRange, lvlook.zCoord * pRange);
			Entity ltarget = null;
			List llist = pEntity.worldObj.getEntitiesWithinAABBExcludingEntity(pEntity, pEntity.getEntityBoundingBox().addCoord(lvlook.xCoord * pRange, lvlook.yCoord * pRange, lvlook.zCoord * pRange).expand(pExpand, pExpand, pExpand));
			double ltdistance = pRange * pRange;
			
			for (int var13 = 0; var13 < llist.size(); ++var13) {
				Entity lentity = (Entity)llist.get(var13);
				
				if (lentity.canBeCollidedWith()) {
					float lexpand = lentity.getCollisionBorderSize() + 0.3F;
					AxisAlignedBB laabb = lentity.getEntityBoundingBox().expand(lexpand, lexpand, lexpand);
					MovingObjectPosition lmop = laabb.calculateIntercept(lvpos, lvview);
					
					if (laabb.isVecInside(lvpos)) {
						if (0.0D < ltdistance || ltdistance == 0.0D) {
							ltarget = lentity;
							ltdistance = 0.0D;
						}
					} else if (lmop != null) {
						double ldis = lvpos.squareDistanceTo(lmop.hitVec);
						
						if (ldis < ltdistance || ltdistance == 0.0D) {
							ltarget = lentity;
							ltdistance = ldis;
						}
					}
				}
			}
			return ltarget;
		}

}
