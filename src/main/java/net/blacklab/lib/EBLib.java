package net.blacklab.lib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "net.blacklab.lib",name="EBLib",version=EBLib.VERSION)
public class EBLib {
	
	public static final String VERSION="3.0.7";
	public static final String VERSION_FORSITE="EL3 Build 7";
	public static final int VERSION_CODE=4;
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventHook());
	}

}
