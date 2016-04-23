package net.blacklab.lmr.entity.mode;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.ai.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.Statics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EntityMode_Shearer extends EntityModeBase {

	public static final int mmode_Ripper	= 0x0081;
	public static final int mmode_TNTD		= 0x00c1;
	public static final int mmode_Detonator	= 0x00c2;

	public int timeSinceIgnited;
	public int lastTimeSinceIgnited;


	public EntityMode_Shearer(EntityLittleMaid pEntity) {
		super(pEntity);
		timeSinceIgnited = -1;
		isAnytimeUpdate = true;
	}

	@Override
	public int priority() {
		return 3100;
	}

	@Override
	public void init() {
		// 登録モードの名称追加
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Ripper", "Ripper");
		ModLoader.addLocalization("littleMaidMob.mode.F-Ripper", "F-Ripper");
		ModLoader.addLocalization("littleMaidMob.mode.D-Ripper", "D-Ripper");
		ModLoader.addLocalization("littleMaidMob.mode.T-Ripper", "T-Ripper");
		ModLoader.addLocalization("littleMaidMob.mode.Ripper", "ja_JP", "毛狩り隊");
		ModLoader.addLocalization("littleMaidMob.mode.F-Ripper", "ja_JP", "毛狩り隊");
		ModLoader.addLocalization("littleMaidMob.mode.D-Ripper", "ja_JP", "毛狩り隊");
		ModLoader.addLocalization("littleMaidMob.mode.T-Ripper", "ja_JP", "毛狩り隊");
		ModLoader.addLocalization("littleMaidMob.mode.TNT-D", "TNT-D");
		ModLoader.addLocalization("littleMaidMob.mode.F-TNT-D", "TNT-D");
		ModLoader.addLocalization("littleMaidMob.mode.D-TNT-D", "TNT-D");
		ModLoader.addLocalization("littleMaidMob.mode.T-TNT-D", "TNT-D");
//		ModLoader.addLocalization("littleMaidMob.mode.TNT-D", "ja_JP", "TNT-D");
		ModLoader.addLocalization("littleMaidMob.mode.Detonator", "Detonator");
		ModLoader.addLocalization("littleMaidMob.mode.F-Detonator", "F-Detonator");
		ModLoader.addLocalization("littleMaidMob.mode.D-Detonator", "D-Detonator");
		ModLoader.addLocalization("littleMaidMob.mode.T-Detonator", "T-Detonator");
		*/
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {


		// Ripper:0x0081
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(owner.aiProfiler);
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

		ltasks[0].addTask(1, owner.aiSwiming);
		ltasks[0].addTask(2, owner.getAISit());
		ltasks[0].addTask(3, owner.aiJumpTo);
		ltasks[0].addTask(4, owner.aiAttack);
		ltasks[0].addTask(5, owner.aiPanic);
		ltasks[0].addTask(6, owner.aiBeg);
		ltasks[0].addTask(7, owner.aiBegMove);
		ltasks[0].addTask(8, owner.aiAvoidPlayer);
//		ltasks[0].addTask(7, pentitylittlemaid.aiCloseDoor);
//		ltasks[0].addTask(8, pentitylittlemaid.aiOpenDoor);
//		ltasks[0].addTask(9, pentitylittlemaid.aiCollectItem);
		ltasks[0].addTask(10, owner.aiFollow);
//		ltasks[0].addTask(11, new EntityAILeapAtTarget(pentitylittlemaid, 0.3F));
		ltasks[0].addTask(11, owner.aiWander);
		ltasks[0].addTask(12, new EntityAIWatchClosest(owner, EntityLivingBase.class, 10F));
		ltasks[0].addTask(12, new EntityAILookIdle(owner));

		ltasks[1].addTask(1, new EntityAILMNearestAttackableTarget(owner, EntityCreeper.class, 0, true));
		ltasks[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityTNTPrimed.class, 0, true));
		ltasks[1].addTask(3, new EntityAILMNearestAttackableTarget(owner, EntitySheep.class, 0, true));

		owner.addMaidMode(ltasks, "Ripper", mmode_Ripper);


		// TNT-D:0x00c1
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = ltasks[0];
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);
		ltasks2[1].addTask(1, new EntityAILMNearestAttackableTarget(owner, EntityCreeper.class, 0, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityTNTPrimed.class, 0, true));

		owner.addMaidMode(ltasks2, "TNT-D", mmode_TNTD);


		// Detonator:0x00c2
		EntityAITasks[] ltasks3 = new EntityAITasks[2];
		ltasks3[0] = pDefaultMove;
		ltasks3[1] = new EntityAITasks(owner.aiProfiler);
		ltasks2[1].addTask(1, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(ltasks2, "Detonator", mmode_Detonator);


	}

	@Override
	public void updateAITick(int pMode) {
		ItemStack litemstack = owner.maidInventory.getCurrentItem();
		if (litemstack != null
				&& (owner.getAttackTarget() instanceof EntityCreeper/* || owner.getAttackTarget().getClass().isAssignableFrom(EntityTNTPrimed.class)*/)) {
			if (pMode == mmode_Ripper) {
				owner.setMaidMode("TNT-D");
				owner.getMaidOverDriveTime().setEnable(true);
			} else if (owner.getMaidModeInt() == mmode_TNTD && litemstack.getItem() instanceof ItemShears) {
				owner.getMaidOverDriveTime().setEnable(true);
			}
		}
		if (!owner.getMaidOverDriveTime().isEnable() && pMode == mmode_TNTD) {
			owner.setMaidMode("Ripper");
//    		getNextEquipItem();
		}
	}

	@Override
	public void onUpdate(int pMode) {
		// 自爆モード
		if (pMode == mmode_Detonator && owner.isEntityAlive()) {
			if (timeSinceIgnited < 0) {
				if (lastTimeSinceIgnited != timeSinceIgnited) {
					owner.getDataManager().set(Statics.dataWatch_Free, Integer.valueOf(0));
				}
				else if (owner.getDataManager().get(Statics.dataWatch_Free) == 1) {
					lastTimeSinceIgnited = timeSinceIgnited = 0;
				}
			}
			lastTimeSinceIgnited = timeSinceIgnited;
			if (timeSinceIgnited > -1) {
				// 最期の瞬間はセツナイ
				if (owner.isMovementBlocked() || timeSinceIgnited > 22) {
					owner.getLookHelper().setLookPositionWithEntity(owner.getMaidMasterEntity(), 40F, 40F);
				}
				LittleMaidReengaged.Debug(String.format("ID:%d(%s)-dom:%d(%d)", owner.getEntityId(), owner.worldObj.isRemote ? "C" : "W", owner.getDominantArm(), owner.maidInventory.currentItem));

				if (owner.maidInventory.isItemExplord(owner.maidInventory.currentItem) && timeSinceIgnited++ > 30) {
					// TODO:自爆威力を対応させたいけど無理ぽ？
					owner.maidInventory.decrStackSize(owner.maidInventory.currentItem, 1);
					// インベントリをブチマケロ！
					owner.maidInventory.dropAllItems(true);
					timeSinceIgnited = -1;
					owner.setDead();
					// Mobによる破壊の是非
					boolean lflag = owner.worldObj.getGameRules().getBoolean("mobGriefing");
					owner.worldObj.createExplosion(owner, owner.posX, owner.posY, owner.posZ, 3F, lflag);
				}
			}
		}
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemShears) {
				owner.setMaidMode("Ripper");
				if (AchievementsLMRE.ac_Shearer != null) {
					pentityplayer.addStat(AchievementsLMRE.ac_Shearer);
				}
				return true;
			}
			if (owner.maidInventory.isItemExplord(0)) {
				owner.setMaidMode("Detonator");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Ripper :
			owner.setBloodsuck(false);
			return true;
		case mmode_TNTD :
			owner.setBloodsuck(false);
			return true;
		case mmode_Detonator :
			owner.setBloodsuck(true);
//			owner.aiPanic.
			timeSinceIgnited = -1;

			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Ripper :
		case mmode_TNTD :
			for (li = 0; li < InventoryLittleMaid.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// はさみ
				if (litemstack.getItem() instanceof ItemShears) {
					return li;
				}
			}
			break;
		case mmode_Detonator :
			for (li = 0; li < InventoryLittleMaid.maxInventorySize; li++) {
				// 爆発物
				if (owner.maidInventory.isItemExplord(li)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}



	@Override
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		if (pMode == mmode_Detonator) {
			// 通常殴り
			return false;
		}

		if (owner.getSwingStatusDominant().canAttack()) {
			ItemStack lis = owner.getCurrentEquippedItem();
			if (pEntity instanceof EntityCreeper) {
				// なんでPrivateにかえたし
				try {
					lis.damageItem((Integer)ObfuscationReflectionHelper.getPrivateValue(EntityCreeper.class,
							(EntityCreeper)pEntity, "field_70833_d", "timeSinceIgnited"), owner);
//							(EntityCreeper)pEntity, 1), owner.maidAvatar);
					ObfuscationReflectionHelper.setPrivateValue(EntityCreeper.class, (EntityCreeper)pEntity, 1, "field_70833_d", "timeSinceIgnited");
				} catch (Exception e) {
					e.printStackTrace();
				}
//				((EntityCreeper)pEntity).timeSinceIgnited = 0;
				owner.setSwing(20, EnumSound.attack_bloodsuck, false);
				owner.addMaidExperience(0.6f);
			} else if (pEntity instanceof EntityTNTPrimed) {
				pEntity.setDead();
				lis.damageItem(1, owner);
//				lis.damageItem(1, owner.maidAvatar);
				owner.setSwing(20, EnumSound.attack_bloodsuck, false);
				owner.addMaidExperience(4.5f);
			} else {
				owner.maidAvatar.interact(pEntity, owner.getCurrentEquippedItem(), EnumHand.MAIN_HAND);
				owner.setSwing(20, EnumSound.attack, false);
				owner.addMaidExperience(2.1f);
			}
			if (lis.stackSize <= 0) {
				owner.maidInventory.setInventoryCurrentSlotContents(null);
				owner.getNextEquipItem();
			}
		}

		return true;
	}

	@Override
	public boolean isSearchEntity() {
		return true;
	}

	@Override
	public boolean checkEntity(int pMode, Entity pEntity) {
		if (owner.maidInventory.currentItem < 0) {
			return false;
		}
		switch (pMode) {
		case mmode_Detonator :
			return !owner.getIFF(pEntity);
		case mmode_Ripper :
			if (pEntity instanceof EntitySheep) {
				EntitySheep les = (EntitySheep)pEntity;
				if (!les.getSheared() && !les.isChild()) {
					return true;
				}
			}
		case mmode_TNTD :
			if (pEntity instanceof EntityCreeper) {
				return true;
			}
			if (pEntity instanceof EntityTNTPrimed) {
				return true;
			}
			break;
		}

		return false;
	}

	protected float setLittleMaidFlashTime(float f) {
		// 爆発カウントダウン発光時間
		if (timeSinceIgnited > -1) {
			return (this.lastTimeSinceIgnited + (this.timeSinceIgnited - this.lastTimeSinceIgnited) * f) / 28.0F;
		}
		return 0F;
	}

	@Override
	public int colorMultiplier(float pLight, float pPartialTicks) {
		float f2 = setLittleMaidFlashTime(pPartialTicks);

		if((int)(f2 * 10F) % 2 == 0) {
			return 0;
		}
		int i = (int)(f2 * 0.2F * 255F);
		if(i < 0)
		{
			i = 0;
		}
		if(i > 255)
		{
			i = 255;
		}
		LittleMaidReengaged.Debug(String.format("%2x", i));
		char c = '\377';
		char c1 = '\377';
		char c2 = '\377';
		return i << 24 | c << 16 | c1 << 8 | c2;
	}

	@Override
	public boolean damageEntity(int pMode, DamageSource par1DamageSource, float par2) {
		// 起爆
		if (pMode == mmode_Detonator && InventoryLittleMaid.isItemExplord(owner.getCurrentEquippedItem())) {
			if (timeSinceIgnited == -1) {
				owner.playSound(SoundEvent.soundEventRegistry.getObject(new ResourceLocation("entity.tnt.primed")), 1.0F, 0.5F);
				owner.getDataManager().set(Statics.dataWatch_Free, Integer.valueOf(1));
			}
//        	if (owner.entityToAttack == null)
			owner.setMaidWait(true);
		}

		return false;
	}

}
