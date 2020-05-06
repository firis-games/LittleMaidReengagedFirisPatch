package net.blacklab.lmr.util;

import java.util.HashMap;
import java.util.Map;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;


/**
 * Entityのデータ読み取り用のクラス
 * 別にEntityにインターフェース付けてもOK
 */
public class ModelCapsLittleMaid extends ModelCapsData {

	private EntityLittleMaid owner;
	private static Map<String, Integer> caps;

	static {
		caps = new HashMap<String, Integer>();
		caps.putAll(getStaticModelCaps());
		caps.put("isBloodsuck", caps_isBloodsuck);
		caps.put("isFreedom", caps_isFreedom);
		caps.put("isTracer", caps_isTracer);
		caps.put("isPlaying", caps_isPlaying);
		caps.put("isLookSuger", caps_isLookSuger);
		caps.put("isBlocking", caps_isBlocking);
		caps.put("isWait", caps_isWait);
		caps.put("isWaitEX", caps_isWaitEX);
		caps.put("isOpenInv", caps_isOpenInv);
		caps.put("isWorking", caps_isWorking);
		caps.put("isWorkingDelay", caps_isWorkingDelay);
		caps.put("isContract", caps_isContract);
		caps.put("isContractEX", caps_isContractEX);
		caps.put("isRemainsC", caps_isRemainsC);
		caps.put("isClock", caps_isClock);
		caps.put("isMasked", caps_isMasked);
		caps.put("isCamouflage", caps_isCamouflage);
		caps.put("isPlanter", caps_isPlanter);
		caps.put("isOverdrive", caps_isOverdrive);
		caps.put("isOverdriveDelay", caps_isOverdriveDelay);
		caps.put("entityIdFactor", caps_entityIdFactor);
		caps.put("height", caps_height);
		caps.put("width", caps_width);
		caps.put("YOffset", caps_YOffset);
		caps.put("mountedYOffset", caps_mountedYOffset);
		caps.put("dominantArm", caps_dominantArm);
//		caps.put("render", caps_render);
//		caps.put("Arms", caps_Arms);
//		caps.put("HeadMount", caps_HeadMount);
//		caps.put("HardPoint", caps_HardPoint);
		caps.put("stabiliser", caps_stabiliser);
		caps.put("Items", caps_Items);
		caps.put("Actions", caps_Actions);
		caps.put("Grounds", caps_Grounds);
		caps.put("Ground", caps_Ground);
		caps.put("Inventory", caps_Inventory);
		caps.put("interestedAngle", caps_interestedAngle);
//		caps.put("Entity", caps_Entity);
//		caps.put("health", caps_health);
		caps.put("currentArmor", caps_currentArmor);
		caps.put("currentEquippedItem", caps_currentEquippedItem);
	}

	public ModelCapsLittleMaid(EntityLittleMaid pOwner) {
		super(pOwner);
		owner = pOwner;
	}

	@Override
	public Map<String, Integer> getModelCaps() {
		return caps;
	}

	@Override
	public Object getCapsValue(int pIndex, Object ...pArg) {
		int li = 0;

		switch (pIndex) {
//		case caps_Entity:
//			return owner;
//		case caps_health:
//			return (int)owner.getHealth();
//		case caps_healthFloat:
//			return owner.getHealth();
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
			return owner.isWorking();
		case caps_isWorkingDelay:
			return owner.isWorkingDelay();
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
			return owner.isCamouflage();
		case caps_isPlanter:
			return owner.isPlanter();
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
//		case caps_mountedYOffset:
//			return owner.textureModel0 == null ? null : owner.textureModel0.getHeight();
//		case caps_render:
//		case caps_Arms:
////		case caps_HeadMount:
////			// TODO 従来HeadMountとか使ってた部分は全部削除した方がすっきりすると思う．
////			return owner.maidInventory.armorInventory.get(3);
//		case caps_HardPoint:
//		case caps_stabiliser:
//			return owner.maidStabilizer;
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
		case caps_PartsVisible:
			return owner.getModelConfigCompound().selectValue;
		case caps_textureData:
			return owner.getModelConfigCompound();
		case caps_currentRightHandItem:
			return getItemStackNull(getHandSideItemStack(EnumHandSide.RIGHT));
		case caps_currentLeftHandItem:
			return getItemStackNull(getHandSideItemStack(EnumHandSide.LEFT));
		}

		return super.getCapsValue(pIndex, pArg);
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_PartsVisible:
			owner.getModelConfigCompound().selectValue = (Integer)pArg[0];
		}
		return super.setCapsValue(pIndex, pArg);
	}
	
	/**
	 * 空の場合はnullに変換するItemStack
	 * @return
	 */
	private ItemStack getItemStackNull(ItemStack stack) {
		return stack.isEmpty() ? null : stack;
	}
	
	/**
	 * 右手or左手のアイテムを取得する
	 * @param handSide
	 * @return
	 */
	private ItemStack getHandSideItemStack(EnumHandSide handSide) {
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
	public void setModelMultiFromModelCaps(ModelMultiBase model, float entityYaw, float partialTicks) {
		
		super.setModelMultiFromModelCaps(model, entityYaw, partialTicks);
		
		if (model == null) return;
		
		EntityLittleMaid maid = this.owner;
		
		//カスタムモーション
		if (this.setCustomMotion(model, maid, entityYaw, partialTicks)) {
			return;
		}
		
		model.setCapsValue(IModelCaps.caps_heldItemLeft, 0);
		model.setCapsValue(IModelCaps.caps_heldItemRight, 0);
//				modelMain.setCapsValue(IModelCaps.caps_onGround, renderSwingProgress(lmaid, par9));
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

		//カスタム設定
		model.setCapsValue(IModelCaps.caps_motionSitting, maid.isMotionSitting());
		
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
				model.setCapsValue(IModelCaps.caps_motionSitting, true);
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
