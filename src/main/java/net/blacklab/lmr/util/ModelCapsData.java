package net.blacklab.lmr.util;

import java.util.List;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * 描画パラメータ管理用クラス
 * @author firis-games
 *
 */
public abstract class ModelCapsData implements IModelCapsData {

	protected EntityLivingBase owner;
	
	//private static Map<String, Integer> caps;
	/*
	static {
		caps = new HashMap<String, Integer>();
		caps.put("Entity", caps_Entity);
		caps.put("health", caps_health);
		caps.put("healthFloat", caps_healthFloat);
		caps.put("ticksExisted", caps_ticksExisted);
		caps.put("heldItems", caps_heldItems);
		caps.put("currentEquippedItem", caps_currentEquippedItem);
		caps.put("currentArmor", caps_currentArmor);
		caps.put("onGround", caps_onGround);
		caps.put("isRiding", caps_isRiding);
		caps.put("isChild", caps_isChild);
		caps.put("isWet", caps_isWet);
		caps.put("isDead", caps_isDead);
		caps.put("isJumping", caps_isJumping);
		caps.put("isInWeb", caps_isInWeb);
		caps.put("isSwingInProgress", caps_isSwingInProgress);
		caps.put("isSneak", caps_isSneak);
		caps.put("isBlocking", caps_isBlocking);
		caps.put("isBurning", caps_isBurning);
		caps.put("isInWater", caps_isInWater);
		caps.put("isInvisible", caps_isInvisible);
		caps.put("isSprinting", caps_isSprinting);
//		caps.put("PosBlock", caps_PosBlock);
		caps.put("PosBlockID", caps_PosBlockID);
		caps.put("PosBlockState", caps_PosBlockState);
		caps.put("PosBlockAir", caps_PosBlockAir);
		caps.put("PosBlockLight", caps_PosBlockLight);
		caps.put("PosBlockPower", caps_PosBlockPower);
		caps.put("isRidingPlayer", caps_isRidingPlayer);
		caps.put("posX", caps_posX);
		caps.put("posY", caps_posY);
		caps.put("posZ", caps_posZ);
		caps.put("pos", caps_pos);
		caps.put("motionX", caps_motionX);
		caps.put("motionY", caps_motionY);
		caps.put("motionZ", caps_motionZ);
		caps.put("motion", caps_pos);
		caps.put("WorldTotalTime", caps_WorldTotalTime);
		caps.put("WorldTime", caps_WorldTime);
		caps.put("MoonPhase", caps_MoonPhase);
		caps.put("rotationYaw", caps_rotationYaw);
		caps.put("rotationPitch", caps_rotationPitch);
		caps.put("prevRotationYaw", caps_prevRotationYaw);
		caps.put("prevRotationPitch", caps_prevRotationPitch);
		caps.put("renderYawOffset", caps_renderYawOffset);
		caps.put("TextureEntity", caps_TextureEntity);
	}
	*/

//	public static Map<String, Integer> getStaticModelCaps() {
//		return caps;
//	}

	public ModelCapsData(EntityLivingBase pOwner) {
		owner = pOwner;
	}

//	@Override
//	public Map<String, Integer> getModelCaps() {
//		return caps;
//	}

	@Override
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_Entity:
			return owner;
		case caps_health:
			int iCapsHealth = (int) (owner.getHealth() / owner.getMaxHealth() * 20F);
			return Math.min(iCapsHealth, 20);
		case caps_healthFloat:
			float fCapsHealth = (owner.getHealth() / owner.getMaxHealth() * 20F);
			return Math.min(fCapsHealth, 20F);
		case caps_ticksExisted:
			return owner.ticksExisted;
		case caps_heldItems:
		case caps_currentEquippedItem:
			ItemStack mainhand = owner.getHeldItemMainhand();
			if (mainhand.isEmpty()) mainhand = null;
			return mainhand;
		case caps_currentArmor:
			ItemStack aromor = ((List<ItemStack>)owner.getArmorInventoryList()).get((Integer) pArg[0]);
			if (aromor.isEmpty()) aromor = null;
			return aromor;
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

		case caps_onGround:
			return owner.onGround;
		case caps_isRiding:
			return owner.isRiding();
		case caps_isRidingPlayer:
			return owner.getRidingEntity() instanceof EntityPlayer;
		case caps_isChild:
			return owner.isChild();
		case caps_isWet:
			return owner.isWet();
		case caps_isDead:
			return owner.isDead;
		case caps_isJumping:
			return false;//owner.isJumping;
		case caps_isInWeb:
			return false;//owner.isInWeb;
		case caps_isSwingInProgress:
			return owner.isSwingInProgress;
		case caps_isSneak:
			return owner.isSneaking();
//		case caps_isBlocking:
//			return owner.isBlocking();
		case caps_isBurning:
			return owner.isBurning();
		case caps_isInWater:
			return owner.isInWater();
		case caps_isInvisible:
			return owner.isInvisible();
		case caps_isSprinting:
			return owner.isSprinting();
		case caps_PosBlockID:
			return owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2]))).getBlock();
		case caps_PosBlockState:
			return owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		case caps_PosBlockAir:
			IBlockState state = owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
			//移動可能ブロックかつ通常ブロックではない
			//Block.causesSuffocationから変更
			return !(state.getMaterial().blocksMovement() && state.isFullCube());
			
		case caps_PosBlockLight:
			return owner.getEntityWorld().getBlockLightOpacity(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		case caps_PosBlockPower:
			return owner.getEntityWorld().getStrongPower(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
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
		case caps_isLeeding:
			return (owner instanceof EntityLiving) && ((EntityLiving)owner).getLeashed();
		case caps_getRidingName:
			return owner.getRidingEntity() == null ? "" : EntityList.getEntityString(owner.getRidingEntity());

		// World
		case caps_WorldTotalTime:
			return owner.getEntityWorld().getWorldInfo().getWorldTotalTime();
		case caps_WorldTime:
			return owner.getEntityWorld().getWorldInfo().getWorldTime();
		case caps_MoonPhase:
			return owner.getEntityWorld().getMoonPhase();
		case caps_TextureEntity:
			return owner;
		}

		return null;
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_health:
			owner.setHealth((Integer)pArg[0]);
			return true;
		case caps_ticksExisted:
			owner.ticksExisted = (Integer)pArg[0];
			return true;
		case caps_heldItems:
		case caps_currentEquippedItem:
			for (EntityEquipmentSlot fSlot : EntityEquipmentSlot.values()) {
				if (fSlot.getSlotIndex() == (Integer)pArg[0]) {
					owner.setItemStackToSlot(fSlot, (ItemStack) pArg[1]);
				}
			}
//			owner.setCurrentItemOrArmor((Integer)pArg[0], (ItemStack)pArg[1]);
			return true;
		case caps_currentArmor:
			for (EntityEquipmentSlot fSlot : EntityEquipmentSlot.values()) {
				if (fSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && fSlot.getIndex() == (Integer)pArg[0]) {
					owner.setItemStackToSlot(fSlot, (ItemStack) pArg[1]);
				}
			}
//			owner.setCurrentItemOrArmor((Integer)pArg[0] + 1, (ItemStack)pArg[1]);
			return true;
		case caps_posX:
			owner.posX = (Double)pArg[0];
			return true;
		case caps_posY:
			owner.posY = (Double)pArg[0];
			return true;
		case caps_posZ:
			owner.posZ = (Double)pArg[0];
			return true;
		case caps_pos:
			owner.setPosition((Double)pArg[0], (Double)pArg[1], (Double)pArg[2]);
			return true;
		case caps_motionX:
			owner.motionX = (Double)pArg[0];
			return true;
		case caps_motionY:
			owner.motionY = (Double)pArg[0];
			return true;
		case caps_motionZ:
			owner.motionZ = (Double)pArg[0];
			return true;
		case caps_motion:
			owner.setVelocity((Double)pArg[0], (Double)pArg[1], (Double)pArg[2]);
			return true;
		case caps_onGround:
			owner.onGround = (Boolean)pArg[0];
			return true;
		case caps_isRiding:
			return owner.isRiding();
//		case caps_isChild:
//		case caps_isWet:
//		case caps_isDead:
//		case caps_isJumping:
//		case caps_isInWeb:
//		case caps_isSwingInProgress:
		case caps_isSneak:
			owner.setSneaking((Boolean)pArg[0]);
//		case caps_isBlocking:
//		case caps_isBurning:
//		case caps_isInWater:
//		case caps_isInvisible:
//		case caps_isSprinting:
		}

		return false;
	}
	
	/**
	 * ModelMultiBaseへ初期値を設定する
	 * @param model
	 * @param modelCaps
	 */
	@Override
	public void setModelMultiFromModelCaps(ModelMultiBase model, float entityYaw, float partialTicks) {
		
		if (model == null) return;
		
		//メイド状態の初期化
		model.setCapsValue(IModelCaps.caps_heldItemLeft, 0);
		model.setCapsValue(IModelCaps.caps_heldItemRight, 0);
//		modelMain.setCapsValue(IModelCaps.caps_onGround, getSwingProgress(par1EntityLiving, par9));
		model.setCapsValue(IModelCaps.caps_isRiding, false);
		model.setCapsValue(IModelCaps.caps_isSneak, false);
		model.setCapsValue(IModelCaps.caps_aimedBow, false);
		model.setCapsValue(IModelCaps.caps_isWait, false);
		model.setCapsValue(IModelCaps.caps_isChild, false);
		model.setCapsValue(IModelCaps.caps_entityIdFactor, 0F);
		model.setCapsValue(IModelCaps.caps_ticksExisted, 0);
		
		//カスタム設定
		model.setCapsValue(IModelCaps.caps_motionSitting, false);
		
	}

}
