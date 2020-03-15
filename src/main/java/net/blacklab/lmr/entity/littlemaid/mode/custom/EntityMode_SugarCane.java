package net.blacklab.lmr.entity.littlemaid.mode.custom;

import java.util.HashMap;
import java.util.Map;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Farmer.checkBlockBlackListManager;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTrigger.Status;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTriggerRegisterHelper;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * サトウキビメイドさん
 * サトウキビを自給自足する
 * @author computer
 *
 */
public class EntityMode_SugarCane extends EntityModeBase {

	public static final String mode_SugarCane = "SugarCane";
	public static final String trigger_SugarCane = "SugarCane:Trigger";
	
	public static final int WATER_RADIUS = 4;
	private int clearCount = 0;

	/**
	 * コンストラクタ
	 * @param pEntity
	 */
	public EntityMode_SugarCane(EntityLittleMaid pEntity) {
		super(pEntity);
	}

	/**
	 * 初期化処理
	 * モード切替用のトリガーを設定する
	 * トリガーで設定することで後付で職業アイテムを設定することができる
	 */
	@Override
	public void init() {
		
		//サトウキビ農家はサトウキビで判断
		Map<Item, Status> trigger = new HashMap<>();
		trigger.put(Items.REEDS, Status.TRIGGER);
		
		ModeTriggerRegisterHelper.register(mode_SugarCane, trigger_SugarCane, trigger);
	}

	/**
	 * モード判断の優先度設定
	 */
	@Override
	public int priority() {
		return 6400;
	}

	/**
	 * 初期化
	 * メイドモードの設定を行う
	 */
	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;

		//サトウキビ農家
		owner.addMaidMode(mode_SugarCane, ltasks);
	}

	/**
	 * サトウキビ農家転職判定
	 */
	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mode_SugarCane, litemstack)) {
				owner.setMaidMode(mode_SugarCane);
				//進捗があったらここに設定する
				
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
		case mode_SugarCane:
			return owner.getModeTrigger().isTriggerable(trigger_SugarCane, par1ItemStack);
		}
		return super.isTriggerItem(pMode, par1ItemStack);
	}

	/**
	 * 転職の際のAI設定などを実行
	 */
	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mode_SugarCane :
			//戦闘系AIをOFF
			owner.setBloodsuck(false);
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(false);
			return true;
		}
		return false;
	}

	/**
	 * 次のアイテム？
	 * 手持ちのアイテムが尽きた場合の判定処理を行う？
	 * TODO
	 */
	@Override
	public int getNextEquipItem(String pMode) {
		int li;
		if ((li = super.getNextEquipItem(pMode)) >= 0) {
			return InventoryLittleMaid.handInventoryOffset;
		}

		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mode_SugarCane :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;
				// サトウキビ
				if (isTriggerItem(mode_SugarCane, litemstack)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}

	/**
	 * 何に使っているか不明
	 * TODO
	 */
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		if(pItemStack.isEmpty()) return false;
		return true;
	}

	/**
	 * checkBlockの実行判定
	 * trueの場合は処理が実行される
	 */
	@Override
	public boolean isSearchBlock() {
		return (!owner.isMaidWait()) && (!owner.getCurrentEquippedItem().isEmpty());
	}
	
	/**
	 * 一定時間後に範囲対象外とするブロックを管理する
	 */
	private checkBlockBlackListManager checkBlockManager = new checkBlockBlackListManager();
	
	/**
	 * サトウキビを植えることができる場所を探す
	 */
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
	/**
	 * サトウキビを植えることができる場所を探す
	 */
	private boolean checkBlockProc(String pMode, int px, int py, int pz) {
		
		if (!super.checkBlock(pMode, px, py, pz)) return false;

		//メイドさんが移動できる場所か判断
		//if(!VectorUtil.canMoveThrough(owner, 0.9D, px + 0.5D, py + 1.9D, pz + 0.5D, py==MathHelper.floor(owner.posY-1D), true, false)) return false;
		
		//サトウキビを植えれるかを判断
		if (isPlantingReeds(px, py, pz)) return true;
		
		//サトウキビを収穫できるかの判断
		if (isHarvestingReeds(px, py, pz)) return true;
		
		
		//5秒に1回判断する
		//メイドモードの切り替え
		//if (owner.ticksExisted % 100 == 0) {
		//	if (pMode.equals(mode_SugarCane)) {
		//		if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
		//		if(owner.maidInventory.getFirstEmptyStack() < 0){
		//			owner.setMaidMode(EntityMode_Basic.mmode_SugarCanePorter);
		//		}
		//	}
		//}
		
		//サトウキビの収穫を判断する
		return false;

	}
	
	/**
	 * executeBlockの実行判定
	 * trueの場合は処理が実行される
	 */
	@Override
	public boolean shouldBlock(String pMode) {
		return !owner.getCurrentEquippedItem().isEmpty();
	}

	/**
	 * ブロックに対してのアクション
	 * 成功した場合はtrueを返す
	 */
	@Override
	public boolean executeBlock(String pMode, int px, int py, int pz) {
		ItemStack curStack = owner.getCurrentEquippedItem();

		//メイドモードの切り替え
		if (pMode.equals(mode_SugarCane)) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_SugarCanePorter);
			}
		}
		
		//トリガーアイテムの判断
		boolean haveNothing = !isTriggerItem(mode_SugarCane, curStack);

		//サトウキビを植えれるか判断
		if (!haveNothing && isPlantingReeds(px, py, pz)) {
			
			//アイテムの擬似使用してサトウキビの植え付け判断
			EnumActionResult useResult = curStack.onItemUse(owner.maidAvatar, owner.getEntityWorld(), new BlockPos(px, py, pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F); 
			if (useResult == EnumActionResult.SUCCESS) {
				//アニメーション
				owner.setSwing(10, EnumSound.Null, false);
				owner.playLittleMaidSound(EnumSound.farmer_farm, false);
				
				//手持ちを切り替える
				if (curStack.isEmpty()) {
					owner.getNextEquipItem();
				}
				
				//管理対象から除外する
				this.checkBlockManager.clearPos(px, py, pz);
				
				return true;
			}
		}
		
		//サトウキビを収穫できるか判断
		if (isHarvestingReeds(px, py, pz)) {
			//上のサトウキビを破壊する
			BlockPos pos = new BlockPos(px,py,pz).up();
			owner.getEntityWorld().destroyBlock(pos, true);
			owner.setSwing(10, EnumSound.Null, false);
			owner.playLittleMaidSound(EnumSound.farmer_harvest, false);
			owner.addMaidExperience(4f);
			executeBlock(pMode, px, py-1, pz);
			
			//管理対象から除外する
			this.checkBlockManager.clearPos(px, py, pz);
			
			return true;
		}
		
		//管理対象から除外する
		this.checkBlockManager.clearPos(px, py, pz);
		
		return false;
	}

	/**
	 * onUpdate
	 */
	@Override
	public void onUpdate(String pMode) {
		if(pMode.equals(mode_SugarCane) && ++clearCount >= 300 && owner.getNavigator().noPath()){
			try{
				if(!owner.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
		}
		//一定時間ごとにリセット
		if (pMode.equals(mode_SugarCane)) {
			this.checkBlockManager.reset();
		}
		
		//運びモードへの切り替え判定
		if (pMode.equals(mode_SugarCane) && owner.ticksExisted % 600 == 0) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_SugarCanePorter);
			}
		}
	}

	@Override
	public void updateAITick(String pMode) {

	}

	/**
	 * 仮設定
	 * @return
	 */
	protected int getHadSeedIndex(){
		for (int i=0; i < owner.maidInventory.getSizeInventory(); i++) {
			ItemStack pStack;
			if (!(pStack = owner.maidInventory.getStackInSlot(i)).isEmpty() &&
					isTriggerItem(mode_SugarCane, pStack)) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * サトウキビを植えれるか判断する
	 * @return
	 */
	private boolean isPlantingReeds(int px, int py, int pz) {
		
		BlockPos pos = new BlockPos(px, py, pz);
		
		//サトウキビを設置可能かつ設置場所が空気ブロックであること
		if (Blocks.REEDS.canPlaceBlockAt(owner.world, pos)
				&& owner.world.getBlockState(pos).getMaterial() == Material.AIR) {
			//設置地点がサトウキビの場合は失敗と判断
			IBlockState state = owner.world.getBlockState(pos);
			IBlockState downState = owner.world.getBlockState(pos.down());
			//サトウキビの場合は何もしない
			if (!(state.getBlock() instanceof BlockReed)
					&& !(downState.getBlock() instanceof BlockReed)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * サトウキビの収穫の判断をする
	 * @return
	 */
	private boolean isHarvestingReeds(int px, int py, int pz) {
		BlockPos pos = new BlockPos(px, py, pz);
		IBlockState state = owner.world.getBlockState(pos);
		IBlockState upState = owner.world.getBlockState(pos.up());
		
		//対象とその下がサトウキビの場合は収穫対象とする
		if (state.getBlock() instanceof BlockReed
				&& upState.getBlock() instanceof BlockReed) {
			return true;
		}
		return false;
	}
}
