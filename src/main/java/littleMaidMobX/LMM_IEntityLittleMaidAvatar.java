package littleMaidMobX;

import net.minecraft.util.DamageSource;

public interface LMM_IEntityLittleMaidAvatar
{
	// --------------------------------------------------------------------------------------------------------
	// EntityPlayer へのアクセス
	// --------------------------------------------------------------------------------------------------------
	public float W_applyArmorCalculations(DamageSource par1DamageSource, float par2);
	public float W_applyPotionDamageCalculations(DamageSource par1DamageSource, float par2);
	public void W_damageArmor(float pDamage);
	public void W_damageEntity(DamageSource par1DamageSource, float par2);


	// --------------------------------------------------------------------------------------------------------
	// LMM 専用処理
	// --------------------------------------------------------------------------------------------------------
	public void setValueVector();

	public void getValueVectorFire(double atx, double aty, double atz, double atl);

	public boolean getIsItemTrigger();

	public boolean isUsingItemLittleMaid();

	public void getValue();

	public boolean getIsItemReload();
	
	public LMM_EntityLittleMaid getMaid();
}
