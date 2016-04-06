package net.blacklab.lmr.util.helper;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.util.EnumFacing;

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

		public static void renderSkeletonHead(TileEntitySkullRenderer skullRenderer,
				float x, float y, float z,
				int p_147530_4_, float p_147530_5_, int p_147530_6_,
				String p_147530_7_) {
			skullRenderer.renderSkull(x, y, z, EnumFacing.getFront(p_147530_4_), p_147530_5_, p_147530_6_, null, 0, 0);
		}

}
