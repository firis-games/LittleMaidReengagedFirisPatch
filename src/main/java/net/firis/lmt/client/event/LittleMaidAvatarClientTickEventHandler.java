package net.firis.lmt.client.event;

import net.firis.lmt.common.manager.PlayerModelManager;
import net.firis.lmt.common.manager.SyncPlayerModelClient;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.firis.lmt.common.modelcaps.PlayerModelConfigCompound;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LittleMaidAvatarClientTickEventHandler {
	
	@SubscribeEvent
	public static void onClientTickEvent(ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.END
				&& Minecraft.getMinecraft().player != null){
			onClientTickEventLittleMaidAvatar(event);
		}
	}
	
	/**
	 * アバターアクションの管理
	 * @param event
	 */
	protected static void onClientTickEventLittleMaidAvatar(ClientTickEvent event) {
		
		boolean isMotionSittingReset = false;
		boolean isMotionWaitReset = false;
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		PlayerModelConfigCompound lmAvatar = PlayerModelManager.getModelConfigCompound(player);
		
		//アクション解除
		//腕振り
		if (!isMotionWaitReset && player.swingProgress != 0.0F) {
			isMotionWaitReset = true;
		}
		
		//右クリック
		if (!isMotionWaitReset) {
	        if (EnumAction.NONE != PlayerModelCaps.getPlayerAction(player, EnumHandSide.RIGHT)
	        		|| EnumAction.NONE != PlayerModelCaps.getPlayerAction(player, EnumHandSide.LEFT)) {
	        	isMotionWaitReset = true;	        	
	        }
		}
		
		//アクション解除
		//縦方向は重力が発生してるので微調整して判断
		if (!isMotionWaitReset || !isMotionSittingReset) {
			if (player.motionX != 0.0D || player.motionZ != 0.0D
					|| player.motionY > 0.0D) {
				isMotionWaitReset = true;
				isMotionSittingReset = true;
			}
		}
		
		if (isMotionWaitReset) {
			//待機モーションリセット
			lmAvatar.resetLMAvatarWaitAction();
		}
		if (isMotionSittingReset) {
			//お座りモーションリセット
			lmAvatar.resetLMAvatarAction();
			//同期処理
			SyncPlayerModelClient.syncModel();
		}
		
		if (!isMotionWaitReset && !isMotionSittingReset) {
			//モーション継続と設定の判断
			lmAvatar.setLMAvatarWaitAction(true);
		}
	}
	
}
