package net.blacklab.lmr.entity.littlemaid.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.blacklab.lmr.client.renderer.entity.RenderLittleMaid;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.entity.littlemaid.mode.EntityModeBase;
import net.blacklab.lmr.util.manager.MaidModeManager;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * メイドさんの職業関連を制御する
 * @author firis-games
 *
 */
public class LMJobController {

	
	private final EntityLittleMaid maid;
	
	/**
	 * 設定中のJob
	 */
	private EntityModeBase maidActiveModeClass = null;
	
	/**
	 * 設定中のJob名
	 */
	private String maidMode = "";
	
	/**
	 * Job一覧
	 */
	private List<EntityModeBase> maidEntityModeList = new ArrayList<>();
	
	/**
	 * JobAI一覧
	 */
	private Map<String, EntityAITasks[]> modeAIMap = new HashMap<>();
	
	/**
	 * 血まみれモードのフラグ
	 */
	protected boolean statBloodsuck = false;
	
	/**
	 * コンストラクタ
	 * @param maid
	 */
	public LMJobController(EntityLittleMaid maid) {
		
		this.maid = maid;
		
	}

	/**
	 * 初期化
	 */
	public void init(EntityAITasks defaultMove, EntityAITasks defaultTargeting) {
		
		//メイドさんのお仕事関連を初期化
		this.maidEntityModeList = MaidModeManager.instance.getModeList(this.maid);
		
		/**
		 * メイドさんのJobへAI設定
		 */
		for (EntityModeBase ieml : maidEntityModeList) {
			ieml.addEntityMode(defaultMove, defaultTargeting);
		}
		
		/**
		 * メイドさんのJobの初期化
		 */
		for (EntityModeBase emb : this.maidEntityModeList) {
			emb.initEntity();
		}
	}
	
	/**
	 * メイドさんの職業を取得
	 * @return
	 */
	@Nonnull
	public EntityModeBase getActiveModeClass() {
		return maidActiveModeClass;
	}
	
	/**
	 * メイドさんの職業を文字列で設定する
	 * @param maidMode
	 */
	public void setActiveMaidMode(String job) {
		
		//名称設定
		this.maidMode = job;
		
		//クラスを設定
		this.setMaidModeClass();
	}

	/**
	 * メイドさんの職業を設定
	 * @param pEntityMode
	 */
	public void setActiveModeClass(@Nonnull EntityModeBase pEntityMode) {
		if (pEntityMode == null) {
			throw new IllegalArgumentException("activeMode cannot be null");
		}
		maidActiveModeClass = pEntityMode;
	}
	
	/**
	 * メイドさんの職業が設定されているかの判定
	 * @return
	 */
	public boolean isActiveModeClass() {
		return getActiveModeClass() != null;
	}
	
	/**
	 * メイドさんの職業を追加する
	 * @param pModeName
	 * @param pAiTasks
	 */
	public void addMaidMode(String pModeName, EntityAITasks[] pAiTasks) {
		this.modeAIMap.put(pModeName, pAiTasks);
	}
	
	public String getMaidModeString() {
		return this.maidMode;
	}
	
	/**
	 * JobAIが存在するかの判断を行う
	 * @param job
	 * @return
	 */
	public boolean isJobAI(String job) {
		if (!modeAIMap.containsKey(job)) return false;
		return false;
	}
	
	public EntityAITasks[] getJobAIMap(String job) {
		return modeAIMap.get(job);
	}
	
	/**
	 * メイドモードの文字列に設定された職業に
	 */
	private void setMaidModeClass() {
		
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			EntityModeBase iem = maidEntityModeList.get(li);
			if (iem.setMode(maidMode)) {
				this.setActiveModeClass(iem);
				break;
			}
		}
		
	}
	
	/**
	 * JobのonUpdate処理の呼び出し
	 */
	public void onUpdate() {
		for (EntityModeBase leb : maidEntityModeList) {
			leb.onUpdate(maidMode);
		}
	}
	
	/**
	 * 職業ごとのpreInteract処理
	 */
	public boolean preProcessInteract(EntityPlayer player, ItemStack stack) {
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			if (maidEntityModeList.get(li).preInteract(player, stack)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 職業ごとのInteract処理
	 */
	public boolean processInteract(EntityPlayer player, ItemStack stack) {
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			if (maidEntityModeList.get(li).interact(player, stack)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 被ダメ時の処理
	 * 
	 * 0:処理継続
	 * 1:falseで終了
	 * 2:trueで終了
	 */
	public int attackEntityFrom(DamageSource source, float amount) {
		int ret = 0;
		for (EntityModeBase lm : maidEntityModeList) {
			float flg = lm.attackEntityFrom(source, amount);
			if (flg > 0) {
				ret = flg == 1 ? 1 : 2;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * 現在の状態から
	 * @param player
	 * @return
	 */
	public boolean changeMode(EntityPlayer player) {
		boolean lflag = false;
		for (int li = 0; li < maidEntityModeList.size() && !lflag; li++) {
			lflag = maidEntityModeList.get(li).changeMode(player);
			if (lflag) {
				this.setActiveModeClass(maidEntityModeList.get(li));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 職業ごとの描画用
	 * @param entityLivingBaseIn
	 * @param x
	 * @param y
	 * @param z
	 */
	@SideOnly(Side.CLIENT)
	public void showSpecial(RenderLittleMaid render, double x, double y, double z) {
		// 追加分
		for (int li = 0; li < this.maidEntityModeList.size(); li++) {
			this.maidEntityModeList.get(li).showSpecial(render, x, y, z);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isCancelPutChestItemStack(ItemStack stack, int slotIndedx) {
		boolean isCancelPut = false;
		Iterator<EntityModeBase> maidModeIterator = maidEntityModeList.iterator();
		while (maidModeIterator.hasNext()) {
			EntityModeBase emb = maidModeIterator.next();
			isCancelPut = emb.isCancelPutChestItemStack(maidMode, stack, slotIndedx);
			if (isCancelPut) {
				break;
			}
		}
		return isCancelPut;
	}
	
	/**
	 * NBTに情報を書き込む
	 * @param nbt
	 */
	public void writeEntityToNBT(NBTTagCompound nbt) {
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			maidEntityModeList.get(li).writeEntityToNBT(nbt);
		}
		
		//tileentity系
		NBTTagCompound lnbt = new NBTTagCompound();
		nbt.setTag("Tiles", lnbt);
		for (int li = 0; li < maidTiles.length; li++) {
			if (maidTiles[li] != null) {
//				lnbt.setIntArray(String.valueOf(li), maidTiles[li]);
				int[] pos = { maidTiles[li].getX(), maidTiles[li].getY(), maidTiles[li].getZ() };
				lnbt.setIntArray(String.valueOf(li), pos);
			}
		}
	}
	
	/**
	 * NBTの情報をクラスへ展開する
	 * @param nbt
	 */
	public void readEntityFromNBT(NBTTagCompound nbt) {
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			maidEntityModeList.get(li).readEntityFromNBT(nbt);
		}
		
		//tileentity系
		NBTTagCompound lnbt = nbt.getCompoundTag("Tiles");
		for (int li = 0; li < maidTiles.length; li++) {
			int ltile[] = lnbt.getIntArray(String.valueOf(li));
			//maidTiles[li] = ltile.length > 0 ? ltile : null;
			maidTiles[li] = ltile.length > 0 ? new BlockPos(ltile[0], ltile[1], ltile[2]) : null;
		}
	}
	
	
	/**
	 * Tile関係をEntityLittleMaidから移動
	 */
	
//	protected int maidTiles[][] = new int[9][3];
//	protected int maidTile[] = new int[3];
	protected BlockPos[] maidTiles = new BlockPos[9];
	protected BlockPos maidTile = BlockPos.ORIGIN;
	
	protected TileEntity maidTileEntity;
	
	public TileEntity getTileEntity() {
		return this.maidTileEntity;
	}
	
	/**
	 * 使っているTileかどうか判定して返す。
	 */
	public boolean isUsingTile(TileEntity pTile) {
		if (this.isActiveModeClass()) {
			return this.getActiveModeClass().isUsingTile(pTile);
		}
		for (int li = 0; li < maidTiles.length; li++) {
			if (maidTiles[li] != null &&
//					pTile.getPos().getX() == maidTiles[li][0] &&
//					pTile.getPos().getY() == maidTiles[li][1] &&
//					pTile.getPos().getZ() == maidTiles[li][2]) {
					pTile.getPos().equals(maidTiles[li])) {
				return true;
			}
		}
		return false;
	}

	public boolean isEqualTile() {
//		return this.maid.getEntityWorld().getTileEntity(new BlockPos(maidTile[0], maidTile[1], maidTile[2])) == maidTileEntity;
		return this.maid.getEntityWorld().getTileEntity(this.maidTile) == maidTileEntity;
	}

	public boolean isTilePos() {
		return maidTileEntity != null;
	}
	
	public BlockPos getCurrentTilePos() {
//		return new BlockPos(maidTile[0], maidTile[1], maidTile[2]);
		return new BlockPos(this.maidTile);
	}

	public void setTilePos(int pX, int pY, int pZ) {
//		maidTile[0] = pX;
//		maidTile[1] = pY;
//		maidTile[2] = pZ;
		this.maidTile = new BlockPos(pX, pY, pZ);
	}
	public void setTilePos(TileEntity pEntity) {
//		maidTile[0] = pEntity.getPos().getX();
//		maidTile[1] = pEntity.getPos().getY();
//		maidTile[2] = pEntity.getPos().getZ();
		this.maidTile = new BlockPos(pEntity.getPos().getX(), pEntity.getPos().getY(), pEntity.getPos().getZ());
		maidTileEntity = pEntity;
	}
	public void setTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
//			if (maidTiles[pIndex] == null) {
//				maidTiles[pIndex] = new int[3];
//			}
//			maidTiles[pIndex][0] = maidTile[0];
//			maidTiles[pIndex][1] = maidTile[1];
//			maidTiles[pIndex][2] = maidTile[2];
			maidTiles[pIndex] = new BlockPos(maidTile);
		}
	}
	
	public TileEntity getTileEntity(int pIndex) {
		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
//			TileEntity ltile = this.maid.getEntityWorld().getTileEntity(new BlockPos(
//					maidTiles[pIndex][0], maidTiles[pIndex][1], maidTiles[pIndex][2]));
			TileEntity ltile = this.maid.getEntityWorld().getTileEntity(maidTiles[pIndex]);
			if (ltile == null) {
				clearTilePos(pIndex);
			}
			return ltile;
		}
		return null;
	}

	public void clearTilePos() {
		maidTileEntity = null;
	}
	public void clearTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
			maidTiles[pIndex] = null;
		}
	}
	public void clearTilePosAll() {
		for (int li = 0; li < maidTiles.length; li++) {
			maidTiles[li] = null;
		}
	}

	public double getDistanceTilePos() {
		return this.maid.getDistance(
//				maidTile[0] + 0.5D,
//				maidTile[1] + 0.5D,
//				maidTile[2] + 0.5D);
				maidTile.getX() + 0.5D,
				maidTile.getY() + 0.5D,
				maidTile.getZ() + 0.5D);
	}
	
	public void looksTilePos() {
		this.maid.getLookHelper().setLookPosition(
//				maidTile[0] + 0.5D, maidTile[1] + 0.5D, maidTile[2] + 0.5D,
				maidTile.getX() + 0.5D, maidTile.getY() + 0.5D, maidTile.getZ() + 0.5D,
				10F, this.maid.getVerticalFaceSpeed());
	}
	
	public BlockPos[] getMaidTiles() {
		return maidTiles;
	}
	
	public BlockPos getMaidTile() {
		return maidTile;
	}
	
	public void setBloodsuck(boolean flg) {
		this.statBloodsuck = flg;
	}
	
	public boolean isBloodsuck() {
		return this.statBloodsuck;
	}
	
}
