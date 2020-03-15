package net.blacklab.lmc.common.event;

import net.blacklab.lmc.common.entity.LMEntityItemAntiDamage;
import net.blacklab.lmc.common.helper.LittleMaidHelper;
import net.blacklab.lmc.common.item.LMItemMaidSugar;
import net.blacklab.lmr.LittleMaidReengaged.LMItems;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.network.LMRNetwork;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class LivingDeathEventHandler {

	/**
	 * イベントの優先度は高め
	 * @param event
	 */
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		
		if (!LMRConfig.cfg_isResurrection) return;
		
		if (!(event.getEntityLiving() instanceof EntityLiving)) return;
		
		EntityLiving entityLiving = (EntityLiving) event.getEntityLiving();
		
		if (entityLiving.getEntityWorld().isRemote) return;
		
		//アニマルメイドさんの場合はこちらから生成する
		EntityLiving maidItemEntityLiving = entityLiving;
		NBTTagCompound livingCustomTag = entityLiving.getEntityData();
		if (!(maidItemEntityLiving instanceof EntityLittleMaid)
				&& livingCustomTag.hasKey(LMItemMaidSugar.ANIMAL_MAID_KEY)) {
			NBTTagCompound animalMaidTag = (NBTTagCompound) livingCustomTag.getTag(LMItemMaidSugar.ANIMAL_MAID_KEY);
			maidItemEntityLiving = (EntityLiving) EntityList.createEntityFromNBT(animalMaidTag, entityLiving.getEntityWorld());
			
			//回復させる
			entityLiving.setHealth(1.0F);
			//炎上を消去
			entityLiving.extinguish();
			//ポーション効果をすべて無効化
			for (PotionEffect effect : entityLiving.getActivePotionEffects()) {
				entityLiving.removePotionEffect(effect.getPotion());
			}
			//動物情報を書き込む
			livingCustomTag = entityLiving.getEntityData();
			livingCustomTag.removeTag(LMItemMaidSugar.ANIMAL_MAID_KEY);
			NBTTagCompound animalTag = LittleMaidHelper.getNBTTagFromEntityLiving(entityLiving);
			maidItemEntityLiving.getEntityData().setTag(LMItemMaidSugar.ANIMAL_MAID_KEY, animalTag);
		}
		
		//メイドさんのみ処理を行う
		if (maidItemEntityLiving instanceof EntityLittleMaid) {
			
			EntityLittleMaid entityMaid = (EntityLittleMaid) maidItemEntityLiving;
			
			//野良メイドはお土産を残さない
			if (entityMaid.getOwnerId() == null) return;
			
			//回復させる
			entityMaid.setHealth(1.0F);
			
			//炎上を消去
			entityMaid.extinguish();
			
			//ポーション効果をすべて無効化
			entityMaid.clearActivePotions();

			//メイドの土産
			ItemStack maidSouvenir = LittleMaidHelper.getItemStackFromEntity(entityMaid, new ItemStack(LMItems.MAID_SOUVENIR));
			
			World world = entityLiving.getEntityWorld();
			
			//Drop用EntityItem
			EntityItem entityMaidSouvenir = new LMEntityItemAntiDamage(world, 
					entityLiving.posX, entityLiving.posY, entityLiving.posZ,
					maidSouvenir);
			entityMaidSouvenir.setDefaultPickupDelay();
			world.spawnEntity(entityMaidSouvenir);
			
			
			//ClientSideの処理が動いてないみたいだからパケット飛ばしてパーティクルを発生させる
			LMRNetwork.PacketSpawnParticleS2C(entityLiving.getPosition(), 0);
			
			//メイドさん消滅処理
			entityLiving.setDead();
			event.setCanceled(true);
			return;
			
		}
	}
	
}
