package net.blacklab.lib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "net.blacklab.lib",name="EBLib",version=EBLib.VERSION, acceptedMinecraftVersions="[1.8,1.8.9]")
public class EBLib {

	public static final String VERSION="3.1.10";
	public static final String VERSION_FORSITE="EL3 Build 10";
	public static final int VERSION_CODE=5;

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventHook());
	}

}
