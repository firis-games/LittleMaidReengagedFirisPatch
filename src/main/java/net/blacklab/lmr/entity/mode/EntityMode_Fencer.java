package net.blacklab.lmr.entity.mode;

import net.blacklab.lmr.achievements.LMMNX_Achievements;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.ai.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.ai.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.TriggerSelect;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * 独自基準としてモード定数は0x0080は平常、0x00c0は血まみれモードと区別。
 */
public class EntityMode_Fencer extends EntityModeBase {

	public static final int mmode_Fencer		= 0x0080;
	public static final int mmode_Bloodsucker	= 0x00c0;


	public EntityMode_Fencer(EntityLittleMaid pEntity) {
		super(pEntity);
		isAnytimeUpdate = true;
	}

	@Override
	public int priority() {
		return 3000;
	}

	@Override
	public void init() {
		// 登録モードの名称追加
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "ja_JP", "護衛剣士");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "F-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "ja_JP", "自由剣士");
		ModLoader.addLocalization("littleMaidMob.mode.T-Fencer", "T-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.D-Fencer", "D-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "ja_JP", "血に飢えた冥土");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "F-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "ja_JP", "通魔冥土");
		ModLoader.addLocalization("littleMaidMob.mode.T-Bloodsucker", "T-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.D-Bloodsucker", "D-Bloodsucker");
		*/
		TriggerSelect.appendTriggerItem(null, "Sword", "");
		TriggerSelect.appendTriggerItem(null, "Axe", "");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Fencer:0x0080
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
//		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
//		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new EntityAILMHurtByTarget(owner, true));
		ltasks[1].addTask(4, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));
		
		owner.addMaidMode(ltasks, "Fencer", mmode_Fencer);
		
		
		// Bloodsucker:0x00c0
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);
		
		ltasks2[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));
		
		owner.addMaidMode(ltasks2, "Bloodsucker", mmode_Bloodsucker);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemSword || TriggerSelect.checkWeapon(owner.getMaidMasterUUID(), "Sword", litemstack)) {
				owner.setMaidMode("Fencer");
				if (LMMNX_Achievements.ac_Fencer != null) {
					pentityplayer.addStat(LMMNX_Achievements.ac_Fencer);
				}
				if (litemstack.getItem() instanceof ItemSpade && LMMNX_Achievements.ac_Buster != null) {
					pentityplayer.addStat(LMMNX_Achievements.ac_Buster);
				}
				return true;
			} else  if (litemstack.getItem() instanceof ItemAxe || TriggerSelect.checkWeapon(owner.getMaidMasterUUID(), "Axe", litemstack)) {
				owner.setMaidMode("Bloodsucker");
				if (LMMNX_Achievements.ac_RandomKiller != null) {
					pentityplayer.addStat(LMMNX_Achievements.ac_RandomKiller);
				}
				if (litemstack.getItem() instanceof ItemSpade && LMMNX_Achievements.ac_Buster != null) {
					pentityplayer.addStat(LMMNX_Achievements.ac_Buster);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Fencer :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(false);
			owner.aiAttack.isGuard = true;
			return true;
		case mmode_Bloodsucker :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(true);
			return true;
		}
		
		return false;
	}
	
	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		int ll = -1;
		double ld = 0;
		double lld;
		ItemStack litemstack;
		
		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Fencer : 
			for (li = 0; li < InventoryLittleMaid.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// 剣
				if (litemstack.getItem() instanceof ItemSword || TriggerSelect.checkWeapon(owner.getMaidMasterUUID(), "Sword", litemstack)) {
					return li;
				}
				
				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = CommonHelper.getAttackVSEntity(litemstack);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		case mmode_Bloodsucker :
			for (li = 0; li < InventoryLittleMaid.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// 斧
				if (litemstack.getItem() instanceof ItemAxe || TriggerSelect.checkWeapon(owner.getMaidMasterUUID(), "Axe", litemstack)) {
					return li;
				}
				
				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = CommonHelper.getAttackVSEntity(litemstack);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		}
		
		return ll;
	}
	
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		// 装備アイテムを回収
		return pItemStack.getItem() instanceof ItemSword || pItemStack.getItem() instanceof ItemAxe;
	}

}
