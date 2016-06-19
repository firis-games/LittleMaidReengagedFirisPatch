package net.blacklab.lib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = "net.blacklab.lib",
		name="EBLib",
		version=EBLib.VERSION,
		acceptedMinecraftVersions="[1.9,1.9.100)",
		updateJSON = "http://mc.el-blacklab.net/eblib-version.json")
public class EBLib {

	public static final String VERSION="5.2.0.3";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
	}

}
