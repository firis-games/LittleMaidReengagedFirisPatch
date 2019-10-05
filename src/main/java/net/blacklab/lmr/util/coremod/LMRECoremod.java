package net.blacklab.lmr.util.coremod;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class LMRECoremod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"net.blacklab.lmr.util.coremod.Transformer"};
	}

	@Override
	public String getModContainerClass() {
		return "net.blacklab.lmr.util.coremod.LMRECoreModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
