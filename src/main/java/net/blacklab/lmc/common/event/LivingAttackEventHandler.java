package net.blacklab.lmc.common.event;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaidAvatarMP;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=LittleMaidReengaged.MODID)
public class LivingAttackEventHandler {
	
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event) {
		
		//フレンドリファイアの制御
		if (LMRConfig.cfg_general_friendly_fire) return;
		
		//ダメージを受けるのがPlayerの場合
		if (event.getEntityLiving() instanceof EntityPlayer) {
			//ペットからの攻撃
			if (event.getSource().getTrueSource() instanceof IEntityOwnable
					&& ((IEntityOwnable)event.getSource().getTrueSource()).getOwner() != null) {
				event.setCanceled(true);
			} else if (event.getSource().getTrueSource() instanceof EntityLittleMaidAvatarMP) {
				event.setCanceled(true);
			}
			
		//ダメージを受けるのがテイム系モブの場合	
		} else if (event.getEntityLiving() instanceof IEntityOwnable
				&& ((IEntityOwnable) event.getEntityLiving()).getOwner() != null) {
			//他のペットからの攻撃
			if (event.getSource().getTrueSource() instanceof IEntityOwnable
					&& ((IEntityOwnable)event.getSource().getTrueSource()).getOwner() != null) {
				event.setCanceled(true);
			//プレイヤーからの攻撃
			} else if (event.getSource().getTrueSource() instanceof EntityPlayer) {
				event.setCanceled(true);
			}
		}
		
	}
}
