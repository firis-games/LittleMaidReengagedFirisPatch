package firis.lmlib.client.renderer.layer;

import firis.lmlib.api.caps.IModelCompound;
import firis.lmlib.api.client.renderer.LMRenderMultiModel;
import firis.lmlib.api.client.renderer.layer.LMLayerArmorBase;
import firis.lmlib.api.entity.ILMModelEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * GUIの防具Layer
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class LayerArmorLittleMaidGui extends LMLayerArmorBase {

	public LayerArmorLittleMaidGui(LMRenderMultiModel<? extends EntityLiving> rendererIn) {
		
		super(rendererIn);
		
	}

	@Override
	protected IModelCompound getArmorModelConfigCompound(EntityLivingBase entityLivingBaseIn, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale,
			EntityEquipmentSlot slot) {
		ILMModelEntity modelEntity = (ILMModelEntity) entityLivingBaseIn;
		return modelEntity.getModelCompoundEntity();
	}
	
}
