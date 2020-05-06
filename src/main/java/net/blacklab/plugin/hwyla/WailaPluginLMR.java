package net.blacklab.plugin.hwyla;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.plugin.hwyla.provider.DataProviderLMR;
import net.blacklab.plugin.hwyla.provider.DataProviderLMRAnimal;
import net.minecraft.entity.EntityLiving;

@WailaPlugin(value=LittleMaidReengaged.MODID)
public class WailaPluginLMR implements IWailaPlugin  {

	public void register(IWailaRegistrar registrar) {
		
		if (LMRConfig.cfg_plugin_hwyla) {
			DataProviderLMR dataProvider = new DataProviderLMR();
			DataProviderLMRAnimal dataProviderAnimal = new DataProviderLMRAnimal();
			
			//メイドさん表示登録
			registrar.registerBodyProvider(dataProvider, EntityLittleMaid.class);
			registrar.registerNBTProvider(dataProvider, EntityLittleMaid.class);
			
			//アニマル表示登録
			registrar.registerBodyProvider(dataProviderAnimal, EntityLiving.class);
			registrar.registerNBTProvider(dataProviderAnimal, EntityLiving.class);
		}
		
	}
}
