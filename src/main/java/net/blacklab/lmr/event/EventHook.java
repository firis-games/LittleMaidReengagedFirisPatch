package net.blacklab.lmr.event;

import net.blacklab.lib.minecraft.item.ItemUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.api.event.LMREvent;
import net.blacklab.lmr.api.mode.UtilModeFarmer;
import net.blacklab.lmr.client.entity.EntityLittleMaidAvatarSP;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.EntityLittleMaidAvatarMP;
import net.blacklab.lmr.entity.IEntityLittleMaidAvatar;
import net.blacklab.lmr.entity.mode.EntityMode_Basic;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHook
{
	@SubscribeEvent
	public void onEntityItemPickupEvent(EntityItemPickupEvent event)
	{
		if(event.getEntityPlayer() instanceof EntityLittleMaidAvatarSP)
		{
			if(event.getItem()!=null)
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntitySpawn(LivingSpawnEvent event){
		if(event.getEntityLiving() instanceof EntityLittleMaid){
			EntityLittleMaid maid = (EntityLittleMaid) event.getEntityLiving();
			if(maid.isContract()||maid.isWildSaved) return;
			maid.onSpawnWithEgg();
//			int c = maid.getTextureBox()[0].getWildColorBits();
//			if(c<=0) maid.setColor(12); else for(int i=15;i>=0;i--){
//				int x = (int) Math.pow(2, i);
//				if((c&x)==x) maid.setColor(i);
//			}
			maid.isWildSaved = true;
//			event.setResult(Result.ALLOW);
//			NBTTagCompound t = new NBTTagCompound();
//			maid.writeEntityToNBT(t);
//			maid.readEntityFromNBT(t);
			if(event.getWorld().isRemote) maid.setTextureNames();
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
		if (!event.player.worldObj.isRemote && LittleMaidReengaged.currentVersion.compareVersion(LittleMaidReengaged.latestVersion) > 0) {
			event.player.addChatComponentMessage(new TextComponentString(String.format("[LittleMaidReengaged]%s : %s",
					I18n.translateToLocal("system.lmmnx.chat.text.newverstion"), LittleMaidReengaged.latestVersion.shownName)));
			event.player.addChatComponentMessage(new TextComponentString(String.format("[LittleMaidReengaged]%s",
					I18n.translateToLocal("system.lmmnx.chat.text.checkversion"))));
		}
	}

	@SubscribeEvent
	public void onEntitySpawned(EntityJoinWorldEvent event){
		if(event.getEntity() instanceof EntityLivingBase){
			event.setCanceled(deleteDoppelganger(true, event.getWorld(), event.getEntity()));
		}

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
		Entity entity = event.getSource().getEntity();
		if (entity instanceof EntityLittleMaidAvatarMP) {
			((EntityLittleMaidAvatarMP) entity).avatar.addMaidExperience(0.16f * event.getAmount());
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Entity entity = event.getSource().getSourceOfDamage();
		if (entity instanceof EntityArrow && ((EntityArrow) entity).shootingEntity instanceof EntityLittleMaid) {
			((EntityLittleMaid)((EntityArrow) entity).shootingEntity).addMaidExperience(0.18f * event.getAmount());
		}
	}

	@SubscribeEvent
	public void onItemPutChest(LMREvent.ItemPutChestEvent event){
		EntityLittleMaid maid = event.maid;
//		IInventory target = event.target;
		ItemStack stack = event.stack;
		if(ItemHelper.isSugar(stack.getItem())|| stack.getItem() == Items.clock){
			event.setCanceled(true);
		}
		if(maid.getMaidModeInt()==EntityMode_Basic.mmode_FarmPorter){
			if(UtilModeFarmer.isSeed(stack.getItem())||UtilModeFarmer.isHoe(maid, stack)){
				event.setCanceled(true);
			}
			if(event.maidStackIndex>13){
				event.setCanceled(false);
			}
		}
		if(event.maidStackIndex==17&&ItemUtil.isHelm(stack)){
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPickUpXP(PlayerPickupXpEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player instanceof EntityLittleMaidAvatarMP) {
			EntityLittleMaid maid = ((EntityLittleMaidAvatarMP) player).avatar;
			maid.addMaidExperience(event.getOrb().getXpValue()/(maid.getExpBooster()*10f));
			maid.playSound("random.orb");
			event.getOrb().setDead();
			event.setCanceled(true);
		}
	}

	public static boolean deleteDoppelganger(boolean loading, World worldObj, Entity entity) {
			// ドッペル対策
			if (LittleMaidReengaged.cfg_antiDoppelganger/* && maidAnniversary > 0L*/) {
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
			}
			return false;
		}
}
