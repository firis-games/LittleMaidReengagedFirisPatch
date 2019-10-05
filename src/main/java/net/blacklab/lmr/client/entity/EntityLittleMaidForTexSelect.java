package net.blacklab.lmr.client.entity;

import java.util.Map;

import net.blacklab.lmr.entity.maidmodel.IModelCaps;
import net.blacklab.lmr.entity.maidmodel.IModelEntity;
import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.entity.maidmodel.TextureBoxBase;
import net.blacklab.lmr.util.EntityCapsLiving;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityLittleMaidForTexSelect extends EntityLiving implements IModelCaps, IModelEntity {

//	public int color;
//	public int textureIndex[] = new int[] { 0, 0 };
//	public MMM_TextureBoxBase textureBox[] = new MMM_TextureBoxBase[] {
//			new MMM_TextureBox(), new MMM_TextureBox() };
//	public boolean contract;
//	public ResourceLocation textures[][] = new ResourceLocation[][] {
//			{ null, null },
//			{ null, null , null , null },
//			{ null, null , null , null },
//			{ null, null , null , null },
//			{ null, null , null , null }
//	};
	protected EntityCapsLiving entityCaps;
	public ModelConfigCompound textureData;
	public boolean modeArmor = false;

	public EntityLittleMaidForTexSelect(World par1World) {
		super(par1World);
		entityCaps = new EntityCapsLiving(this);
		textureData = new ModelConfigCompound(this, entityCaps);
	}

	@Override
	protected void entityInit() {
		// TODO 作者が使う意志を見せなければ，俺はソースをコメントアウトし尽くすだけだぁ！
		super.entityInit();
		// color
//		dataWatcher.addObject(19, Integer.valueOf(0));
		// 20:選択テクスチャインデックス
//		dataWatcher.addObject(20, Integer.valueOf(0));
	}

/*
	@Override
	public int getMaxHealth() {
		return 20;
	}
*/
	@Override
	public float getBrightness() {
		return this.getEntityWorld() == null ? 0.0F : super.getBrightness();
	}

	// EntityCaps

	@Override
	public Map<String, Integer> getModelCaps() {
		return entityCaps.getModelCaps();
	}

	@Override
	public Object getCapsValue(int pIndex, Object... pArg) {
		return entityCaps.getCapsValue(pIndex, pArg);
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		return entityCaps.setCapsValue(pIndex, pArg);
	}

	// TextureEntity

	@Override
	public void setTexturePackName(TextureBox[] pTextureBox) {
		// Client
		textureData.setTexturePackName(pTextureBox);
//		textureBox[0] = pTextureBox[0];
//		textureBox[1] = pTextureBox[1];
//		setTextureNames();
//		// 身長変更用
//		setSize(textureBox[0].getWidth(null), textureBox[0].getHeight(null));
//		setPosition(posX, posY, posZ);
		// モデルの初期化
//		((MMM_TextureBox)textureBox[0]).models[0].setCapsValue(MMM_IModelCaps.caps_changeModel, this);
	}

	/**
	 * テクスチャのファイル名を獲得
	 */
	public void setTextureNames() {
		textureData.setTextureNames();
//		textures[0][0] = ((MMM_TextureBox)textureBox[0]).getTextureName(color + (contract ? 0 : MMM_TextureManager.tx_wild));
//		textures[0][1] = ((MMM_TextureBox)textureBox[0]).getTextureName(color + (contract ? MMM_TextureManager.tx_eyecontract : MMM_TextureManager.tx_eyewild));
//		textures[1][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, getCurrentItemOrArmor(1));
//		textures[1][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, getCurrentItemOrArmor(2));
//		textures[1][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, getCurrentItemOrArmor(3));
//		textures[1][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, getCurrentItemOrArmor(4));
//		textures[2][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, getCurrentItemOrArmor(1));
//		textures[2][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, getCurrentItemOrArmor(2));
//		textures[2][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, getCurrentItemOrArmor(3));
//		textures[2][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, getCurrentItemOrArmor(4));
//		textures[3][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, getCurrentItemOrArmor(1));
//		textures[3][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, getCurrentItemOrArmor(2));
//		textures[3][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, getCurrentItemOrArmor(3));
//		textures[3][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, getCurrentItemOrArmor(4));
//		textures[4][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, getCurrentItemOrArmor(1));
//		textures[4][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, getCurrentItemOrArmor(2));
//		textures[4][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, getCurrentItemOrArmor(3));
//		textures[4][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, getCurrentItemOrArmor(4));
	}

	public void setTextureNames(String pArmorName) {
		TextureBox lbox;
		textureData.textureModel[0] = null;
		textureData.textureModel[1] = null;
		textureData.textureModel[2] = null;
		
		if (textureData.textureBox[0] instanceof TextureBox) {
			int lc = (textureData.color & 0x00ff) + (textureData.contract ? 0 : ModelManager.tx_wild);
			lbox = (TextureBox)textureData.textureBox[0];
			if (lbox.hasColor(lc)) {
				textureData.textures[0][0] = lbox.getTextureName(lc);
				lc = (textureData.color & 0x00ff) + (textureData.contract ? ModelManager.tx_eyecontract : ModelManager.tx_eyewild);
				textureData.textures[0][1] = lbox.getTextureName(lc);
				textureData.textureModel[0] = lbox.models[0];
			}
		}
		if (textureData.textureBox[1] instanceof TextureBox) {
			lbox = (TextureBox)textureData.textureBox[1];
			for (int i = 0; i < 4; i++) {
				textureData.textures[1][i] = lbox.getArmorTextureName(ModelManager.tx_armor1, pArmorName, 0);
				textureData.textures[2][i] = lbox.getArmorTextureName(ModelManager.tx_armor2, pArmorName, 0);
				textureData.textures[3][i] = lbox.getArmorTextureName(ModelManager.tx_armor1light, pArmorName, 0);
				textureData.textures[4][i] = lbox.getArmorTextureName(ModelManager.tx_armor2light, pArmorName, 0);
			}
			textureData.textureModel[1] = lbox.models[1];
			textureData.textureModel[2] = lbox.models[2];
		}
		
//		textures[0][0] = ((MMM_TextureBox)textureBox[0]).getTextureName(color + (contract ? 0 : MMM_TextureManager.tx_wild));
//		textures[0][1] = ((MMM_TextureBox)textureBox[0]).getTextureName(color + (contract ? MMM_TextureManager.tx_eyecontract : MMM_TextureManager.tx_eyewild));
//		textures[1][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, pArmorName, 0);
//		textures[1][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, pArmorName, 0);
//		textures[1][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, pArmorName, 0);
//		textures[1][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1, pArmorName, 0);
//		textures[2][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, pArmorName, 0);
//		textures[2][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, pArmorName, 0);
//		textures[2][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, pArmorName, 0);
//		textures[2][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2, pArmorName, 0);
//		textures[3][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, pArmorName, 0);
//		textures[3][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, pArmorName, 0);
//		textures[3][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, pArmorName, 0);
//		textures[3][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor1light, pArmorName, 0);
//		textures[4][0] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, pArmorName, 0);
//		textures[4][1] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, pArmorName, 0);
//		textures[4][2] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, pArmorName, 0);
//		textures[4][3] = ((MMM_TextureBox)textureBox[1]).getArmorTextureName(MMM_TextureManager.tx_armor2light, pArmorName, 0);
	}

	@Override
	public void setColor(byte pColor) {
		textureData.setColor(pColor);
//		color = pColor;
	}

	@Override
	public byte getColor() {
		return textureData.getColor();
//		return color;
	}

	@Override
	public void setContract(boolean pContract) {
		textureData.setContract(pContract);
//		contract = pContract;
	}

	@Override
	public boolean isContract() {
		return textureData.isContract();
//		return contract;
	}

	@Override
	public void setTextureBox(TextureBoxBase[] pTextureBox) {
		textureData.setTextureBox(pTextureBox);
//		textureBox = pTextureBox;
	}

	@Override
	public TextureBoxBase[] getTextureBox() {
		return textureData.getTextureBox();
//		return textureBox;
	}

	@Override
	public void setTextures(int pIndex, ResourceLocation[] pNames) {
		textureData.setTextures(pIndex, pNames);
//		textures[pIndex] = pNames;
	}

	@Override
	public ResourceLocation[] getTextures(int pIndex) {
		return textureData.getTextures(pIndex);
//		return pIndex < textures.length ? textures[pIndex] : null;
	}

	@Override
	public ModelConfigCompound getModelConfigCompound() {
		return textureData;
	}

	@Override
	public int getBrightnessForRender() {
		// 一定の明るさを返す
		return 0x00f000f0;
	}

	@Override
	public void setSize(float par1, float par2)
	{
		super.setSize(par1, par2);
	}
}
