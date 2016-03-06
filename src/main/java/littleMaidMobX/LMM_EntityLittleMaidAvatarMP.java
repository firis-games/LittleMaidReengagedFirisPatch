package littleMaidMobX;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import wrapper.W_Common;



public class LMM_EntityLittleMaidAvatarMP extends FakePlayer implements LMM_IEntityLittleMaidAvatar
{
	public LMM_EntityLittleMaid avatar;
	/** いらん？ **/
	public boolean isItemTrigger;
	/** いらん？ **/
	public boolean isItemReload;
	/** いらん？ **/
	private boolean isItemPreReload;
	private double appendX;
	private double appendY;
	private double appendZ;

	public LMM_EntityLittleMaidAvatarMP(World par1World)
	{
		super(MinecraftServer.getServer().worldServerForDimension(par1World == null ? 0 : par1World.provider.getDimensionId()),
				W_Common.newGameProfile("1", "LMM_EntityLittleMaidAvatar"));
	}

	public LMM_EntityLittleMaidAvatarMP(World par1World, LMM_EntityLittleMaid par2EntityLittleMaid) {
		this(par1World);

		// 初期設定
		avatar = par2EntityLittleMaid;
		dataWatcher = avatar.getDataWatcher();

		this.dataWatcher.addObject(LMM_Statics.dataWatch_AbsorptionAmount, Float.valueOf(0.0F));

		inventory = avatar.maidInventory;
		inventory.player = this;
	}

	// 実績参照
	@Override
	public StatisticsFile getStatFile() {
		// ご主人様がいれば、ご主人様の実績を返す。
		if (this.avatar != null && this.avatar.getMaidMasterEntity() != null) {
			return MinecraftServer.getServer()
					.getConfigurationManager().getPlayerStatsFile(avatar.getMaidMasterEntity());
		}
		return super.getStatFile();
	}

	////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void entityInit()
	{
		super.entityInit();
	}
	public void readEntityFromNBT(NBTTagCompound var1)
	{
		super.readEntityFromNBT(var1);
	}
	public void writeEntityToNBT(NBTTagCompound var1)
	{
		super.writeEntityToNBT(var1);
	}
	public ItemStack getHeldItem()
	{
		return super.getHeldItem();
	}
	public void setCurrentItemOrArmor(int var1, ItemStack var2)
	{
		super.setCurrentItemOrArmor(var1, var2);
	}
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
	protected String getHurtSound() {
		return null;
	}

	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return false;
	}

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
		if (worldObj.isRemote) {
			// Client
			LMM_LittleMaidMobNX.proxy.onItemPickup(this, entity, i);
		} else {
			super.onItemPickup(entity, i);
		}
	}

	// TODO 現状無意味ですねわかります
	@Override
	public void onCriticalHit(Entity par1Entity) {
		if (worldObj.isRemote) {
			// Client
			LMM_LittleMaidMobNX.proxy.onCriticalHit(this, par1Entity);
		} else {
		}
	}

	@Override
	public void onEnchantmentCritical(Entity par1Entity) {
		if (worldObj.isRemote) {
			LMM_LittleMaidMobNX.proxy.onEnchantmentCritical(this, par1Entity);
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
			LMM_LittleMaidMobNX.Debug(String.format("ID:%d Given Damege:%f", avatar.getEntityId(), ll - ((EntityLivingBase)par1Entity).getHealth()));
		}
	}

	@Override
	public ItemStack getCurrentEquippedItem() {
		return avatar.getCurrentEquippedItem();
	}

	@Override
	public ItemStack getCurrentArmor(int par1) {
		//TODO GGG		return avatar.func_130225_q(par1);
		return avatar.getCurrentArmor(par1);
	}

	@Override
	public ItemStack getEquipmentInSlot(int par1) {
		return avatar.getEquipmentInSlot(par1);
	}

	/*
	@Override
	public ItemStack[] getLastActiveItems() {
		return avatar.getLastActiveItems();
	}
	*/
/*
	@Override
	protected void alertWolves(EntityLivingBase par1EntityLiving, boolean par2) {
		// ここを設定しちゃうと通常ではぬるぽ落ちする
	}
*/
	@Override
	public void destroyCurrentEquippedItem() {
		// アイテムが壊れたので次の装備を選択
		/* TODO:但し、Forge等でプレーヤーイベントを設定しているものだとぬるぽ落ちするので、何らかの対策が必要。
		 * ↑FakePlayerでスキップしてくれない？ */
//		super.destroyCurrentEquippedItem();
		inventory.setInventorySlotContents(inventory.currentItem, (ItemStack)null);
		avatar.getNextEquipItem();
	}

	@Override
	public void onKillEntity(EntityLivingBase entityliving) {
		avatar.onKillEntity(entityliving);
	}

	protected Entity getEntityServer() {
		return worldObj.isRemote ? null : this;
	}

	// Item使用関連

	public int getItemInUseDuration(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseDuration();
	}
//	@Deprecated
	@Override
	public int getItemInUseDuration() {
		return getItemInUseDuration(avatar.maidDominantArm);
	}

	public ItemStack getItemInUse(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUse();
	}
//	@Deprecated
	@Override
	public ItemStack getItemInUse() {
		return getItemInUse(avatar.maidDominantArm);
	}

	public int getItemInUseCount(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseCount();
	}
//	@Deprecated
	@Override
	public int getItemInUseCount() {
		return getItemInUseCount(avatar.maidDominantArm);
	}

	public boolean isUsingItem(int pIndex) {
		return avatar.getSwingStatus(pIndex).isUsingItem();
	}
//	@Deprecated
	@Override
	public boolean isUsingItem() {
		return isUsingItem(avatar.maidDominantArm);
	}
	public boolean isUsingItemLittleMaid() {
		return super.isUsingItem() | isItemTrigger;
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
	@Override
	public void clearItemInUse() {
//		super.clearItemInUse();
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		clearItemInUse(avatar.maidDominantArm);
	}

	public void stopUsingItem(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).stopUsingItem(getEntityServer());
	}
//	@Deprecated
	@Override
	public void stopUsingItem() {
//		super.stopUsingItem();
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		stopUsingItem(avatar.maidDominantArm);
	}

	public void setItemInUse(int pIndex, ItemStack itemstack, int i) {
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		avatar.getSwingStatus(pIndex).setItemInUse(itemstack, i, getEntityServer());
	}
//	@Deprecated
	@Override
	public void setItemInUse(ItemStack itemstack, int i) {
//		super.setItemInUse(itemstack, i);
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		setItemInUse(avatar.maidDominantArm, itemstack, i);
	}

	@Override
	public void setEating(boolean par1) {
		avatar.setEating(par1);
	}

	@Override
	public boolean isEating() {
		return avatar.isEating();
	}

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

	@Override
	public boolean isBlocking() {
		return avatar.isBlocking();
	}

	@Override
	public void playSound(String par1Str, float par2, float par3) {
		avatar.playSound(par1Str, par2, par3);
	}
	/*
	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}
	*/

//	@Override
//	public void sendChatToPlayer(ChatMessageComponent var1) {
//		// チャットメッセージは使わない。
//	}

	@Override
	public void addChatMessage(IChatComponent var1) {
		// チャットメッセージは使わない。
	}

	// 不要？

	/*
	@Override
	protected void setHideCape(int par1, boolean par2) {}

	@Override
	protected boolean getHideCape(int par1) {
		return false;
	}
	*/

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
	public BaseAttributeMap getAttributeMap() {
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
	public Collection getActivePotionEffects() {
		return avatar.getActivePotionEffects();
	}

	@Override
	public void clearActivePotions() {
		avatar.clearActivePotions();
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		avatar.onChangedPotionEffect(par1PotionEffect, par2);
	}

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
		//yOffset = avatar.yOffset;
		renderYawOffset = avatar.renderYawOffset;
		prevRenderYawOffset = avatar.prevRenderYawOffset;
		rotationYawHead = avatar.rotationYawHead;
		//attackTime = avatar.attackTime;
	}

	public void getValueVector(double atx, double aty, double atz, double atl) {
		// EntityLittleMaidから値をコピー
		double l = MathHelper.sqrt_double(atl);
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
		double l = MathHelper.sqrt_double(atl);
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
		//avatar.yOffset = yOffset;
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
		if (isSwingInProgress) avatar.setSwinging(LMM_EnumSound.Null, false);

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
		if (isSwingInProgress) avatar.setSwinging(LMM_EnumSound.Null, false);
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
	public boolean isEntityInvulnerable(DamageSource source){
		return false;
	}

	@Override
	public void sendEnterCombat() {
	}

	@Override
	public void sendEndCombat() {
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
	public LMM_EntityLittleMaid getMaid() {
		return avatar;
	}
}
