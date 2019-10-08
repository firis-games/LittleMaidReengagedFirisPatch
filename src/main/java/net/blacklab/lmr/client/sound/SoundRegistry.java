package net.blacklab.lmr.client.sound;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.blacklab.lib.obj.Pair;
import net.blacklab.lib.obj.SinglePair;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.EnumSound;

public class SoundRegistry {

	public static final String DEFAULT_TEXTURE_REGISTRATION_KEY = "!#DEFAULT#!";

	// Sound→((テクスチャ名+色)+パス)の順．
	private EnumMap<EnumSound, HashMap<Pair<String, Integer>, String>> registerMap;
	// 実際の参照パス
	private Map<String, List<String>> pathMap;

	// LivingVoiceの再生レート
	private static Map<String, Float> ratioMap;

	// ロックされたテクスチャ
	private List<String> markedTexture = new ArrayList<String>();

	private static SoundRegistry instR = new SoundRegistry();

	private SoundRegistry() {
		registerMap = new EnumMap<EnumSound, HashMap<Pair<String,Integer>,String>>(EnumSound.class);
		pathMap = new HashMap<String, List<String>>();
		ratioMap = new HashMap<String, Float>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerSoundName(EnumSound enumSound, String texture, Integer color, String name) {
		// サウンド・ネームの登録
		Map<Pair<String, Integer>, String> map = instR.registerMap.get(enumSound);
		if (map == null) {
			instR.registerMap.put(enumSound, new HashMap<Pair<String,Integer>, String>());
			map = instR.registerMap.get(enumSound);
		} else if (map.containsKey(new SinglePair(texture, color)) && !map.get(new SinglePair(texture, color)).equals("<P>")) {
			return;
		}
		map.put(new SinglePair<String, Integer>(texture, color), name);
	}

	public static void setLivingVoiceRatio(String name, Float ratio) {
		ratioMap.put(name, ratio);
	}

	public static Float getLivingVoiceRatio(String name) {
		Float f = ratioMap.get(name);
		if (f == null) {
			f = LittleMaidReengaged.cfg_voiceRate;
		}
		return f;
	}

	protected static void markTexVoiceReserved(String texture) {
		instR.markedTexture.add(texture);
		for (EnumSound enumSound: new ArrayList<EnumSound>(Arrays.asList(EnumSound.values()))) {
			Map<Pair<String, Integer>, String> map = instR.registerMap.get(enumSound);
			if (map == null) {
				instR.registerMap.put(enumSound, new HashMap<Pair<String,Integer>, String>());
				map = instR.registerMap.get(enumSound);
			}
			map.put(new SinglePair<String, Integer>(texture, -1), "<P>");

			for (int i=0; i<16; i++) {
				map.remove(new SinglePair<String, Integer>(texture, i));
			}
		}
	}

	public static boolean isTexVoiceMarked(String texture) {
		return instR.markedTexture.contains(texture);
	}

	protected static void copySoundsAdjust() {
		for (EnumSound eSound: EnumSound.values()) {
			int i = eSound.index;
			Map<Pair<String, Integer>, String> srcMap = instR.registerMap.get(EnumSound.getEnumSound(i & 0xff0));
			if (srcMap == null) {
				continue;
			}
			Map<Pair<String, Integer>, String> dstMap = instR.registerMap.get(eSound);
			if (dstMap == null) {
				instR.registerMap.put(eSound, new HashMap<Pair<String,Integer>, String>());
				dstMap = instR.registerMap.get(eSound);
			}

			for (Entry<Pair<String, Integer>, String> entryD: new HashMap<Pair<String, Integer>, String>(dstMap).entrySet()) {
				if ("^".equals(entryD.getValue())) {
					if (DEFAULT_TEXTURE_REGISTRATION_KEY.equals(entryD.getKey().getKey()) &&
							entryD.getKey().getValue() == -1) {
						for (Entry<Pair<String, Integer>, String> entryS: srcMap.entrySet()) {
							if (!isTexVoiceMarked(entryS.getKey().getKey())) {
								dstMap.put(entryS.getKey(), entryS.getValue());
							}
						}
					} else {
						dstMap.put(entryD.getKey(), srcMap.get(entryD.getKey()));
					}
				}
			}
		}
	}

	public static List<String> getRegisteredNamesList() {
		List<String> retmap = new ArrayList<String>();
		for (Map<Pair<String, Integer>, String> v: instR.registerMap.values()) {
			for (String f: v.values()) {
				if (f!=null && !retmap.contains(f) && !f.endsWith("^") && !f.equals("<P>") && !f.isEmpty()) retmap.add(f);
			}
		}
		return retmap;
	}

	public static void registerSoundPath(String name, String path) {
		// サウンドの種類を増やす
		List<String> g = instR.pathMap.get(name);
		if (g == null) {
			instR.pathMap.put(name, new ArrayList<String>());
			g = instR.pathMap.get(name);
		}
		g.add(path);
	}

	public static String getSoundRegisteredName(EnumSound sound, String texture, Integer color) {
		HashMap<Pair<String, Integer>, String> tMap = instR.registerMap.get(sound);
		Pair<String, Integer> value = new SinglePair<String, Integer>(null, 0);
		if (tMap != null) {
			for (Entry<Pair<String, Integer>, String> entry: tMap.entrySet()) {
				if (entry.getValue() == null || entry.getValue().isEmpty()) {
					continue;
				}
				if (entry.getKey().getKey().equals(texture) && entry.getKey().getValue() == color &&
						value.getValue() < 3) {
					LittleMaidReengaged.Debug("FOUND 3 %s", entry.getValue());
					value.setKey(entry.getValue()).setValue(3);
				}
				if (entry.getKey().getKey().equals(texture) && entry.getKey().getValue() == -1 &&
						value.getValue() < 2) {
					LittleMaidReengaged.Debug("FOUND 2 %s", entry.getValue());
					value.setKey(entry.getValue()).setValue(2);
				}
				if (entry.getKey().getKey().equals(DEFAULT_TEXTURE_REGISTRATION_KEY) && entry.getKey().getValue() == color &&
						value.getValue() < 1) {
					LittleMaidReengaged.Debug("FOUND 1 %s", entry.getValue());
					value.setKey(entry.getValue()).setValue(1);
				}
				if (entry.getKey().getKey().equals(DEFAULT_TEXTURE_REGISTRATION_KEY) && entry.getKey().getValue() == -1 &&
						value.getValue() == 0) {
					LittleMaidReengaged.Debug("FOUND 0 %s", entry.getValue());
					value.setKey(entry.getValue());
				}
			}
		}
		return value.getKey();
	}

	public static boolean isSoundNameRegistered(String name) {
		//mc1.12.2対応
		for (String key : getRegisteredNamesList()) {
			if(key.toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getPathListFromRegisteredName(String name) {
		return instR.pathMap.get(convertPathNameFromMc1_12_2(name));
	}

	public static String getPathFromRegisteredName(String name){
		List<String> g = getPathListFromRegisteredName(name);
		if (g == null) return null;
		String ret = g.get((int)(Math.random() * g.size()));
		return ret;
	}

	public static InputStream getSoundStream(String name) {
		String aString = getPathFromRegisteredName(name);
		LittleMaidReengaged.Debug("GETSTREAM %s", aString);
		return LittleMaidReengaged.class.getClassLoader().getResourceAsStream(aString);
	}

	public static InputStream getSoundStream(EnumSound sound, String texture, Integer color) {
		return getSoundStream(getSoundRegisteredName(sound, texture, color));
	}
	
	public static String convertPathNameFromMc1_12_2(String name) {
		String keyName = name;
		//mc1.12.2対応
		for (String key : instR.pathMap.keySet()) {
			if(key.toLowerCase().equals(name.toLowerCase())) {
				keyName = key;
				break;
			}
		}
		return keyName;
	}
	
	/**
	 * mc1.12.2はリソースパスが小文字として扱われるため
	 * Sound取得時に正確な名前を取得する必要がある
	 * @param name
	 * @return
	 */
	public static String convertPathNameListFromMc1_12_2(String name) {
		String keyName = name;
		//mc1.12.2対応
		for (List<String> list : instR.pathMap.values()) {
			for (String key : list) {
				if(key.toLowerCase().equals(name.toLowerCase())) {
					keyName = key;
					break;
				}				
			}
		}
		return keyName;
	}

}
