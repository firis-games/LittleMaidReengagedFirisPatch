package wrapper;

import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;

public interface W_IClient
{
	public void renderSkeletonHead(TileEntitySkullRenderer skullRenderer,
			float x, float y, float z,
			int p_147530_4_, float p_147530_5_, int p_147530_6_,
			String p_147530_7_);
}
