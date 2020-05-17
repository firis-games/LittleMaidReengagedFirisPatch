package net.blacklab.lmc.common.villager;

import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/**
 * メイドさん斡旋村人
 * @author firis-games
 *
 */
public class VillagerCareerMaidBroker extends VillagerCareer {
	public VillagerCareerMaidBroker(VillagerProfession parent) {
		super(parent, "MaidBroker");
	}
}
