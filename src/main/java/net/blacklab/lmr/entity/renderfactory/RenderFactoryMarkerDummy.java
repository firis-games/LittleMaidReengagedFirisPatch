package net.blacklab.lmr.entity.renderfactory;

import net.blacklab.lmr.client.renderer.entity.RenderEntityMarkerDummy;
import net.blacklab.lmr.entity.EntityMarkerDummy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFactoryMarkerDummy implements IRenderFactory<EntityMarkerDummy> {

	@Override
	public Render<? super EntityMarkerDummy> createRenderFor(RenderManager manager) {
		return new RenderEntityMarkerDummy();
	}

}
