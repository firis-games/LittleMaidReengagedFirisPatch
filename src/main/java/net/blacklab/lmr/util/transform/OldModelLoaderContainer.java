package net.blacklab.lmr.util.transform;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class OldModelLoaderContainer extends DummyModContainer {

	public OldModelLoaderContainer() {
		super(new ModMetadata());
		ModMetadata lmeta = getMetadata();

		lmeta.modId		= "oldmodelloader";
		lmeta.name		= "LMR OldModelLoader";
		lmeta.version	= LittleMaidReengaged.VERSION;
		lmeta.authorList	= Arrays.asList("Verclene");
		lmeta.description	= "The MultiModel before 1.6.2 is read.";
		lmeta.url			= "";
		lmeta.credits		= "";
		setEnabledState(true);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		// これ付けないとDisableで判定されちゃう。
		bus.register(this);
		return true;
	}

}
