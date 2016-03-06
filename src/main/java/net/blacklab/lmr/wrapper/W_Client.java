package net.blacklab.lmr.wrapper;

import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;

// バージョン差分吸収をおこなう。
public class W_Client
{
	private static final W_IClient instance = getInstance();
	
	private static W_IClient getInstance()
	{
		/*
		final String VER = Loader.instance().getMCVersionString();
		if(VER.indexOf("1.7.2") > 0)
		{
			return new wrapper.mc172.W_CClient();
		}
		else if(VER.indexOf("1.7.10") > 0)
		{
		*/
			return new net.blacklab.lmr.wrapper.mc18.W_CClient();
		//}
		//return null;
	}
	
	public static void renderSkeletonHead(TileEntitySkullRenderer skullRenderer,
			float x, float y, float z,
			int p_147530_4_, float p_147530_5_, int p_147530_6_,
			String p_147530_7_)
	{
		instance.renderSkeletonHead(skullRenderer, x, y, z, p_147530_4_, p_147530_5_, p_147530_6_, p_147530_7_);
	}
}
