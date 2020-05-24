package net.blacklab.lmr.entity.maidmodel.api;

import java.util.ArrayList;
import java.util.List;

import firis.lmlibrary.api.caps.IModelCaps;
import firis.lmlibrary.api.model.ModelLittleMaidBase;
import firis.lmlibrary.api.model.motion.ILMMotionRotationAngles;
import net.blacklab.lmr.entity.maidmodel.base.motion.LMMotionItemCarryer;

/**
 * メイドさんのモーションを管理する
 * @author firis-games
 *
 */
public class LMMotionHandler {

	public static List<ILMMotionRotationAngles> motionRatationAnglesList = new ArrayList<>();
	
	
	/**
	 * 現時点では内蔵のモーションは直接登録する
	 * いずれはAPI化する
	 */
	public static void init() {
		
		//アイテム運び用
		motionRatationAnglesList.add(new LMMotionItemCarryer());
		
	}
	
	/**
	 * モーション設定実行
	 */
	public static void postMotionRotationAngles(ModelLittleMaidBase model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps) {
		//RotationAnglesの最後に描画処理の追加処理を行う
		for (ILMMotionRotationAngles invoke : motionRatationAnglesList) {
			invoke.postRotationAngles(model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, pEntityCaps);
		}
	}
	
}
