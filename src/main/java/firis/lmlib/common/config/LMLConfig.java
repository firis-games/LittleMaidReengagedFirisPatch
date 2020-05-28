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
	public static float cfg_voiceRate = 0.2F;
	
	/**
	 * 設定ファイル読込
	 * @param configFile
	 */
	public static void init(FMLPreInitializationEvent event) {
		
		File configFile = new File(event.getModConfigurationDirectory(), "LMLibrary.cfg");
		Configuration config = new Configuration(configFile, LMLibrary.VERSION, true);
		config.load();
		
		
		
		config.save();
	}
}
