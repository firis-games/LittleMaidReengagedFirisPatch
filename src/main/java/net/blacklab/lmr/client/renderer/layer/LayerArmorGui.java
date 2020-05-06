package net.blacklab.lmr.client.renderer.layer;

import net.blacklab.lmr.client.renderer.entity.RenderModelMulti;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * GUIの防具Layer
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class LayerArmorGui extends LayerArmorLittleMaidBase {

	public LayerArmorGui(RenderModelMulti<? extends EntityLiving> rendererIn) {
		
		super(rendererIn);
		
	}
	
}
