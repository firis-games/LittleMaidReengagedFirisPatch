package firis.lmavatar.common.proxy;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Proxy
 * @author firis-games
 *
 */
public interface IProxy {

	default public EntityPlayer getClientPlayer() {
		return null;
	}
}
