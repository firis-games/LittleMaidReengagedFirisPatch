package net.blacklab.lmr.entity.experience;

import java.util.UUID;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.api.item.LMMNX_API_Item;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.mode.EntityMode_DeathWait;
import net.blacklab.lmr.network.LMMNX_NetSync;
import net.blacklab.lmr.util.MaidHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class ExperienceHandler {

	protected EntityLittleMaid theMaid;

	private static final String uuidString = "NX_EXP_HP_BOOSTER";
	public static final UUID NX_EXP_HP_BOOSTER = UUID.nameUUIDFromBytes(uuidString.getBytes());

	private boolean isWaitRevive = false;
	// 死亡までの猶予時間
	private int deathCount = 0;
	// 復帰までに最低限必要になる時間
	private int pauseCount = 0;
	private int requiredSugarToRevive = 0;
	private DamageSource deadCause = DamageSource.generic;

	public ExperienceHandler(EntityLittleMaid maid) {
		theMaid = maid;
	}

	public void onLevelUp(int level) {
		/*
		 * 報酬付与・固定アイテム
		 */
		if (level%20 == 0) {
			MaidHelper.giveItem(new ItemStack(Items.name_tag), theMaid);
		}
		if (level%50 == 0) {
			MaidHelper.giveItem(new ItemStack(Items.emerald, level/50), theMaid);
		}
		if (level%100 == 0) {
			MaidHelper.giveItem(new ItemStack(Items.diamond, level/100), theMaid);
		}
		if (level%150 == 0) {
			MaidHelper.giveItem(new ItemStack(Items.nether_star, 1), theMaid);
		}

		/*
		 * 最大HP上昇
		 */
		double modifyamount = 0;
		double prevamount = 0;
		if (level > 30) {
			modifyamount += (Math.min(level, 50)-30)/2;
		}
		if (level > 50) {
			modifyamount += MathHelper.floor_float((Math.min(level, 75)-50)/2.5f);
		}
		if (level > 75) {
			modifyamount += MathHelper.floor_float((Math.min(level, 150)-75)/7.5f);
		}
		if (level > 150) {
			modifyamount += (Math.min(level, 300)-150)/15;
		}
		IAttributeInstance maxHPattr = theMaid.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		AttributeModifier existedMod = maxHPattr.getModifier(NX_EXP_HP_BOOSTER);
		if (existedMod != null) {
			prevamount = existedMod.getAmount();
		}
		if (modifyamount != 0 || prevamount < modifyamount) {
			// Modifier書き換え
			float prevHP = theMaid.getHealth();
			AttributeModifier maxHPmod = new AttributeModifier(NX_EXP_HP_BOOSTER, uuidString, modifyamount, 0);
			if (existedMod != null) {
				maxHPattr.removeModifier(existedMod);
			}
			maxHPattr.applyModifier(maxHPmod);
			// たぶんremoveModifierした時に20を超える体力が削られちゃうので，再設定．
			theMaid.setHealth(prevHP);
		}
	}

	public boolean onDeath(DamageSource cause) {
		LittleMaidReengaged.Debug("HOOK CATCH");
		if (theMaid.getMaidLevel() >= 20 && !cause.getDamageType().equals("outOfWorld") && !cause.getDamageType().equals("lmmnx_timeover") && !isWaitRevive) {
			// 復帰に必要な砂糖はレベルが低いほど大きく，猶予は少なく
			LittleMaidReengaged.Debug("DISABLING Remote=%s", theMaid.worldObj.isRemote);
			theMaid.playSound("dig.glass");
			deathCount = (int) Math.min(1200, 200 + Math.pow(theMaid.getMaidLevel()-20, 1.8));
			pauseCount = (int) Math.max(100, 600 - (theMaid.getMaidLevel()-20)*6.5);
			requiredSugarToRevive = Math.max(32, 64 - (int)((theMaid.getMaidLevel()-20)/100f*32f));
			deadCause = cause;
			isWaitRevive = true;
			LittleMaidReengaged.Debug("TURN ON COUNT=%d/%d", deathCount, pauseCount);
			return true;
		}
		return false;
	}

	public void onUpdate() {
		if (!theMaid.worldObj.isRemote && isWaitRevive) {
			LittleMaidReengaged.Debug("COUNT %d/%d", deathCount, pauseCount);
			// 死亡判定
			if (--deathCount <= 0 && !theMaid.isDead) {
				isWaitRevive = false;
				LittleMaidReengaged.Debug("TIMEOVER.");
				theMaid.attackEntityFrom(
						new DamageSource("lmmnx_timeover").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(),
						Float.MAX_VALUE);
				theMaid.playSound("mob.ghast.death");
				theMaid.playSound("dig.glass");
				if (theMaid.getMaidMasterEntity() != null) {
					theMaid.getMaidMasterEntity().addChatComponentMessage(new ChatComponentText(
							theMaid.sprintfDeadCause(StatCollector.translateToLocal("littleMaidMob.chat.text.timedeath"), deadCause)));
				}
			}

			// 行動不能
			if ((--pauseCount > 0 || deathCount > 0) && (theMaid.isMaidWait() || theMaid.getMaidModeInt() != EntityMode_DeathWait.mmode_DeathWait)) {
				theMaid.setMaidWait(false);
				theMaid.setMaidMode(EntityMode_DeathWait.mmode_DeathWait);
			}
			if (theMaid.onGround && deathCount%20 == 0 && !theMaid.isSneaking()) {
				theMaid.setSneaking(true);
			}

			// 砂糖を持っているか？
			int sugarCount = 0;
			for (int i=0; i<18 && sugarCount < requiredSugarToRevive; i++) {
				ItemStack stack = theMaid.maidInventory.mainInventory[i];
				if (stack!=null && LMMNX_API_Item.isSugar(stack.getItem())) {
					sugarCount += stack.stackSize;
				}
			}
			// 砂糖が規定数以上ある場合は死亡猶予
			if (sugarCount >= requiredSugarToRevive) {
				deathCount++;
				// 復帰
				if (deathCount > 0 && pauseCount <= 0) {
					theMaid.setHealth(Math.min(8f + Math.min(24f, MathHelper.floor_float((theMaid.getMaidLevel()-20)/100f*24f)), theMaid.getMaxHealth()));
					theMaid.eatSugar(false,false,false);
					for(int i=0; i<18 && requiredSugarToRevive > 0; i++) {
						ItemStack stack = theMaid.maidInventory.mainInventory[i];
						if (stack!=null && LMMNX_API_Item.isSugar(stack.getItem())) {
							int consumesize = Math.min(stack.stackSize, requiredSugarToRevive);
							stack.stackSize -= consumesize;
							if (stack.stackSize <= 0) {
								stack = null;
							}
							requiredSugarToRevive -= consumesize;
						}
					}
					theMaid.setMaidWait(false);
					theMaid.setMaidMode(EntityMode_Basic.mmode_Escorter);
					isWaitRevive = false;
				}
			}
		}
	}

	public boolean onDeathUpdate() {
		if (isWaitRevive && deathCount > 0) {
			return true;
		} else if (!theMaid.worldObj.isRemote && theMaid.getHealth() <= 0f) {
			byte b[] = new byte[] {
					LMMNX_NetSync.LMMNX_Sync,
					0, 0, 0, 0,
					LMMNX_NetSync.LMMNX_Sync_UB_ManualOnDeath, 0
			};
			theMaid.syncNet(b);
		}
		return false;
	}

	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		isWaitRevive = tagCompound.getBoolean("LMMNX_EXP_HANDLER_DEATH_WAIT");
		deathCount = tagCompound.getInteger("LMMNX_EXP_HANDLER_DEATH_DCNT");
		pauseCount = tagCompound.getInteger("LMMNX_EXP_HANDLER_DEATH_PCNT");
		requiredSugarToRevive = tagCompound.getInteger("LMMNX_EXP_HANDLER_DEATH_REQ");
		String causeType = tagCompound.getString("LMMNX_EXP_HANDLER_DEATH_CAUSE_T");
		if (causeType != null && !causeType.isEmpty()) {
			NBTTagCompound causeEntityTag = tagCompound.getCompoundTag("LMMNX_EXP_HADNLER_DEATH_CAUSE_E");
			if (causeEntityTag != null) {
				Entity entity = EntityList.createEntityFromNBT(causeEntityTag, theMaid.worldObj);
				if (entity != null) {
					deadCause = new EntityDamageSource(causeType, entity);
				} else {
					deadCause = new DamageSource(causeType);
				}
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("LMMNX_EXP_HANDLER_DEATH_WAIT", isWaitRevive);
		tagCompound.setInteger("LMMNX_EXP_HANDLER_DEATH_DCNT", deathCount);
		tagCompound.setInteger("LMMNX_EXP_HANDLER_DEATH_PCNT", pauseCount);
		tagCompound.setInteger("LMMNX_EXP_HANDLER_DEATH_REQ", requiredSugarToRevive);
		tagCompound.setString("LMMNX_EXP_HANDLER_DEATH_CAUSE_T", deadCause.getDamageType());
		if (deadCause.getSourceOfDamage() != null) {
			NBTTagCompound causeEntityTag = new NBTTagCompound();
			deadCause.getSourceOfDamage().writeToNBT(causeEntityTag);
			tagCompound.setTag("LMMNX_EXP_HANDLER_DEATH_CAUSE_E", causeEntityTag);
		}
	}

}
