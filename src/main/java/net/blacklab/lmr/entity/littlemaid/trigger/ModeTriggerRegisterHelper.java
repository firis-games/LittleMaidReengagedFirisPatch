package net.blacklab.lmr.entity.littlemaid.trigger;

import java.util.HashMap;
import java.util.Map;

import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTrigger.Status;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ModeTriggerRegisterHelper {
	
	/**
	 * Configから生成するTriggerアイテムのマップ
	 */
	private static Map<String, Map<Item, Status>> triggerItemIdMap = new HashMap<>();
	
	
	public static void register(String maidMode, String maidTrigger) {
		register(maidMode, maidTrigger, new HashMap<>());
	}
	
	/**
	 * ConfigのIdを含むアイテムをtriggerへ登録する
	 */
	public static void register(String maidMode, String maidTrigger, Map<Item, Status> addTriggerMap) {
		
		String triggerKey = maidMode.toLowerCase();

		//対象が存在しない場合一度だけ生成する
		if (!triggerItemIdMap.containsKey(triggerKey)) {
			Map<Item, Status> newTriggerMap = new HashMap<>(addTriggerMap);
			for (String cfgItem : LMRConfig.cfg_trigger_item_ids) {
				String[] items = cfgItem.split(":");
				if (items.length == 3) {
					String mode = items[0].toLowerCase();
					//モードが一致するかの判断
					if (triggerKey.equals(mode)) {
						//アイテムIDのチェック
						Item triggerItem = Item.REGISTRY.getObject(new ResourceLocation(items[1], items[2]));
						if (triggerItem != null) {
							//triggerへ登録
							newTriggerMap.put(triggerItem, Status.TRIGGER);
						}
					}
				}
			}
			//trgger登録
			triggerItemIdMap.put(triggerKey, newTriggerMap);
		}
		
		//Map取得
		Map<Item, Status> triggerMap = triggerItemIdMap.get(triggerKey);
		
		//トリガーの登録を行う
		ModeTrigger.registerTrigger(maidTrigger, triggerMap);
	}

}
