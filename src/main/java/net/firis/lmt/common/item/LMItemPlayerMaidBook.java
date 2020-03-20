package net.firis.lmt.common.item;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class LMItemPlayerMaidBook extends Item {
	
	/**
	 * コンストラクタ
	 */
	public LMItemPlayerMaidBook() {
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.MISC);
	}
	/**
	 * 左クリックからのアイテム化
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		setDressUpPlayerFromMaid(player, entity);
		return true;
    }
	
	/**
	 * Shift＋右クリックからのアイテム化
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
		setDressUpPlayerFromMaid(playerIn, target);
		return true;
    }
	
	/**
	 * プレイヤーがメイドさんの見た目になる
	 */
	public void setDressUpPlayerFromMaid(EntityPlayer player, Entity entity) {
		
		if (!(entity instanceof EntityLittleMaid)) return;
		
		//対象のメイドさんからモデル情報を取得する
		EntityLittleMaid enityMaid = (EntityLittleMaid) entity;
		
		//メイドモデル名取得
		String maidModelName = enityMaid.getTextureBox()[0].textureName;
		String armorModelName = enityMaid.getTextureBox()[1].textureName;
		
		//メイドさんのテクスチャ
		String maidTexture = enityMaid.getTextures(0)[0].toString();
		
		//防具テクスチャ(頭防具から)
		String armorTexture0 = enityMaid.getTextures(1)[0] == null ? "" : enityMaid.getTextures(1)[0].toString();
		String armorTexture1 = enityMaid.getTextures(1)[1] == null ? "" : enityMaid.getTextures(1)[1].toString();
		String armorTexture2 = enityMaid.getTextures(1)[2] == null ? "" : enityMaid.getTextures(1)[2].toString();
		String armorTexture3 = enityMaid.getTextures(1)[3] == null ? "" : enityMaid.getTextures(1)[3].toString();
		
		NBTTagCompound nbt = player.getEntityData();
		
		nbt.setString("maidModel", maidModelName);
		nbt.setString("armorModel", armorModelName);
		nbt.setString("maidTexture", maidTexture);
		nbt.setString("armorTexture0", armorTexture0);
		nbt.setString("armorTexture1", armorTexture1);
		nbt.setString("armorTexture2", armorTexture2);
		nbt.setString("armorTexture3", armorTexture3);
		
	}

}
