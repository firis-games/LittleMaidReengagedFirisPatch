package net.firis.lmt.client.event;

import net.firis.lmt.client.renderer.RendererMaidPlayerMultiModel;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventLMAvatar extends Event {

	/**
	 * プレイヤーアバターのLayerを追加する
	 * 
	 * FMLPreInitializationEventのタイミングで
	 * MinecraftForge.EVENT_BUS.registerで登録します
	 * MinecraftForge.EVENT_BUS.register(new RendererAvatarAddLayerEvent());
	 * 
	 * ClientEventLMAvatarはClientサイド限定なのでproxyなどで分離して登録処理を呼び出す
	 * 
	 */
	public static class RendererAvatarAddLayerEvent extends ClientEventLMAvatar {
		
		private final RendererMaidPlayerMultiModel renderer;
		
		public RendererAvatarAddLayerEvent(RendererMaidPlayerMultiModel renderer) {
			this.renderer = renderer;
		}
		
		public RendererMaidPlayerMultiModel getRenderer() {
			return this.renderer;
		}
	}
	
}
