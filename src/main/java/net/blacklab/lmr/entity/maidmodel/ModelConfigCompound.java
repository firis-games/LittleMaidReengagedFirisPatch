package net.blacklab.lmr.entity.maidmodel;

import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.util.IModelCapsData;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.EnumColor;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * テクスチャ管理用の変数群をまとめたもの。
 */
public class ModelConfigCompound  {
//public class MMM_TextureData implements MMM_ITextureEntity {

	/**
	 * 使用されるテクスチャリソースのコンテナ
	 *
	 */
//	public static class TextureCompound {
//		private ResourceLocation[][] textures;
//
//		public TextureCompound() {
//			textures = new ResourceLocation[][] {
//				/**
//				 * 基本、発光
//				 */
//				{ null, null },
//				/**
//				 * アーマー内：頭、胴、腰、足
//				 */
//				{ null, null, null, null },
//				/**
//				 * アーマー外：頭、胴、腰、足
//				 */
//				{ null, null, null, null },
//				/**
//				 * アーマー内発光：頭、胴、腰、足
//				 */
//				{ null, null, null, null },
//				/**
//				 * アーマー外発光：頭、胴、腰、足
//				 */
//				{ null, null, null, null }
//			};
//		}
//
//		public ResourceLocation getMainTexture(EnumTextureType type) {
//			return textures[0][type.index];
//		}
//
//		public void setMainTexture(EnumTextureType type, ResourceLocation resourceLocation) {
//			textures[0][type.index] = resourceLocation;
//		}
//
//		public ResourceLocation getArmorTexture(EnumTextureType type, EnumArmorRenderParts parts) {
//			return textures[type.index*2 + parts.layerIndex][parts.textureIndex];
//		}
//
//		public void setArmorTexture(EnumTextureType type, EnumArmorRenderParts parts, ResourceLocation pLocation) {
//			textures[type.index*2 + parts.layerIndex][parts.textureIndex] = pLocation;
//		}
//	}

	protected EntityLivingBase owner;
	
	protected IModelCapsData entityCaps;
	public IModelCapsData getModelCaps() {
		return this.entityCaps;
	}
	
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
	protected LMTextureBox textureBoxArmor = null;
	
	/**
	 * 0:基本、発光
	 * 1:アーマー内：頭、胴、腰、足
	 * 2:アーマー外：頭、胴、腰、足
	 * 3:アーマー内発光：頭、胴、腰、足
	 * 4:アーマー外発光：頭、胴、腰、足
	 */
//	protected ResourceLocation textures[][];
//	public void setTexturesLittleMaid(ResourceLocation resource) {
//		textures[0][0] = resource;
//	}
//	public void setTexturesLightLittleMaid(ResourceLocation resource) {
//		textures[0][1] = resource;
//	}
//	public void setTexturesInnerArmor(ResourceLocation resource) {
//		//暫定設定（全部のtexturesを同じに設定する）
//		textures[1][0] = resource;
//		textures[1][1] = resource;
//		textures[1][2] = resource;
//		textures[1][3] = resource;
//	}
//	public void setTexturesOuterArmor(ResourceLocation resource) {
//		//暫定設定（全部のtexturesを同じに設定する）
//		textures[2][0] = resource;
//		textures[2][1] = resource;
//		textures[2][2] = resource;
//		textures[2][3] = resource;
//	}
//	public void setTexturesLightInnerArmor(ResourceLocation resource) {
//		//暫定設定（全部のtexturesを同じに設定する）
//		textures[3][0] = resource;
//		textures[3][1] = resource;
//		textures[3][2] = resource;
//		textures[3][3] = resource;
//	}
//	public void setTexturesLightOuterArmor(ResourceLocation resource) {
//		//暫定設定（全部のtexturesを同じに設定する）
//		textures[4][0] = resource;
//		textures[4][1] = resource;
//		textures[4][2] = resource;
//		textures[4][3] = resource;
//	}
	
	public ResourceLocation getTextureLittleMaid() {
		if (this.textureBoxLittleMaid == null) return null;
		return this.textureBoxLittleMaid.getTextureLittleMaid(this.color);
	}
	public ResourceLocation getLightTextureLittleMaid() {
		if (this.textureBoxLittleMaid == null) return null;
		return this.textureBoxLittleMaid.getLightTextureLittleMaid(this.color);
	}
	
	
	/**
	 * 0:メイドテクスチャ
	 * 1:アーマーテクスチャ
	 */
//	protected TextureBoxBase textureBox[];
	public LMTextureBox getTextureBoxLittleMaid() {
		return this.textureBoxLittleMaid;
	}
	public LMTextureBox getTextureBoxArmor() {
		return this.textureBoxArmor;
	}
	public void setTextureBoxLittleMaid(LMTextureBox textureBox) {
		this.textureBoxLittleMaid = textureBox;
	}
	public void setTextureBoxArmor(LMTextureBox textureBox) {
		this.textureBoxArmor = textureBox;
	}
	
	/**
	 * 0:メイドモデル
	 * 1:インナーアーマーモデル
	 * 2:アウターアーマーモデル
	 */
//	protected ModelMultiBase textureModel[];
	public ModelMultiBase getModelLittleMaid() {
		if (textureBoxLittleMaid == null) return null;
		return textureBoxLittleMaid.getModelLittleMaid();
	}
	public ModelMultiBase getModelInnerArmor() {
		if (textureBoxArmor == null) return null;
		return textureBoxArmor.getModelInnerArmor();
	}
	public ModelMultiBase getModelOuterArmor() {
		if (textureBoxArmor == null) return null;
		return textureBoxArmor.getModelOuterArmor();
	}
//	public void setModelLittleMaid(ModelMultiBase textureModel) {
//		this.textureModel[0] = textureModel;
//	}
//	public void setModelInnerArmor(ModelMultiBase textureModel) {
//		this.textureModel[1] = textureModel;
//	}
//	public void setModelOuterArmor(ModelMultiBase textureModel) {
//		this.textureModel[2] = textureModel;
//	}

	/**
	 * 表示制御に使うフラグ群<br>
	 * int型32bitで保存。
	 */
	public int selectValue;


//	public int data_Color	= 19;
//	public int data_Texture	= 20;
//	public int data_Value	= 21;


	public ModelConfigCompound(EntityLivingBase pEntity, IModelCapsData pCaps) {
		owner = pEntity;
		entityCaps = pCaps;
//		textures = new ResourceLocation[][] {
//				/**
//				 * 基本、発光
//				 */
//				{ null, null },
//				/**
//				 * アーマー内：頭、胴、腰、足
//				 */
//				{ null, null, null, null },
//				/**
//				 * アーマー外：頭、胴、腰、足
//				 */
//				{ null, null, null, null },
//				/**
//				 * アーマー内発光：頭、胴、腰、足
//				 */
//				{ null, null, null, null },
//				/**
//				 * アーマー外発光：頭、胴、腰、足
//				 */
//				{ null, null, null, null }
//		};
//		color = 12;
//		contract = false;
//		textureBox = new TextureBoxBase[2];
//		textureBox[0] = textureBox[1] = ModelManager.instance.getDefaultTexture(owner.getClass());
//		textureModel = new ModelMultiBase[3];
		
		//パラメータ初期化
		this.textureBoxLittleMaid = LMTextureBoxManager.instance.getDefaultLMTextureBox();
		this.textureBoxArmor = this.textureBoxLittleMaid;
		this.color = EnumColor.BROWN.getColor();
		this.contract = false;
		
//		//テクスチャ系更新
//		this.setTextureNames();
	}

	/**
	 * テクスチャリソースを現在値に合わせて設定する。
	 */
//	public boolean setTextureNames() {
//		textureModel[0] = null;
//		textureModel[1] = null;
//		textureModel[2] = null;
//
//		//if (owner.getEntityWorld().isRemote) {
//		//	return setTextureNamesClient();
//		//}
//		//return setTextureNamesServer();
//		return setTextureNamesClient();
//	}

//	/**
//	 * テクスチャリソースを現在値に合わせて設定する。
//	 */
//	protected boolean setTextureNamesClient() {
//		// Client
//		boolean lf = false;
//		TextureBox lbox;
//
//		if (textureBox[0] instanceof TextureBox) {
//			int lc = (color & 0x00ff) + (contract ? 0 : ModelManager.tx_wild);
//			lbox = (TextureBox)textureBox[0];
//			if (lbox.hasColor(lc)) {
//				textures[0][0] = lbox.getTextureName(lc);
//				lc = (color & 0x00ff) + (contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
//				textures[0][1] = lbox.getTextureName(lc);
//				lf = true;
//				textureModel[0] = lbox.models[0];
//			}
//			// TODO ★ 暫定処置 クライアントに存在しないテクスチャが指定された場合、デフォルトを読み出す。
//			else
//			{
//				lbox = ModelManager.instance.getDefaultTexture((IModelEntity)owner);
//				textureBox[0] = textureBox[1] = lbox;
//
//				if (lbox.hasColor(lc)) {
//					textures[0][0] = lbox.getTextureName(lc);
//					lc = (color & 0x00ff) + (contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
//					textures[0][1] = lbox.getTextureName(lc);
//					lf = true;
//					textureModel[0] = lbox.models[0];
//				}
//				else
//				{
//					// もう諦める
//				}
//			}
//		} else {
////			textureBox[0] = MMM_TextureManager.instance.getTextureBoxServerIndex(textureIndex[0]);
//			throw new IllegalStateException("Texture setting error. Maybe ModelBoxServer is set?");
//		}
//		if (textureBox[1] instanceof TextureBox && owner != null) {
//			lbox = (TextureBox)textureBox[1];
//			for (int i = 0; i < 4; i++) {
//				EntityEquipmentSlot lSlot = null;
//				for (EntityEquipmentSlot pSlot: EntityEquipmentSlot.values()) {
//					if (pSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && pSlot.getIndex() == i) {
//						lSlot = pSlot;
//					}
//				}
//				ItemStack is = owner.getItemStackFromSlot(lSlot);
//				textures[1][i] = lbox.getArmorTextureName(ModelManager.tx_armor1, is);
//				textures[2][i] = lbox.getArmorTextureName(ModelManager.tx_armor2, is);
//				textures[3][i] = lbox.getArmorTextureName(ModelManager.tx_armor1light, is);
//				textures[4][i] = lbox.getArmorTextureName(ModelManager.tx_armor2light, is);
//			}
//			textureModel[1] = lbox.models[1];
//			textureModel[2] = lbox.models[2];
//		} else {
//			textureBox[0] = MMM_TextureManager.instance.getTextureBoxServerIndex(textureIndex[0]);
//			throw new IllegalStateException("Texture setting error. Maybe ModelBoxServer is set?");
//		}
//		return lf;
//	}

//	@Deprecated
//	protected boolean setTextureNamesServer() {
//		// Server
//		boolean lf = false;
//		TextureBoxServer lbox;
//		if (textureBox[0] instanceof TextureBoxServer) {
//			lbox = (TextureBoxServer)textureBox[0];
//			if (lbox.localBox != null) {
//				int lc = (color & 0x00ff) + (contract ? 0 : ModelManager.tx_wild);
//				if (lbox.localBox.hasColor(lc)) {
//					if (CommonHelper.isClient) {
//						textures[0][0] = lbox.localBox.getTextureName(lc);
//						lc = (color & 0x00ff) + (contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
//						textures[0][1] = lbox.localBox.getTextureName(lc);
//					}
//					lf = true;
//					textureModel[0] = lbox.localBox.models[0];
//				}
//			}
//		}
//		if (textureBox[1] instanceof TextureBoxServer && owner != null) {
//			lbox = (TextureBoxServer)textureBox[1];
//			if (lbox.localBox != null) {
//				if (CommonHelper.isClient) {
//					for (int i = 0; i < 4; i++) {
//						for (EntityEquipmentSlot pSlot: EntityEquipmentSlot.values()) {
//							if (pSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && pSlot.getIndex() == i) {
//								ItemStack is = owner.getItemStackFromSlot(pSlot);
//								textures[1][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor1, is);
//								textures[2][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor2, is);
//								textures[3][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor1light, is);
//								textures[4][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor2light, is);
//								break;
//							}
//						}
//					}
//				}
//				textureModel[1] = lbox.localBox.models[1];
//				textureModel[2] = lbox.localBox.models[2];
//			}
//		}
//		return lf;
//	}

	/**
	 * 次のリトルメイドモデルを探す
	 * @param pTargetTexture
	 */
	public void setNextTexturePackege() {
		//次のテクスチャを探す
		LMTextureBox nextTextureBox = LMTextureBoxManager.instance.getNextPackege(this.textureBoxLittleMaid, this.color);
		if (nextTextureBox == null) {
			nextTextureBox = LMTextureBoxManager.instance.getDefaultLMTextureBox();
		}
		this.textureBoxLittleMaid = nextTextureBox;
	}
	
	/**
	 * 次の防具モデルを取得する
	 * @param pTargetTexture
	 */
	public void setNextTextureArmorPackege() {
		LMTextureBox nextTextureBox = LMTextureBoxManager.instance.getNextArmorPackege(this.textureBoxArmor);
		if (nextTextureBox == null) {
			nextTextureBox = LMTextureBoxManager.instance.getDefaultLMTextureBox();
		}
		this.textureBoxArmor = nextTextureBox;
	}
	
	/**
	 * 前のリトルメイドモデルを探す
	 * @param pTargetTexture
	 */
	public void setPrevTexturePackege() {
		//次のテクスチャを探す
		LMTextureBox prevTextureBox = LMTextureBoxManager.instance.getPrevPackege(this.textureBoxLittleMaid, this.color);
		if (prevTextureBox == null) {
			prevTextureBox = LMTextureBoxManager.instance.getDefaultLMTextureBox();
		}
		this.textureBoxLittleMaid = prevTextureBox;
	}
	
	/**
	 * 前の防具モデルを取得する
	 * @param pTargetTexture
	 */
	public void setPrevTextureArmorPackege() {
		LMTextureBox prevTextureBox = LMTextureBoxManager.instance.getPrevArmorPackege(this.textureBoxArmor);
		if (prevTextureBox == null) {
			prevTextureBox = LMTextureBoxManager.instance.getDefaultLMTextureBox();
		}
		this.textureBoxArmor = prevTextureBox;
	}
	
	/**
	 * 毎時処理
	 */
	public void onUpdate() {

		// 不具合対応
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=160#p210319
//		if(textureBox!=null && textureBox.length>0 && textureBox[0]!=null)
//		{
//			// モデルサイズのリアルタイム変更有り？
//			if (textureBox[0].isUpdateSize()) {
//				setSize();
//			}
//		}
		
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

//		if(textureBox!=null && textureBox.length>0 && textureBox[0]!=null)
//		{
//			// サイズの変更
//	//		owner.setSize(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
//			if(owner instanceof EntityLittleMaid)
//			{
//				((EntityLittleMaid)owner).setSize2(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
//			}
//			else if(owner instanceof EntityLittleMaidForTexSelect)
//			{
//				((EntityLittleMaidForTexSelect)owner).setSize(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
//			}
//
//			if (owner instanceof EntityAgeable) {
//				// EntityAgeableはこれをしないと大きさ変更しないようになってる、くそう。
//				((EntityAgeable)owner).setScaleForAge(owner.isChild());
//			}
//		}
		
		if (this.textureBoxLittleMaid != null && this.textureBoxLittleMaid.getModelLittleMaid() != null) {
			
			if (owner instanceof EntityLittleMaid) {
				((EntityLittleMaid)owner).setSize2(
						this.textureBoxLittleMaid.getModelLittleMaid().getWidth(entityCaps), 
						this.textureBoxLittleMaid.getModelLittleMaid().getHeight(entityCaps));
				
			} else if(owner instanceof EntityLittleMaidForTexSelect) {
				((EntityLittleMaidForTexSelect)owner).setSize(
						this.textureBoxLittleMaid.getModelLittleMaid().getWidth(entityCaps), 
						this.textureBoxLittleMaid.getModelLittleMaid().getHeight(entityCaps));
			}
			
			if (owner instanceof EntityAgeable) {
				//EntityAgeableはこれをしないと大きさ変更しないようになってる
				((EntityAgeable)owner).setScaleForAge(owner.isChild());
			}
		}
	}


////	@Override
//	public void setTexturePackName(TextureBox[] pTextureBox) {
//		// Client
//		for (int li = 0; li < pTextureBox.length; li++) {
//			textureBox[li] = pTextureBox[li];
//		}
//		setSizeMultiModel();
//	}

//	@Override
	public boolean setColor(byte pColor) {
		boolean lf = (color != pColor);
		color = pColor;
		return lf;
	}

//	@Override
	public int getColor() {
		return color;
	}

//	@Override
	public void setContract(boolean pContract) {
		contract = pContract;
	}

//	@Override
	public boolean isContract() {
		return contract;
	}

//	@Override
//	public void setTextureBox(TextureBoxBase[] pTextureBox) {
//		textureBox = pTextureBox;
//	}

//	@Override
//	public TextureBoxBase[] getTextureBox() {
//		return textureBox;
//	}

////	@Override
//	public void setTextures(int pIndex, ResourceLocation[] pNames) {
//		textures[pIndex] = pNames;
//	}

////	@Override
//	public ResourceLocation[] getTextures(int pIndex) {
//		return textures[pIndex];
//	}


//	/**
//	 * 野生の色をランダムで獲得する。
//	 */
//	public byte getWildColor() {
//		return textureBox[0].getRandomWildColor(owner.getRNG());
//	}

//	/**
//	 * テクスチャ名称からランダムで設定する。
//	 * @param pName
//	 */
//	public void setTextureInitServer(String pName) {
//		LittleMaidReengaged.Debug("request Init Texture: %s", pName);
//		//textureBox[0] = textureBox[1] = ModelManager.instance.getTextureBoxServer(pName);
//		textureBox[0] = textureBox[1] = ModelManager.instance.getTextureBox(pName);
////		setTextureNames();
//		if (textureBox[0] == null) {
//			throw new NullPointerException("TEXTURE BOX IS NULL");
//		}
//		color = textureBox[0].getRandomWildColor(owner.getRNG());
//	}

//	public void setTextureInitClient() {
//		TextureBox lbox = ModelManager.instance.getDefaultTexture(owner.getClass());
//		for (int li = 0; li < textureBox.length; li++) {
//			textureBox[li] = lbox;
//		}
//		color = textureBox[0].getRandomWildColor(owner.getRNG());
//	}

//	public String getTextureName(int pIndex) {
//		try{
//			return textureBox[pIndex].textureName;
//		}catch(Exception e){
//			return "default";
//		}
//	}
	
	/**
	 * メイドさんのテクスチャパック名を取得する
	 * @return
	 */
	public String getTextureNameLittleMaid() {
		if (this.textureBoxLittleMaid == null) return "default_Orign";
		return this.textureBoxLittleMaid.getTextureModelName();
	}
	
	/**
	 * メイドさんのテクスチャパック名を取得する
	 * @return
	 */
	public String getTextureNameArmor() {
		if (this.textureBoxArmor == null) return "default_Orign";
		return this.textureBoxArmor.getTextureModelName();
	}

	/**
	 * メイドさんのGUIテクスチャを取得する
	 * @return
	 */
	public ResourceLocation getGUITexture() {
		//return ((TextureBox)textureBox[0]).getTextureName(ModelManager.tx_gui);
		return this.textureBoxLittleMaid.getTextureGuiBackground();
	}

	/**
	 *
	 * @param pIndex 0-31
	 * @return
	 */
//	public boolean isValueFlag(int pIndex) {
//		return ((selectValue >>> pIndex) & 0x01) == 1;
//	}

	/**
	 *
	 * @param pIndex 0-31
	 * @param pFlag
	 */
//	public void setValueFlag(int pIndex, boolean pFlag) {
//		selectValue |= ((pFlag ? 1 : 0) << pIndex);
//	}
	
	/**
	 * モデル情報をリフレッシュする
	 * 
	 * 設定値を元に
	 * 
	 * @return
	 */
	public boolean refreshModels(String modelMaid, byte color, String modelArmor, boolean isContract) {
		
		LMTextureBox maidBox = this.getTextureBoxLittleMaid();
		LMTextureBox armorBox = this.getTextureBoxArmor();
		
		//現在の状態が一致するか確認
		if (maidBox != null && armorBox != null) {
			if (maidBox.getTextureModelName().equals(modelMaid) 
					&& armorBox.getTextureModelName().equals(modelArmor)
					&& color == this.getColor()
					&& isContract == this.isContract()) {
				return false;
			}
		}
		
		//再設定
//		this.setTextureBoxLittleMaid(ModelManager.instance.getTextureBox(modelMaid));
//		this.setTextureBoxArmor(ModelManager.instance.getTextureBox(modelArmor));
		this.setTextureBoxLittleMaid(LMTextureBoxManager.instance.getLMTextureBox(modelMaid));
		this.setTextureBoxArmor(LMTextureBoxManager.instance.getLMTextureBox(modelArmor));
		this.setColor(color);
		this.setContract(isContract);
		
//		//テクスチャ系を更新
//		this.setTextureNames();
		
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
//		//テクスチャ系を更新
//		this.setTextureNames();
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
	public boolean refreshModelsArmor(String modelArmor) {
		//再設定
		this.setTextureBoxArmor(LMTextureBoxManager.instance.getLMTextureBox(modelArmor));
//		//テクスチャ系を更新
//		this.setTextureNames();
		//メイドモデルのサイズを更新
		this.setSizeMultiModel();
		return true;
	}
	
	/**
	 * 防具モデルの表示非表示制御用
	 * @return
	 */
	public boolean isArmorVisible(int no) {
		
		if (owner instanceof EntityLittleMaid) {
			EntityLittleMaid maid = (EntityLittleMaid) owner;
			return maid.isArmorVisible(no);
		}
		
		return true;
		
		
	}

}
