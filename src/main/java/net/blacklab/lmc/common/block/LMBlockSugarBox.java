package net.blacklab.lmc.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmc.common.tileentity.LMTileSugarBox;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * シュガーボックス
 * @author firis-games
 *
 */
public class LMBlockSugarBox extends AbstractBlockContainer {

	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	public LMBlockSugarBox() {
		super(Material.PISTON);
		this.setHardness(5.0F);
		this.setResistance(20.0F);
		this.setLightLevel(1.0F);
		this.setCreativeTab(CreativeTabs.MISC);
	}

	/**
	 * TileEntity
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new LMTileSugarBox();
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.sugar_box.info"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + I18n.format("tile.sugar_box.details"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + "Range : 7×7 Chunk");
    }
	
}
