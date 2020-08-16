package net.blacklab.lmr.util;

import firis.lmlib.api.caps.ModelCapsEntityBase;
import firis.lmlib.api.motion.LMMotionSitdown;
import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;


/**
 * Entityのデータ読み取り用のクラス
 * 別にEntityにインターフェース付けてもOK
 */
public class ModelCapsLittleMaid extends ModelCapsEntityBase<EntityLittleMaid> {

	public ModelCapsLittleMaid(EntityLittleMaid pOwner) {
		super(pOwner);
	}

	/**
	 * メイドさんのパラメータ
	 */
	@Override
	public Object getCapsValue(int pIndex, Object ...pArg) {
		int li = 0;
		switch (pIndex) {
		case caps_getRidingType:
			if (owner.getRidingEntity() instanceof EntityLittleMaid) return "littlemaid";
			break;
		case caps_isBloodsuck:
			return owner.isBloodsuck();
		case caps_isFreedom:
			return owner.isFreedom();
		case caps_isTracer:
			return owner.isTracer();
		case caps_isPlaying:
			return owner.isPlaying();
		case caps_isLookSuger:
			return owner.isLookSuger();
		case caps_isBlocking:
			return owner.isBlocking();
		case caps_isWait:
			return owner.isMaidWait();
		case caps_isWaitEX:
			return owner.isMaidWaitEx();
		case caps_isOpenInv:
			return owner.isOpenInventory();
		case caps_isWorking:
//			return owner.isWorking();
			return owner.jobController.isWorking();
		case caps_isWorkingDelay:
//			return owner.isWorkingDelay();
			return owner.jobController.isWorkingDelay();
		case caps_isContract:
			return owner.isContract();
		case caps_isContractEX:
			return owner.isContractEX();
		case caps_isRemainsC:
			return owner.isRemainsContract();
		case caps_isClock:
			return owner.isClockMaid();
		case caps_isMasked:
			return owner.isMaskedMaid();
		case caps_isCamouflage:
			//return owner.isCamouflage();
			//カモフラージュ！
			//ItemSkullを頭の位置に装備している場合にのみtrueを返す
			//現状は実装なしのため常にfalseを返すように変更
			return false;
		case caps_isPlanter:
			//return owner.isPlanter();
			//鉢植え状態！
			//カボチャや苗木などが頭の位置に装備している場合にのみtrueを返す
			//現状は実装なしのため常にfalseを返すように変更
			return false;
		case caps_isOverdrive:
			return owner.getMaidOverDriveTime().isEnable();
		case caps_isOverdriveDelay:
			return owner.getMaidOverDriveTime().isDelay();
		case caps_entityIdFactor:
			return owner.entityIdFactor;
		case caps_height:
			return owner.getModelConfigCompound().getTextureBoxLittleMaid() == null ? 
					null : owner.getModelConfigCompound().getTextureBoxLittleMaid().getModelLittleMaid().getyOffset(this);
		case caps_mountedYOffset:
			return owner.getModelConfigCompound().getTextureBoxLittleMaid() == null ? 
					null : owner.getModelConfigCompound().getTextureBoxLittleMaid().getModelLittleMaid().getMountedYOffset(this);
		case caps_dominantArm:
			return owner.getDominantArm();
		case caps_Items:
			ItemStack[] lstacks = new ItemStack[owner.mstatSwingStatus.length];
			for (SwingStatus ls : owner.mstatSwingStatus) {
				ItemStack stack = ls.getItemStack(owner);
				stack = stack.isEmpty() ? null : stack;
				lstacks[li++] = stack;
			}
			return lstacks;
		case caps_Actions:
			EnumAction[] lactions = new EnumAction[owner.mstatSwingStatus.length];
			for (SwingStatus ls : owner.mstatSwingStatus) {
				lactions[li++] = ls.isUsingItem() ? ls.getItemStack(owner).getItemUseAction() : null;
			}
			return lactions;
		case caps_Grounds:
			float[] lgrounds = new float[owner.mstatSwingStatus.length];
			for (SwingStatus ls : owner.mstatSwingStatus) {
				lgrounds[li++] = ls.onGround;
			}
			return lgrounds;
		case caps_Ground:
			// float (int pIndex, int pDefVal)
			if (owner.mstatSwingStatus.length < (Integer)pArg[0]) {
				return pArg[1];
			}
			return owner.mstatSwingStatus[(Integer)pArg[0]].onGround;
		case caps_Inventory:
			return owner.maidInventory;
		case caps_interestedAngle:
			return owner.getInterestedAngle((Float)pArg[0]);
//		case caps_currentArmor:
//			return owner.getCurrentItemOrArmor((Integer)pArg[0] + 1);
		case caps_heldItems:
		case caps_currentEquippedItem:
			return getItemStackNull(owner.getCurrentEquippedItem());
		case caps_currentRightHandItem:
			return getItemStackNull(getHandSideItemStack(EnumHandSide.RIGHT));
		case caps_currentLeftHandItem:
			return getItemStackNull(getHandSideItemStack(EnumHandSide.LEFT));
		case caps_job:
			return owner.jobController.getMaidModeString().toLowerCase();
		case caps_multimodel_motion:
			boolean isMotionSitting = owner.isMotionSitting();
			EnumMaidMotion lmmotion = owner.getMaidMotion();
			if (isMotionSitting || (lmmotion == EnumMaidMotion.SIT)) {
				return LMMotionSitdown.MOTION_ID;
			}
			return owner.getMotionId();
		}
		return super.getCapsValue(pIndex, pArg);
	}
	
	/**
	 * 右手or左手のアイテムを取得する
	 * @param handSide
	 * @return
	 */
	@Override
	protected ItemStack getHandSideItemStack(EnumHandSide handSide) {
		EnumHandSide mainSide = this.owner.getDominantArm() == 0 ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
		if (mainSide == handSide) {
			//利き手
			return this.owner.getCurrentEquippedItem();
		} else {
			//オフハンド
			return this.owner.getHeldItemOffhand();
		}
	}
	
	/**
	 * ModelMultiBaseへ初期値を設定する
	 * @param model
	 * @param modelCaps
	 */
	@Override
	public void initModelMultiBase(ModelMultiBase model, float entityYaw, float partialTicks) {
		
		super.initModelMultiBase(model, entityYaw, partialTicks);
		
		if (model == null) return;
		
		EntityLittleMaid maid = this.owner;
		
		//カスタムモーション
		if (this.setCustomMotion(model, maid, entityYaw, partialTicks)) {
			return;
		}
		
		model.setCapsValue(IModelCaps.caps_heldItemLeft, 0);
		model.setCapsValue(IModelCaps.caps_heldItemRight, 0);
		model.setCapsValue(IModelCaps.caps_onGround,
				maid.mstatSwingStatus[0].getSwingProgress(partialTicks),
				maid.mstatSwingStatus[1].getSwingProgress(partialTicks));
		model.setCapsValue(IModelCaps.caps_isRiding, maid.isRidingRender());
		model.setCapsValue(IModelCaps.caps_isSneak, maid.isSneaking());
		model.setCapsValue(IModelCaps.caps_aimedBow, maid.isAimebow());
		model.setCapsValue(IModelCaps.caps_isWait, maid.isMaidWait());
		model.setCapsValue(IModelCaps.caps_isChild, maid.isChild());
		model.setCapsValue(IModelCaps.caps_entityIdFactor, maid.entityIdFactor);
		model.setCapsValue(IModelCaps.caps_ticksExisted, maid.ticksExisted);
		model.setCapsValue(IModelCaps.caps_dominantArm, maid.getDominantArm());
		
	}
	
	/**
	 * LMRFP独自処理
	 * 
	 * モーション固定Rendererを制御する
	 * 歩行や首の傾きは別部分で制御しているようなのでここでは制御できない
	 * 
	 */
	protected boolean setCustomMotion(ModelMultiBase model, EntityLittleMaid entity, float entityYaw, float partialTicks) {
		
		EnumMaidMotion motion = entity.getMaidMotion();
		
		switch (motion) {
			case NONE:
				return false;
			
			//標準状態
			case DEFAULT:
				break;

			//待機
			case WAIT:
				model.setCapsValue(IModelCaps.caps_isLookSuger, true);
				model.setCapsValue(IModelCaps.caps_isWait, true);
				break;
			
			//砂糖らぶ
			case LOOKSUGAR:
				break;

			//スニーク
			case SNEAK:
				model.setCapsValue(IModelCaps.caps_isSneak, true);
				break;
				
			//おすわり（LMRFPのお座りは待機と複合）
			case SIT:
				model.setCapsValue(IModelCaps.caps_isWait, true);
				model.setCapsValue(IModelCaps.caps_isRiding, true);
				break;
			
			//弓構え
			case BOW:
				model.setCapsValue(IModelCaps.caps_aimedBow, true);
				break;
		}
		
		//共通設定
		//腕降り制御に使われている
		model.setCapsValue(IModelCaps.caps_onGround,
				entity.mstatSwingStatus[0].getSwingProgress(partialTicks),
				entity.mstatSwingStatus[1].getSwingProgress(partialTicks));
		
		return true;
	}
	
}
