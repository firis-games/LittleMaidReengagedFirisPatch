package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import firis.lmlib.api.constant.EnumSound;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTriggerRegisterHelper;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.helper.VectorUtil;
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

	public static final String mmode_Farmer		= "Farmer";
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
		ModeTriggerRegisterHelper.register(mmode_Farmer, mtrigger_Hoe);
		//メイドモードをFarmerSeedで種を設定できるように想定
		ModeTriggerRegisterHelper.register(mmode_Farmer + "Seed", mtrigger_Seed);
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
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mmode_Farmer, litemstack)) {
				owner.setMaidMode(mmode_Farmer);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.Farmer);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean isTriggerItem(String pMode, ItemStack par1ItemStack) {
		if (par1ItemStack.isEmpty()) {
			return false;
		}
		switch (pMode) {
		case mmode_Farmer:
			return owner.getModeTrigger().isTriggerable(mtrigger_Hoe, par1ItemStack, ItemHoe.class);
		}
		return super.isTriggerItem(pMode, par1ItemStack);
	}
	
	/**
	 * 種アイテムのトリガー判断処理
	 * @param itemStack
	 * @return
	 */
	private boolean isTriggerItemSeed(ItemStack stack) {
		return owner.getModeTrigger().isTriggerable(mtrigger_Seed, stack, IPlantable.class);
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
				if (litemstack.isEmpty()) continue;

				// クワ
				if (isTriggerItem(mmode_Farmer, litemstack)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		if(pItemStack.isEmpty()) return false;
		return true;//UtilModeFarmer.isHoe(owner, pItemStack)||UtilModeFarmer.isSeed(pItemStack.getItem())||UtilModeFarmer.isCrop(pItemStack.getItem());
	}

	@Override
	public boolean isSearchBlock() {
		return !owner.isMaidWait()&&(!owner.getCurrentEquippedItem().isEmpty());
	}

	@Override
	public boolean shouldBlock(String pMode) {
		return !owner.getCurrentEquippedItem().isEmpty();
	}

	/**
	 * 一定時間後に範囲対象外とするブロックを管理する
	 */
	private checkBlockBlackListManager checkBlockManager = new checkBlockBlackListManager();
	
	@Override
	public boolean checkBlock(String pMode, int px, int py, int pz) {

		//処理対象外の場合は強制でfalse
		if (checkBlockManager.isBlackList(px, py, pz)) return false;

		boolean ret = checkBlockProc(pMode, px, py, pz);
		
		//処理対象外のカウントダウン
		if (ret) {
			checkBlockManager.setCountDown(px, py, pz);
		}
		
		return ret;
		
	}
	
	private boolean checkBlockProc(String pMode, int px, int py, int pz) {
		if (!super.checkBlock(pMode, px, py, pz)) return false;

		if(!VectorUtil.canMoveThrough(owner, 0.9D, px + 0.5D, py + 1.9D, pz + 0.5D, py==MathHelper.floor(owner.posY-1D), true, false)) return false;
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
		
		boolean ret = false;
		
		ItemStack curStack = owner.getCurrentEquippedItem();
		
		if (pMode.equals(mmode_Farmer)) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_FarmPorter);
			}
		}

		boolean haveNothing = !isTriggerItem(mmode_Farmer, curStack);

		if (!haveNothing && isUnfarmedLand(px,py,pz) &&
				curStack.onItemUse(owner.maidAvatar, owner.getEntityWorld(), new BlockPos(px, py, pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F) == EnumActionResult.SUCCESS) {
			owner.setSwing(10, EnumSound.NULL, false);
			owner.playLittleMaidVoiceSound(EnumSound.FARMER_FARM, true);

			/*
			if (owner.maidAvatar.capabilities.isCreativeMode) {
				lis.getCount() = li;
			}
			*/
			if (curStack.getCount() <= 0) {
				owner.maidInventory.setInventoryCurrentSlotContents(ItemStack.EMPTY);
				owner.getNextEquipItem();
			}
//			owner.getNavigator().clearPathEntity();
			ret = true;
		}
		if(isFarmedLand(px,py,pz)){
			//種を持っている
			int index = getHadSeedIndex();
			if(index != -1){
				
				int svCurrentIdx = owner.getDataWatchCurrentItem();
				owner.maidInventory.setCurrentItemIndex(index);
				
				ItemStack stack = owner.maidInventory.getStackInSlot(index);
				int li = stack.getCount();
				stack.onItemUse(owner.maidAvatar, owner.getEntityWorld(), new BlockPos(px,py,pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F);
				
				owner.maidInventory.setCurrentItemIndex(svCurrentIdx);
				
				owner.playLittleMaidVoiceSound(EnumSound.FARMER_PLANT, true);
				if (owner.maidAvatar.capabilities.isCreativeMode) {
					stack.setCount(li);
				}
				owner.setSwing(10, EnumSound.NULL, false);
				if(stack.getCount()<=0){
					owner.maidInventory.setInventorySlotContents(index, ItemStack.EMPTY);
				}

				ret = true;

			}
		}
		if(isCropGrown(px,py,pz)){
			// 収穫
			BlockPos pos = new BlockPos(px,py,pz);
			owner.getEntityWorld().destroyBlock(pos, true);
			owner.setSwing(10, EnumSound.NULL, false);
			owner.playLittleMaidVoiceSound(EnumSound.FARMER_HARVEST, true);
			owner.addMaidExperience(4f);
			executeBlock(pMode, px, py-1, pz);
			ret = true;
		}
		
		if (ret) {
			//管理対象から除外する
			this.checkBlockManager.clearPos(px, py, pz);
			return true;
		}
		
		return false;
	}

	@Override
	public void onUpdate(String pMode) {
		// TODO 自動生成されたメソッド・スタブ
		if(pMode.equals(mmode_Farmer) && ++clearCount >= 300 && owner.getNavigator().noPath()){
			try{
//				if(!owner.isWorking()){
				if(!owner.jobController.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
		}
		//一定時間ごとにリセット
		if (pMode.equals(mmode_Farmer)) {
			this.checkBlockManager.reset();
		}
		//運びモードへの切り替え判定
		if (pMode.equals(mmode_Farmer) && owner.ticksExisted % 200 == 0) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_FarmPorter);
			}
		}
	}

	@Override
	public void updateAITick(String pMode) {

	}

	protected int getHadSeedIndex(){
		for (int i=0; i < owner.maidInventory.getSizeInventory(); i++) {
			ItemStack pStack;
			if (!(pStack = owner.maidInventory.getStackInSlot(i)).isEmpty() &&
					this.isTriggerItemSeed(pStack)) {
				return i;
			}
		}
		return -1;
	}

	protected boolean isUnfarmedLand(int x, int y, int z){
		//耕されておらず、直上が空気ブロック
		//近くに水があるときにとりあえず耕す用
		Block b = owner.getEntityWorld().getBlockState(new BlockPos(x,y,z)).getBlock();
		return (Block.isEqualTo(b, Blocks.DIRT)||Block.isEqualTo(b, Blocks.GRASS))&&
				owner.getEntityWorld().isAirBlock(new BlockPos(x,y+1,z)) && isBlockWatered(x, y, z);
	}

	protected boolean isFarmedLand(int x, int y, int z){
		//耕されていて、直上が空気ブロック
		IBlockState state = owner.getEntityWorld().getBlockState(new BlockPos(x,y,z));
		if(state.getBlock() instanceof BlockFarmland){
			return owner.getEntityWorld().isAirBlock(new BlockPos(x,y+1,z));
		}
		return false;
	}

	protected boolean isCropGrown(int x, int y, int z){
		BlockPos position = new BlockPos(x, y, z);
		IBlockState state = owner.getEntityWorld().getBlockState(position);
		Block block = state.getBlock();

		if(block instanceof BlockCrops){
			// Max age -> Cannot glow(#34)
			return !((BlockCrops)block).canGrow(owner.getEntityWorld(), position, state, owner.getEntityWorld().isRemote);
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	protected boolean isBlockWatered(int x, int y, int z){
		// 雨天時は検索範囲を制限
		//boolean flag = owner.getEntityWorld().isRaining();
		BlockPos pos = new BlockPos(x,y,z);
		Iterator iterator = BlockPos.getAllInBoxMutable(pos.add(-WATER_RADIUS, 0, -WATER_RADIUS),
				pos.add(WATER_RADIUS, 1, WATER_RADIUS)).iterator();
		BlockPos.MutableBlockPos mutableblockpos;

		//IBlockState iState;
		do
		{
			if (!iterator.hasNext())
			{
				return false;
			}

			mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
		}
		//while ((iState = owner.getEntityWorld().getBlockState(mutableblockpos)).getMaterial() != Material.WATER);
		while ((owner.getEntityWorld().getBlockState(mutableblockpos)).getMaterial() != Material.WATER);

		return true;
	}
	
	
	/**
	 * ブロック操作系の職業メイドさんでブラックリストブロックを管理する
	 */
	public static class checkBlockBlackListManager {
		
		//作業対象外とするまでの時間（Tickではないみたい）
		private final int graceTime = 40;
		
		//作業対象外座標のリセット時間(600秒)
		private final int allResetTimeTick = 12000;
		
		/**
		 * 一定時間にリセットする
		 */
		private Map<BlockPos, Integer> checkBlockBlackList = new HashMap<>();
		
		/**
		 * ブラックリストをリセットタイマー
		 */
		private int resetCountTimer = allResetTimeTick;
		
		/**
		 * 対象の座標が処理対象外か確認する
		 */
		public boolean isBlackList(int x, int y, int z) {
			BlockPos pos = new BlockPos(x, y, z);
			if (checkBlockBlackList.containsKey(pos)) {
				Integer count = checkBlockBlackList.get(pos);
				if (count <= 0) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * ブラックリスト用のカウントダウン
		 * 0になるとisBlackListがtrueになる
		 */
		public void setCountDown(int x, int y, int z) {
			BlockPos pos = new BlockPos(x, y, z);
			if (!checkBlockBlackList.containsKey(pos)) {
				checkBlockBlackList.put(pos, graceTime);
			}
			if (checkBlockBlackList.containsKey(pos)) {
				Integer count = checkBlockBlackList.get(pos);
				count--;
				checkBlockBlackList.put(pos, count);
				if (count <= 0) {
					checkBlockBlackList.put(pos, 0);
				}
			}
		}
		
		/**
		 * 実行した場合に一旦クリアする
		 */
		public void clearPos(int x, int y, int z) {
			BlockPos pos = new BlockPos(x, y, z);
			if (checkBlockBlackList.containsKey(pos)) {
				checkBlockBlackList.remove(pos);
			}
		}
		
		/**
		 * 初期化する
		 */
		public void reset() {
			this.resetCountTimer--;
			if (0 >= resetCountTimer) {
				checkBlockBlackList.clear();
				resetCountTimer = allResetTimeTick;
			}
		}
	}
	
	@Override
	public boolean isCancelPutChestItemStack(String pMode, ItemStack stack, int slotIndedx) {
		
		String mode = pMode;
		if (EntityMode_Basic.mmode_FarmPorter.equals(pMode)) {
			mode = mmode_Farmer;
		}
		
		//農家メイドさんの場合の判定
		if (mmode_Farmer.equals(mode)) {
			
			//13番目のスロットまで
			//種はのこす
			if(slotIndedx < 9 && this.isTriggerItemSeed(stack)) {
				return true;
			}
		}
		
		return this.isTriggerItem(mode, stack);
	}
}
