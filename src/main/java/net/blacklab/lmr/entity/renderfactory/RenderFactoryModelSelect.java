package net.blacklab.lmr.entity.renderfactory;

import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.client.renderer.entity.LMMNX_RenderEntitySelect;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderFactoryModelSelect implements IRenderFactory<EntityLittleMaidForTexSelect> {

	@SideOnly(Side.CLIENT)
	@Override
	public Render<? super EntityLittleMaidForTexSelect> createRenderFor(RenderManager manager) {
		return new LMMNX_RenderEntitySelect(manager, 0.0F);
	}

}
