package firis.lmlib.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * LMLibraryCore
 * @author firis-games
 *
 */
public class LMLibCoreMod implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"firis.lmlib.core.LMLibTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "firis.lmlib.core.LMLibCoreModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
