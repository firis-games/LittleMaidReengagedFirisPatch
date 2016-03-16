package mmmlibx.lib;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mmmlibx.lib.multiModel.MMMLoader.MMMTransformer;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.CommonHelper;
import net.blacklab.lmr.util.DevMode;
import net.blacklab.lmr.util.FileList;
import net.blacklab.lmr.util.FileList.CommonClassLoaderWrapper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@Mod(	modid	= "MMMLibX",
		name	= "MMMLibX",
		version	= LittleMaidReengaged.VERSION,
		acceptedMinecraftVersions=LittleMaidReengaged.ACCEPTED_MCVERSION)
public class MMMLib {

	public static boolean cfg_isModelAlphaBlend = true;
/**	public static final int cfg_startVehicleEntityID = 0;	Forgeには不要	*/
	public static boolean isModelAlphaBlend = true;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (LittleMaidReengaged.cfg_PrintDebugMessage||DevMode.DEVELOPMENT_DEBUG_MODE) {
			System.out.println(String.format("MMMLib-" + pText, pData));
		}
	}
	public static void Debug(boolean isRemote, String pText, Object... pData) {
		// デバッグメッセージ
		Debug("[Side="+(isRemote? "Client":"Server")+"]" + pText, pData);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent pEvent) {
		// ClassLoaderを初期化
		List<URL> urls = new ArrayList<URL>();
		try {
			urls.add(FileList.dirMods.toURI().toURL());
		} catch (MalformedURLException e1) {
		}
		if(DevMode.DEVMODE==DevMode.DEVMODE_ECLIPSE){
			for(File f:FileList.dirDevIncludeClasses){
				try {
					urls.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
				}
			}
		}
		FileList.COMMON_CLASS_LOADER = new CommonClassLoaderWrapper(urls.toArray(new URL[]{}), MMMLib.class.getClassLoader());

		// MMMLibが立ち上がった時点で旧モデル置き換えを開始
		MMMTransformer.isEnable = true;
		
		// コンフィグの解析・設定
		isModelAlphaBlend	= LittleMaidReengaged.cfg_isModelAlphaBlend;
		cfg_isModelAlphaBlend = isModelAlphaBlend;
		
/*
		String ls;
		ls = "DestroyAll";
		lconf.addCustomCategoryComment(ls, "Package destruction of the fixed range is carried out.");
		DestroyAllManager.isDebugMessage = lconf.get(ls, "isDebugMessage", false).getBoolean(false);
		
		ls = "GunsBase";
		lconf.addCustomCategoryComment(ls, "Basic processing of a firearm.");
		GunsBase.isDebugMessage = lconf.get(ls, "isDebugMessage", false).getBoolean(false);
		
		ls = "MoveScreen";
		lconf.addCustomCategoryComment(ls, "The position of a window is automatically moved to a start-up.");
 		MoveWindow.isMoveWindow	= lconf.get(ls, "isMoveWindow", false).getBoolean(false);
		MoveWindow.windowPosX	= lconf.get(ls, "windowPosX", 20).getInt(20);
		MoveWindow.windowPosY	= lconf.get(ls, "windowPosY", 50).getInt(50);
		
		ls = "EzRecipes";
		lconf.addCustomCategoryComment(ls, "Append Recipes from JSON.");
		EzRecipes.isDebugMessage = lconf.get(ls, "isDebugMessage", false).getBoolean(false);
*/

		MMM_StabilizerManager.init();

		// テクスチャパックの構築
		ModelManager.instance.init();
		ModelManager.instance.loadTextures();
		// ロード
		if (CommonHelper.isClient) {
			// テクスチャパックの構築
//			MMM_TextureManager.loadTextures();
//			MMM_StabilizerManager.loadStabilizer();
			// テクスチャインデックスの構築
			Debug("Localmode: InitTextureList.");
			ModelManager.instance.initTextureList(true);
		} else {
			ModelManager.instance.loadTextureServer();
		}
	}

	@Mod.EventHandler
	public void loaded(FMLPostInitializationEvent pEvent) {
		// 独自スクリプトデコーダー
//		EzRecipes.init();
		// 
//		GunsBase.initAppend();
		
		// 旧モデル用変換開始
		MMMTransformer.isEnable = true;
//		MultiModelManager.instance.execute();
		
		// TODO test
		List<File> llist = FileList.getAllmodsFiles(FileList.COMMON_CLASS_LOADER, true);
		for (File lf : llist) {
			Debug("targetFiles: %s", lf.getAbsolutePath());
		}
		try {
			Class<?> lc = ReflectionHelper.getClass(FileList.COMMON_CLASS_LOADER, "net.minecraft.entity.EntityLivingBase");
			Debug("test-getClass: %s", lc.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
