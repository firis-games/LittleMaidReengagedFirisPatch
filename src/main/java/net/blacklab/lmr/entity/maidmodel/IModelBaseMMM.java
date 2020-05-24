package net.blacklab.lmr.entity.maidmodel;

import firis.lmlibrary.api.caps.IModelCaps;

@Deprecated
public interface IModelBaseMMM extends IModelCaps {

//	public void renderItems(EntityLivingBase pEntity, Render pRender);
	public void showArmorParts(int pParts);
	public void setEntityCaps(IModelCaps pModelCaps);
//	public void setRender(Render pRender);
	public void setArmorRendering(boolean pFlag);

}
