package net.blacklab.lmr.entity.mode;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lib.vevent.VEventBus;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.api.event.EventLMRE;
import net.blacklab.lmr.api.mode.UtilModeFarmer;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.entity.ai.EntityAILMHurtByTarget;
import net.blacklab.lmr.entity.ai.EntityAILMWildWatchClosest;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.TriggerSelect;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

public class EntityMode_Basic extends EntityModeBlockBase {

	public static final int mmode_Wild			= 0x0000;
	public static final int mmode_Escorter		= 0x0001;
	public static final int mmode_FarmPorter	= 0x0024;
	
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
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Strike", "Strike");
		ModLoader.addLocalization("littleMaidMob.mode.Wait", "Wait");
		ModLoader.addLocalization("littleMaidMob.mode.Wild", "Wild");
		ModLoader.addLocalization("littleMaidMob.mode.Wild", "ja_JP", "野生種");
		ModLoader.addLocalization("littleMaidMob.mode.Escorter", "Escorter");
		ModLoader.addLocalization("littleMaidMob.mode.Escorter", "ja_JP", "従者");
		ModLoader.addLocalization("littleMaidMob.mode.F-Escorter", "Freedom");
		ModLoader.addLocalization("littleMaidMob.mode.D-Escorter", "D-Escorter");
		ModLoader.addLocalization("littleMaidMob.mode.T-Escorter", "Tracer");
		*/
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
		ltasks[0].addTask(5, owner.aiRestrictRain);
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

		owner.addMaidMode(ltasks, "Wild", mmode_Wild);

		// Escorter:0x0001
		ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
		owner.addMaidMode(ltasks, "Escorter", mmode_Escorter);
		owner.addMaidMode(ltasks, "FarmPorter", mmode_FarmPorter);
		
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (UtilModeFarmer.isHoe(owner, litemstack)) {
				owner.setMaidMode("FarmPorter");
				return true;
			}
		}
		owner.setMaidMode("Escorter");
		return true;
	}
	
	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Wild :
			owner.setFreedom(true);
//			owner.aiWander.setEnable(true);
			return true;
		case mmode_Escorter :
			owner.aiAvoidPlayer.setEnable(false);
			for (int li = 0; li < owner.mstatSwingStatus.length; li++) {
				owner.setEquipItem(li, -1);
			}
			return true;
		case mmode_FarmPorter :
			return true;
		}
//		owner.getNavigator().clearPathEntity()
		return false;
	}
	
	@Override
	public int getNextEquipItem(int pMode) {
		return 0;
	}
	
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return !owner.isMaidWait();
	}

	@Override
	public boolean isSearchBlock() {
		if ((owner.getMaidModeInt() == mmode_Escorter||owner.getMaidModeInt()==mmode_FarmPorter)
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
	public boolean shouldBlock(int pMode) {
		return myInventory instanceof TileEntity;
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		TileEntity ltile = owner.worldObj.getTileEntity(new BlockPos(px, py, pz));
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
		if (fusedTiles.contains(ltile)) {
			// 既に通り過ぎた場所よッ！
			return false;
		}
		
		double ldis = owner.getDistanceTilePosSq(ltile);
		if (fDistance > ldis) {
			myInventory = (IInventory)ltile;
			fDistance = ldis;
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean overlooksBlock(int pMode) {
		// チェストカートの検索
		List<TileEntity> list = owner.worldObj.loadedTileEntityList;
		for (TileEntity lentity : list) {
			if(!(lentity instanceof TileEntityChest)) continue;
			if (!fusedTiles.contains(lentity)) {
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
			owner.setTilePos((TileEntity)myInventory);
			return myInventory != null;
		}
		//1.8検討
		//owner.setTarget((Entity)myInventory);
		return false;
	}

	@Override
	public void startBlock(int pMode) {
	}

	@Override
	public void resetBlock(int pMode) {
		clearMy();
//		fusedTiles.clear();
	}

	protected void clearMy() {
		myInventory = null;
		if (myChest != null) {
			myChest.closeInventory(owner.maidAvatar);
			myChest = null;
		}
		owner.clearTilePos();
		owner.setAttackTarget(null);
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
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
					owner.setWorking(true);
					putChest();
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
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		// チェストまでのパスを作る
		boolean lf = false;
		if (!owner.isMaidWaitEx()) {
			double distance;
			if (myInventory instanceof TileEntity) {
				distance = owner.getDistanceTilePos();
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
		Block lblock = owner.worldObj.getBlockState(ltile.getPos()).getBlock();
		myChest = myInventory;
		if (lblock instanceof BlockChest) {
			myChest = ((BlockChest)lblock).getLockableContainer(owner.worldObj, ltile.getPos());
		}
		
		return myChest != null;
	}

	protected void putChest() {
		// チェストに近接
		if (owner.getSwingStatusDominant().canAttack() && myChest != null) {
			// 砂糖、時計、被っているヘルム以外のアイテムを突っ込む
			ItemStack is;
			LittleMaidReengaged.Debug(String.format("getChest:%d", maidSearchCount));
			while ((is = owner.maidInventory.getStackInSlot(maidSearchCount)) == null && maidSearchCount < InventoryLittleMaid.maxInventorySize) {
				maidSearchCount++;
			}

			EventLMRE.ItemPutChestEvent event = new EventLMRE.ItemPutChestEvent(owner,myChest,is,maidSearchCount);
			if (is != null){
				if(!VEventBus.instance.post(event)){
//					mod_littleMaidMob.Debug("getchest2.");
					boolean f = false;
					for (int j = 0; j < myChest.getSizeInventory() && is.stackSize > 0; j++)
					{
						ItemStack isc = myChest.getStackInSlot(j);
						if (isc == null)
						{
//							mod_littleMaidMob.Debug(String.format("%s -> NULL", is.getItemName()));
							myChest.setInventorySlotContents(j, is.copy());
							is.stackSize = 0;
							f = true;
							break;
						}
						else if (isc.isStackable() && isc.isItemEqual(is))
						{
//							mod_littleMaidMob.Debug(String.format("%s -> %s", is.getItemName(), isc.getItemName()));
							f = true;
							isc.stackSize += is.stackSize;
							if (isc.stackSize > isc.getMaxStackSize())
							{
								is.stackSize = isc.stackSize - isc.getMaxStackSize();
								isc.stackSize = isc.getMaxStackSize();
							} else {
								is.stackSize = 0; 
								break;
							}
						}
					}
					if (is.stackSize <= 0) {
						owner.maidInventory.setInventorySlotContents(maidSearchCount, null);
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
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		if (pEntity == myInventory) {
			// チェスト付カートとか
			Entity lentity = (Entity)myInventory;
			if (!lentity.isDead) {
				if (owner.getDistanceSqToEntity(lentity) < 5D)	{
					owner.getNavigator().clearPathEntity();
					if (myChest == null) {
						myChest = (IInventory)lentity;
						fusedTiles.add(myChest);
						myChest.openInventory(owner.maidAvatar);
					}
					if (myChest != null) {
						owner.getLookHelper().setLookPositionWithEntity(lentity, 30F, 40F);
					}
					// チェストに収納
					putChest();
				} else {
					// チェストまでのパスを作る
					if (!owner.isMaidWaitEx()) {
						double distance = owner.getDistanceSqToEntity(lentity);
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
				if (pitemstack != null) {
					// 追加分の処理
					// TODO いる？
					//owner.setPathToEntity(null);
					if (owner.isRemainsContract()) {
						if (pitemstack.getItem() instanceof ItemAppleGold) {
							// ゴールデンアッポー
							if(!owner.worldObj.isRemote) {
								((ItemAppleGold)pitemstack.getItem()).onItemUseFinish(pitemstack, owner.worldObj, owner.maidAvatar);
							}
							return true;
						}
						else if (pitemstack.getItem() instanceof ItemBucketMilk && !owner.getActivePotionEffects().isEmpty()) {
							// 牛乳に相談だ
							if(!owner.worldObj.isRemote) {
								owner.clearActivePotions();
							}
							CommonHelper.decPlayerInventory(pentityplayer, -1, 1);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
