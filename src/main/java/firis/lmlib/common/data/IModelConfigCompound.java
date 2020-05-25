package firis.lmlib.common.data;

import firis.lmmm.api.model.ModelMultiBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Model系と連携するための定義
 * @author firis-games
 *
 */
public interface IModelConfigCompound {

	/**
	 * マルチモデル描画用パラメータ管理クラス
	 * @return
	 */
	public IModelCapsData getModelCaps();
	
	/**
	 * マルチモデルのテクスチャ系取得
	 * @return
	 */
	public ResourceLocation getTextureLittleMaid();
	public ResourceLocation getLightTextureLittleMaid();
	public ResourceLocation getTextureInnerArmor(EntityEquipmentSlot slot);
	public ResourceLocation getLightTextureInnerArmor(EntityEquipmentSlot slot);
	public ResourceLocation getTextureOuterArmor(EntityEquipmentSlot slot);
	public ResourceLocation getLightTextureOuterArmor(EntityEquipmentSlot slot);
	
	/**
	 * マルチモデルのモデル系取得
	 * @return
	 */
	public ModelMultiBase getModelLittleMaid();
	public ModelMultiBase getModelInnerArmor(EntityEquipmentSlot slot);
	public ModelMultiBase getModelOuterArmor(EntityEquipmentSlot slot);
	
	/**
	 * 防具描画制御用
	 * 0:インナー
	 * 1:発光インナー
	 * 2:アウター
	 * 3:発光アウター
	 */
	public boolean isArmorTypeVisible(int type);
	
	/**
	 * Entityが透明状態か判断
	 * @return
	 */
	public boolean isInvisible();
	
	/**
	 * 描画対象の輝度を取得する
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender();
}
