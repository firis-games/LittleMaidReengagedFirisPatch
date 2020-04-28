package net.blacklab.lmr.util.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blacklab.lmr.util.manager.pack.LittleMaidTextureModelPack;
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
public class LittleMaidTextureModelManager {
	
	public static LittleMaidTextureModelManager instance = new LittleMaidTextureModelManager();
	
	/**
	 * テクスチャモデルパック一覧
	 */
	protected Map<String, LittleMaidTextureModelPack> littleMaidTextureModelPackMap = new HashMap<>();
	
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
			LittleMaidTextureModelPack textureModelPack = new LittleMaidTextureModelPack(texturePack, multiModelPack);
			this.littleMaidTextureModelPackMap.put(textureModelPack.getTextureModelName(), textureModelPack);
			
			//野生メイドさん設定
			if (textureModelPack.isWildLittleMaid()) {
				this.wildLittleMaidList.add(textureModelPack.getTextureModelName());
			}
		}
		
	}

}
