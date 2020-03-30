package net.firis.lmt.client.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.firis.lmt.common.modelcaps.PlayerModelCaps;
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
	
	/**
	 * 管理用クラス
	 * @param <T>
	 */
	public static class PlayerStat<T> {
		protected Map<UUID, T> statMap = new HashMap<UUID, T>();
		protected T defaultValue;
		public PlayerStat() {
			defaultValue = null;
		}
		public PlayerStat(T def) {
			defaultValue = def;
		}
		
		public T getStat(EntityPlayer player) {
			T stat = defaultValue;
			if (statMap.containsKey(player.getUniqueID())) {
				stat = statMap.get(player.getUniqueID());
			}
			return stat;
		}
		public void setStat(EntityPlayer player, T value) {
			statMap.put(player.getUniqueID(), value);
		}
		
	}
	
	/**
	 * アクションキーの保存
	 */
	public static PlayerStat<Boolean> lmAvatarAction = new PlayerStat<Boolean>(false);

	/**
	 * アクションキーの保存
	 */
	public static PlayerStat<Boolean> lmAvatarWaitAction = new PlayerStat<Boolean>(false);
	public static PlayerStat<Integer> lmAvatarWaitCounter = new PlayerStat<Integer>(0);

	
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
			lmAvatarWaitAction.setStat(player, false);
			lmAvatarWaitCounter.setStat(player, player.ticksExisted);
		}
		if (isMotionSittingReset) {
			//お座りモーションリセット
			lmAvatarAction.setStat(player, false);
		}
		
		if (!isMotionWaitReset && !isMotionSittingReset) {
			
			//モーション継続状態と判断
			Integer counter = lmAvatarWaitCounter.getStat(player);
			
			//初期化
			if (counter == 0) lmAvatarWaitCounter.setStat(player, player.ticksExisted);
			
			//100tickで待機状態On
			if ((player.ticksExisted - counter) >= 100) {
				lmAvatarWaitAction.setStat(player, true);
			}
		}
	}
	
}
