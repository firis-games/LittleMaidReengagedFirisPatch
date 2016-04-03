package net.blacklab.lmr.entity.renderfactory;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFactoryLittleMaid implements IRenderFactory<EntityLittleMaid> {

	@Override
	public Render<? super EntityLittleMaid> createRenderFor(RenderManager manager) {
		return new RenderLittleMaid(manager, 0.3F);
	}

}
