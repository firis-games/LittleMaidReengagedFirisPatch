package firis.lmavatar.config;

import firis.lmavatar.LittleMaidAvatar;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigChangedEventHandler {

	/**
	 * Config変更後の処理
	 */
	@SubscribeEvent
	public static void onPostConfigChangedEvent(ConfigChangedEvent.PostConfigChangedEvent event) {
		if (LittleMaidAvatar.MODID.equals(event.getModID())) {
			//設定値の更新
			FirisConfig.syncConfig();
		}
	}
}
