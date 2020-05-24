package net.blacklab.lmr.entity.maidmodel.base.motion;

import firis.lmlibrary.api.caps.IModelCaps;
import firis.lmlibrary.api.model.ModelLittleMaidBase;
import firis.lmlibrary.api.model.motion.ILMMotionRotationAngles;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.minecraft.entity.Entity;

/**
 * 追加モーション
 * @author firis-games
 *
 */
public class LMMotionItemCarryer implements ILMMotionRotationAngles {

	/**
	 * メイドさんがチェストを持っている動き
	 */
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps) {
		
		Entity entity = (Entity) pEntityCaps.getCapsValue(IModelCaps.caps_Entity);
		
		if (!(entity instanceof EntityLittleMaid)) return false;
		
		EntityLittleMaid maid = (EntityLittleMaid) entity;
		
		String job = maid.jobController.getMaidModeString();
		boolean idDisplay = false;
    	if (EntityMode_Basic.mmode_FarmPorter.equals(job) 
    			|| EntityMode_Basic.mmode_LumberjackPorter.equals(job)
    			|| EntityMode_Basic.mmode_RipperPorter.equals(job)
    			|| EntityMode_Basic.mmode_SugarCanePorter.equals(job)
    			|| EntityMode_Basic.mmode_AnglerPorter.equals(job)) {
    		idDisplay = true;
    	}
    	
    	if (!idDisplay) return false;
		
		//アイテム運びモード
		model.bipedRightArm.setRotateAngle(-1.0F, -0.2F, 0.0F);
		model.bipedLeftArm.setRotateAngle(-1.0F, 0.2F, 0.0F);
		
		return true;
	}

}
