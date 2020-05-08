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
import net.minecraft.util.DamageSource;
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
	}
	
	/**
	 * NBTの情報をクラスへ展開する
	 * @param nbt
	 */
	public void readEntityFromNBT(NBTTagCompound nbt) {
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			maidEntityModeList.get(li).readEntityFromNBT(nbt);
		}
	}
	
}
