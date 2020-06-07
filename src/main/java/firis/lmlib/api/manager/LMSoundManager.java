package firis.lmlib.api.manager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import firis.lmlib.LMLibrary;
import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.constant.EnumSound;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.common.config.LMLConfig;
import firis.lmlib.common.helper.ResourceFileHelper;
import firis.lmlib.common.loader.LMSoundHandler;
import firis.lmlib.common.loader.json.JsonResourceLittleMaidCustomSound;
import firis.lmlib.common.loader.json.JsonResourceLittleMaidCustomSound.ModelVoice;
import firis.lmlib.common.loader.json.JsonResourceLittleMaidSound.ResourceLittleMaidSoundpack;
import net.minecraft.util.ResourceLocation;

/**
 * メイドさんのサウンド関連を管理する
 * @author firis-games
 *
 */
public class LMSoundManager {
		
	/**
	 * カスタムモデルサウンド
	 * 
	 * モデル＋カラー番号でボイス設定を切り替える
	 */
	private JsonResourceLittleMaidCustomSound customSound = null;
	
	/**
	 * デフォルトのサウンドパックを設定する
	 */
	private String defaultSoundpack = "";
	
	/**
	 * Sounds.jsonで利用しているoggファイルのClassloaderパスを保持する
	 */
	private List<String> classloaderResoucePath = new ArrayList<>();
	
	/**
	 * LivingVoiceRateの一覧
	 */
	private Map<String, Float> livingVoiceRate = new HashMap<>();
	
	/**
	 * sounds.jsonファイルの定義
	 */
	private String sounds_json = "";
	
	/**
	 * 読み込んだSoundから必要なものを生成する
	 * モデルの生成処理が終わっていることが前提
	 */
	public void createSounds() {
		
		//サウンドパックがロードされていない場合は何もしない
		if (!LMSoundHandler.resourceLittleMaidSound.isLoadSoundpack()) return;
		
		//デフォルトボイス設定追加
		this.defaultSoundpack = LMSoundHandler.resourceLittleMaidSound.getDefaultSoundpackName();
		
		//カスタムのJson生成
		if (!this.loadJsonCustomModelVoice()) {
			//読み込めなかった場合に作成
			this.createJsonCustomModelVoice();
		}
		
		//sounds.json生成
		this.createJsonMinecraftSounds();
		
		//セットアップ
		//Mod内で使用する形式へ変換する
		for (ResourceLittleMaidSoundpack soundinfo : LMSoundHandler.resourceLittleMaidSound.getSoundpackList()) {
			
			//変換処理
			for(String voiceId : soundinfo.voices.keySet()) {
				//クラスローダーのパスをセットする
				for (String voicePath : soundinfo.voices.get(voiceId)) {
					classloaderResoucePath.add(voicePath);
				}
			}
			
			//livingVoiceのリストを作成する
			this.livingVoiceRate.put(soundinfo.soundpackName, soundinfo.livingVoiceRate);
			
		}
	}
	
	/**
	 * カスタム設定を読込
	 */
	private boolean loadJsonCustomModelVoice() {
		
		boolean ret = false;
		
		//Jsonファイルを読込
		customSound = ResourceFileHelper.readFromJson("setting_custom_sounds.json", JsonResourceLittleMaidCustomSound.class);
		if (customSound != null) {
			ret = true;
		}
		return ret;
	}

	/**
	 * モデルとサウンドパックを紐づける設定を生成する
	 */
	private void createJsonCustomModelVoice() {
		
		JsonResourceLittleMaidCustomSound jsonObject = new JsonResourceLittleMaidCustomSound();
		jsonObject.default_voice = this.defaultSoundpack;
		
		for (LMTextureBox textureBox : LMLibraryAPI.instance().getTextureManager().getLMTextureBoxList()) {
			
			//メイドさんのテクスチャが存在する場合のみ対象とする
			if (textureBox.hasLittleMaid()) {
				
				JsonResourceLittleMaidCustomSound.ModelVoice modelVoice = new JsonResourceLittleMaidCustomSound.ModelVoice(jsonObject.default_voice);
				
				//カラー情報を持つ場合のみ個別カラー設定を追加する
				for (int color = 0; color < 16; color++) {
					if (textureBox.hasColor(color)) {
						modelVoice.addColor(color);
					}
				}
				//カスタマイズ情報を書き出し
				jsonObject.custom_voice.put(textureBox.getTextureModelName(), modelVoice);
			}			
		}
		
		//生成したものを保持する
		customSound = jsonObject;
		
		//設定ファイルを出力する
		ResourceFileHelper.writeToJson("setting_custom_sounds.json", jsonObject);
		
	}
	
	/**
	 * サウンドパック一覧を元にMinecraftで使用する
	 * @return
	 */
	private void createJsonMinecraftSounds() {
		
		JsonObject jsonObject = new JsonObject();
		
		//サウンドパック一覧をMinecraftのSounds.json形式へ変換する
		for (ResourceLittleMaidSoundpack soundinfo : LMSoundHandler.resourceLittleMaidSound.getSoundpackList()) {
			
			//レコード単位で生成する
			for (String voiceId : soundinfo.voices.keySet()) {
				
				JsonObject elementObject = new JsonObject();
				
				//音声名
				String elementName = soundinfo.soundpackName + "." + voiceId;
				
				//category
				elementObject.addProperty("category", "master");
				
				//Sounds
				JsonArray soundsElements = new JsonArray();
				for (String voice : soundinfo.voices.get(voiceId)) {
					//sounds.json形式のパスへ変換する
					String voicePath = voice;
					voicePath = LMLibrary.MODID + ":" + elementName + "//" + voicePath;
					soundsElements.add(voicePath);
				}
				elementObject.add("sounds", soundsElements);
				
				//親Objectへ挿入する
				jsonObject.add(elementName, elementObject);
			}
		}
		
		this.sounds_json = ResourceFileHelper.jsonToString(jsonObject);
		
		//ファイルを書き出し
		if (LMLConfig.cfg_loader_output_sounds_json) {
			ResourceFileHelper.writeToFile("sounds.json", this.sounds_json);
		}
		
	}
	
	/**
	 * 対象の音声が存在するか判断する
	 * @param resoucePath
	 * @return
	 */
	public boolean isResourceExists(ResourceLocation resource) {
		//音声パスが存在するかチェックする
		return !getConvertSoundPath(resource).equals("");
	}
	
	/**
	 * 音声の実体パスを返却する
	 * @param resource
	 * @return
	 */
	public String getResourceClassLoaderPath(ResourceLocation resource) {
		return getConvertSoundPath(resource);
	}
	
	/**
	 * ResourceLocationから音声パスの実体を取得する
	 * Minecraft1.12.2ではパスは強制的に小文字になるためここで変換する
	 * @param resource
	 * @return
	 */
	private String getConvertSoundPath(ResourceLocation resource) {
		
		String ret = "";
		
		String path = "";
		String[] paths = resource.getResourcePath().split("//");
		if (paths.length == 2) {
			path = paths[1].replace(".ogg", "") + ".ogg";
		}
		
		for (String classLoaderPath : classloaderResoucePath) {
			//小文字化して照合する
			if (path.toLowerCase().equals(classLoaderPath.toLowerCase())) {
				ret = classLoaderPath;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * デフォルトボイスパックがない場合はサウンドパックがないと判断する
	 * @return
	 */
	public boolean isFoundSoundpack() {
		return !defaultSoundpack.equals("");
	}
	
	/**
	 * モデルとカラー情報をもとに音声を取得する
	 * @param sound
	 * @param texture
	 * @param color
	 * @return
	 */
	public String getSoundNameWithModel(EnumSound sound, String texture, Integer color) {
		
		String soundpack = this.defaultSoundpack;
		
		//カスタムサウンド設定から使用するSoundpackを取得する
		for (String modelName : customSound.custom_voice.keySet()) {
			if (modelName.equals(texture)) {
				ModelVoice modelVoice = customSound.custom_voice.get(modelName);
				//モデルのデフォルトパックを反映
				soundpack = modelVoice.default_voice;
				//色情報を確認
				if (modelVoice.color_voice != null &&
						modelVoice.color_voice.containsKey(color)) {
					//色の個別設定があれば参照する
					soundpack = modelVoice.color_voice.get(color);
				}
				break;
			}
		}
		
		//サウンドIDを生成
		String soundType = soundpack + "." + sound.getVoiceId();
		return soundType;
	}
	
	/**
	 * Resourcepackで利用するsounds.jsonをInputStream形式で返却する
	 * @return
	 */
	public InputStream getResourcepackSoundsJson() {
		InputStream is = null;
		try {
			String sounds = this.sounds_json;
			if (LMLConfig.cfg_loader_output_sounds_json) {
				ResourceFileHelper.writeToFile("sounds.json", this.sounds_json);
			}
			is = new ByteArrayInputStream(sounds.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LMLibrary.logger.error("getResourcepackSoundsJson : ", e);
		};
		return is;
	}
	
	/**
	 * LivingVoiceのレートを取得する
	 * @param textureName
	 * @return
	 */
	public Float getLivingVoiceRatio(String soundpack) {
		
		if (this.livingVoiceRate.containsKey(soundpack)) {
			return this.livingVoiceRate.get(soundpack);
		}
		
		//対象外の場合は標準レートを返却する
		return LMLConfig.cfg_livingVoiceRate;
	}
}
