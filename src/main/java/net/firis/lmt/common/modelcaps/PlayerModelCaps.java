package net.firis.lmt.common.modelcaps;

import java.util.ArrayList;
import java.util.List;

import firis.lmlib.api.caps.ModelCapsEntityBase;
import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.firis.lmt.common.manager.PlayerModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

/**
 * IModelCapsのEntityPlayer用
 * @author firis-games
 *
 */
public class PlayerModelCaps extends ModelCapsEntityBase<EntityPlayer> {
	
	/**
	 * コンストラクタ
	 * @param player
	 */
	public PlayerModelCaps(EntityPlayer player) {
		super(player);
	}
	
	/**
	 * Playerの情報を取得する
	 */
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_onGround:
			return this.getOnGrounds();
		case caps_isRiding:
			if (this.isFirstPersonView()) return false;
			//疑似お座りモーションを管理する
			return owner.isRiding() || PlayerModelManager.getModelConfigCompound(owner).getLMAvatarAction();
		case caps_motionSitting:
			//疑似お座りモーション
			return PlayerModelManager.getModelConfigCompound(owner).getLMAvatarAction();
		case caps_isRidingPlayer:
			return false;
		case caps_isSneak:
			if (this.isFirstPersonView()) return false;
			return owner.isSneaking();
		case caps_isLeeding:
			return false;
			
		//弓構え
		case caps_aimedBow:
			//プレイヤーが弓構え
			return getPlayerAction(owner, EnumHandSide.RIGHT) == EnumAction.BOW 
			|| getPlayerAction(owner, EnumHandSide.LEFT) == EnumAction.BOW;
			
		//Entityごとの揺らぎを持たせているらしい？
		case caps_entityIdFactor:
			return 0.0F;
			
		//利き手
		case caps_dominantArm:
			return owner.getPrimaryHand() == EnumHandSide.RIGHT ? 0 : 1;
		
		//アイテムを持った時の腕振り制御
		case caps_heldItemLeft:
			if (EnumAction.BLOCK == getPlayerAction(owner, EnumHandSide.LEFT)) return 3.5F;
			return 0.0F;
			
		case caps_heldItemRight:
			if (EnumAction.BLOCK == getPlayerAction(owner, EnumHandSide.RIGHT)) return 3.5F;
			return 0.0F;
			
		//メイドさん待機モーション
		case caps_isWait:
			if (this.isFirstPersonView()) return false;
			return PlayerModelManager.getModelConfigCompound(owner).getLMAvatarWaitAction();
			
		//砂糖を持った時の首傾げ
		case caps_isLookSuger:
			return ItemHelper.isSugar(this.owner.getHeldItemMainhand());
		}

		//親クラスの情報を取得する
		return super.getCapsValue(pIndex, pArg);
	}
	
	private List<Integer> modelCapsList = this.initModelCapsList();
	private List<Integer> initModelCapsList() {
		List<Integer> caps = new ArrayList<>();
		
		//モデル側へ受け渡すcapsIdを設定
		caps.add(IModelCaps.caps_heldItemLeft);
		caps.add(IModelCaps.caps_heldItemRight);
		caps.add(IModelCaps.caps_onGround);
		caps.add(IModelCaps.caps_isRiding);
		caps.add(IModelCaps.caps_isSneak);
		caps.add(IModelCaps.caps_aimedBow);
		caps.add(IModelCaps.caps_isWait);
		caps.add(IModelCaps.caps_isChild);
		caps.add(IModelCaps.caps_entityIdFactor);
		caps.add(IModelCaps.caps_ticksExisted);
		caps.add(IModelCaps.caps_dominantArm);
		caps.add(IModelCaps.caps_motionSitting);
		
		return caps;
	}
	
	/**
	 * メイドさんのmstatSwingStatusを仮想で再現
	 * @param player
	 * @return
	 */
	protected float[] getOnGrounds() {
		
		float onGrounds[] = new float[] {0.0F, 0.0F};
		
		//利き腕取得
		EnumHandSide dominantHand = this.owner.getPrimaryHand();
		boolean isDominantRight = dominantHand == EnumHandSide.RIGHT;
		
		//右か左かの判断
		boolean isMainHand = EnumHand.MAIN_HAND == this.owner.swingingHand;
		
		//左利きの場合は左右逆転する
		if (!isDominantRight) {
			isMainHand = !isMainHand;
		}
		
		//腕振り
		/*　tick単位での腕振り制御位置
		腕を振った時にplayer.swingProgressにこの値が設定される
		0.16666667
		0.16666667
		0.16666667
		0.33333334
		0.33333334
		0.33333334
		0.5
		0.5
		0.5
		0.6666667
		0.6666667
		0.6666667
		0.8333333
		0.8333333
		0.8333333
		*/
		if (isMainHand) {
			//右振り
			onGrounds[0] = this.owner.swingProgress;
			onGrounds[1] = 0.0F;
		} else {
			//左振り
			onGrounds[0] = 0.0F;
			onGrounds[1] = this.owner.swingProgress;
		}
		return onGrounds;
	}
	
	/**
	 * プレイヤーのEnumActionを取得する
	 * 右手と左手で判断する
	 */
	public static EnumAction getPlayerAction(EntityPlayer player, EnumHandSide handSide) {
		
		//利き手を考慮して判断する
		ItemStack itemStack;
		if (player.getPrimaryHand() == handSide) {
			itemStack = player.getHeldItemMainhand();
		} else {
			itemStack = player.getHeldItemOffhand();
		}
        
        //手持ちアイテムあり かつ アイテム使用中
        if (!itemStack.isEmpty() && player.getItemInUseCount() > 0) {
       		return itemStack.getItemUseAction();
        }
        return EnumAction.NONE;
	}

	/**
	 * ModelMultiBase初期化
	 */
	@Override
	public void initModelMultiBase(ModelMultiBase model, float entityYaw, float partialTicks) {
		
		//初期化設定
		for (Integer capsId : modelCapsList) {
			//onGroundだけ特殊処理
			if (IModelCaps.caps_onGround == capsId) {
				float[] onGround = (float[]) this.getCapsValue(capsId);
				model.setCapsValue(capsId, onGround[0], onGround[1]);
			} else {
				model.setCapsValue(capsId, this.getCapsValue(capsId));
			}
		}
		
	}
	
	/**
	 * 一人称視点の判断を行う
	 * @return
	 */
	private boolean isFirstPersonView() {
		return Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;
	}
	
}
