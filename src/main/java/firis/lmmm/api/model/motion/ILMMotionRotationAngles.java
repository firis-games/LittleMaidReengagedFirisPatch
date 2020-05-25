package firis.lmmm.api.model.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelLittleMaidBase;

/**
 * RotationAngles時に呼ばれるモーション設定処理
 * @author firis-games
 *
 */
public interface ILMMotionRotationAngles extends ILMMotion {
	public boolean postRotationAngles(ModelLittleMaidBase model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps);
}
