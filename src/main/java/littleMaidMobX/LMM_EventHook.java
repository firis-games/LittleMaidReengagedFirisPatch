package littleMaidMobX;

import net.blacklab.lib.minecraft.item.ItemUtil;
import net.blacklab.lmmnx.api.event.LMMNX_Event;
import net.blacklab.lmmnx.api.item.LMMNX_API_Item;
import net.blacklab.lmmnx.api.mode.LMMNX_API_Farmer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class LMM_EventHook
{
	@SubscribeEvent
	public void onEntityItemPickupEvent(EntityItemPickupEvent event)
	{
		if(event.entityPlayer instanceof LMM_EntityLittleMaidAvatar)
		{
			if(event.item!=null)
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntitySpawn(LivingSpawnEvent event){
		if(event.entityLiving instanceof LMM_EntityLittleMaid){
			LMM_EntityLittleMaid maid = (LMM_EntityLittleMaid) event.entityLiving;
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
			if(event.world.isRemote) maid.setTextureNames();
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
		if (!event.player.worldObj.isRemote && LMM_LittleMaidMobNX.currentVersion.compareVersion(LMM_LittleMaidMobNX.latestVersion) > 0) {
			event.player.addChatComponentMessage(new ChatComponentText(String.format("[LittleMaidMobNX]%s : %s",
					StatCollector.translateToLocal("system.lmmnx.chat.text.newverstion"), LMM_LittleMaidMobNX.latestVersion.shownName)));
			event.player.addChatComponentMessage(new ChatComponentText(String.format("[LittleMaidMobNX]%s",
					StatCollector.translateToLocal("system.lmmnx.chat.text.checkversion"))));
		}
	}

	@SubscribeEvent
	public void onEntitySpawned(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityLivingBase){
			event.setCanceled(deleteDoppelganger(true, event.world, event.entity));
		}

		// TODO issue #9 merge from LittleMaidMobAX(https://github.com/asiekierka/littleMaidMobX/commit/92b2850b1bc4a70b69629cfc84c92748174c8bc6)
		if (event.entity instanceof EntityArrow) {
				EntityArrow arrow = (EntityArrow) event.entity;
				if (arrow.shootingEntity instanceof LMM_IEntityLittleMaidAvatar) {
					LMM_IEntityLittleMaidAvatar avatar = (LMM_IEntityLittleMaidAvatar) arrow.shootingEntity;
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
					LMM_LittleMaidMobNX.Debug("Set "+event.entity.getClass()+" field shootingEntity from avator to maid");
			}
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		Entity entity = event.source.getEntity();
		if (entity instanceof LMM_EntityLittleMaidAvatarMP) {
			((LMM_EntityLittleMaidAvatarMP) entity).avatar.addMaidExperience(0.16f * event.ammount);
		}
	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		Entity entity = event.source.getSourceOfDamage();
		if (entity instanceof EntityArrow && ((EntityArrow) entity).shootingEntity instanceof LMM_EntityLittleMaid) {
			((LMM_EntityLittleMaid)((EntityArrow) entity).shootingEntity).addMaidExperience(0.18f * event.ammount);
		}
	}
	
	@SubscribeEvent
	public void onItemPutChest(LMMNX_Event.LMMNX_ItemPutChestEvent event){
		LMM_EntityLittleMaid maid = event.maid;
//		IInventory target = event.target;
		ItemStack stack = event.stack;
		if(LMMNX_API_Item.isSugar(stack.getItem())|| stack.getItem() == Items.clock){
			event.setCanceled(true);
		}
		if(maid.getMaidModeInt()==LMM_EntityMode_Basic.mmode_FarmPorter){
			if(LMMNX_API_Farmer.isSeed(stack.getItem())||LMMNX_API_Farmer.isHoe(maid, stack)){
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
		EntityPlayer player = event.entityPlayer;
		if (player instanceof LMM_EntityLittleMaidAvatarMP) {
			LMM_EntityLittleMaid maid = ((LMM_EntityLittleMaidAvatarMP) player).avatar;
			maid.addMaidExperience(event.orb.getXpValue()/(maid.getExpBooster()*10f));
			maid.playSound("random.orb");
			event.orb.setDead();
			event.setCanceled(true);
		}
	}

	public static boolean deleteDoppelganger(boolean loading, World worldObj, Entity entity) {
			// ドッペル対策
			if (LMM_LittleMaidMobNX.cfg_antiDoppelganger/* && maidAnniversary > 0L*/) {
				for (int i = 0; i < worldObj.loadedEntityList.size(); i++) {
					Entity entity1 = (Entity)worldObj.loadedEntityList.get(i);
					if (!entity1.isDead && entity1 instanceof EntityLivingBase) {
						EntityLivingBase elm = (EntityLivingBase)entity1;

						if (elm.equals(entity)) continue;

						boolean c1 = elm.getClass().getName().equals(entity.getClass().getName());

						boolean c2 = elm.getUniqueID().equals(entity.getUniqueID());

						if (c1 && c2) {
							LMM_LittleMaidMobNX.Debug("REMOVE DOPPELGANGER UUID %s", entity.getUniqueID());
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
