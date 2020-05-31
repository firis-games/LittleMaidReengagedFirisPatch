package firis.lmlib.api.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.api.resource.MultiModelPack;
import firis.lmlib.api.resource.TexturePack;

/**
 * メイドさんのテクスチャモデルを管理するクラス
 * @author firis-games
 * 
 * TexturePackManager
 * MultiModelPackManager
 * 上の二つを結合してMinecraft上で使うための状態にする
 * このクラスからメイドモデルを取得する場合対象が無いときはデフォルトモデルを返す
 *
 */
public class LMTextureBoxManager {
	
	public static String defaultTextureModelName = "default_Orign";
	
	/**
	 * テクスチャモデルパック一覧
	 */
	protected Map<String, LMTextureBox> littleMaidTextureModelPackMap = new LinkedHashMap<>();
	
	/**
	 * 野生メイドさんを持つテクスチャモデル
	 */
	protected List<String> wildLittleMaidList = new ArrayList<>();
	
	/**
	 * 初期化する
	 */
	public void init() {
		
		//TexturePackをベースにメイドさんのテクスチャモデルを作成する
		for(String texturePackName : TexturePackManager.instance.texturePackMap.keySet()) {
			
			//TexturePack取得
			TexturePack texturePack = TexturePackManager.instance.texturePackMap.get(texturePackName);
			
			//MultiModelPackを取得
			MultiModelPack multiModelPack = MultiModelPackManager.instance.getMultiModelPackWithTexturePack(texturePack.getMultiModelName());
			
			//マルチモデルがない場合は処理しない
			if (multiModelPack == null) continue;
			
			//テクスチャモデルを生成する
			LMTextureBox textureModelPack = new LMTextureBox(texturePack, multiModelPack);
			String textureModelName = textureModelPack.getTextureModelName();
			this.littleMaidTextureModelPackMap.put(textureModelName, textureModelPack);
			
			//野生メイドさん設定
			if (textureModelPack.isWildLittleMaid()) {
				this.wildLittleMaidList.add(textureModelName);
			}
		}
		
	}
	
	/**
	 * テクスチャモデルの一覧を取得
	 * @return
	 */
	public Collection<LMTextureBox> getLMTextureBoxList() {
		return this.littleMaidTextureModelPackMap.values();
	}
	
	/**
	 * 指定のテクスチャモデルが存在するかチェックする
	 * @param textureName
	 * @return
	 */
	public boolean isLMTextureBox(String textureName) {
		if (this.littleMaidTextureModelPackMap.containsKey(textureName)) {
			return true;
		}
		return false;
	}

	/**
	 * 指定のテクスチャモデルを取得する
	 * 
	 * 存在しないテクスチャの場合はデフォルトモデルを返す
	 * @return
	 */
	public LMTextureBox getLMTextureBox(String textureName) {
		if (this.littleMaidTextureModelPackMap.containsKey(textureName)) {
			return this.littleMaidTextureModelPackMap.get(textureName);
		}
		return this.getDefaultLMTextureBox();
	}
	
	/**
	 * デフォルトのテクスチャモデルを取得する
	 * @return
	 */
	public LMTextureBox getDefaultLMTextureBox() {
		return this.littleMaidTextureModelPackMap.get(defaultTextureModelName);
	}
	
	/**
	 * 同一カラーの次のモデルを取得する
	 * 
	 * 存在しないテクスチャの場合はデフォルトモデルを返す
	 * @param nowBox
	 * @param color
	 * @return
	 */
	public LMTextureBox getNextPackege(LMTextureBox nowBow, int color) {
		
		// 次のテクスチャパッケージの名前を返す
		boolean f = false;
		LMTextureBox lreturn = null;
		
		//テクスチャモデルを探す
		for (LMTextureBox ltb : this.littleMaidTextureModelPackMap.values()) {
			if (ltb.hasColor(color) && ltb.hasLittleMaid()) {
				if (f) {
					return ltb;
				}
				if (lreturn == null) {
					lreturn = ltb;
				}
			}
			if (ltb == nowBow) {
				f = true;
			}
		}
		return lreturn == null ? this.getDefaultLMTextureBox() : lreturn;
	}

	/**
	 * 同一カラーの前のモデルを探す
	 * 
	 * 存在しないテクスチャの場合はデフォルトモデルを返す
	 * @param nowBox
	 * @param color
	 * @return
	 */
	public LMTextureBox getPrevPackege(LMTextureBox nowBox, int color) {
		
		// 前のテクスチャパッケージの名前を返す
		LMTextureBox lreturn = null;
		
		//テクスチャモデルを探す
		for (LMTextureBox ltb : this.littleMaidTextureModelPackMap.values()) {
			if (ltb == nowBox) {
				if (lreturn != null) {
					break;
				}
			}
			if (ltb.hasColor(color) && ltb.hasLittleMaid()) {
				lreturn = ltb;
			}
		}
		return lreturn == null ? this.getDefaultLMTextureBox() : lreturn;
	}
	
	/**
	 * 防具モデルの次のモデルを取得する
	 * 
	 * 存在しないテクスチャの場合はデフォルトモデルを返す
	 * @param nowBox
	 * @param color
	 * @return
	 */
	public LMTextureBox getNextArmorPackege(LMTextureBox nowBow) {
		
		// 次のテクスチャパッケージの名前を返す
		boolean f = false;
		LMTextureBox lreturn = null;
		
		//テクスチャモデルを探す
		for (LMTextureBox ltb : this.littleMaidTextureModelPackMap.values()) {
			if (ltb.hasArmor()) {
				if (f) {
					return ltb;
				}
				if (lreturn == null) {
					lreturn = ltb;
				}
				if (ltb == nowBow) {
					f = true;
				}
			}
		}
		return lreturn == null ? this.getDefaultLMTextureBox() : lreturn;
	}
	
	/**
	 * 防具モデルの前のモデルを取得する
	 * 
	 * 存在しないテクスチャの場合はデフォルトモデルを返す
	 * @param nowBox
	 * @return
	 */
	public LMTextureBox getPrevArmorPackege(LMTextureBox nowBox) {
		// 前のテクスチャパッケージの名前を返す
		LMTextureBox lreturn = null;
		
		//テクスチャモデルを探す
		for (LMTextureBox ltb : this.littleMaidTextureModelPackMap.values()) {
			if (ltb == nowBox) {
				if (lreturn != null) {
					break;
				}
			}
			if (ltb.hasArmor()) {
				lreturn = ltb;
			}
		}
		return lreturn == null ? this.getDefaultLMTextureBox() : lreturn;
	}
	
	/**
	 * スポーン用モデル名をランダムに取得する
	 */
	public LMTextureBox getRandomTexture(Random rand) {
		//野生メイドさんのテクスチャを取得
		String wild = wildLittleMaidList.get(rand.nextInt(wildLittleMaidList.size()));
		return this.getLMTextureBox(wild);
	}
}
