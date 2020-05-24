package firis.lmlibrary.api.model.motion;

import firis.lmlibrary.api.caps.IModelCaps;
import firis.lmlibrary.api.model.ModelLittleMaidBase;

/**
 * RotationAngles時に呼ばれるモーション設定処理
 * @author firis-games
 *
 */
public interface ILMMotionRotationAngles extends ILMMotion {
	public boolean postRotationAngles(ModelLittleMaidBase model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps);
}
