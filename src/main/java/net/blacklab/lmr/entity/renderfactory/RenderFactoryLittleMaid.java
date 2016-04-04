package net.blacklab.lmr.entity.renderfactory;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderFactoryLittleMaid implements IRenderFactory<EntityLittleMaid> {

	@SideOnly(Side.CLIENT)
	@Override
	public Render<? super EntityLittleMaid> createRenderFor(RenderManager manager) {
		return new RenderLittleMaid(manager, 0.3F);
	}

}
