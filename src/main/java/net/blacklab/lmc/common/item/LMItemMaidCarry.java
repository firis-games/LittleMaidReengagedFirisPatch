package net.blacklab.lmc.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmc.common.entity.LMEntityItemAntiDamage;
import net.blacklab.lmc.common.helper.LittleMaidHelper;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Mob持ち運び用アイテム
 * @author computer
 *
 */
public class LMItemMaidCarry extends Item {

	/**
	 * コンストラクタ
	 */
	public LMItemMaidCarry() {
		
		this.setMaxStackSize(1);
		
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	/**
	 * 左クリックからのアイテム化
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		//メイドさんアイテム化
		return createMaidItemStack(stack, player, entity);
    }
	
	/**
	 * Shift＋右クリックからのアイテム化
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
		//メイドさんアイテム化
		boolean ret = createMaidItemStack(stack, playerIn, target);
		if (ret && stack.hasTagCompound()) {
			playerIn.setHeldItem(hand, stack);			
		}
		return ret;
    }
	
	/**
	 * メイドさんを生成する
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		
		ItemStack stack = player.getHeldItem(hand);
		
		if (!stack.isEmpty() 
				&& stack.getItem() instanceof LMItemMaidCarry
				&& stack.hasTagCompound()) {
			
			BlockPos position = pos.offset(facing);
			double x = position.getX() + 0.5;
			double y = position.getY();
			double z = position.getZ() + 0.5;
			
			//メイドさんのスポーン
			LittleMaidHelper.spawnEntityFromItemStack(stack, worldIn, x, y, z);
			
			//Tag情報を初期化
			stack.setTagCompound(null);
			
			return EnumActionResult.SUCCESS;

		}
		
        return EnumActionResult.PASS;
    }
	
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.maid_carry.info"));
		
		if (stack.hasTagCompound()) {
			
			//OwnerId
			if (stack.getTagCompound().hasKey("maid_owner")) {
				tooltip.add("Owner : " + stack.getTagCompound().getString("maid_owner"));
			}
			//メイド名
			if (stack.getTagCompound().hasKey("maid_name")) {
				tooltip.add("Maid : " + stack.getTagCompound().getString("maid_name"));
			}
			
		}
    }
	
	/**
	 * 耐性EntityItemを利用する
	 */
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	/**
	 * 耐性EntityItemを生成する
	 * voidダメージ以外は無効化する
	 */
	@Override
	@Nullable
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
		EntityItem entity = new LMEntityItemAntiDamage(world, location.posX, location.posY, location.posZ, itemstack);
		entity.setDefaultPickupDelay();

		entity.motionX = location.motionX;
		entity.motionY = location.motionY;
		entity.motionZ = location.motionZ;
		
        return entity;
    }
	
	/**
	 * NBTタグを持つ場合にエフェクト表示
	 */
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.hasTagCompound();
    }
	
	/**
	 * メイドさんをアイテム化
	 * @return
	 */
	public boolean createMaidItemStack(ItemStack stack, EntityPlayer player, Entity entity) {
	
		//メイドさんチェック
		if (!(entity instanceof EntityLittleMaid)) {
			return false;
		}
		
		EntityLittleMaid entityMaid = (EntityLittleMaid) entity;
		
		//契約メイドさんチェック
		if (!player.getUniqueID().equals(entityMaid.getMaidMasterUUID())) {
			return true;
		}
		
		//NBTがある場合は何もしない
		if (stack.hasTagCompound()) {
			return true;
		}
		
		//メイド用スポーン情報の書き込み
		LittleMaidHelper.getItemStackFromEntity(entityMaid, stack);
		
		//メイドさん消去
		entityMaid.setDead();
		
		return true;
	}
	
}
