package firis.lmmm.api.model.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelLittleMaidBase;

/**
 * メイドさんのモーション割込み用インターフェース
 * @author firis-games
 *
 */
public interface ILMMotion {
	
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps);
	
}
