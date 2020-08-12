package net.blacklab.lmr.entity.littlemaid.mode.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import firis.lmlib.api.constant.EnumSound;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTriggerRegisterHelper;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * 釣り人メイドさん
 * 魚とかを釣りたい
 * 
 */
public class EntityMode_Angler extends EntityModeBase {

	public static final String mode_Angler = "Angler";
	public static final String trigger_Angler = "Angler:Trigger";
	
	private int clearCount = 0;
	
	private Random rand = new Random();
	
	/**
	 * アニメーション管理に利用するフラグ
	 * NBTには保存しない
	 */
	private int progressTime = 0;
	private boolean isProgress = false;
	
	/**
	 * コンストラクタ
	 * @param pEntity
	 */
	public EntityMode_Angler(EntityLittleMaid pEntity) {
		super(pEntity);
	}

	/**
	 * 初期化処理
	 * モード切替用のトリガーを設定する
	 * トリガーで設定することで後付で職業アイテムを設定することができる
	 */
	@Override
	public void init() {
		//Classで判断する
		ModeTriggerRegisterHelper.register(mode_Angler, trigger_Angler);
	}
	
	/**
	 * トリガーアイテムを判断する
	 */
	@Override
	protected boolean isTriggerItem(String pMode, ItemStack par1ItemStack) {
		if (par1ItemStack.isEmpty()) {
			return false;
		}
		switch (pMode) {
		case mode_Angler:
			//釣り竿でトリガー確認
			return owner.getModeTrigger().isTriggerable(trigger_Angler, par1ItemStack, ItemFishingRod.class);
		}
		return super.isTriggerItem(pMode, par1ItemStack);
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

		//釣り人メイドさん
		owner.addMaidMode(mode_Angler, ltasks);
	}

	/**
	 * 釣り人メイドさん転職判定
	 */
	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mode_Angler, litemstack)) {
				owner.setMaidMode(mode_Angler);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.Angler);
				return true;
			}
		}
		return false;
	}

	/**
	 * 転職の際のAI設定などを実行
	 */
	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mode_Angler :
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
		case mode_Angler :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;
				// 木こり
				if (this.isTriggerItem(mode_Angler, litemstack)) {
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
	 * 収穫対象の原木を探す
	 */
	@Override
	public boolean checkBlock(String pMode, int px, int py, int pz) {
		
		if (!super.checkBlock(pMode, px, py, pz)) return false;
		
		//釣りできるかの判断
		if (isFishing(px, py, pz)) return true;
		
		//釣りの判断
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
		if (pMode.equals(mode_Angler)) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_AnglerPorter);
			}
		}
		
		//トリガーアイテムの判断
		boolean haveNothing = !this.isTriggerItem(mode_Angler, curStack);

		//this.isFelling = this.owner.getMaidMasterEntity().fishEntity != null;
		
		
		//釣り出来る場合
		if (!haveNothing && isFishing(px, py, pz)) {
			//初回
			if (this.isProgress == false) {
				
				//ブロックの数で必要な時間を加算する
				this.isProgress = true;

				//20秒～40秒のランダム
				this.progressTime = 20 * 20 + (this.rand.nextInt(21) * 20);
				
			//アニメーション中
			} else if (this.progressTime > 0) {
				
				//腕振りのみ
				owner.setSwing(10, EnumSound.NULL, false);
				this.progressTime--;
				
			//アニメーション完了
			} else {

				//作業完了
				//釣りあげるアイテムをドロップする
				ItemStack drop = createFishingDrop();
				InventoryHelper.spawnItemStack(this.owner.world, 
						this.owner.posX, this.owner.posY, this.owner.posZ, drop);
				
				//ツールにダメージ
				curStack.damageItem(1, owner.maidAvatar);
				
				owner.playLittleMaidVoiceSound(EnumSound.FARMER_HARVEST, true);
				owner.addMaidExperience(1.0F);
				
				//関連パラメータをリセット
				this.isProgress = false;
				this.progressTime = 0;
				
				//手持ちを切り替える
				if (curStack.isEmpty()) {
					owner.getNextEquipItem();
				}
				
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * onUpdate
	 */
	@Override
	public void onUpdate(String pMode) {
		if(pMode.equals(mode_Angler) && ++clearCount >= 300 && owner.getNavigator().noPath()){
			try{
//				if(!owner.isWorking()){
				if(!owner.jobController.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
		}
		
		//運びモードへの切り替え判定
		if (pMode.equals(mode_Angler) && owner.ticksExisted % 200 == 0) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_AnglerPorter);
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
					this.isTriggerItem(mode_Angler, pStack)) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * 釣りができるかどうかの判断をする
	 * 水源ブロックかつ周囲のブロックに固体ブロックがあること
	 * 対象座標を中心に3x3が水源ブロックの判断を行う
	 * @return
	 */
	private boolean isFishing(int px, int py, int pz) {
		
		BlockPos pos = new BlockPos(px, py, pz);
		
		//チェック対象が水源ブロック
		if (!isWaterBlock(pos)) return false;
		
		//座標を中心に3x3
		List<BlockPos> groundList = new ArrayList<>();
		groundList.add(pos);
		groundList.add(pos.north());
		groundList.add(pos.north().east());
		groundList.add(pos.east());
		groundList.add(pos.east().south());
		groundList.add(pos.south());
		groundList.add(pos.south().west());
		groundList.add(pos.west());
		groundList.add(pos.west().north());
		
		//固体チェック
		boolean ret = false;
		for (BlockPos chkPos : groundList) {
			IBlockState state = this.owner.world.getBlockState(chkPos);
			if (state.getMaterial().isSolid()) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * 対象ブロックが水源かの判断を行う
	 * @param pos
	 * @return
	 */
	private boolean isWaterBlock(BlockPos pos) {
		
		IBlockState state = owner.world.getBlockState(pos);
		Material material = state.getMaterial();
		if (material == Material.WATER 
				&& ((Integer)state.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * 移動の制御
	 */
	@Override
	public boolean outrangeBlock(String pMode, int pX, int pY, int pZ) {
		
		BlockPos posBase = new BlockPos(pX, pY, pZ);
		Iterable<BlockPos> iterablePos = BlockPos.getAllInBox(
				posBase.north(1).west(1), 
				posBase.south(1).east(1));
		
		Iterator<BlockPos> iteratorPos = iterablePos.iterator();
		while (iteratorPos.hasNext()) {
			BlockPos pos = iteratorPos.next();
			IBlockState state = this.owner.world.getBlockState(pos);
			if (state.getMaterial().isSolid()) {
				return owner.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0F);
			}
		}
		
		//標準の位置
		return owner.getNavigator().tryMoveToXYZ(pX, pY, pZ, 1.0F);
	}
	
	/**
	 * LootTableから釣りのドロップを取得する
	 * @return
	 */
	private ItemStack createFishingDrop() {

		//ワールドを取得
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
		LootContext.Builder ctxBuild = new LootContext.Builder((WorldServer) world);
		LootTable loottable = world
				.getLootTableManager()
				.getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
		
		List<ItemStack> resultList = loottable.generateLootForPools(rand, ctxBuild.build());
		
		Collections.shuffle(resultList);
		
		ItemStack result = ItemStack.EMPTY.copy();
		for(ItemStack stack : resultList) {
			result = stack;
			break;
		}
		return result;
	}
	
	/**
	 * isTriggerItemを使う場合はisTriggerItem側で職業判定をちゃんとやっていること
	 */
	@Override
	public boolean isCancelPutChestItemStack(String pMode, ItemStack stack, int slotIndedx) {
		
		String mode = pMode;
		if (EntityMode_Basic.mmode_AnglerPorter.equals(pMode)) {
			mode = mode_Angler;
		}
		
		return this.isTriggerItem(mode, stack);
	}
	
}
