package net.blacklab.lmr.entity.littlemaid.mode;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ai.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.helper.ItemHelper;
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

/**
 * 毛狩りとクリーパー無効化
 *
 */
public class EntityMode_Shearer extends EntityModeBase {

	public static final String mmode_Ripper		= "Shearer";
	public static final String mmode_TNTD		= "TNT-D";
	public static final String mmode_Detonator	= "Detonator";

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
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {


		// Ripper:0x0081
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(owner.aiProfiler);
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

		ltasks[0].addTask(1, owner.aiSwiming);
		ltasks[0].addTask(2, owner.getAISit());
//		ltasks[0].addTask(3, owner.aiJumpTo);
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

		ltasks[1].addTask(1, new EntityAILMNearestAttackableTarget<>(owner, EntityCreeper.class, 0, true));
		ltasks[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityTNTPrimed.class, 0, true));
		ltasks[1].addTask(3, new EntityAILMNearestAttackableTarget<>(owner, EntitySheep.class, 0, true));

		owner.addMaidMode(mmode_Ripper, ltasks);


		// TNT-D:0x00c1
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = ltasks[0];
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);
		ltasks2[1].addTask(1, new EntityAILMNearestAttackableTarget<>(owner, EntityCreeper.class, 0, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityTNTPrimed.class, 0, true));

		owner.addMaidMode(mmode_TNTD, ltasks2);


		// Detonator:0x00c2
		EntityAITasks[] ltasks3 = new EntityAITasks[2];
		ltasks3[0] = pDefaultMove;
		ltasks3[1] = new EntityAITasks(owner.aiProfiler);
		ltasks2[1].addTask(1, new EntityAILMNearestAttackableTarget<>(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Detonator, ltasks3);


	}

	@Override
	public void updateAITick(String pMode) {
		ItemStack litemstack = owner.maidInventory.getCurrentItem();
		if (!litemstack.isEmpty()
				&& (owner.getAttackTarget() instanceof EntityCreeper/* || owner.getAttackTarget().getClass().isAssignableFrom(EntityTNTPrimed.class)*/)) {
			if (pMode.equals(mmode_Ripper)) {
				owner.setMaidMode(mmode_TNTD);
				owner.getMaidOverDriveTime().setEnable(true);
			} else if (owner.getMaidModeString().equals(mmode_TNTD) && litemstack.getItem() instanceof ItemShears) {
				owner.getMaidOverDriveTime().setEnable(true);
			}
		}
		if (!owner.getMaidOverDriveTime().isEnable() && pMode == mmode_TNTD) {
			owner.setMaidMode(mmode_Ripper);
//    		getNextEquipItem();
		}
		
		//一定時間後にターゲット解除
		this.resetTarget(pMode);
	}

	@Override
	public void onUpdate(String pMode) {
		// 自爆モード
		if (pMode.equals(mmode_Detonator) && owner.isEntityAlive()) {
			if (timeSinceIgnited < 0) {
				if (lastTimeSinceIgnited != timeSinceIgnited) {
					owner.getDataManager().set(EntityLittleMaid.dataWatch_Free, Integer.valueOf(0));
				}
				else if (owner.getDataManager().get(EntityLittleMaid.dataWatch_Free) == 1) {
					lastTimeSinceIgnited = timeSinceIgnited = 0;
				}
			}
			lastTimeSinceIgnited = timeSinceIgnited;
			if (timeSinceIgnited > -1) {
				// 最期の瞬間はセツナイ
				if (owner.isMovementBlocked() || timeSinceIgnited > 22) {
					owner.getLookHelper().setLookPositionWithEntity(owner.getMaidMasterEntity(), 40F, 40F);
				}
				LittleMaidReengaged.Debug(String.format("ID:%d(%s)-dom:%d(%d)", owner.getEntityId(), owner.getEntityWorld().isRemote ? "C" : "W", owner.getDominantArm(), owner.maidInventory.getCurrentItemIndex()));

				if (owner.maidInventory.isItemExplord(owner.maidInventory.getCurrentItemIndex()) && timeSinceIgnited++ > 30) {
					// TODO:自爆威力を対応させたいけど無理ぽ？
					owner.maidInventory.decrStackSize(owner.maidInventory.getCurrentItemIndex(), 1);
					// インベントリをブチマケロ！
					owner.maidInventory.dropAllItems(true);
					timeSinceIgnited = -1;
					owner.setDead();
					// Mobによる破壊の是非
					boolean lflag = owner.getEntityWorld().getGameRules().getBoolean("mobGriefing");
					owner.getEntityWorld().createExplosion(owner, owner.posX, owner.posY, owner.posZ, 3F, lflag);
				}
			}
		}
		
		//運びモードへの切り替え判定
		if (pMode.equals(mmode_Ripper) && owner.ticksExisted % 200 == 0) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_RipperPorter);
			}
		}
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();;
		if (!litemstack.isEmpty()) {
			if (litemstack.getItem() instanceof ItemShears) {
				owner.setMaidMode(mmode_Ripper);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.Ripper);
				return true;
			}
			if (ItemHelper.isItemExplord(litemstack)) {
				owner.setMaidMode(mmode_Detonator);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
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
	public int getNextEquipItem(String pMode) {
		int li;
		if ((li = super.getNextEquipItem(pMode)) >= 0) {
			return li;
		}

		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Ripper :
		case mmode_TNTD :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;

				// はさみ
				if (isTriggerItem(pMode, litemstack)) {
					return li;
				}
			}
			break;
		case mmode_Detonator :
			for (li = 0; li < owner.maidInventory.getSizeInventory(); li++) {
				// 爆発物
				if (isTriggerItem(pMode, owner.maidInventory.getStackInSlot(li))) {
					return li;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	protected boolean isTriggerItem(String pMode, ItemStack par1ItemStack) {
		if (par1ItemStack.isEmpty()) {
			return false;
		}

		switch (pMode) {
		case mmode_Ripper:
		case mmode_TNTD:
			return par1ItemStack.getItem() instanceof ItemShears;
		case mmode_Detonator:
			return ItemHelper.isItemExplord(par1ItemStack);
		}
		return super.isTriggerItem(pMode, par1ItemStack);
	}

	@Override
	public boolean attackEntityAsMob(String pMode, Entity pEntity) {
		if (pMode.equals(mmode_Detonator)) {
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
				owner.maidAvatar.interactOn(pEntity, EnumHand.MAIN_HAND);
				owner.setSwing(20, EnumSound.attack, false);
				owner.addMaidExperience(2.1f);
			}
			if (lis.getCount() <= 0) {
				owner.maidInventory.setInventoryCurrentSlotContents(ItemStack.EMPTY);
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
	public boolean checkEntity(String pMode, Entity pEntity) {
		if (owner.maidInventory.getCurrentItemIndex() < 0) {
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
	public boolean damageEntity(String pMode, DamageSource par1DamageSource, float par2) {
		// 起爆
		if (pMode.equals(mmode_Detonator) && ItemHelper.isItemExplord(owner.getCurrentEquippedItem())) {
			if (timeSinceIgnited == -1) {
				owner.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.tnt.primed")), 1.0F, 0.5F);
				owner.getDataManager().set(EntityLittleMaid.dataWatch_Free, Integer.valueOf(1));
			}
//        	if (owner.entityToAttack == null)
			owner.setMaidWait(true);
		}

		return false;
	}

	
	private static final int resetTargetTime = 200;
	private int resetTargetTimeCount = resetTargetTime;
	
	/**
	 * 一定時間ごとにターゲットを初期化する
	 * @param pMode
	 */
	private void resetTarget(String pMode) {
		//毛刈りモードのみ
		if (mmode_Ripper.equals(pMode)) {
			if (owner.getAttackTarget() != null) {
				resetTargetTimeCount--;
				//リセットタイム
				if (resetTargetTimeCount <= 0) {
					owner.setAttackTarget(null);
				}
			} else {
				resetTargetTimeCount = resetTargetTime;
			}
		}
	}
	
	/**
	 * isTriggerItemを使う場合はisTriggerItem側で職業判定をちゃんとやっていること
	 */
	@Override
	public boolean isCancelPutChestItemStack(String pMode, ItemStack stack, int slotIndedx) {
		
		String mode = pMode;
		if (EntityMode_Basic.mmode_RipperPorter.equals(pMode)) {
			mode = mmode_Ripper;
		}
		
		return this.isTriggerItem(mode, stack);
	}
	
}
