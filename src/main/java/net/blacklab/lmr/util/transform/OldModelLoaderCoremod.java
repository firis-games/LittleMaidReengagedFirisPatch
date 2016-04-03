package net.blacklab.lmr.util.transform;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

//@TransformerExclusions({"net.blacklab.lmr.util.transform"})
public class OldModelLoaderCoremod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"net.blacklab.lmr.util.transform.Transformer"};
	}

	@Override
	public String getModContainerClass() {
		return "net.blacklab.lmr.util.transform.OldModelLoaderContainer";
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
