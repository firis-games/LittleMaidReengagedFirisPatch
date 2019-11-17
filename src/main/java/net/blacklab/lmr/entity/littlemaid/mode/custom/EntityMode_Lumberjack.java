package net.blacklab.lmr.entity.littlemaid.mode.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTrigger;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
		ModeTrigger.registerTrigger(trigger_Lumberjack, new HashMap<>());
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
		case trigger_Lumberjack:
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
			if (isTriggerItem(trigger_Lumberjack, litemstack)) {
				owner.setMaidMode(mode_Lumberjack);
				//進捗があったらここに設定する
				
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
				if (this.isTriggerItem(trigger_Lumberjack, litemstack)) {
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
				owner.setMaidMode(EntityMode_Basic.mmode_SugarCanePorter);
			}
		}
		
		//トリガーアイテムの判断
		boolean haveNothing = !this.isTriggerItem(trigger_Lumberjack, curStack);

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
				owner.setSwing(10, EnumSound.Null, false);
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
				
				owner.playLittleMaidSound(EnumSound.farmer_harvest, false);
				owner.addMaidExperience(0.5f * (float)this.fellingBlockPosList.size());
				
				//関連パラメータをリセット
				this.isFelling = false;
				this.fellingTime = 0;
				this.fellingBlockPosList = null;
				this.fellingLeafBlockPosList = null;
				
				//植えれる場合は苗木を植える
				int saplingIndex = this.getSaplingItemStackIndex();
				if (saplingIndex >= 0) {
					int svCurrentIdx = owner.maidInventory.currentItem;
					owner.maidInventory.currentItem = saplingIndex;
					ItemStack saplingStack = owner.maidInventory.getStackInSlot(saplingIndex);
					EnumActionResult useResult = saplingStack.onItemUse(owner.maidAvatar, owner.getEntityWorld(), new BlockPos(px, py, pz), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 1.0F, 0.5F);
					owner.maidInventory.currentItem = svCurrentIdx;
					if (useResult == EnumActionResult.SUCCESS) {
						owner.playLittleMaidSound(EnumSound.farmer_farm, false);
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
				if(!owner.isWorking()){
					if(owner.aiCollectItem.shouldExecute()) owner.aiCollectItem.updateTask();
				}
			}catch(NullPointerException e){}
			clearCount=0;
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
					this.isTriggerItem(trigger_Lumberjack, pStack)) {
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
		
		if (this.isLog(px, py, pz)
				&& this.isPlanting(px, py - 1, pz)) {
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
		
		//オフハンドアイテム
		ItemStack logStack = owner.getHeldItemOffhand();
		
		//対象が原木を継承したブロックであること
		if (logStack.isEmpty()) {
			if (state.getBlock() instanceof BlockLog) {
				return true;
			}
		} else {
			int meta = state.getBlock().getMetaFromState(state);
			ItemStack metaStack = new ItemStack(state.getBlock(), 1, meta);
			if (metaStack.getItem() == logStack.getItem()
					&& metaStack.getMetadata() == logStack.getMetadata()) {
				return true;
			}
		}
		return false;
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
		for (int i = 0; i < owner.maidInventory.getSizeInventory(); i++) {
			ItemStack stack = owner.maidInventory.getStackInSlot(i);
			//決めうちで判断
			if (!stack.isEmpty()) {
				if (Block.getBlockFromItem(stack.getItem()) instanceof BlockSapling) {
					return i;
				}
			}
		}
		return -1;
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
		
		return false;
	}
	
	/**
	 * 破壊対象のブロックを変数へセットする
	 * 葉ブロックも考慮する
	 */
	private void setFellingBlock(int px, int py, int pz) {
		this.fellingBlockPosList = new ArrayList<>();
		this.fellingLeafBlockPosList = new ArrayList<>();
		
		//伐採範囲をサーチ(暫定)
		BlockPos basePos = new BlockPos(px, py, pz);
		for (BlockPos pos : BlockPos.getAllInBox(basePos.up(20).north(7).west(7), 
				basePos.down(2).south(7).east(7))) {
			//原木
			if (this.isLog(pos.getX(), pos.getY(), pos.getZ())) {
				fellingBlockPosList.add(pos);
			//葉
			} else if (this.isLeaf(pos.getX(), pos.getY(), pos.getZ())) {
				fellingLeafBlockPosList.add(pos);
			}
		}
	}
}
