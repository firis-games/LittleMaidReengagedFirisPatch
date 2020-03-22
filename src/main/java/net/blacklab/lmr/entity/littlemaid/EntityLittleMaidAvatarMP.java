package net.blacklab.lmr.entity.littlemaid;

import java.util.Collection;
import java.util.List;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.inventory.ContainerInventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;



public class EntityLittleMaidAvatarMP extends FakePlayer implements IEntityLittleMaidAvatar
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

	public EntityLittleMaidAvatarMP(World par1World)
	{
		super(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(par1World == null ? 0 : par1World.provider.getDimension()),
				CommonHelper.newGameProfile("1", "LMM_EntityLittleMaidAvatar"));
	}

	public EntityLittleMaidAvatarMP(World par1World, EntityLittleMaid par2EntityLittleMaid) {
		this(par1World);

		// 初期設定
		avatar = par2EntityLittleMaid;
		// TODO dataManager has been taken over.
		dataManager = avatar.getDataManager();

//		this.dataManager.register(Statics.dataWatch_AbsorptionAmount, Float.valueOf(0.0F));

		/*
		 * TODO 要調整
		 */
		inventory = avatar.maidInventory;
		inventory.player = this;
		inventoryContainer = new ContainerInventoryLittleMaid(inventory, avatar);
	}

	// 実績参照
	@Override
	public StatisticsManagerServer getStatFile() {
		// ご主人様がいれば、ご主人様の実績を返す。
		if (this.avatar != null && this.avatar.getMaidMasterEntity() != null) {
			// TODO Server only, so picking up from vanilla method. Is it correct?
			return ((EntityPlayerMP)avatar.getMaidMasterEntity()).getStatFile();
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

	@Override
	public ItemStack getHeldItem(EnumHand pHand)
	{
		return avatar.getHeldItem(pHand);
	}

	public World getEntityWorld(){ return super.getEntityWorld(); }
	////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		try {
			getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(20d);
		} catch (Exception exception){
			exception.printStackTrace();
		}
//		this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.13000000417232513D);
	}

	@Override
	public IAttributeInstance getEntityAttribute(IAttribute attribute) {
		return avatar==null? super.getEntityAttribute(attribute) : avatar.getEntityAttribute(attribute);
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
		super.onUpdate();

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

		// TODO EntityPlayerをOverrideしていないばっかりにこの値が変わっていなかった？
		ticksSinceLastSwing++;
		
		//Cooldownカウント実行
		this.getCooldownTracker().tick();
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

	// TODO 現状無意味ですねわかります
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
		this.remodeled_attackTargetEntityWithCurrentItem(par1Entity);

		if (ll > 0) {
			LittleMaidReengaged.Debug(String.format("ID:%d Given Damege:%f", avatar.getEntityId(), ll - ((EntityLivingBase)par1Entity).getHealth()));
		}
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return avatar.getArmorInventoryList();
	}

	@Override
	public Iterable<ItemStack> getHeldEquipment() {
		return avatar.getHeldEquipment();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return avatar.getItemStackFromSlot(slotIn);
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

//	@Override
	public void destroyCurrentEquippedItem() {
		// アイテムが壊れたので次の装備を選択
		// TODO Maybe will not be called
//		super.destroyCurrentEquippedItem();
		inventory.setInventorySlotContents(inventory.currentItem, ItemStack.EMPTY);
		avatar.getNextEquipItem();
	}

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

	public ItemStack getItemInUse(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUse();
	}

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

	@Override
	public boolean isHandActive() {
		return isUsingItem(avatar.getDominantArm());
	}

	@Override
	public EnumHand getActiveHand() {
		return EnumHand.MAIN_HAND;
	}

	public boolean isUsingItemLittleMaid() {
		return isHandActive() || getIsItemTrigger();
	}

	@Override
	public boolean getIsItemTrigger() {
		return isItemTrigger;
	}

	@Override
	public boolean getIsItemReload() {
		return isItemReload;
	}

	public void stopUsingItem(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).stopUsingItem(getEntityServer());
	}

	@Override
	public void stopActiveHand() {
		stopUsingItem(avatar.getDominantArm());
		avatar.stopActiveHand();
		super.stopActiveHand();
	}
	
	@Override
	public void resetActiveHand() {
		stopUsingItem(avatar.getDominantArm());
		avatar.resetActiveHand();
		super.resetActiveHand();
	}

	public void setItemInUse(int pIndex, ItemStack itemstack, int i) {
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		avatar.getSwingStatus(pIndex).setItemInUse(itemstack, i, getEntityServer());
	}

	@Override
	public void setActiveHand(EnumHand p_184598_1_) {
		isItemTrigger = true;
		avatar.setActiveHand(p_184598_1_);
		super.setActiveHand(p_184598_1_);
	}

//	@Override
	public void setItemInUse(ItemStack itemstack, int i) {
//		super.setItemInUse(itemstack, i);
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		setItemInUse(avatar.getDominantArm(), itemstack, i);
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
	public void playSound(SoundEvent par1Str, float par2, float par3) {
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

	@Override public void sendMessage(ITextComponent component) {}

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
	public AbstractAttributeMap getAttributeMap() {
//		return super.func_110140_aT();
		return avatar == null ? super.getAttributeMap() : avatar.getAttributeMap();
	}

	@Override
	public void addPotionEffect(PotionEffect par1PotionEffect) {
		if (avatar != null) avatar.addPotionEffect(par1PotionEffect);
	}

	@Override
	public PotionEffect getActivePotionEffect(Potion par1Potion) {
		return avatar == null ? super.getActivePotionEffect(par1Potion) 
				: avatar.getActivePotionEffect(par1Potion);
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return avatar == null ? super.getActivePotionEffects() 
				: avatar.getActivePotionEffects();
	}

	@Override
	public void clearActivePotions() {
		if (avatar != null) avatar.clearActivePotions();
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		if (avatar != null) avatar.onChangedPotionEffect(par1PotionEffect, par2);
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
	public EntityLittleMaid getMaid() {
		return avatar;
	}

	@Override
	public void sendContainerToPlayer(Container containerIn) {
	}

	//@Override
	//public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
	//}
	
	@Override
	protected CooldownTracker createCooldownTracker()
    {
        return new CooldownTracker();
    }
	
	/***********************************************************************************/
	/** EntityPlayerのdataManagerのIDとEntityLittleMaidのdataManagerのIDが混在しておかしくなる       */
	/** 特定の条件下で例えばLEFT_SHOULDER_ENTITYがLittleMaid側のfloatを取得してしまう                              */
	/** 極力dataManagerを利用しているところを潰す　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　  */
	/***********************************************************************************/
	@Override
	public NBTTagCompound getLeftShoulderEntity()
    {
		//LEFT_SHOULDER_ENTITY
        return new NBTTagCompound();
    }

	@Override
    protected void setLeftShoulderEntity(NBTTagCompound tag)
    {
    	//LEFT_SHOULDER_ENTITY
    }

	@Override
    public NBTTagCompound getRightShoulderEntity()
    {
    	//RIGHT_SHOULDER_ENTITY
		return new NBTTagCompound();
    }
	
	@Override
    protected void setRightShoulderEntity(NBTTagCompound tag)
    {
    	//RIGHT_SHOULDER_ENTITY
    }
	
	//Score系は対応済み
	
	//メイドさんのハンドに差し替え
	@Override
    public EnumHandSide getPrimaryHand()
    {
    	//MAIN_HAND
        return avatar.getPrimaryHand();
    }

	@Override
    public void setPrimaryHand(EnumHandSide hand)
    {
    	//MAIN_HAND
    }
	/***********************************************************************************/
	
	/**
	 * 別ModからsetPositionAndUpdateを呼ばれた場合にエラーが発生する
	 * this.connectionを利用しないようにメソッドを変更
	 */
	@Override
	public void setPositionAndUpdate(double x, double y, double z)
	{
		this.setPosition(x, y, z);
	}
	
	
	/**
	 * 別Modから特殊なレシピ操作されるとパケットが送信されるため落ちる
	 * （アイテムを拾った際にレシピ追加など）
	 * レシピ関連処理を握りつぶす
	 */
	@Override
	public void unlockRecipes(List<IRecipe> p_192021_1_){}
	
	@Override
	public void unlockRecipes(ResourceLocation[] p_193102_1_) {}
	
	@Override
	public void resetRecipes(List<IRecipe> p_192022_1_) {}
	
	@Override
	public PlayerAdvancements getAdvancements()
    {
		EntityPlayer player = this.avatar.getMaidMasterEntity();
		if (player instanceof EntityPlayerMP) {
			return ((EntityPlayerMP) player).getAdvancements();
		}
		return super.getAdvancements();
    }
	
	/**
	 * EntityPlayer.attackTargetEntityWithCurrentItemを改変
	 * 
	 * DamageSourceをMob形式へ変更する
	 * 
	 * @param targetEntity
	 */
    public void remodeled_attackTargetEntityWithCurrentItem(Entity targetEntity)
    {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(this, targetEntity)) return;
        if (targetEntity.canBeAttackedWithItem())
        {
            if (!targetEntity.hitByEntity(this))
            {
                float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                float f1;

                if (targetEntity instanceof EntityLivingBase)
                {
                    f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
                }
                else
                {
                    f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
                }

                float f2 = this.getCooledAttackStrength(0.5F);
                f = f * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                this.resetCooldown();

                if (f > 0.0F || f1 > 0.0F)
                {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackModifier(this);

                    if (this.isSprinting() && flag)
                    {
                        this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding() && targetEntity instanceof EntityLivingBase;
                    flag2 = flag2 && !this.isSprinting();

                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(this, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2)
                    {
                        f *= hitResult.getDamageModifier();
                    }

                    f = f + f1;
                    boolean flag3 = false;
                    double d0 = (double)(this.distanceWalkedModified - this.prevDistanceWalkedModified);

                    if (flag && !flag2 && !flag1 && this.onGround && d0 < (double)this.getAIMoveSpeed())
                    {
                        ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);

                        if (itemstack.getItem() instanceof ItemSword)
                        {
                            flag3 = true;
                        }
                    }

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(this);

                    if (targetEntity instanceof EntityLivingBase)
                    {
                        f4 = ((EntityLivingBase)targetEntity).getHealth();

                        if (j > 0 && !targetEntity.isBurning())
                        {
                            flag4 = true;
                            targetEntity.setFire(1);
                        }
                    }

                    double d1 = targetEntity.motionX;
                    double d2 = targetEntity.motionY;
                    double d3 = targetEntity.motionZ;
                    boolean flag5 = targetEntity.attackEntityFrom(this.getDamageSourceMob(), f);

                    if (flag5)
                    {
                        if (i > 0)
                        {
                            if (targetEntity instanceof EntityLivingBase)
                            {
                                ((EntityLivingBase)targetEntity).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                            }
                            else
                            {
                                targetEntity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * 0.017453292F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * 0.017453292F) * (float)i * 0.5F));
                            }

                            this.motionX *= 0.6D;
                            this.motionZ *= 0.6D;
                            this.setSprinting(false);
                        }

                        if (flag3)
                        {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * f;

                            for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D)))
                            {
                                if (entitylivingbase != this && entitylivingbase != targetEntity && !this.isOnSameTeam(entitylivingbase) && this.getDistanceSq(entitylivingbase) < 9.0D)
                                {
                                    entitylivingbase.knockBack(this, 0.4F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                                    entitylivingbase.attackEntityFrom(this.getDamageSourceMob(), f3);
                                }
                            }

                            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            this.spawnSweepParticles();
                        }

                        if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged)
                        {
                            ((EntityPlayerMP)targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.motionX = d1;
                            targetEntity.motionY = d2;
                            targetEntity.motionZ = d3;
                        }

                        if (flag2)
                        {
                            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
                            this.onCriticalHit(targetEntity);
                        }

                        if (!flag2 && !flag3)
                        {
                            if (flag)
                            {
                                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
                            }
                            else
                            {
                                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
                            }
                        }

                        if (f1 > 0.0F)
                        {
                            this.onEnchantmentCritical(targetEntity);
                        }

                        this.setLastAttackedEntity(targetEntity);

                        if (targetEntity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, this);
                        }

                        EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
                        ItemStack itemstack1 = this.getHeldItemMainhand();
                        Entity entity = targetEntity;

                        if (targetEntity instanceof MultiPartEntityPart)
                        {
                            IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)targetEntity).parent;

                            if (ientitymultipart instanceof EntityLivingBase)
                            {
                                entity = (EntityLivingBase)ientitymultipart;
                            }
                        }

                        if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase)
                        {
                            ItemStack beforeHitCopy = itemstack1.copy();
                            itemstack1.hitEntity((EntityLivingBase)entity, this);

                            if (itemstack1.isEmpty())
                            {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(this, beforeHitCopy, EnumHand.MAIN_HAND);
                                this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof EntityLivingBase)
                        {
                            float f5 = f4 - ((EntityLivingBase)targetEntity).getHealth();
                            this.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

                            if (j > 0)
                            {
                                targetEntity.setFire(j * 4);
                            }

                            if (this.world instanceof WorldServer && f5 > 2.0F)
                            {
                                int k = (int)((double)f5 * 0.5D);
                                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (double)(targetEntity.height * 0.5F), targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        this.addExhaustion(0.1F);
                    }
                    else
                    {
                        this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);

                        if (flag4)
                        {
                            targetEntity.extinguish();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Player形式ではなくMob形式でダメージソースを取得する
     * @return
     */
    private DamageSource getDamageSourceMob() {
    	return DamageSource.causeMobDamage(this.getMaid());
    }
}
