package net.blacklab.lmr.entity.maidmodel.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;

/**
 * 追加モーション
 * @author firis-games
 *
 */
public class LMMotionItemCarryer implements ILMMotion {

	/**
	 * メイドさんがチェストを持っている動き
	 */
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps) {
		
		String job = ModelCapsHelper.getCapsValueString(pEntityCaps, IModelCaps.caps_job);
		boolean idDisplay = false;
    	if (EntityMode_Basic.mmode_FarmPorter.toLowerCase().equals(job) 
    			|| EntityMode_Basic.mmode_LumberjackPorter.toLowerCase().equals(job)
    			|| EntityMode_Basic.mmode_RipperPorter.toLowerCase().equals(job)
    			|| EntityMode_Basic.mmode_SugarCanePorter.toLowerCase().equals(job)
    			|| EntityMode_Basic.mmode_AnglerPorter.toLowerCase().equals(job)) {
    		idDisplay = true;
    	}
    	
    	if (!idDisplay) return false;
		
		//アイテム運びモード
		model.bipedRightArm.setRotateAngle(-1.0F, -0.2F, 0.0F);
		model.bipedLeftArm.setRotateAngle(-1.0F, 0.2F, 0.0F);
		
		return true;
	}

}
