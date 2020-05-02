package net.blacklab.lmr.entity.maidmodel;

import net.blacklab.lmr.client.renderer.entity.RenderModelMulti;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class ModelBaseNihil extends ModelBase {

	public RenderModelMulti<? extends EntityLiving> rendererLivingEntity;

	public boolean isAlphablend;
	public boolean isModelAlphablend;
	public IModelBaseMMM capsLink;
	public int lighting;
	public IModelCaps entityCaps;
	public boolean isRendering;
	/**
	 * レンダリングが実行された回数。
	 * ダメージ時などの対策。
	 */
	public int renderCount;


//	@Override
//	public ModelRenderer getRandomModelBox(Random par1Random) {
//		return modelArmorInner.getRandomModelBox(par1Random);
//	}

	public void showAllParts() {
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4,
			float par5, float par6, float par7) {
		renderCount++;
	}

}
