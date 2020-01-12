package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.HashMap;
import java.util.List;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.config.LMRConfig;
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

/**
 * 弓士メイドさん
 *
 */
public class EntityMode_Archer extends EntityModeBase {

	public static final String mmode_Archer			= "Archer";
	public static final String mmode_Blazingstar	= "BlazingStar";
	
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
		ltasks[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks[1].addTask(2, new EntityAILMNearestAttackableTarget<EntityLivingBase>(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Archer, ltasks);

		// Blazingstar:0x00c3
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);

		ltasks2[1].addTask(1, new EntityAILMHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new EntityAILMNearestAttackableTarget<EntityLivingBase>(owner, EntityLivingBase.class, 0, true));

		owner.addMaidMode(mmode_Blazingstar, ltasks2);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();

		if (!litemstack.isEmpty()) {
			Item item = litemstack.getItem();
			
			if (owner.getModeTrigger().isTriggerable(mtrigger_Bow, item, item instanceof ItemBow)) {
				if (owner.maidInventory.getInventorySlotContainItem(ItemFlintAndSteel.class) > -1) {
					owner.setMaidMode(mmode_Blazingstar);
					//進捗
					AchievementsLMRE.grantAC(pentityplayer, AC.BlazingStar);
				} else {
					owner.setMaidMode(mmode_Archer);
					//進捗
					AchievementsLMRE.grantAC(pentityplayer, AC.Archer);
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
				if (litemstack.isEmpty()) continue;

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
		if (par1ItemStack.isEmpty()) {
			return false;
		}
		
		Item item = par1ItemStack.getItem();
		return owner.getModeTrigger().isTriggerable(mtrigger_Bow, item, item instanceof ItemBow);
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		if (pItemStack.isEmpty()) {
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
		if (!isInventoryArrowItem()) return false;
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

	@SuppressWarnings("deprecation")
	@Override
	public void updateAITick(String pMode) {
		if (!isInventoryArrowItem()) {
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
			World lworld = owner.getEntityWorld();
			List<Entity> llist = lworld.getEntitiesWithinAABB(Entity.class, owner.getEntityBoundingBox().grow(16D, 16D, 16D));
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
			owner.maidInventory.getCurrentItem().useItemRightClick(owner.getEntityWorld(), owner.maidAvatar, EnumHand.MAIN_HAND);
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
	
	/**
	 * 対象アイテムが苗木判定か確認する
	 */
	private boolean isInventoryArrowItem() {
		
		//矢ｸﾗｽ判定
		if (!(owner.maidInventory.getInventorySlotContainItem(ItemArrow.class) < 0)) {
			return true;
		}
		//ItemIdで判定
		for (String itemId : LMRConfig.cfg_ac_arrow_item_ids) {
			if (!(owner.maidInventory.getInventorySlotContainItemId(itemId) < 0)) {
				return true;
			}			
		}
		return false;
	}

}
