package net.blacklab.lmr.entity.maidmodel;

import java.util.EnumMap;
import java.util.Map;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.util.IModelCapsData;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.EnumColor;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * テクスチャ管理用クラス
 */
public class ModelConfigCompound implements IModelConfigCompound {
	
	/**
	 * 対象のEntity
	 */
	protected EntityLivingBase owner;
	
	/**
	 * 描画用のModelCaps
	 */
	protected IModelCapsData entityCaps;

	/**
	 * 選択色
	 */
	protected int color;
	
	/**
	 * 契約テクスチャを選択するかどうか
	 */
	protected boolean contract;
	
	/**
	 * メイドさんのテクスチャモデル
	 */
	protected LMTextureBox textureBoxLittleMaid = null;

	/**
	 * 防具のテクスチャモデル
	 */
	protected Map<EntityEquipmentSlot, LMTextureBox> textureBoxArmor = new EnumMap<>(EntityEquipmentSlot.class);
	
	/**
	 * 初回ロード用フラグ
	 */
	private boolean isFirstRefresh = false;
	
	/**
	 * コンストラクタ
	 * @param pEntity
	 * @param pCaps
	 */
	public ModelConfigCompound(EntityLivingBase pEntity, IModelCapsData pCaps) {
		
		//パラメータ保存
		owner = pEntity;
		entityCaps = pCaps;
		
		//パラメータ初期化
		this.textureBoxLittleMaid = LMTextureBoxManager.instance.getDefaultLMTextureBox();
		
		//防具モデル初期化
		this.setTextureBoxArmorAll(this.textureBoxLittleMaid);
		
		this.color = EnumColor.BROWN.getColor();
		this.contract = false;
		
		//テクスチャ初回更新用フラグ
		this.isFirstRefresh = false;
	}
	
	/**
	 * 描画用のModelCapsを取得する
	 */
	@Override
	public IModelCapsData getModelCaps() {
		return this.entityCaps;
	}
	
	/**
	 * 色設定
	 * @param pColor
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * 色取得
	 * @return
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * 契約状態設定
	 * @param pContract
	 */
	public void setContract(boolean contract) {
		this.contract = contract;
	}

	/**
	 * 契約状態確認
	 * @return
	 */
	public boolean isContract() {
		return contract;
	}
	
	/**
	 * メイドさんのテクスチャ
	 * @return
	 */
	@Override
	public ResourceLocation getTextureLittleMaid() {
		if (this.textureBoxLittleMaid == null) return null;
		return this.textureBoxLittleMaid.getTextureLittleMaid(this.color);
	}
	
	/**
	 * メイドさんの発光テクスチャ
	 * @return
	 */
	@Override
	public ResourceLocation getLightTextureLittleMaid() {
		if (this.textureBoxLittleMaid == null) return null;
		return this.textureBoxLittleMaid.getLightTextureLittleMaid(this.color);
	}
	
	/**
	 * インナー防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureInnerArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getTextureInnerArmor(ItemStack.EMPTY);
	}
	
	/**
	 * インナー発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureInnerArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getLightTextureInnerArmor(ItemStack.EMPTY);
	}
	
	/**
	 * アウター防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureOuterArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getTextureOuterArmor(ItemStack.EMPTY);
	}
	
	/**
	 * アウター発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureOuterArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getLightTextureOuterArmor(ItemStack.EMPTY);
	}
	
	/**
	 * メイドモデルを取得する
	 */
	@Override
	public ModelMultiBase getModelLittleMaid() {
		if (textureBoxLittleMaid == null) return null;
		return textureBoxLittleMaid.getModelLittleMaid();
	}
	
	/**
	 * インナー防具モデルを取得する
	 * @param slot
	 * @return
	 */
	@Override
	public ModelMultiBase getModelInnerArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getModelInnerArmor();
	}
	
	/**
	 * アウター防具モデルを取得する
	 */
	@Override
	public ModelMultiBase getModelOuterArmor(EntityEquipmentSlot slot) {
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		if (armorBox == null) return null;
		return armorBox.getModelOuterArmor();
	}
	
	/**
	 * メイドさんテクスチャモデルを取得する
	 * @return
	 */
	public LMTextureBox getTextureBoxLittleMaid() {
		return this.textureBoxLittleMaid;
	}
	
	/**
	 * 防具テクスチャモデルを取得する
	 * @param slot
	 * @return
	 */
	public LMTextureBox getTextureBoxArmor(EntityEquipmentSlot slot) {
		if (!textureBoxArmor.containsKey(slot)) return null;
		return textureBoxArmor.get(slot);
	}
	
	/**
	 * メイドさんモデルを設定する
	 * @param textureBox
	 */
	public void setTextureBoxLittleMaid(LMTextureBox textureBox) {
		this.textureBoxLittleMaid = textureBox;
	}
	
	/**
	 * 防具モデルを設定する
	 * @param textureBox
	 * @param slot
	 */
	public void setTextureBoxArmor(EntityEquipmentSlot slot, LMTextureBox textureBox) {
		this.textureBoxArmor.put(slot, textureBox);
	}
	
	/**
	 * 防具モデル一括扱いで取得する
	 */
	public LMTextureBox getTextureBoxArmorAll() {
		
		LMTextureBox textureBox;
				
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.HEAD);
		if (textureBox != null) return textureBox;
		
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.CHEST);
		if (textureBox != null) return textureBox;
		
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.LEGS);
		if (textureBox != null) return textureBox;
		
		textureBox = this.getTextureBoxArmor(EntityEquipmentSlot.FEET);
		if (textureBox != null) return textureBox;
		
		return LMTextureBoxManager.instance.getDefaultLMTextureBox();
		
	}
	
	/**
	 * 防具モデルに一括で設定する
	 * @param textureBox
	 */
	public void setTextureBoxArmorAll(LMTextureBox textureBox) {
		this.textureBoxArmor.put(EntityEquipmentSlot.HEAD, textureBox);
		this.textureBoxArmor.put(EntityEquipmentSlot.CHEST, textureBox);
		this.textureBoxArmor.put(EntityEquipmentSlot.LEGS, textureBox);
		this.textureBoxArmor.put(EntityEquipmentSlot.FEET, textureBox);
	}
	
	/**
	 * 次のリトルメイドモデルを探す
	 * @param pTargetTexture
	 */
	public void setNextTexturePackege() {
		//次のテクスチャを探す
		LMTextureBox nextTextureBox = LMTextureBoxManager.instance.getNextPackege(this.textureBoxLittleMaid, this.color);
		this.textureBoxLittleMaid = nextTextureBox;
		
		//防具テクスチャも設定
		if (this.textureBoxLittleMaid.hasArmor()) {
			this.setTextureBoxArmorAll(this.textureBoxLittleMaid);
		} else {
			this.setTextureBoxArmorAll(LMTextureBoxManager.instance.getDefaultLMTextureBox());
		}
	}
	
	/**
	 * 次の防具モデルを取得する
	 * @param pTargetTexture
	 */
	public void setNextTextureArmorPackege() {
		LMTextureBox nextTextureBox = LMTextureBoxManager.instance.getNextArmorPackege(this.getTextureBoxArmorAll());
		this.setTextureBoxArmorAll(nextTextureBox);
	}
	
	/**
	 * 前のリトルメイドモデルを探す
	 * @param pTargetTexture
	 */
	public void setPrevTexturePackege() {
		//次のテクスチャを探す
		LMTextureBox prevTextureBox = LMTextureBoxManager.instance.getPrevPackege(this.textureBoxLittleMaid, this.color);
		this.textureBoxLittleMaid = prevTextureBox;
		
		//防具テクスチャも設定
		if (this.textureBoxLittleMaid.hasArmor()) {
			this.setTextureBoxArmorAll(this.textureBoxLittleMaid);
		} else {
			this.setTextureBoxArmorAll(LMTextureBoxManager.instance.getDefaultLMTextureBox());
		}
	}
	
	/**
	 * 前の防具モデルを取得する
	 * @param pTargetTexture
	 */
	public void setPrevTextureArmorPackege() {
		LMTextureBox prevTextureBox = LMTextureBoxManager.instance.getPrevArmorPackege(this.getTextureBoxArmorAll());
		this.setTextureBoxArmorAll(prevTextureBox);
	}
	
	/**
	 * 毎時処理
	 */
	public void onUpdate() {
		//リアルタイムサイズ変更処理
		if (this.textureBoxLittleMaid != null && this.textureBoxLittleMaid.getModelLittleMaid() != null) {
			if (this.textureBoxLittleMaid.getModelLittleMaid().isUpdateSize()) {
				this.setSizeMultiModel();
			}
		}
	}

	/**
	 * モデルサイズのリアルタイム変更処理
	 */
	protected void setSizeMultiModel() {
		if (this.textureBoxLittleMaid != null && this.textureBoxLittleMaid.getModelLittleMaid() != null) {
			if (owner instanceof IMultiModelEntity) {
				((IMultiModelEntity)owner).setSizeMultiModel(
						this.textureBoxLittleMaid.getModelLittleMaid().getWidth(entityCaps), 
						this.textureBoxLittleMaid.getModelLittleMaid().getHeight(entityCaps));
				
			}
			if (owner instanceof EntityAgeable) {
				//EntityAgeableはこれをしないと大きさ変更しないようになってる
				((EntityAgeable)owner).setScaleForAge(owner.isChild());
			}
		}
	}
	
	/**
	 * メイドさんのテクスチャパック名を取得する
	 * @return
	 */
	public String getTextureNameLittleMaid() {
		if (this.textureBoxLittleMaid == null) return LMTextureBoxManager.defaultTextureModelName;
		return this.textureBoxLittleMaid.getTextureModelName();
	}
	
	/**
	 * メイドさんのテクスチャパック名を取得する
	 * @return
	 */
	public String getTextureNameArmor() {
		if (this.getTextureBoxArmorAll() == null) return LMTextureBoxManager.defaultTextureModelName;
		return this.getTextureBoxArmorAll().getTextureModelName();
	}

	/**
	 * メイドさんのGUIテクスチャを取得する
	 * @return
	 */
	public ResourceLocation getGUITexture() {
		if (this.textureBoxLittleMaid == null) return null;
		return this.textureBoxLittleMaid.getTextureGuiBackground();
	}
	
	/**
	 * モデル情報をリフレッシュする
	 * 
	 * 設定値を元に
	 * 
	 * @return
	 */
	public boolean refreshModels(String modelMaid, 
			byte color,
			String modelArmorHead,
			String modelArmorChest,
			String modelArmorLegs,
			String modelArmorFeet,
			boolean isContract) {
		
		//初回リフレッシュ判定
		if (this.isFirstRefresh) {
			LMTextureBox maidBox = this.getTextureBoxLittleMaid();
			LMTextureBox armorBoxHead = this.getTextureBoxArmor(EntityEquipmentSlot.HEAD);
			LMTextureBox armorBoxChest = this.getTextureBoxArmor(EntityEquipmentSlot.CHEST);
			LMTextureBox armorBoxLegs = this.getTextureBoxArmor(EntityEquipmentSlot.LEGS);
			LMTextureBox armorBoxFeet = this.getTextureBoxArmor(EntityEquipmentSlot.FEET);
			
			//現在の状態が一致するか確認
			if (maidBox != null && armorBoxHead != null && armorBoxChest != null && armorBoxLegs != null && armorBoxFeet != null) {
				if (maidBox.getTextureModelName().equals(modelMaid) 
						&& armorBoxHead.getTextureModelName().equals(modelArmorHead)
						&& armorBoxChest.getTextureModelName().equals(modelArmorChest)
						&& armorBoxLegs.getTextureModelName().equals(modelArmorLegs)
						&& armorBoxFeet.getTextureModelName().equals(modelArmorFeet)
						&& color == this.getColor()
						&& isContract == this.isContract()) {
					return false;
				}
			}
		} else {
			//初回はチェックなしで更新する
			this.isFirstRefresh = true;
		}
		
		//再設定
		this.setTextureBoxLittleMaid(LMTextureBoxManager.instance.getLMTextureBox(modelMaid));
		this.setTextureBoxArmor(EntityEquipmentSlot.HEAD, LMTextureBoxManager.instance.getLMTextureBox(modelArmorHead));
		this.setTextureBoxArmor(EntityEquipmentSlot.CHEST, LMTextureBoxManager.instance.getLMTextureBox(modelArmorChest));
		this.setTextureBoxArmor(EntityEquipmentSlot.LEGS, LMTextureBoxManager.instance.getLMTextureBox(modelArmorLegs));
		this.setTextureBoxArmor(EntityEquipmentSlot.FEET, LMTextureBoxManager.instance.getLMTextureBox(modelArmorFeet));
		this.setColor(color);
		this.setContract(isContract);
		
		//メイドモデルのサイズを更新
		this.setSizeMultiModel();
		
		return true;
	}
	
	/**
	 * メイドテクスチャを設定する
	 * @param modelMaid
	 * @param color
	 * @param modelArmor
	 * @param isContract
	 * @return
	 */
	public boolean refreshModelsLittleMaid(String modelMaid, byte color) {
		//再設定
		this.setTextureBoxLittleMaid(LMTextureBoxManager.instance.getLMTextureBox(modelMaid));
		this.setColor(color);
		//メイドモデルのサイズを更新
		this.setSizeMultiModel();
		return true;
	}
	
	/**
	 * メイドテクスチャを設定する
	 * @param modelMaid
	 * @param color
	 * @param modelArmor
	 * @param isContract
	 * @return
	 */
	public boolean refreshModelsArmor(EntityEquipmentSlot slot, String modelArmor) {
		//再設定
		this.setTextureBoxArmor(slot, LMTextureBoxManager.instance.getLMTextureBox(modelArmor));
		return true;
	}
	
	/**
	 * 防具モデルの表示非表示制御用
	 * @return
	 */
	@Override
	public boolean isArmorTypeVisible(int type) {
		if (owner instanceof EntityLittleMaid) {
			EntityLittleMaid maid = (EntityLittleMaid) owner;
			return maid.isArmorVisible(type);
		}
		return true;
	}
	
	/**
	 * 対象Entityの透明判定判断
	 * @return
	 */
	public boolean isInvisible() {
		return this.owner.isInvisible();
	}
	
	/**
	 * 描画対象の輝度を取得する
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender() {
		return this.owner.getBrightnessForRender();
	}

}
