package net.blacklab.lmr.entity.maidmodel;

import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.caps.IModelCapsData;
import firis.lmlib.api.caps.IMultiModelEntity;
import firis.lmlib.api.caps.ModelConfigCompoundBase;
import firis.lmlib.api.manager.LMTextureBoxManager;
import firis.lmlib.api.resource.LMTextureBox;
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
		LMTextureBox nextTextureBox = LMLibraryAPI.instance().getTextureManager().getNextPackege(this.textureBoxLittleMaid, this.color);
		this.textureBoxLittleMaid = nextTextureBox;
		
		//防具テクスチャも設定
		if (this.textureBoxLittleMaid.hasArmor()) {
			this.setTextureBoxArmorAll(this.textureBoxLittleMaid);
		} else {
			this.setTextureBoxArmorAll(LMLibraryAPI.instance().getTextureManager().getDefaultLMTextureBox());
		}
	}
	
	/**
	 * 次の防具モデルを取得する
	 * @param pTargetTexture
	 */
	public void setNextTextureArmorPackege() {
		LMTextureBox nextTextureBox = LMLibraryAPI.instance().getTextureManager().getNextArmorPackege(this.getTextureBoxArmorAll());
		this.setTextureBoxArmorAll(nextTextureBox);
	}
	
	/**
	 * 前のリトルメイドモデルを探す
	 * @param pTargetTexture
	 */
	public void setPrevTexturePackege() {
		//次のテクスチャを探す
		LMTextureBox prevTextureBox = LMLibraryAPI.instance().getTextureManager().getPrevPackege(this.textureBoxLittleMaid, this.color);
		this.textureBoxLittleMaid = prevTextureBox;
		
		//防具テクスチャも設定
		if (this.textureBoxLittleMaid.hasArmor()) {
			this.setTextureBoxArmorAll(this.textureBoxLittleMaid);
		} else {
			this.setTextureBoxArmorAll(LMLibraryAPI.instance().getTextureManager().getDefaultLMTextureBox());
		}
	}
	
	/**
	 * 前の防具モデルを取得する
	 * @param pTargetTexture
	 */
	public void setPrevTextureArmorPackege() {
		LMTextureBox prevTextureBox = LMLibraryAPI.instance().getTextureManager().getPrevArmorPackege(this.getTextureBoxArmorAll());
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
		this.setTextureBoxLittleMaid(LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelMaid));
		this.setTextureBoxArmor(EntityEquipmentSlot.HEAD, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelArmorHead));
		this.setTextureBoxArmor(EntityEquipmentSlot.CHEST, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelArmorChest));
		this.setTextureBoxArmor(EntityEquipmentSlot.LEGS, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelArmorLegs));
		this.setTextureBoxArmor(EntityEquipmentSlot.FEET, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelArmorFeet));
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
		this.setTextureBoxLittleMaid(LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelMaid));
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
		this.setTextureBoxArmor(slot, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(modelArmor));
		return true;
	}
}
