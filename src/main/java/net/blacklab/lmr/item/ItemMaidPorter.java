package net.blacklab.lmr.item;

import java.util.List;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.experience.ExperienceUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMaidPorter extends Item {
	public ItemMaidPorter() {
		setMaxStackSize(1);
		setUnlocalizedName(LittleMaidReengaged.DOMAIN + ":maidporter");
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		NBTTagCompound stackTag = stack.getTagCompound();
		if (stackTag != null) {
			String customName = stackTag.getString(LittleMaidReengaged.DOMAIN + ":MAID_NAME");
			float experience = stackTag.getFloat(LittleMaidReengaged.DOMAIN + ":EXPERIENCE");
			
			if (!customName.isEmpty()) {
				tooltip.add("Name: ".concat(customName));
			}
			tooltip.add(String.format("Level: %3d", ExperienceUtil.getLevelFromExp(experience)));
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		}
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound != null) {
			if (worldIn.isAirBlock(pos.add(0, 1, 0)) && worldIn.isAirBlock(pos.add(0, 2, 0))) {
				String customName = tagCompound.getString(LittleMaidReengaged.DOMAIN + ":MAID_NAME");
				float experience = tagCompound.getFloat(LittleMaidReengaged.DOMAIN + ":EXPERIENCE");
				
				EntityLittleMaid lMaid = new EntityLittleMaid(worldIn) {
					@Deprecated
					public EntityLittleMaid addMaidExperienceWithoutEvent(float value) {
						maidExperience += value;
						return this;
					}
				}.addMaidExperienceWithoutEvent(experience);
				lMaid.setLocationAndAngles(pos.getX(), pos.getY()+1, pos.getZ(), 0, 0);
				worldIn.spawnEntityInWorld(lMaid);
				lMaid.processInteract(playerIn, EnumHand.MAIN_HAND, new ItemStack(Items.cake));
				
				if (!customName.isEmpty()) {
					lMaid.setCustomNameTag(customName);
				}
				lMaid.maidInventory.clear();
				lMaid.maidInventory.readFromNBT(tagCompound.getTagList(LittleMaidReengaged.DOMAIN + ":MAID_INVENTORY", 10));
				
				lMaid.setTextureNameMain(tagCompound.getString(LittleMaidReengaged.DOMAIN + ":MAIN_MODEL_NAME"));
				lMaid.setTextureNameArmor(tagCompound.getString(LittleMaidReengaged.DOMAIN + ":ARMOR_MODEL_NAME"));
				lMaid.setColor(tagCompound.getInteger(LittleMaidReengaged.DOMAIN + ":MAID_COLOR"));
			} else {
				return EnumActionResult.PASS;
			}
		}
		playerIn.setItemStackToSlot(hand==EnumHand.OFF_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND, null);
		return EnumActionResult.SUCCESS;
	}
}
