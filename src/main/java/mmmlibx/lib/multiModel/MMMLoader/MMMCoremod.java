package mmmlibx.lib.multiModel.MMMLoader;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

//@TransformerExclusions({"mmmlibx.lib.multiModel.MMMLoader"})
public class MMMCoremod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"mmmlibx.lib.multiModel.MMMLoader.MMMTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "mmmlibx.lib.multiModel.MMMLoader.MMMModContainer";
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
