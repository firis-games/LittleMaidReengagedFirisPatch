package net.blacklab.lmr.entity.maidmodel;

import firis.lmlibrary.lib.common.data.IModelCapsData;
import firis.lmlibrary.lib.manager.pack.LMTextureBox;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * メイドさん用のModelConfigCompound
 * @author firis-games
 *
 */
public class ModelConfigCompoundLittleMaid extends ModelConfigCompound {

	/**
	 * メイドさん
	 */
	private EntityLittleMaid maid;
	
	/**
	 * 初期化
	 * @param pEntity
	 * @param pCaps
	 */
	public ModelConfigCompoundLittleMaid(EntityLittleMaid pEntity, IModelCapsData pCaps) {
		super(pEntity, pCaps);
		this.maid = pEntity;
		
	}

	/**
	 * インナー防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureInnerArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = maid.maidInventory.armorItemInSlot(slot.getIndex());
		return armorBox.getTextureInnerArmor(stack);
		
	}
	
	/**
	 * インナー発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureInnerArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = maid.maidInventory.armorItemInSlot(slot.getIndex());
		return armorBox.getLightTextureInnerArmor(stack);
	}
	
	/**
	 * アウター防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureOuterArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = maid.maidInventory.armorItemInSlot(slot.getIndex());
		return armorBox.getTextureOuterArmor(stack);
	}
	
	/**
	 * アウター発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureOuterArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = maid.maidInventory.armorItemInSlot(slot.getIndex());
		return armorBox.getLightTextureOuterArmor(stack);
	}
	
	/**
	 * 防具モデルの表示非表示制御用
	 * @return
	 */
	@Override
	public boolean isArmorTypeVisible(int type) {
		return maid.isArmorVisible(type);
	}
}
