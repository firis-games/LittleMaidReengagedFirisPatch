package firis.lmlibrary.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class LMLibCoreMod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"firis.lmlibrary.core.LMLibTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "firis.lmlibrary.core.LMLibCoreModContainer";
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
