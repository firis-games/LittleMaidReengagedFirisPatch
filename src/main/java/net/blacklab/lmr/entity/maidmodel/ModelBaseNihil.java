package net.blacklab.lmr.entity.maidmodel;

import net.blacklab.lmr.util.IModelCapsData;
import net.minecraft.client.model.ModelBase;

public abstract class ModelBaseNihil extends ModelBase {

//	protected RenderModelMulti<? extends EntityLiving> rendererLivingEntity;

//	protected boolean isAlphablend;
//	public boolean isModelAlphablend;
//	public IModelBaseMMM capsLink;
	protected int lighting;
	protected IModelCapsData entityCaps;
	protected boolean isRendering;
	
//	/**
//	 * レンダリングが実行された回数。
//	 * ダメージ時などの対策。
//	 */
//	protected int renderCount;


//	@Override
//	public ModelRenderer getRandomModelBox(Random par1Random) {
//		return modelArmorInner.getRandomModelBox(par1Random);
//	}

	abstract public void showAllParts();
	
//	@Override
//	public void render(Entity par1Entity, float par2, float par3, float par4,
//			float par5, float par6, float par7) {
//		renderCount++;
//	}

}
