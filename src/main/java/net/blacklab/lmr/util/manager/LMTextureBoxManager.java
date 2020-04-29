package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.blacklab.lmr.util.manager.pack.MultiModelPack;
import net.blacklab.lmr.util.manager.pack.TexturePack;

/**
 * メイドさんのテクスチャモデルを管理するクラス
 * @author firis-games
 * 
 * TexturePackManager
 * MultiModelPackManager
 * 上の二つを結合してMinecraft上で使うための状態にする
 *
 */
public class LMTextureBoxManager {
	
	public static LMTextureBoxManager instance = new LMTextureBoxManager();
	
	protected static String defaultTextureModelName = "default_Orign";
	
	/**
	 * テクスチャモデルパック一覧
	 */
	protected Map<String, LMTextureBox> littleMaidTextureModelPackMap = new HashMap<>();
	
	/**
	 * 野生メイドさんを持つテクスチャモデル
	 */
	protected List<String> wildLittleMaidList = new ArrayList<>();
	
	/**
	 * 初期化する
	 */
	public void init() {
		
		//マルチモデル初期化
		MultiModelPackManager.instance.init();
		
		//テクスチャパック初期化
		TexturePackManager.instance.init();
		
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
	 * 指定のテクスチャモデルを取得する
	 * @return
	 */
	public LMTextureBox getLMTextureBox(String textureName) {
		if (this.littleMaidTextureModelPackMap.containsKey(textureName)) {
			return this.littleMaidTextureModelPackMap.get(textureName);
		}
		return null;
	}
	
	/**
	 * メイドさんのデフォルトテクスチャモデルを取得する
	 * @param textureName
	 * @return
	 */
	public LMTextureBox getDefaultLMTextureBox() {
		return this.littleMaidTextureModelPackMap.get(defaultTextureModelName);
	}
	
}
