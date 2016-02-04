package net.blacklab.lib;

import net.blacklab.lib.version.Version;
import net.blacklab.lib.version.Version.VersionData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "net.blacklab.lib",name="EBLib",version=EBLib.VERSION, acceptedMinecraftVersions="[1.8,1.8.9]")
public class EBLib {

	public static final String VERSION="3.3.13";
	public static final String VERSION_FORSITE="EL3 Build 13";
	public static final int VERSION_CODE=7;
	
	public static final VersionData currentVersion = new VersionData(VERSION_CODE, VERSION, VERSION_FORSITE);
	public static VersionData latestVersion = new VersionData(1, "1.0.1", "EL1");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		latestVersion = Version.getLatestVersion("http://mc.el-blacklab.net/eblibversion.txt");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventHook());
	}

}
