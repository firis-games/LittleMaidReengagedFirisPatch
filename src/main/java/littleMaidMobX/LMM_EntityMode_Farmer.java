package littleMaidMobX;

import java.util.Iterator;

import net.blacklab.lib.minecraft.item.ItemUtil;
import net.blacklab.lib.minecraft.vector.VectorUtil;
import net.blacklab.lmmnx.achievements.LMMNX_Achievements;
import net.blacklab.lmmnx.api.mode.LMMNX_API_Farmer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

/**
 * メイド農家。付近の農地に移動し耕作可能であれば耕す。
 * @author Verclene
 *
 */
public class LMM_EntityMode_Farmer extends LMM_EntityModeBase {
	
	public static final int mmode_Farmer = 0x0023;
	public static final double limitDistance_Freedom = 361D;
	public static final double limitDistance_Follow  = 100D;
	public static final int WATER_RADIUS = 4;

	private int clearCount = 0;

	public LMM_EntityMode_Farmer(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ
		LMM_TriggerSelect.appendTriggerItem(null, "Hoe", "");
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
		
		owner.addMaidMode(ltasks, "Farmer", mmode_Farmer);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (LMMNX_API_Farmer.isHoe(owner, litemstack)) {
				owner.setMaidMode("Farmer");
				if (LMMNX_Achievements.ac_Farmer != null) {
					pentityplayer.triggerAchievement(LMMNX_Achievements.ac_Farmer);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
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
	public int getNextEquipItem(int pMode) {
		int li;
		ItemStack litemstack;
		
		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Farmer : 
			for (li = 0; li < LMM_InventoryLittleMaid.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// クワ
				if (LMMNX_API_Farmer.isHoe(owner,litemstack)) {
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
		return LMMNX_API_Farmer.isHoe(owner, pItemStack)||LMMNX_API_Farmer.isSeed(pItemStack.getItem())||LMMNX_API_Farmer.isCrop(pItemStack.getItem());
	}
	
	@Override
	public boolean isSearchBlock() {
		return !owner.isMaidWait()&&(owner.getCurrentEquippedItem()!=null);
	}

	@Override
	public boolean shouldBlock(int pMode) {
		return owner.getCurrentEquippedItem() != null;
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		if(owner.isFreedom()){
			if(owner.getPosition().distanceSq(px, py, pz)>limitDistance_Freedom){
				return false;
			}
		}else if(owner.getMaidMasterEntity()!=null){
			if(owner.getMaidMasterEntity().getDistanceSq(px,py,pz)>limitDistance_Follow){
				return false;
			}
		}
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
	public boolean executeBlock(int pMode, int px, int py, int pz) {
//		if(owner.worldObj.isRemote) return false;
		ItemStack curStack = owner.getCurrentEquippedItem();

		boolean haveNothing = !LMMNX_API_Farmer.isHoe(owner,curStack);
		
		if (!haveNothing && isUnfarmedLand(px,py,pz) &&
				curStack.onItemUse(owner.maidAvatar, owner.worldObj, new BlockPos(px, py, pz), EnumFacing.UP, 0.5F, 1.0F, 0.5F)) {
			owner.setSwing(10, LMM_EnumSound.Null, false);
			owner.playLittleMaidSound(LMM_EnumSound.farmer_farm, false);
			
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
				stack.onItemUse(owner.maidAvatar, owner.worldObj, new BlockPos(px,py,pz), EnumFacing.UP, 0.5F, 1.0F, 0.5F);
				owner.playLittleMaidSound(LMM_EnumSound.farmer_plant, false);
				if (owner.maidAvatar.capabilities.isCreativeMode) {
					stack.stackSize = li;
				}
				owner.setSwing(10, LMM_EnumSound.Null, false);
				if(stack.stackSize<=0){
					owner.maidInventory.setInventorySlotContents(index, null);
				}
			}
		}
		if(isCropGrown(px,py,pz)){
			// 収穫
			BlockPos pos = new BlockPos(px,py,pz);
			owner.worldObj.destroyBlock(pos, true);
			owner.setSwing(10, LMM_EnumSound.Null, false);
			owner.playLittleMaidSound(LMM_EnumSound.farmer_harvest, false);
			owner.addMaidExperience(4f);
			executeBlock(pMode,px,py-1,pz);
//			return true;
		}
		return false;
	}
	
	@Override
	public void onUpdate(int pMode) {
		// TODO 自動生成されたメソッド・スタブ
		if(pMode==mmode_Farmer&&++clearCount>=300&&owner.getNavigator().noPath()){
			try{
				if(!owner.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
		}
	}

	@Override
	public void updateAITick(int pMode) {
		if (pMode == mmode_Farmer && owner.getNextEquipItem()) {
			if(owner.getAIMoveSpeed()>0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack()==-1){
				owner.setMaidMode("FarmPorter");
			}
		}
		if(pMode==LMM_EntityMode_Basic.mmode_FarmPorter &&
				owner.maidInventory.getFirstEmptyStack()>-1 &&
				!owner.mstatWorkingCount.isEnable()){
			owner.setMaidMode("Farmer");
		}
	}

	protected int getHadSeedIndex(){
		int r=-1;
		for(String fname:LMMNX_API_Farmer.getItemsListForSeed()){
			Item item = ItemUtil.getItemByStringId(fname);
			r = owner.maidInventory.getInventorySlotContainItem(item);
			if(r!=-1) break;
		}
		return r;
	}

	protected boolean isUnfarmedLand(int x, int y, int z){
		//耕されておらず、直上が空気ブロック
		//近くに水があるときにとりあえず耕す用
		Block b = owner.worldObj.getBlockState(new BlockPos(x,y,z)).getBlock();
		return (Block.isEqualTo(b, Blocks.dirt)||Block.isEqualTo(b, Blocks.grass))&&
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
		IBlockState state = owner.worldObj.getBlockState(new BlockPos(x,y,z));
		if(state.getBlock() instanceof BlockCrops){
			int age = (Integer) state.getValue(BlockCrops.AGE);
			if(age==7) return true;
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

		do
		{
			if (!iterator.hasNext())
			{
				return false;
			}
			
			mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
		}
		while (owner.worldObj.getBlockState(mutableblockpos).getBlock().getMaterial() != Material.water);

		return true;
	}

}
