package net.blacklab.lmr.api.item;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** メイドに与えた時、または食べた時に特殊な効果を発揮するアイテムを追加するためのインターフェース。
 */
public interface IItemSpecialSugar {
	
	/** インベントリに入れておいた実装アイテムをメイドが食べた時に発揮する効果。
	 * @param maid 対象のメイドを示すインスタンス
	 * @param purpose 砂糖の用途
	 * @param stack メイドが食べるアイテムのItemStack。食べられる前のサイズなので注意
	 * @return falseを返すとデフォルトの砂糖によるハート0.5分回復をしなくなる。デフォルトでの被ダメージ後の回復もされなくなるので注意。
	 */
	public boolean onSugarEaten(EntityLittleMaid maid, EntityLittleMaid.EnumConsumeSugar purpose, ItemStack stack);
	
	/** 実装アイテムを直接与えた時の処理。
	 * @param world
	 * @param player 砂糖を与えたプレイヤー
	 * @param stack メイドが食べるアイテムのItemStack。与える前のサイズなので注意
	 * @param maid 砂糖を与えられたメイド
	 * @return falseを返すとモード切替をしなくなる。
	 */
	public boolean onSugarInteract(World world, EntityPlayer player, ItemStack stack, EntityLittleMaid maid);
	
}
