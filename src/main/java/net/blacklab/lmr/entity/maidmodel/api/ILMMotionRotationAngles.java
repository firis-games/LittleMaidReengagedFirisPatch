package net.blacklab.lmr.entity.maidmodel.api;

import net.blacklab.lmr.entity.maidmodel.base.ModelLittleMaidBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;

/**
 * RotationAngles時に呼ばれるモーション設定処理
 * @author firis-games
 *
 */
public interface ILMMotionRotationAngles extends ILMMotion {
	public boolean postRotationAngles(ModelLittleMaidBase model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps);
}
