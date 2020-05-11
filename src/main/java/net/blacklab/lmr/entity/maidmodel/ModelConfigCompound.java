package net.blacklab.lmr.entity.maidmodel;

import net.blacklab.lmr.util.IModelCapsData;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

/**
 * テクスチャ管理用クラス
 */
public class ModelConfigCompound extends ModelConfigCompoundBase {
	
	/**
	 * 初回ロード用フラグ
	 */
	private boolean isFirstRefresh = false;
	
	/**
	 * コンストラクタ
	 * @param pEntity
	 * @param pCaps
	 */
	public ModelConfigCompound(EntityLivingBase entity, IModelCapsData caps) {
		
		super(entity, caps);
		
		//テクスチャ初回更新用フラグ
		this.isFirstRefresh = false;
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
}
