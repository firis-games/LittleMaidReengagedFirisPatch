package net.blacklab.lmmnx.util;

public enum LMMNX_DevMode {
	/**開発環境以外でのプレイ**/NOT_IN_DEV,
	/**IDEを使用しない開発環境**/DEVMODE_NO_IDE,
	/**eclipseによる開発環境**/DEVMODE_ECLIPSE;

	/**
	 * 開発モードの指定です。
	 * 開発環境でEntityModeやModelの開発を行う際はNOT_IN_DEVMODE「以外」にしてください。
	 * 開発環境外であるか、eclipseを使用して開発環境を構築しているかの設定です。LMMNX_DevModeの値を指定してください。
	 */
	public static final LMMNX_DevMode DEVMODE = NOT_IN_DEV;

	public static final String[] INCLUDEPROJECT = new String[]{};

	/**
	 * 開発環境専用デバッグモード
	 */
	public static final boolean DEVELOPMENT_DEBUG_MODE = false;
}
