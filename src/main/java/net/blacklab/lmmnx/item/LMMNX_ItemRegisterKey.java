package net.blacklab.lmmnx.item;

import java.util.List;

import littleMaidMobX.LMM_LittleMaidMobNX;
import littleMaidMobX.LMM_TriggerSelect;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class LMMNX_ItemRegisterKey extends Item {
	
	public static final String RK_MODE_TAG = "LMMNX_RK_MODE";
	public static final String RK_COUNT = "LMMNX_RK_COUNT";
	
	public static final int RK_MAX_COUNT = 32;
	
	public LMMNX_ItemRegisterKey() {
		setUnlocalizedName(LMM_LittleMaidMobNX.DOMAIN + ":lmmnx_registerkey");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn,
			EntityPlayer playerIn) {
		NBTTagCompound tagCompound = itemStackIn.getTagCompound();
		if(tagCompound==null) {
			tagCompound = new NBTTagCompound();
			itemStackIn.setTagCompound(tagCompound);
		}
		
		int index = 0;
		String modeString = tagCompound.getString(RK_MODE_TAG);
		
		// 登録モードを切り替える．
		index = LMM_TriggerSelect.selector.indexOf(modeString) + 1;
		if(index >= LMM_TriggerSelect.selector.size()) index = 0;

		modeString = LMM_TriggerSelect.selector.get(index);
		tagCompound.setString(RK_MODE_TAG, modeString);
		
		if(worldIn.isRemote)
			playerIn.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("littleMaidMob.chat.text.changeregistermode") + modeString));

		return itemStackIn;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,
			List tooltip, boolean advanced) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null) {
			tooltip.add("Mode: "+tagCompound.getString(RK_MODE_TAG));
			tooltip.add("Remains: " + (RK_MAX_COUNT-tagCompound.getInteger(RK_COUNT)));
		}
	}
/*
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		// TODO 自動生成されたメソッド・スタブ
		ItemStack stack = new ItemStack(new LMMNX_ItemRegisterKey(),1,0);

		NBTTagCompound tagCompound = new NBTTagCompound();
		String str = LMM_TriggerSelect.selector.get(0);
		tagCompound.setString(RK_MODE_TAG, str);
		stack.setTagCompound(tagCompound);

		subItems.add(stack);
	}
*/
	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn,
			int itemSlot, boolean isSelected) {
		if(!stack.hasTagCompound()){
			NBTTagCompound t = new NBTTagCompound();
			t.setString(RK_MODE_TAG, LMM_TriggerSelect.selector.get(0));
			stack.setTagCompound(t);
		}
	}

}
