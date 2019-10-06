package net.blacklab.lmr.util;

import net.minecraft.launchwrapper.Launch;

public enum DevMode {
	;

	/**
	 * 開発環境専用デバッグモード
	 */
	public static final boolean DEVELOPMENT_DEBUG_MODE = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
}
