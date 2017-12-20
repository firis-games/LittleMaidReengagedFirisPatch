package net.blacklab.lmr.entity.maidmodel;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.util.EnumArmorRenderParts;
import net.blacklab.lmr.util.EnumTextureType;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * テクスチャ管理用の変数群をまとめたもの。
 */
public class ModelConfigCompound  {
//public class MMM_TextureData implements MMM_ITextureEntity {

	public EntityLivingBase owner;
	public IModelCaps entityCaps;

	/**
	 * 使用されるテクスチャリソースのコンテナ
	 *
	 */
	public static class TextureCompound {
		private ResourceLocation[][] textures;

		public TextureCompound() {
			textures = new ResourceLocation[][] {
				/**
				 * 基本、発光
				 */
				{ null, null },
				/**
				 * アーマー内：頭、胴、腰、足
				 */
				{ null, null, null, null },
				/**
				 * アーマー外：頭、胴、腰、足
				 */
				{ null, null, null, null },
				/**
				 * アーマー内発光：頭、胴、腰、足
				 */
				{ null, null, null, null },
				/**
				 * アーマー外発光：頭、胴、腰、足
				 */
				{ null, null, null, null }
			};
		}

		public ResourceLocation getMainTexture(EnumTextureType type) {
			return textures[0][type.index];
		}

		public void setMainTexture(EnumTextureType type, ResourceLocation resourceLocation) {
			textures[0][type.index] = resourceLocation;
		}

		public ResourceLocation getArmorTexture(EnumTextureType type, EnumArmorRenderParts parts) {
			return textures[type.index*2 + parts.layerIndex][parts.textureIndex];
		}

		public void setArmorTexture(EnumTextureType type, EnumArmorRenderParts parts, ResourceLocation pLocation) {
			textures[type.index*2 + parts.layerIndex][parts.textureIndex] = pLocation;
		}
	}

	public ResourceLocation textures[][];
	/**
	 * 選択色
	 */
	public byte color;
	/**
	 * 契約テクスチャを選択するかどうか
	 */
	public boolean contract;

	public TextureBoxBase textureBox[];
	public ModelMultiBase textureModel[];

	/**
	 * 表示制御に使うフラグ群<br>
	 * int型32bitで保存。
	 */
	public int selectValue;


	public int data_Color	= 19;
	public int data_Texture	= 20;
	public int data_Value	= 21;


	public ModelConfigCompound(EntityLivingBase pEntity, IModelCaps pCaps) {
		owner = pEntity;
		entityCaps = pCaps;
		textures = new ResourceLocation[][] {
				/**
				 * 基本、発光
				 */
				{ null, null },
				/**
				 * アーマー内：頭、胴、腰、足
				 */
				{ null, null, null, null },
				/**
				 * アーマー外：頭、胴、腰、足
				 */
				{ null, null, null, null },
				/**
				 * アーマー内発光：頭、胴、腰、足
				 */
				{ null, null, null, null },
				/**
				 * アーマー外発光：頭、胴、腰、足
				 */
				{ null, null, null, null }
		};
		color = 12;
		contract = false;
		textureBox = new TextureBoxBase[2];
		textureBox[0] = textureBox[1] = ModelManager.instance.getDefaultTexture(owner.getClass());
		textureModel = new ModelMultiBase[3];
	}

	/**
	 * テクスチャリソースを現在値に合わせて設定する。
	 */
	public boolean setTextureNames() {
		textureModel[0] = null;
		textureModel[1] = null;
		textureModel[2] = null;

		if (owner.worldObj.isRemote) {
			return setTextureNamesClient();
		}
		return setTextureNamesServer();
	}

	/**
	 * テクスチャリソースを現在値に合わせて設定する。
	 */
	protected boolean setTextureNamesClient() {
		// Client
		boolean lf = false;
		TextureBox lbox;

		if (textureBox[0] instanceof TextureBox) {
			int lc = (color & 0x00ff) + (contract ? 0 : ModelManager.tx_wild);
			lbox = (TextureBox)textureBox[0];
			if (lbox.hasColor(lc)) {
				textures[0][0] = lbox.getTextureName(lc);
				lc = (color & 0x00ff) + (contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
				textures[0][1] = lbox.getTextureName(lc);
				lf = true;
				textureModel[0] = lbox.models[0];
			}
			// TODO ★ 暫定処置 クライアントに存在しないテクスチャが指定された場合、デフォルトを読み出す。
			else
			{
				lbox = ModelManager.instance.getDefaultTexture((IModelEntity)owner);
				textureBox[0] = textureBox[1] = lbox;

				if (lbox.hasColor(lc)) {
					textures[0][0] = lbox.getTextureName(lc);
					lc = (color & 0x00ff) + (contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
					textures[0][1] = lbox.getTextureName(lc);
					lf = true;
					textureModel[0] = lbox.models[0];
				}
				else
				{
					// もう諦める
				}
			}
		} else {
//			textureBox[0] = MMM_TextureManager.instance.getTextureBoxServerIndex(textureIndex[0]);
			throw new IllegalStateException("Texture setting error. Maybe ModelBoxServer is set?");
		}
		if (textureBox[1] instanceof TextureBox && owner != null) {
			lbox = (TextureBox)textureBox[1];
			for (int i = 0; i < 4; i++) {
				EntityEquipmentSlot lSlot = null;
				for (EntityEquipmentSlot pSlot: EntityEquipmentSlot.values()) {
					if (pSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && pSlot.getIndex() == i) {
						lSlot = pSlot;
					}
				}
				ItemStack is = owner.getItemStackFromSlot(lSlot);
				textures[1][i] = lbox.getArmorTextureName(ModelManager.tx_armor1, is);
				textures[2][i] = lbox.getArmorTextureName(ModelManager.tx_armor2, is);
				textures[3][i] = lbox.getArmorTextureName(ModelManager.tx_armor1light, is);
				textures[4][i] = lbox.getArmorTextureName(ModelManager.tx_armor2light, is);
			}
			textureModel[1] = lbox.models[1];
			textureModel[2] = lbox.models[2];
		} else {
//			textureBox[0] = MMM_TextureManager.instance.getTextureBoxServerIndex(textureIndex[0]);
			throw new IllegalStateException("Texture setting error. Maybe ModelBoxServer is set?");
		}
		return lf;
	}

	protected boolean setTextureNamesServer() {
		// Server
		boolean lf = false;
		TextureBoxServer lbox;
		if (textureBox[0] instanceof TextureBoxServer) {
			lbox = (TextureBoxServer)textureBox[0];
			if (lbox.localBox != null) {
				int lc = (color & 0x00ff) + (contract ? 0 : ModelManager.tx_wild);
				if (lbox.localBox.hasColor(lc)) {
					if (CommonHelper.isClient) {
						textures[0][0] = lbox.localBox.getTextureName(lc);
						lc = (color & 0x00ff) + (contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
						textures[0][1] = lbox.localBox.getTextureName(lc);
					}
					lf = true;
					textureModel[0] = lbox.localBox.models[0];
				}
			}
		}
		if (textureBox[1] instanceof TextureBoxServer && owner != null) {
			lbox = (TextureBoxServer)textureBox[1];
			if (lbox.localBox != null) {
				if (CommonHelper.isClient) {
					for (int i = 0; i < 4; i++) {
						for (EntityEquipmentSlot pSlot: EntityEquipmentSlot.values()) {
							if (pSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && pSlot.getIndex() == i) {
								ItemStack is = owner.getItemStackFromSlot(pSlot);
								textures[1][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor1, is);
								textures[2][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor2, is);
								textures[3][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor1light, is);
								textures[4][i] = lbox.localBox.getArmorTextureName(ModelManager.tx_armor2light, is);
								break;
							}
						}
					}
				}
				textureModel[1] = lbox.localBox.models[1];
				textureModel[2] = lbox.localBox.models[2];
			}
		}
		return lf;
	}

	public void setNextTexturePackege(int pTargetTexture) {
		if (pTargetTexture == 0) {
			int lc = getColor() + (isContract() ? 0 : ModelManager.tx_wild);
			// TODO ★ 暫定処置
			if(textureBox[0] instanceof TextureBox)
			{
				textureBox[0] = ModelManager.instance.getNextPackege((TextureBox)textureBox[0], lc);
			}
			else
			{
				textureBox[0] = null;
			}
			if (textureBox[0] == null) {
				// 指定色が無い場合は標準モデルに
				textureBox[0] = textureBox[1] = ModelManager.instance.getDefaultTexture((IModelEntity)owner);
				setColor((byte)0xc);
			} else {
				textureBox[1] = textureBox[0];
			}
			if (!((TextureBox)textureBox[1]).hasArmor()) {
				pTargetTexture = 1;
			}
		}
		if (pTargetTexture == 1) {
			textureBox[1] = ModelManager.instance.getNextArmorPackege((TextureBox)textureBox[1]);
		}
	}

	public void setPrevTexturePackege(int pTargetTexture) {
		if (pTargetTexture == 0) {
			int lc = getColor() + (isContract() ? 0 : ModelManager.tx_wild);
			textureBox[0] = ModelManager.instance.getPrevPackege((TextureBox)textureBox[0], lc);
			textureBox[1] = textureBox[0];
			if (!((TextureBox)textureBox[1]).hasArmor()) {
				pTargetTexture = 1;
			}
		}
		if (pTargetTexture == 1) {
			textureBox[1] = ModelManager.instance.getPrevArmorPackege((TextureBox)textureBox[1]);
		}
	}

	/**
	 * 毎時処理
	 */
	public void onUpdate() {

		// 不具合対応
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&start=160#p210319
		if(textureBox!=null && textureBox.length>0 && textureBox[0]!=null)
		{
			// モデルサイズのリアルタイム変更有り？
			if (textureBox[0].isUpdateSize) {
				setSize();
			}
		}
	}

	public void setSize() {

		if(textureBox!=null && textureBox.length>0 && textureBox[0]!=null)
		{
			// サイズの変更
	//		owner.setSize(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
			if(owner instanceof EntityLittleMaid)
			{
				((EntityLittleMaid)owner).setSize2(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
			}
			else if(owner instanceof EntityLittleMaidForTexSelect)
			{
				((EntityLittleMaidForTexSelect)owner).setSize(textureBox[0].getWidth(entityCaps), textureBox[0].getHeight(entityCaps));
			}

			if (owner instanceof EntityAgeable) {
				// EntityAgeableはこれをしないと大きさ変更しないようになってる、くそう。
				((EntityAgeable)owner).setScaleForAge(owner.isChild());
			}
		}
	}


//	@Override
	public void setTexturePackName(TextureBox[] pTextureBox) {
		// Client
		for (int li = 0; li < pTextureBox.length; li++) {
			textureBox[li] = pTextureBox[li];
		}
		setSize();
	}

//	@Override
	public boolean setColor(byte pColor) {
		boolean lf = (color != pColor);
		color = pColor;
		return lf;
	}

//	@Override
	public byte getColor() {
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
	public void setTextureBox(TextureBoxBase[] pTextureBox) {
		textureBox = pTextureBox;
	}

//	@Override
	public TextureBoxBase[] getTextureBox() {
		return textureBox;
	}

//	@Override
	public void setTextures(int pIndex, ResourceLocation[] pNames) {
		textures[pIndex] = pNames;
	}

//	@Override
	public ResourceLocation[] getTextures(int pIndex) {
		return textures[pIndex];
	}


	/**
	 * 野生の色をランダムで獲得する。
	 */
	public byte getWildColor() {
		return textureBox[0].getRandomWildColor(owner.getRNG());
	}

	/**
	 * テクスチャ名称からランダムで設定する。
	 * @param pName
	 */
	public void setTextureInitServer(String pName) {
		LittleMaidReengaged.Debug("request Init Texture: %s", pName);
		textureBox[0] = textureBox[1] = ModelManager.instance.getTextureBoxServer(pName);
//		setTextureNames();
		if (textureBox[0] == null) {
			throw new NullPointerException("TEXTURE BOX IS NULL");
		}
		color = textureBox[0].getRandomWildColor(owner.getRNG());
	}

	public void setTextureInitClient() {
		TextureBox lbox = ModelManager.instance.getDefaultTexture(owner.getClass());
		for (int li = 0; li < textureBox.length; li++) {
			textureBox[li] = lbox;
		}
		color = textureBox[0].getRandomWildColor(owner.getRNG());
	}

	public String getTextureName(int pIndex) {
		try{
			return textureBox[pIndex].textureName;
		}catch(Exception e){
			return "default";
		}
	}

	public ResourceLocation getGUITexture() {
		return ((TextureBox)textureBox[0]).getTextureName(ModelManager.tx_gui);
	}

	/**
	 *
	 * @param pIndex 0-31
	 * @return
	 */
	public boolean isValueFlag(int pIndex) {
		return ((selectValue >>> pIndex) & 0x01) == 1;
	}

	/**
	 *
	 * @param pIndex 0-31
	 * @param pFlag
	 */
	public void setValueFlag(int pIndex, boolean pFlag) {
		selectValue |= ((pFlag ? 1 : 0) << pIndex);
	}

}
