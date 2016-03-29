package net.blacklab.lmr.entity.ai;

import net.blacklab.lib.minecraft.vector.VectorUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.IEntityLittleMaidAvatar;
import net.blacklab.lmr.entity.actionsp.SwingStatus;
import net.blacklab.lmr.entity.mode.EntityMode_Archer;
import net.blacklab.lmr.entity.mode.EntityMode_Playing;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAILMAttackArrow extends EntityAIBase implements IEntityAI {

	protected boolean fEnable;

	protected EntityLittleMaid fMaid;
	protected EntityPlayer fAvatar;
	protected InventoryLittleMaid fInventory;
	protected SwingStatus swingState;
	protected World worldObj;
	protected EntityLivingBase fTarget;
	protected int fForget;


	public EntityAILMAttackArrow(EntityLittleMaid pEntityLittleMaid) {
		fMaid = pEntityLittleMaid;
		fAvatar = pEntityLittleMaid.maidAvatar;
		fInventory = pEntityLittleMaid.maidInventory;
		swingState = pEntityLittleMaid.getSwingStatusDominant();
		worldObj = pEntityLittleMaid.worldObj;
		fEnable = false;
		setMutexBits(3);
	}

	public IEntityLittleMaidAvatar getAvatarIF()
	{
		return (IEntityLittleMaidAvatar)fAvatar;
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase entityliving = fMaid.getAttackTarget();
		boolean a = isExecutable();
		if (a && !VectorUtil.canMoveThrough(
				fMaid, fMaid.getEyeHeight(),
				entityliving.posX, entityliving.posY+entityliving.getEyeHeight(), entityliving.posZ, false, true, false)) {
			fMaid.setAttackTarget(null);
			//fMaid.setTarget(null);
//			fMaid.getNavigator().clearPathEntity();
			fTarget = null;
			resetTask();
			return false;
		}
		return a;
	}

	protected boolean isExecutable() {
		EntityLivingBase entityliving = fMaid.getAttackTarget();
		if(fMaid.isMaidWaitEx()) return false;

		if (!fEnable || entityliving == null || entityliving.isDead) {
			fMaid.setAttackTarget(null);
			//fMaid.setTarget(null);
//			fMaid.getNavigator().clearPathEntity();
			fTarget = null;
			resetTask();
			return false;
		}
		if (fMaid.getMaidModeInt() == EntityMode_Archer.mmode_Archer ||
				fMaid.getMaidModeInt() == EntityMode_Archer.mmode_Blazingstar) {
//			for (ItemStack stack: fMaid.maidInventory.mainInventory) {
//				if (stack != null && stack.getItem()==Items.arrow || TriggerSelect.checkWeapon(fMaid.getMaidMasterUUID(), "Arrow", stack)) {
					fTarget = entityliving;
					return true;
//				}
//			}
		}
		if (fMaid.isPlaying()) {
			fTarget = entityliving;
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
//		fMaid.playLittleMaidSound(fMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, false);
		swingState = fMaid.getSwingStatusDominant();
	}

	@Override
	public boolean continueExecuting() {
		return isExecutable() || (fTarget != null && !fMaid.getNavigator().noPath());
	}

	@Override
	public void resetTask() {
		fTarget = null;
//		fAvatar.stopUsingItem();
		fAvatar.stopActiveHand();
//		fAvatar.clearItemInUse();
		fForget=0;
	}

	@Override
	public void updateTask() {

		double backupPosX = fMaid.posX;
		double backupPosZ = fMaid.posZ;

		// プレイヤーに乗っていると射線にプレイヤーが入り、撃てなくなるため僅かに目標エンティティに近づける
		// 関数を抜ける前に元に戻す必要があるので途中で return しないこと
		if(fMaid.getRidingEntity() instanceof EntityPlayer)
		{
			double dtx = fTarget.posX - fMaid.posX;
			double dtz = fTarget.posZ - fMaid.posZ;
			double distTarget = MathHelper.sqrt_double(dtx*dtx + dtz*dtz);
			fMaid.posX += dtx / distTarget * 1.0;	// 1m 目標に近づける
			fMaid.posZ += dtz / distTarget * 1.0;	// 1m 目標に近づける
		}

		double lrange = 225D;
		double ldist = fMaid.getDistanceSqToEntity(fTarget);
		boolean lsee = fMaid.getEntitySenses().canSee(fTarget) &&
				VectorUtil.canMoveThrough(
						fMaid, fMaid.getEyeHeight(),
						fTarget.posX, fTarget.posY+fTarget.getEyeHeight(), fTarget.posZ, false, true, false);

		// 攻撃対象を見る
		if (fTarget!=null) fMaid.getLookHelper().setLookPositionWithEntity(fTarget, 30F, 30F);

		if(fForget>=15){
			resetTask();
			return;
		}

		// 視界の外に出たら一定時間で飽きる
		if (lsee) {
			fForget = 0;
		} else {
			fForget++;
			return;
		}

		if (ldist < lrange) {
			if(fTarget==null){
				resetTask();
				return;
			}

			// 有効射程内
			double atx = fTarget.posX - fMaid.posX;
			double aty = fTarget.posY - fMaid.posY;
			double atz = fTarget.posZ - fMaid.posZ;
			if (fTarget.isEntityAlive()) {
				ItemStack litemstack = fMaid.getCurrentEquippedItem();
				// 敵とのベクトル
				double atl = atx * atx + aty * aty + atz * atz;
				double il = -1D;
				double milsq = 10D;
				Entity masterEntity = fMaid.getMaidMasterEntity();
				if (masterEntity != null && !fMaid.isPlaying()) {
					// 主とのベクトル
					double amx = masterEntity.posX - fMaid.posX;
					double amy = masterEntity.posY - fMaid.posY;//-2D
					double amz = masterEntity.posZ - fMaid.posZ;

					// この値が０～１ならターゲットとの間に主がいる
					il = (amx * atx + amy * aty + amz * atz) / atl;

					// 射線ベクトルと主との垂直ベクトル
					double mix = (fMaid.posX + il * atx) - masterEntity.posX;
					double miy = (fMaid.posY + il * aty) - masterEntity.posY;// + 2D;
					double miz = (fMaid.posZ + il * atz) - masterEntity.posZ;
					// 射線から主との距離
					milsq = mix * mix + miy * miy + miz * miz;
//					mod_LMM_littleMaidMob.Debug("il:%f, milsq:%f", il, milsq);
				}

				if (litemstack != null && !(litemstack.getItem() instanceof ItemFood) && !fMaid.weaponReload) {
//					int lastentityid = worldObj.loadedEntityList.size();
					int itemcount = litemstack.stackSize;
					fMaid.mstatAimeBow = true;
					getAvatarIF().getValueVectorFire(atx, aty, atz, atl);
					// ダイヤ、金ヘルムなら味方への誤射を気持ち軽減
					boolean lcanattack = true;
					boolean ldotarget = false;
					double tpr = Math.sqrt(atl);
					Entity lentity = MaidHelper.getRayTraceEntity(fMaid.maidAvatar, tpr + 1.0F, 1.0F, 1.0F);
					ItemStack headstack = fInventory.armorInventory[3];
					Item helmid = headstack == null ? null : headstack.getItem();
					if (helmid == Items.diamond_helmet || helmid == Items.golden_helmet) {
						// 射線軸の確認
						if (lentity != null && fMaid.getIFF(lentity)) {
							lcanattack = false;
//							mod_LMM_littleMaidMob.Debug("ID:%d-friendly fire to ID:%d.", fMaid.entityId, lentity.entityId);
						}
					}
					if (lentity == fTarget) {
						ldotarget = true;
					}
					lcanattack &= (milsq > 3D || il < 0D);
					lcanattack &= ldotarget;
					// 横移動
					if (!lcanattack) {
						// 射撃位置を確保する
						double tpx = fMaid.posX;
						double tpy = fMaid.posY;
						double tpz = fMaid.posZ;
//						double tpr = Math.sqrt(atl) * 0.5D;
						tpr = tpr * 0.5D;
						if (fMaid.isBloodsuck()) {
							// 左回り
							tpx += (atz / tpr);
							tpz -= (atx / tpr);
						} else {
							// 右回り
							tpx -= (atz / tpr);
							tpz += (atx / tpr);
						}
						fMaid.getNavigator().tryMoveToXYZ(tpx, tpy, tpz, 1.0F);
					}
					else if (lsee & ldist < 100) {
						fMaid.getNavigator().clearPathEntity();
//						mod_LMM_littleMaidMob.Debug("Shooting Range.");
					}

					lcanattack &= lsee;
//            		mod_littleMaidMob.Debug(String.format("id:%d at:%d", entityId, attackTime));
					if (((fMaid.weaponFullAuto && !lcanattack) || (lcanattack && fMaid.getSwingStatusDominant().canAttack())) && getAvatarIF().getIsItemTrigger()) {
						// シュート
						// フルオート武器は射撃停止
						LittleMaidReengaged.Debug("id:%d shoot.", fMaid.getEntityId());
						fAvatar.stopActiveHand();
						fMaid.setSwing(30, EnumSound.shoot, !fMaid.isPlaying());
						if (fMaid.isPlaying()) {
							resetTask();
						}
					} else {
						// チャージ
						if (litemstack.getMaxItemUseDuration() > 500) {
//                			mod_littleMaidMob.Debug(String.format("non reload.%b", isMaskedMaid));
							// リロード無しの通常兵装
							if (!getAvatarIF().isUsingItemLittleMaid()) {
								// 構え
								if (!fMaid.weaponFullAuto || lcanattack) {
									int at = ((helmid == Items.iron_helmet) || (helmid == Items.diamond_helmet)) ? 26 : 16;
									if (swingState.attackTime < at) {
										ActionResult<ItemStack> result = litemstack.useItemRightClick(worldObj, fAvatar, EnumHand.MAIN_HAND);
										if (result.getType() != EnumActionResult.SUCCESS) {
											LittleMaidReengaged.Debug("id:%d bow trigger failed.", fMaid.getEntityId());
											resetTask();
											return;
										}
										fMaid.setSwing(at, EnumSound.sighting, !fMaid.isPlaying());
										litemstack = result.getResult();
										LittleMaidReengaged.Debug("id:%d redygun.", fMaid.getEntityId());
									}
								} else {
									if(fMaid.maidMode!=EntityMode_Playing.mmode_Playing)
										LittleMaidReengaged.Debug(String.format("ID:%d-friendly fire FullAuto.", fMaid.getEntityId()));
								}
							}
						}
						else if (litemstack.getMaxItemUseDuration() == 0) {
							// 通常投擲兵装
							if (swingState.canAttack() && !fAvatar.isHandActive()) {
								if (lcanattack) {
									litemstack = litemstack.useItemRightClick(worldObj, fAvatar, EnumHand.MAIN_HAND).getResult();
									// 意図的にショートスパンで音が鳴るようにしてある
									fMaid.mstatAimeBow = false;
									fMaid.setSwing(10, (litemstack.stackSize == itemcount) ? EnumSound.shoot_burst : EnumSound.Null, !fMaid.isPlaying());
									LittleMaidReengaged.Debug(String.format("id:%d throw weapon.(%d:%f:%f)", fMaid.getEntityId(), swingState.attackTime, fMaid.rotationYaw, fMaid.rotationYawHead));
									swingState.attackTime = 5;
									if (fMaid.maidMode == EntityMode_Playing.mmode_Playing) {
										fMaid.setMaidWaitCount(10);
//										return;
									}
								} else {
									if(fMaid.maidMode!=EntityMode_Playing.mmode_Playing)
										LittleMaidReengaged.Debug(String.format("ID:%d-friendly fire throw weapon.", fMaid.getEntityId()));
								}
							}
						} else {
							// リロード有りの特殊兵装
							if (!getAvatarIF().isUsingItemLittleMaid()) {
								litemstack = litemstack.useItemRightClick(worldObj, fAvatar, EnumHand.MAIN_HAND).getResult();
								LittleMaidReengaged.Debug(String.format("%d reload.", fMaid.getEntityId()));
							}
							// リロード終了まで強制的に構える
							swingState.attackTime = 5;
						}
					}
//            		maidAvatarEntity.setValueRotation();
					getAvatarIF().setValueVector();
					// アイテムが亡くなった
					if (litemstack.stackSize <= 0) {
						fMaid.destroyCurrentEquippedItem();
						fMaid.getNextEquipItem();
					} else {
						fInventory.setInventoryCurrentSlotContents(litemstack);
					}

					// 発生したEntityをチェックしてmaidAvatarEntityが居ないかを確認
					// TODO issue #9 merge from LittleMaidMobAX(https://github.com/asiekierka/littleMaidMobX/commit/92b2850b1bc4a70b69629cfc84c92748174c8bc6)
/*
					List<Entity> newentitys = worldObj.loadedEntityList.subList(lastentityid, worldObj.loadedEntityList.size());
					boolean shootingflag = false;
					if (newentitys != null && newentitys.size() > 0) {
						LMM_LittleMaidMobNX.Debug(String.format("new FO entity %d", newentitys.size()));
						for (Entity te : newentitys) {
							if (te.isDead) {
								shootingflag = true;
								continue;
							}
							try {
								// 飛翔体の主を置き換える
								Field fd[] = te.getClass().getDeclaredFields();
//                				mod_littleMaidMob.Debug(String.format("%s, %d", e.getClass().getName(), fd.length));
								for (Field ff : fd) {
									// 変数を検索しAvatarと同じ物を自分と置き換える
									ff.setAccessible(true);
									Object eo = ff.get(te);
									if (eo.equals(fAvatar)) {
										ff.set(te, this);
										LMM_LittleMaidMobNX.Debug("Replace FO Owner.");
									}
								}
							}
							catch (Exception exception) {
							}
						}
					}
					// 既に命中していた場合の処理
					if (shootingflag) {
						for (Object obj : worldObj.loadedEntityList) {
							if (obj instanceof EntityCreature && !(obj instanceof LMM_EntityLittleMaid)) {
								EntityCreature ecr = (EntityCreature)obj;
								//1.8修正検討
								if (ecr.getAttackTarget() == fAvatar) {
									ecr.setAttackTarget(fMaid);
									ecr.setRevengeTarget(fMaid);
									ecr.getNavigator().getPathToEntityLiving(fMaid);
								}
							}
						}
					}
*/
				}
			}
		} else {
			// 有効射程外
			if (fMaid.getNavigator().noPath()) {
				fMaid.getNavigator().tryMoveToEntityLiving(fTarget, 1.0);
//				fMaid.setAttackTarget(null);
			}
//			if (fMaid.weaponFullAuto && getAvatarIF().getIsItemTrigger()) {
//				fAvatar.stopActiveHand();
//			} else {
//				fAvatar.clearItemInUse();
//			}
			resetTask();
		}


		// プレイヤーが射線に入らないように、変更したメイドさんの位置を元に戻す
		fMaid.posX = backupPosX;
		fMaid.posZ = backupPosZ;
	}

	@Override
	public void setEnable(boolean pFlag) {
		fEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return fEnable;
	}

}
