package firis.lmlib.api.resource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import firis.lmlib.api.constant.EnumColor;
import firis.lmlib.client.resources.LMTextureResourcePack;
import firis.lmmm.api.model.ModelMultiBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * メイドさんのモデルを管理する
 * 
 * 1つのモデルのテクスチャとマルチモデルをまとめて管理する
 * 
 * @author firis-games
 *
 */
public class LMTextureBox {
	
	/**
	 * テクスチャモデル名
	 */
	protected String textureModelName = "";
	
	/**
	 * テクスチャパック名
	 */
	protected String texturePackName = "";

	/**
	 * マルチモデルパック
	 */
	protected MultiModelPack multiModelPack;
	
	/**
	 * 通常メイドさんテクスチャ
	 */
	protected Map<EnumColor, ResourceLocation> textureLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * 野生メイドさんテクスチャ
	 */
	protected Map<EnumColor, ResourceLocation> textureWildLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * 通常メイドさんのデフォルト発光テクスチャ
	 */
	protected ResourceLocation textureDefaultLightLittleMaid = null;
	
	/**
	 * 野生メイドさんのデフォルト発光テクスチャ
	 */
	protected ResourceLocation textureDefaultLightWildLittleMaid = null;
	
	/**
	 * 通常メイドさんの発行テクスチャ
	 */
	protected Map<EnumColor, ResourceLocation> textureLightLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * 野生メイドさんの発行テクスチャ
	 */
	protected Map<EnumColor, ResourceLocation> textureLightWildLittleMaid = new EnumMap<>(EnumColor.class);
	
	/**
	 * メイドさんのGUIテクスチャ
	 */
	protected ResourceLocation textureGuiBackground = null;
	
	/**
	 * メイドさんのインナーアーマーテクスチャ
	 * 0-9
	 */
	protected Map<Integer, ResourceLocation> textureInnerArmor = new HashMap<>();

	/**
	 * メイドさんのアウターアーマーテクスチャ
	 * 0-9
	 */
	protected Map<Integer, ResourceLocation> textureOuterArmor = new HashMap<>();
	
	/**
	 * メイドさんの発光インナーアーマーテクスチャ
	 * 0-9
	 */
	protected Map<Integer, ResourceLocation> textureLightInnerArmor = new HashMap<>();

	/**
	 * メイドさんの発光アウターアーマーテクスチャ
	 * 0-9
	 */
	protected Map<Integer, ResourceLocation> textureLightOuterArmor = new HashMap<>();
	
	/**
	 * テクスチャモデルの初期化
	 */
	public LMTextureBox(TexturePack texturePack, MultiModelPack multiModelPack) {
		
		//テクスチャ名設定
		this.textureModelName = texturePack.getTexturePackName() + "_" + multiModelPack.multiModelName;
		
		//テクスチャパック名
		this.texturePackName = texturePack.getTexturePackName();
				
		//マルチモデル設定
		this.multiModelPack = multiModelPack;
		
		//テクスチャを設定する
		this.initTexturePack(texturePack);
	}
	
	/**
	 * テクスチャパックをMinecraftの使用可能な形式へ変換する
	 * @param texturePack
	 */
	private void initTexturePack(TexturePack texturePack) {
		
		//色毎のメイドさんのテクスチャ
		this.createTextureMapTypeColor(texturePack.textureLittleMaid, this.textureLittleMaid);
		
		//色毎の野良メイドさんテクスチャ
		this.createTextureMapTypeColor(texturePack.textureWildLittleMaid, this.textureWildLittleMaid);
		
		//色毎のメイドさんの発光テクスチャ
		this.createTextureMapTypeColor(texturePack.textureLightLittleMaid, this.textureLightLittleMaid);
		
		//色毎の野良メイドさんの発光テクスチャ
		this.createTextureMapTypeColor(texturePack.textureLightWildLittleMaid, this.textureLightWildLittleMaid);
		
		//メイドさんのインナーアーマーテクスチャ
		this.createTextureMapTypeInt(texturePack.textureInnerArmor, this.textureInnerArmor);
		
		//メイドさんのアウターアーマーテクスチャ
		this.createTextureMapTypeInt(texturePack.textureOuterArmor, this.textureOuterArmor);
		
		//メイドさんのインナーアーマー発光テクスチャ
		this.createTextureMapTypeInt(texturePack.textureLightInnerArmor, this.textureLightInnerArmor);
		
		//メイドさんのアウターアーマー発光テクスチャ
		this.createTextureMapTypeInt(texturePack.textureLightOuterArmor, this.textureLightOuterArmor);
		
		//通常メイドさんのデフォルト発光テクスチャ
		this.textureDefaultLightLittleMaid = convertResourceLocationPath(texturePack.textureDefaultLightLittleMaid);
		
		//野良メイドさんのデフォルト発光テクスチャ
		this.textureDefaultLightWildLittleMaid = convertResourceLocationPath(texturePack.textureDefaultLightWildLittleMaid);
		
		//メイドさんのGUIテクスチャ
		this.textureGuiBackground = convertResourceLocationPath(texturePack.textureGuiBackground);
		
	}
	
	/**
	 * TexturePackのEnumColorタイプのテクスチャを変換する
	 */
	private void createTextureMapTypeColor(Map<EnumColor, String> textureMap, Map<EnumColor, ResourceLocation> textureResourceMap) {
		for (EnumColor color : textureMap.keySet()) {
			ResourceLocation rl = convertResourceLocationPath(textureMap.get(color));
			if (rl != null) {
				textureResourceMap.put(color, rl);
			}
		}
	}
	
	/**
	 * TexturePackのIntegerタイプのテクスチャを変換する
	 */
	private void createTextureMapTypeInt(Map<Integer, String> textureMap, Map<Integer, ResourceLocation> textureResourceMap) {
		for (Integer idx : textureMap.keySet()) {
			ResourceLocation rl = convertResourceLocationPath(textureMap.get(idx));
			if (rl != null) {
				textureResourceMap.put(idx, rl);
			}
		}
	}
	
	/**
	 * テクスチャのパスをResourceLocationに変換する
	 * 
	 * @param path
	 * @return
	 */
	private static ResourceLocation convertResourceLocationPath(String path) {
		
		if ("".equals(path) || path == null) return null;
		
		//旧テクスチャパス対応
		//大文字小文字が含まれる場合はOldZipTexturesWrapperを利用する
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT &&
				((!path.equals(path.toLowerCase())))) {
			LMTextureResourcePack.addTexturePath(path);
		}
		
		return new ResourceLocation(path.replace("assets/minecraft/", ""));
	}
	
	/**
	 * テクスチャモデル名を返す
	 * @return
	 */
	public String getTextureModelName() {
		return this.textureModelName;
	}
	
	/**
	 * テクスチャパック名を返す
	 * @return
	 */
//	public String getTexturePackName() {
//		return this.texturePackName;
//	}

	/**
	 * メイドさんのデフォルトテクスチャを取得する
	 * @return
	 */
	public ResourceLocation getTextureLittleMaidDefault() {
		for (EnumColor color : this.textureLittleMaid.keySet()) {
			return this.textureLittleMaid.get(color);
		}
		return null;
	}
	
	/**
	 * メイドさんのテクスチャを取得する
	 * @return
	 */
	public ResourceLocation getTextureLittleMaid(int color) {
		EnumColor enumColor = EnumColor.getColorFromInt(color);
		if (this.textureLittleMaid.containsKey(enumColor)) {
			return this.textureLittleMaid.get(enumColor);
		}
		return null;
	}
	
	/**
	 * メイドさんの発光テクスチャを取得する
	 * @return
	 */
	public ResourceLocation getLightTextureLittleMaid(int color) {
		
		//個別テクスチャ確認
		EnumColor enumColor = EnumColor.getColorFromInt(color);
		if (this.textureLightLittleMaid.containsKey(enumColor)) {
			return this.textureLightLittleMaid.get(enumColor);
		}
		
		//デフォルト発光テクスチャ確認
		if (this.textureDefaultLightLittleMaid != null) {
			return textureDefaultLightLittleMaid;
		}
		
		//対象なし
		return null;
	}
	
	/**
	 * 野生メイドさんのテクスチャを取得する
	 * @return
	 */
	public ResourceLocation getTextureWildLittleMaid(int color) {
		EnumColor enumColor = EnumColor.getColorFromInt(color);
		if (this.textureWildLittleMaid.containsKey(enumColor)) {
			return this.textureWildLittleMaid.get(enumColor);
		}
		return null;
	}
	
	/**
	 * メイドさんの発光テクスチャを取得する
	 * @return
	 */
	public ResourceLocation getLightTextureWildLittleMaid(int color) {
		
		//個別テクスチャ確認
		EnumColor enumColor = EnumColor.getColorFromInt(color);
		if (this.textureLightWildLittleMaid.containsKey(enumColor)) {
			return this.textureLightWildLittleMaid.get(enumColor);
		}
		
		//デフォルト発光テクスチャ確認
		if (this.textureDefaultLightWildLittleMaid != null) {
			return textureDefaultLightWildLittleMaid;
		}
		//対象なし
		return null;
	}
	
	/**
	 * 防具系の破損率を計算してテクスチャを返却する
	 * 
	 * テクスチャのindexは下記の通り
	 * 0:耐久度が100%以下の時に表示
	 * 1:耐久度が90%以下の時に表示
	 * 2:耐久度が80%以下の時に表示
	 * 3:耐久度が70%以下の時に表示
	 * 4:耐久度が60%以下の時に表示
	 * 5:耐久度が50%以下の時に表示
	 * 6:耐久度が40%以下の時に表示
	 * 7:耐久度が30%以下の時に表示
	 * 8:耐久度が20%以下の時に表示
	 * 9:耐久度が10%以下の時に表示
	 * @return
	 */
	private ResourceLocation getTextureDamagedArmor(ItemStack stack, Map<Integer, ResourceLocation> armorTextureMap) {
		int damage = 0;
		//ダメージありのアイテム
		if (!stack.isEmpty() && stack.isItemStackDamageable()) {
			//ダメージレート
			int damageRate = (int) Math.ceil(((double) stack.getMaxDamage() - (double) stack.getItemDamage()) / (double) stack.getMaxDamage() * 10); 
			damageRate = 10 - damageRate;
			//0以下になるまでループ
			for (int i = damageRate; 0 < i; i--) {
				if (armorTextureMap.containsKey(i)) {
					damage = i;
					break;
				}
			}			
		}
		if (armorTextureMap.containsKey(damage)) {
			return armorTextureMap.get(damage);
		}
		return null;
	}
	
	/**
	 * メイドさんインナー防具のテクスチャを取得する
	 * @param color
	 * @return
	 */
	public ResourceLocation getTextureInnerArmor(ItemStack stack) {
		return getTextureDamagedArmor(stack, this.textureInnerArmor);
	}
	
	/**
	 * メイドさんインナー防具のテクスチャを取得する
	 * @param color
	 * @return
	 */
	public ResourceLocation getTextureOuterArmor(ItemStack stack) {
		return getTextureDamagedArmor(stack, this.textureOuterArmor);
	}
	
	/**
	 * メイドさんインナー防具のテクスチャを取得する
	 * @param color
	 * @return
	 */
	public ResourceLocation getLightTextureInnerArmor(ItemStack stack) {
		return getTextureDamagedArmor(stack, this.textureLightInnerArmor);
	}
	
	/**
	 * メイドさんインナー防具のテクスチャを取得する
	 * @param color
	 * @return
	 */
	public ResourceLocation getLightTextureOuterArmor(ItemStack stack) {
		return getTextureDamagedArmor(stack, this.textureLightOuterArmor);
	}
	
	/**
	 * メイドさんのGUIテクスチャを取得する
	 * @param color
	 * @return
	 */
	public ResourceLocation getTextureGuiBackground() {
		return this.textureGuiBackground;
	}
	
	/**
	 * 野生のメイドさんテクスチャが存在するか確認
	 * @return
	 */
	public boolean isWildLittleMaid() {
		return this.textureWildLittleMaid.size() > 0;
	}
	
	/**
	 * メイドさんのモデルを取得する
	 * @return
	 */
	public ModelMultiBase getModelLittleMaid() {
		return this.multiModelPack.modelLittleMaid;
	}
	
	/**
	 * メイドさんのインナーアーマーモデルを取得する
	 * @return
	 */
	public ModelMultiBase getModelInnerArmor() {
		return this.multiModelPack.modelInnerArmor;
	}
	
	/**
	 * メイドさんのアウターアーマーモデルを取得する
	 * @return
	 */
	public ModelMultiBase getModelOuterArmor() {
		return this.multiModelPack.modelOuterArmor;
	}
	
	/**
	 * 色が存在するかのチェック
	 * @param color
	 * @return
	 */
	public boolean hasColor(int color) {
		
		if (!EnumColor.hasColor(color)) return false;
		
		if (!this.textureLittleMaid.containsKey(EnumColor.getColorFromInt(color))) return false;
		
		return true;
	}
	
	/**
	 * 野生メイドさんの色が存在するかのチェック
	 * @param color
	 * @return
	 */
	public boolean hasWildColor(int color) {
		
		if (!EnumColor.hasColor(color)) return false;
		
		if (!this.textureWildLittleMaid.containsKey(EnumColor.getColorFromInt(color))) return false;
		
		return true;
	}
	
	/**
	 * 野生メイドさんの色をランダムで取得する
	 * @param rand
	 * @return
	 */
	public EnumColor getRandomWildColor(Random rand) {
		
		if (this.textureWildLittleMaid.size() == 0) return null;
		
		List<EnumColor> wildList = new ArrayList<>(textureWildLittleMaid.keySet());
		//野生メイドさん色
		EnumColor wildColor = wildList.get(rand.nextInt(wildList.size()));
		return wildColor;
	}
	
	/**
	 * アーマーが存在するかのチェック
	 * @return
	 */
	public boolean hasArmor() {
		return !(this.textureInnerArmor.size() == 0 && this.textureOuterArmor.size() == 0);
	}
	
	/**
	 * メイドさんのテクスチャが存在するかのチェック
	 * @return
	 */
	public boolean hasLittleMaid() {
		return this.textureLittleMaid.size() != 0;
	}
	
	/**
	 * 野生メイドさんのテクスチャが存在するかのチェック
	 * @return
	 */
	public boolean hasWildLittleMaid() {
		return this.textureWildLittleMaid.size() != 0;
	}
	
}
