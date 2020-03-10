package net.blacklab.lmr.util.manager.plugin;

import net.blacklab.lmr.api.plugin.ILittleMaidPlugin;
import net.blacklab.lmr.api.plugin.LittleMaidPlugin;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Archer;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_BookDecorder;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Cooking;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Farmer;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Fencer;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Healer;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Pharmacist;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Playing;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Shearer;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_TorchLayer;
import net.blacklab.lmr.entity.littlemaid.mode.custom.EntityMode_Lumberjack;
import net.blacklab.lmr.entity.littlemaid.mode.custom.EntityMode_SugarCane;
import net.blacklab.lmr.util.manager.MaidModeManager;

@LittleMaidPlugin
public class LittleMaidRegisterPlugin implements ILittleMaidPlugin {

	/**
	 * メイドさんのモードを登録する
	 */
	@Override
	public void registerLittleMaidMode(MaidModeManager manager) {

		//メイドモードクラスの登録
		//基本職業
		manager.register(EntityMode_Archer.class);
		manager.register(EntityMode_Basic.class);
		manager.register(EntityMode_BookDecorder.class);
		manager.register(EntityMode_Cooking.class);
		manager.register(EntityMode_Farmer.class);
		manager.register(EntityMode_Fencer.class);
		manager.register(EntityMode_Healer.class);
		manager.register(EntityMode_Pharmacist.class);
		manager.register(EntityMode_Playing.class);
		manager.register(EntityMode_Shearer.class);
		manager.register(EntityMode_TorchLayer.class);
		
		//カスタム職業
		manager.register(EntityMode_Lumberjack.class);
		manager.register(EntityMode_SugarCane.class);
		
	}
}
