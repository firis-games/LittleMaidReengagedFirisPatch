package net.firis.lmt.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.firis.lmt.config.FirisConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		
		if (!player.world.isRemote) return;
		
		if (!(entity instanceof EntityLittleMaid)) return;
		
		//対象のメイドさんからモデル情報を取得する
		EntityLittleMaid entityMaid = (EntityLittleMaid) entity;
		
		//メイドモデル名取得
		String maidModelName = entityMaid.getModelConfigCompound().getTextureNameLittleMaid();
		Integer maidModelColor = (int) entityMaid.getColor();
		String armorModelName = entityMaid.getModelConfigCompound().getTextureNameArmor();
		
		//メイドモデルの設定
		if (!player.isSneaking()) {
			
			//Config操作用
			Property propModel = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "01.MaidModel", FirisConfig.cfg_maid_model);
			Property propModelColor = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "02.MaidColorNo", FirisConfig.cfg_maid_color);

			//メイドモデルの指定
			propModel.set(maidModelName);
			propModelColor.set(maidModelColor);
			
		//アーマーモデルの設定
		} else {
			
			//Config操作用
			Property propModelArmorHelmet = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "03.ArmorHelmetModel", FirisConfig.cfg_armor_model_head);
			Property propModelArmorChest = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "04.ArmorChestplateModel", FirisConfig.cfg_armor_model_body);
			Property propModelArmorLegg = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "05.ArmorLeggingsModel", FirisConfig.cfg_armor_model_leg);
			Property propModelArmorBoots = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "06.ArmorBootsModel", FirisConfig.cfg_armor_model_boots);
			
			//スニーク中はアーマーモデルも設定
			if (player.getHeldItemOffhand().isEmpty()) {
				
				//全部のモデルを反映
				propModelArmorHelmet.set(armorModelName);
				propModelArmorChest.set(armorModelName);
				propModelArmorLegg.set(armorModelName);
				propModelArmorBoots.set(armorModelName);
				
			} else {
				ItemStack offHandStack = player.getHeldItemOffhand();
				//頭防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.HEAD, player)) {
					propModelArmorHelmet.set(armorModelName);
				}
				//胴防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.CHEST, player)) {
					propModelArmorChest.set(armorModelName);
				}
				//腰防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.LEGS, player)) {
					propModelArmorLegg.set(armorModelName);
				}
				//足防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.FEET, player)) {
					propModelArmorBoots.set(armorModelName);
				}
			}
		}
		
		//設定ファイル同期
		FirisConfig.syncConfig();
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.player_maid_book.info"));
    }
}
