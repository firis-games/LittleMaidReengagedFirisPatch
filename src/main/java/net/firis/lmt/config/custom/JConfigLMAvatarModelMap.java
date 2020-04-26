package net.firis.lmt.config.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * LMAvatarのモデルの設定クラス
 * @author firis-games
 *
 */
public class JConfigLMAvatarModelMap {
	
	public Map<String, JConfigLMAvatarModel> lmavatar = new HashMap<>();
	
	public void put(String key, JConfigLMAvatarModel value) {
		this.lmavatar.put(key, value);
	}
	
	public JConfigLMAvatarModel get(String key) {
		return this.lmavatar.get(key);
	}
	
	public boolean containsKey(String key) {
		return this.lmavatar.containsKey(key);
	}

}
