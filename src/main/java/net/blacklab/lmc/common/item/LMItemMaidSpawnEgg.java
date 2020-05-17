package net.blacklab.lmc.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmr.LittleMaidReengaged;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * メイドさん用のスポーンエッグ
 * 
 * スポーンエッグのスポーン処理を利用する方式
 * @author firis-games
 *
 */
public class LMItemMaidSpawnEgg extends Item {

	private static ResourceLocation rlLittleMaid = new ResourceLocation(LittleMaidReengaged.MODID, "littlemaid");
	
	public LMItemMaidSpawnEgg() {
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	 /**
	 * ブロックに対してアイテムを使用する
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemstack = player.getHeldItem(hand);

		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return EnumActionResult.FAIL;
		}
		
		//スポーン処理
		BlockPos blockpos = pos.offset(facing);
		Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, rlLittleMaid, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.0D, (double)blockpos.getZ() + 0.5D);

		//事後処理
		if (entity != null) {
			if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
				entity.setCustomNameTag(itemstack.getDisplayName());
			}

			ItemMonsterPlacer.applyItemEntityDataToEntity(worldIn, player, itemstack, entity);

			if (!player.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.maid_spawn_egg.info"));
    }
	
}
