package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.HashMap;
import java.util.Iterator;

import net.blacklab.lib.minecraft.vector.VectorUtil;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ModeTrigger;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.IPlantable;

/**
 * メイド農家。付近の農地に移動し耕作可能であれば耕す。
 * @author Verclene
 *
 */
public class EntityMode_Farmer extends EntityModeBase {

	public static final String mmode_Farmer		= "SYS:Farmer";
	public static final String mtrigger_Hoe		= "Farmer:Hoe";
	public static final String mtrigger_Seed 	= "Farmer:Seed";
	public static final int WATER_RADIUS = 4;
	private int clearCount = 0;

	public EntityMode_Farmer(EntityLittleMaid pEntity) {
		super(pEntity);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void init() {
		ModeTrigger.registerTrigger(mtrigger_Hoe, new HashMap<>());
	}

	@Override
	public int priority() {
		// TODO 自動生成されたメソッド・スタブ
		return 6300;
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove,
			EntityAITasks pDefaultTargeting) {
		// TODO 自動生成されたメソッド・スタブ
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;

		owner.addMaidMode(mmode_Farmer, ltasks);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (litemstack != null) {
			if (owner.getModeTrigger().isTriggerable(mtrigger_Hoe, litemstack, ItemHoe.class)) {
				owner.setMaidMode(mmode_Farmer);
				if (pentityplayer != null) {
					pentityplayer.addStat(AchievementsLMRE.ac_Farmer);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(String pMode) {
		// TODO 自動生成されたメソッド・スタブ
		switch (pMode) {
		case mmode_Farmer :
			owner.setBloodsuck(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(String pMode) {
		int li;
		if ((li = super.getNextEquipItem(pMode)) >= 0) {
			return InventoryLittleMaid.handInventoryOffset;
		}

		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Farmer :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// クワ
				if (owner.getModeTrigger().isTriggerable(mtrigger_Hoe, litemstack, ItemHoe.class)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		if(pItemStack==null) return false;
		return true;//UtilModeFarmer.isHoe(owner, pItemStack)||UtilModeFarmer.isSeed(pItemStack.getItem())||UtilModeFarmer.isCrop(pItemStack.getItem());
	}

	@Override
	public boolean isSearchBlock() {
		return !owner.isMaidWait()&&(owner.getCurrentEquippedItem()!=null);
	}

	@Override
	public boolean shouldBlock(String pMode) {
		return owner.getCurrentEquippedItem() != null;
	}

	@Override
	public boolean checkBlock(String pMode, int px, int py, int pz) {
		if (!super.checkBlock(pMode, px, py, pz)) return false;

		if(!VectorUtil.canMoveThrough(owner, 0.9D, px + 0.5D, py + 1.9D, pz + 0.5D, py==MathHelper.floor_double(owner.posY-1D), true, false)) return false;
		if(isUnfarmedLand(px,py,pz)) return true;
		if(isFarmedLand(px,py,pz)){
			/*耕地が見つかっても、
			 * ①周りに未耕作の地域がある場合はtrueを返さない
			 * ②種を持っていない場合もfalse
			 */
/*
			int p=WATER_RADIUS*3;
			for(int az=-p;az<=p;az++){
				for(int ax=-p;ax<=p;ax++){
					if(isUnfarmedLand(px+ax,py,pz+az)) return false;
				}
			}
*/
			if(getHadSeedIndex()==-1)
				return false;
			return true;
		}
		if(isCropGrown(px,py,pz)) return true;
		return false;
	}

	@Override
	public boolean executeBlock(String pMode, int px, int py, int pz) {
		ItemStack curStack = owner.getCurrentEquippedItem();

		boolean haveNothing = !owner.getModeTrigger().isTriggerable(mtrigger_Hoe, curStack, ItemHoe.class);

		if (!haveNothing && isUnfarmedLand(px,py,pz) &&
				curStack.onItemUse(owner.maidAvatar, owner.worldObj, new BlockPos(px, py, pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F) == EnumActionResult.SUCCESS) {
			owner.setSwing(10, EnumSound.Null, false);
			owner.playLittleMaidSound(EnumSound.farmer_farm, false);

			/*
			if (owner.maidAvatar.capabilities.isCreativeMode) {
				lis.stackSize = li;
			}
			*/
			if (curStack.stackSize <= 0) {
				owner.maidInventory.setInventoryCurrentSlotContents(null);
				owner.getNextEquipItem();
			}
//			owner.getNavigator().clearPathEntity();
		}
		if(isFarmedLand(px,py,pz)){
			//種を持っている
			int index = getHadSeedIndex();
			if(index!=-1){
				ItemStack stack = owner.maidInventory.getStackInSlot(index);
				int li = stack.stackSize;
				stack.onItemUse(owner.maidAvatar, owner.worldObj, new BlockPos(px,py,pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F);
				owner.playLittleMaidSound(EnumSound.farmer_plant, false);
				if (owner.maidAvatar.capabilities.isCreativeMode) {
					stack.stackSize = li;
				}
				owner.setSwing(10, EnumSound.Null, false);
				if(stack.stackSize<=0){
					owner.maidInventory.setInventorySlotContents(index, null);
				}
			}
		}
		if(isCropGrown(px,py,pz)){
			// 収穫
			BlockPos pos = new BlockPos(px,py,pz);
			owner.worldObj.destroyBlock(pos, true);
			owner.setSwing(10, EnumSound.Null, false);
			owner.playLittleMaidSound(EnumSound.farmer_harvest, false);
			owner.addMaidExperience(4f);
			executeBlock(pMode,px,py-1,pz);
//			return true;
		}
		return false;
	}

	@Override
	public void onUpdate(String pMode) {
		// TODO 自動生成されたメソッド・スタブ
		if(pMode.equals(mmode_Farmer) && ++clearCount >= 300 && owner.getNavigator().noPath()){
			try{
				if(!owner.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
		}
	}

	@Override
	public void updateAITick(String pMode) {
		if (pMode.equals(mmode_Farmer)) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_FarmPorter);
			}
		}
	}

	protected int getHadSeedIndex(){
		for (int i=0; i < owner.maidInventory.getSizeInventory(); i++) {
			ItemStack pStack;
			if ((pStack = owner.maidInventory.getStackInSlot(i)) != null &&
					owner.getModeTrigger().isTriggerable(mtrigger_Seed, pStack, IPlantable.class)) {
				return i;
			}
		}
		return -1;
	}

	protected boolean isUnfarmedLand(int x, int y, int z){
		//耕されておらず、直上が空気ブロック
		//近くに水があるときにとりあえず耕す用
		Block b = owner.worldObj.getBlockState(new BlockPos(x,y,z)).getBlock();
		return (Block.isEqualTo(b, Blocks.DIRT)||Block.isEqualTo(b, Blocks.GRASS))&&
				owner.worldObj.isAirBlock(new BlockPos(x,y+1,z)) && isBlockWatered(x, y, z);
	}

	protected boolean isFarmedLand(int x, int y, int z){
		//耕されていて、直上が空気ブロック
		IBlockState state = owner.worldObj.getBlockState(new BlockPos(x,y,z));
		if(state.getBlock() instanceof BlockFarmland){
			return owner.worldObj.isAirBlock(new BlockPos(x,y+1,z));
		}
		return false;
	}

	protected boolean isCropGrown(int x, int y, int z){
		BlockPos position = new BlockPos(x, y, z);
		IBlockState state = owner.worldObj.getBlockState(position);
		Block block = state.getBlock();

		if(block instanceof BlockCrops){
			// Max age -> Cannot glow(#34)
			return !((BlockCrops)block).canGrow(owner.worldObj, position, state, owner.worldObj.isRemote);
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	protected boolean isBlockWatered(int x, int y, int z){
		// 雨天時は検索範囲を制限
		boolean flag = owner.worldObj.isRaining();
		BlockPos pos = new BlockPos(x,y,z);
		Iterator iterator = BlockPos.getAllInBoxMutable(pos.add(-WATER_RADIUS, 0, -WATER_RADIUS),
				pos.add(WATER_RADIUS, 1, WATER_RADIUS)).iterator();
		BlockPos.MutableBlockPos mutableblockpos;

		IBlockState iState;
		do
		{
			if (!iterator.hasNext())
			{
				return false;
			}

			mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
		}
		while ((iState = owner.worldObj.getBlockState(mutableblockpos)).getMaterial() != Material.WATER);

		return true;
	}
	
}
