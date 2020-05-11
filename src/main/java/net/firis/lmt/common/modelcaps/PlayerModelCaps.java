package net.firis.lmt.common.modelcaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.blacklab.lmr.entity.maidmodel.base.ModelMultiBase;
import net.blacklab.lmr.entity.maidmodel.caps.IModelCaps;
import net.blacklab.lmr.util.IModelCapsData;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.firis.lmt.client.event.LittleMaidAvatarClientTickEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * IModelCapsのEntityPlayer用
 * @author firis-games
 *
 */
public class PlayerModelCaps implements IModelCapsData {
	
	private final EntityPlayer owner;
	
	private boolean isFirstPerson = false;
	
	public void setFirstPerson(boolean flg) {
		this.isFirstPerson = flg;
	}
	
	public PlayerModelCaps(EntityPlayer player) {
		this.owner = player;
	}
	
	@Deprecated
	@Override
	public Map<String, Integer> getModelCaps() {
		return null;
	}

	/**
	 * set処理は利用しない
	 */
	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public Object getCapsValue(int pIndex, Object... pArg) {
		switch (pIndex) {
		case caps_Entity:
			return owner;
		case caps_health:
			int iCapsHealth = (int) (owner.getHealth() / owner.getMaxHealth() * 20F);
			return Math.min(iCapsHealth, 20);
		case caps_healthFloat:
			float fCapsHealth = (owner.getHealth() / owner.getMaxHealth() * 20F);
			return Math.min(fCapsHealth, 20F);
		case caps_ticksExisted:
			return owner.ticksExisted;
		case caps_heldItems:
		case caps_currentEquippedItem:
			ItemStack mainhand = owner.getHeldItemMainhand();
			if (mainhand.isEmpty()) mainhand = null;
			return mainhand;
		case caps_currentArmor:
			ItemStack aromor = ((List<ItemStack>)owner.getArmorInventoryList()).get((Integer) pArg[0]);
			if (aromor.isEmpty()) aromor = null;
			return aromor;
		case caps_posX:
			return owner.posX;
		case caps_posY:
			return owner.posY;
		case caps_posZ:
			return owner.posZ;
		case caps_pos:
			if (pArg == null) {
				return new Double[] {owner.posX, owner.posY, owner.posZ};
			}
			return (Integer)pArg[0] == 0 ? owner.posX : (Integer)pArg[0] == 1 ? owner.posY : owner.posZ;
		case caps_motionX:
			return owner.motionX;
		case caps_motionY:
			return owner.motionY;
		case caps_motionZ:
			return owner.motionZ;
		case caps_motion:
			if (pArg == null) {
				return new Double[] {owner.motionX, owner.motionY, owner.motionZ};
			}
			return (Integer)pArg[0] == 0 ? owner.motionX : (Integer)pArg[0] == 1 ? owner.motionY : owner.motionZ;

		case caps_rotationYaw:
			return owner.rotationYaw;
		case caps_rotationPitch:
			return owner.rotationPitch;
		case caps_prevRotationYaw:
			return owner.prevRotationYaw;
		case caps_prevRotationPitch:
			return owner.prevRotationPitch;
		case caps_renderYawOffset:
			return owner.renderYawOffset;
		case caps_onGround:
			return this.getOnGrounds();
		case caps_isRiding:
			if (this.isFirstPerson) return false;
			//疑似お座りモーションを管理する
			return owner.isRiding() || LittleMaidAvatarClientTickEventHandler.lmAvatarAction.getStat(owner);
		case caps_motionSitting:
			//疑似お座りモーション
			return LittleMaidAvatarClientTickEventHandler.lmAvatarAction.getStat(owner);
		case caps_isRidingPlayer:
			return false;
		case caps_isChild:
			return owner.isChild();
		case caps_isWet:
			return owner.isWet();
		case caps_isDead:
			return owner.isDead;
		case caps_isJumping:
			return false;//owner.isJumping;
		case caps_isInWeb:
			return false;//owner.isInWeb;
		case caps_isSwingInProgress:
			return owner.isSwingInProgress;
		case caps_isSneak:
			if (this.isFirstPerson) return false;
			return owner.isSneaking();
		case caps_isBurning:
			return owner.isBurning();
		case caps_isInWater:
			return owner.isInWater();
		case caps_isInvisible:
			return owner.isInvisible();
		case caps_isSprinting:
			return owner.isSprinting();
		case caps_PosBlockID:
			return owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2]))).getBlock();
		case caps_PosBlockState:
			return owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		case caps_PosBlockAir:
			
			IBlockState state = owner.getEntityWorld().getBlockState(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
			
			return !state.getBlock().causesSuffocation(state);
			
		case caps_PosBlockLight:
			return owner.getEntityWorld().getBlockLightOpacity(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		case caps_PosBlockPower:
			return owner.getEntityWorld().getStrongPower(new BlockPos(
					MathHelper.floor(owner.posX + (Double)pArg[0]),
					MathHelper.floor(owner.posY + (Double)pArg[1]),
					MathHelper.floor(owner.posZ + (Double)pArg[2])));
		case caps_boundingBox:
			if (pArg == null) {
				return owner.getEntityBoundingBox();
			}
			switch ((Integer)pArg[0]) {
			case 0:
				return owner.getEntityBoundingBox().maxX;
			case 1:
				return owner.getEntityBoundingBox().maxY;
			case 2:
				return owner.getEntityBoundingBox().maxZ;
			case 3:
				return owner.getEntityBoundingBox().minX;
			case 4:
				return owner.getEntityBoundingBox().minY;
			case 5:
				return owner.getEntityBoundingBox().minZ;
			}
		case caps_isLeeding:
			return false;
		case caps_getRidingName:
			return owner.getRidingEntity() == null ? "" : EntityList.getEntityString(owner.getRidingEntity());

		// World
		case caps_WorldTotalTime:
			return owner.getEntityWorld().getWorldInfo().getWorldTotalTime();
		case caps_WorldTime:
			return owner.getEntityWorld().getWorldInfo().getWorldTime();
		case caps_MoonPhase:
			return owner.getEntityWorld().getMoonPhase();
		case caps_TextureEntity:
			return owner;
			
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
			if (this.isFirstPerson) return false;
			return LittleMaidAvatarClientTickEventHandler.lmAvatarWaitAction.getStat(owner);
			
		//砂糖を持った時の首傾げ
		case caps_isLookSuger:
			return ItemHelper.isSugar(this.owner.getHeldItemMainhand());
		}

		return null;
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
	 * ModelCapsの情報をModelBaseへ反映する
	 */
	public void setModelMultiBaseCapsFromModelCaps(ModelMultiBase model) {
		
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
	 * IModelCapsData
	 */
	@Override
	public void setModelMultiFromModelCaps(ModelMultiBase model, float entityYaw, float partialTicks) {
		
		//初期化設定
		this.setModelMultiBaseCapsFromModelCaps(model);
		
	}
	
}
