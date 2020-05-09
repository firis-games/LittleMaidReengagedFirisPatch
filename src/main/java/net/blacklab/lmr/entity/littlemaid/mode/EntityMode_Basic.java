package net.blacklab.lmr.entity.littlemaid.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.api.event.EventLMRE;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.ai.motion.EntityAILMWildWatchClosest;
import net.blacklab.lmr.entity.littlemaid.ai.target.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.littlemaid.mode.custom.EntityMode_Angler;
import net.blacklab.lmr.entity.littlemaid.mode.custom.EntityMode_Lumberjack;
import net.blacklab.lmr.entity.littlemaid.mode.custom.EntityMode_SugarCane;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;

/**
 * 野生モード
 * 通常モード
 * 農家チェスト格納モード
 *　サトウキビ農家格納モード
 *　木こり格納モード
 */
public class EntityMode_Basic extends EntityModeBlockBase {

	public static final String mmode_Wild			= "Wild";
	public static final String mmode_Escort			= "Escort";
	public static final String mmode_FarmPorter		= "FarmPort";
	public static final String mmode_SugarCanePorter= "SugarCanePort";
	public static final String mmode_LumberjackPorter= "LumberjackPort";
	public static final String mmode_RipperPorter   = "ShearerPort";
	public static final String mmode_AnglerPorter   = "AnglerPort";
	
	//porterモード -> 戻す先のモード
	public static final Map<String, String> mmode_list_ModePorter = initMmodeListModePorter();
	public static Map<String, String> initMmodeListModePorter() {
		Map<String, String> porterMap = new HashMap<>();
		
		//自由行動中
		porterMap.put(mmode_Escort, mmode_Escort);
		//農家メイドさん
		porterMap.put(mmode_FarmPorter, EntityMode_Farmer.mmode_Farmer);
		//サトウキビ農家メイドさん
		porterMap.put(mmode_SugarCanePorter, EntityMode_SugarCane.mode_SugarCane);
		//木こりメイドさん
		porterMap.put(mmode_LumberjackPorter, EntityMode_Lumberjack.mode_Lumberjack);
		//毛刈りメイドさん
		porterMap.put(mmode_RipperPorter, EntityMode_Shearer.mmode_Ripper);
		//釣り師メイドさん
		porterMap.put(mmode_AnglerPorter, EntityMode_Angler.mode_Angler);
		
		return porterMap;
	}

	private IInventory myInventory;
	private IInventory myChest;
	private List<IInventory> fusedTiles;
	private double lastdistance;
	private int maidSearchCount;

	/**
	 * Wild, Escorter
	 */
	public EntityMode_Basic(EntityLittleMaid pEntity) {
		super(pEntity);
		fusedTiles = new ArrayList<IInventory>();
//		myTile = null;
	}

	@Override
	public int priority() {
		return 9000;
	}

	@Override
	public void init() {
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Wild
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(null);
		ltasks[1] = new EntityAITasks(null);

		ltasks[0].addTask(1, owner.aiSwiming);
		ltasks[0].addTask(2, owner.aiAttack);
		ltasks[0].addTask(3, owner.aiPanic);
		ltasks[0].addTask(4, owner.aiBegMove);
		ltasks[0].addTask(4, owner.aiBeg);
//		ltasks[0].addTask(5, owner.aiRestrictRain);
		ltasks[0].addTask(6, owner.aiFreeRain);
//        ltasks[0].addTask(4, new EntityAIMoveIndoors(this));
//		ltasks[0].addTask(7, owner.aiCloseDoor);
//		ltasks[0].addTask(8, owner.aiOpenDoor);
		ltasks[0].addTask(9, owner.aiCollectItem);
		ltasks[0].addTask(10, new EntityAILeapAtTarget(owner, 0.3F));
		ltasks[0].addTask(11, owner.aiWander);
		ltasks[0].addTask(12, new EntityAILMWildWatchClosest(owner, EntityLivingBase.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAILMWildWatchClosest(owner, EntityLittleMaid.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAILMWildWatchClosest(owner, EntityPlayer.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAILookIdle(owner));

		ltasks[1].addTask(1, new EntityAILMHurtByTarget(owner, false));

		owner.addMaidMode(mmode_Wild, ltasks);

		// Escorter:0x0001
		ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
		owner.addMaidMode(mmode_Escort, ltasks);
		
		//各職業の運び屋メイドさんモードを追加
		for (String porterMode : mmode_list_ModePorter.keySet()) {
			owner.addMaidMode(porterMode, ltasks);
		}		
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		
		//通常モードへ移行
		owner.setMaidMode(mmode_Escort);
		
		return true;
	}

	@Override
	public boolean setMode(String pMode) {
		switch (pMode) {
		case mmode_Wild :
			if (owner.isContractEX()) {
				return setMode(mmode_Escort);
			}
			owner.setFreedom(true);
//			owner.aiWander.setEnable(true);
			return true;
		case "Escorter":
			owner.setMaidMode(mmode_Escort);
			break;
		case mmode_Escort :
//			owner.aiAvoidPlayer.setEnable(false);
			for (int li = 0; li < owner.mstatSwingStatus.length; li++) {
				owner.setEquipItem(li, -1);
			}
			return true;
		}
		
		//運び屋モードの判定
		if (mmode_list_ModePorter.containsKey(pMode)) {
			return true;
		}
		
//		owner.getNavigator().clearPathEntity()
		return false;
	}

	@Override
	public int getNextEquipItem(String pMode) {
		// Use mainhand slot prior
		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return !owner.isMaidWait();
	}

	@Override
	public boolean isSearchBlock() {
		
		//運び屋モードの場合はtrue
		boolean isPorterMode = mmode_list_ModePorter.containsKey(owner.getMaidModeString());
		
		//各運び屋モード かつ 自由行動 かつ 待機状態ではない かつ インベントリの空きがない場合は継続
		if (isPorterMode
				&& owner.isFreedom() && !owner.isMaidWait() &&
				owner.maidInventory.getFirstEmptyStack() == -1) {
			// 対象をまだ見つけていないときは検索を行う。
			fDistance = 100F;
			return myInventory == null;
		}
//		clearMy();
//		fusedTiles.clear();
		return false;
	}

	@Override
	public boolean shouldBlock(String pMode) {
		return myInventory instanceof TileEntity;
	}

	@Override
	public boolean checkBlock(String pMode, int px, int py, int pz) {
		TileEntity ltile = owner.getEntityWorld().getTileEntity(new BlockPos(px, py, pz));
		if (!(ltile instanceof IInventory)) {
			return false;
		}
		if (((IInventory)ltile).getSizeInventory() < 18) {
			// インベントリのサイズが１８以下なら対象としない。
			return false;
		}

		// 世界のメイドから
		if (checkWorldMaid(ltile)) return false;
		// 使用済みチェック
		if (fusedTiles.contains((IInventory)ltile)) {
			// 既に通り過ぎた場所よッ！
			return false;
		}

		double ldis = this.getDistanceTilePosSq(ltile);
		if (fDistance > ldis) {
			myInventory = (IInventory)ltile;
			fDistance = ldis;
		}
		return false;
	}

	@Override
	public boolean overlooksBlock(String pMode) {
		// チェストカートの検索
		List<TileEntity> list = owner.getEntityWorld().loadedTileEntityList;
		for (TileEntity lentity : list) {
			if(!(lentity instanceof TileEntityChest)) continue;
			if (!fusedTiles.contains((IInventory)lentity)) {
				if (((IInventory)lentity).getSizeInventory() < 18) {
					// インベントリが一定サイズ以下はスキップ
					continue;
				}
				double lr = lentity.getDistanceSq(owner.posX,owner.posY,owner.posZ);
				// 見える位置にある最も近い調べていないカートチェスト

				if (fDistance > lr/* && owner.getEntitySenses().canSee(lentity)*/) {
					myInventory = (IInventory)lentity;
					fDistance = lr;
				}
			}
		}
		lastdistance = -1;
		myChest = null;
		maidSearchCount = 0;
		if (myInventory instanceof TileEntity) {
			owner.jobController.setTilePos((TileEntity)myInventory);
			return myInventory != null;
		}
		//1.8検討
		//owner.setTarget((Entity)myInventory);
		return false;
	}

	@Override
	public void resetBlock(String pMode) {
		clearMy();
//		fusedTiles.clear();
	}

	protected void clearMy() {
		myInventory = null;
		if (myChest != null) {
			myChest.closeInventory(owner.maidAvatar);
			myChest = null;
		}
		owner.jobController.clearTilePos();
		owner.setAttackTarget(null);
	}

	@Override
	public boolean executeBlock(String pMode, int px, int py, int pz) {
//		isMaidChaseWait = true;
		if (myInventory instanceof TileEntityChest) {
			// ブロック系のチェスト
			TileEntityChest lchest = (TileEntityChest)myInventory;
			if (!lchest.isInvalid()) {
				// 使用直前に可視判定
				//if (MMM_Helper.canBlockBeSeen(owner, lchest.getPos().getX(), lchest.getPos().getY(), lchest.getPos().getZ(), false, true, false)) {
					if (myChest == null) {
						getChest();
						if (myChest != null) {
							myChest.openInventory(owner.maidAvatar);
						} else {
							// 開かないチェスト
							myInventory = null;
						}
					}
					// チェストに収納
//					owner.setWorking(true);
					owner.jobController.setStartWorking();
					putChest(pMode);
					return true;
				//} else {
				//	// 見失った
				//	clearMy();
				//}
			}
			// Tileの消失
			clearMy();
		} else {
			// 想定外のインベントリ
			if (myInventory != null) {
				fusedTiles.add(myInventory);
			}
			clearMy();
		}
		return false;
	}

	@Override
	public boolean outrangeBlock(String pMode, int pX, int pY, int pZ) {
		// チェストまでのパスを作る
		boolean lf = false;
		if (!owner.isMaidWaitEx()) {
			double distance;
			if (myInventory instanceof TileEntity) {
				distance = owner.jobController.getDistanceTilePos();
				if (distance == lastdistance) {
					// TODO:現状無意味
					// 移動が固まらないように乱数加速
					LittleMaidReengaged.Debug("Assert.");
					lf = true;
				} else {
					lf = CommonHelper.setPathToTile(owner, (TileEntity)myInventory, false);
				}
			} else {
				distance = 0;
			}
			lastdistance = distance;
			// レンジ外のチェストは閉じる
			if (myChest != null) {
				myChest.closeInventory(owner.maidAvatar);
				myChest = null;
			}
		}
		return lf;
	}

	@Override
	public void farrangeBlock() {
		super.farrangeBlock();
		clearMy();
	}


	protected boolean getChest() {
		// チェストを獲得
		if (myInventory == null) {
			return false;
		}
		// 検索済みにスタック
		fusedTiles.add(myInventory);
		if (myInventory instanceof TileEntityChest) {
			TileEntityChest lchest = (TileEntityChest)myInventory;
			if (!lchest.adjacentChestChecked) {
				lchest.checkForAdjacentChests();
			}
			fusedTiles.add(lchest.adjacentChestXNeg);
			fusedTiles.add(lchest.adjacentChestXPos);
			fusedTiles.add(lchest.adjacentChestZNeg);
			fusedTiles.add(lchest.adjacentChestZPos);
		}

		TileEntity ltile = (TileEntity)myInventory;
		Block lblock = owner.getEntityWorld().getBlockState(ltile.getPos()).getBlock();
		myChest = myInventory;
		if (lblock instanceof BlockChest) {
			myChest = ((BlockChest)lblock).getLockableContainer(owner.getEntityWorld(), ltile.getPos());
		}

		return myChest != null;
	}

	protected void putChest(String pMode) {
		// チェストに近接
		if (owner.getSwingStatusDominant().canAttack() && myChest != null) {
			// 砂糖、時計、被っているヘルム以外のアイテムを突っ込む
			ItemStack is;
			LittleMaidReengaged.Debug(String.format("getChest:%d", maidSearchCount));
			if (mmode_LumberjackPorter.equals(pMode)) {
				//木こりメイドさんのアイテム格納処理
				boolean isFirstSapling = false;
				is = owner.maidInventory.getStackInSlot(maidSearchCount);
				
				//苗木判定
				for (int idx = 0; idx < maidSearchCount; idx++) {
					ItemStack sapling = owner.maidInventory.getStackInSlot(idx);
					if (EntityMode_Lumberjack.isSaplingItem(sapling.getItem())) {
						isFirstSapling = true;
						break;
					}
				}
				
				for (; maidSearchCount < InventoryLittleMaid.maxInventorySize; maidSearchCount++) {
					is = owner.maidInventory.getStackInSlot(maidSearchCount);
					//苗木判定
					if (!isFirstSapling && !is.isEmpty() && EntityMode_Lumberjack.isSaplingItem(is.getItem())) {
						//1回目の苗木の場合はスキップ
						isFirstSapling = true;
						continue;
					}
					//空じゃない場合は続き
					if (!is.isEmpty()) {
						break;
					}
				}
			} else {
				while ((is = owner.maidInventory.getStackInSlot(maidSearchCount)).isEmpty() && maidSearchCount < InventoryLittleMaid.maxInventorySize) {
					maidSearchCount++;
				}
			}
			
			if (!is.isEmpty()){
				EventLMRE.ItemPutChestEvent event = new EventLMRE.ItemPutChestEvent(owner,myChest,is,maidSearchCount);
				if(!VEventBus.instance.post(event)){
//					mod_littleMaidMob.Debug("getchest2.");
					boolean f = false;
					for (int j = 0; j < myChest.getSizeInventory() && is.getCount() > 0; j++)
					{
						ItemStack isc = myChest.getStackInSlot(j);
						if (isc.isEmpty())
						{
//							mod_littleMaidMob.Debug(String.format("%s -> NULL", is.getItemName()));
							myChest.setInventorySlotContents(j, is.copy());
							is.setCount(0);
							f = true;
							break;
						}
						else if (isc.isStackable() && isc.isItemEqual(is))
						{
//							mod_littleMaidMob.Debug(String.format("%s -> %s", is.getItemName(), isc.getItemName()));
							f = true;
							isc.setCount(isc.getCount() + is.getCount());
							if (isc.getCount() > isc.getMaxStackSize())
							{
								is.setCount(isc.getCount() - isc.getMaxStackSize());
								isc.setCount(isc.getMaxStackSize());
							} else {
								is.setCount(0);
								break;
							}
						}
					}
					if (is.getCount() <= 0) {
						owner.maidInventory.setInventorySlotContents(maidSearchCount, ItemStack.EMPTY);
					}
					if (f) {
						owner.playSound("entity.item.pickup");
						owner.setSwing(2, EnumSound.Null, false);
					}
				}
			}
//			mod_littleMaidMob.Debug(String.format("getchest3:%d", maidSearchCount));
			if (++maidSearchCount >= InventoryLittleMaid.maxInventorySize) {
				// 検索済みの対象をスタック
//				serchedChest.add(myChest);
				clearMy();
				lastdistance = 0D;
				LittleMaidReengaged.Debug("endChest.");
				// 空きができたら捜索終了
				if (owner.maidInventory.getFirstEmptyStack() > -1) {
					LittleMaidReengaged.Debug("Search clear.");
					fusedTiles.clear();
//					if(owner.getMaidModeInt()==mmode_FarmerChest){
//						owner.setMaidMode("Farmer");
//					}
				}
			}
		}
	}

	@Override
	public boolean attackEntityAsMob(String pMode, Entity pEntity) {
		if (pEntity == myInventory) {
			// チェスト付カートとか
			Entity lentity = (Entity)myInventory;
			if (!lentity.isDead) {
				if (owner.getDistanceSq(lentity) < 5D)	{
					owner.getNavigator().clearPath();
					if (myChest == null) {
						myChest = (IInventory)lentity;
						fusedTiles.add(myChest);
						myChest.openInventory(owner.maidAvatar);
					}
					if (myChest != null) {
						owner.getLookHelper().setLookPositionWithEntity(lentity, 30F, 40F);
					}
					// チェストに収納
					putChest(pMode);
				} else {
					// チェストまでのパスを作る
					if (!owner.isMaidWaitEx()) {
						double distance = owner.getDistanceSq(lentity);
						if (distance == lastdistance) {
							// TODO: 現状無意味
							LittleMaidReengaged.Debug("Assert.");
						} else {
							owner.getNavigator().tryMoveToXYZ(lentity.posX, lentity.posY, lentity.posZ, 1.0F);
						}
						lastdistance = distance;
//						mod_littleMaidMob.Debug(String.format("Rerute:%b", hasPath()));
					}
					if (myChest != null) {
						myChest.closeInventory(owner.maidAvatar);
						myChest = null;
					}
				}
			} else {
				// Entityの死亡
				clearMy();
			}
			return true;
		}
		// ターゲットが変わってる？
		clearMy();
		return true;
	}

	@Override
	public boolean isChangeTartget(Entity pTarget) {
		if (pTarget instanceof IInventory) {
			return false;
		}
		return super.isChangeTartget(pTarget);
	}

	@Override
	public boolean preInteract(EntityPlayer pentityplayer, ItemStack pitemstack) {
		// しゃがみ時は処理無効
		if (pentityplayer.isSneaking()) {
			return false;
		}
		if (owner.isContract()) {
			// 契約状態
			if (owner.isEntityAlive() && owner.isMaidContractOwner(pentityplayer)) {
				if (!pitemstack.isEmpty()) {
					// 追加分の処理
					// TODO いる？
					//owner.setPathToEntity(null);
					if (owner.isRemainsContract()) {
						if (pitemstack.getItem() instanceof ItemAppleGold) {
							// ゴールデンアッポー
							if(!owner.getEntityWorld().isRemote) {
								((ItemAppleGold)pitemstack.getItem()).onItemUseFinish(pitemstack, owner.getEntityWorld(), owner.maidAvatar);
							}
							return true;
						}
						else if (pitemstack.getItem() instanceof ItemBucketMilk && !owner.getActivePotionEffects().isEmpty()) {
							// 牛乳に相談だ
							if(!owner.getEntityWorld().isRemote) {
								owner.clearActivePotions();
							}
							// TODO これバケツなくなるで
							pitemstack.splitStack(1);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void updateAITick(String pMode) {
		
		//運び屋モードの場合はtrue
		boolean isPorterMode = mmode_list_ModePorter.containsKey(pMode);
		
		//運び屋モードの場合は元のモードへ戻す
		if (isPorterMode &&
				owner.maidInventory.getFirstEmptyStack() > -1 &&
//				!owner.getWorkingCount().isEnable()) {
				!owner.jobController.isWorking()) {
			
			//元の職業へ戻す
			String beforMode = mmode_list_ModePorter.get(pMode);
			owner.setMaidMode(beforMode);
			owner.getNextEquipItem();
			
			//インベントリの同期
			if (!beforMode.equals(pMode)) {
				LMRNetwork.syncLittleMaidInventory(owner);
			}
		}

		super.updateAITick(pMode);
		
		//一定時間ごとにリセット検索したチェストをリセット
		if (owner.ticksExisted % 100 == 0) fusedTiles.clear();
		
	}

}
