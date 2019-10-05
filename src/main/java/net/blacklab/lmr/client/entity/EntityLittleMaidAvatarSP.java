package net.blacklab.lmr.client.entity;

import java.util.Collection;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.IEntityLittleMaidAvatar;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;



public class EntityLittleMaidAvatarSP extends EntityPlayer implements IEntityLittleMaidAvatar
{
	public EntityLittleMaid avatar;
	/** いらん？ **/
	public boolean isItemTrigger;
	/** いらん？ **/
	public boolean isItemReload;
	/** いらん？ **/
	private boolean isItemPreReload;
	private double appendX;
	private double appendY;
	private double appendZ;

	// TODO クライアント用のあばたーはお飾りでよくね？もっと削れるかも

	public EntityLittleMaidAvatarSP(World par1World)
	{
		super(par1World, CommonHelper.newGameProfile("1", "LMM_EntityLittleMaidAvatar"));
	}

	public EntityLittleMaidAvatarSP(World par1World, EntityLittleMaid par2EntityLittleMaid) {
		super(par1World, CommonHelper.newGameProfile("1", "LMM_EntityLittleMaidAvatar"));

		// 初期設定
		avatar = par2EntityLittleMaid;
		dataManager = avatar.getDataManager();

//		this.dataManager.register(Statics.dataWatch_AbsorptionAmount, Float.valueOf(0.0F));

		// TODO Client限定ゴマカシ
		inventory = new InventoryPlayer(this);
		inventory.player = this;
	}

	////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void entityInit()
	{
		super.entityInit();
	}
	public Entity getCommandSenderEntity(){ return super.getCommandSenderEntity(); }
	public World getEntityWorld(){ return super.getEntityWorld(); }
	////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void applyEntityAttributes() {
		// 初期設定殺し
		// 初期設定値はダミーに設定される。
		super.applyEntityAttributes();
//		this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.13000000417232513D);
	}

	@Override
	public float getEyeHeight() {
		return avatar.getEyeHeight();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override public boolean canUseCommand(int i, String s){ return false; }

	@Override
	public void addStat(StatBase par1StatBase, int par2) {}

	@Override
	public void addScore(int par1) {}

	@Override
	public void onUpdate() {
//		posX = avatar.posX;
		EntityPlayer lep = avatar.getMaidMasterEntity();
		setEntityId(avatar.getEntityId());

		if (lep != null) {
			capabilities.isCreativeMode = lep.capabilities.isCreativeMode;
		}

		if (xpCooldown > 0) {
			xpCooldown--;
		}
		avatar.setExperienceValue(experienceTotal);
	}

	@Override
	public void onItemPickup(Entity entity, int i) {
		// アイテム回収のエフェクト
		if (getEntityWorld().isRemote) {
			// Client
			LittleMaidReengaged.proxy.onItemPickup(this, entity, i);
		} else {
			super.onItemPickup(entity, i);
		}
	}

	@Override
	public void onCriticalHit(Entity par1Entity) {
		if (getEntityWorld().isRemote) {
			// Client
			LittleMaidReengaged.proxy.onCriticalHit(this, par1Entity);
		} else {
		}
	}

	@Override
	public void onEnchantmentCritical(Entity par1Entity) {
		if (getEntityWorld().isRemote) {
			LittleMaidReengaged.proxy.onEnchantmentCritical(this, par1Entity);
		} else {
		}
	}

	@Override
	public void attackTargetEntityWithCurrentItem(Entity par1Entity) {
		float ll = 0;
		if (par1Entity instanceof EntityLivingBase) {
			ll = ((EntityLivingBase)par1Entity).getHealth();
		}
		super.attackTargetEntityWithCurrentItem(par1Entity);

		if (ll > 0) {
			LittleMaidReengaged.Debug(String.format("ID:%d Given Damege:%f", avatar.getEntityId(), ll - ((EntityLivingBase)par1Entity).getHealth()));
		}
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return avatar.getItemStackFromSlot(slotIn);
	}

/*
	@Override
	protected void alertWolves(EntityLivingBase par1EntityLiving, boolean par2) {
		// ここを設定しちゃうと通常ではぬるぽ落ちする
	}
*/

	@Override
	public void onKillEntity(EntityLivingBase entityliving) {
		avatar.onKillEntity(entityliving);
	}

	protected Entity getEntityServer() {
		return getEntityWorld().isRemote ? null : this;
	}

	// Item使用関連

	public int getItemInUseDuration(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseDuration();
	}
//	@Deprecated
	/*
	@Override
	public int getItemInUseDuration() {

		return getItemInUseDuration(avatar.getDominantArm());
	}
	*/

	public ItemStack getItemInUse(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUse();
	}
//	@Deprecated
//	@Override
	public ItemStack getItemInUse() {
		return getItemInUse(avatar.getDominantArm());
	}

	public int getItemInUseCount(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseCount();
	}
//	@Deprecated
	@Override
	public int getItemInUseCount() {
		return getItemInUseCount(avatar.getDominantArm());
	}

	public boolean isUsingItem(int pIndex) {
		return avatar.getSwingStatus(pIndex).isUsingItem();
	}
//	@Deprecated
//	@Override
	public boolean isUsingItem() {
		return isUsingItem(avatar.getDominantArm());
	}
	public boolean isUsingItemLittleMaid() {
		return isUsingItem() | isItemTrigger;
	}

	@Override
	public boolean getIsItemTrigger() {
		return isItemTrigger;
	}

	@Override
	public boolean getIsItemReload() {
		return isItemReload;
	}

	public void clearItemInUse(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).clearItemInUse(getEntityServer());
	}

//	@Deprecated
//	@Override
	public void clearItemInUse() {
//		super.clearItemInUse();
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		clearItemInUse(avatar.getDominantArm());
	}

	public void stopUsingItem(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).stopUsingItem(getEntityServer());
	}
//	@Deprecated
	@Override
	public void stopActiveHand() {
//		super.stopUsingItem();
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		stopUsingItem(avatar.getDominantArm());
	}

	public void setItemInUse(int pIndex, ItemStack itemstack, int i) {
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		avatar.getSwingStatus(pIndex).setItemInUse(itemstack, i, getEntityServer());
	}
//	@Deprecated
//	@Override
	public void setItemInUse(ItemStack itemstack, int i) {
//		super.setItemInUse(itemstack, i);
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		setItemInUse(avatar.getDominantArm(), itemstack, i);
	}

/*
	@Override
	public void setEating(boolean par1) {
		avatar.setEating(par1);
	}

	@Override
	public boolean isEating() {
		return avatar.isEating();
	}
*/

	@Override
	public void setAir(int par1) {
		avatar.setAir(par1);
	}

	@Override
	public int getAir() {
		return avatar.getAir();
	}

	@Override
	public void setFire(int par1) {
		avatar.setFire(par1);
	}

	@Override
	public boolean isBurning() {
		return avatar.isBurning();
	}

	@Override
	protected void setFlag(int par1, boolean par2) {
		avatar.setFlag(par1, par2);
	}

/*
	@Override
	public boolean isBlocking() {
		return avatar.isBlocking();
	}
*/

	@Override
	public void playSound(SoundEvent par1Str, float par2, float par3) {
		avatar.playSound(par1Str, par2, par3);
	}

	/*
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}
	*/

//	@Override
//	public void sendChatToPlayer(ChatMessageComponent var1) {
//		// チャットメッセージは使わない。
//	}

	@Override public void sendMessage(ITextComponent component) {}

	// 不要？

	protected void setHideCape(int par1, boolean par2) {}

	protected boolean getHideCape(int par1) {
		return false;
	}

	@Override
	public void setScore(int par1) {}

	@Override
	public int getScore() {
		return 0;
	}

	public void setAbsorptionAmount(float par1) {
		avatar.setAbsorptionAmount(par1);
	}

	public float getAbsorptionAmount() {
		return avatar.getAbsorptionAmount();
	}

	/**
	 * 属性値リストを取得
	 */
	public AbstractAttributeMap getAttributeMap() {
//		return super.func_110140_aT();
		return avatar == null ? super.getAttributeMap() : avatar.getAttributeMap();
	}

	@Override
	public void addPotionEffect(PotionEffect par1PotionEffect) {
		avatar.addPotionEffect(par1PotionEffect);
	}

	@Override
	public PotionEffect getActivePotionEffect(Potion par1Potion) {
		return avatar.getActivePotionEffect(par1Potion);
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return avatar.getActivePotionEffects();
	}

	@Override
	public void clearActivePotions() {
		avatar.clearActivePotions();
	}

	/*
	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		avatar.onChangedPotionEffect(par1PotionEffect, par2);
	}
	*/

	public void getValue() {
		// EntityLittleMaidから値をコピー
		setPosition(avatar.posX, avatar.posY, avatar.posZ);
		prevPosX = avatar.prevPosX;
		prevPosY = avatar.prevPosY;
		prevPosZ = avatar.prevPosZ;
		rotationPitch = avatar.rotationPitch;
		rotationYaw = avatar.rotationYaw;
		prevRotationPitch = avatar.prevRotationPitch;
		prevRotationYaw = avatar.prevRotationYaw;
		//yOffset = avatar.getYOffset();
		renderYawOffset = avatar.renderYawOffset;
		prevRenderYawOffset = avatar.prevRenderYawOffset;
		rotationYawHead = avatar.rotationYawHead;
		//attackTime = avatar.attackTime;
	}

	public void getValueVector(double atx, double aty, double atz, double atl) {
		// EntityLittleMaidから値をコピー
		double l = MathHelper.sqrt(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= avatar.rotationPitch;
		prevRotationPitch	= avatar.prevRotationPitch;
		rotationYaw			= avatar.rotationYaw;
		prevRotationYaw		= avatar.prevRotationYaw;
		renderYawOffset		= avatar.renderYawOffset;
		prevRenderYawOffset	= avatar.prevRenderYawOffset;
		rotationYawHead		= avatar.rotationYawHead;
		prevRotationYawHead	= avatar.prevRotationYawHead;
		//yOffset = avatar.yOffset;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	/**
	 * 射撃管制用、rotationを頭に合わせる
	 */
	public void getValueVectorFire(double atx, double aty, double atz, double atl) {
		// EntityLittleMaidから値をコピー
		double l = MathHelper.sqrt(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= updateDirection(avatar.rotationPitch);
		prevRotationPitch	= updateDirection(avatar.prevRotationPitch);
		rotationYaw			= updateDirection(avatar.rotationYawHead);
		prevRotationYaw		= updateDirection(avatar.prevRotationYawHead);
		renderYawOffset		= updateDirection(avatar.renderYawOffset);
		prevRenderYawOffset	= updateDirection(avatar.prevRenderYawOffset);
		rotationYawHead		= updateDirection(avatar.rotationYawHead);
		prevRotationYawHead	= updateDirection(avatar.prevRotationYawHead);
		//yOffset = avatar.yOffset;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	protected float updateDirection(float pValue) {
		pValue %= 360F;
		if (pValue < 0) pValue += 360F;
		return pValue;
	}


	public void setValue() {
		// EntityLittleMiadへ値をコピー
		avatar.setPosition(posX, posY, posZ);
		avatar.prevPosX = prevPosX;
		avatar.prevPosY = prevPosY;
		avatar.prevPosZ = prevPosZ;
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		//avatar.getYOffset();
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		//avatar.getSwingStatusDominant().attackTime = avatar.attackTime = attackTime;
	}

	public void setValueRotation() {
		// EntityLittleMiadへ値をコピー
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		if (isSwingInProgress) avatar.setSwinging(EnumSound.Null, false);

	}

	public void setValueVector() {
		// EntityLittleMiadへ値をコピー
		avatar.posX = posX - appendX;
		avatar.posY = posY - appendY;
		avatar.posZ = posZ - appendZ;
		avatar.prevPosX = prevPosX - appendX;
		avatar.prevPosY = prevPosY - appendY;
		avatar.prevPosZ = prevPosZ - appendZ;
		avatar.rotationPitch	 = rotationPitch;
		avatar.prevRotationPitch = prevRotationPitch;
//		avatar.rotationYaw			= rotationYaw;
//		avatar.prevRotationYaw		= prevRotationYaw;
//		avatar.renderYawOffset		= renderYawOffset;
//		avatar.prevRenderYawOffset	= prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		if (isSwingInProgress) avatar.setSwinging(EnumSound.Null, false);
	}

	public void W_damageArmor(float par1){
		super.damageArmor(par1);
	}

	public float applyArmorCalculations(DamageSource par1DamageSource, float par2)
	{
		return super.applyArmorCalculations(par1DamageSource, par2);
	}

	public float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2)
	{
		return super.applyPotionDamageCalculations(par1DamageSource, par2);
	}

	public void W_damageEntity(DamageSource par1DamageSource, float par2)
	{
		super.damageEntity(par1DamageSource, par2);
	}

	@Override
	public float W_applyArmorCalculations(DamageSource par1DamageSource, float par2) {
		return applyArmorCalculations(par1DamageSource, par2);
	}

	@Override
	public float W_applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
		return applyPotionDamageCalculations(par1DamageSource, par2);
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public EntityLittleMaid getMaid() {
		return avatar;
	}

	@Override
	public boolean isCreative() {
		// TODO Auto-generated method stub
		return false;
	}
}
