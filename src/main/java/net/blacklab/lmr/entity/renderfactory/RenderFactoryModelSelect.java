package net.blacklab.lmr.entity.renderfactory;

import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.entity.RenderEntitySelect;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFactoryModelSelect implements IRenderFactory<EntityLittleMaidForTexSelect> {

	@Override
	public Render<? super EntityLittleMaidForTexSelect> createRenderFor(RenderManager manager) {
		return new RenderEntitySelect(manager, 0.0F);
	}

}
