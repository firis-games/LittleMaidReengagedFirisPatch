package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.entity.ai.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.ai.EntityAILMNearestAttackableTarget;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTrigger;
import net.blacklab.lmr.util.helper.MaidHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMode_Archer extends EntityModeBase {

	public static final String mmode_Archer			= "SYS:Archer";
	public static final String mmode_Blazingstar	= "SYS:BlazingStar";
	
	public static final String mtrigger_Bow			= "Archer:Bow";
	public static final String mtrigger_Arrow		= "Archer:Arrow";

	@Override
	public int priority() {
		return 3200;
	}

	public EntityMode_Archer(EntityLittleMaid pEntity) {
		super(pEntity);
		isAnytimeUpdate = true;
	}

	@Override
	public void init() {
		// 登録モードの名称追加
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Archer", "Archer");
		ModLoader.addLocalization("littleMaidMob.mode.F-Archer", "F-Archer");
		ModLoader.addLocalization("littleMaidMob.mode.T-Archer", "T-Archer");
		ModLoader.addLocalization("littleMaidMob.mode.D-Archer", "D-Archer");
//		ModLoader.addLocalization("littleMaidMob.mode.Archer", "ja_JP", "射手");
		ModLoader.addLocalization("littleMaidMob.mode.Blazingstar", "Blazingstar");
		ModLoader.addLocalization("littleMaidMob.mode.F-Blazingstar", "F-Blazingstar");
		ModLoader.addLocalization("littleMaidMob.mode.T-Blazingstar", "T-Blazingstar");
		ModLoader.addLocalization("littleMaidMob.mode.D-Blazingstar", "D-Blazingstar");
//		ModLoader.addLocalization("littleMaidMob.mode.Blazingstar", "ja_JP", "刃鳴散らす者");
		*/
		ModeTrigger.registerTrigger(mtrigger_Bow, new HashMap<>());
		ModeTrigger.registerTrigger(mtrigger_Arrow, new HashMap<>());
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Archer:0x0083
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

//		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
//		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new EntityAILMHurtByTarget(owner, true));
		ltasks[1].addTask(4, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Archer, ltasks);

		// Blazingstar:0x00c3
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);

		ltasks2[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Blazingstar, ltasks2);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();

		if (litemstack != null) {
			Item item = litemstack.getItem();
			
			if (owner.getModeTrigger().isTriggerable(mtrigger_Bow, item, item instanceof ItemBow)) {
				if (owner.maidInventory.getInventorySlotContainItem(ItemFlintAndSteel.class) > -1) {
					owner.setMaidMode(mmode_Blazingstar);
					if (pentityplayer != null) {
						pentityplayer.addStat(AchievementsLMRE.ac_BlazingStar);
					}
				} else {
					owner.setMaidMode(mmode_Archer);
					if (pentityplayer != null) {
						pentityplayer.addStat(AchievementsLMRE.ac_Archer);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mmode_Archer :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(false);
			return true;
		case mmode_Blazingstar :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(true);
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
		case mmode_Archer :
		case mmode_Blazingstar :
			// Except off hand slot
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 射手
				if (isTriggerItem(pMode, litemstack)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	protected boolean isTriggerItem(String pMode, ItemStack par1ItemStack) {
		if (par1ItemStack == null) {
			return false;
		}
		
		Item item = par1ItemStack.getItem();
		return owner.getModeTrigger().isTriggerable(mtrigger_Bow, item, item instanceof ItemBow);
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		if (pItemStack == null) {
			return false;
		}
		
		Item item = pItemStack.getItem();
		return owner.getModeTrigger().isTriggerable(mtrigger_Bow, item, item instanceof ItemBow)
				/* || TriggerSelect.checkTrigger(ls, "Bow", pItemStack.getItem())*/;
	}

	@Override
	public boolean isSearchEntity() {
		return true;
	}

	@Override
	public boolean checkEntity(String pMode, Entity pEntity) {
		if (owner.maidInventory.getInventorySlotContainItem(ItemArrow.class) < 0) return false;
		if (!MaidHelper.isTargetReachable(owner, pEntity, 18 * 18)) return false;

		return !owner.getIFF(pEntity);
	}

	@Override
	public void onUpdate(String pMode) {
		switch (pMode) {
		case mmode_Archer:
		case mmode_Blazingstar:
			owner.getWeaponStatus();
//			updateGuns();
		}

	}

	@Override
	public void updateAITick(String pMode) {
		if (owner.maidInventory.getInventorySlotContainItem(ItemArrow.class) < 0) {
			owner.setAttackTarget(null);
		}

		switch (pMode) {
		case mmode_Archer:
//			owner.getWeaponStatus();
//			updateGuns();
			break;
		case mmode_Blazingstar:
//			owner.getWeaponStatus();
//			updateGuns();
			// Blazingstarの特殊効果
			World lworld = owner.worldObj;
			List<Entity> llist = lworld.getEntitiesWithinAABB(Entity.class, owner.getEntityBoundingBox().expand(16D, 16D, 16D));
			for (int li = 0; li < llist.size(); li++) {
				Entity lentity = llist.get(li);
				if (lentity.isEntityAlive() && lentity.isBurning() && owner.getRNG().nextFloat() > 0.9F) {
					// 発火！
					int lx = (int)owner.posX;
					int ly = (int)owner.posY;
					int lz = (int)owner.posZ;

					IBlockState iState;
					if (lworld.isAirBlock(new BlockPos(lx, ly, lz)) || (iState = lworld.getBlockState(new BlockPos(lx, ly, lz))).getBlock().getMaterial(iState).getCanBurn()) {
						lworld.playSound(lx + 0.5D, ly + 0.5D, lz + 0.5D, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.firecharge.use")), SoundCategory.NEUTRAL, 1.0F, owner.getRNG().nextFloat() * 0.4F + 0.8F, false);
						lworld.setBlockState(new BlockPos(lx, ly, lz), Blocks.FIRE.getDefaultState());
					}
				}
			}
		}
	}

	protected void updateGuns() {
		if (owner.getAttackTarget() == null || !owner.getAttackTarget().isEntityAlive()) {
			// 対象が死んだ
			if (!owner.weaponReload) {
				if (owner.maidAvatar.isHandActive()) {
					// ターゲットが死んでいる時はアイテムの使用をクリア
//					if (owner.getAvatarIF().getIsItemReload()) {
						owner.maidAvatar.stopActiveHand();
//						LittleMaidReengaged.Debug(String.format("id:%d cancel reload.", owner.getEntityId()));
//					} else {
//						owner.maidAvatar.clearItemInUse();
						LittleMaidReengaged.Debug(String.format("id:%d clear.", owner.getEntityId()));
//					}
				}
			} else {
				owner.mstatAimeBow = true;
			}
		}
		if (owner.weaponReload && !owner.maidAvatar.isHandActive()) {
			// 特殊リロード
			owner.maidInventory.getCurrentItem().useItemRightClick(owner.worldObj, owner.maidAvatar, EnumHand.MAIN_HAND);
			LittleMaidReengaged.Debug("id:%d force reload.", owner.getEntityId());
			owner.mstatAimeBow = true;
		}

	}

	@Override
	public double getDistanceToSearchTargets() {
		return 24d;
	}

	@Override
	public double getLimitRangeSqOnFollow() {
		return 16 * 16;
	}

	@Override
	public double getFreedomTrackingRangeSq() {
		return 21 * 21;
	}

}
