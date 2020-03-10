package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.api.plugin.ILittleMaidPlugin;
import net.blacklab.lmr.api.plugin.LittleMaidPlugin;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * プラグイン処理を管理する
 * @author computer
 *
 */
public class PluginManager {

	public static Map<EventPriority, List<ILittleMaidPlugin>> priorityPluginList = new HashMap<>();
	
	//優先度リスト
	private static List<EventPriority> priorityList = initPriorityList();
	private static List<EventPriority> initPriorityList() {
		List<EventPriority> priorityList = new ArrayList<>();
		priorityList.add(EventPriority.HIGHEST);
		priorityList.add(EventPriority.HIGH);
		priorityList.add(EventPriority.NORMAL);
		priorityList.add(EventPriority.LOW);
		priorityList.add(EventPriority.LOWEST);
		return priorityList;
	}
	
	/**
	 * preInitでpluginクラスの取得を行う
	 * @param event
	 */
	public static void preInitPluginLoad(FMLPreInitializationEvent event) {
		
		ASMDataTable asmDataTable = event.getAsmData();
		
    	//Pluginアノテーションを持つデータを取得
    	Set<ASMData> asmDatas = asmDataTable.getAll(LittleMaidPlugin.class.getCanonicalName());
    	
    	//対象を取得する
    	for (ASMData asmData : asmDatas) {
    		try {
    			//アノテーション情報を取得する
    			Map<String, Object> annoList = asmData.getAnnotationInfo();
    			String modid = (String) annoList.get("modid");
    			ModAnnotation.EnumHolder enumHolderPriority = (ModAnnotation.EnumHolder) annoList.get("priority");
    			EventPriority priority = EventPriority.NORMAL;
    			
    			//getAnnotationInfoはデフォルト値が反映されないようなので手動で設定する
    			modid = modid != null ? modid : "";
    			priority = enumHolderPriority == null ? priority : EventPriority.valueOf(enumHolderPriority.getValue());
    			
    			//ModIdが指定されている場合はModが読み込まれているか判断
    			if ("".equals(modid) || Loader.isModLoaded(modid)) {
    				
    				Class<?> asmClass = Class.forName(asmData.getClassName());
        			Class<? extends ILittleMaidPlugin> asmInstanceClass = asmClass.asSubclass(ILittleMaidPlugin.class);
        			
        			//優先度によって格納場所を変更
        			List<ILittleMaidPlugin> pluginList = priorityPluginList.getOrDefault(priority, new ArrayList<>());
        			pluginList.add(asmInstanceClass.newInstance());
        			priorityPluginList.put(priority, pluginList);
    			}
    		} catch (Exception e) {
    			LittleMaidReengaged.logger.error("plugin load error " + asmData.getClassName());
			}
    	}
	}
	
	/**
	 * preInitでプラグインの処理を行う
	 */
	public static void preInitRegisterPlugin(FMLPreInitializationEvent event) {

		//メイドモードの登録処理を優先順で行う
		for (EventPriority priority : priorityList) {
			
			List<ILittleMaidPlugin> pluginList = priorityPluginList.getOrDefault(priority, new ArrayList<>());
			
			for (ILittleMaidPlugin plugin : pluginList) {
				plugin.registerLittleMaidMode(MaidModeManager.instance);
			}
		}
		
		//メイドモードの初期化処理
		MaidModeManager.instance.init();
		
	}
	
}
