package firis.lmavatar.common.modelcaps;

import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmlib.api.caps.ModelCapsEntityBase;
import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.model.ModelMultiBase;
import firis.lmmm.api.model.motion.LMMotionSitdown;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
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
		case caps_isRiding:
			if (this.isFirstPersonView()) return false;
			//疑似お座りモーションを管理する
			return owner.isRiding() || PlayerModelManager.getModelConfigCompound(owner).getLMAvatarAction();
		//お座りモーション制御用
		case caps_multimodel_motion:
			//疑似お座りモーション
			boolean isSitdown = PlayerModelManager.getModelConfigCompound(owner).getLMAvatarAction();
			return isSitdown ? LMMotionSitdown.SITDOWN : null;
		case caps_isSneak:
			if (this.isFirstPersonView()) return false;
			return owner.isSneaking();
			
		//弓構え
		case caps_aimedBow:
			//プレイヤーが弓構え
			return getPlayerAction(owner, EnumHandSide.RIGHT) == EnumAction.BOW 
			|| getPlayerAction(owner, EnumHandSide.LEFT) == EnumAction.BOW;
		
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
			return this.owner.getHeldItemMainhand().getItem() == Items.SUGAR;
		}

		//親クラスの情報を取得する
		return super.getCapsValue(pIndex, pArg);
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
