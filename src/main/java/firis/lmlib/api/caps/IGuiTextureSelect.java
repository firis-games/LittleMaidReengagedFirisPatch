package firis.lmlib.api.caps;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * テクスチャ選択機能を使う場合に必要なインターフェース定義
 * 野生状態のGUI機能の利用は想定していない
 * @author firis-games
 *
 */
public interface IGuiTextureSelect {

	/**
	 * マルチモデルの名称系を取得する
	 */
	public String getTextureLittleMaid();
	public String getTextureArmor(EntityEquipmentSlot slot);
	
	/**
	 * 色情報を取得する
	 */
	public int getTextureColor();
	
	/**
	 * リトルメイドモデルを設定する
	 * クライアント側のみの通知のため同期処理は独自実装が必要
	 */
	public void syncTextureLittleMaid(String textureName, int color);
	
	
	/**
	 * アーマーモデルを設定する
	 * クライアント側のみの通知のため同期処理は独自実装が必要
	 */
	public void syncTextureArmor(String headTextureName, String chestTextureName, String legsTextureName, String feetTextureName);
	
}
