package net.blacklab.lmc.common.item;

import static net.blacklab.lmr.util.Statics.dataWatch_Flags_remainsContract;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmc.common.entity.LMEntityItemAntiDamage;
import net.blacklab.lmc.common.helper.LittleMaidHelper;
import net.blacklab.lmr.LittleMaidReengaged.LMItems;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
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
 * メイドの土産
 * @author computer
 *
 */
public class LMItemMaidSouvenir extends Item {

	
	public LMItemMaidSouvenir() {
		
		//クリエイティブタブには登録しない
		this.setMaxStackSize(1);
		
	}

	/**
	 * メイドさんを生成する
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		
		ItemStack stack = player.getHeldItem(hand);
		
		if (!stack.isEmpty() 
				&& stack.getItem() == LMItems.MAID_SOUVENIR 
				&& stack.hasTagCompound()) {
			
			BlockPos position = pos.offset(facing);
			double x = position.getX() + 0.5;
			double y = position.getY();
			double z = position.getZ() + 0.5;
			
			//メイドさんのスポーン
			Entity entity = LittleMaidHelper.spawnEntityFromItemStack(stack, worldIn, x, y, z);
			
			//ストライキ状態にする
			if (entity != null) {
				EntityLittleMaid maid = (EntityLittleMaid) entity;
				
				//契約時間をリセット
				maid.clearMaidContractLimit();
				//ストライキを設定する
				maid.setMaidFlags(true, dataWatch_Flags_remainsContract);
				
			}
			
			//アイテムを消費
			//クリエイティブでも消費させる
			//stack.shrink(1);
			player.setHeldItem(hand, ItemStack.EMPTY);
			
			//1秒のCoolDownTimeを設定
			player.getCooldownTracker().setCooldown(this, 20 * 1);
			
			return EnumActionResult.SUCCESS;

		}
		
        return EnumActionResult.PASS;
    }
	
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.maid_souvenir.info"));
		
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
	
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	@Override
	@Nullable
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
		EntityItem entity = new LMEntityItemAntiDamage(world, location.posX, location.posY, location.posZ, itemstack);
		entity.setDefaultPickupDelay();

		entity.motionX = location.motionX;
		entity.motionY = location.motionY;
		entity.motionZ = location.motionZ;
		
		//発光設定
		if (LMRConfig.cfg_general_item_glowing) {
			entity.setGlowing(true);
		}
		
        return entity;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.hasTagCompound();
    }
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
    {
        return stack.hasTagCompound() ? EnumRarity.RARE : EnumRarity.COMMON;
    }
}
