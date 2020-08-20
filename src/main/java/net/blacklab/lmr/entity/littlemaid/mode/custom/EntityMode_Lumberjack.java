package net.blacklab.lmr.entity.littlemaid.mode.custom;

import java.util.ArrayList;
import java.util.List;

import firis.lmlib.api.constant.EnumSound;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTriggerRegisterHelper;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * 木こりメイドさん
 * 原木をなんとかする
 * @author computer
 *
 */
public class EntityMode_Lumberjack extends EntityModeBase {

	public static final String mode_Lumberjack = "Lumberjack";
	public static final String trigger_Lumberjack = "Lumberjack:Trigger";
	
	private int clearCount = 0;
	
	/**
	 * 伐採のアニメーション管理に利用するフラグ
	 * NBTには保存しない
	 */
	private int fellingTime = 0;
	private boolean isFelling = false;
	private List<BlockPos> fellingBlockPosList = null;
	private List<BlockPos> fellingLeafBlockPosList = null;
	
	/**
	 * コンストラクタ
	 * @param pEntity
	 */
	public EntityMode_Lumberjack(EntityLittleMaid pEntity) {
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
		ModeTriggerRegisterHelper.register(mode_Lumberjack, trigger_Lumberjack);
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
		case mode_Lumberjack:
			//メインハンドが斧かつオフハンドが斧でない
			boolean ret1 = owner.getModeTrigger().isTriggerable(trigger_Lumberjack, par1ItemStack, ItemAxe.class);
			boolean ret2 = !owner.getModeTrigger().isTriggerable(trigger_Lumberjack, owner.getHeldItemOffhand(), ItemAxe.class);
			return ret1 && ret2;
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

		//木こりメイド
		owner.addMaidMode(mode_Lumberjack, ltasks);
	}

	/**
	 * 木こりメイド転職判定
	 */
	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.getHandSlotForModeChange();
		if (!litemstack.isEmpty()) {
			if (isTriggerItem(mode_Lumberjack, litemstack)) {
				owner.setMaidMode(mode_Lumberjack);
				//進捗
				AchievementsLMRE.grantAC(pentityplayer, AC.LumberJack);
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
		case mode_Lumberjack :
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
		case mode_Lumberjack :
			for (li = 0; li < owner.maidInventory.getSizeInventory() - 1; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack.isEmpty()) continue;
				// 木こり
				if (this.isTriggerItem(mode_Lumberjack, litemstack)) {
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
		
		//収穫できるかの判断
		if (isHarvesting(px, py, pz)) return true;
		
		//収穫を判断する
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
		if (pMode.equals(mode_Lumberjack)) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_LumberjackPorter);
			}
		}
		
		//トリガーアイテムの判断
		boolean haveNothing = !this.isTriggerItem(mode_Lumberjack, curStack);

		//原木を伐採できるかを判断する
		if (!haveNothing && isHarvesting(px, py, pz)) {
			
			//初回
			if (this.isFelling == false) {
				
				//伐採範囲をサーチ
				this.setFellingBlock(px, py, pz);
				
				//ブロックの数で必要な時間を加算する
				this.isFelling = true;
				this.fellingTime = fellingBlockPosList.size() * 10
						+ fellingLeafBlockPosList.size() * 1;
				
			//伐採中アニメーション
			} else if (this.fellingTime > 0) {
				//腕振りのみ
				owner.setSwing(10, EnumSound.NULL, false);
				this.fellingTime--;
				
			//伐採完了
			} else {
				//伐採完了
				for (BlockPos pos : this.fellingBlockPosList) {
					owner.getEntityWorld().destroyBlock(pos, true);
				}
				for (BlockPos pos : this.fellingLeafBlockPosList) {
					owner.getEntityWorld().destroyBlock(pos, true);
				}
				
				//ツールに伐採ダメージ
				//耐久度が足りなくても一括破壊は動かす
				//葉ブロック分は無視
				curStack.damageItem(this.fellingBlockPosList.size(), owner.maidAvatar);
				
				owner.playLittleMaidVoiceSound(EnumSound.FARMER_HARVEST, true);
				owner.addMaidExperience(0.5f * (float)this.fellingBlockPosList.size());
				
				//関連パラメータをリセット
				this.isFelling = false;
				this.fellingTime = 0;
				this.fellingBlockPosList = null;
				this.fellingLeafBlockPosList = null;
				
				//植えれる場合は苗木を植える
				int saplingIndex = this.getSaplingItemStackIndex();
				if (saplingIndex >= 0) {
					int svCurrentIdx = owner.maidInventory.getCurrentItemIndex();
					owner.maidInventory.setCurrentItemIndex(saplingIndex);
					ItemStack saplingStack = owner.maidInventory.getStackInSlot(saplingIndex);
					EnumActionResult useResult = saplingStack.onItemUse(owner.maidAvatar, owner.getEntityWorld(), new BlockPos(px, py, pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F);
					owner.maidInventory.setCurrentItemIndex(svCurrentIdx);
					if (useResult == EnumActionResult.SUCCESS) {
						owner.playLittleMaidVoiceSound(EnumSound.FARMER_FARM, true);
					}
				}
				
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
		if(pMode.equals(mode_Lumberjack) && ++clearCount >= 300 && owner.getNavigator().noPath()){
			try{
//				if(!owner.isWorking()){
				if(!owner.jobController.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
		}
		
		//運びモードへの切り替え判定
		if (pMode.equals(mode_Lumberjack) && owner.ticksExisted % 200 == 0) {
			if(owner.getAIMoveSpeed() > 0.5F) owner.setAIMoveSpeed(0.5F);
			if(owner.maidInventory.getFirstEmptyStack() < 0){
				owner.setMaidMode(EntityMode_Basic.mmode_LumberjackPorter);
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
					this.isTriggerItem(mode_Lumberjack, pStack)) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * 原木の収穫の判断をする
	 * 対象ブロックが原木かつ1つ下のブロックが苗木を植えれるブロックであること
	 * @return
	 */
	private boolean isHarvesting(int px, int py, int pz) {
		
		//原木かつ下のブロックが苗木
		if (this.isLog(px, py, pz)
				&& this.isPlanting(px, py - 1, pz)) {
			return true;
		}
		
		BlockPos maidPos = owner.getPosition();
		
		//メイドさんの一つ下の座標 かつ 原木or葉ブロック
		if (maidPos.getY() - 1 == py 
				&& (this.isLog(px, py, pz) || this.isLeaf(px, py, pz))) {
			return true;
		}
		
		//メイドさんと被ってる葉or原木
		if (maidPos.getX() == px && (maidPos.getY() == py || maidPos.getY() + 1 == py) && maidPos.getZ() == pz
				&& (this.isLog(px, py, pz) || this.isLeaf(px, py, pz))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 原木かどうかの判断を行う
	 * 固定で原木系ブロックを判断している
	 * 
	 * オフハンドにアイテムを持っている場合はそのアイテムのみ伐採する
	 * @return
	 */
	private boolean isLog(int px, int py, int pz) {
		
		BlockPos pos = new BlockPos(px, py, pz);
		IBlockState state = owner.world.getBlockState(pos);
		
		//原木ブロックチェック
		boolean ret = isLogBlock(state.getBlock());
		if (!ret) {
			return false;
		}
		
		//オフハンドアイテム
		ItemStack logStack = owner.getHeldItemOffhand();
		ret = isLogBlock(Block.getBlockFromItem(logStack.getItem()));
		
		//カスタム動作はなし
		if (!ret) {
			return true;
		}

		int meta = state.getBlock().getMetaFromState(state);
		ItemStack metaStack = new ItemStack(state.getBlock(), 1, meta);
		if (metaStack.getItem() == logStack.getItem()
				&& metaStack.getMetadata() == logStack.getMetadata()) {
			return true;
		}
		
		return false;

	}
	
	/**
	 * 対象ブロックが原木判定か確認する
	 */
	public boolean isLogBlock(Block block) {
		
		boolean ret = false;
		
		//対象が原木を継承したブロックであること
		if (block instanceof BlockLog) {
			ret = true;
		}
		
		//原木ブロック設定
		if (!ret && LMRConfig.cfg_lj_log_block_ids.contains(
				block.getRegistryName().toString())) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 苗木を植えれるか判断する
	 * 固定で土系のみ対象とする
	 * @return
	 */
	private boolean isPlanting(int px, int py, int pz) {
		
		BlockPos pos = new BlockPos(px, py, pz);
		IBlockState state = owner.world.getBlockState(pos);
		
		if (state.getBlock() == Blocks.DIRT 
				|| state.getBlock() == Blocks.GRASS) {
			return true;
		}
		return false;
	}
	
	/**
	 * メイドさんのインベントリから苗木のindexを取得する
	 * @return
	 */
	private int getSaplingItemStackIndex() {
		
		//例外処理追加
		//オフハンドに原木 or 苗木 でないものを持っている場合は
		//植林を行わない
		ItemStack offStack = owner.getHeldItemOffhand();
		boolean isOffSapling = false;
		if (!offStack.isEmpty()) {
			isOffSapling = isSaplingItem(offStack.getItem());
			if (!isLogBlock(Block.getBlockFromItem(offStack.getItem()))
					&& !isOffSapling) {
				return -1;
			}
		}
		
		for (int i = 0; i < owner.maidInventory.getSizeInventory(); i++) {
			ItemStack stack = owner.maidInventory.getStackInSlot(i);
			//決めうちで判断
			if (!stack.isEmpty()) {
				
				if (isOffSapling) {
					//カスタム苗木判定
					if (stack.getItem() == offStack.getItem()
							&& stack.getMetadata() == offStack.getMetadata()) {
						return i;
					}
				}else{
					//標準苗木判定
					if (isSaplingItem(stack.getItem())) {
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * 対象アイテムが苗木判定か確認する
	 */
	public static boolean isSaplingItem(Item item) {
		
		//nullの場合無効
		if (item == null) return false;
		
		//苗木
		if (Block.getBlockFromItem(item) instanceof BlockSapling) {
			return true;
		}
		
		//苗木アイテム判定
		if (LMRConfig.cfg_lj_sapling_item_ids.contains(
				item.getRegistryName().toString())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 葉ブロックかどうかの判断を行う
	 * 
	 * @return
	 */
	private boolean isLeaf(int px, int py, int pz) {
		
		BlockPos pos = new BlockPos(px, py, pz);
		IBlockState state = owner.world.getBlockState(pos);
		
		//対象がBlockLeavesを継承したブロックであること
		if (state.getBlock() instanceof BlockLeaves) {
			return true;
		}
		
		//葉ブロック設定
		if (LMRConfig.cfg_lj_leaf_block_ids.contains(
				state.getBlock().getRegistryName().toString())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 破壊対象のブロックを変数へセットする
	 * 葉ブロックも考慮する
	 */
	private void setFellingBlock(int px, int py, int pz) {
		this.fellingBlockPosList = new ArrayList<>();
		this.fellingLeafBlockPosList = new ArrayList<>();
		
		//伐採範囲をサーチ
		BlockPos basePos = new BlockPos(px, py, pz);
		
		//ベースブロックを追加する
		fellingBlockPosList.add(basePos);
		
		//破壊対象の原木を取得
		getAdjacentBlock(basePos, fellingBlockPosList, fellingLeafBlockPosList);
		
	}
	
	/**
	 * 隣接する原木ブロックを再帰的に取得する
	 * @param basePos
	 * @return
	 */
	private void getAdjacentBlock(BlockPos basePos, List<BlockPos> logList, List<BlockPos> leafList) {
		//256ブロック以上は処理を停止する
		if (logList.size() > 1024) return;
		if (leafList.size() > 1024) return;
		
		for (EnumFacing facing : EnumFacing.VALUES) {
			BlockPos pos = basePos.offset(facing);
			//原木判断
			if (this.isLog(pos.getX(), pos.getY(), pos.getZ())) {
				if (!logList.contains(pos)) {
					logList.add(pos);
					//再起呼び
					getAdjacentBlock(pos, logList, leafList);
				}
			} else if (this.isLeaf(pos.getX(), pos.getY(), pos.getZ())) {
				if (!leafList.contains(pos)) {
					leafList.add(pos);
					getAdjacentBlock(pos, logList, leafList);
				}
			}
		}
	}
	
	/**
	 * isTriggerItemを使う場合はisTriggerItem側で職業判定をちゃんとやっていること
	 */
	@Override
	public boolean isCancelPutChestItemStack(String pMode, ItemStack stack, int slotIndedx) {
		
		String mode = pMode;
		if (EntityMode_Basic.mmode_LumberjackPorter.equals(pMode)) {
			mode = mode_Lumberjack;
		}
		
		return this.isTriggerItem(mode, stack);
	}
	
	
	/**
	 * 有効射程距離を超えた時の処理
	 */
	@Override
	public boolean outrangeBlock(String pMode, int pX, int pY, int pZ) {
		//木こり時の特殊判定
		if (mode_Lumberjack.equals(pMode)) {
			BlockPos maidPos = owner.getPosition();
			if (maidPos.getX() == pX && (maidPos.getY() == pY || maidPos.getY() + 1 == pY) && maidPos.getZ() == pZ) {
				return true;
			}
		}
		
		return owner.getNavigator().tryMoveToXYZ(pX, pY, pZ, 1.0F);
	}
	
}
