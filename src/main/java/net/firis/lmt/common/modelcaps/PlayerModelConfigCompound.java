package net.firis.lmt.common.modelcaps;

import net.blacklab.lmr.entity.maidmodel.ModelConfigCompoundBase;
import net.blacklab.lmr.util.IModelCapsData;
import net.minecraft.entity.EntityLivingBase;

/**
 * PlayerAvatar用パラメータクラス
 * @author firis-games
 * 
 * モーションなどの表示フラグもここで管理する
 *
 */
public class PlayerModelConfigCompound extends ModelConfigCompoundBase {
	
	/**
	 * LMAvatarが有効化どうかの判断
	 */
	private boolean enableLMAvatar = true;
	
	/**
	 * 待機アクション
	 */
	private boolean lmAvatarWaitAction = false;  
	
	/**
	 * 待機アクション用カウンター
	 */
	private Integer lmAvatarWaitCounter = 0;
	
	/**
	 *　LMアバターアクション
	 */
	private boolean lmAvatarAction = false;  
	
	/**
	 * コンストラクタ
	 * @param entity
	 * @param caps
	 */
	public PlayerModelConfigCompound(EntityLivingBase entity, IModelCapsData caps) {
		super(entity, caps);
	}
	
	/**
	 * アクション状態を取得する
	 * @return
	 */
	public boolean getLMAvatarAction() {
		return this.lmAvatarAction;
	}
	
	/**
	 * 待機状態を取得する
	 */
	public boolean getLMAvatarWaitAction() {
		return this.lmAvatarWaitAction;
	}

	/**
	 * LMアバターのアクションを設定する
	 */
	public void setLMAvatarAction(boolean isAction) {
		this.lmAvatarAction = isAction;
	}
	
	/**
	 * LMアバターの待機アクション
	 * 一定時間経過後にtrueと判断する
	 * @param isAction
	 */
	public void setLMAvatarWaitAction(boolean isAction) {
		//モーション継続状態と判断
		Integer counter = lmAvatarWaitCounter;
		
		//初期化
		if (counter == 0) lmAvatarWaitCounter = owner.ticksExisted;
		
		//100tickで待機状態On
		if ((owner.ticksExisted - counter) >= 100) {
			this.lmAvatarWaitAction = true;
		}
	}
	
	/**
	 * LMアバターのアクションをリセットする
	 */
	public void resetLMAvatarAction() {
		this.lmAvatarAction = false;
	}
	
	/**
	 * LMアバターの待機アクションをリセットする
	 */
	public void resetLMAvatarWaitAction() {
		this.lmAvatarWaitAction = false;
		this.lmAvatarWaitCounter = owner.ticksExisted;
	}
	
	/**
	 * LMAvatarの有効無効設定
	 * @param enable
	 */
	public void setEnableLMAvatar(boolean enable) {
		this.enableLMAvatar = enable;
	}
	
	/**
	 * LMAvatarの設定
	 * @return
	 */
	public boolean getEnableLMAvatar() {
		return this.enableLMAvatar;
	}
}
