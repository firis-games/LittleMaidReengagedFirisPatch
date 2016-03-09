package net.blacklab.lmr.util.helper;

import net.minecraft.client.renderer.OpenGlHelper;

public class RendererHelper {

	/*
		public static World getMCtheWorld() {
			if (Helper.mc !=  null) {
				return Helper.mc.theWorld;
			}
			return null;
		}
	*/
		public static void setLightmapTextureCoords(int pValue) {
	//		int ls = pValue % 65536;
	//		int lt = pValue / 65536;
			int ls = pValue & 0xffff;
			int lt = pValue >>> 16;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ls, lt);
		}

}
