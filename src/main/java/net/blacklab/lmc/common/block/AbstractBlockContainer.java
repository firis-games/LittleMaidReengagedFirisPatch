package net.blacklab.lmc.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractBlockContainer extends BlockContainer {

	protected AbstractBlockContainer(Material materialIn) {
		super(materialIn);
	}
	
	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

	/**
	 * ブロック破壊時のイベント
	 */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null) {
        	IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        	if (capability != null) {
        		for (int i = 0; i < capability.getSlots(); i++) {
        			ItemStack stack = capability.getStackInSlot(i);
        			if (!stack.isEmpty()) {
        				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
        			}
        		}
        		worldIn.updateComparatorOutputLevel(pos, this);
        	}
        }
        super.breakBlock(worldIn, pos, state);
    }
}
