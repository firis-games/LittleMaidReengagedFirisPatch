package mmmlibx.lib;

import mmmlibx.lib.multiModel.model.mc162.IModelCaps;
import mmmlibx.lib.multiModel.model.mc162.ModelMultiBase;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.CommonHelper;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;



/**
 * テクスチャ管理用の変数群をまとめたもの。
 */
public class MMM_TextureData  {
//public class MMM_TextureData implements MMM_ITextureEntity {

	public EntityLivingBase owner;
	public IModelCaps entityCaps;
	
	/**
	 * 使用されるテクスチャリソースのコンテナ
	 */
	public ResourceLocation textures[][];
	/**
	 * 選択色
	 */
	public int color;
	/**
	 * 契約テクスチャを選択するかどうか
	 */
	public boolean contract;
	
	public MMM_TextureBoxBase textureBox[];
	public int textureIndex[];
	public ModelMultiBase textureModel[];
	
	/**
	 * 表示制御に使うフラグ群<br>
	 * int型32bitで保存。
	 */
	public int selectValue;


	public int data_Color	= 19;
	public int data_Texture	= 20;
	public int data_Value	= 21;


	public MMM_TextureData(EntityLivingBase pEntity, IModelCaps pCaps) {
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
		textureBox = new MMM_TextureBoxBase[2];
		textureBox[0] = textureBox[1] = MMM_TextureManager.instance.getDefaultTexture(owner.getClass());
		textureIndex = new int[] { 0, 0 };
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
		MMM_TextureBox lbox;
		
		if (textureBox[0] instanceof MMM_TextureBox) {
			int lc = (color & 0x00ff) + (contract ? 0 : MMM_TextureManager.tx_wild);
			lbox = (MMM_TextureBox)textureBox[0];
			if (lbox.hasColor(lc)) {
				textures[0][0] = lbox.getTextureName(lc);
				lc = (color & 0x00ff) + (contract ? MMM_TextureManager.tx_eyecontract : MMM_TextureManager.tx_eyewild);
				textures[0][1] = lbox.getTextureName(lc);
				lf = true;
				textureModel[0] = lbox.models[0];
			}
			// TODO ★ 暫定処置 クライアントに存在しないテクスチャが指定された場合、デフォルトを読み出す。
			else
			{
				lbox = MMM_TextureManager.instance.getDefaultTexture((ITextureEntity)owner);
				textureBox[0] = textureBox[1] = lbox;

				if (lbox.hasColor(lc)) {
					textures[0][0] = lbox.getTextureName(lc);
					lc = (color & 0x00ff) + (contract ? MMM_TextureManager.tx_eyecontract : MMM_TextureManager.tx_eyewild);
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
			textureBox[0] = MMM_TextureManager.instance.getTextureBoxServerIndex(textureIndex[0]);
		}
		if (textureBox[1] instanceof MMM_TextureBox && owner != null) {
			lbox = (MMM_TextureBox)textureBox[1];
			for (int i = 0; i < 4; i++) {
				ItemStack is = i==3?
						(owner instanceof EntityLittleMaid?
								((EntityLittleMaid)owner).getHeadMountStackCopy():null) :
						owner.getInventory()[i+1];
				textures[1][i] = lbox.getArmorTextureName(MMM_TextureManager.tx_armor1, is);
				textures[2][i] = lbox.getArmorTextureName(MMM_TextureManager.tx_armor2, is);
				textures[3][i] = lbox.getArmorTextureName(MMM_TextureManager.tx_armor1light, is);
				textures[4][i] = lbox.getArmorTextureName(MMM_TextureManager.tx_armor2light, is);
			}
			textureModel[1] = lbox.models[1];
			textureModel[2] = lbox.models[2];
		} else {
			textureBox[0] = MMM_TextureManager.instance.getTextureBoxServerIndex(textureIndex[0]);
		}
		return lf;
	}

	protected boolean setTextureNamesServer() {
		// Server
		boolean lf = false;
		MMM_TextureBoxServer lbox;
		if (textureBox[0] instanceof MMM_TextureBoxServer) {
			lbox = (MMM_TextureBoxServer)textureBox[0];
			if (lbox.localBox != null) {
				int lc = (color & 0x00ff) + (contract ? 0 : MMM_TextureManager.tx_wild);
				if (lbox.localBox.hasColor(lc)) {
					if (CommonHelper.isClient) {
						textures[0][0] = lbox.localBox.getTextureName(lc);
						lc = (color & 0x00ff) + (contract ? MMM_TextureManager.tx_eyecontract : MMM_TextureManager.tx_eyewild);
						textures[0][1] = lbox.localBox.getTextureName(lc);
					}
					lf = true;
					textureModel[0] = lbox.localBox.models[0];
				}
			}
		}
		if (textureBox[1] instanceof MMM_TextureBoxServer && owner != null) {
			lbox = (MMM_TextureBoxServer)textureBox[1];
			if (lbox.localBox != null) {
				if (CommonHelper.isClient) {
					for (int i = 0; i < 4; i++) {
						ItemStack is = owner.getEquipmentInSlot(i + 1);
						textures[1][i] = lbox.localBox.getArmorTextureName(MMM_TextureManager.tx_armor1, is);
						textures[2][i] = lbox.localBox.getArmorTextureName(MMM_TextureManager.tx_armor2, is);
						textures[3][i] = lbox.localBox.getArmorTextureName(MMM_TextureManager.tx_armor1light, is);
						textures[4][i] = lbox.localBox.getArmorTextureName(MMM_TextureManager.tx_armor2light, is);
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
			int lc = getColor() + (isContract() ? 0 : MMM_TextureManager.tx_wild);
			// TODO ★ 暫定処置
			if(textureBox[0] instanceof MMM_TextureBox)
			{
				textureBox[0] = MMM_TextureManager.instance.getNextPackege((MMM_TextureBox)textureBox[0], lc);
			}
			else
			{
				textureBox[0] = null;
			}
			if (textureBox[0] == null) {
				// 指定色が無い場合は標準モデルに
				textureBox[0] = textureBox[1] = MMM_TextureManager.instance.getDefaultTexture((ITextureEntity)owner);
				setColor(12);
			} else {
				textureBox[1] = textureBox[0];
			}
			if (!((MMM_TextureBox)textureBox[1]).hasArmor()) {
				pTargetTexture = 1;
			}
		}
		if (pTargetTexture == 1) {
			textureBox[1] = MMM_TextureManager.instance.getNextArmorPackege((MMM_TextureBox)textureBox[1]);
		}
	}

	public void setPrevTexturePackege(int pTargetTexture) {
		if (pTargetTexture == 0) {
			int lc = getColor() + (isContract() ? 0 : MMM_TextureManager.tx_wild);
			textureBox[0] = MMM_TextureManager.instance.getPrevPackege((MMM_TextureBox)textureBox[0], lc);
			textureBox[1] = textureBox[0];
			if (!((MMM_TextureBox)textureBox[1]).hasArmor()) {
				pTargetTexture = 1;
			}
		}
		if (pTargetTexture == 1) {
			textureBox[1] = MMM_TextureManager.instance.getPrevArmorPackege((MMM_TextureBox)textureBox[1]);
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

	protected void setSize() {
		
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
	public void setTexturePackIndex(int pColor, int[] pIndex) {
		// Server
		for (int li = 0; li < pIndex.length; li++) {
			textureIndex[li] = pIndex[li];
			textureBox[li] = MMM_TextureManager.instance.getTextureBoxServer(textureIndex[li]);
		}
		color = pColor;
		setSize();
	}

//	@Override
	public void setTexturePackName(MMM_TextureBox[] pTextureBox) {
		// Client
		for (int li = 0; li < pTextureBox.length; li++) {
			textureBox[li] = pTextureBox[li];
		}
		setSize();
	}

//	@Override
	public boolean setColor(int pColor) {
		boolean lf = (color != pColor);
		color = pColor;
		return lf;
	}

//	@Override
	public int getColor() {
		return color & 0x00ff;
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
	public void setTextureBox(MMM_TextureBoxBase[] pTextureBox) {
		textureBox = pTextureBox;
	}

//	@Override
	public MMM_TextureBoxBase[] getTextureBox() {
		return textureBox;
	}

//	@Override
	public void setTextureIndex(int[] pTextureIndex) {
		textureIndex = pTextureIndex;
	}

//	@Override
	public int[] getTextureIndex() {
		return textureIndex;
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
	public int getWildColor() {
		return textureBox[0].getRandomWildColor(owner.getRNG());
	}

	/**
	 * テクスチャ名称からランダムで設定する。
	 * @param pName
	 */
	public void setTextureInitServer(String pName) {
		MMMLib.Debug("request Init Texture: %s", pName);
		textureIndex[0] = textureIndex[1] =
				MMM_TextureManager.instance.getIndexTextureBoxServer((ITextureEntity)owner, pName);
		textureBox[0] = textureBox[1] = MMM_TextureManager.instance.getTextureBoxServer(textureIndex[0]);
		color = textureBox[0].getRandomWildColor(owner.getRNG());
	}
	public void setTextureInitClient() {
		MMM_TextureBox lbox = MMM_TextureManager.instance.getDefaultTexture(owner.getClass());
		for (int li = 0; li < textureBox.length; li++) {
			textureBox[li] = lbox;
			textureIndex[li] = MMM_TextureManager.instance.getIndexTextureBoxServerIndex(lbox);
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
		return ((MMM_TextureBox)textureBox[0]).getTextureName(MMM_TextureManager.tx_gui);
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

	/**
	 * 保有パラメーターの保存。<br>
	 * サーバー用。
	 * @param par1nbtTagCompound
	 */
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		NBTTagCompound lnbt = new NBTTagCompound();
		lnbt.setIntArray("Textures", textureIndex);
		lnbt.setInteger("Color", color);
		lnbt.setBoolean("Contract", contract);
		lnbt.setInteger("SelectValue", selectValue);
		par1nbtTagCompound.setTag("TextureData", lnbt);
	}

	/**
	 * 保有パラメーターの読出。<br>
	 * サーバー用。
	 * @param par1nbtTagCompound
	 */
	public void readToNBT(NBTTagCompound par1nbtTagCompound) {
		if (par1nbtTagCompound.hasKey("TextureData")) {
			NBTTagCompound lnbt = par1nbtTagCompound.getCompoundTag("TextureData");
			color = lnbt.getInteger("Color");
			contract = lnbt.getBoolean("Contract");
			selectValue = lnbt.getInteger("SelectValue");
			int[] intList = lnbt.getIntArray("Textures");
			
			if (intList.length > 0) {
				setTexturePackIndex(color, intList);
			} else {
				// ローカルに在るデフォルトのテクスチャを設定
				MMM_TextureBox lbox = MMM_TextureManager.instance.getDefaultTexture((ITextureEntity)owner);
				int li = MMM_TextureManager.instance.getIndexTextureBoxServerIndex(lbox);
				setTexturePackIndex(color, new int[] {li, li});
			}
		}
	}

	/**
	 * 
	 * @param pColor
	 * @param pTextureIndex
	 * @return
	 */
	public boolean updateTexture(int pColor, int[] pTextureIndex) {
		boolean lf = false;
		lf |= setColor(pColor);
		for (int li = 0; li < pTextureIndex.length; li++) {
			if (textureIndex[li] != pTextureIndex[li]) {
				textureIndex[li] = pTextureIndex[li];
				lf |= true;
			}
		}
		if (lf) {
			setTextureNames();
		}
		
		return lf;
	}

	// パッケージ化用
	/**
	 * 監視用のdataWatcherを設定する。
	 * @param pDataWatcher
	 */
	public void entityInit(DataWatcher pDataWatcher) {
		// Color
		pDataWatcher.addObject(data_Color, Byte.valueOf((byte)0));
		// 選択テクスチャインデックス
		pDataWatcher.addObject(data_Texture, Integer.valueOf(0));
		// モデルパーツの表示フラグ
		pDataWatcher.addObject(data_Value, Integer.valueOf(0));
	}

	protected void setWatchedColor(int pColor) {
		owner.getDataWatcher().updateObject(data_Color, (byte)pColor);
	}

	protected int getWatchedColor() {
		return owner.getDataWatcher().getWatchableObjectByte(data_Color);
	}
	
}
