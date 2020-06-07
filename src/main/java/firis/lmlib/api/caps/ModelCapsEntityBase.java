package firis.lmlib.api.caps;

import java.util.ArrayList;
import java.util.List;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * EntityLivingBase用マルチモデルのパラメータ管理のベースクラス
 * @author firis-games
 *
 */
public abstract class ModelCapsEntityBase<T extends EntityLivingBase> implements IModelCapsEntity {

	/**
	 * ModelCapsEntityのOwner
	 */
	protected T owner;
	
	/**
	 * ModelMultiBaseで初期化するIModelCapsリスト
	 */
	protected List<Integer> modelCapsList = this.initModelCapsList();
	
	/**
	 * コンストラクタ
	 * @param pOwner
	 */
	public ModelCapsEntityBase(T pOwner) {
		this.owner = pOwner;
	}
	
	@Override
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		//腕振りの状態
		case caps_onGround:
			return this.getOnGrounds();
		//騎乗状態
		case caps_isRiding:
			return owner.isRiding();
		//子供状態
		case caps_isChild:
			return owner.isChild();
		//リアルタイムサイズ（未使用）
		case caps_isUpdateSize:
			break;
		//左手の傾き制御
		case caps_heldItemLeft:
			return 0.0F;
		//右手の傾き制御
		case caps_heldItemRight:
			return 0.0F;
		//手持ちのアイテム
		case caps_heldItems:
		case caps_currentEquippedItem:
			return this.getItemStackNull(owner.getHeldItemMainhand());
		//スニーク状態
		case caps_isSneak:
			return owner.isSneaking();
		//弓構え
		case caps_aimedBow:
			return false;
		//自身のEntity
		case caps_Entity:
		case caps_TextureEntity:
			return owner;
		//HP
		case caps_health:
			int iCapsHealth = (int) (owner.getHealth() / owner.getMaxHealth() * 20F);
			return Math.min(iCapsHealth, 20);
		//HP(float)
		case caps_healthFloat:
			float fCapsHealth = (owner.getHealth() / owner.getMaxHealth() * 20F);
			return Math.min(fCapsHealth, 20F);
		//tick
		case caps_ticksExisted:
			return owner.ticksExisted;
		//指定IDの防具アイテム
		case caps_currentArmor:
			ItemStack aromor = ((List<ItemStack>)owner.getArmorInventoryList()).get((Integer) pArg[0]);
			return this.getItemStackNull(aromor);
		//左手に持ったアイテム
		case caps_currentLeftHandItem:
			return this.getItemStackNull(this.getHandSideItemStack(EnumHandSide.LEFT));
		//右手に持ったアイテム
		case caps_currentRightHandItem:
			return this.getItemStackNull(this.getHandSideItemStack(EnumHandSide.RIGHT));
		//水濡れ状態（水中 or 雨）
		case caps_isWet:
			return owner.isWet();
		//死亡状態
		case caps_isDead:
			return owner.isDead;
		//ジャンプ状態（アクセス不可）
		case caps_isJumping:
			return false;//owner.isJumping;
		//蜘蛛の巣に引っかかった状態（アクセス不可）
		case caps_isInWeb:
			return false;//owner.isInWeb;
		//SwingProgress
		case caps_isSwingInProgress:
			return owner.isSwingInProgress;
		//炎上状態
		case caps_isBurning:
			return owner.isBurning();
		//水中状態
		case caps_isInWater:
			return owner.isInWater();
		//透明状態
		case caps_isInvisible:
			return owner.isInvisible();
		//走り状態
		case caps_isSprinting:
			return owner.isSprinting();
		//紐付き状態
		case caps_isLeeding:
			return (owner instanceof EntityLiving) && ((EntityLiving)owner).getLeashed();
		//乗ってるEntityの名前
		case caps_getRidingName:
			return owner.getRidingEntity() == null ? "" : EntityList.getEntityString(owner.getRidingEntity());
		//Entityの座標
		case caps_posX:
			return owner.posX;
		case caps_posY:
			return owner.posY;
		case caps_posZ:
			return owner.posZ;
		case caps_pos:
			if (pArg == null) {
				return new Double[] {owner.posX, owner.posY, owner.posZ};
			}
			return (Integer)pArg[0] == 0 ? owner.posX : (Integer)pArg[0] == 1 ? owner.posY : owner.posZ;
		//Entityのモーション値
		case caps_motionX:
			return owner.motionX;
		case caps_motionY:
			return owner.motionY;
		case caps_motionZ:
			return owner.motionZ;
		case caps_motion:
			if (pArg == null) {
				return new Double[] {owner.motionX, owner.motionY, owner.motionZ};
			}
			return (Integer)pArg[0] == 0 ? owner.motionX : (Integer)pArg[0] == 1 ? owner.motionY : owner.motionZ;
		//Entityの当たり判定
		case caps_boundingBox:
			if (pArg == null) {
				return owner.getEntityBoundingBox();
			}
			switch ((Integer)pArg[0]) {
			case 0:
				return owner.getEntityBoundingBox().maxX;
			case 1:
				return owner.getEntityBoundingBox().maxY;
			case 2:
				return owner.getEntityBoundingBox().maxZ;
			case 3:
				return owner.getEntityBoundingBox().minX;
			case 4:
				return owner.getEntityBoundingBox().minY;
			case 5:
				return owner.getEntityBoundingBox().minZ;
			}
		//Entityの傾きの値
		case caps_rotationYaw:
			return owner.rotationYaw;
		case caps_rotationPitch:
			return owner.rotationPitch;
		case caps_prevRotationYaw:
			return owner.prevRotationYaw;
		case caps_prevRotationPitch:
			return owner.prevRotationPitch;
		case caps_renderYawOffset:
			return owner.renderYawOffset;
		//Blockを取得する
		case caps_PosBlockID:
			return owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2]))).getBlock();
		//BlockStateを取得する
		case caps_PosBlockState:
			return owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		//空気ブロック判定
		case caps_PosBlockAir:
			IBlockState state = owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
			//移動可能ブロックかつ通常ブロックではない
			//Block.causesSuffocationから変更
			return !(state.getMaterial().blocksMovement() && state.isFullCube());
		//光源ブロック判定
		case caps_PosBlockLight:
			return owner.getEntityWorld().getBlockLightOpacity(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		//RS動力判定
		case caps_PosBlockPower:
			return owner.getEntityWorld().getStrongPower(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		//プレイヤーへ騎乗しているかの判定
		case caps_isRidingPlayer:
			return owner.getRidingEntity() instanceof EntityPlayer;
			
		//Worldに関係する各種情報を取得する
		case caps_WorldTotalTime:
			return owner.getEntityWorld().getWorldInfo().getWorldTotalTime();
		case caps_WorldTime:
			return owner.getEntityWorld().getWorldInfo().getWorldTime();
		case caps_MoonPhase:
			return owner.getEntityWorld().getMoonPhase();
			
		//**************************************************
		//　メイドさんに関係する情報
		// 基本的にoff状態として返す
		//**************************************************
		case caps_isRendering:
		case caps_isBloodsuck:
		case caps_isFreedom:
		case caps_isTracer:
		case caps_isPlaying:
		case caps_isLookSuger: //砂糖を見たときの首傾け
		case caps_isBlocking:
		case caps_isWait: //メイドさん待機モーション
		case caps_isWaitEX:
		case caps_isOpenInv:
		case caps_isWorking:
		case caps_isWorkingDelay:
		case caps_isContract:
		case caps_isContractEX:
		case caps_isRemainsC:
		case caps_isClock:
		case caps_isMasked:
		case caps_isCamouflage:
		case caps_isPlanter:
		case caps_isOverdrive:
		case caps_isOverdriveDelay:
			return false;
		//Entityごとの揺らぎ
		case caps_entityIdFactor:
		//メイドさんのサイズ関連（未使用）
		case caps_width:
		case caps_height:
		case caps_YOffset:
		case caps_mountedYOffset:
			return 0.0F;
		//利き手
		case caps_dominantArm:
			return owner.getPrimaryHand() == EnumHandSide.RIGHT ? 0 : 1;
		//未使用と判断するパラメータ
		case caps_Items:
		case caps_Actions:
		case caps_Grounds:
		case caps_Inventory:
		case caps_interestedAngle:
			break;
		//カスタムモーション用
		case caps_multimodel_motion:
		case caps_looking_rotation:
			return null;
		}
		return null;
	}

	/**
	 * setCapsValueは使用しない
	 */
	@Deprecated
	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		return false;
	}
	
	protected List<Integer> initModelCapsList() {
		List<Integer> caps = new ArrayList<>();
		
		//モデル側へ受け渡すcapsIdを設定
		caps.add(IModelCaps.caps_heldItemLeft);
		caps.add(IModelCaps.caps_heldItemRight);
		caps.add(IModelCaps.caps_onGround);
		caps.add(IModelCaps.caps_isRiding);
		caps.add(IModelCaps.caps_isSneak);
		caps.add(IModelCaps.caps_aimedBow);
		caps.add(IModelCaps.caps_isWait);
		caps.add(IModelCaps.caps_isChild);
		caps.add(IModelCaps.caps_entityIdFactor);
		caps.add(IModelCaps.caps_ticksExisted);
		caps.add(IModelCaps.caps_dominantArm);
		
		return caps;
	}
	
	/**
	 * ModelMultiBaseへ初期値を設定する
	 * @param model
	 * @param modelCaps
	 */
	@Override
	public void initModelMultiBase(ModelMultiBase model, float entityYaw, float partialTicks) {
		
		if (model == null) return;
		
		//メイド状態の初期化
		model.setCapsValue(IModelCaps.caps_heldItemLeft, 0);
		model.setCapsValue(IModelCaps.caps_heldItemRight, 0);
		model.setCapsValue(IModelCaps.caps_onGround, 0.0F, 0.0F);
		model.setCapsValue(IModelCaps.caps_isRiding, false);
		model.setCapsValue(IModelCaps.caps_isSneak, false);
		model.setCapsValue(IModelCaps.caps_aimedBow, false);
		model.setCapsValue(IModelCaps.caps_isWait, false);
		model.setCapsValue(IModelCaps.caps_isChild, false);
		model.setCapsValue(IModelCaps.caps_entityIdFactor, 0F);
		model.setCapsValue(IModelCaps.caps_ticksExisted, 0);
		
		//パラメータの反映
		for (Integer capsId : modelCapsList) {
			//onGroundだけ特殊処理
			if (IModelCaps.caps_onGround == capsId) {
				float[] onGround = (float[]) this.getCapsValue(capsId);
				model.setCapsValue(capsId, onGround[0], onGround[1]);
			} else {
				model.setCapsValue(capsId, this.getCapsValue(capsId));
			}
		}
		
	}
	
	/**
	 * メイドさんのmstatSwingStatusを仮想で再現
	 * @param player
	 * @return
	 */
	protected float[] getOnGrounds() {
		
		float onGrounds[] = new float[] {0.0F, 0.0F};
		
		//利き腕取得
		EnumHandSide dominantHand = this.owner.getPrimaryHand();
		boolean isDominantRight = dominantHand == EnumHandSide.RIGHT;
		
		//右か左かの判断
		boolean isMainHand = EnumHand.MAIN_HAND == this.owner.swingingHand;
		
		//左利きの場合は左右逆転する
		if (!isDominantRight) {
			isMainHand = !isMainHand;
		}
		
		//腕振り
		/*　tick単位での腕振り制御位置
		腕を振った時にplayer.swingProgressにこの値が設定される
		0.16666667
		0.16666667
		0.16666667
		0.33333334
		0.33333334
		0.33333334
		0.5
		0.5
		0.5
		0.6666667
		0.6666667
		0.6666667
		0.8333333
		0.8333333
		0.8333333
		*/
		if (isMainHand) {
			//右振り
			onGrounds[0] = this.owner.swingProgress;
			onGrounds[1] = 0.0F;
		} else {
			//左振り
			onGrounds[0] = 0.0F;
			onGrounds[1] = this.owner.swingProgress;
		}
		return onGrounds;
	}

	/**
	 * 空の場合はnullに変換するItemStack
	 * @return
	 */
	protected ItemStack getItemStackNull(ItemStack stack) {
		return stack.isEmpty() ? null : stack;
	}
	
	/**
	 * 右手or左手のアイテムを取得する
	 * @param handSide
	 * @return
	 */
	protected ItemStack getHandSideItemStack(EnumHandSide handSide) {
		EnumHandSide mainSide = this.owner.getPrimaryHand();
		if (mainSide == handSide) {
			//メインハンド
			return this.owner.getHeldItemMainhand();
		} else {
			//オフハンド
			return this.owner.getHeldItemOffhand();
		}
	}
}
