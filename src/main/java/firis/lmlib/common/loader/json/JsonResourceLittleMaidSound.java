package firis.lmlib.common.loader.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loaderで読み込んだサウンドパック情報
 * 
 * Json形式で入出力できることを想定する
 * 
 * @author firis-games
 *
 */
public class JsonResourceLittleMaidSound {
	
	//デフォルトサウンドパック
	public String default_soundpack = "";
	
	//サウンドパック名をキーに詳細情報を持つ
	public Map<String, ResourceLittleMaidSoundpack> soundpacks = new HashMap<>();
	
	/**
	 * サウンドパック情報を追加する
	 */
	public void addSoundpack(String soundName, Float livingVoiceRate, Map<String, List<String>> voices) {
		//サウンドパック追加
		soundpacks.put(soundName, new ResourceLittleMaidSoundpack(soundName, livingVoiceRate, voices));
		
		//デフォルトサウンドパック名
		if ("".equals(default_soundpack)) {
			default_soundpack = soundName;
		}
	}
	
	/**
	 * サウンドパックが存在するか判断する
	 * @return
	 */
	public boolean isLoadSoundpack() {
		return (soundpacks.size() > 0);
	}
	
	/**
	 * デフォルトのサウンドパックを取得する
	 * @return
	 */
	public String getDefaultSoundpackName() {
		return default_soundpack;
	}
	
	/**
	 * サウンドパックのリストを取得する
	 * @return
	 */
	public List<ResourceLittleMaidSoundpack> getSoundpackList() {
		return new ArrayList<>(this.soundpacks.values());
	}

	/**
	 * サウンドパック単位の管理用クラス
	 * @author firis-games
	 */
	public static class ResourceLittleMaidSoundpack {
		
		public ResourceLittleMaidSoundpack(String soundName, Float livingVoiceRate, Map<String, List<String>> voices) {
			this.soundpackName = soundName;
			this.livingVoiceRate = livingVoiceRate;
			this.voices = voices;
		}
		
		//サウンドパック名
		public String soundpackName = "";
		
		//サウンドパックのRate設定
		public Float livingVoiceRate = 1.0F;
		
		//サウンドパックのボイス一覧
		public Map<String, List<String>> voices = new HashMap<>();
		
	}
}
