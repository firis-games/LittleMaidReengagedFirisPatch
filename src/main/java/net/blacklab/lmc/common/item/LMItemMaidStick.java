package net.blacklab.lmc.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * メイド棒
 * @author firis-games
 *
 */
public class LMItemMaidStick extends Item {

	/**
	 * コンストラクタ
	 */
	public LMItemMaidStick() {
		
		this.setMaxStackSize(1);
		
		this.setCreativeTab(CreativeTabs.MISC);
	}
	
	
	/**
	 * 右クリック
	 */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	if (handIn == EnumHand.MAIN_HAND) {
    		return onItemRightClickMainHand(worldIn, playerIn, handIn);
    	} else {
    		return onItemRightClickOffHand(worldIn, playerIn, handIn);
    	}
    }
    
    /**
     * メインハンドの場合
     */
    protected ActionResult<ItemStack> onItemRightClickMainHand(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	//有効範囲
    	//デフォルト10ブロック
    	int range = LMRConfig.cfg_general_maid_stick_range;
    	
    	List<EntityLittleMaid> maidList = worldIn.getEntitiesWithinAABB(EntityLittleMaid.class, new AxisAlignedBB(
    			playerIn.getPosition().add(-range, -range, -range), playerIn.getPosition().add(range, range, range)));
    	
    	Boolean isWait = null;
    	Boolean isFreedom = null;
    	
    	for (EntityLittleMaid maid : maidList) {
    		//契約メイドさんか確認
    		if (!maid.isMaidContractOwner(playerIn)) break;
    		
    		if (isWait == null || isFreedom == null) {
    			isWait = maid.isMaidWait();
    			isFreedom = maid.isFreedom();
	    		//スニーク判定
	    		if (playerIn.isSneaking()) {
	    			//自由行動の反転
	    			isFreedom = !isFreedom;
	    		} else {
	    			//待機状態の反転
	    			isWait = !isWait;
	    		}
    		}
    		
    		//待機状態を反転する
    		maid.changeMaidWaitAndFreedom(isWait, isFreedom, playerIn);
    		worldIn.setEntityState(maid, (byte)11);
    		maid.playSound("entity.item.pickup");
    		
    	}
    	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    
    /**
     * オフハンドの場合
     */
    protected ActionResult<ItemStack> onItemRightClickOffHand(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	//有効範囲128ブロック（設定なし）
    	int range = 128;
    	
    	//集めるときは20
    	if (playerIn.isSneaking()) {
    		range = 20;
    	}
    	
    	List<EntityLittleMaid> maidList = worldIn.getEntitiesWithinAABB(EntityLittleMaid.class, new AxisAlignedBB(
    			playerIn.getPosition().add(-range, -range, -range), playerIn.getPosition().add(range, range, range)));
    	
    	for (EntityLittleMaid maid : maidList) {
    		if (!playerIn.isSneaking()) {
        		//5s発光状態を付与
        		maid.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 0, false, false));    			
    		} else {
    			//メイドさんのご主人様が自分であること
				if (maid.isContract() && maid.isMaidContractOwner(playerIn)) {
					//メイドさんをワープ
					maid.setPosition(playerIn.posX + (worldIn.rand.nextDouble() - 0.5D) * 2.0D, 
							playerIn.posY, 
							playerIn.posZ + (worldIn.rand.nextDouble() - 0.5D) * 2.0D);
				}
    		}
    	}
    	
    	//対象がいる場合は効果音
    	if (maidList.size() != 0) {
    		playerIn.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.item.pickup")), 1.0F, 1.0F);
    	}
    	//クールタイム設定
    	playerIn.getCooldownTracker().setCooldown(this, 20);
    	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.maid_stick.info"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + I18n.format("item.maid_stick.details1"));
		tooltip.add(TextFormatting.DARK_AQUA.toString() + TextFormatting.ITALIC.toString() + I18n.format("item.maid_stick.details2"));
		tooltip.add(TextFormatting.DARK_GREEN.toString() + TextFormatting.ITALIC.toString() + I18n.format("item.maid_stick.details3"));
		tooltip.add(TextFormatting.DARK_GREEN.toString() + TextFormatting.ITALIC.toString() + I18n.format("item.maid_stick.details4"));
    }
}
