package net.blacklab.lmr.entity.experience;

import net.minecraft.util.math.MathHelper;

public class ExperienceUtil {

	private static final double K = Math.log(5000) / 348;
	private static final double EXP_K = Math.exp(K);
	private static final double A = 10 / Math.exp(2*K);

	public static final int EXP_FUNCTION_MAX = 300;

	public static float getRequiredExpToLevel(int level) {
		return (float) (A / K * (Math.exp(K * level) - EXP_K));
	}

	public static int getLevelFromExp(float exp) {
		if (exp<=0) {
			return 1;
		}
		if (exp > getRequiredExpToLevel(EXP_FUNCTION_MAX)) {
			return EXP_FUNCTION_MAX;
		}
		return MathHelper.floor_double(Math.log(K*exp/A + EXP_K)/K);
	}

	public static int getBoosterLimit(int level) {
		// レベルによるブースト制限
		if (level <=  10) return   1;
		if (level <=  20) return   5;
		if (level <=  50) return  20;
		if (level <= 100) return  50;
		return 64 * 18;
	}
}
