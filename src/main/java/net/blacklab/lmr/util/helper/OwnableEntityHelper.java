package net.blacklab.lmr.util.helper;

import java.util.UUID;

import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

public class OwnableEntityHelper {

	public static UUID getOwner(IEntityOwnable entity) {
		UUID ownerUUID = entity.getOwnerId();
	
		// メイドがターゲットを探す際に、狼などのテイム可能なモブのオーナー名を取得してチェックする
		// この時オーナー名が NULL だと NULL.isEmpty() と呼び出してしまいクラッシュする。
		// ここにNULLチェックを入れてクラッシュを防ぐ
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&p=212078#p212038
		return ownerUUID!=null ? ownerUUID : EntityPlayer.getOfflineUUID("Player");
	}

	public static void setOwner(EntityTameable entity, UUID name) {
		entity.setOwnerId(name);
	}

}
