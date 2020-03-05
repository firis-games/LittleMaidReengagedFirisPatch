package net.blacklab.lmr.api.client.event;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventLMRE extends Event {

	/**
	 * メイドさんのLayerを追加する
	 * 
	 * FMLPreInitializationEventのタイミングで
	 * MinecraftForge.EVENT_BUS.registerで登録します
	 * MinecraftForge.EVENT_BUS.register(new RendererLittleMaidAddLayerEventHandler());
	 * 
	 * ClientEventLMREはClientサイド限定なのでproxyなどで分離して登録処理を呼び出す
	 * 
	 */
	public static class RendererLittleMaidAddLayerEvent extends ClientEventLMRE {
		
		private final RenderLittleMaid renderer;
		
		public RendererLittleMaidAddLayerEvent(RenderLittleMaid renderer) {
			this.renderer = renderer;
		}
		
		public RenderLittleMaid getRenderer() {
			return this.renderer;
		}
	}
	
}
