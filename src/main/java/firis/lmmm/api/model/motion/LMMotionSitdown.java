package firis.lmmm.api.model.motion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelLittleMaidBase;

/**
 * メイドさんのお座りモーション制御用
 * @author firis-games
 *
 */
public class LMMotionSitdown implements ILMMotion {
	
	public static final String SITDOWN = "lm:sitdown";
	
	/**
	 * お座りモーションのお座り位置調整用
	 */
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps entityCaps) {
		//カスタム設定
		//お座りモーションの場合はモデル側で位置を調整する
		if (model.isRiding && SITDOWN.equals(motion)) {
			model.mainFrame.rotationPointY += 5.00F;
		}
		return false;
	}

}
