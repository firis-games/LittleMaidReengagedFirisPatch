package net.blacklab.lmc.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmc.common.helper.LittleMaidHelper;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.network.LMRNetwork;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * メイドシュガー
 *
 */
public class LMItemMaidSugar extends Item {
	
	/**
	 * EntityDataNBT保存用キー
	 */
	public static String ANIMAL_MAID_KEY = "animalLittleMaid";
	
	public LMItemMaidSugar() {
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		return metamorphoseMaid(entity, player, EnumHand.MAIN_HAND);
    }
	
	/**
	 * Shift＋右クリックからのアイテム化
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
		return metamorphoseMaid(target, playerIn, hand);
    }
	
	
	/**
	 * リトルメイドの変身処理
	 * @return
	 */
	public boolean metamorphoseMaid(Entity targetEntity, EntityPlayer player, EnumHand hand) {
		
		//生き物であること
		if (!(targetEntity instanceof EntityLiving)) {
			return false;
		}
		
		ItemStack stack = player.getHeldItem(hand);
		
		EntityLiving living = (EntityLiving) targetEntity;
		World world = living.getEntityWorld();
		
		if (world.isRemote) return false;
		
		//カスタムNBTデータ取得
		NBTTagCompound custamTag = living.getEntityData();
		NBTTagCompound animalMaidTag = null;;
		if (custamTag.hasKey(ANIMAL_MAID_KEY)) {
			animalMaidTag = (NBTTagCompound) custamTag.getTag(ANIMAL_MAID_KEY);
		}
		
		//メイドさんの動物変身
		if (animalMaidTag != null && living instanceof EntityLittleMaid) {
			
			//動物を生成
			EntityLiving animalEntity = (EntityLiving) EntityList.createEntityFromNBT(animalMaidTag, world);
			
			//メイドさん情報を登録
			animalEntity = createMetamorphoseEntityLiving(living, animalEntity);
			
			if (!world.isRemote) {
				//サーバーサイド
				//動物のスポーン
				animalEntity.setLocationAndAngles(living.posX, living.posY, living.posZ, 
						0.0F, 0.0F);
				world.spawnEntity(animalEntity);
				//メイドさん消去
				living.setDead();
				
				//ハートパーティクルと音設定
				//world.setEntityState(animalEntity, (byte)7);
				LMRNetwork.PacketSpawnParticleS2C(animalEntity.getPosition(), 0);
				((EntityLittleMaid) living).playSound("entity.leashknot.place");

			}
			if (player.capabilities.isCreativeMode != true) {
				stack.shrink(1);
			}
			return true;
			
		}
		
		//動物のメイド変身
		//タグを持っている場合は無条件でアニマル形態と判断する
		if (animalMaidTag != null) {
			
			//メイドさんを生成する
			EntityLittleMaid littleMaid = (EntityLittleMaid) EntityList.createEntityFromNBT(animalMaidTag, world);
			
			//動物情報を登録
			littleMaid = (EntityLittleMaid) createMetamorphoseEntityLiving(living, littleMaid);
			
			if (!world.isRemote) {
				//メイドさんのスポーン
				littleMaid.setLocationAndAngles(living.posX, living.posY, living.posZ, 
						0.0F, 0.0F);
				world.spawnEntity(littleMaid);
				
				//メイドさん消去
				living.setDead();
				
				//ハートパーティクルと音設定
				world.setEntityState(littleMaid, (byte)7);
				littleMaid.playSound("entity.item.pickup");
			}

			if (player.capabilities.isCreativeMode != true) {
				stack.shrink(1);
			}
			return true;
		}
		
		//テイム動物のメイド化
		if (animalMaidTag == null && isAnimalMaid(living, player)) {
			
			//メイドさんを生成する
			EntityLittleMaid littleMaid = (EntityLittleMaid) EntityList.createEntityByIDFromName(
					new ResourceLocation(LittleMaidReengaged.DOMAIN, "littlemaid"), world);

			//メイドさん初期契約
			littleMaid.setFirstContract(player);

			//動物情報を登録
			littleMaid = (EntityLittleMaid) createMetamorphoseEntityLiving(living, littleMaid);

			if (!world.isRemote) {
				//メイドさんのスポーン
				littleMaid.setLocationAndAngles(living.posX, living.posY, living.posZ, 
						0.0F, 0.0F);
				world.spawnEntity(littleMaid);
				
				//動物消去
				living.setDead();
				
				//ハートパーティクルと音設定
				world.setEntityState(littleMaid, (byte)7);
				littleMaid.playSound("entity.item.pickup");
			}
			
			if (player.capabilities.isCreativeMode != true) {
				stack.shrink(1);
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * メイドさんの変身対象の動物か判断する
	 * 
	 * テイム系の動物 + ご主人様が一致する場合対象とする
	 * @return
	 */
	public static boolean isAnimalMaid(EntityLiving living, EntityPlayer player) {

		//メイドさんは除外
		if (living instanceof EntityLittleMaid) return false;

		//テイム系Mob
		if (living instanceof IEntityOwnable) {
			
			IEntityOwnable tameableEntity = (IEntityOwnable) living;
			
			//ご主人様判定
			if (player.getUniqueID().equals(tameableEntity.getOwnerId())) {
				return true;
			}
		//馬系Mob
		} else if (living instanceof AbstractHorse) {
			
			AbstractHorse targetEntity = (AbstractHorse) living;
			//ご主人様判定
			if (player.getUniqueID().equals(targetEntity.getOwnerUniqueId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * メイドさん -> 動物
	 * 動物 -> メイドさん
	 * 変換Entityを生成する
	 * @return
	 */
	public static EntityLiving createMetamorphoseEntityLiving(EntityLiving fromEntity, EntityLiving toEntity) {

		//From側のカスタムデータを削除する
		fromEntity.getEntityData().removeTag(LMItemMaidSugar.ANIMAL_MAID_KEY);

		//fromEntityをNBT化
		NBTTagCompound animalTag = LittleMaidHelper.getNBTTagFromEntityLiving(fromEntity);
		
		//fromEntity情報の登録
		NBTTagCompound customTag = toEntity.getEntityData();
		customTag.setTag(ANIMAL_MAID_KEY, animalTag);
		
		//HPの同期
		double fromHpRatio = ((double)fromEntity.getHealth()) / fromEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
		double toHpMax = toEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
		
		toEntity.setHealth((float) Math.max(1.0D, toHpMax * fromHpRatio));
		
		return toEntity;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.maid_sugar.info"));
    }
	
}
