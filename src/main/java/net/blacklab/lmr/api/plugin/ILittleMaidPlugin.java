package net.blacklab.lmr.api.plugin;

import net.blacklab.lmr.util.manager.MaidModeManager;

public interface ILittleMaidPlugin {

	/**
	 * メイドさんのモードを追加する
	 */
	default void registerLittleMaidMode(MaidModeManager manager) {
	}
}
