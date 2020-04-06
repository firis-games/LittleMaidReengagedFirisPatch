package net.blacklab.lmr.util.loader.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * サウンドパック保存用Jsonフォーマット
 * @author firis-games
 *
 */
public class JsonConfigLittleMaidSound {
	
	//サウンドパック名
	public String voiceName = "";
	
	//サウンドパックのRate設定
	public Float voiceRate = 1.0F;
	
	//サウンドパックのボイス一覧
	public Map<String, List<String>> voices = new HashMap<>();

}
