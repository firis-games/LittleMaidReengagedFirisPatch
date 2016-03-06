package mmmlibx.lib;

import java.util.HashMap;
import java.util.Map;

import mmmlibx.lib.multiModel.model.mc162.IModelCaps;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class MMM_EntityCaps implements IModelCaps {

	protected EntityLivingBase owner;
	private static Map<String, Integer> caps;

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

	public static Map<String, Integer> getStaticModelCaps() {
		return caps;
	}

	public MMM_EntityCaps(EntityLivingBase pOwner) {
		owner = pOwner;
	}

	@Override
	public Map<String, Integer> getModelCaps() {
		return caps;
	}

	@Override
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_Entity:
			return owner;
		case caps_health:
			return (int)owner.getHealth();
		case caps_healthFloat:
			return owner.getHealth();
		case caps_ticksExisted:
			return owner.ticksExisted;
		case caps_heldItems:
		case caps_currentEquippedItem:
			return owner.getHeldItem();
		case caps_currentArmor:
			return owner.getEquipmentInSlot((Integer)pArg[0] + 1);
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
			return owner.ridingEntity instanceof EntityPlayer;
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
			return owner.worldObj.getBlockState(new BlockPos(
					MathHelper.floor_double(owner.posX + (Double)pArg[0]),
					MathHelper.floor_double(owner.posY + (Double)pArg[1]),
					MathHelper.floor_double(owner.posZ + (Double)pArg[2]))).getBlock();
		case caps_PosBlockState:
			return owner.worldObj.getBlockState(new BlockPos(
					MathHelper.floor_double(owner.posX + (Double)pArg[0]),
					MathHelper.floor_double(owner.posY + (Double)pArg[1]),
					MathHelper.floor_double(owner.posZ + (Double)pArg[2])));
		case caps_PosBlockAir:
			return !owner.worldObj.getBlockState(new BlockPos(
					MathHelper.floor_double(owner.posX + (Double)pArg[0]),
					MathHelper.floor_double(owner.posY + (Double)pArg[1]),
					MathHelper.floor_double(owner.posZ + (Double)pArg[2]))).getBlock().isVisuallyOpaque();
		case caps_PosBlockLight:
			return owner.worldObj.getBlockLightOpacity(new BlockPos(
					MathHelper.floor_double(owner.posX + (Double)pArg[0]),
					MathHelper.floor_double(owner.posY + (Double)pArg[1]),
					MathHelper.floor_double(owner.posZ + (Double)pArg[2])));
		case caps_PosBlockPower:
			return owner.worldObj.getStrongPower(new BlockPos(
					MathHelper.floor_double(owner.posX + (Double)pArg[0]),
					MathHelper.floor_double(owner.posY + (Double)pArg[1]),
					MathHelper.floor_double(owner.posZ + (Double)pArg[2])));
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
			return owner.ridingEntity == null ? "" : EntityList.getEntityString(owner.ridingEntity);
		
		// World
		case caps_WorldTotalTime:
			return owner.worldObj.getWorldInfo().getWorldTotalTime();
		case caps_WorldTime:
			return owner.worldObj.getWorldInfo().getWorldTime();
		case caps_MoonPhase:
			return owner.worldObj.getMoonPhase();
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
			owner.setCurrentItemOrArmor((Integer)pArg[0], (ItemStack)pArg[1]);
			return true;
		case caps_currentArmor:
			owner.setCurrentItemOrArmor((Integer)pArg[0] + 1, (ItemStack)pArg[1]);
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

}
