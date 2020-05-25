package firis.lmlib.core;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

/**
 * 旧APIで構築されたマルチモデルクラスを現APIへ変換する
 * @author firis-games
 *
 */
public class LMLibCoreModContainer extends DummyModContainer {

	public LMLibCoreModContainer() {
		super(new ModMetadata());
		ModMetadata lmeta = getMetadata();
		lmeta.modId = "lmlibrary_coremod";
		lmeta.name = "LMLibraryCoreMod";
		lmeta.version = "0.1";
		lmeta.authorList = Arrays.asList("firis-games");
		lmeta.description = "Convert Old LittleMaid Multi-Model Classes. 1.6.2 to 1.12.2";
		lmeta.url = "";
		lmeta.credits = "";
		setEnabledState(true);
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}
