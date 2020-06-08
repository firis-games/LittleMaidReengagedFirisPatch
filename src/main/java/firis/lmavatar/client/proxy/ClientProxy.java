package firis.lmavatar.client.proxy;

import firis.lmavatar.common.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * クライアントサイドProxy
 * @author firis-games
 *
 */
public class ClientProxy implements IProxy {

	/**
	 * クライアントプレイヤーを取得する
	 */
	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
