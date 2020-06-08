package firis.lmlib.api.caps;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * IModelCompoundとEntityを連携するためのインターフェース
 * @author firis-games
 *
 */
public interface IModelCompoundEntity extends IModelCompound {

	/**
	 * マルチモデルの名称系を取得する
	 */
	public String getTextureModelNameLittleMaid();
	public String getTextureModelNameArmor(EntityEquipmentSlot slot);
	
	/**
	 * 色情報を取得する
	 */
	public int getColor();
	
	/**
	 * 契約情報を取得する
	 */
	public boolean isContract();
	
}
