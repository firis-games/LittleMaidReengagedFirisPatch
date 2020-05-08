package net.blacklab.lmr.event;

import net.blacklab.lib.vevent.SubscribeVEvent;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.api.event.EventLMRE;
import net.blacklab.lmr.client.entity.EntityLittleMaidAvatarSP;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaidAvatarMP;
import net.blacklab.lmr.entity.littlemaid.IEntityLittleMaidAvatar;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHookLMRE
{
	@SubscribeEvent
	public void onEntityItemPickupEvent(EntityItemPickupEvent event)
	{
		if(event.getEntityPlayer() instanceof EntityLittleMaidAvatarSP)
		{
			if(event.getItem() != null)
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if(event.getEntity() instanceof EntityLivingBase){
			event.setCanceled(deleteDoppelganger(true, event.getWorld(), event.getEntity()));
		}

//		if(event.getEntity() instanceof EntityLittleMaid){
//			EntityLittleMaid maid = (EntityLittleMaid) event.getEntity();
//			if(maid.isContract()||maid.isWildSaved) return;
//			maid.onSpawnWithEgg();
////			int c = maid.getTextureBox()[0].getWildColorBits();
////			if(c<=0) maid.setColor(12); else for(int i=15;i>=0;i--){
////				int x = (int) Math.pow(2, i);
////				if((c&x)==x) maid.setColor(i);
////			}
//			maid.isWildSaved = true;
////			event.setResult(Result.ALLOW);
////			NBTTagCompound t = new NBTTagCompound();
////			maid.writeEntityToNBT(t);
////			maid.readEntityFromNBT(t);
//			if(event.getWorld().isRemote) maid.setTextureNames();
//		}

		// TODO issue #9 merge from LittleMaidMobAX(https://github.com/asiekierka/littleMaidMobX/commit/92b2850b1bc4a70b69629cfc84c92748174c8bc6)
		if (event.getEntity() instanceof EntityArrow) {
				EntityArrow arrow = (EntityArrow) event.getEntity();
				if (arrow.shootingEntity instanceof IEntityLittleMaidAvatar) {
					IEntityLittleMaidAvatar avatar = (IEntityLittleMaidAvatar) arrow.shootingEntity;
					/* if (arrow.isDead) {
						for (Object obj : arrow.worldObj.loadedEntityList) {
							if (obj instanceof EntityCreature && !(obj instanceof LMM_EntityLittleMaid)) {
								EntityCreature ecr = (EntityCreature)obj;
								if (ecr.getEntityToAttack() == avatar) {
									ecr.setTarget(avatar.getMaid());
								}
							}
						}
					} */
					arrow.shootingEntity = avatar.getMaid();
					LittleMaidReengaged.Debug("Set "+event.getEntity().getClass()+" field shootingEntity from avator to maid");
			}
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		Entity entity = event.getSource().getTrueSource();
		if (entity instanceof EntityLittleMaidAvatarMP) {
			((EntityLittleMaidAvatarMP) entity).avatar.addMaidExperience(0.16f * event.getAmount());
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Entity entity = event.getSource().getTrueSource();
		if (entity instanceof EntityArrow && ((EntityArrow) entity).shootingEntity instanceof EntityLittleMaid) {
			((EntityLittleMaid)((EntityArrow) entity).shootingEntity).addMaidExperience(0.18f * event.getAmount());
		}
	}

	@SubscribeEvent
	public void onPickUpXP(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player instanceof EntityLittleMaidAvatarMP) {
			EntityLittleMaid maid = ((EntityLittleMaidAvatarMP) player).avatar;
			maid.addMaidExperience(event.getOrb().getXpValue() / (maid.getExperienceHandler().getExpBooster() * 10f));
			maid.playSound("random.orb");
			event.getOrb().setDead();
			event.setCanceled(true);
		}
	}

	public static boolean deleteDoppelganger(boolean loading, World worldObj, Entity entity) {
		// ドッペル対策
		for (int i = 0; i < worldObj.loadedEntityList.size(); i++) {
			Entity entity1 = (Entity)worldObj.loadedEntityList.get(i);
				if (!entity1.isDead && entity1 instanceof EntityLivingBase) {
				EntityLivingBase elm = (EntityLivingBase)entity1;
				if (elm.equals(entity)) continue;

				boolean c1 = elm.getClass().getName().equals(entity.getClass().getName());
				boolean c2 = elm.getUniqueID().equals(entity.getUniqueID());

				if (c1 && c2) {
					LittleMaidReengaged.Debug("REMOVE DOPPELGANGER UUID %s", entity.getUniqueID());

					if (entity.getEntityId() > elm.getEntityId()) {
						elm.setDead();
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	@SubscribeVEvent
	public void onItemPutChest(EventLMRE.ItemPutChestEvent event){
		LittleMaidReengaged.Debug("HOOK");
		
		EntityLittleMaid maid = event.maid;
		ItemStack stack = event.stack;
//		String maidModeString = maid.getMaidModeString();

		//砂糖扱いのアイテムと時計を除外する
		if(ItemHelper.isSugar(stack) || stack.getItem() == Items.CLOCK){
			event.setCanceled(true);
		}
		
		//各職業のアイテム除外判定
		boolean isCancelPut = maid.jobController.isCancelPutChestItemStack(stack, event.maidStackIndex);
//		Iterator<EntityModeBase> maidModeIterator = maid.maidEntityModeList.iterator();
//		while (maidModeIterator.hasNext()) {
//			EntityModeBase maidMode = maidModeIterator.next();
//			isCancelPut = maidMode.isCancelPutChestItemStack(maidModeString, stack, event.maidStackIndex);
//			if (isCancelPut) {
//				break;
//			}
//		}
		if (isCancelPut) {
			event.setCanceled(true);
		}
	}
	
	/**
	 * 攻撃の仕返し先をプレイヤーアバターからメイドさんへ変更
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
		
		EntityLivingBase target = event.getTarget();
		
		if (target instanceof EntityLittleMaidAvatarMP) {
			EntityLittleMaidAvatarMP avatarTarget = (EntityLittleMaidAvatarMP) target;
			event.getEntityLiving().setRevengeTarget(avatarTarget.getMaid());
		}
		
	}
}
