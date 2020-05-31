package firis.lmlib.api.resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import firis.lmmm.api.model.ModelMultiBase;

/**
 * マルチモデルパック管理用クラス
 * 
 * マルチモデル単位でパラメータを保持する
 * @author firis-games
 *
 */
public class MultiModelPack {

	/**
	 * マルチモデル名
	 */
	protected String multiModelName = "";
	
	/**
	 * メイドさんモデル
	 */
	protected ModelMultiBase modelLittleMaid = null;
	
	/**
	 * インナー防具モデル
	 */
	protected ModelMultiBase modelInnerArmor = null;
	
	/**
	 * アウター防具モデル
	 */
	protected ModelMultiBase modelOuterArmor = null;
	
	/**
	 * コンストラクタ
	 * @param multiModelName
	 * @param clazz
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public MultiModelPack(String multiModelName, Class<? extends ModelMultiBase> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		//名称指定
		this.multiModelName = multiModelName;
		
		Constructor<? extends ModelMultiBase> constructorMMBase = clazz.getConstructor(float.class);
		
		//メイドモデル初期化
		this.modelLittleMaid = constructorMMBase.newInstance(0.0F);
		
		//アーマーモデル初期化
		float[] lsize = this.modelLittleMaid.getArmorModelsSize();
		this.modelInnerArmor = constructorMMBase.newInstance(lsize[0]);
		this.modelOuterArmor = constructorMMBase.newInstance(lsize[1]);
		
	}
	
	
}
