package net.blacklab.lmr.achievements;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;


/**
 * 実績の進捗置き換えクラス
 * @author computer
 *
 */
public class AchievementsLMRE {

	/**
	 * 進捗一覧
	 */
	public static enum AC {
		// 契約
		Contract("contract"),
		
		// 各モード
		Fencer("fencer"),
		Archer("archer"),
		Cook("cooking"),
		Farmer("farmer"),
		Healer("healer"),
		Pharmacist("pharmacist"),
		Ripper("ripper"),
		Torcher("torcher"),
		
		// モード拡張
		RandomKiller("bloodsucker"),
		Buster("buster"),
		BlazingStar("blazingstar"),
		
		// アクション
		MyFavorite("myfavorite"),
		Boost("boost"),
		Overprtct("overprtct"),
		Ashikubi("ashikubi"),
		;
		
		private AC(String value) {
			this.value = value;
		}
		private final String value;
		public String getValue() {
			return this.value;
		}
	}
	
	/**
	 * 進捗を有効化
	 * @param player
	 * @param achievment
	 */
	public static void grantAC(EntityPlayer player, AC achievment) {
		
		if (player == null || !(player instanceof EntityPlayerMP)) return;
		
		AdvancementManager manager = player.world.getMinecraftServer().getAdvancementManager();
		Advancement advancement = manager.getAdvancement(new ResourceLocation(LittleMaidReengaged.DOMAIN, achievment.getValue()));
		
		//有効な進捗の場合はトリガーキック
		if (advancement != null) {
			((EntityPlayerMP)player).getAdvancements().grantCriterion(advancement, "done");
		}
		
	}

}
