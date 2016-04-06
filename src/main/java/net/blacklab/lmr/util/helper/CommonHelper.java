package net.blacklab.lmr.util.helper;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

public class CommonHelper {

	public static final boolean isClient;
	public static final Minecraft mc;

	static {
		Minecraft lm = null;
		try {
			lm =  Minecraft.getMinecraft();// ModLoader.getMinecraftInstance();
		} catch (Exception e) {
//			e.printStackTrace();
		} catch (Error e) {
//			e.printStackTrace();
		}
		mc = lm;
		isClient = mc != null;
	}

	public static UUID getPlayerUUID(EntityPlayer par1EntityPlayer) {
		return par1EntityPlayer.getUniqueID();
	}

	/**
	 * 現在の実行環境がローカルかどうかを判定する。
	 */
	public static boolean isLocalPlay() {
		return isClient && mc.isIntegratedServerRunning();
	}

	/**
	 * アイテムに追加効果が在るかを判定する。
	 * Forge対策。
	 * @param pItemStack
	 * @return
	 */
	public static boolean hasEffect(ItemStack pItemStack) {
		// マジClientSIDEとか辞めてほしい。
		if (pItemStack != null) {
			Item litem = pItemStack.getItem();
			if (litem instanceof ItemPotion) {
				List llist = PotionUtils.getEffectsFromStack(pItemStack);
				return llist != null && !llist.isEmpty();
			}
		}
		return false;
	}

	/**
	 *  アイテムに設定された攻撃力を見る
	 * @param pItemStack
	 * @return
	 */
	public static double getAttackVSEntity(ItemStack pItemStack) {
		AttributeModifier lam = (AttributeModifier)pItemStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName());
		return lam == null ? 0 : lam.getAmount();
	}

	public static GameProfile newGameProfile(String UUIDid, String name) {
		return new GameProfile(UUID.randomUUID(), name);
	}

	public static void notifyAdmins(ICommandSender sender, ICommand cmd, int p_152374_2_, String s, Object ... p_152374_4_) {
		CommandBase.notifyOperators(sender, cmd, p_152374_2_, s, p_152374_4_);
	}

}
