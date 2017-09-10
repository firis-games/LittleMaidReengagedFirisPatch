package net.blacklab.lmr.entity.mode;

import java.util.List;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.EnumSound;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMode_Playing extends EntityModeBase {
	
	public static enum PlayRole {
		NOTPLAYING,
		QUICKSHOOTER,
		STOCKSHOOTER
	}

	public static final String mmode_Playing	= "SYS:Playing";

	public int playingTick = 0;

	public int fcounter;

	public EntityMode_Playing(EntityLittleMaid pEntity) {
		super(pEntity);
		fcounter = 0;
		isAnytimeUpdate = true;
	}

	@Override
	public int priority() {
		return 9100;
	}

	@Override
	public void init() {
		/* langファイルに移動
		ModLoader.addLocalization("littleMaidMob.mode.Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.T-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.F-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.D-Playing", "Playing");
		*/
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Playing:0x00ff
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
//		ltasks[1] = new EntityAITasks(owner.aiProfiler);

//		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
//		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));

		owner.addMaidMode(mmode_Playing, ltasks);
	}

	public static boolean checkSnows(int x, int y, int z, World world) {
		// 周りが雪か？
		int snowCnt = 0;
		snowCnt += Block.isEqualTo(world.getBlockState(new BlockPos(x,   y, z  )).getBlock(), Blocks.SNOW_LAYER) ? 3: 0;
		snowCnt += Block.isEqualTo(world.getBlockState(new BlockPos(x+1, y, z  )).getBlock(), Blocks.SNOW_LAYER) ? 1: 0;
		snowCnt += Block.isEqualTo(world.getBlockState(new BlockPos(x-1, y, z  )).getBlock(), Blocks.SNOW_LAYER) ? 1: 0;
		snowCnt += Block.isEqualTo(world.getBlockState(new BlockPos(x,   y, z+1)).getBlock(), Blocks.SNOW_LAYER) ? 1: 0;
		snowCnt += Block.isEqualTo(world.getBlockState(new BlockPos(x,   y, z-1)).getBlock(), Blocks.SNOW_LAYER) ? 1: 0;

		return snowCnt >= 5;
	}

	protected boolean movePlaying() {
		//
		int x = MathHelper.floor_double(owner.posX);
		int y = MathHelper.floor_double(owner.posY);
		int z = MathHelper.floor_double(owner.posZ);
		Path pe = null;

		// CW方向に検索領域を広げる
		loop_search:
			for (int a = 2; a < 18 && pe == null; a += 2) {
				x--;
				z--;
				for (int b = 0; b < a; b++) {
					// N
					for (int c = 0; c < 4; c++) {
						if (checkSnows(x, y, z, owner.worldObj)) {
							pe = owner.getNavigator().getPathToXYZ(x, y - 1, z);
//							pe = owner.getNavigator().getEntityPathToXYZ(owner, x, y - 1, z, 10F, true, false, false, true);
							if (pe != null) {
								break loop_search;
							}
						}
						if (c == 0) x++;
						if (c == 1) z++;
						if (c == 2) x--;
						if (c == 3) z--;
					}
				}
			}
		if (pe != null) {
			owner.getNavigator().setPath(pe, 1.0F);
			LittleMaidReengaged.Debug("Find Snow Area-%d:%d, %d, %d.", owner.getEntityId(), x, y, z);
			return true;
		}
		return false;

	}

	protected void playingSnowWar() {
		switch (fcounter) {
		case 0:
			// 有り玉全部投げる
			owner.setSitting(false);
			owner.setSneaking(false);
			if (!owner.getNextEquipItem()) {
				owner.setAttackTarget(null);

				owner.getNavigator().clearPathEntity();
				fcounter = 1;
			} else if (owner.getAttackTarget() == null) {
				// メイドとプレーヤー（無差別）をターゲットに
				List<Entity> list = owner.worldObj.getEntitiesWithinAABBExcludingEntity(owner, owner.getEntityBoundingBox().expand(16D, 4D, 16D));
				for (Entity e : list) {
					if (e != null && (e instanceof EntityPlayer || e instanceof EntityLittleMaid)) {
						if (owner.getRNG().nextBoolean()) {
							owner.setAttackTarget((EntityLivingBase)e);
							break;
						}
					}
				}
			}
			break;
		case 1:
			// 乱数加速
			owner.setAttackTarget(null);
			if (owner.getNavigator().noPath()) {
				fcounter = 2;
			}
			break;

		case 2:
			// 雪原を探す
			if (owner.getAttackTarget() == null && owner.getNavigator().noPath()) {
				if (movePlaying()) {
					fcounter = 3;
				} else {
					owner.setPlayingRole(PlayRole.NOTPLAYING);
					fcounter = 0;
				}
			} else {
				owner.setAttackTarget(null);
			}
//			isMaidChaseWait = true;
			break;
		case 3:
			// 雪原へ到着
			if (owner.getNavigator().noPath()) {
				if (checkSnows(
						MathHelper.floor_double(owner.posX),
						MathHelper.floor_double(owner.posY),
						MathHelper.floor_double(owner.posZ),
						owner.worldObj)) {
//					owner.isMaidChaseWait = true;
					//1.8検討
					//owner.attackTime = 30;
					if (owner.getPlayingRole().equals(PlayRole.QUICKSHOOTER)) {
						fcounter = 8;
					} else {
						fcounter = 4;
					}
				} else {
					// 再検索
					fcounter = 2;
				}
			}
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			// リロード
			//1.8検討
			if (owner.arrowHitTimer <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Items.SNOWBALL))) {
					owner.playSound("entity.item.pickup");
					if (owner.getPlayingRole().equals(PlayRole.STOCKSHOOTER)) {
						owner.setSwing(5, EnumSound.collect_snow, false);
						fcounter = 0;
					} else {
						owner.setSwing(30, EnumSound.collect_snow, false);
						fcounter++;
					}
				} else {
					owner.setPlayingRole(PlayRole.NOTPLAYING);
					fcounter = 0;
				}
			}
//			owner.isMaidChaseWait = true;
			owner.setJumping(false);
			owner.getNavigator().clearPathEntity();
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX),
					MathHelper.floor_double(owner.posY - 1D),
					MathHelper.floor_double(owner.posZ),
					30F, 40F);
			owner.setSitting(true);
			break;
		case 8:
			// リロード
//			isMaidChaseWait = true;
			if (owner.arrowHitTimer <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Items.SNOWBALL))) {
					owner.setSwing(5, EnumSound.collect_snow, false);
					owner.playSound("entity.item.pickup");
					fcounter = 0;
				} else {
					owner.setPlayingRole(PlayRole.NOTPLAYING);
					fcounter = 0;
				}
			}
//			isMaidChaseWait = true;
			owner.setSneaking(true);
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX),
					MathHelper.floor_double(owner.posY - 1D),
					MathHelper.floor_double(owner.posZ),
					30F, 40F);
			break;
		}

	}


	@Override
	public void updateAITick(String pMode) {
		if(owner.playingTick++<5 || !pMode.equals(mmode_Playing)){
			return;
		}
		owner.playingTick = 0;
		if (owner.isFreedom() || !owner.isContractEX()) {
			// 自由行動中の固体は虎視眈々と隙をうかがう。
			if (owner.worldObj.isDaytime()) {
				// 昼間のお遊び

				// 雪原判定
				if (!owner.isPlaying()) {
					// TODO:お遊び判定
					int xx = MathHelper.floor_double(owner.posX);
					int yy = MathHelper.floor_double(owner.posY);
					int zz = MathHelper.floor_double(owner.posZ);

					// 3x3が雪の平原ならお遊び判定が発生
					boolean f = true;
					for (int z = -1; z < 2; z++) {
						for (int x = -1; x < 2; x++) {
							f &= Block.isEqualTo(owner.worldObj.getBlockState(new BlockPos(xx + x, yy, zz + z)).getBlock(), Blocks.SNOW_LAYER);
						}
					}
					int lpr = owner.getRNG().nextInt(100) - 97;
					PlayRole tSwitchTo = (f && lpr > 0) ? (lpr == 1 ? PlayRole.QUICKSHOOTER : PlayRole.STOCKSHOOTER) : PlayRole.NOTPLAYING;
					owner.setPlayingRole(tSwitchTo);
					fcounter = 0;
					if (f) {
						// mod_littleMaidMob.Debug(String.format("playRole-%d:%d", entityId, playingRole));
					}

				} else if (!owner.getPlayingRole().equals(PlayRole.NOTPLAYING)) {
					// 夜の部終了
					owner.setPlayingRole(PlayRole.NOTPLAYING);
					fcounter = 0;
				} else {
					// お遊びの実行をここに書く？
					if (!owner.getPlayingRole().equals(PlayRole.NOTPLAYING)) {
						playingSnowWar();
					}

				}

			} else {
				if (!owner.getPlayingRole().equals(PlayRole.NOTPLAYING)) {
					// 昼の部終了
					owner.setPlayingRole(PlayRole.NOTPLAYING);
					fcounter = 0;
				}
			}

			// チェスト判定
			if (owner.getAttackTarget() == null
					&& owner.maidInventory.getFirstEmptyStack() == -1) {

			}
		}
	}

	@Override
	public float attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (par1DamageSource.getSourceOfDamage() instanceof EntitySnowball) {
			// お遊び判定用、雪玉かどうか判定
			owner.setMaidDamegeSound(EnumSound.hurt_snow);
			if (!owner.isContractEX() || (owner.isFreedom() && owner.maidMode.equals(EntityMode_Basic.mmode_Escorter))) {
				owner.setPlayingRole(PlayRole.QUICKSHOOTER);
				owner.setMaidWait(false);
				owner.setMaidWaitCount(0);
				LittleMaidReengaged.Debug("playingMode Enable.");
			}
		}
		return 0F;
	}

	@Override
	public boolean setMode(String pMode) {
		if (pMode.equals(mmode_Playing)) {
			if(!owner.worldObj.isDaytime()) return false;
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(false);
			return true;
		}

		return false;
	}

	@Override
	public int getNextEquipItem(String pMode) {
		ItemStack litemstack = null;
		if (owner.isPlaying()) {
			for (int li = 0; li < owner.maidInventory.getSizeInventory(); li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 雪球
				if (litemstack.getItem() instanceof ItemSnowball) {
					return li;
				}
			}
		}
		return -1;
	}

}
