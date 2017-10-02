package net.blacklab.lmr.entity.littlemaid.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;

public class ModeTrigger implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static enum Status {
		NOT_REGISTERED,
		TRIGGER,
		NON_TRIGGER
	}

	private static List<String> selector = new ArrayList<>();
	
	private static ModeTrigger defaultTrigger = new ModeTrigger();
	
	private Map<String, Map<Item, Status>> triggerMap;
	
	private ModeTrigger() {
		triggerMap = new HashMap<>();
	}
	
	public static ModeTrigger getDefaultInstance() {
		if (selector.isEmpty()) {
			throw new IllegalStateException("No selector is registered");
		}
		ModeTrigger result = defaultTrigger.clone();
		return result;
	}
	
	/**
	 * Register selector key. 
	 * The key cannot be duplicated, so "[modid]:[selector]" form is recommended.
	 * @param pSelector
	 * @param pDefaultMap
	 */
	public static void registerTrigger(String pSelector, Map<Item, Status> pDefaultMap) {
		if (selector.contains(pSelector)) {
			throw new IllegalArgumentException("The key "+pSelector+" is already registered");
		}
		selector.add(pSelector);
		
		defaultTrigger.appendTriggerMap(pSelector, pDefaultMap);
	}
	
	public static List<String> getSelectorList() {
		return selector;
	}

	private void appendTriggerMap(String pSelector, Map<Item, Status> pMap) {
		if (pMap == null) {
			throw new IllegalArgumentException(new NullPointerException("DefaultMap cannot be null"));
		}
		triggerMap.put(pSelector, pMap);
	}
	
	@Nonnull
	private Map<Item, Status> getTriggerMap(String pSelector) {
		if (!selector.contains(pSelector)) {
			throw new IllegalArgumentException("The key "+pSelector+" is not registered");
		}
		if (!triggerMap.containsKey(pSelector)) {
			triggerMap.put(pSelector, new HashMap<>());
		}
		return triggerMap.get(pSelector);
	}
	
	public void activateTrigger(String pSelector, Item pItem) {
		setTriggerStatus(pSelector, pItem, Status.TRIGGER);
	}
	
	public void deactivateTrigger(String pSelector, Item pItem) {
		setTriggerStatus(pSelector, pItem, Status.NON_TRIGGER);
	}
	
	private void setTriggerStatus(String pSelector, Item pItem, Status pStatus) {
		Map<Item, Status> target = getTriggerMap(pSelector);
		target.put(pItem, pStatus);
	}
	
	@Nonnull
	private Status getTriggerStatus(String pSelector, Item pItem) {
		Map<Item, Status> target = getTriggerMap(pSelector);
		Status tStatus = target.get(pItem);
		if (tStatus == null) {
			tStatus = Status.NOT_REGISTERED;
		}
		return tStatus;
	}
	
	public boolean isTriggerable(String pSelector, Item pItem, boolean pArgument) {
		Status tStatus = getTriggerStatus(pSelector, pItem);
		return tStatus == Status.NON_TRIGGER ? false : pArgument || tStatus == Status.TRIGGER;
	}
	
	public boolean isTriggerable(String pSelector, ItemStack pStack, Class<?> itemClass) {
		if (pStack == null) {
			return false;
		}
		
		Item item = pStack.getItem();
		return isTriggerable(pSelector, item, itemClass.isAssignableFrom(item.getClass()));
	}
	
	public boolean isTriggerable(String pSelector, ItemStack pStack) {
		if (pStack == null) {
			return false;
		}
		
		return isTriggerable(pSelector, pStack.getItem(), false);
	}
	
	public void writeToNBT(NBTTagCompound pCompound) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		
		// Process Each Selctor
		for (String tSelector : getSelectorList()) {
			Map<Item, Status> targetMap = getTriggerMap(tSelector);
			// List of Trigger Item
			NBTTagList appendedList = new NBTTagList();
			
			// Process Each Item
			for (Entry<Item, Status> tEntry : targetMap.entrySet()) {
				String tItemValue = "";
				
				switch (tEntry.getValue()) {
				case TRIGGER:
					tItemValue = Item.REGISTRY.getNameForObject(tEntry.getKey()).toString();
					break;
				case NON_TRIGGER:
					tItemValue = "-".concat(Item.REGISTRY.getNameForObject(tEntry.getKey()).toString());
					break;
				default:
				}
				
				appendedList.appendTag(new NBTTagString(tItemValue));
			}
			
			tagCompound.setTag(tSelector, appendedList);
		}
		
		pCompound.setTag(LittleMaidReengaged.DOMAIN + ":MODETRIGGER", tagCompound);
	}
	
	public void readFromNBT(NBTTagCompound pCompound) {
		NBTTagCompound targetCompound = pCompound.getCompoundTag(LittleMaidReengaged.DOMAIN + ":MODETRIGGER");
		
		if (targetCompound.hasNoTags()) {
			return;
		}
		
		// Process Each Selector
		for (String pSelector : getSelectorList()) {
			NBTTagList targetList = targetCompound.getTagList(pSelector, 9);
			
			// Process Each Item
			for (int i = 0; i < targetList.tagCount(); i++) {
				NBTTagString tItemTag = (NBTTagString) targetList.get(i);
				String itemName = tItemTag.getString();
				
				// Trigger or Non-Trigger?
				Status triggerStatus = Status.NOT_REGISTERED;
				if (itemName.startsWith("-")) {
					triggerStatus = Status.NON_TRIGGER;
					itemName = itemName.substring(1);
				} else {
					triggerStatus = Status.TRIGGER;
				}
				
				// Set
				Item item = Item.REGISTRY.getObject(new ResourceLocation(itemName));
				setTriggerStatus(pSelector, item, triggerStatus);
			}
		}
	}
	
	@Override
	protected ModeTrigger clone() {
		ModeTrigger result = new ModeTrigger();
		
		for (String tSelector : getSelectorList()) {
			Map<Item, Status> tMap = defaultTrigger.getTriggerMap(tSelector);
			result.appendTriggerMap(tSelector, new HashMap<>(tMap));
		}
		
		return result;
	}

}
