package firis.lmlib.client.entity;

import firis.lmlib.api.caps.IModelCompoundEntity;
import firis.lmlib.api.caps.ModelCapsEntityBase;
import firis.lmlib.api.caps.ModelCompoundEntityBase;
import firis.lmlib.api.entity.ILMModelEntity;
import firis.lmlib.api.resource.LMTextureBox;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;

/**
 * TextureSelect表示用リトルメイド
 * 
 * @author firis-games
 *
 */
public class EntityLittleMaidGui extends EntityLiving implements ILMModelEntity {

	/**
	 * マルチモデル描画用パラメータ
	 */
	protected ModelCompoundEntityBase<EntityLittleMaidGui> modelConfigCompound;

	/**
	 * コンストラクタ
	 * @param world
	 */
	public EntityLittleMaidGui(World world) {
		super(world);
		this.modelConfigCompound = new ModelCompoundEntityBase<EntityLittleMaidGui>(this, new ModelCapsEntityBase<EntityLittleMaidGui>(this) {}) {};
	}
	
	@Override
	public float getBrightness() {
		return 0.0F;
	}
	
	@Override
	public int getBrightnessForRender() {
		return 0x00f000f0;
	}
	
	/**
	 * @ILMModelEntity
	 */
	@Override
	public IModelCompoundEntity getModelCompoundEntity() {
		return this.modelConfigCompound;
	}
	
	/**
	 * リトルメイドテクスチャを設定する
	 */
	public void setTextureLittleMaid(LMTextureBox textureBox) {
		
		//メイドモデル設定
		this.modelConfigCompound.setTextureBoxLittleMaid(textureBox);
		
		//アーマーモデル非表示設定
		this.modelConfigCompound.setTextureBoxArmorAll(null);
	}
	
	/**
	 * リトルメイドの色・契約設定
	 * @param color
	 * @param contract
	 */
	public void setTextureLittleMaidColor(int color, boolean contract) {
		this.modelConfigCompound.setColor(color);
		this.modelConfigCompound.setContract(contract);		
	}
	
	/**
	 * アーマーテクスチャを設定する
	 */
	public void setTextureArmor(LMTextureBox head, LMTextureBox chest, LMTextureBox legs, LMTextureBox feet) {
		
		//メイドモデル非表示設定
		this.modelConfigCompound.setTextureBoxLittleMaid(null);
		
		//アーマーモデル非表示設定
		this.modelConfigCompound.setTextureBoxArmor(EntityEquipmentSlot.HEAD, head);
		this.modelConfigCompound.setTextureBoxArmor(EntityEquipmentSlot.CHEST, chest);
		this.modelConfigCompound.setTextureBoxArmor(EntityEquipmentSlot.LEGS, legs);
		this.modelConfigCompound.setTextureBoxArmor(EntityEquipmentSlot.FEET, feet);
		
	}
}
