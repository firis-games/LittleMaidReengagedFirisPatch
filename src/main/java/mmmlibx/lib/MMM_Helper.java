package mmmlibx.lib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class MMM_Helper {

	public static final boolean isClient;
	public static final Package fpackage = null;
//	public static final String packegeBase;
	public static final boolean isForge = true;//ModLoader.isModLoaded("Forge");
	public static final Minecraft mc;
//	public static Method methGetSmeltingResultForge = null;
	public static Class entityRegistry = null;
	public static Method registerModEntity = null;
	protected static final Map<Class, Class>replaceEntitys = new HashMap<Class, Class>();
	protected static Map<String, Integer> entityIDList = new HashMap<String, Integer>();
	
	static {
		// TODO 必要ない？
//		fpackage = ModLoader.class.getPackage();
//		packegeBase = "";//fpackage == null ? "" : fpackage.getName().concat(".");

		Minecraft lm = null;
		try {
			lm =  Minecraft.getMinecraft();// ModLoader.getMinecraftInstance();
		} catch (Exception e) {
//			e.printStackTrace();
		} catch (Error e) {
//			e.printStackTrace();
		}
		mc = lm;
		isClient = mc != null;
		/*
		if (isForge) {
			try {
				methGetSmeltingResultForge = FurnaceRecipes.class.getMethod("getExperience", ItemStack.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				entityRegistry = getNameOfClass("cpw.mods.fml.common.registry.EntityRegistry");
				registerModEntity = entityRegistry.getMethod("registerModEntity",
						Class.class, String.class, int.class, Object.class, int.class, int.class, boolean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
	}

	/**
	 * 現在の実行環境がローカルかどうかを判定する。
	 */
	public static boolean isLocalPlay() {
		return isClient && mc.isIntegratedServerRunning();
	}

	/**
	 * マルチ対応用。
	 * ItemStackに情報更新を行うと、サーバー側との差異からSlotのアップデートが行われる。
	 * その際、UsingItemの更新処理が行われないため違うアイテムに持替えられたと判定される。
	 * ここでは比較用に使われるスタックリストを強制的に書換える事により対応した。
	 */
	public static void updateCheckinghSlot(Entity pEntity, ItemStack pItemstack) {
		if (pEntity instanceof EntityPlayerMP) {
			// サーバー側でのみ処理
			EntityPlayerMP lep = (EntityPlayerMP)pEntity;
			Container lctr = lep.openContainer;
			for (int li = 0; li < lctr.inventorySlots.size(); li++) {
				ItemStack lis = lctr.getSlot(li).getStack(); 
				if (lis == pItemstack) {
					lctr.inventoryItemStacks.set(li, pItemstack.copy());
					break;
				}
			}
		}
	}
	
	public static String getPlayerName(EntityPlayer player)
	{
		return player.getGameProfile().getName();
	}
	
	/**
	 * Forge用クラス獲得。
	 */
	/*
	使用箇所無し 削除
	public static Class getForgeClass(BaseMod pMod, String pName) {
		if (isForge) {
			pName = pName.concat("_Forge");
		}
		return getNameOfClass(pName);
	}
	*/

	/**
	 * 名前からクラスを獲得する
	 */
	public static Class getNameOfClass(String pName) {
		if (fpackage != null) {
			pName = fpackage.getName() + "." + pName;
		}
		Class lclass = null;
		try {
			lclass = Class.forName(pName);
		} catch (Exception e) {
			MMMLib.Debug("Class:%s is not found.", pName);
		}
		
		return lclass;
	}

	/**
	 * 送信用データのセット
	 */
	public static void setValue(byte[] pData, int pIndex, int pVal, int pSize) {
		for (int li = 0; li < pSize; li++) {
			pData[pIndex++] = (byte)(pVal & 0xff);
			pVal = pVal >>> 8;
		}
	}
	
	public static void setInt(byte[] pData, int pIndex, int pVal) {
		pData[pIndex + 3]	= (byte)(pVal & 0xff);
		pData[pIndex + 2]	= (byte)((pVal >>> 8) & 0xff);
		pData[pIndex + 1]	= (byte)((pVal >>> 16) & 0xff);
		pData[pIndex + 0]	= (byte)((pVal >>> 24) & 0xff);
	}
	
	public static int getInt(byte[] pData, int pIndex) {
		return (pData[pIndex + 3] & 0xff) | ((pData[pIndex + 2] & 0xff) << 8) | ((pData[pIndex + 1] & 0xff) << 16) | ((pData[pIndex + 0] & 0xff) << 24);
	}

	public static void setFloat(byte[] pData, int pIndex, float pVal) {
		setInt(pData, pIndex, Float.floatToIntBits(pVal));
	}

	public static float getFloat(byte[] pData, int pIndex) {
		return Float.intBitsToFloat(getInt(pData, pIndex));
	}

	public static void setShort(byte[] pData, int pIndex, int pVal) {
		pData[pIndex++]	= (byte)(pVal & 0xff);
		pData[pIndex]	= (byte)((pVal >>> 8) & 0xff);
	}

	public static short getShort(byte[] pData, int pIndex) {
		return (short)((pData[pIndex] & 0xff) | ((pData[pIndex + 1] & 0xff) << 8));
	}

	public static String getStr(byte[] pData, int pIndex, int pLen) {
		String ls = new String(pData, pIndex, pLen);
		return ls;
	}
	public static String getStr(byte[] pData, int pIndex) {
		return getStr(pData, pIndex, pData.length - pIndex);
	}

	public static void setStr(byte[] pData, int pIndex, String pVal) {
		byte[] lb = pVal.getBytes();
		for (int li = pIndex; li < pData.length; li++) {
			pData[li] = lb[li - pIndex];
		}
	}

	// 状況判断要関数群
	public static boolean canBlockBeSeen(Entity pEntity, int x, int y, int z, boolean toTop, boolean do1, boolean do2) {
		// ブロックの可視判定
		Vec3 vec3d = new Vec3(pEntity.posX, pEntity.posY + pEntity.getEyeHeight(), pEntity.posZ);
		Vec3 vec3d1 = new Vec3(x + 0.5D, y + (toTop ? 0.9D : 0.5D), z + 0.5D);
		
		MovingObjectPosition movingobjectposition = pEntity.worldObj.rayTraceBlocks(vec3d, vec3d1, do1, do2, false);
		if (movingobjectposition == null) {
			return false;
		}
		if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
			if (movingobjectposition.hitVec.xCoord == MathHelper.floor_double(vec3d1.xCoord) && 
				movingobjectposition.hitVec.yCoord == MathHelper.floor_double(vec3d1.yCoord) &&
				movingobjectposition.hitVec.zCoord == MathHelper.floor_double(vec3d1.zCoord)) {
				return true;
			}
		}
		return false;
	}

	public static boolean setPathToTile(EntityLiving pEntity, TileEntity pTarget, boolean flag) {
		// Tileまでのパスを作る
		PathNavigate lpn = pEntity.getNavigator();
		float lspeed = 1.0F;
		// TODO 1.8後回し いらん気もする
		/*
		// 向きに合わせて距離を調整
		int i = (pTarget.getPos().getY() == MathHelper.floor_double(pEntity.posY) && flag) ? 2 : 1;
		switch (pEntity.worldObj.getBlockState(new BlockPos(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ())).getValue()) {
		case 3:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ() + i, lspeed);
		case 2:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		case 5:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX() + 1, pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		case 4:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX() - i, pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		default:
			return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
		}
		*/
		return lpn.tryMoveToXYZ(pTarget.getPos().getX(), pTarget.getPos().getY(), pTarget.getPos().getZ(), lspeed);
	}

	/**
	 * Modloader環境下で空いているEntityIDを返す。
	 * 有効な値を獲得できなければ-1を返す。
	 */
	/*
	private static int getNextEntityID(boolean isLiving) {
		if (isLiving) {
			// 生物用
			for (int li = 1; li < 256; li++) {
				if (EntityList.getClassFromID(li) == null) {
					return li;
				}
			}
		} else {
			// 物用
			for (int li = MMMLib.cfg_startVehicleEntityID; li < MMMLib.cfg_startVehicleEntityID + 2048; li++) {
				if (EntityList.getClassFromID(li) == null) {
					return li;
				}
			}
		}
		return -1;
	}
	*/

	/**
	 * Entityを返す。
	 */
	public static Entity getEntity(byte[] pData, int pIndex, World pWorld) {
		return pWorld.getEntityByID(MMM_Helper.getInt(pData, pIndex));
	}

	/**
	 * 変数「avatar」から値を取り出し戻り値として返す。
	 * avatarが存在しない場合は元の値を返す。
	 * avatarはEntityLiving互換。
	 */
	public static Entity getAvatarEntity(Entity pEntity){
		// littleMaid用コードここから
		if (pEntity == null) return null;
		try {
			// 射手の情報をEntityLittleMaidAvatarからEntityLittleMaidへ置き換える
			Field field = pEntity.getClass().getField("avatar");
			pEntity = (EntityLivingBase)field.get(pEntity);
		} catch (NoSuchFieldException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		// ここまで
		return pEntity;
	}

	/**
	 * 変数「maidAvatar」から値を取り出し戻り値として返す。
	 * maidAvatarが存在しない場合は元の値を返す。
	 * maidAvatarはEntityPlayer互換。
	 */
	public static Entity getAvatarPlayer(Entity entity) {
		// メイドさんチェック
		try {
			Field field = entity.getClass().getField("maidAvatar");
			entity = (Entity)field.get(entity);
		}
		catch (NoSuchFieldException e) {
		}
		catch (Exception e) {
		}
		return entity;
	}

	/**
	 * プレーヤのインベントリからアイテムを減らす
	 */
	public static ItemStack decPlayerInventory(EntityPlayer par1EntityPlayer, int par2Index, int par3DecCount) {
		if (par1EntityPlayer == null) {
			return null;
		}
		
		if (par2Index == -1) {
			par2Index = par1EntityPlayer.inventory.currentItem;
		}
		ItemStack itemstack1 = par1EntityPlayer.inventory.getStackInSlot(par2Index);
		if (itemstack1 == null) {
			return null;
		}
		
		if (!par1EntityPlayer.capabilities.isCreativeMode) {
			// クリエイティブだと減らない
			itemstack1.stackSize -= par3DecCount;
		}
		
		if (itemstack1.getItem() instanceof ItemPotion) {
			if(itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Items.glass_bottle, par3DecCount));
				return null;
			}
			par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle, par3DecCount));
		} else {
			if (itemstack1.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par2Index, null);
				return null;
			}
		}
		
		return itemstack1;
	}

	protected static float convRevision(String pRev) {
		Pattern lp = Pattern.compile("(\\d+)(\\w*)");
		Matcher lm = lp.matcher(pRev);
		float lf = 0;
		if (lm.find()) {
			lf = Integer.valueOf(lm.group(1));
			if (!lm.group(2).isEmpty()) {
				lf += (lm.group(2).charAt(0) - 96) * 0.01;
			}
		}
		return lf;
	}
	protected static float convRevision() {
		// TODO いる？
		return 0;// convRevision(MMMLib.Revision);
	}

	/**
	 * 指定されたリビジョンよりも古ければ例外を投げてストップ
	 */
	public static void checkRevision(String pRev) {
		if (convRevision() < convRevision(pRev)) {
			// 適合バージョンではないのでストップ
		// TODO ★後回し
		//	ModLoader.getLogger().warning("you must check MMMLib revision.");
		//	throw new RuntimeException("The revision of MMMLib is old.");
		}
	}

	/**
	 * EntityListに登録されていいるEntityを置き換える。
	 */
	public static void replaceEntityList(Class pSrcClass, Class pDestClass) {
		// EntityList登録情報を置き換え
		// 古いEntityでもスポーンできるように一部の物は二重登録
		try {
			// stringToClassMapping
			Map lmap;
			int lint = 0;
			String ls = "";
			lmap = EntityList.stringToClassMapping;
			for (Entry<String, Class> le : ((Map<String, Class>)lmap).entrySet()) {
				if (le.getValue() == pSrcClass) {
					le.setValue(pDestClass);
				}
			}
			// classToStringMapping
			lmap = EntityList.classToStringMapping;
			if (lmap.containsKey(pSrcClass)) {
				ls = (String)lmap.get(pSrcClass);
//				lmap.remove(pSrcClass);
				lmap.put(pDestClass, ls);
			}
			// IDtoClassMapping
			lmap = EntityList.idToClassMapping;
			for (Entry<Integer, Class> le : ((Map<Integer, Class>)lmap).entrySet()) {
				if (le.getValue() == pSrcClass) {
					le.setValue(pDestClass);
				}
			}
			// classToIDMapping なんぜコイツだけprivateのまま？
			lmap = (Map)ObfuscationReflectionHelper.getPrivateValue(EntityList.class, null, "field_75624_e", "classToIDMapping");
			if (lmap.containsKey(pSrcClass)) {
				lint = (Integer)lmap.get(pSrcClass);
//				lmap.remove(pSrcClass);
				lmap.put(pDestClass, lint);
			}
			replaceEntitys.put(pSrcClass, pDestClass);
			MMMLib.Debug("Replace %s -> %s(EntityListID: %d, EntityListString: %s)", pSrcClass.getSimpleName(), pDestClass.getSimpleName(), lint, ls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void replaceCreatureList(List<SpawnListEntry> pMobs) {
		if (pMobs == null) return;
		for (Entry<Class, Class> le : replaceEntitys.entrySet()) {
			for (int j = 0; j < pMobs.size(); j++) {
				if (pMobs.get(j).entityClass == le.getKey()) {
					pMobs.get(j).entityClass = le.getValue();
					MMMLib.Debug("ReplaceCreatureList: %s -> %s", le.getKey().getSimpleName(), le.getValue().getSimpleName());
				}
			}
		}
	}

	/**
	 * バイオームの設定Entityを置き換えられたEntityへ置き換える。
	 * 基本的にMMMLib以外からは呼ばれない。
	 */
	protected static void replaceBaiomeSpawn() {
		// バイオームの発生処理をのっとる
		if (replaceEntitys.isEmpty()) return;
		BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();
		for (int i = 0; i < biomeList.length; i++) {
			if (biomeList[i] == null) continue;
			MMMLib.Debug("ReplaceBaiomeSpawn:%s", biomeList[i].biomeName);
			MMMLib.Debug("[Creature]");
			replaceCreatureList(biomeList[i].getSpawnableList(EnumCreatureType.CREATURE));//.spawnableCreatureList);
			MMMLib.Debug("[WaterCreature]");
			replaceCreatureList(biomeList[i].getSpawnableList(EnumCreatureType.WATER_CREATURE));//.spawnableWaterCreatureList);
			MMMLib.Debug("[CaveCreature]");
			replaceCreatureList(biomeList[i].getSpawnableList(EnumCreatureType.AMBIENT));//.spawnableCaveCreatureList);
			MMMLib.Debug("[Monster]");
			replaceCreatureList(biomeList[i].getSpawnableList(EnumCreatureType.MONSTER));//.spawnableMonsterList);
		}
	}

	/**
	 * 視線の先にいる最初のEntityを返す
	 * @param pEntity
	 * 視点
	 * @param pRange
	 * 視線の有効距離
	 * @param pDelta
	 * 時刻補正
	 * @param pExpand
	 * 検知領域の拡大範囲
	 * @return
	 */
	public static Entity getRayTraceEntity(EntityLivingBase pEntity, double pRange, float pDelta, float pExpand) {
		Vec3 lvpos = new Vec3(
				pEntity.posX, pEntity.posY + pEntity.getEyeHeight(), pEntity.posZ);
//		Vec3 lvpos = pEntity.getPosition(pDelta).addVector(0D, pEntity.getEyeHeight(), 0D);
		Vec3 lvlook = pEntity.getLook(pDelta);
		Vec3 lvview = lvpos.addVector(lvlook.xCoord * pRange, lvlook.yCoord * pRange, lvlook.zCoord * pRange);
		Entity ltarget = null;
		List llist = pEntity.worldObj.getEntitiesWithinAABBExcludingEntity(pEntity, pEntity.getEntityBoundingBox().addCoord(lvlook.xCoord * pRange, lvlook.yCoord * pRange, lvlook.zCoord * pRange).expand(pExpand, pExpand, pExpand));
		double ltdistance = pRange * pRange;
		
		for (int var13 = 0; var13 < llist.size(); ++var13) {
			Entity lentity = (Entity)llist.get(var13);
			
			if (lentity.canBeCollidedWith()) {
				float lexpand = lentity.getCollisionBorderSize() + 0.3F;
				AxisAlignedBB laabb = lentity.getEntityBoundingBox().expand(lexpand, lexpand, lexpand);
				MovingObjectPosition lmop = laabb.calculateIntercept(lvpos, lvview);
				
				if (laabb.isVecInside(lvpos)) {
					if (0.0D < ltdistance || ltdistance == 0.0D) {
						ltarget = lentity;
						ltdistance = 0.0D;
					}
				} else if (lmop != null) {
					double ldis = lvpos.squareDistanceTo(lmop.hitVec);
					
					if (ldis < ltdistance || ltdistance == 0.0D) {
						ltarget = lentity;
						ltdistance = ldis;
					}
				}
			}
		}
		return ltarget;
	}


	// Forge対策

	/**
	 * Forge対策用のメソッド
	 */
	public static ItemStack getSmeltingResult(ItemStack pItemstack) {
/*
		if (methGetSmeltingResultForge != null) {
			try {
				return (ItemStack)methGetSmeltingResultForge.invoke(FurnaceRecipes.smelting(), pItemstack);
			}catch (Exception e) {
			}
		}
*/
		return FurnaceRecipes.instance().getSmeltingResult(pItemstack);
	}

	/**
	 * アイテムに追加効果が在るかを判定する。
	 * Forge対策。
	 * @param pItemStack
	 * @return
	 */
	public static boolean hasEffect(ItemStack pItemStack) {
		// マジClientSIDEとか辞めてほしい。
		if (pItemStack != null) {
			Item litem = pItemStack.getItem();
			if (litem instanceof ItemPotion) {
				List llist = ((ItemPotion)litem).getEffects(pItemStack);
				return llist != null && !llist.isEmpty();
			}
		}
		return false;
	}

	/**
	 * Blockのインスタンスを置き換える。
	 * static finalの変数に対して行うのでForgeでは無効。
	 * @param pOriginal
	 * @param pReplace
	 * @return
	 */
	/*
	public static boolean replaceBlock(Block pOriginal, Block pReplace) {
		if (isForge) {
			return false;
		}
		try {
			// Blockのstatic final分の置換え
			Field[] lfield = Block.class.getDeclaredFields();
			for (int li = 0; li < lfield.length; li++) {
				if (!Modifier.isStatic(lfield[li].getModifiers())) {
					// static以外は対象外
					continue;
				}
				
				Object lobject = lfield[li].get(null);
				if (lobject == pOriginal) {
					ModLoader.setPrivateValue(Block.class, null, li, pReplace);
					return true;
				}
			}
		}
		catch(Exception exception) {
		}
		return false;
	}
	*/

	/**
	 * 16進数の文字列をIntへ変換する。
	 * 0xffffffff対策。
	 * @param pValue
	 * @return
	 */
	public static int getHexToInt(String pValue) {
		String ls = "00000000".concat(pValue);
		int llen = ls.length();
		int li = Integer.parseInt(ls.substring(llen - 4, llen), 16);
		int lj = Integer.parseInt(ls.substring(llen - 8, llen - 4), 16);
		return (lj << 16) | li;
	}

	/**
	 *  アイテムに設定された攻撃力を見る
	 * @param pItemStack
	 * @return
	 */
	public static double getAttackVSEntity(ItemStack pItemStack) {
		AttributeModifier lam = (AttributeModifier)pItemStack.getAttributeModifiers().get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		return lam == null ? 0 : lam.getAmount();
	}

}
