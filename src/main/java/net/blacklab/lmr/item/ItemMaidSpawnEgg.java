package net.blacklab.lmr.item;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMaidSpawnEgg extends Item
{

	public ItemMaidSpawnEgg()
	{
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.MISC);
		setUnlocalizedName(LittleMaidReengaged.DOMAIN + ":spawn_littlemaid_egg");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		//Block block = worldIn.getBlockState(pos).getBlock();
		int par4 = pos.getX(); int par5 = pos.getY() + 1; int par6 = pos.getZ();
		/*
		par4 += Facing.offsetsXForSide[par7];
		par5 += Facing.offsetsYForSide[par7];
		par6 += Facing.offsetsZForSide[par7];
		*/
		double d0 = 0.0D;

//		if (par7 == EnumFacing.UP && block.getRenderType(par3World.getBlockState(par46X)) == EnumBlockRenderType)
//		{
//			d0 = 0.5D;
//		}

		Entity entity = spawnMaid(worldIn, par4 + 0.5D, par5 + d0, par6 + 0.5D);

		if (entity != null)
		{
			if (entity instanceof EntityLivingBase && stack.hasDisplayName())
			{
				((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
			}

			if (!player.capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}

		return EnumActionResult.SUCCESS;
	}

	public static Entity spawnMaid(World par0World, double par2, double par4, double par6)
	{
		EntityLiving entityliving = null;
		try {
			entityliving = new EntityLittleMaid(par0World);

			entityliving.setLocationAndAngles(par2, par4, par6, (par0World.rand.nextFloat() * 360.0F) - 180.0F, 0.0F);
//			((LMM_EntityLittleMaid)entityliving).setTextureNames();
			
			//((EntityLittleMaid) entityliving).onSpawnWithEgg();
			entityliving.onInitialSpawn(par0World.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
			
			par0World.spawnEntity(entityliving);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return entityliving;
	}

}
