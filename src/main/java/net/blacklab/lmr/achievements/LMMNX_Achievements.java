package net.blacklab.lmr.achievements;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class LMMNX_Achievements {

	// 契約
	public static Achievement ac_Contract;
	
	// 各モード
	public static Achievement ac_Fencer;
	public static Achievement ac_Archer;
	public static Achievement ac_Cook;
	public static Achievement ac_Farmer;
	public static Achievement ac_Healer;
	public static Achievement ac_Pharmacist;
	public static Achievement ac_Shearer;
	public static Achievement ac_TorchLayer;
	
	// モード拡張
	public static Achievement ac_RandomKiller;
	public static Achievement ac_Buster;

	public static Achievement ac_BlazingStar;
	
	// アクション
	public static Achievement ac_MyFavorite;
	public static Achievement ac_Boost;
	public static Achievement ac_Overprtct;
	public static Achievement ac_Ashikubi;
	
	public static void initAchievements() {
		ac_Contract		= (Achievement) new Achievement("achievement.contract"		, "contract"	, 0, 0, Items.cake				, null			).initIndependentStat().registerStat();
		// 各モードの実績
		ac_Fencer		= (Achievement) new Achievement("achievement.fencer"		, "fencer"		,-5,-3, Items.diamond_sword		, ac_Contract	).initIndependentStat().registerStat();
		ac_RandomKiller	= (Achievement) new Achievement("achievement.bloodsucker"	, "bloodsucker"	,-4,-4, Items.diamond_axe		, ac_Fencer		).initIndependentStat().registerStat();
		ac_Archer		= (Achievement) new Achievement("achievement.archer"		, "archer"		,-3,-3, Items.bow				, ac_Contract	).initIndependentStat().registerStat();
		ac_BlazingStar	= (Achievement) new Achievement("achievement.blazingstar"	, "blazingstar"	,-2,-4, Items.flint_and_steel	, ac_Archer		).initIndependentStat().registerStat();
		ac_Cook			= (Achievement) new Achievement("achievement.cooking"		, "cooking"		,-1,-3, Items.coal				, ac_Contract	).initIndependentStat().registerStat();
		ac_Farmer		= (Achievement) new Achievement("achievement.farmer"		, "farmer"		, 1,-3, Items.diamond_hoe		, ac_Contract	).initIndependentStat().registerStat();
		ac_Healer		= (Achievement) new Achievement("achievement.healer"		, "healer"		, 2,-3, Items.bread				, ac_Contract	).initIndependentStat().registerStat();
		ac_Pharmacist	= (Achievement) new Achievement("achievement.pharmacist"	, "pharmacist"	, 3,-3, Items.nether_wart		, ac_Contract	).initIndependentStat().registerStat();
		ac_Shearer		= (Achievement) new Achievement("achievement.ripper"		, "ripper"		, 4,-3, Items.shears			, ac_Contract	).initIndependentStat().registerStat();
		ac_TorchLayer	= (Achievement) new Achievement("achievement.torcher"		, "torcher"		, 5,-3, Blocks.torch			, ac_Contract	).initIndependentStat().registerStat();
		
		// モード拡張実績
		ac_Buster		= (Achievement) new Achievement("achievement.zombuster"		, "zombuster"	, 2, 1, Items.iron_shovel		, ac_Contract	).initIndependentStat().registerStat();
		
		// アクション系実績
		ac_MyFavorite	= (Achievement) new Achievement("achievement.myfavorite"	, "myfavorite"	, 5, 3, Items.sugar				, ac_Contract	).setSpecial().initIndependentStat().registerStat();
		ac_Boost		= (Achievement) new Achievement("achievement.boost"			, "boost"		,-2,-2, Items.gunpowder			, ac_Contract	).initIndependentStat().registerStat();
		ac_Overprtct	= (Achievement) new Achievement("achievement.overprtct"		, "overprtct"	,-3,-1, Items.diamond_chestplate, ac_Contract	).initIndependentStat().registerStat();
		ac_Ashikubi		= (Achievement) new Achievement("achievement.ashikubi"		, "ashikubi"	,-4, 2, Items.chainmail_boots	, ac_Contract	).initIndependentStat().registerStat();
		
		Achievement[] achievements = new Achievement[] {
				ac_Contract,
				ac_Fencer,
				ac_RandomKiller,
				ac_Buster,
				ac_Archer,
				ac_BlazingStar,
				ac_Cook,
				ac_Farmer,
				ac_Healer,
				ac_Pharmacist,
				ac_Shearer,
				ac_TorchLayer,
				ac_Overprtct,
				ac_Ashikubi,
				ac_Boost,
				ac_MyFavorite
				};
		AchievementPage.registerAchievementPage(new AchievementPage("LittleMaidNX", achievements));
	}

}
