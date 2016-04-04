package net.blacklab.lmr.entity.renderfactory;

import mmmlibx.lib.MMM_RenderDummy;
import net.blacklab.lmr.entity.EntityMarkerDummy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderFactoryMarkerDummy implements IRenderFactory<EntityMarkerDummy> {

	@SideOnly(Side.CLIENT)
	@Override
	public Render<? super EntityMarkerDummy> createRenderFor(RenderManager manager) {
		return new MMM_RenderDummy();
	}

}
