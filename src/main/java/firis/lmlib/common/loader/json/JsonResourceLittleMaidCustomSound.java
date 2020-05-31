package firis.lmlib.common.loader.json;

import java.util.HashMap;
import java.util.Map;

/**
 * モデルとカスタムを紐づける設定
 * @author firis-games
 *
 */
public class JsonResourceLittleMaidCustomSound {
	
	/**
	 * 標準で使用するサウンドパック名
	 */
	public String default_voice = "";
	
	/**
	 * モデル単位でサウンドパックを設定する
	 */
	public Map<String, ModelVoice> custom_voice = new HashMap<>();
	
	
	/**
	 * モデル単位のサウンド設定管理用クラス
	 * @author firis-games
	 *
	 */
	public static class ModelVoice {
		
		public String default_voice = "";
		public Map<Integer, String> color_voice = new HashMap<>();
		
		public ModelVoice(String voice) {
			default_voice = voice;
			color_voice = new HashMap<>();
		}
		
		public void addColor(int color) {
			color_voice.put(color, this.default_voice);
		}
	}

}
