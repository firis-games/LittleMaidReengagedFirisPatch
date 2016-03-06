package mmmlibx.lib.multiModel.model.mc162;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

public interface IModelBaseMMM extends IModelCaps {

	public void renderItems(EntityLivingBase pEntity, Render pRender);
	public void showArmorParts(int pParts);
	public void setEntityCaps(IModelCaps pModelCaps);
	public void setRender(Render pRender);
	public void setArmorRendering(boolean pFlag);

}
