package firis.lmlib;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import firis.lmlib.client.resources.OldZipTexturesWrapper;
import firis.lmlib.client.resources.SoundResourcePack;
import firis.lmlib.loader.LMFileLoader;
import firis.lmlib.manager.LMTextureBoxManager;
import firis.lmlib.manager.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
		modid = LMLibrary.MODID, 
		name = LMLibrary.NAME,
		version = LMLibrary.VERSION,
		dependencies = LMLibrary.MOD_DEPENDENCIES,
		acceptedMinecraftVersions = LMLibrary.MOD_ACCEPTED_MINECRAFT_VERSIONS
)
@EventBusSubscriber(modid=LMLibrary.MODID)
public class LMLibrary {

    public static final String MODID = "lmlibrary";
    public static final String NAME = "LMLibrary";
    public static final String VERSION = "0.1";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);";
    public static final String MOD_ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";
    
    @Instance(LMLibrary.MODID)
    public static LMLibrary INSTANCE;
    
    /** logger */
    public static Logger logger = LogManager.getLogger(LMLibrary.MODID);
    
    /**
     * コンストラクタ
     */
	public LMLibrary() {
		//カスタムリソースパック追加
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			@SuppressWarnings("unchecked")
			List<IResourcePack> defaultResourcePacks = (List<IResourcePack>)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), new String[] { "defaultResourcePacks", "field_110449_ao" });
			defaultResourcePacks.add(new SoundResourcePack());
			defaultResourcePacks.add(new OldZipTexturesWrapper());
		}
	}
    
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
		//リトルメイドファイルローダー
		LMFileLoader.instance.load();
		
		//テクスチャモデル初期化
		LMTextureBoxManager.instance.init();
		
		//サウンドパックセットアップ
		SoundManager.instance.createSounds();
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
    
}
