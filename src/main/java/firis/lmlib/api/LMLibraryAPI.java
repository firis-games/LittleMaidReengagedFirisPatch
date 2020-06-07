package firis.lmlib.api;

import firis.lmlib.LMLibrary;
import firis.lmlib.api.constant.EnumSound;
import firis.lmlib.api.manager.LMSoundManager;
import firis.lmlib.api.manager.LMTextureBoxManager;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

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
	
	/**
	 * サウンドパックの利用可否を取得する
	 * @return
	 */
	public boolean isSoundPack() {
		return this.soundManager.isFoundSoundpack();
	}
	
	/**
	 * サウンドパックの再生用SoundEventを取得する
	 * @return
	 * 再生できない場合はnullを返却する
	 */
	public SoundEvent getSoundEvent(EnumSound sound, String texture, Integer color, boolean isLivingVoiceRate) {
		
		//Sound情報取得
		String soundName = this.soundManager.getSoundNameWithModel(sound, texture, color);
		if (soundName == null || soundName.isEmpty()) {
			return null;
		}
		
		// LivingVoiceのRateを判定する
		if (isLivingVoiceRate) {
			if ((sound.getId() & 0xff0) == 0x500) {
				//soundからサウンドパック名からRateを取得する
				String soundPackName = soundName.substring(0 , soundName.lastIndexOf("."));
				float ratio = this.soundManager.getLivingVoiceRatio(soundPackName);
				// Rate判断
				if (Math.random() > ratio) {
					return null;
				}
			}	
		}
		return new SoundEvent(new ResourceLocation(LMLibrary.MODID, soundName));
	}
	
}
