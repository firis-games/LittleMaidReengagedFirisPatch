package net.blacklab.lmr.entity.littlemaid.mode;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.DevMode;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityMode_Debug extends EntityMode_Basic {

	public static final String mmode_Debug = "D:DEBUG";

	public EntityMode_Debug(EntityLittleMaid pEntity) {
		super(pEntity);
		isAnytimeUpdate = true;
	}

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public void init() {
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;

		if(DevMode.DEVELOPMENT_DEBUG_MODE) owner.addMaidMode(mmode_Debug, ltasks);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		if(!DevMode.DEVELOPMENT_DEBUG_MODE) return false;
		ItemStack litemstackl0 = owner.maidInventory.getStackInSlot(17);
		ItemStack litemstackl1 = owner.maidInventory.getStackInSlot(16);
		if (litemstackl0 != null && litemstackl1 != null) {
			if (litemstackl0.getItem() == LittleMaidReengaged.spawnEgg && litemstackl1.getItem() == Item.getItemFromBlock(Blocks.BARRIER)) {
				owner.setMaidMode(mmode_Debug);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
		if(!DevMode.DEVELOPMENT_DEBUG_MODE) return false;
		switch (pMode) {
		case mmode_Debug :
			owner.setBloodsuck(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			return true;
		}

		return false;
	}

	@Override
	public void updateAITick(String pMode) {
		super.updateAITick(pMode);
		if (pMode.equals(mmode_Debug)) {
			owner.addMaidExperience(10f);
			/*
			try {
				int limit = (Integer) ObfuscationReflectionHelper.getPrivateValue(LMM_EntityLittleMaid.class, owner, "maidContractLimit");
				limit-=5000;
				if (limit<0) {
					limit = 0;
				}
				ObfuscationReflectionHelper.setPrivateValue(LMM_EntityLittleMaid.class, owner, limit, "maidContractLimit");
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
			}
			*/
		}
	}

}
