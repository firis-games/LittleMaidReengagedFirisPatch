package firis.lmlib.api;

import firis.lmlib.api.manager.LMSoundManager;
import firis.lmlib.api.manager.LMTextureBoxManager;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * LMLibraryのAPI
 * @author firis-games
 *
 */
public class LMLibraryAPI {
	
	/**
	 * APIインスタンス
	 */
	private static final LMLibraryAPI INSTANCE = new LMLibraryAPI();
	public static LMLibraryAPI instance() {
		return INSTANCE;
	}
	
	/**
	 * テクスチャマネージャー
	 */
	private LMTextureBoxManager textureManager = new LMTextureBoxManager();
	
	/**
	 * サウンドマネージャー
	 */
	private LMSoundManager soundManager = new LMSoundManager();
	
	/**
	 * テクスチャマネージャを取得する
	 * @return
	 */
	public LMTextureBoxManager getTextureManager() {
		return this.textureManager;
	}
	
	/**
	 * サウンドマネージャを取得する
	 * @return
	 */
	public LMSoundManager getSoundManager() {
		return this.soundManager;
	}
	
	/**
	 * リトルメイド用追加モーションを登録する
	 */
	public void registerLittleMaidMotion(ILMMotion lmMotion) {
		ModelLittleMaidBase.littleMaidMotions.add(lmMotion);
	}

}
