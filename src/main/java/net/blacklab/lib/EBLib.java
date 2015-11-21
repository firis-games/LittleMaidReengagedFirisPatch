package net.blacklab.lib;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "net.blacklab.lib",name="EBLib",version=EBLib.VERSION)
public class EBLib {
	
	public static final String VERSION="2.0.3";
	public static final String VERSION_FORSITE="EL2 Build 3";
	public static final int VERSION_CODE=3;
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		FMLCommonHandler.instance().bus().register(new EventHook());
	}

}
