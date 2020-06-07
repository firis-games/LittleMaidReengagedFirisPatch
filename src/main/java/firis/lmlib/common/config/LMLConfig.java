package firis.lmlib.common.config;

import java.io.File;

import firis.lmlib.LMLibrary;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * LMLibrary設定
 * @author firis-games
 *
 */
public class LMLConfig {
	
	protected static final String GROUP_LOADER = "LOADER";
	
	/**
	 * 開発モード判定用
	 */
	public static final boolean DEVELOPER_MODE = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	
	/**
	 * loaderキャッシュ機能
	 */
	public static boolean cfg_loader_is_cache = false;
	
	/**
	 * ディレクトリのテクスチャを直接参照する機能
	 */
	public static boolean cfg_loader_texture_from_directory = true;
	
	/**
	 * sounds.jsonをファイルとして出力する設定
	 */
	public static boolean cfg_loader_output_sounds_json = false;
	
	/**
	 * おしゃべり頻度の設定
	 */
	public static float cfg_livingVoiceRate = 0.2F;
	
	/**
	 * 設定ファイル読込
	 * @param configFile
	 */
	public static void init(FMLPreInitializationEvent event) {
		
		File configFile = new File(event.getModConfigurationDirectory(), "LMLibrary.cfg");
		Configuration config = new Configuration(configFile, LMLibrary.VERSION, true);
		config.load();
		
		//グループコメント
		config.addCustomCategoryComment(GROUP_LOADER, "LMLibrary Loader Setting");
		
		//ファイルローダー機能のキャッシュ機能設定
		cfg_loader_is_cache = config.getBoolean("EnableFileLoaderCache", GROUP_LOADER, false,
				"Enable FileLoader Caching.");
		
		//sounds.jsonファイルの出力設定
		cfg_loader_output_sounds_json = config.getBoolean("OutputSoundsJson", GROUP_LOADER, false,
				"Output sounds.json.");
		
		//テクスチャのリソースパックロードの設定
		cfg_loader_texture_from_directory = config.getBoolean("EnableTextureLoadResourcepack", GROUP_LOADER, false,
				"Developer mode setting. Reads a texture from a resourcepack.");
		
		//メイドさんのランダムVoiceRate
		cfg_livingVoiceRate = config.getFloat("LivingVoiceRate", GROUP_LOADER, 0.2F, 0.0F, 1.0F,
				"Setting the rate for living voice playback.[1.0 = 100%]");
		
		config.save();
	}
}
