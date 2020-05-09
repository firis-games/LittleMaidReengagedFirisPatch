package net.blacklab.lmr.entity.littlemaid;

import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Aimebow;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Bloodsuck;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Freedom;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_LooksSugar;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_OverDrive;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Register;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Tracer;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Wait;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_Working;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_looksWithInterest;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_looksWithInterestAXIS;
import static net.blacklab.lmr.util.Statics.dataWatch_Flags_remainsContract;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.blacklab.lib.minecraft.item.ItemUtil;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.LittleMaidReengaged.LMItems;
import net.blacklab.lmr.achievements.AchievementsLMRE;
import net.blacklab.lmr.achievements.AchievementsLMRE.AC;
import net.blacklab.lmr.client.entity.EntityLittleMaidAvatarSP;
import net.blacklab.lmr.config.LMRConfig;
import net.blacklab.lmr.entity.littlemaid.ai.EntityAILMCollectItem;
import net.blacklab.lmr.entity.littlemaid.ai.EntityAILMWait;
import net.blacklab.lmr.entity.littlemaid.ai.action.EntityAILMOpenDoor;
import net.blacklab.lmr.entity.littlemaid.ai.action.EntityAILMRestrictOpenDoor;
import net.blacklab.lmr.entity.littlemaid.ai.attack.EntityAILMAttackArrow;
import net.blacklab.lmr.entity.littlemaid.ai.attack.EntityAILMAttackOnCollide;
import net.blacklab.lmr.entity.littlemaid.ai.motion.EntityAILMBeg;
import net.blacklab.lmr.entity.littlemaid.ai.motion.EntityAILMWatchClosest;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMBegMove;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMFindBlock;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMFleeRain;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMFollowOwner;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMMoveTowardsRestriction;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMSwimming;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMTeleport;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMTracerMove;
import net.blacklab.lmr.entity.littlemaid.ai.move.EntityAILMWander;
import net.blacklab.lmr.entity.littlemaid.controller.LMExperienceController;
import net.blacklab.lmr.entity.littlemaid.controller.LMJobController;
import net.blacklab.lmr.entity.littlemaid.controller.LMSoundController;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Basic;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Playing;
import net.blacklab.lmr.entity.littlemaid.mode.EntityMode_Playing.PlayRole;
import net.blacklab.lmr.entity.littlemaid.trigger.ModeTrigger;
import net.blacklab.lmr.entity.maidmodel.IMultiModelEntity;
import net.blacklab.lmr.entity.maidmodel.ModelConfigCompound;
import net.blacklab.lmr.entity.maidmodel.ModelConfigCompoundLittleMaid;
import net.blacklab.lmr.entity.pathnavigate.PathNavigatorLittleMaid;
import net.blacklab.lmr.inventory.InventoryLittleMaid;
import net.blacklab.lmr.item.ItemTriggerRegisterKey;
import net.blacklab.lmr.network.GuiHandler;
import net.blacklab.lmr.network.LMRMessage;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.util.Counter;
import net.blacklab.lmr.util.EnumMaidMotion;
import net.blacklab.lmr.util.EnumSound;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.ModelCapsLittleMaid;
import net.blacklab.lmr.util.SwingStatus;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.blacklab.lmr.util.helper.ItemHelper;
import net.blacklab.lmr.util.helper.OwnableEntityHelper;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.EnumColor;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLittleMaid extends EntityTameable implements IMultiModelEntity {

	// 定数はStaticsへ移動
//	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("lmm.littleMaidMob".getBytes());
	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
//	protected static final UUID maidUUIDSneak = UUID.nameUUIDFromBytes("lmm.littleMaidMob.sneak".getBytes());
	protected static final UUID maidUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
	protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).setSaved(false);
	protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).setSaved(false);
	protected static AttributeModifier attSneakingSpeed = (new AttributeModifier(maidUUIDSneak, "Sneking speed ampd", -0.4D, 2)).setSaved(false);

	/** Absoption効果をクライアント側へ転送するのに使う */
	// TODO DataManagerは手探り
	protected static final DataParameter<Float> dataWatch_Absoption		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.FLOAT);
	/** メイドカラー(byte) */
	protected static final DataParameter<Byte> dataWatch_Color			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.BYTE);
	/**
	 * MSB|0x0000 0000|LSB<br>
	 *       |    |本体のテクスチャインデックス<br>
	 *       |アーマーのテクスチャインデックス<br>
	 */
	protected static final DataParameter<Integer> dataWatch_Texture		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** モデルパーツの表示フラグ(Integer) */
	protected static final DataParameter<Integer> dataWatch_Parts			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/**
	 * 各種フラグを一纏めにしたもの。
	 */
	// TODO VARって何
	protected static final DataParameter<Integer> dataWatch_Flags			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** 紐の持ち主のEntityID。 */
	protected static final DataParameter<Integer> dataWatch_Gotcha			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** メイドモード(Short) */
	protected static final DataParameter<String> dataWatch_Mode			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.STRING);
	/** 利き腕(Byte) */
	protected static final DataParameter<Integer> dataWatch_DominamtArm	= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** アイテムの使用判定、腕毎(Integer) */
	protected static final DataParameter<Integer> dataWatch_ItemUse		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	/** 保持経験値→メイド経験値で上書きな */
	protected static final DataParameter<Float> dataWatch_MaidExpValue		= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.FLOAT);
	// TODO この処遇は何とするか．EntityPlayer#ABSORPTIONはprivateだし
	/** EntityPlayer と EntityTameable で17番がかぶっているため、EntityPlayer側を28へ移動。 */
	protected static final DataParameter<Float> dataWatch_AbsorptionAmount	= EntityDataManager.createKey(EntityLittleMaidAvatarMP.class, DataSerializers.FLOAT);

	/**
	 * 自由設定値。
	 */
	public static final DataParameter<Integer> dataWatch_Free			= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	
	/** お座りモーションフラグ */
	//0:通常
	//1:お座り
	protected static final DataParameter<Integer> dataWatch_WaitMotion	= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	
	//手持ちアイテムのIDを強制で設定する用
	protected static final DataParameter<Integer> dataWatch_CurrentItem	= EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.VARINT);
	
	/** メイドさんのテクスチャ情報 */
	protected static final DataParameter<String> dataWatch_texture_LittleMaid = EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.STRING);
	protected static final DataParameter<String> dataWatch_texture_Armor_head = EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.STRING);
	protected static final DataParameter<String> dataWatch_texture_Armor_chest = EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.STRING);
	protected static final DataParameter<String> dataWatch_texture_Armor_legs = EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.STRING);
	protected static final DataParameter<String> dataWatch_texture_Armor_feet = EntityDataManager.createKey(EntityLittleMaid.class, DataSerializers.STRING);
	
	public int getDataWatchCurrentItem() {
		return this.dataManager.get(dataWatch_CurrentItem);
	}
	
	public void setDataWatchCurrentItem(int currentItem) {
		dataManager.set(EntityLittleMaid.dataWatch_CurrentItem, currentItem);
	}

//	protected long maidContractLimit;		// 契約失効日
	protected int maidContractLimit;		// 契約期間
	public long maidAnniversary;			// 契約日UIDとして使用
	private int maidDominantArm;			// 利き腕、1Byte
	
	/** テクスチャ関連のデータを管理 **/
	protected ModelConfigCompound modelConfigCompound;
	
//	public Map<String, EquippedStabilizer> maidStabilizer = new HashMap<String, EquippedStabilizer>();

	public float getLastDamage(){
		return lastDamage;
	}

//	public int fencerDefDetonateTick = 0;

//	public int jumpTicks;

	public InventoryLittleMaid maidInventory;
	public EntityPlayer maidAvatar;
//	public EntityCaps maidCaps;	// Client側のみ

//	public List<EntityModeBase> maidEntityModeList;
	/** Associate mode String with Tasks **/
//	public Map<String, EntityAITasks[]> modeAIMap;

	/** Mode String **/
//	public String maidMode;

	public boolean maidTracer;
	public boolean maidFreedom;
	public boolean maidWait;
	public int homeWorld;
	protected int maidTiles[][] = new int[9][3];
	public int maidTile[] = new int[3];
	public TileEntity maidTileEntity;

	// 動的な状態
	protected EntityPlayer mstatMasterEntity;	// 主
	protected double mstatMasterDistanceSq;		// 主との距離、計算軽量化用
	protected Entity mstatgotcha;				// ワイヤード用
	protected boolean mstatBloodsuck;
	protected boolean mstatClockMaid;
//	// マスク判定
//	protected int mstatMaskSelect;
	// 追加の頭部装備
//	protected boolean mstatCamouflage;
//	protected boolean mstatPlanter;
//	protected boolean isMaidChaseWait;
	protected int mstatWaitCount;
	protected int mstatTime;
	protected Counter maidOverDriveTime;
//	protected boolean mstatFirstLook;
	protected boolean mstatLookSuger;
	protected Counter workingCount;
	protected PlayRole mstatPlayingRole;
	protected String mstatWorking;
//	protected String mstatModeName;
	protected boolean mstatOpenInventory;

	protected int ticksSinceLastDamage = 0;

	// 腕振り
	public SwingStatus mstatSwingStatus[];
	public boolean mstatAimeBow;
	// 首周り
	private boolean looksWithInterest;
	private boolean looksWithInterestAXIS;
	private float rotateAngleHead;			// Angle
	private float prevRotateAngleHead;		// prevAngle

//	public float defaultWidth;
//	public float defaultHeight;

	/**
	 * 個体ごとに値をバラつかせるのに使う。
	 */
	public float entityIdFactor;

	public boolean weaponFullAuto;	// 装備がフルオート武器かどうか
	public boolean weaponReload;	// 装備がリロードを欲しているかどうか
	
//	public boolean maidCamouflage;

	// 音声関連はLittleMaidSoundManagerへまとめる
	// 音声
//	protected LMM_EnumSound maidAttackSound;
//	private EnumSound maidDamegeSound;
	//protected int maidSoundInterval;
//	protected float maidSoundRate;

	// クライアント専用音声再生フラグ
	//private CopyOnWriteArrayList<EnumSound> playingSound = new CopyOnWriteArrayList<EnumSound>();

	// 実験用
//	private int firstload = 100;
//	public String statusMessage = "";

	// AI
	public EntityAITempt aiTempt;
	public EntityAILMBeg aiBeg;
	public EntityAILMBegMove aiBegMove;
	public EntityAILMOpenDoor aiOpenDoor;
	public EntityAILMRestrictOpenDoor aiCloseDoor;
//	public EntityAILMAvoidPlayer aiAvoidPlayer;
	public EntityAILMFollowOwner aiFollow;
	public EntityAILMMoveTowardsRestriction aiMoveTowardsRestriction;
	public EntityAILMAttackOnCollide aiAttack;
	public EntityAILMAttackArrow aiShooting;
	public EntityAILMCollectItem aiCollectItem;
//	public EntityAILMRestrictRain aiRestrictRain;
	public EntityAILMFleeRain aiFreeRain;
	public EntityAILMWander aiWander;
//	public EntityAILMJumpToMaster aiJumpTo;
	public EntityAILMFindBlock aiFindBlock;
	public EntityAILMTracerMove aiTracer;
	public EntityAILMSwimming aiSwiming;
	public EntityAIPanic aiPanic;

	public EntityAILMWatchClosest aiWatchClosest;
	public EntityAILMTeleport aiJumpTo;
	// ActiveModeClass
//	private EntityModeBase maidActiveModeClass;
	public Profiler aiProfiler;

	//モデル
	//protected String textureNameMain;
	//protected String textureNameArmor;
//	public String getTextureNameMain() {
//		return dataManager.get(EntityLittleMaid.dataWatch_texture_LittleMaid);
//	}

	public int playingTick = 0;

//	public boolean isWildSaved = false;

//	// サーバ用テクスチャ処理移行フラグ
//	private boolean isMadeTextureNameFlag = false;

	protected int maidArmorVisible = 15;

	private boolean isInsideOpaque = false;
	protected Counter registerTick;
	protected String registerMode;

	// NX5 レベル関連
//	protected float maidExperience = 0;				// 経験値
//	protected LMExperienceController experienceHandler;	// 経験値アクション制御
//	private int gainExpBoost = 1;					// 取得経験値倍率

	/**
	 * 経験値管理クラス
	 */
	protected LMExperienceController experienceHandler;	// 経験値アクション制御

//	protected boolean modelChangeable = true;
	
	private ModeTrigger modeTrigger;
	
	// 音声関連をまとめるもの
	public LMSoundController soundManager = null;
	
	//　メイドさんの職業関連をまとめるもの
	public LMJobController jobController = null;
	
	
	/**
	 * コンストラクタ
	 * @param par1World
	 */
	public EntityLittleMaid(World par1World) {
		
		super(par1World);
		
		//インベントリ初期化
		// 初期設定
		maidInventory = new InventoryLittleMaid(this);
		if (par1World != null ) {
			if(par1World.isRemote)
			{
				maidAvatar = new EntityLittleMaidAvatarSP(par1World, this);
			}
			else
			{
				WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(par1World == null ? 0 : par1World.provider.getDimension());
				GameRules gameRules = worldServer.getGameRules();
				NBTTagCompound oldGameRules = null;
				try{
					oldGameRules = gameRules.writeToNBT();
					gameRules.setOrCreateGameRule("spawnRadius", "0");
					maidAvatar = new EntityLittleMaidAvatarMP(par1World, this);
				}catch(Throwable throwable){
					throwable.printStackTrace();
					maidAvatar = null;
					setDead();
					return;
				} finally {
					if (oldGameRules != null) {
						gameRules.readFromNBT(oldGameRules);
					}
				}
			}
		}
		mstatOpenInventory = false;
//		isMaidChaseWait = false;
		mstatTime = 6000;

		maidOverDriveTime = new Counter(5, 300, -32);
		workingCount = new Counter(11, 10, -10);
		registerTick = new Counter(200, 200, -20);
		mstatPlayingRole = PlayRole.NOTPLAYING;

		// モデルレンダリング用のフラグ獲得用ヘルパー関数
//		maidCaps = new EntityCaps(this);

//		textureNameMain = textureNameArmor = "default_"+ModelManager.defaultModelName;

		//メイドモデル設定準備
		modelConfigCompound = new ModelConfigCompoundLittleMaid(this, new ModelCapsLittleMaid(this));
		
		//サイズ初期化処理(通常メイドさんのサイズを設定)
		this.setSize(0.5F, 1.35F);
		
//		if (getEntityWorld().isRemote) {
//			// 形態形成場
//			modelConfigCompound.setColor((byte)0xc);
//			TextureBox ltb[] = new TextureBox[2];
//			ltb[0] = ltb[1] = ModelManager.instance.getDefaultTexture(this);
//			setTexturePackName(ltb);
//		}

		entityIdFactor = getEntityId() * 70;
		// 腕振り
		mstatSwingStatus = new SwingStatus[] { new SwingStatus(), new SwingStatus()};
		setDominantArm(rand.nextInt(mstatSwingStatus.length));

		// 再生音声
//		maidAttackSound = LMM_EnumSound.attack;
//		setMaidDamegeSound(EnumSound.hurt);
//		maidSoundInterval = 0;

		//dataManager.addObject(16, new Byte((byte)0));

		// 野生種用初期値設定
		setHealth(15F);

		// 移動用フィジカル設定
		((PathNavigateGround)navigator).setBreakDoors(true);

		// TODO:これはテスト
//		maidStabilizer.put("HeadTop", MMM_StabilizerManager.getStabilizer("WitchHat", "HeadTop"));

		// EntityModeの追加
		//maidEntityModeList = ((EntityModeHandler) LoaderSearcher.INSTANCE.getInstanceOfHandler(EntityModeHandler.class)).getModeList(this);//EntityModeManager.getModeList(this);
//		maidEntityModeList = MaidModeManager.instance.getModeList(this);
//
//		modeAIMap = new HashMap<>();

		//メイドさんの職業管理用
		initMaidMode();
//		mstatModeName = "";
//		// 初期化時実行コード
//		for (EntityModeBase lem : maidEntityModeList) {
//			lem.initEntity();
//		}
		
		modeTrigger = ModeTrigger.getDefaultInstance();
		setMaidMode(EntityMode_Basic.mmode_Wild);
		
		setExperienceHandler(new LMExperienceController(this));

		/*
		if(par1World.isRemote){
			NBTTagCompound t = new NBTTagCompound();
			writeEntityToNBT(t);
			readEntityFromNBT(t);
			t = null;
		}
		*/
		
		//メイドさんの音声管理用
		this.soundManager = new LMSoundController(this);
	}

	public IEntityLittleMaidAvatar getAvatarIF()
	{
		return (IEntityLittleMaidAvatar)maidAvatar;
	}

	/**
	 * 野良メイドの初期スポーンにのみ呼び出される
	 * 
	 * 野良テクスチャの反映処理
	 */
	/*
	public void onSpawnWithEgg() {
		// テクスチャーをランダムで選択
		String ls;
		if (LMRConfig.cfg_isFixedWildMaid) {
			ls = "default_Orign";
		} else {
			ls = ModelManager.instance.getRandomTextureString(rand);
		}
		modelConfigCompound.setTextureInitServer(ls);
		LittleMaidReengaged.Debug("init-ID:%d, %s:%d", getEntityId(), modelConfigCompound.getTextureBoxLittleMaid().textureName, modelConfigCompound.getColor());
//		setTexturePackIndex(textureData.getColor(), textureData.textureIndex);
		setTextureNameMain(modelConfigCompound.getTextureBoxLittleMaid().textureName);
		setTextureNameArmor(modelConfigCompound.getTextureBoxArmor().textureName);
//		recallRenderParamTextureName(textureModelNameForClient, textureArmorNameForClient);
		if(!isContract()) {
			setMaidMode(EntityMode_Basic.mmode_Wild);
			onSpawnWild();
		}
	}
	*/

//	protected void onSpawnWild() {
//		// 野生メイドの色設定処理
//		int nsize = 0;
//		byte avaliableColor[] = new byte[16];
//		TextureBoxBase box = getModelConfigCompound().getTextureBoxLittleMaid();
//		for (byte i=0; i<16; i++) {
//			if ((box.wildColor & 1<<i) > 0) {
//				avaliableColor[nsize++] = i;
//			}
//		}
//		setColor(avaliableColor[rand.nextInt(nsize)]);
//	}

	protected void applyEntityAttributes() {
		// 初期パラメーター
		super.applyEntityAttributes();
		// 対象移動可能範囲
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		// 基本移動速度
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		// 標準攻撃力１
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		// 攻撃速度
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(5D);
		// Wrap EntityPlayer
		getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		/*
		 * DataWatcherはクライアントからサーバーへは値を渡さない、渡せない。
		 */

		// 使用中リスト
		// 0:Flags
		// 1:Air
		// 2, 3, 4, 5,
		// 6: HP
		// 7, 8:PotionMap
		// 9: ArrowCount
		// 10: 固有名称
		// 11: 名付判定
		// 12: GrowingAge
		// 16: Tame(4), Sit(1)
		// 17: ownerName

		// maidAvater用EntityPlayer互換変数
		// 17 -> 18
		// 18 : Absoption効果をクライアント側へ転送するのに使う
		dataManager.register(EntityLittleMaid.dataWatch_Absoption, Float.valueOf(0));

		// 独自分
		// 19:maidColor
		dataManager.register(EntityLittleMaid.dataWatch_Color, (byte)0xc);
		// 20:選択テクスチャインデックス
		// TODO いらん？
		dataManager.register(EntityLittleMaid.dataWatch_Texture, Integer.valueOf(0));
		// 21:モデルパーツの表示フラグ
		dataManager.register(EntityLittleMaid.dataWatch_Parts, Integer.valueOf(0));
		// 22:状態遷移フラグ群(32Bit)、詳細はStatics参照
		dataManager.register(EntityLittleMaid.dataWatch_Flags, Integer.valueOf(0));
		// 23:GotchaID
		dataManager.register(EntityLittleMaid.dataWatch_Gotcha, Integer.valueOf(0));
		// 24:メイドモード
		dataManager.register(EntityLittleMaid.dataWatch_Mode, "");
		// 25:利き腕
		dataManager.register(EntityLittleMaid.dataWatch_DominamtArm, Integer.valueOf(0));
		// 26:アイテムの使用判定
		dataManager.register(EntityLittleMaid.dataWatch_ItemUse, Integer.valueOf(0));
		// 27:保持経験値
		/**
		 * TODO 旧コメにあった「互換性保持」の意味がよく分からなかった．
		 * バニラには27番を使うMobはいないし，旧版継承の問題？
		 * バニラ由来のexperienceValueはほとんど利用していないので上書き．
		 */
		dataManager.register(EntityLittleMaid.dataWatch_MaidExpValue , Float.valueOf(0));

		// TODO:test
		// 31:自由変数、EntityMode等で使用可能な変数。
		dataManager.register(EntityLittleMaid.dataWatch_Free, new Integer(0));

//		defaultWidth = width;
//		defaultHeight = height;
		
		//お座り状態
		dataManager.register(EntityLittleMaid.dataWatch_WaitMotion, Integer.valueOf(0));
		
		//手持ちアイテム同期用
		dataManager.register(EntityLittleMaid.dataWatch_CurrentItem, Integer.valueOf(-1));
		
		//メイドテクスチャ情報
		//メイドテクスチャの初期設定
		String littleMaidTexture = LMTextureBoxManager.defaultTextureModelName;
		dataManager.register(EntityLittleMaid.dataWatch_texture_LittleMaid, littleMaidTexture);
		dataManager.register(EntityLittleMaid.dataWatch_texture_Armor_head, littleMaidTexture);
		dataManager.register(EntityLittleMaid.dataWatch_texture_Armor_chest, littleMaidTexture);
		dataManager.register(EntityLittleMaid.dataWatch_texture_Armor_legs, littleMaidTexture);
		dataManager.register(EntityLittleMaid.dataWatch_texture_Armor_feet, littleMaidTexture);
	}

	/**
	 * メイドさんの職業関連を初期化
	 */
	public void initMaidMode() {
		
		// AI
		aiBeg = new EntityAILMBeg(this, 8F);
		aiBegMove = new EntityAILMBegMove(this, 1.0F);
		aiOpenDoor = new EntityAILMOpenDoor(this, true);
		aiCloseDoor = new EntityAILMRestrictOpenDoor(this);
//		aiAvoidPlayer = new EntityAILMAvoidPlayer(this, 1.0F, 3);
		aiFollow = new EntityAILMFollowOwner(this, 1.0F, 81D);
		aiMoveTowardsRestriction = new EntityAILMMoveTowardsRestriction(this, 1.0);
		aiAttack = new EntityAILMAttackOnCollide(this, 1.0F, true);
		aiShooting = new EntityAILMAttackArrow(this);
		aiCollectItem = new EntityAILMCollectItem(this, 1.0F);
//		aiRestrictRain = new EntityAILMRestrictRain(this);
		aiFreeRain = new EntityAILMFleeRain(this, 1.0F);
		aiWander = new EntityAILMWander(this, 1.0F);
		aiJumpTo = new EntityAILMTeleport(this);
		aiFindBlock = new EntityAILMFindBlock(this);
		aiSwiming = new EntityAILMSwimming(this);
		aiPanic = new EntityAIPanic(this, 2.0F);
		aiTracer = new EntityAILMTracerMove(this);
		aiSit = new EntityAILMWait(this);

		aiWatchClosest = new EntityAILMWatchClosest(this, EntityLivingBase.class, 10F);

		// TODO:これいらなくね？
		aiProfiler = getEntityWorld() != null && getEntityWorld().profiler != null ? getEntityWorld().profiler : null;

		// 動作モード用のTasksListを初期化
		EntityAITasks ltasks[] = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(aiProfiler);
		ltasks[1] = new EntityAITasks(aiProfiler);

		// default
		ltasks[0].addTask(1, aiSwiming);
		ltasks[0].addTask(2, aiSit);
		ltasks[0].addTask(3, aiJumpTo);
		ltasks[0].addTask(4, aiFindBlock);
		ltasks[0].addTask(5, aiShooting);
		ltasks[0].addTask(6, aiAttack);
		//ltasks[0].addTask(8, aiPanic);
		ltasks[0].addTask(10, aiBeg);
		ltasks[0].addTask(11, aiBegMove);
		// TODO Needed?
//		ltasks[0].addTask(20, aiAvoidPlayer);
		ltasks[0].addTask(21, aiFreeRain);
		ltasks[0].addTask(22, aiCollectItem);
		// 移動用AI
		ltasks[0].addTask(30, aiTracer);
		ltasks[0].addTask(31, aiFollow);
		ltasks[0].addTask(32, aiMoveTowardsRestriction);
		ltasks[0].addTask(33, aiWander);
		ltasks[0].addTask(34, new EntityAILeapAtTarget(this, 0.3F));
		// Mutexの影響しない特殊行動
		ltasks[0].addTask(40, aiCloseDoor);
		ltasks[0].addTask(41, aiOpenDoor);
//		ltasks[0].addTask(42, aiRestrictRain);
		
		// 首の動き単独
		ltasks[0].addTask(51, aiWatchClosest);
		ltasks[0].addTask(52, new EntityAILookIdle(this));

		//職業管理用コントローラー
		this.jobController = new LMJobController(this);
		this.jobController.init(ltasks[0], ltasks[1]);

//		// 追加分
//		for (EntityModeBase ieml : maidEntityModeList) {
//			ieml.addEntityMode(ltasks[0], ltasks[1]);
//		}
	}

	@Override
	protected PathNavigate createNavigator(World worldIn)
	{
		return new PathNavigatorLittleMaid(this, worldIn);
	}

	/**
	 * メイドさんの職業を追加する
	 */
	public void addMaidMode(String pModeName, EntityAITasks[] pAiTasks) {
//		modeAIMap.put(pModeName, pAiTasks);
		this.jobController.addMaidMode(pModeName, pAiTasks);
	}

	public String getMaidModeString() {
		return this.jobController.getMaidModeString();
	}

	public String getMaidModeStringForDisplay() {
		if (!isContract()) {
			return getMaidModeString();
		} else if (!isRemainsContract()) {
			return "Strike";
		} else if (isMaidWait()) {
			return "Wait";
		} else if (isPlaying()) {
			return "Playing";
		} else {
			String ls = getMaidModeString();
			ls = ls.substring(ls.lastIndexOf(":") + 1);
//			if (maidOverDriveTime.isEnable()) {
//				ls = "D-" + ls;
//			} else
			if (isTracer()&&isFreedom()) {
				ls = "T-" + ls;
			} else if (isFreedom()) {
				ls = "F-" + ls;
			}
			return ls;
		}
	}

	public boolean setMaidMode(String pname) {
		return setMaidMode(pname, false);
	}

/*
	public boolean isInWater() {
		IBlockState state = getEntityWorld().getBlockState(getPosition());
		return inWater ? true : state.getBlock().getMaterial(state) == Material.water;
	}
*/

	public int[][] getMaidTiles() {
		return maidTiles;
	}

	public void setMaidArmorVisible(int i){
		if(i<0) i=0;
		if(i>15)i=15;
		maidArmorVisible = i;
	}

	public void setMaidArmorVisible(boolean a, boolean b, boolean c, boolean d){
		setMaidArmorVisible((a?1:0)<<3 | (b?1:0)<<2 | (c?1:0)<<1 | (d?1:0));
	}

	public boolean isArmorVisible(int index){
		if(index<0||index>3) return true;
		return ((maidArmorVisible>>(3-index)) & 0x1) != 0;
	}

	public void syncMaidArmorVisible() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setInteger("Visible", maidArmorVisible);
		syncNet(LMRMessage.EnumPacketMode.SYNC_ARMORFLAG, tagCompound);
	}

	/**
	 * Client用
	 * 経験値ブースト値の取得
	 */
	public void requestExpBoost() {
		syncNet(LMRMessage.EnumPacketMode.SERVER_REQUEST_BOOST, null);
	}

	/**
	 * 経験値ブーストの同期
	 */
	public void syncExpBoost() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setInteger("Booster", this.experienceHandler.getExpBooster());

		syncNet(LMRMessage.EnumPacketMode.SYNC_EXPBOOST, tagCompound);
	}

	
	/**
	 * クライアントのモデル情報をサーバーへ送信する
	 */
	@SideOnly(Side.CLIENT)
	public void syncModelNamesToServer() {
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setString("Main", this.modelConfigCompound.getTextureBoxLittleMaid().getTextureModelName());
		tagCompound.setString("ArmorHead", getTextureBoxArmorNameWithDefault(this.modelConfigCompound.getTextureBoxArmor(EntityEquipmentSlot.HEAD)));
		tagCompound.setString("ArmorChest", getTextureBoxArmorNameWithDefault(this.modelConfigCompound.getTextureBoxArmor(EntityEquipmentSlot.CHEST)));
		tagCompound.setString("ArmorLegs", getTextureBoxArmorNameWithDefault(this.modelConfigCompound.getTextureBoxArmor(EntityEquipmentSlot.LEGS)));
		tagCompound.setString("ArmorFeet", getTextureBoxArmorNameWithDefault(this.modelConfigCompound.getTextureBoxArmor(EntityEquipmentSlot.FEET)));
		tagCompound.setByte("Color", (byte)this.modelConfigCompound.getColor());

		syncNet(LMRMessage.EnumPacketMode.SYNC_MODEL, tagCompound);
	}
	
	/**
	 * 互換対策Nullの場合はデフォルトへ差し替え
	 * @return
	 */
	private String getTextureBoxArmorNameWithDefault(LMTextureBox textureBox) {
		
		if (textureBox != null) return textureBox.getTextureModelName();
		
		return LMTextureBoxManager.defaultTextureModelName;
	}
	
	/**
	 * クライアントから受け取った情報を更新する
	 * @SideOnly(Side.SERVER)にしたかったが
	 * この処理はServerとClientの両方で呼ばれているためつけるとエラーになる
	 * 現時点ではこのままやる
	 * @param modelNameMain
	 */
	public void reciveModelNamesFromClient(NBTTagCompound tagCompound) {
		
		String modelNameMain = tagCompound.getString("Main");
		String modelNameArmorHead = tagCompound.getString("ArmorHead");
		String modelNameArmorChest = tagCompound.getString("ArmorChest");
		String modelNameArmorLegs = tagCompound.getString("ArmorLegs");
		String modelNameArmorFeet = tagCompound.getString("ArmorFeet");
		byte modelColor =  tagCompound.getByte("Color");
		
		dataManager.set(EntityLittleMaid.dataWatch_texture_LittleMaid, modelNameMain);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_head, modelNameArmorHead);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_chest, modelNameArmorChest);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_legs, modelNameArmorLegs);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_feet, modelNameArmorFeet);
		this.setColor(modelColor);
	}
	

	public void syncNet(LMRMessage.EnumPacketMode pMode, NBTTagCompound tagCompound) {
		LittleMaidReengaged.Debug("Id: %d, Send: %s", getEntityId(), pMode);
		if(getEntityWorld().isRemote){
			LMRNetwork.sendPacketToServer(pMode, getEntityId(), tagCompound);
		}else{
			LMRNetwork.sendPacketToAllPlayer(pMode, getEntityId(), tagCompound);
		}
	}

	public boolean setMaidMode(String pName, boolean pplaying) {
		// モードに応じてAIを切り替える
		velocityChanged = true;
		if (this.jobController.isJobAI(pName)) return false;
		
		//同一モードの場合は何もしない
		if (pName.equals(this.jobController.getMaidModeString())) return false;
		
		if (!pplaying) {
			mstatWorking = pName;
		}
//		mstatModeName = pName;
//		maidMode = pName;
		
		//メイドさんの職業を設定
		String maidMode = pName;
		
		//メイドモードの同期
		dataManager.set(EntityLittleMaid.dataWatch_Mode, maidMode);
		
		//AIの再設定
		EntityAITasks[] ltasks = this.jobController.getJobAIMap(maidMode);
		// AIを根底から書き換える
		if (ltasks.length > 0 && ltasks[0] != null) {
			setMaidModeAITasks(ltasks[0], tasks);
		} else {
			setMaidModeAITasks(null, tasks);
		}
		if (ltasks.length > 1 && ltasks[1] != null) {
			setMaidModeAITasks(ltasks[1], targetTasks);
		} else {
			setMaidModeAITasks(null, targetTasks);
		}

		// AIモードを初期化する
		maidAvatar.stopActiveHand();
		setSitting(false);
//		setSneaking(false);
//		setActiveModeClass(null);
//		aiJumpTo.setEnable(true);
//		aiFollow.setEnable(true);
		aiAttack.setEnable(true);
		aiShooting.setEnable(false);
//		aiAvoidPlayer.setEnable(true);
//		aiWander.setEnable(maidFreedom);
		setBloodsuck(false);
		clearTilePosAll();
		
//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			EntityModeBase iem = maidEntityModeList.get(li);
//			if (iem.setMode(maidMode)) {
//				this.jobController.setActiveModeClass(iem);
//				break;
//			}
//		}

		//職業に応じたAIモードの設定
		this.jobController.setActiveMaidMode(maidMode);

		getNextEquipItem();

		return true;
	}

	protected void setMaidModeAITasks(EntityAITasks pTasksSRC, EntityAITasks pTasksDEST) {
		// 既存のAIを削除して置き換える。
		// 動作をクリア
		List<EntityAIBase> originAIs = new ArrayList<EntityAIBase>();
		for (Iterator<EntityAITaskEntry> iterator = pTasksDEST.taskEntries.iterator(); iterator.hasNext(); ) {
			EntityAITaskEntry lEntry = iterator.next();
			originAIs.add(lEntry.action);
		}
		for (EntityAIBase pAiBase: originAIs) {
			pTasksDEST.removeTask(pAiBase);
		}

		// 動作追加
		for (Iterator<EntityAITaskEntry> iterator = pTasksSRC.taskEntries.iterator(); iterator.hasNext(); ) {
			pTasksDEST.taskEntries.add(iterator.next());
		}
	}
	public static ArrayList<EntityAITaskEntry> getEntityAITasks_taskEntries(EntityAITasks task)
	{
		return new ArrayList<>(task.taskEntries);
	}
	public static ArrayList<EntityAITaskEntry> getEntityAITasks_executingTaskEntries(EntityAITasks task)
	{
		return new ArrayList<>(task.taskEntries);
	}

	/**
	 * 適用されているモードクラス
	 * This method is nullable, so check if isActiveModeClass() is true
	 */
//	@Nonnull
//	public final EntityModeBase getActiveModeClass() {
//		return maidActiveModeClass;
//	}

//	public void setActiveModeClass(@Nonnull EntityModeBase pEntityMode) {
//		if (pEntityMode == null) {
//			throw new IllegalArgumentException("activeMode cannot be null");
//		}
//		maidActiveModeClass = pEntityMode;
//	}

//	public final boolean isActiveModeClass() {
//		return getActiveModeClass() != null;
//	}

	public Counter getWorkingCount() {
		return workingCount;
	}

	/**
	 * ダメージ音声の割込み
	 */
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		if(getHealth() > 0.0F) playLittleMaidDamageVoiceSound(false);
		return null;
	}

	/**
	 * 死亡ダメージ音声の割込み
	 */
	@Override
	protected SoundEvent getDeathSound() {
		playLittleMaidVoiceSound(EnumSound.death, false);
		return null;
	}

/*
	@Override
	protected SoundEvent getLivingSound() {
		// 普段の声
		//LMM_LittleMaidMobNX.Debug("DEBUG INFO=tick %d", livingSoundTick);
		//livingSoundTick--;
		return null;//"dummy.living";
	}
*/

//	public EnumSound getMaidDamegeSound() {
//		return maidDamegeSound;
//	}

//	public void setMaidDamegeSound(EnumSound maidDamegeSound) {
//		this.maidDamegeSound = maidDamegeSound;
//	}
	
	/**
	 * ダメージ音声を取得する
	 * @return
	 */
	public EnumSound getMaidDamegeSound() {
		return this.soundManager.getDamageSound();
	}
	
	/**
	 * ダメージ音声を設定する
	 * @param maidDamegeSound
	 */
	public void setMaidDamegeSound(EnumSound maidDamegeSound) {
		this.soundManager.setDamageSound(maidDamegeSound);
	}
	
	/**
	 * 文字列指定による音声再生
	 * 標準のplaySoundを呼び出すための形式を整える
	 */
	public void playSound(String soundName) {
		this.playSound(soundName, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
	}
	
	/**
	 * 文字列指定による音声再生
	 * 標準のplaySoundを呼び出すための形式を整える
	 */
	public void playSound(String soundName, float pitch) {
		LittleMaidReengaged.Debug("REQUESTED PLAYING SOUND: %s", soundName);
		
		SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
		if (soundEvent != null) {
			if (getEntityWorld().isRemote) {
				LittleMaidReengaged.Debug("PLAYING SOUND EVENT-%s", soundEvent.getSoundName().toString());
			}
			this.playSound(soundEvent, this.getSoundVolume(), pitch);
		}
		
	}

	/**
	 * 文字列指定による音声再生
	 */
//	public void playSound(String pName, float pitch) {
//		LittleMaidReengaged.Debug("REQUESTED PLAYING SOUND: %s", pName);
////		if(!getEntityWorld().isRemote) {
//			SoundEvent sEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(pName));
//			if (sEvent != null) {
//				if (getEntityWorld().isRemote)
//					LittleMaidReengaged.Debug("PLAYING SOUND EVENT-%s", sEvent.getSoundName().toString());
//				playSound(sEvent, 1, pitch);
//			} else if (getEntityWorld().isRemote) {
//				// ClientOnlyなダメ元
//				sEvent = new SoundEvent(new ResourceLocation(pName));
//				LittleMaidReengaged.Debug("PLAYING SOUND EVENT-%s", sEvent.getSoundName().toString());
//				try {
//					playSound(sEvent, 1, pitch);
//				} catch (Exception exception) {}
//			}
////		}
//	}

	/**
	 * ネットワーク対応音声再生
	 */
	/*
	public void playSound(EnumSound enumsound, boolean force) {
		if (getEntityWorld().isRemote && enumsound!=EnumSound.Null && maidSoundInterval <= 0) {
			if (!force) {
				if(Math.random() > LMRConfig.cfg_voiceRate) {
					return;
				}
			}
			playingSound.add(enumsound);
			maidSoundInterval = 10;
//			float lpitch = LMM_LittleMaidMobNX.cfg_VoiceDistortion ? (rand.nextFloat() * 0.2F) + 0.95F : 1.0F;
//			LMM_LittleMaidMobNX.proxy.playLittleMaidSound(getEntityWorld(), posX, posY, posZ, s, getSoundVolume(), lpitch, false);
		}
	}
	*/
	
	/**
	 * メイドさんの音声再生処理
	 * @param enumsound
	 * @param isRandom
	 */
	public void playLittleMaidVoiceSound(EnumSound sound, boolean isRandom) {
		this.soundManager.playVoiceSound(sound, isRandom);
	}
	
	/**
	 * メイドさんのダメージ音声音声処理
	 * @param isRandom
	 */
	public void playLittleMaidDamageVoiceSound(boolean isRandom) {
		this.soundManager.playDamageVoiceSound(isRandom);
	}
	
	/**
	 * 音声再生用。
	 * 通常の再生ではネットワーク越しになるのでその対策。
	 */
	/*
	public void playLittleMaidSound(EnumSound enumsound, boolean force) {
		// 音声の再生
		if (enumsound == EnumSound.Null) return;
//		if (!force && rand.nextFloat() > LMRConfig.cfg_voiceRate) return;
		if (!getEntityWorld().isRemote) {
			// Server
//			if((LMM_LittleMaidMobNX.cfg_ignoreForceSound || !force) && new Random().nextInt(LMM_LittleMaidMobNX.cfg_soundPlayChance)!=0) return;
			LittleMaidReengaged.Debug("id:%d-%s, seps:%04x-%s", getEntityId(), "Server",  enumsound.index, enumsound.name());

			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("Sound", enumsound.index);
			tagCompound.setBoolean("Force", force);

			syncNet(LMRMessage.EnumPacketMode.CLIENT_PLAY_SOUND, tagCompound);
		}
	}
	*/

	/*
	@Override
	public void playLivingSound() {
		if (!getEntityWorld().isRemote) return;
		// 普段の声
		//LMM_LittleMaidMobNX.Debug("DEBUG INFO=tick %d", livingSoundTick);
		//livingSoundTick--;
		if(getAttackTarget()!=null/ * || Math.random() > 0.3 * /) return;
		EnumSound so = EnumSound.Null;
		if (getHealth() < 10)
			so = EnumSound.living_whine;
		else / * if (rand.nextFloat() < maidSoundRate) * /{
			if (mstatTime > 23500 || mstatTime < 1500) {
				so = EnumSound.living_morning;
			} else if (mstatTime < 12500) {
				if (isContract()) {
					Biome Biome = getEntityWorld().getBiome(getPosition());
					TempCategory ltemp = Biome.getTempCategory();
					if (ltemp == TempCategory.COLD) {
						so = EnumSound.living_cold;
					} else if (ltemp == TempCategory.WARM) {
						so = EnumSound.living_hot;
					} else {
						so = EnumSound.living_daytime;
					}
					if (getEntityWorld().isRaining()) {
						if (Biome.getEnableSnow()) {
							so = EnumSound.living_snow;
						} else {
							so = EnumSound.living_rain;
						}
					}
				} else {
					so = EnumSound.living_daytime;
				}
			} else {
				so = EnumSound.living_night;
			}
		}

		//if(livingSoundTick<=0){
			LittleMaidReengaged.Debug("id:%d LivingSound:%s", getEntityId(), getEntityWorld() == null ? "null" : getEntityWorld().isRemote ? "Client" : "Server");
//			if(!getEntityWorld().isRemote)
//				playLittleMaidSound(so, false);
//			else
		// LivingSoundの再生調整はonEntityUpdateで行う
		//playSound(so, true);
		this.playLittleMaidVoiceSound(so, false);
		//	livingSoundTick = 1;
		//}
	}
	*/
	
	/**
	 * メイドさんの普段の啼声
	 */
	@Override
	public void playLivingSound() {
		
		//クライアントのみ
		if (!getEntityWorld().isRemote) return;
		
		//戦闘中はしゃべらない
		if(getAttackTarget() != null) return;
		
		//現在時刻
		int worldtime = (int)(getEntityWorld().getWorldTime() % 24000);
		
		EnumSound sound = EnumSound.Null;
		
		boolean isClock = maidInventory.getInventorySlotContainItem(Items.CLOCK) != -1;
		
		//HPが低い時
		if (getHealth() < 10) {
			sound = EnumSound.living_whine;
			
		//天気による
		} else if (getEntityWorld().isRaining()) {
			Biome biome = world.getBiome(getPosition());
			//雪か雨
			if (biome.isSnowyBiome()) {
				//雪の時
				sound = EnumSound.living_snow;
			} else {
				//雨の時
				sound = EnumSound.living_rain;
			}
		//時計所持の朝の啼き声
		} else if ((worldtime > 23500 || worldtime < 1500) && isClock) {
			sound = EnumSound.living_morning;
		//時計所持の夜の啼き声
		} else if (12500 < worldtime && isClock) {
			sound = EnumSound.living_night;
		//こんにちはの挨拶
		} else {
			sound = EnumSound.living_daytime;
			//バイオームの気温でセリフ変更
			Biome biome = getEntityWorld().getBiome(getPosition());
			TempCategory tempCategory = biome.getTempCategory();
			if (tempCategory == TempCategory.COLD) {
				sound = EnumSound.living_cold;
			} else if (tempCategory == TempCategory.WARM) {
				sound = EnumSound.living_hot;
			}
		}
		
		LittleMaidReengaged.Debug("id:%d LivingSound:%s", getEntityId(), getEntityWorld() == null ? "null" : getEntityWorld().isRemote ? "Client" : "Server");
		
		//Sound設定
		this.playLittleMaidVoiceSound(sound, true);
	}
	
	/**
	 * 時計もちメイドさんのVoiceフラグ
	 * 0:リセット状態
	 * 1:朝の挨拶後
	 * 2:夜の挨拶後
	 */
	private int littleMaidClockVoiceFlg = 0;
	
	/**
	 * 時計もちメイドさんの啼声
	 */
	public void playLivingSoundLittleMaidWithClock() {
		
		//サーバーで判断
		if (getEntityWorld().isRemote) return;
		
		//1秒に1回だけ処理を行う
		if (this.ticksExisted % 20 != 0) return;
		
		//契約メイドさんのみ
		if (!this.isContractEX()) return;
		
		//戦闘中はしゃべらない
		if(getAttackTarget() != null) return;
		
		//時計所持
		if (maidInventory.getInventorySlotContainItem(Items.CLOCK) == -1) return;
		
		//ご主人さまとの距離
		EntityPlayer player = getMaidMasterEntity();
		if (player == null) return;
		double masterDistanceSq = getDistanceSq(player);
		
		//5マス以上離れている場合は何もしない
		double range = 5.0D * 5.0D;
		if (masterDistanceSq > range) return;
		
		//視線が通っているか
		if (!getEntitySenses().canSee(player)) return;
		
		EnumSound sound = EnumSound.Null;

		//現在時刻
		int worldtime = (int)(getEntityWorld().getWorldTime() % 24000);
		
		//時間帯によって変える
		//6:00 - 7:30まで
		if (littleMaidClockVoiceFlg != 1 && worldtime <= 1500) {
			//朝の挨拶
			sound = EnumSound.goodmorning;
			littleMaidClockVoiceFlg = 1;
			
		//18:00 - 
		} else if (littleMaidClockVoiceFlg != 2 && 12000 <= worldtime && player.isPlayerSleeping()) {
			//おやすみなさい
			sound = EnumSound.goodnight;
			littleMaidClockVoiceFlg = 2;
			
		}
		
		//ボイス再生
		if (sound != EnumSound.Null) {
			playLittleMaidVoiceSound(sound, false);
		}
	}

	@Override
	public void onKillEntity(EntityLivingBase par1EntityLiving) {
		super.onKillEntity(par1EntityLiving);
		if (isBloodsuck()) {
			playLittleMaidVoiceSound(EnumSound.laughter, true);
		} else {
			setAttackTarget(null);
		}
	}

	@Override
	protected boolean canDespawn() {
		// デスポーン判定
		return isTamed()||hasCustomName() ? false : LMRConfig.cfg_canDespawn;
	}

	@Override
	public boolean getCanSpawnHere() {
		// スポーン可能か？
		if (LMRConfig.cfg_spawnLimit <= getMaidCount()) {
			LittleMaidReengaged.Debug("Spawn Limit.");
			return false;
		}
		/*
		// TODO:サーバー側で判定できないので意味なし?
		MMM_TextureBox lbox = MMM_TextureManager.instance.getTextureBox(textureBox[0]);
		if (getEntityWorld() == null || textureModel == null
				|| !textureBox[0].mo.getCanSpawnHere(getEntityWorld(), lx, ly, lz, this)) {
			mod_LMM_littleMaidMob.Debug(String.format("%s is can't spawn hear.", textureName));
			return false;
		}
		*/
		if (LMRConfig.cfg_Dominant) {
			// ドミナント
			return getEntityWorld().checkNoEntityCollision(getEntityBoundingBox())
					&& getEntityWorld().getCollisionBoxes(this, getEntityBoundingBox()).isEmpty()
					&& !getEntityWorld().containsAnyLiquid(getEntityBoundingBox())
					/*&& getBlockPathWeight(lx, ly, lz) >= 0.0F*/;
		}
		return super.getCanSpawnHere();
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox() {
		return super.getEntityBoundingBox();
	}

	@Override
	public void setDead() {
		if (mstatgotcha != null&&maidAvatar != null) {
			// 首紐をドロップ
			EntityItem entityitem = new EntityItem(getEntityWorld(), mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Items.STRING));
			getEntityWorld().spawnEntity(entityitem);
			mstatgotcha = null;
		}
		
		//PlayerAvatarも消去
		this.maidAvatar.setDead();
		
		super.setDead();
	}

	/**
	 * 読み込み領域内のメイドさんの数
	 */
	public int getMaidCount() {
		int lj = 0;
		for (int li = 0; li < getEntityWorld().loadedEntityList.size(); li++) {
			if (getEntityWorld().loadedEntityList.get(li) instanceof EntityLittleMaid) {
				lj++;
			}
		}
		return lj;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable var1) {
		// お子さんの設定
		return null;
	}

	// エフェクト表示
	public void showParticleFX(EnumParticleTypes s) {
		showParticleFX(s, 1D, 1D, 1D);
	}

	public void showParticleFX(EnumParticleTypes s, double d, double d1, double d2) {
		showParticleFX(s, d, d1, d2, 0D, 0D, 0D);
	}

	public void showParticleFX(EnumParticleTypes s, double d, double d1, double d2, double d3, double d4, double d5 ) {
		for (int i = 0; i < 7; i++) {
			double d6 = rand.nextGaussian() * d + d3;
			double d7 = rand.nextGaussian() * d1 + d4;
			double d8 = rand.nextGaussian() * d2 + d5;

			getEntityWorld().spawnParticle(s, (posX + rand.nextFloat() * width * 2.0F) - width, posY + 0.5D + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d6, d7, d8);
		}
	}

	@Override
	public void handleStatusUpdate(byte par1) {
		// getEntityWorld().setEntityState(this, (byte))で指定されたアクションを実行
		switch (par1) {
		case 10:
			// 不機嫌
			showParticleFX(EnumParticleTypes.SMOKE_NORMAL, 0.02D, 0.02D, 0.02D);
			break;
		case 11:
			// ゴキゲン
			double a = getContractLimitDays() / 7D;
			double d6 = a * 0.3D;
			double d7 = a;
			double d8 = a * 0.3D;

			getEntityWorld().spawnParticle(EnumParticleTypes.NOTE, posX, posY + height + 0.1D, posZ, d6, d7, d8);
			break;
		case 12:
			// 自由行動
			showParticleFX(EnumParticleTypes.REDSTONE, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
			break;
		case 13:
			// 不自由行動
			showParticleFX(EnumParticleTypes.SMOKE_NORMAL, 0.02D, 0.02D, 0.02D);
			break;
		case 14:
			// トレーサー
			showParticleFX(EnumParticleTypes.EXPLOSION_NORMAL, 0.3D, 0.3D, 0.3D, 0.0D, 0.0D, 0.0D);
			break;
		case 17:
			// トリガー登録
			showParticleFX(EnumParticleTypes.FIREWORKS_SPARK, 0.05D, 0.05D, 0.05D);
			break;
		default:
			super.handleStatusUpdate(par1);
		}
	}

	// ポーション効果のエフェクト
	public void setAbsorptionAmount(float par1) {
		// AbsorptionAmount
		if (par1 < 0.0F) {
			par1 = 0.0F;
		}

		dataManager.set(EntityLittleMaid.dataWatch_Absoption, Float.valueOf(par1));
	}
	public float getAbsorptionAmount() {
		return dataManager.get(EntityLittleMaid.dataWatch_Absoption);
	}


	public int colorMultiplier(float pLight, float pPartialTicks) {
		// 発光処理用
		int lbase = 0, i = 0, j = 0, k = 0, x = 0, y = 0;
		if (maidOverDriveTime.isDelay()) {
			j = 0x00df0000;
			if (maidOverDriveTime.isEnable()) {
				x = 128;
			}else{
				x = (int) (128 - maidOverDriveTime.getValue() * (128f / 32));
			}
		}
		if (registerTick.isDelay()) {
			k = 0x0000df00;
			if (registerTick.isEnable()) {
				y = 128;
			}else{
				y = (int) (128 - registerTick.getValue() * (128f / 20));
			}
		}
		i = x==0 ? (y>=128 ? y : 0) : (y==0 ? x : Math.min(x, y));
		lbase = i << 24 | j | k;

		if (this.jobController.isActiveModeClass()) {
			lbase = lbase | this.jobController.getActiveModeClass().colorMultiplier(pLight, pPartialTicks);
		}

		return lbase;
	}


	// AI関連
	protected boolean isAIEnabled() {
		// 新AI対応
		return true;
	}

	/**
	 * 敵味方識別
	 * falseなら敵対
	 */
	public boolean getIFF(Entity pEntity) {
		// 敵味方識別(敵=false)
		if (pEntity == null || pEntity == mstatMasterEntity) {
			return true;
		}

		int tt = IFF.getIFF(getMaidMasterUUID(), pEntity);
		switch (tt) {
		case IFF.iff_Enemy:
			return false;
		case IFF.iff_Friendry:
			return true;
		case IFF.iff_Unknown:
			if (isBloodsuck()) {
				// 血に餓えている時は敵
				return false;
			}
			if (pEntity instanceof EntityLittleMaid) {
				// お遊びモードのメイドには敵対しない
				if (((EntityLittleMaid)pEntity).isPlaying()) {
					return true;
				}
			}
			if (pEntity instanceof EntityCreature) {
				// 相手が何をターゲットにしているかで決まる
				Entity et = ((EntityCreature)pEntity).getAttackTarget();
				if (et != null && et == mstatMasterEntity) {
					return false;
				}
				if (et == this) {
					return false;
				}
				if (et instanceof EntityLittleMaid) {
					// 同じマスターのメイドを攻撃対象としている
					if (((EntityLittleMaid)et).getMaidMasterEntity() == mstatMasterEntity) {
						return false;
					}
				}
			}
			return true;

		default :
			return false;
		}
	}

	@Override
	public boolean canAttackClass(Class <? extends EntityLivingBase > par1Class) {
		// IFFの設定、クラス毎の判定しかできないので使わない。
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		//反撃設定
		if(par1Entity instanceof EntityMob && !(par1Entity instanceof EntityCreeper)){
			((EntityMob) par1Entity).setAttackTarget(this);
			((EntityMob) par1Entity).setRevengeTarget(this);
			((EntityMob) par1Entity).getNavigator().setPath(getNavigator().getPath(), ((EntityMob)par1Entity).moveForward);
		}

		// 正常時は回復優先処理
		if (getHealth() < 10 && !isBloodsuck() && ItemHelper.hasSugar(this)) {
			return true;
		}

		// 特殊な攻撃処理
		if (this.jobController.isActiveModeClass() 
				&& this.jobController.getActiveModeClass().attackEntityAsMob(this.jobController.getMaidModeString(), par1Entity)) {
			return true;
		}

		// 標準処理
		setSwing(20, isBloodsuck() ? EnumSound.attack_bloodsuck : EnumSound.attack, !isPlaying());
		maidAvatar.attackTargetEntityWithCurrentItem(par1Entity);
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		// お好みは何？
		if (isContractEX()) {
			return ItemHelper.isSugar(par1ItemStack);
		}
		return ItemHelper.isCake(par1ItemStack);
	}


	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		// データセーブ
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setTag("Inventory", maidInventory.writeToNBT(new NBTTagList()));
		par1nbtTagCompound.setString("Mode", getMaidModeString());
		par1nbtTagCompound.setBoolean("Wait", isMaidWait());
		par1nbtTagCompound.setBoolean("Freedom", isFreedom());
		par1nbtTagCompound.setBoolean("Tracer", isTracer());
//		par1nbtTagCompound.setBoolean("isWildSaved", isWildSaved);
		par1nbtTagCompound.setInteger("LimitCount", maidContractLimit);
		par1nbtTagCompound.setLong("Anniversary", maidAnniversary);
//		par1nbtTagCompound.setInteger("EXP", experienceValue);
		par1nbtTagCompound.setInteger("DominantArm", getDominantArm());
		par1nbtTagCompound.setByte("ColorB", getColor());
//		par1nbtTagCompound.setString("texName", modelConfigCompound.getTextureNameLittleMaid());
//		par1nbtTagCompound.setString("texArmor", modelConfigCompound.getTextureNameArmor());
		par1nbtTagCompound.setInteger("maidArmorVisible", maidArmorVisible);
		//if(textureNameMain==null) textureNameMain = "default_Orign";
		par1nbtTagCompound.setString("textureModelNameForClient", dataManager.get(EntityLittleMaid.dataWatch_texture_LittleMaid));
		//if(textureNameArmor==null) textureNameArmor = "default_Orign";
//		par1nbtTagCompound.setString("textureArmorNameForClient", dataManager.get(EntityLittleMaid.dataWatch_texture_Armor));
		par1nbtTagCompound.setString("textureArmorNameForClientHead", dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_head));
		par1nbtTagCompound.setString("textureArmorNameForClientChest", dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_chest));
		par1nbtTagCompound.setString("textureArmorNameForClientLegs", dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_legs));
		par1nbtTagCompound.setString("textureArmorNameForClientFeet", dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_feet));
//		par1nbtTagCompound.setBoolean("isMadeTextureNameFlag", isMadeTextureNameFlag);

		//カスタム分
		par1nbtTagCompound.setInteger("WaitMotion", dataManager.get(EntityLittleMaid.dataWatch_WaitMotion));
		
		NBTTagCompound prevtargettag = new NBTTagCompound();
		par1nbtTagCompound.setTag("prevtarget", prevtargettag);
		// HomePosition
		par1nbtTagCompound.setInteger("homeX", getPosition().getX());
		par1nbtTagCompound.setInteger("homeY", getPosition().getY());
		par1nbtTagCompound.setInteger("homeZ", getPosition().getZ());
		par1nbtTagCompound.setInteger("homeWorld", homeWorld);

//		par1nbtTagCompound.setFloat(LittleMaidReengaged.MODID + ":MAID_EXP", maidExperience);
//		par1nbtTagCompound.setInteger(LittleMaidReengaged.MODID + ":EXP_BOOST", gainExpBoost);

		// 肩車
		boolean isRide = isRiding();
		par1nbtTagCompound.setBoolean(LittleMaidReengaged.MODID + ":riding", isRide);
		if (isRide) {
			if (getRidingEntity() instanceof EntityPlayer) {
				par1nbtTagCompound.setString(LittleMaidReengaged.MODID + ":ridingPlayer", getRidingEntity().getUniqueID().toString());
			}
			par1nbtTagCompound.setIntArray(LittleMaidReengaged.MODID + ":lastPosition", new int[]{(int) posX, (int) posY, (int) posZ});
		}

		// Tiles
		NBTTagCompound lnbt = new NBTTagCompound();
		par1nbtTagCompound.setTag("Tiles", lnbt);
		for (int li = 0; li < maidTiles.length; li++) {
			if (maidTiles[li] != null) {
				lnbt.setIntArray(String.valueOf(li), maidTiles[li]);
			}
		}
//		// 追加分
//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			maidEntityModeList.get(li).writeEntityToNBT(par1nbtTagCompound);
//		}
		//メイド職業の情報をNBTへ書き出し
		this.jobController.writeEntityToNBT(par1nbtTagCompound);

		//経験値情報をNBTへ書き出し
		this.getExperienceHandler().writeEntityToNBT(par1nbtTagCompound);
		
		modeTrigger.writeToNBT(par1nbtTagCompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		// データロード
		super.readEntityFromNBT(par1nbtTagCompound);

		LittleMaidReengaged.Debug("read." + getEntityWorld().isRemote);

		maidInventory.readFromNBT(par1nbtTagCompound.getTagList("Inventory", 10));
		setMaidWait(par1nbtTagCompound.getBoolean("Wait"));
		setFreedom(par1nbtTagCompound.getBoolean("Freedom"));
		setTracer(par1nbtTagCompound.getBoolean("Tracer"));
		
		setMaidMode(par1nbtTagCompound.getString("Mode"));
		
		if (par1nbtTagCompound.hasKey("LimitCount")) {
			maidContractLimit = par1nbtTagCompound.getInteger("LimitCount");
		} else {
			long lcl = par1nbtTagCompound.getLong("Limit");
			if (isContract() && lcl == 0) {
				maidContractLimit = 24000;
			} else {
				maidContractLimit = (int)((lcl - getEntityWorld().getWorldTime()));
			}
		}
		if (isContract() && maidContractLimit == 0) {
			// 値がおかしい時は１日分
//				maidContractLimit = getEntityWorld().getWorldTime() + 24000L;
			maidContractLimit = 24000;
		}
		maidAnniversary = par1nbtTagCompound.getLong("Anniversary");
		if (maidAnniversary == 0L && isContract()) {
			// ダミーの数値を入れる
			maidAnniversary = getEntityWorld().getWorldTime() - getEntityId();
		}
		if (maidAvatar != null) {
			maidAvatar.experienceTotal = par1nbtTagCompound.getInteger("EXP");
		}
		setDominantArm(par1nbtTagCompound.getInteger("DominantArm"));
		if (mstatSwingStatus.length <= getDominantArm()) {
			setDominantArm(0);
		}
//		textureData.textureIndex[0] = ModelManager.instance.getIndexTextureBoxServer(this, par1nbtTagCompound.getString("texName"));
//		textureData.textureIndex[1] = ModelManager.instance.getIndexTextureBoxServer(this, par1nbtTagCompound.getString("texArmor"));
//		textureData.getTextureBoxLittleMaid() = ModelManager.instance.getTextureBoxServer("default_"+ModelManager.defaultModelName);
//		textureData.getTextureBoxArmor() = ModelManager.instance.getTextureBoxServer("default_"+ModelManager.defaultModelName);

		// HomePosition
		int lhx = par1nbtTagCompound.getInteger("homeX");
		int lhy = par1nbtTagCompound.getInteger("homeY");
		int lhz = par1nbtTagCompound.getInteger("homeZ");
//			func_110172_bL().set(lhx, lhy, lhz);
		setHomePosAndDistance(new BlockPos(lhx, lhy, lhz),(int)getMaximumHomeDistance());
		homeWorld = par1nbtTagCompound.getInteger("homeWorld");

		// Tiles
		NBTTagCompound lnbt = par1nbtTagCompound.getCompoundTag("Tiles");
		for (int li = 0; li < maidTiles.length; li++) {
			int ltile[] = lnbt.getIntArray(String.valueOf(li));
			maidTiles[li] = ltile.length > 0 ? ltile : null;
		}

//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			maidEntityModeList.get(li).readEntityFromNBT(par1nbtTagCompound);
//		}
		//メイドさんの職業
		this.jobController.readEntityFromNBT(par1nbtTagCompound);

		String armorHead = par1nbtTagCompound.getString("textureArmorNameForClientHead");
		String armorChest = par1nbtTagCompound.getString("textureArmorNameForClientChest");
		String armorLegs = par1nbtTagCompound.getString("textureArmorNameForClientLegs");
		String armorFeet = par1nbtTagCompound.getString("textureArmorNameForClientFeet");
		
		dataManager.set(EntityLittleMaid.dataWatch_texture_LittleMaid, par1nbtTagCompound.getString("textureModelNameForClient"));
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_head, armorHead);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_chest, armorChest);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_legs, armorLegs);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_feet, armorFeet);
		
		if (par1nbtTagCompound.hasKey("Color")) {
			setColor((byte)par1nbtTagCompound.getInteger("Color"));
			par1nbtTagCompound.removeTag("Color");
		} else {
			setColor(par1nbtTagCompound.getByte("ColorB"));
		}
		refreshModels();
//		if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
//			syncModelNames();
//		}

//		isMadeTextureNameFlag = par1nbtTagCompound.getBoolean("isMadeTextureNameFlag");

//		maidExperience = par1nbtTagCompound.getFloat(LittleMaidReengaged.MODID + ":MAID_EXP");
//		setExpBooster(par1nbtTagCompound.getInteger(LittleMaidReengaged.MODID + ":EXP_BOOST"));
		
//		dataManager.set(EntityLittleMaid.dataWatch_MaidExpValue, maidExperience);

		// 肩車
		boolean isRide = par1nbtTagCompound.getBoolean(LittleMaidReengaged.MODID + ":riding");
		if (isRide) {
			int[] lastPosition = par1nbtTagCompound.getIntArray(LittleMaidReengaged.MODID + ":lastPosition");
			setLocationAndAngles(lastPosition[0], lastPosition[1], lastPosition[2], 0, 0);

			String playerUid = par1nbtTagCompound.getString(LittleMaidReengaged.MODID + ":ridingPlayer");
			if (!playerUid.isEmpty()) {
				try {
					EntityPlayer ridingPlayer = getEntityWorld().getPlayerEntityByUUID(UUID.fromString(playerUid));
					if (ridingPlayer != null) {
						startRiding(ridingPlayer);
					}
				} catch(IllegalArgumentException exception) {}
			}
		}

//		LittleMaidReengaged.Debug("READ %s %s", textureNameMain, textureNameArmor);

		this.onInventoryChanged();
//		isWildSaved = par1nbtTagCompound.getBoolean("isWildSaved");
		setMaidArmorVisible(par1nbtTagCompound.hasKey("maidArmorVisible")?par1nbtTagCompound.getInteger("maidArmorVisible"):15);
//		syncMaidArmorVisible();

		//経験値
		getExperienceHandler().readEntityFromNBT(par1nbtTagCompound);
		dataManager.set(EntityLittleMaid.dataWatch_MaidExpValue, getExperienceHandler().getMaidExperience());

		
		modeTrigger.readFromNBT(par1nbtTagCompound);
		
		//カスタム分
		dataManager.set(EntityLittleMaid.dataWatch_WaitMotion, par1nbtTagCompound.getInteger("WaitMotion"));

	}

	public boolean canBePushed()
	{
		// --------------------------------------------
		// 肩車状態でプレイヤーが馬に乗っているときは、当たり判定をなくす。
		if(isMaidWait()) return false;
		if (getRidingEntity() != null && getRidingEntity() == mstatMasterEntity) {
			if(getRidingEntity().getRidingEntity() instanceof EntityHorse)
			{
				return false;
			}
		}
		// --------------------------------------------

		return !isDead;
	}

	// おんぶおばけは無敵
	@Override
	public boolean canBeCollidedWith() {
		if (getRidingEntity() != null && getRidingEntity() == mstatMasterEntity) {
			ItemStack litemstack = mstatMasterEntity.getHeldItemMainhand();
			return litemstack.isEmpty() || (litemstack.getItem() == Items.SADDLE);
		}
		return super.canBeCollidedWith();
	}

	@Override
	public boolean canBeAttackedWithItem() {
		if (getRidingEntity() != null && getRidingEntity() == mstatMasterEntity) {
			return false;
		}
		return super.canBeAttackedWithItem();
	}

	@Override
	public double getMountedYOffset() {
		// TODO: Changed from 'riddenByEntity'. Is it correct?
		if (getControllingPassenger() instanceof EntityChicken) {
			return height + 0.03D;
		}
		if (getControllingPassenger() instanceof EntitySquid) {
			return height - 0.2D;
		}
		return super.getMountedYOffset() + 0.35D;
	}

	@Override
	public double getYOffset() {
		double yOffset = -0.30D;

		if(getRidingEntity() instanceof EntityPlayer) {
			// 姿勢制御

			// --------------------------------------------
			// プレイヤーが馬に乗っているときは、肩車ではなく馬の後ろに乗る
			if(getRidingEntity().getRidingEntity() instanceof EntityHorse)
			{
				if(getEntityWorld().isRemote) {
					return yOffset - 2.8F;
				} else {
					return yOffset - 1.0F;
				}
			}
		}
		return yOffset;
	}

	/*
	@Override
	public void updateRidden() {
		super.updateRidden();

		if(getRidingEntity() instanceof EntityPlayer) {
			EntityPlayer lep = (EntityPlayer)getRidingEntity();

			// ヘッドハガー
			renderYawOffset = lep.renderYawOffset;
			prevRenderYawOffset = lep.prevRenderYawOffset;

			renderYawOffset = lep.renderYawOffset;
			if (((rotationYaw - renderYawOffset) % 360F) > 90F) {
				rotationYaw = renderYawOffset + 90F;
			}
			if (((rotationYaw - renderYawOffset) % 360F) < -90F) {
				rotationYaw = renderYawOffset - 90F;
			}
			if (((rotationYawHead - renderYawOffset) % 360F) > 90F) {
				rotationYawHead = renderYawOffset + 90F;
			}
			if (((rotationYawHead - renderYawOffset) % 360F) < -90F) {
				rotationYawHead = renderYawOffset - 90F;
			}

			double dx, dz;
			// --------------------------------------------
			// プレイヤーが馬に乗っているときは、肩車ではなく馬の後ろに乗る
			// getRidingEntity() はsuper.updateRidden();によってNULLになる事があるので注意
			if(lep.getRidingEntity() instanceof EntityHorse)
			{
				EntityHorse horse = (EntityHorse)lep.getRidingEntity();
				if(getEntityWorld().isRemote)
				{
					dx = Math.sin((horse.renderYawOffset * Math.PI) / 180D) * 0.5;
					dz = Math.cos((horse.renderYawOffset * Math.PI) / 180D) * 0.5;
				}
				else
				{
					dx = Math.sin((horse.renderYawOffset * Math.PI) / 180D) * 0.9;
					dz = Math.cos((horse.renderYawOffset * Math.PI) / 180D) * 0.9;
				}
			}
			else
			{
				dx = Math.sin((lep.renderYawOffset * Math.PI) / 180D) * 0.35;
				dz = Math.cos((lep.renderYawOffset * Math.PI) / 180D) * 0.35;
			}
			// --------------------------------------------

			posX += dx;
			posZ -= dz;
//			lastTickPosX = llpx;
//			lastTickPosY = llpy;
//			lastTickPosZ = llpz;
		}
	}
	*/

	@Override
	public float getSwingProgress(float par1) {
		for (SwingStatus lswing : mstatSwingStatus) {
			lswing.getSwingProgress(par1);
		}
		return getSwingStatusDominant().onGround;
	}

	// 首周り
	public void setLooksWithInterest(boolean f) {
		if (looksWithInterest != f) {
			looksWithInterest = f;

			int li = dataManager.get(EntityLittleMaid.dataWatch_Flags);
			li = looksWithInterest ? (li | dataWatch_Flags_looksWithInterest) : (li & ~dataWatch_Flags_looksWithInterest);
			li = looksWithInterestAXIS ? (li | dataWatch_Flags_looksWithInterestAXIS) : (li & ~dataWatch_Flags_looksWithInterestAXIS);
			dataManager.set(EntityLittleMaid.dataWatch_Flags, Integer.valueOf(li));
		}
	}

	public boolean getLooksWithInterest() {
		looksWithInterest = (dataManager.get(EntityLittleMaid.dataWatch_Flags) & dataWatch_Flags_looksWithInterest) > 0;
		looksWithInterestAXIS = (dataManager.get(EntityLittleMaid.dataWatch_Flags) & dataWatch_Flags_looksWithInterestAXIS) > 0;

		return looksWithInterest && !isHeadMount();
	}

	/**
	 * 首傾げ制御
	 * 固定モーション用分岐を追加
	 * @param f
	 * @return
	 */
	public float getInterestedAngle(float f) {
		if (!maidInventory.armorInventory.get(3).isEmpty()) {
			return 0f;
		}
		//固定モーション分岐
		if (this.maidMotion == EnumMaidMotion.LOOKSUGAR) {
			return 0.99999994F * ((looksWithInterestAXIS ? 0.08F : -0.08F) * (float)Math.PI);
		}
		return (prevRotateAngleHead + (rotateAngleHead - prevRotateAngleHead) * f) * ((looksWithInterestAXIS ? 0.08F : -0.08F) * (float)Math.PI);
	}


	// ダメージコントロール
//	@Override
	public boolean isBlocking() {
		return getSwingStatusDominant().isBlocking();
//		return maidAvatar.isBlocking();
	}

	@Override
	protected void damageArmor(float pDamage) {
		maidInventory.damageArmor(pDamage);
		getAvatarIF().W_damageArmor(pDamage);
	}

	@Override
	public int getTotalArmorValue() {
		return maidAvatar.getTotalArmorValue();
	}

	@Override
	protected float applyArmorCalculations(DamageSource par1DamageSource, float par2) {
		return getAvatarIF().W_applyArmorCalculations(par1DamageSource, par2);
	}

	@Override
	protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
		return getAvatarIF().W_applyPotionDamageCalculations(par1DamageSource, par2);
	}

	@Override
	protected void damageEntity(DamageSource par1DamageSource, float damageAmount) {
		// ダメージソースに応じて音声変更
		if (par1DamageSource == DamageSource.FALL) {
			setMaidDamegeSound(EnumSound.hurt_fall);
			if (isContractEX() && damageAmount>=19 && damageAmount<getHealth()) {
				//進捗
				AchievementsLMRE.grantAC(getMaidMasterEntity(), AC.Ashikubi);
			}
		}
		if(!par1DamageSource.isUnblockable() && isBlocking()) {
			// ブロッキング
//			par2 = (1.0F + par2) * 0.5F;
			LittleMaidReengaged.Debug(String.format("Blocking success ID:%d, %f -> %f" , getEntityId(), damageAmount, (damageAmount = (1.0F + damageAmount) * 0.5F)));
			setMaidDamegeSound(EnumSound.hurt_guard);
		}
		//デバッグ
		//maidInventory.armorInventory[2] = null;

		// 被ダメ
		float llasthealth = getHealth();
		if (damageAmount > 0 && this.jobController.getActiveModeClass() != null && !this.jobController.getActiveModeClass().damageEntity(this.jobController.getMaidModeString(), par1DamageSource, damageAmount)) {
			getAvatarIF().W_damageEntity(par1DamageSource, damageAmount);
//			super.damageEntity(par1DamageSource, par2);

			// ダメージを受けると待機を解除
			setMaidWait(false);
			
			this.getCombatTracker().trackDamage(par1DamageSource, llasthealth, damageAmount);
		}

		/*
		if (llasthealth == getHealth() && maidDamegeSound == LMM_EnumSound.hurt) {
			maidDamegeSound = LMM_EnumSound.hurt_nodamege;
		}
		*/
		LittleMaidReengaged.Debug(String.format("GetDamage ID:%d, %s, %f/ %f" , getEntityId(), par1DamageSource.damageType, llasthealth - getHealth(), damageAmount));
//		super.damageEntity(par1DamageSource, par2);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (getEntityWorld().isRemote) {
			return false;
		}

		Entity entity = par1DamageSource.getTrueSource();
		boolean force = true;

		if(par1DamageSource.getTrueSource() instanceof EntitySnowball) force = false;

		if(par1DamageSource.getDamageType().equalsIgnoreCase("thrown"))
		{
			if(entity!=null && maidAvatar!=null && entity.getEntityId()==maidAvatar.getEntityId())
			{
				return false;
			}
		}

		LittleMaidReengaged.Debug("LMM_EntityLittleMaid.attackEntityFrom "+this+"("+maidAvatar+") <= "+entity);

		// ダメージソースを特定して音声の設定
		setMaidDamegeSound(EnumSound.hurt);
		if (par1DamageSource == DamageSource.IN_FIRE || par1DamageSource == DamageSource.ON_FIRE || par1DamageSource == DamageSource.LAVA) {
			setMaidDamegeSound(EnumSound.hurt_fire);
		}
		
//		for (EntityModeBase lm : maidEntityModeList) {
//			float li = lm.attackEntityFrom(par1DamageSource, par2);
//			if (li > 0) return li == 1 ? false : true;
//		}
		
		//Job関連の処理
		int retAttack = this.jobController.attackEntityFrom(par1DamageSource, par2);
		if (retAttack > 0) return retAttack == 1 ? false : true;

		setMaidWait(false);
		setMaidWaitCount(0);
		if (par2 > 0) {
			// 遊びは終わりだ！
			setPlayingRole(PlayRole.NOTPLAYING);
//			coolingTick  = 200;
			getNextEquipItem();
		}
		// ゲーム難易度によるダメージ・クールタイム補正
		if(isContract() && (entity instanceof EntityLivingBase) || (entity instanceof EntityArrow)) {
			// Removed cooltime for continuous damage
			ticksSinceLastDamage = 15;

			if(getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL) {
				par2 = 0;
			}
			if(getEntityWorld().getDifficulty() == EnumDifficulty.EASY && par2 > 0) {
				par2 = par2 / 2 + 1;
				ticksSinceLastDamage = 12;
			}
			if(getEntityWorld().getDifficulty() == EnumDifficulty.HARD) {
				par2 = MathHelper.floor(par2 * 1.5f);
				ticksSinceLastDamage = 18;
			}
		}

		// EXP penalty
		if (par1DamageSource.getTrueSource() instanceof EntityPlayer || par1DamageSource.getDamageType().equals("inWall") ||
				par1DamageSource.getDamageType().equals("inFire") || par1DamageSource.getDamageType().equals("inLava") ||
				par1DamageSource.getDamageType().equals("anvil") || par1DamageSource.getDamageType().equals("fall") ||
				par1DamageSource.getDamageType().equals("cactus") || par1DamageSource.getDamageType().equals("onFire")) {
			addMaidExperience(-0.7f*par2);
		}

//		if (par2 == 0 && maidMode != mmode_Detonator) {
		if (par2 == 0) {
			// ノーダメージ
			if (getMaidDamegeSound() == EnumSound.hurt) {
				setMaidDamegeSound(EnumSound.hurt_nodamege);
			}
			
			//playLittleMaidVoiceSound(getMaidDamegeSound(), !force);
			playLittleMaidDamageVoiceSound(!force);

			return false;
		}

		if(super.attackEntityFrom(par1DamageSource, par2)) {
			//契約者の名前チェックはマルチ用
//			if(force) playSound("game.player.hurt");
			if (isContract() && entity != null) {
				if (getIFF(entity) && !isPlaying()) {
					//1.8検討
					//fleeingTick = 0;
					return true;
				}
			} else if (maidInventory.getCurrentItem().isEmpty()) {
				return true;
			}
			//1.8検討
			//fleeingTick = 0;
//			entityToAttack = entity;
			/*
			if (entity != null) {
				setPathToEntity(getEntityWorld().getPathEntityToEntity(this, entityToAttack, 16F, true, false, false, true));
			}
			if (maidMode == mmode_Healer && entity instanceof EntityLiving) {
				// ヒーラーは薬剤で攻撃
				maidInventory.currentItem = maidInventory.getInventorySlotContainItemPotion(true, 0, ((EntityLiving)entity).isEntityUndead() & isMaskedMaid);
			}
			*/
			return true;
		}
		return false;


//		return maidAvatar.attackEntityFrom(par1DamageSource, par2);
	}

	/**
	 * 対象にポーションを使う。
	 */
	public void usePotionTotarget(EntityLivingBase entityliving) {
		ItemStack itemstack = maidInventory.getCurrentItem();
		if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemPotion) {
			// ポーション効果の発動
			itemstack.shrink(1);
			List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
			if (list != null) {
				PotionEffect potioneffect;
				for (Iterator<PotionEffect> iterator = list.iterator(); iterator.hasNext(); entityliving.addPotionEffect(new PotionEffect(potioneffect))) {
					potioneffect = (PotionEffect)iterator.next();
					addMaidExperience(0.49f*(potioneffect.getDuration()==0?240:potioneffect.getDuration())/20);
				}
			}
			maidInventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
		}
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		int k = rand.nextInt(3 + par2);
		for(int j = 0; j <= k; j++) {
			if(rand.nextInt(50) == 0) {
				entityDropItem(new ItemStack(Items.DYE, 1, 3), 0F);
			}
			dropItem(Items.SUGAR, 1);
		}

		// インベントリをブチマケロ！
		maidInventory.dropAllItems();
	}

	@Override
	protected Item getDropItem() {
		return Items.SUGAR;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return 0;
	}


//	@Override
//	public void applyEntityCollision(Entity par1Entity) {
//		// 閉所接触回避用
//		super.applyEntityCollision(par1Entity);
//
//		if (par1Entity instanceof EntityLittleMaid) {
//			if (((EntityLittleMaid)par1Entity).aiAvoidPlayer.isActive) {
//				aiAvoidPlayer.isActive = true;
//			}
//		} else if (par1Entity == mstatMasterEntity) {
//			aiAvoidPlayer.setActive();
//		}
//	}

	public void updateAITasks()
	{
		super.updateAITasks();
		try {
			tasks.onUpdateTasks();
		} catch (ConcurrentModificationException exception) {
			// TODO Unsuitable silence
		}
		this.jobController.getActiveModeClass().updateAITick(getMaidModeString());
	}

	@Override
	public void onEntityUpdate() {
		
		//クライアント限定で音声再生処理を行う
		this.soundManager.onEntityUpdate();
		/*
		if(getEntityWorld().isRemote&&!playingSound.isEmpty()){
			//サウンド設定されている分まとめて再生する
			Iterator<EnumSound> iterator = playingSound.iterator();
			while(iterator.hasNext()){
				
				EnumSound enumsound = iterator.next();
				LittleMaidReengaged.Debug("REQ %s", enumsound);

				//音声パックがロードされていない場合はこちら
				if (!SoundLoader.isFoundSoundpack()) {
					playSound(enumsound.DefaultValue, 1.0f);
//					getEntityWorld().playSound(posX, posY, posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation(enumsound.DefaultValue)), SoundCategory.VOICE, getSoundVolume(), lpitch, false);
					playingSound.remove(enumsound);
					continue;
				}

				//音声パックがロードされている場合はこちら
				//テクスチャと色とsoundtypeで実際に再生する音声を取得する
				String sname = SoundRegistry.getSoundRegisteredName(enumsound, textureNameMain, (int) getColor());
				LittleMaidReengaged.Debug("STC %s,%d/FRS %s", textureNameMain, getColor(), sname);
				
				//対象がない場合は何もしない
				if (sname == null || sname.isEmpty()) {
					playingSound.remove(enumsound);
					continue;
				}

				if ((enumsound.index & 0xf00) == EnumSound.living_daytime.index) {
					// LivingSound LivingVoiceRateを確認
					Float ratio = SoundRegistry.getLivingVoiceRatio(sname);
					if (ratio == null) ratio = LMRConfig.cfg_voiceRate;
					// カットオフ
					if (rand.nextFloat() > ratio) {
						playingSound.remove(enumsound);
						continue;
					}
				}

				LittleMaidReengaged.Debug(String.format("id:%d, se:%04x-%s (%s)", getEntityId(), enumsound.index, enumsound.name(), sname));

//				playSound(LittleMaidReengaged.DOMAIN+":"+sname, lpitch);
				getEntityWorld().playSound(posX, posY, posZ, new SoundEvent(new ResourceLocation(LittleMaidReengaged.DOMAIN+":"+sname)), getSoundCategory(), getSoundVolume(), 1f, false);
				playingSound.remove(enumsound);
			}
//			LMM_LittleMaidMobNX.proxy.playLittleMaidSound(getEntityWorld(), posX, posY, posZ, playingSound, getSoundVolume(), lpitch, false);
		}
		*/
		super.onEntityUpdate();
	}

	/**
	 * 埋葬対策コピー
	 */
	@SuppressWarnings("deprecation")
	private boolean isBlockTranslucent(int par1, int par2, int par3) {
		IBlockState iState = getEntityWorld().getBlockState(new BlockPos(par1, par2, par3));
		return iState.getBlock().isNormalCube(iState);
	}

	/**
	 * 埋葬対策コピー
	 */
	@Override
	protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
		// EntityPlayerSPのを引っ張ってきた
		int var7 = MathHelper.floor(par1);
		int var8 = MathHelper.floor(par3);
		int var9 = MathHelper.floor(par5);
		double var10 = par1 - var7;
		double var12 = par5 - var9;

		boolean lflag = false;
		for (int li = 0; li < height; li++) {
			lflag |= isBlockTranslucent(var7, var8 + li, var9);
		}
		if (lflag) {
			boolean var14 = !isBlockTranslucent(var7 - 1, var8, var9) && !isBlockTranslucent(var7 - 1, var8 + 1, var9);
			boolean var15 = !isBlockTranslucent(var7 + 1, var8, var9) && !isBlockTranslucent(var7 + 1, var8 + 1, var9);
			boolean var16 = !isBlockTranslucent(var7, var8, var9 - 1) && !isBlockTranslucent(var7, var8 + 1, var9 - 1);
			boolean var17 = !isBlockTranslucent(var7, var8, var9 + 1) && !isBlockTranslucent(var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;

			if (var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}

			if (var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}

			if (var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}

			if (var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}

			float var21 = 0.1F;

			if (var18 == 0) {
				motionX = (-var21);
			}

			if (var18 == 1) {
				motionX = var21;
			}

			if (var18 == 4) {
				motionZ = (-var21);
			}

			if (var18 == 5) {
				motionZ = var21;
			}

			return !(var14 | var15 | var16 | var17);
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onLivingUpdate() {
		float lhealth = getHealth();
		if (lhealth > 0) {
			if (!getEntityWorld().isRemote) {
				if (getSwingStatusDominant().canAttack()) {
					// 通常時は回復優先
					HEALPHASE: if (lhealth < getMaxHealth() && ticksSinceLastDamage <= 0) {
						if (isBloodsuck() && getAttackTarget() != null) {
							break HEALPHASE;
						}
						consumeSugar(EnumConsumeSugar.HEAL);
					}

					// つまみ食い
					float jobFactor = this.jobController.getActiveModeClass() != null ? this.jobController.getActiveModeClass().getSugarSpeed() : 1;
					int expBoost = this.getExperienceHandler().getExpBooster();
					if (rand.nextInt(MathHelper.floor(
							50000 / jobFactor / (expBoost * (1.05f+0.005f * expBoost)))) == 0) {
						consumeSugar(EnumConsumeSugar.OTHER);
					}
					// 契約更新
					if (isContractEX()) {
						float f = getContractLimitDays();
						if (f <= 6) {
							// 契約更新
							consumeSugar(EnumConsumeSugar.RECONTRACT);
						}
					}
				}
			}
		}

		//雪合戦試験
		if ((isFreedom() || !isContractEX()) && getEntityWorld().isDaytime() && !isPlaying() 
				&& (this.jobController.getMaidModeString().equals("Wild")
						|| this.jobController.getMaidModeString().equals("Escort"))){
			if(EntityMode_Playing.checkSnows(
						MathHelper.floor(posX),
						MathHelper.floor(posY),
						MathHelper.floor(posZ), getEntityWorld())){
				setPlayingRole(PlayRole.QUICKSHOOTER);
			}else{
				setPlayingRole(PlayRole.NOTPLAYING);
			}
		}

		/*
		if(getMaidModeInt()==LMM_EntityMode_Healer.mmode_Healer){

		}*/

		try {
			super.onLivingUpdate();
		} catch (Exception exception) {
			System.out.println("EntityLittleMaid.onLivingUpdate.super");
			exception.printStackTrace();
		}

		((PathNavigateGround)navigator).setCanSwim(true);

		if(!getEntityWorld().isRemote) maidInventory.decrementAnimations();

		if(!getEntityWorld().isRemote){
//			float rot = getRotationYawHead();
			int py = MathHelper.floor(getEntityBoundingBox().minY);

			/*
			float movespeed = getAIMoveSpeed();

			BlockPos targetPos = new BlockPos(px+XBOUND_BLOCKOFFS[pitchindex], py, pz+ZBOUND_BLOCKOFFS[pitchindex]);
			// TODO BETA
			if(movespeed!=0 && !isMaidWait() && isCollidedHorizontally && (onGround&&!isInWater()) &&
					PathNodeType.BLOCKED == WalkNodeProcessor.func_186330_a(getEntityWorld(), targetPos.getX(), targetPos.getY()  , targetPos.getZ()) &&
					PathNodeType.WALKABLE == WalkNodeProcessor.func_186330_a(getEntityWorld(), targetPos.getX(), targetPos.getY()+1, targetPos.getZ())){
				//段差にギリ載せ
				setLocationAndAngles(posX+0.05*XBOUND_BLOCKOFFS[pitchindex], posY+1D, posZ+0.05*ZBOUND_BLOCKOFFS[pitchindex], rotationYaw, rotationPitch);
			}
			*/

			// 埋まった
			OPAQUE: if(isEntityInsideOpaqueBlock()){
				if(!isInsideOpaque) for(int i=2;i<10;i++){
					IBlockState state1 = getEntityWorld().getBlockState(new BlockPos(posX, py+i, posZ));
					IBlockState state2 = getEntityWorld().getBlockState(new BlockPos(posX, py+i+1, posZ));
					if(!state1.getBlock().causesSuffocation(state1) && !state2.getBlock().causesSuffocation(state2)){
						setLocationAndAngles(posX, py+i, posZ, rotationYaw, rotationPitch);
						break OPAQUE;
					}
				}
				isInsideOpaque = true;
			}else{
				isInsideOpaque = false;
			}
		}

		if(lhealth > 0) {
			// 近接監視の追加はここ
			// アイテムの回収
			if (!getEntityWorld().isRemote) {
				List<Entity> list = getEntityWorld().getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D));
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						Entity entity = (Entity)list.get(i);
						if (!entity.isDead) {
							if (entity instanceof EntityArrow &&
									(getEntityWorld().getDifficulty() == EnumDifficulty.HARD ? ((EntityArrow) entity).shootingEntity == this : true)) {
								// 特殊回収
								((EntityArrow)entity).pickupStatus = PickupStatus.ALLOWED;
							}
							entity.onCollideWithPlayer(maidAvatar);
						}
					}
				}
			}
			/*
			// 時計を持っている
			// TODO:多分この辺りの処理はおかしい
			if (isContractEX() && mstatClockMaid) {
				// ゲーム内時間に合わせた音声の再生
				mstatTime = (int)(getEntityWorld().getWorldTime() % 24000);
				if (mstatMasterEntity != null) {
					boolean b = mstatMasterEntity.isPlayerSleeping();

					if (mstatMasterDistanceSq < 25D && getEntitySenses().canSee(mstatMasterEntity))	{
						EnumSound lsound = EnumSound.Null;
						if (mstatFirstLook && (mstatTime > 23500 || mstatTime < 1500)) {
							lsound = EnumSound.goodmorning;
							mstatFirstLook = false;
						}
						else if (!mstatFirstLook && b) {
							lsound = EnumSound.goodnight;
							mstatFirstLook = true;
						}
						else if (mstatFirstLook && !b) {
							mstatFirstLook = false;
						}

						if (lsound != EnumSound.Null) {
							playLittleMaidVoiceSound(lsound, false);
							setLooksWithInterest(true);
						}
					} else {
						if (!mstatFirstLook && (b || (mstatTime > 18000 && mstatTime < 23500))) {
							mstatFirstLook = true;
						}
					}
				}
			} else {
				mstatTime = 6000;
			}
			*/
			//時計を持ったメイドさん
			this.playLivingSoundLittleMaidWithClock();

			// TNT-D System
			maidOverDriveTime.onUpdate();
			if (maidOverDriveTime.isDelay()) {
				for (int li = 0; li < mstatSwingStatus.length; li++) {
					mstatSwingStatus[li].attackTime--;
				}
				if (maidOverDriveTime.isEnable()) {
					getEntityWorld().spawnParticle(EnumParticleTypes.REDSTONE, (posX + rand.nextFloat() * width * 2.0F) - width, posY + 0.5D + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, 1.2D, 0.4D, 0.4D);
				}
				if (!getEntityWorld().isRemote) {
					Entity lattackentity = getAttackTarget();
					if (lattackentity == null) {
						lattackentity = getRevengeTarget();
					}
					if (lattackentity != null) {
						Path pe = getNavigator().getPathToEntityLiving(lattackentity);//getPathEntityToEntity(this, lattackentity, 16F, true, false, false, true);
						if (pe != null) {
							pe.incrementPathIndex();
							if (!pe.isFinished()) {
								Vec3d v = pe.getPosition(this);
								setPosition(v.x, v.y, v.z);
							}
						}
					}
				}
			}

		}
	}

	public Counter getMaidOverDriveTime() {
		return maidOverDriveTime;
	}

	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch){
		super.setLocationAndAngles(x, y, z, yaw, pitch);
	}

	@Override
	public void onUpdate() {
		int litemuse = 0;
//		resetNavigator();
		
		//手持ちアイテムの同期
		if (!this.world.isRemote) {
			int currentItem = dataManager.get(EntityLittleMaid.dataWatch_CurrentItem);
			if (currentItem != this.maidInventory.currentItem) {
				dataManager.set(EntityLittleMaid.dataWatch_CurrentItem, this.maidInventory.currentItem);
			}
		} else {
			this.maidInventory.currentItem = dataManager.get(EntityLittleMaid.dataWatch_CurrentItem);
		}

		this.getHeldEquipment();
		if (registerTick.isDelay()){
			registerTick.onUpdate();

			if (!registerTick.isEnable() && registerTick.getValue() == 0 && !getEntityWorld().isRemote) {
				getMaidMasterEntity().sendMessage(new TextComponentTranslation("littleMaidMob.chat.text.cancelregistration").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
			}
		}

		// 飛び道具用
		weaponFullAuto = false;
		weaponReload = false;

		// 主の確認など
		mstatMasterEntity = getMaidMasterEntity();
		if (mstatMasterEntity != null) {
			mstatMasterDistanceSq = getDistanceSq(mstatMasterEntity);
		}
//		// モデルサイズのリアルタイム変更有り？
//		modelConfigCompound.onUpdate();

		getExperienceHandler().onUpdate();

		// リアルタイム変動値をアップデート
		if (getEntityWorld().isRemote) {
			
			// クライアント側
			//boolean lupd = false;
			//lupd |= updateMaidContract();
			//lupd |= updateMaidColor();
//			lupd |= updateTexturePack();
			//updateTexturePack();
			//if (lupd) {
			//	setTextureNames();
			//}
			setMaidMode(dataManager.get(EntityLittleMaid.dataWatch_Mode));
			setDominantArm(dataManager.get(EntityLittleMaid.dataWatch_DominamtArm));
			updateMaidFlagsClient();
			updateGotcha();

			// メイド経験値の同期
			if (ticksExisted % 10 == 0) {
				//maidExperience = dataManager.get(EntityLittleMaid.dataWatch_MaidExpValue);
				this.getExperienceHandler().setMaidExperience(dataManager.get(EntityLittleMaid.dataWatch_MaidExpValue));
			}

			// 腕の挙動関連
			litemuse = dataManager.get(EntityLittleMaid.dataWatch_ItemUse);
			for (int li = 0; li < mstatSwingStatus.length; li++) {
				ItemStack lis = mstatSwingStatus[li].getItemStack(this);
				if ((litemuse & (1 << li)) > 0 && !lis.isEmpty()) {
					mstatSwingStatus[li].setItemInUse(lis, lis.getMaxItemUseDuration(), this);
				} else {
					mstatSwingStatus[li].stopUsingItem(this);
				}
			}

			// Entity初回生成時のインベントリ更新用
			// ClientサイドにおいてthePlayerが取得できるまでに時間がかかる？ので待機
			// サーバーの方が先に起動するのでクライアント側が更新を受け取れない
//			if (firstload > 0) {
//				if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
//					syncNet(LMRMessage.EnumPacketMode.SERVER_REQUEST_MODEL, null);
//					syncNet(LMRMessage.EnumPacketMode.REQUEST_CURRENT_ITEM, null);
//					firstload = 0;
//				}
//			}
		} else {
			boolean lf;
			// サーバー側
			updateRemainsContract();
			// Overdrive
			lf = maidOverDriveTime.isEnable();
			if (getMaidFlags(dataWatch_Flags_OverDrive) != lf) {
				if (lf) {
					playLittleMaidVoiceSound(EnumSound.TNT_D, false);
				}
				setMaidFlags(lf, dataWatch_Flags_OverDrive);
			}
			// Working!
			lf = workingCount.isEnable();
			if (getMaidFlags(dataWatch_Flags_Working) != lf) {
				setMaidFlags(lf, dataWatch_Flags_Working);
			}
			// トリガー登録
			lf = registerTick.isEnable();
			if (getMaidFlags(dataWatch_Flags_Register) != lf) {
				setMaidFlags(lf, dataWatch_Flags_Register);
			}
			// 拗ねる
			if (!isRemainsContract() && !isFreedom()) {
				setFreedom(true);
				setMaidWait(false);
			}
			// 移動速度の変更
			IAttributeInstance latt = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			// 属性を解除
			latt.removeModifier(attCombatSpeed);
			if (isContract()) {
				if (!isFreedom() || (getRevengeTarget() != null || getAttackTarget() != null)) {
					// 属性を設定
					latt.applyModifier(attCombatSpeed);
				}
			}
			// スニーキング判定
			latt.removeModifier(attSneakingSpeed);
			if ((onGround && isSneaking()) || isUsingItem()) {
				latt.applyModifier(attSneakingSpeed);
			}
//			isSprinting()

		}

		// 独自処理用毎時処理
//		for (EntityModeBase leb : maidEntityModeList) {
//			leb.onUpdate(maidMode);
//		}
		//メイド職業のonUpdate処理
		this.jobController.onUpdate();

		super.onUpdate();

		// Debug

		// SwingUpdate
		SwingStatus lmss1 = getSwingStatusDominant();
		prevSwingProgress = maidAvatar.prevSwingProgress = lmss1.prevSwingProgress;
		swingProgress = maidAvatar.swingProgress = lmss1.swingProgress;
		swingProgressInt = maidAvatar.swingProgressInt = lmss1.swingProgressInt;
		isSwingInProgress = maidAvatar.isSwingInProgress = lmss1.isSwingInProgress;

		// Aveterの毎時処理
		if (maidAvatar != null) {
			getAvatarIF().getValue();
			maidAvatar.onUpdate();
//			maidAvatar.setValue();
		}

		// カウンタ系
		if (mstatWaitCount > 0) {
			if (hasPath()) {
				mstatWaitCount = 0;
			} else {
				mstatWaitCount--;
			}
		}
		//if (maidSoundInterval > 0) {
		//	maidSoundInterval--;
		//}
		this.soundManager.onUpdate();
		if (ticksSinceLastDamage > 0) {
			ticksSinceLastDamage--;
		}

		// くびかしげ
		prevRotateAngleHead = rotateAngleHead;
		if (getLooksWithInterest()) {
			rotateAngleHead = rotateAngleHead + (1.0F - rotateAngleHead) * 0.4F;
		} else {
			rotateAngleHead = rotateAngleHead + (0.0F - rotateAngleHead) * 0.4F;
		}

		if (getAttackTarget() != null || getRevengeTarget() != null) {
			setWorking(true);
		}
		// お仕事カウンター
		workingCount.onUpdate();

		// 腕の挙動に関する処理
		litemuse = 0;
		for (int li = 0; li < mstatSwingStatus.length; li++) {
			mstatSwingStatus[li].onUpdate(this);
			if (mstatSwingStatus[li].isUsingItem()) {
				litemuse |= (1 << li);
			}
		}
		// 標準変数に対する数値の代入
		SwingStatus lmss = getSwingStatusDominant();
		prevSwingProgress = maidAvatar.prevSwingProgress = lmss.prevSwingProgress;
		swingProgress = maidAvatar.swingProgress = lmss.swingProgress;
		swingProgressInt = maidAvatar.swingProgressInt = lmss.swingProgressInt;
		isSwingInProgress = maidAvatar.isSwingInProgress = lmss.isSwingInProgress;

		// 持ち物の確認
		this.onInventoryChanged();

		if (!getEntityWorld().isRemote) {
			// サーバー側処理
			// アイテム使用状態の更新
			dataManager.set(EntityLittleMaid.dataWatch_ItemUse, litemuse);

			// 弓構え
			mstatAimeBow &= !getSwingStatusDominant().canAttack();
			// 構えの更新
			updateAimebow();

			// 自分より大きなものは乗っけない（イカ除く）
			if (getControllingPassenger() != null && !(getControllingPassenger() instanceof EntitySquid)) {
				if (height * width < getControllingPassenger().height * getControllingPassenger().width) {
					if (getControllingPassenger() instanceof EntityLivingBase) {
						attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)getControllingPassenger()), 0);
					}
					getControllingPassenger().dismountRidingEntity();
					return;
				}
			}

			// 斧装備時は攻撃力が上がる
			IAttributeInstance latt = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			// 属性を解除
			latt.removeModifier(attAxeAmp);
			ItemStack lis = getCurrentEquippedItem();
			if (!lis.isEmpty() && lis.getItem() instanceof ItemAxe) {
				// 属性を設定
				latt.applyModifier(attAxeAmp);
			}

//			// Auto-fix transparent maid
//			if (!isContract() && firstload > 0) {
//				if(((1 << getColor()) & (modelConfigCompound.getTextureBoxLittleMaid().wildColor)) == 0) {
//					byte r = modelConfigCompound.getWildColor();
//					if (r < 0) {
//						onSpawnWithEgg();
//					} else {
//						setColor(r);
//					}
//				}
//				firstload = 0;
//			}
		}

		// 紐で拉致
		if(mstatgotcha != null) {
			double d = mstatgotcha.getDistanceSq(this);
			if (getAttackTarget() == null) {
				// インコムごっこ用
				if (d > 4D) {
//					setPathToEntity(null);
					getNavigator().clearPath();
					getLookHelper().setLookPositionWithEntity(mstatgotcha, 15F, 15F);
				}
				if (d > 12.25D) {
//					setPathToEntity(getEntityWorld().getPathEntityToEntity(mstatgotcha, this, 16F, true, false, false, true));
					getNavigator().tryMoveToXYZ(mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, 1.0F);
					getLookHelper().setLookPositionWithEntity(mstatgotcha, 15F, 15F);
				}
			}
			if (d > 25D) {
				double d1 = mstatgotcha.posX - posX;
				double d3 = mstatgotcha.posZ - posZ;
				double d5 = 0.125D / (Math.sqrt(d1 * d1 + d3 * d3) + 0.0625D);
				d1 *= d5;
				d3 *= d5;
				motionX += d1;
				motionZ += d3;
			}
			if (d > 42.25D) {
				double d2 = mstatgotcha.posX - posX;
				double d4 = mstatgotcha.posZ - posZ;
				double d6 = 0.0625D / (Math.sqrt(d2 * d2 + d4 * d4) + 0.0625D);
				d2 *= d6;
				d4 *= d6;
				mstatgotcha.motionX -= d2;
				mstatgotcha.motionZ -= d4;
			}
			if (d > 64D) {
				setGotcha(0);
				mstatgotcha = null;
				playSound("random.drr");
			}
			if(rand.nextInt(16) == 0) {
				List<Entity> list = getEntityWorld().getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(8D, 8D, 8D));
				for (int k = 0; k < list.size(); k++) {
					Entity entity = (Entity)list.get(k);
					if (!(entity instanceof EntityMob)) {
						continue;
					}
					EntityMob entitymob = (EntityMob)entity;
					if (entitymob.getAttackTarget() == mstatgotcha) {
						//1.8検討
						entitymob.setAttackTarget(this);
						entitymob.getNavigator().setPath(getNavigator().getPath(), entitymob.moveForward);
					}
				}
			}
		}
		
		//マルチモデル関連のonUpdate処理
		if (this.world.isRemote) {
			
			//モデルリフレッシュ
			this.refreshModels();
			
			//モデルのリアルタイムサイズ変更処理
			this.modelConfigCompound.onUpdate();
		}
		
		//this.modelConfigCompound.setTextureInitServer(pName);

	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		
		//既存の復活機能を無効化
		//if (getEntityWorld().isRemote || getExperienceHandler().onDeath(par1DamageSource)) {
		//	return;
		//}
		
		//チャットメッセージの設定をConfigと差し替え
		Boolean showDeathMessages = this.world.getGameRules().getBoolean("showDeathMessages");
		this.world.getGameRules().setOrCreateGameRule("showDeathMessages", ((Boolean)LMRConfig.cfg_DeathMessage).toString());

		//死亡メッセージはMinecraftの機能で表示するように変更
		super.onDeath(par1DamageSource);
		
		//死亡メッセージ設定の復元
		this.world.getGameRules().setOrCreateGameRule("showDeathMessages", showDeathMessages.toString());
		
	}

	/**
	 * Client専用．
	 */
	private boolean manualDeath = false;

	@Override
	protected void onDeathUpdate() {
		if (!getEntityWorld().isRemote) {
			if (getExperienceHandler().onDeathUpdate()) {
				return;
			}
		} else {
			if (!manualDeath) {
				showParticleFX(EnumParticleTypes.SUSPENDED_DEPTH);
				return;
			}
		}
		super.onDeathUpdate();
	}

	@SideOnly(Side.CLIENT)
	public void manualOnDeath() {
		onDeath(new DamageSource("lmmnx_timeover"));
		manualDeath = true;
	}

	// ポーションエフェクト
	@Override
	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		super.onNewPotionEffect(par1PotionEffect);
		if (mstatMasterEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP)mstatMasterEntity).connection.sendPacket(new SPacketEntityEffect(getEntityId(), par1PotionEffect));
		}
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		super.onChangedPotionEffect(par1PotionEffect, par2);
		// TODO:必要かどうかのチェック
//		if (mstatMasterEntity instanceof EntityPlayerMP) {
//			((EntityPlayerMP)mstatMasterEntity).connection.sendPacketToPlayer(new Packet41EntityEffect(getEntityId(), par1PotionEffect));
//		}
	}

	@Override
	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		super.onFinishedPotionEffect(par1PotionEffect);
		if (mstatMasterEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP)mstatMasterEntity).connection.sendPacket(new SPacketRemoveEntityEffect(getEntityId(), par1PotionEffect.getPotion()));
		}
	}



	/**
	 *  インベントリが変更されました。
	 */
	public void onInventoryChanged() {
		
		if (!this.maidInventory.inventoryChanged) {
			return;
		}
		
		checkClockMaid();
//		checkHeadMount();
		if (this.jobController.getActiveModeClass() != null && !getHandSlotForModeChange().isEmpty())
			if (maidInventory.isChanged(InventoryLittleMaid.handInventoryOffset) ||
					maidInventory.isChanged(InventoryLittleMaid.handInventoryOffset + 1)) {
				setMaidModeAuto(getMaidMasterEntity());
			}

		//同期処理
		//インベントリの同期
		LMRNetwork.syncLittleMaidInventory(this);
		
		this.getNextEquipItem();
		
		
		//フラグリセット
		this.maidInventory.inventoryChanged = false;
		
//		setArmorTextureValue();
	}

	/**
	 * インベントリにある次の装備品を選択
	 */
	public boolean getNextEquipItem() {
		int li;
		if (this.jobController.isActiveModeClass()) {
			li = this.jobController.getActiveModeClass().getNextEquipItem(this.jobController.getMaidModeString());
		} else {
			li = -1;
		}
		setEquipItem(getDominantArm(), li);
		return li > -1;
	}

	public ItemStack getHandSlotForModeChange() {
		return maidInventory.getStackInSlot(InventoryLittleMaid.handInventoryOffset);
	}

	public void setEquipItem(int pArm, int pIndex) {
//		if (pArm == getDominantArm()) {
		if (pIndex >= maidInventory.getSizeInventory()) {
			pIndex = -1;
		}
			maidInventory.currentItem = pIndex;
			this.dataManager.set(dataWatch_CurrentItem, maidInventory.currentItem);
//		}
		int li = mstatSwingStatus[pArm].index;
		if (li != pIndex) {
			if (li > -1) {
				maidInventory.setChanged(li);
			}
			if (pIndex > -1) {
				maidInventory.setChanged(pIndex);
			}
			mstatSwingStatus[pArm].setSlotIndex(pIndex);
		}
	}
	public void setEquipItem(int pIndex) {
		setEquipItem(getDominantArm(), pIndex);
	}


	/**
	 * 対応型射撃武器のリロード判定
	 */
	public void getWeaponStatus() {
		// 飛び道具用の特殊処理
		ItemStack is = maidInventory.getCurrentItem();
		if (is.isEmpty()) return;

		try {
			Method me = is.getItem().getClass().getMethod("isWeaponReload", ItemStack.class, EntityPlayer.class);
			weaponReload = (Boolean)me.invoke(is.getItem(), is, maidAvatar);
		}catch (NoClassDefFoundError e) {
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
		}

		try {
			Method me = is.getItem().getClass().getMethod("isWeaponFullAuto", ItemStack.class);
			weaponFullAuto = (Boolean)me.invoke(is.getItem(), is);
		}catch (NoClassDefFoundError e) {
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
		}
	}

	// 保持アイテム関連

	/**
	 * 現在の装備品
	 */
	public ItemStack getCurrentEquippedItem() {
		return maidInventory.getCurrentItem();
	}

	@Override
	public ItemStack getHeldItem(EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND) {
			return maidInventory.getCurrentItem();
		}
		return maidInventory.getStackInSlot(InventoryLittleMaid.handInventoryOffset + 1);
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return maidInventory.armorInventory;
	}

	@Override
	public Iterable<ItemStack> getHeldEquipment() {
		return Arrays.asList(new ItemStack[]{getCurrentEquippedItem(), getHeldItemOffhand()});
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		if (slotIn == EntityEquipmentSlot.OFFHAND) {
			return getHeldItem(EnumHand.OFF_HAND);
		}
		if (slotIn == EntityEquipmentSlot.MAINHAND) {
			return getHeldItem(EnumHand.MAIN_HAND);
		} else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
			return maidInventory.armorInventory.get(slotIn.getIndex());
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		if (slotIn == EntityEquipmentSlot.MAINHAND) {
			maidInventory.setInventoryCurrentSlotContents(stack);
		} else if (slotIn == EntityEquipmentSlot.OFFHAND) {
			maidInventory.setInventorySlotContents(InventoryLittleMaid.handInventoryOffset + 1, stack);
		} else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
			maidInventory.setInventorySlotContents(slotIn.getIndex() + InventoryLittleMaid.maxInventorySize, stack);
			////アーマースロット変更時にテクスチャを再設定する
			//setTextureNames();
		} else {
			// TODO What was this used for?
/*
			par1 -= 5;
			// 持ち物のアップデート
			// 独自拡張:普通にスロット番号の通り、上位８ビットは装備スロット
			// par1はShortで渡されるのでそのように。
			int lslotindex = par1 & 0x7f;
			int lequip = (par1 >>> 8) & 0xff;
			maidInventory.setInventorySlotContents(lslotindex, stack);
			maidInventory.resetChanged(lslotindex);	// これは意味ないけどな。
			maidInventory.inventoryChanged = true;
//			if (par1 >= maidInventory.mainInventory.length) {
//				LMM_Client.setArmorTextureValue(this);
//			}

			for (SwingStatus lss: mstatSwingStatus) {
				if (lslotindex == lss.index) {
					lss.index = -1;
				}
			}
			if (lequip != 0xff) {
				setEquipItem(lequip, lslotindex);
//				mstatSwingStatus[lequip].index = lslotindex;
			}
			if (lslotindex >= InventoryLittleMaid.maxInventorySize) {
				setTextureNames();
			}
			String s = stack == null ? null : stack.getDisplayName();
			LittleMaidReengaged.Debug(String.format("ID:%d Slot(%2d:%d):%s", getEntityId(), lslotindex, lequip, s == null ? "NoItem" : s));
*/
		}
	}

	@Override
	public boolean isMovementBlocked() {
		return super.isMovementBlocked();
	}

	public double getDistanceSqToMaster() {
		return mstatMasterDistanceSq;
	}

	protected void checkClockMaid() {
		// 時計を持っているか？
		mstatClockMaid = maidInventory.getInventorySlotContainItem(Items.CLOCK) > -1;
	}
	/**
	 * 時計を持っているか?
	 */
	public boolean isClockMaid() {
		return mstatClockMaid;
	}

	/**
	 * メットを被ってるか
	 */
	public boolean isMaskedMaid() {
		return !getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();//mstatMaskSelect > -1;
	}

//	protected void checkHeadMount() {
		// 追加の頭部装備の判定
		// TODO Render Head.
/*
		ItemStack lis = maidInventory.getHeadMount();
		mstatPlanter = false;
		mstatCamouflage = false;
		if (lis != null) {
			if (lis.getItem() instanceof ItemBlock) {
				Block lblock = Block.getBlockFromItem(lis.getItem());
//				mstatPlanter =	(lblock instanceof BlockFlower	  && lblock.getRenderType() ==  1) ||
				mstatPlanter =	(lblock.getRenderType() ==  1) ||
								(lblock instanceof BlockDoublePlant && lblock.getRenderType() == 40);
				mstatCamouflage = (lblock instanceof BlockLeaves) || (lblock instanceof BlockPumpkin) || (lblock instanceof BlockStainedGlass);
			} else if (lis.getItem() instanceof ItemSkull) {
				mstatCamouflage = true;
			}
		}
*/
//	}
	
	/**
	 * カモフラージュ！
	 */
//	public boolean isCamouflage() {
//		return mstatCamouflage;
//	}
	
//	/**
//	 * 鉢植え状態
//	 */
//	public boolean isPlanter() {
//		return mstatPlanter;
//	}

	/**
	 * ポーション等による腕振りモーションの速度補正
	 */
	public int getSwingSpeedModifier() {
		if (isPotionActive(Potion.getPotionFromResourceLocation("haste"))) {
			return 6 - (1 + getActivePotionEffect(Potion.getPotionFromResourceLocation("haste")).getAmplifier()) * 1;
		}

		if (isPotionActive(Potion.getPotionFromResourceLocation("mining_fatigue"))) {
			return 6 + (1 + getActivePotionEffect(Potion.getPotionFromResourceLocation("mining_fatigue")).getAmplifier()) * 2;
		}
		return 6;
	}

	/**
	 * 手持ちアイテムの破壊
	 */
	public void destroyCurrentEquippedItem() {
		maidInventory.setInventoryCurrentSlotContents(ItemStack.EMPTY);
	}

	/**
	 * メイドインベントリを開く
	 * @param pEntityPlayer
	 */
	public void displayGUIMaidInventory(EntityPlayer pEntityPlayer) {
		if (!getEntityWorld().isRemote) {
			GuiHandler.maidServer = this;
			pEntityPlayer.openGui(LittleMaidReengaged.instance, GuiHandler.GUI_ID_INVVENTORY, getEntityWorld(),
					(int)posX, (int)posY, (int)posZ);
		}
		else
		{
			GuiHandler.maidClient = this;
		}
	}

	@Override
	public boolean processInteract(EntityPlayer par1EntityPlayer, EnumHand hand)
	{
		LittleMaidReengaged.Debug(getEntityWorld().isRemote, "LMM_EntityLittleMaid.interact:"+par1EntityPlayer.getGameProfile().getName());
		float lhealth = getHealth();
		
		ItemStack par3ItemStack = par1EntityPlayer.getHeldItem(hand);

		// プラグインでの処理を先に行う
//		for (int li = 0; li < maidEntityModeList.size(); li++) {
//			if (maidEntityModeList.get(li).preInteract(par1EntityPlayer, par3ItemStack)) {
//				return true;
//			}
//		}
		//メイドさん職業の処理
		if (this.jobController.preProcessInteract(par1EntityPlayer, par3ItemStack)) return true;
		
		// しゃがみ時は処理無効
		if (par1EntityPlayer.isSneaking()) {
			return false;
		}
		// ナデリ判定
		if (lhealth > 0F && par1EntityPlayer.getControllingPassenger() != null && !(par1EntityPlayer.getControllingPassenger() instanceof EntityLittleMaid)) {
			// 載せ替え
			par1EntityPlayer.getControllingPassenger().startRiding(this);
			return true;
		}

		if (mstatgotcha == null && par1EntityPlayer.fishEntity == null) {
			if(!par3ItemStack.isEmpty() && par3ItemStack.getItem() == Items.LEAD) {
				// 紐で繋ぐ
				setGotcha(par1EntityPlayer.getEntityId());
				mstatgotcha = par1EntityPlayer;
				par3ItemStack.splitStack(1);
				playSound("entity.item.pickup");
				return true;
			}

			if (isContract()) {
				// Issue #35: 契約失効時の処理を優先化
				if(!isRemainsContract() && !par3ItemStack.isEmpty()){
					// ストライキ
					if (ItemHelper.isSugar(par3ItemStack)) {
						// 受取拒否
						getEntityWorld().setEntityState(this, (byte)10);
						return true;
					} else if (ItemHelper.isCake(par3ItemStack)) {
						// 再契約
						par3ItemStack.splitStack(1);
						maidContractLimit = (24000 * 7);
						setFreedom(false);
						setTracer(false);
						setMaidWait(false);
						setMaidMode(EntityMode_Basic.mmode_Escort);
						if(!isMaidContractOwner(par1EntityPlayer)){
							// あんなご主人なんか捨てて、僕のもとへおいで(洗脳)
							OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));
							playLittleMaidVoiceSound(EnumSound.getCake, false);
							getEntityWorld().setEntityState(this, (byte)7);
							maidContractLimit = (24000 * 7);
							maidAnniversary = getEntityWorld().getTotalWorldTime();
						}else{
							// ごめんねメイドちゃん
							getEntityWorld().setEntityState(this, (byte)11);
							playLittleMaidVoiceSound(EnumSound.Recontract, false);

						}
						return true;
					}

				}
				// 契約状態
				if (/*lhealth > 0F && */isMaidContractOwner(par1EntityPlayer)) {
					if (!par3ItemStack.isEmpty()) {
						// 追加分の処理
						// プラグインでの処理を先に行う
						//for (int li = 0; li < maidEntityModeList.size(); li++) {
						//	if (maidEntityModeList.get(li).interact(par1EntityPlayer, par3ItemStack)) {
						//		return true;
						//	}
						//}
						//メイドさん職業の処理
						if (this.jobController.processInteract(par1EntityPlayer, par3ItemStack)) return true;
						
						if (isRemainsContract()) {
							// 通常
							if (ItemHelper.isSugar(par3ItemStack)) {
								// モード切替
								boolean cmode = ItemHelper.onSugarInteract(this, par1EntityPlayer, par3ItemStack);
								//boolean cmode = true;
								//if(par3ItemStack.getItem() instanceof IItemSpecialSugar){
								//	cmode = ((IItemSpecialSugar)par3ItemStack.getItem()).onSugarInteract(getEntityWorld(), par1EntityPlayer, par3ItemStack, this);
								//}
								par3ItemStack.splitStack(1);
								eatSugar(false, true, false);
								if(!cmode) return true;
								getEntityWorld().setEntityState(this, (byte)11);

								// TODO 口開くよ…
								LittleMaidReengaged.Debug("give suger." + getEntityWorld().isRemote);
								if (!getEntityWorld().isRemote) {
									setFreedom(isFreedom());
									if (isMaidWait()) {
										// 動作モードの切替
										setMaidModeAuto(par1EntityPlayer);
										setMaidWait(false);
									} else {
										// 待機
										setMaidWait(true);
									}
								}
								return true;
							}
							//お座りモーション判定
							else if (par3ItemStack.getItem() == Item.getItemFromBlock(Blocks.CARPET)) {
								
								//カーペットは消費しない

								//♪の表示
								getEntityWorld().setEntityState(this, (byte)11);
								//効果音
								playSound("entity.item.pickup");
								
								int maidWaitMotion = dataManager.get(EntityLittleMaid.dataWatch_WaitMotion);
								maidWaitMotion = maidWaitMotion == 1 ? 0 : 1;
								dataManager.set(EntityLittleMaid.dataWatch_WaitMotion, maidWaitMotion);
								return true;
							}
							//ミルク
							else if (par3ItemStack.getItem() == Items.MILK_BUCKET) {
								
								//バフデバフを全てクリア
								//♪の表示
								getEntityWorld().setEntityState(this, (byte)11);
								
								//効果音
								playSound("entity.item.pickup");
								
								//ポーション効果をリセット
								this.clearActivePotions();
								par1EntityPlayer.setHeldItem(hand, new ItemStack(Items.BUCKET));
								
								return true;
							}
							//バケツのみ
							else if (LMRConfig.cfg_custom_maid_milk
									&& par3ItemStack.getItem() == Items.BUCKET) {
								
								//ハートの表示
								getEntityWorld().setEntityState(this, (byte)7);
								
								//効果音
								playSound("entity.cow.milk");
								
								//牛乳の取得
								ItemStack milkStack = new ItemStack(Items.MILK_BUCKET);
								
								//○○印のミルク名を設定
								if (LMRConfig.cfg_secret_maid_milk) {
									String milkLabel = LMRConfig.cfg_secret_maid_milk_producer_default;
									if (this.hasCustomName()) {
										milkLabel = this.getCustomNameTag();
									}
									
									//整形
									milkLabel = String.format(LMRConfig.cfg_secret_maid_milk_producer_label, milkLabel);
									
									//名前をセット
									NBTTagCompound milkTag = new NBTTagCompound();
									NBTTagCompound displayTag = new NBTTagCompound();
									displayTag.setString("Name", milkLabel);
									milkTag.setTag("display", displayTag);
									milkStack.setTagCompound(milkTag);
								}
								
								//手持ちのアイテムを減らしてミルクバケツをセットする
								setItemStackPlayerHand(milkStack, hand, par1EntityPlayer);

								return true;
							}
							else if (par3ItemStack.getItem()==LMItems.REGISTERKEY &&
									!par1EntityPlayer.getEntityWorld().isRemote) {
								// トリガーセット
								if (registerTick.isEnable()) {
									registerTick.setEnable(false);
									par1EntityPlayer.sendMessage(new TextComponentTranslation("littleMaidMob.chat.text.cancelregistration").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
									return true;
								}

								NBTTagCompound tagCompound = par3ItemStack.getTagCompound();
								if (tagCompound == null) return false;

								String modeString = tagCompound.getString(ItemTriggerRegisterKey.RK_MODE_TAG);
								if (modeString.isEmpty()) return false;

								registerMode = modeString;
								registerTick.setValue(200);

								int count = tagCompound.getInteger(ItemTriggerRegisterKey.RK_COUNT);
								if(++count >= ItemTriggerRegisterKey.RK_MAX_COUNT) {
									par1EntityPlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
								}
								tagCompound.setInteger(ItemTriggerRegisterKey.RK_COUNT, count);

								par1EntityPlayer.sendMessage(new TextComponentTranslation("littleMaidMob.chat.text.readyregistration", registerMode));
								if(count >= ItemTriggerRegisterKey.RK_MAX_COUNT-10){
									if(count<ItemTriggerRegisterKey.RK_MAX_COUNT){
										par1EntityPlayer.sendMessage(new TextComponentTranslation("littleMaidMob.chat.text.warningcount",
												(ItemTriggerRegisterKey.RK_MAX_COUNT-count)).setStyle(new Style().setColor(TextFormatting.YELLOW)));
									} else {
										par1EntityPlayer.sendMessage(new TextComponentTranslation("littleMaidMob.chat.text.endcount")
												.setStyle(new Style().setColor(TextFormatting.DARK_RED)));
									}
								}

								return true;
							} else if (registerTick.isEnable() && !par1EntityPlayer.getEntityWorld().isRemote) {
								// TODO Trigger Save each maid
								registerTick.setEnable(false);
								return true;
							} else if (par3ItemStack.getItem() == Items.DYE) {
								// カラーメイド
								if (canChangeModel()) {
									if (!getEntityWorld().isRemote) {
										setColor((byte)(15 - par3ItemStack.getItemDamage()));
									}
									return true;
								} else {
									// TODO print block-message
								}
							}
							else if (par3ItemStack.getItem() == Items.FEATHER) {
								// 自由行動
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								if(!getEntityWorld().isRemote){
									setFreedom(!isFreedom());
									getEntityWorld().setEntityState(this, isFreedom() ? (byte)12 : (byte)13);
								}
								return true;
							} else if (par3ItemStack.getItem() == Items.GUNPOWDER) {
								// test TNT-D
								maidOverDriveTime.setValue(par3ItemStack.getCount() * 10);
								playSound("mob.zombie.infect");
								if (par3ItemStack.getCount() == 64) {
									//進捗
									AchievementsLMRE.grantAC(getMaidMasterEntity(), AC.Boost);
								}
								par3ItemStack.splitStack(par3ItemStack.getCount());
								return true;
							}
							else if (par3ItemStack.getItem() == Items.BOOK) {
								// IFFのオープン
//								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//								if (getEntityWorld().isRemote) {
									par1EntityPlayer.openGui(LittleMaidReengaged.instance,
											GuiHandler.GUI_ID_IFF,
											getEntityWorld(),
											(int)posX,
											(int)posY,
											(int)posZ);
//								}
								return true;
							}/*
							else if ((itemstack1.getItem() == Items.glass_bottle) && (experienceValue >= 5)) {
								// Expボトル
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								if (!getEntityWorld().isRemote) {
									entityDropItem(new ItemStack(Items.experience_bottle), 0.5F);
									experienceValue -= 5;
									if (maidAvatar != null) {
										maidAvatar.experienceTotal -= 5;
									}
								}
								return true;
							}*/
							else if (par3ItemStack.getItem() instanceof ItemPotion) {
								// ポーション
								if(!getEntityWorld().isRemote) {
									List<PotionEffect> list = PotionUtils.getEffectsFromStack(par3ItemStack);
									if (list != null) {
										PotionEffect potioneffect;
										for (Iterator<PotionEffect> iterator = list.iterator(); iterator.hasNext(); addPotionEffect(new PotionEffect(potioneffect))) {
											potioneffect = iterator.next();
										}
									}
								}
								par3ItemStack.splitStack(1);
								return true;
							}
							else if (isFreedom() && par3ItemStack.getItem() == Items.REDSTONE) {
								// Tracer
								par3ItemStack.splitStack(1);
								setMaidWait(false);
								setTracer(!isTracer());
								if (isTracer()) {
									getEntityWorld().setEntityState(this, (byte)14);
								} else {
									getEntityWorld().setEntityState(this, (byte)12);
								}
								return true;
							}else if(par3ItemStack.getItem() == Items.STICK){
								if(getDominantArm()==0){
									setDominantArm(1);
								}else{
									setDominantArm(0);
								}
								return true;
							}
						}
					}
					// メイドインベントリ
					OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));
					getNavigator().clearPath();
					isJumping = false;
					if(!getEntityWorld().isRemote){
						syncMaidArmorVisible();
						syncExpBoost();
					}
					displayGUIMaidInventory(par1EntityPlayer);
					return true;
				}
			} else {
				// 未契約
				if (!par3ItemStack.isEmpty()) {
					if (ItemHelper.isCake(par3ItemStack)) {
						// 契約
						par3ItemStack.splitStack(1);

						deathTime = 0;
						if (!getEntityWorld().isRemote) {
							
							//初回契約メソッドをまとめる
							this.setFirstContract(par1EntityPlayer);
							
							//進捗
							//AchievementsLMRE.grantAC(par1EntityPlayer, AC.Contract);
							//setContract(true);
							//getNavigator().clearPath();
							//OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(par1EntityPlayer));

							playLittleMaidVoiceSound(EnumSound.getCake, false);
//							playLittleMaidSound(LMM_EnumSound.getCake, true);
//							playTameEffect(true);
							getEntityWorld().setEntityState(this, (byte)7);
							//// 契約記念日と、初期契約期間
							//maidContractLimit = (24000 * 7);
							//maidAnniversary = getEntityWorld().getTotalWorldTime();

							//setHealth(20);
							//setPlayingRole(PlayRole.NOTPLAYING);
							//setMaidWait(false);
							//setFreedom(false);
							//setMaidMode(EntityMode_Basic.mmode_Escort);
							// テクスチャのアップデート:いらん？
//							LMM_Net.sendToAllEClient(this, new byte[] {LMM_Net.LMN_Client_UpdateTexture, 0, 0, 0, 0});

						}
						return true;
					}
//					getEntityWorld().setEntityState(this, (byte)6);
				}
			}
		} else if (/*lhealth > 0F && */mstatgotcha != null) {
			if (!getEntityWorld().isRemote) {
				EntityItem entityitem = new EntityItem(getEntityWorld(), mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Items.STRING));
				getEntityWorld().spawnEntity(entityitem);
				setGotcha(0);
				mstatgotcha = null;
			}
			return true;
		}

		return false;
	}

	/**
	 * Set maid's mode automatically.
	 * @return whether the mode was changed
	 */
	private boolean setMaidModeAuto(EntityPlayer par1EntityPlayer) {
		boolean lflag = false;
		String orgnMode = getMaidModeString();

//		setActiveModeClass(null);
		//for (int li = 0; li < maidEntityModeList.size() && !lflag; li++) {
		//	lflag = maidEntityModeList.get(li).changeMode(par1EntityPlayer);
		//	if (lflag) {
		//		this.jobController.setActiveModeClass(maidEntityModeList.get(li));
		//	}
		//}
		
		//職業変更
		lflag = this.jobController.changeMode(par1EntityPlayer);
		
		if (!lflag) {
			setMaidMode(EntityMode_Basic.mmode_Escort);
			setEquipItem(-1);
//			maidInventory.currentItem = -1;
		}
		getNextEquipItem();

		if (!orgnMode.equals(getMaidModeString())) {
			clearTilePosAll();
			getNavigator().clearPath();
			setAttackTarget(null);
			return true;
		}
		return false;
	}

	// メイドの契約設定
	@Override
	public boolean isTamed() {
		return isContract();
	}
	public boolean isContract() {
//		return getEntityWorld().isRemote ? maidContract : super.isTamed();
		return super.isTamed();
	}
	public boolean isContractEX() {
		return isContract() && isRemainsContract();
	}

	@Override
	public void setTamed(boolean par1) {
		setContract(par1);
	}
	
	public void setContract(boolean flag) {
		super.setTamed(flag);
		modelConfigCompound.setContract(flag);
	}
	
	/**
	 * 初回契約用メソッド
	 * 
	 * エフェクトは実行しない
	 */
	public void setFirstContract(EntityPlayer player) {
		boolean contractFlag = true;
		super.setTamed(contractFlag);
		modelConfigCompound.setContract(contractFlag);
		
		//初期設定
		
		//進捗設定
		AchievementsLMRE.grantAC(player, AC.Contract);
		
		setContract(true);
		getNavigator().clearPath();
		OwnableEntityHelper.setOwner(this, CommonHelper.getPlayerUUID(player));

		// 契約記念日と、初期契約期間
		maidContractLimit = (24000 * 7);
		maidAnniversary = getEntityWorld().getTotalWorldTime();

		setHealth(20);
		setPlayingRole(PlayRole.NOTPLAYING);
		setMaidWait(false);
		setFreedom(false);
		setMaidMode(EntityMode_Basic.mmode_Escort);		
	}

	/**
	 * 契約期間の残りがあるかを確認
	 */
	protected void updateRemainsContract() {
		boolean lflag = false;
		if (maidContractLimit > 0) {
			maidContractLimit--;
			lflag = true;
		}
		if (getMaidFlags(dataWatch_Flags_remainsContract) != lflag) {
			setMaidFlags(lflag, dataWatch_Flags_remainsContract);
		}
	}
	/**
	 * ストライキに入っていないか判定
	 * @return
	 */
	public boolean isRemainsContract() {
		return getMaidFlags(dataWatch_Flags_remainsContract);
	}

	public float getContractLimitDays() {
		return maidContractLimit > 0 ? (maidContractLimit / 24000F) : -1F;
	}

	public boolean updateMaidContract() {
		// 同一性のチェック
		boolean lf = isContract();
		if (modelConfigCompound.isContract() != lf) {
			modelConfigCompound.setContract(lf);
			return true;
		}
		return false;
	}

	@Override
	@Deprecated
	public EntityLivingBase getOwner() {
		return getMaidMasterEntity();
	}

	public UUID getMaidMasterUUID() {
		return OwnableEntityHelper.getOwner(this);
	}

	@Nullable
	public EntityPlayer getMaidMasterEntity() {
		// 主を獲得
		if (isContract()) {
			/*
			EntityPlayer entityplayer = mstatMasterEntity;
			if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
				UUID lname;
				// サーバー側ならちゃんとオーナ判定する

				// Minecraftクラスのプレイヤーを取得していたが、サーバには存在しないためプロキシをかませる。サーバならNULL固定
				EntityPlayer clientPlayer = LittleMaidReengaged.proxy.getClientPlayer();

				if (!LittleMaidReengaged.proxy.isSinglePlayer()
						|| clientPlayer == null) {
					lname = getMaidMasterUUID();
				} else {
					lname = CommonHelper.getPlayerUUID(clientPlayer);
				}
				entityplayer = getEntityWorld().getPlayerEntityByUUID(lname);
				// とりあえず主の名前を入れてみる
				// TODO:再設定は不可になったので経過観察
//				maidAvatar.username = lname;

				if (entityplayer != null && maidAvatar != null) {
					maidAvatar.capabilities.isCreativeMode = entityplayer.capabilities.isCreativeMode;
				}

			}
			return entityplayer;
			*/
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
		}
		return null;
	}

	//未使用削除
	//public boolean isMaidContractOwner(UUID pname) {
	//	return pname.equals(CommonHelper.getPlayerUUID(mstatMasterEntity));
	//}

	/**
	 * メイドさんのご主人様判定を行う
	 * @param pentity
	 * @return
	 */
	public boolean isMaidContractOwner(EntityPlayer pentity) {
		
		//みんなのメイドさん
		if (LMRConfig.cfg_cstm_everyones_maid && pentity != null) {
			return true;
		}
		
		return pentity == getMaidMasterEntity();

//		return pentity == mstatMasterEntity;
	}

	// メイドの待機設定
	public boolean isMaidWait() {
		return maidWait;
	}

	public boolean isMaidWaitEx() {
		return isMaidWait() | (mstatWaitCount > 0) | isOpenInventory();
	}

	public void setMaidWait(boolean pflag) {
		// 待機常態の設定、 isMaidWait系でtrueを返すならAIが勝手に移動を停止させる。
		maidWait = pflag;
		setMaidFlags(pflag, dataWatch_Flags_Wait);
		aiSit.setSitting(pflag);
		//maidWait = pflag;
		isJumping = false;
		setAttackTarget(null);
		setRevengeTarget(null);
		//setPathToEntity(null);
		getNavigator().clearPath();
		setPlayingRole(PlayRole.NOTPLAYING);
		if(pflag){
			//setMaidModeAITasks(null,null);
			setWorking(false);
			getNavigator().clearPath();
			clearTilePosAll();
			/*
			setHomePosAndDistance(
					new BlockPos(MathHelper.floor(lastTickPosX),
					MathHelper.floor(lastTickPosY),
					MathHelper.floor(lastTickPosZ)), 0);
					*/
		}else{
			setMaidMode(this.jobController.getMaidModeString(), false);
		}
		velocityChanged = true;
	}

	public void setMaidWaitCount(int count) {
		mstatWaitCount = count;
	}


	// インベントリの表示関係
	// まさぐれるのは一人だけ
	public void setOpenInventory(boolean flag) {
		mstatOpenInventory = flag;
	}

	public boolean isOpenInventory() {
		return mstatOpenInventory;
	}

	/**
	 * GUIを開いた時にサーバー側で呼ばれる。
	 */
	public void onGuiOpened() {
		setOpenInventory(true);
	}

	/**
	 * GUIを閉めた時にサーバー側で呼ばれる。
	 */
	public void onGuiClosed() {
		setOpenInventory(false);
		// TODO Waiting?
//		int li = maidMode & 0x0080;
		setMaidWaitCount(50);
		
		//Inventory Change
		maidInventory.inventoryChanged = true;
	}

	// 腕振り
	public void setSwing(int attacktime, EnumSound enumsound, boolean force) {
		setSwing(attacktime, enumsound, getDominantArm(), force);
	}

	public void setSwing(int pattacktime, EnumSound enumsound, int pArm, boolean force) {
		mstatSwingStatus[pArm].attackTime = pattacktime;
//		maidAttackSound = enumsound;
//		soundInterval = 0;// いるか？
		if (!weaponFullAuto) {
			setSwinging(pArm, enumsound, force);
		}
		if (!getEntityWorld().isRemote) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setByte("Arm", (byte) pArm);
			tagCompound.setInteger("Sound", enumsound.index);
			tagCompound.setBoolean("Force", force);

			syncNet(LMRMessage.EnumPacketMode.CLIENT_SWINGARM, tagCompound);
		}
	}

	public void setSwinging(EnumSound pSound, boolean force) {
		setSwinging(getDominantArm(), pSound, force);
	}
	public void setSwinging(int pArm, EnumSound pSound, boolean force) {
		if(!pSound.equals(EnumSound.Null)) playLittleMaidVoiceSound(pSound, !force);
		if (mstatSwingStatus[pArm].setSwinging()) {
			maidAvatar.swingProgressInt = -1;
//			maidAvatar.swingProgressInt = -1;
			maidAvatar.isSwingInProgress = true;
		}
	}

	public boolean getSwinging() {
		return getSwinging(getDominantArm());
	}
	public boolean getSwinging(int pArm) {
		return mstatSwingStatus[pArm].isSwingInProgress;
	}

	/**
	 * 利き腕のリロードタイム
	 */
	public SwingStatus getSwingStatusDominant() {
		return mstatSwingStatus[getDominantArm()];
	}

	public SwingStatus getSwingStatus(int pindex) {
		return mstatSwingStatus[pindex];
	}


	public int getDominantArm() {
		return maidDominantArm;
	}
	
	/**
	 * 利き手
	 */
	@Override
	public EnumHandSide getPrimaryHand() {
		return this.getDominantArm() == 0 ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
	}

	// 今宵のメイドは血に飢えておる
	public void setBloodsuck(boolean pFlag) {
		mstatBloodsuck = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_Bloodsuck);
	}

	public boolean isBloodsuck() {
		return mstatBloodsuck;
	}


	// 砂糖関連
	public void setLookSuger(boolean pFlag) {
		mstatLookSuger = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_LooksSugar);
	}

	/**
	 * 砂糖をみているか判定
	 * 固定モーション用分岐追加
	 * @return
	 */
	public boolean isLookSuger() {
		if (this.maidMotion == EnumMaidMotion.LOOKSUGAR) {
			return true;
		}
		return mstatLookSuger;
	}

	public static enum EnumConsumeSugar{
		/**通常回復**/HEAL,
		/**契約更新**/RECONTRACT,
		/**その他（つまみ食いとか）**/OTHER
	};

	/** インベントリ内の砂糖を食べる。左上から消費する。
	 * @param mode EnumConsumeSugar型の定数
	 */
	protected void consumeSugar(EnumConsumeSugar mode){
		NonNullList<ItemStack> stacklist = maidInventory.mainInventory;
		ItemStack stack = ItemStack.EMPTY;
		Item item = null;
		int index = -1;
		for(int i = 0; i < stacklist.size(); i++){
			ItemStack ts = stacklist.get(i);
			if (ts.isEmpty())continue;
			Item ti = ts.getItem();
			if (ItemHelper.isSugar(ts)){
				stack = ts;
				item = ti;
				index = i;
				break;
			}
		}
		if(item == null || stack.isEmpty() || index == -1) return;
		//if(item == Items.SUGAR){
		//	eatSugar(true, true, mode==EnumConsumeSugar.RECONTRACT);
		//}else if(item instanceof IItemSpecialSugar){
		//	//モノグサ実装。良い子の皆さんはちゃんとif使うように…
		//	eatSugar(((IItemSpecialSugar)item).onSugarEaten(this, mode, stack), true, mode==EnumConsumeSugar.RECONTRACT);
		//}
		if (ItemHelper.isSugar(stack)) {
			eatSugar(ItemHelper.onSugarEaten(this, mode, stack), true, mode==EnumConsumeSugar.RECONTRACT);
		}
		if (mode == EnumConsumeSugar.RECONTRACT) {
			addMaidExperience(3.5f);
		}
		maidInventory.decrStackSize(index, Math.max(1, mode == EnumConsumeSugar.OTHER ? 1 : this.experienceHandler.getExpBooster()));
	}

	/** 主に砂糖を食べる仕草やその後のこと。consumeSugar()から呼ばれる．
	 * interact()等メイドインベントリ外の砂糖を食べさせるときはこっちを直接呼ぶべし．
	 * ペロッ・・・これは・・・砂糖ッ！！
	 * @param heal デフォルトの1回復をするか？
	 * @param motion 腕を振るか？
	 * @param recontract 契約延長効果アリ？
	 */
	public void eatSugar(boolean heal, boolean motion, boolean recontract) {
		if (motion) {
			setSwing(2, (getMaxHealth() - getHealth() <= 1F) ?  EnumSound.eatSugar_MaxPower : EnumSound.eatSugar, false);
		}
		int h = hurtResistantTime;
		if(heal) {
			heal(1);
		}
		hurtResistantTime = h;
		playSound("entity.item.pickup");
		LittleMaidReengaged.Debug(("eat Sugar." + getEntityWorld().isRemote));

		if (recontract) {
			// 契約期間の延長
			maidContractLimit += 24000;
			if (maidContractLimit > 168000) {
				maidContractLimit = 168000;	// 24000 * 7
			}

			if (getEntityWorld().getTotalWorldTime() - maidAnniversary > 24000 * 365) {
				//進捗
				AchievementsLMRE.grantAC(getMaidMasterEntity(), AC.MyFavorite);
			}
		}

		// 暫定処理
		if (maidAvatar != null) {
			maidAvatar.getFoodStats().addStats(20, 20F);
		}
	}


	// お仕事チュ
	/**
	 * 仕事中かどうかの設定
	 */
	public void setWorking(boolean pFlag) {
		workingCount.setEnable(pFlag);
	}

	/**
	 * 仕事中かどうかを返す
	 */
	public boolean isWorking() {
		return workingCount.isEnable();
	}

	/**
	 * 仕事が終了しても余韻を含めて返す
	 */
	public boolean isWorkingDelay() {
		return workingCount.isDelay();
	}

	/**
	 * トレーサーモードの設定
	 */
	public void setTracer(boolean pFlag) {
		maidTracer = pFlag;
		aiTracer.setEnable(pFlag);
		setMaidFlags(pFlag, dataWatch_Flags_Tracer);
//		if (maidTracer) {
//			setFreedom(true);
//		}
		aiTracer.setEnable(pFlag);
	}

	/**
	 * トレーサーモードであるか？
	 */
	public boolean isTracer() {
		return getMaidFlags(dataWatch_Flags_Tracer);
	}


	// お遊びモード
	public void setPlayingRole(PlayRole pValue) {

		if (!mstatPlayingRole.equals(pValue)) {
			mstatPlayingRole = pValue;
			if (pValue.equals(PlayRole.NOTPLAYING)) {
				setAttackTarget(null);
				setMaidMode(mstatWorking , true);
			} else {
				setMaidMode(EntityMode_Playing.mmode_Playing, true);
			}
		}
	}

	public PlayRole getPlayingRole() {
		return mstatPlayingRole;
	}

	public boolean isPlaying() {
		return !mstatPlayingRole.equals(PlayRole.NOTPLAYING);
	}


	// 自由行動
	public void setFreedom(boolean pFlag) {
		// AI関連のリセットもここで。
		maidFreedom = pFlag;
		if (!getEntityWorld().isRemote) {
//			aiRestrictRain.setEnable(pFlag);
			aiFreeRain.setEnable(pFlag);
			aiWander.setEnable(pFlag);
//			aiJumpTo.setEnable(!pFlag);
//			aiAvoidPlayer.setEnable(!pFlag);
			aiFollow.setEnable(!pFlag);
			aiMoveTowardsRestriction.setEnable(maidFreedom);
			aiTracer.setEnable(isTracer()&&pFlag);
//			setAIMoveSpeed(pFlag ? moveSpeed_Nomal : moveSpeed_Max);
//			setMoveForward(0.0F);
			setPlayingRole(PlayRole.NOTPLAYING);
			if (maidFreedom && isContract()) {
				setTracer(isTracer());
				setHomePosAndDistance(getPosition(), (int) getMaximumHomeDistance());
			} else {
				detachHome();
			}
			setMaidFlags(maidFreedom, dataWatch_Flags_Freedom);
		} else {
		}
	}

	@Override
	public float getMaximumHomeDistance() {
		if (this.jobController.getActiveModeClass() != null) {
			return MathHelper.sqrt(this.jobController.getActiveModeClass().getFreedomTrackingRangeSq());
		}
		return 20f;
	}

	public boolean isFreedom() {
		return maidFreedom;
	}

//	public void onWarp() {
//		getActiveModeClass().onWarp();
//	}

	public boolean isHeadMount(){
		return ItemUtil.isHelm(maidInventory.armorInventory.get(3));
	}

//	/**
//	 *  レベルを取得
//	 * @return
//	 */
//	public int getMaidLevel() {
//		return ExperienceUtil.getLevelFromExp(maidExperience);
//	}

//	/**
//	 *  現在経験値を取得
//	 * @return
//	 */
//	public float getMaidExperience() {
//		if (maidExperience<=0) {
//			return 0;
//		}
//		return maidExperience;
//	}

//	/**
//	 * 経験値を追加
//	 * @param value
//	 */
//	public void addMaidExperience(float value) {
//		if (!isContractEX() || getEntityWorld().isRemote) {
//			return;
//		}
//
//		int currentLevel = getMaidLevel();
//		if (maidExperience > 0) {
//			value *= getExpBooster();
//		}
//		maidExperience += value;
//
//		// レベルが下がってしまう場合はそれ以上引かない
//		if (maidExperience < ExperienceUtil.getRequiredExpToLevel(currentLevel)) {
//			maidExperience = ExperienceUtil.getRequiredExpToLevel(currentLevel);
//		}
//
//		// 最大レベル
//		if (maidExperience > ExperienceUtil.getRequiredExpToLevel(ExperienceUtil.EXP_FUNCTION_MAX)) {
//			maidExperience = ExperienceUtil.getRequiredExpToLevel(ExperienceUtil.EXP_FUNCTION_MAX);
//		}
//
//		dataManager.set(EntityLittleMaid.dataWatch_MaidExpValue, maidExperience);
//		boolean flag = false;
//		for (;maidExperience >= ExperienceUtil.getRequiredExpToLevel(currentLevel+1); currentLevel++) {
//			// 一度に複数レベルアップした場合にその分だけ呼ぶ
//			if (!flag) {
//				playSound("random.levelup");
//				flag = true;
//			}
//			getExperienceHandler().onLevelUp(currentLevel+1);
//			VEventBus.instance.post(new EventLMRE.MaidLevelUpEvent(this, getMaidLevel()));
//		}
//	}
	
	/**
	 * 経験値を追加
	 * @param value
	 */
	public void addMaidExperience(float value) {
		
		this.getExperienceHandler().addMaidExperience(value);
		
		//同期処理
		if (!this.world.isRemote) {
			dataManager.set(EntityLittleMaid.dataWatch_MaidExpValue, this.getExperienceHandler().getMaidExperience());
		}
		
	}

	/**
	 * 取得経験値による操作を定義するExperienceHandlerを取得
	 * @return
	 */
	public LMExperienceController getExperienceHandler() {
		return experienceHandler;
	}

	public void setExperienceHandler(LMExperienceController handler) {
		if (handler == null) {
			throw new NullPointerException("ExperienceHandler cannot be null!");
		}
		experienceHandler = handler;
	}

//	/**
//	 * 経験値ブーストを取得
//	 */
//	public int getExpBooster() {
//		return gainExpBoost;
//	}

//	/**
//	 * 経験値ブーストを設定
//	 * @param v ブースト値(0以上)
//	 */
//	public void setExpBooster(int v) {
//		if (v < 1) {
//			v = 1;
//		}
//		if (v > ExperienceUtil.getBoosterLimit(getMaidLevel())) {
//			v = ExperienceUtil.getBoosterLimit(getMaidLevel());
//		}
//		gainExpBoost = v;
//	}

	/**
	 * サーバーへテクスチャパックのインデックスを送る。
	 * クライアント側の処理
	 */
//	public boolean sendTextureToServer() {
//		// 16bitあればテクスチャパックの数にたりんべ
////		MMM_TextureManager.instance.postSetTexturePack(this, textureData.getColor(), textureData.getTextureBox());
//		return true;
//	}

//	private boolean checkedTextureUpdate = false;

	/**
	 * テクスチャパックの更新を確認
	 * @return
	 */
//	public boolean updateTexturePack() {
//		/*
//		boolean lflag = false;
//		int ltexture = dataManager.getWatchableObjectInt(dataWatch_Texture);
//		int larmor = (ltexture >>> 16) & 0xffff;
//		ltexture &= 0xffff;
//		if (textureData.textureIndex[0] != ltexture) {
//			textureData.textureIndex[0] = ltexture;
//			lflag = true;
//		}
//		if (textureData.textureIndex[1] != larmor) {
//			textureData.textureIndex[1] = larmor;
//			lflag = true;
//		}
//		if (lflag) {
//			MMM_TextureManager.instance.postGetTexturePack(this, textureData.getTextureIndex());
//		}
//		return lflag;
//		*/
//		// TODO 移行準備:テクスチャ設定
//		if(!checkedTextureUpdate){
//			checkedTextureUpdate = true;
//		}
//		return false;
//	}

	
	public byte getColor() {
//		return textureData.getColor();
		return dataManager.get(EntityLittleMaid.dataWatch_Color);
	}

	public void setColor(byte index) {
		//modelConfigCompound.setColor(index);
		dataManager.set(EntityLittleMaid.dataWatch_Color, index);
	}

	public boolean updateMaidColor() {
		// 同一性のチェック
		byte lc = getColor();
		if (modelConfigCompound.getColor() != lc) {
			modelConfigCompound.setColor(lc);
			return true;
		}
		return false;
	}

	/**
	 * 紐の持ち主
	 */
	public void updateGotcha() {
		int lid = dataManager.get(EntityLittleMaid.dataWatch_Gotcha);
		if (lid == 0) {
			mstatgotcha = null;
			return;
		}
		if (mstatgotcha != null && mstatgotcha.getEntityId() == lid) {
			return;
		}
		for (int li = 0; li < getEntityWorld().loadedEntityList.size(); li++) {
			if (((Entity)getEntityWorld().loadedEntityList.get(li)).getEntityId() == lid) {
				mstatgotcha = (Entity)getEntityWorld().loadedEntityList.get(li);
				break;
			}
		}
	}

	public void setGotcha(int pEntityID) {
		dataManager.set(EntityLittleMaid.dataWatch_Gotcha, Integer.valueOf(pEntityID));
	}
	public void setGotcha(Entity pEntity) {
		setGotcha(pEntity == null ? 0 : pEntity.getEntityId());
	}


	/**
	 * 弓構えを更新
	 */
	public void updateAimebow() {
		boolean lflag = (maidAvatar != null && getAvatarIF().isUsingItemLittleMaid()) || mstatAimeBow;
		setMaidFlags(lflag, dataWatch_Flags_Aimebow);
	}

	public boolean isAimebow() {
		return (dataManager.get(EntityLittleMaid.dataWatch_Flags) & dataWatch_Flags_Aimebow) > 0;
	}


	/**
	 * 各種フラグのアップデート
	 */
	public void updateMaidFlagsClient() {
		int li = dataManager.get(EntityLittleMaid.dataWatch_Flags);
		maidFreedom = (li & dataWatch_Flags_Freedom) > 0;
		maidTracer = (li & dataWatch_Flags_Tracer) > 0;
		maidWait = (li & dataWatch_Flags_Wait) > 0;
		mstatAimeBow = (li & dataWatch_Flags_Aimebow) > 0;
		mstatLookSuger = (li & dataWatch_Flags_LooksSugar) > 0;
		mstatBloodsuck = (li & dataWatch_Flags_Bloodsuck) > 0;
		looksWithInterest = (li & dataWatch_Flags_looksWithInterest) > 0;
		looksWithInterestAXIS = (li & dataWatch_Flags_looksWithInterestAXIS) > 0;
		maidOverDriveTime.updateClient((li & dataWatch_Flags_OverDrive) > 0);
		workingCount.updateClient((li & dataWatch_Flags_Working) > 0);
		registerTick.updateClient((li & dataWatch_Flags_Register) > 0);
	}

	/**
	 * フラグ群に値をセット。
	 */
	public void setMaidFlags(boolean pFlag, int pFlagvalue) {
		int li = dataManager.get(EntityLittleMaid.dataWatch_Flags);
		li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
		dataManager.set(EntityLittleMaid.dataWatch_Flags, Integer.valueOf(li));
	}

	/**
	 * 指定されたフラグを獲得
	 */
	public boolean getMaidFlags(int pFlagvalue) {
		return (dataManager.get(EntityLittleMaid.dataWatch_Flags) & pFlagvalue) > 0;
	}

	/**
	 *  利き腕の設定
	 */
	public void setDominantArm(int pindex) {
		if (mstatSwingStatus.length <= pindex) return;
		if (maidDominantArm == pindex) return;
		for (SwingStatus lss : mstatSwingStatus) {
			lss.index = lss.lastIndex = -1;
		}
		maidDominantArm = pindex;
		dataManager.set(EntityLittleMaid.dataWatch_DominamtArm, maidDominantArm);
		LittleMaidReengaged.Debug("Change Dominant.");
	}

	@Override
	public void setHomePosAndDistance(BlockPos par1, int par4) {
		homeWorld = dimension;
		super.setHomePosAndDistance(par1, par4);
	}

//	public void setTexturePackName(TextureBox[] pTextureBox) {
//		// Client
//		modelConfigCompound.setTexturePackName(pTextureBox);
//		setTextureNames();
//		LittleMaidReengaged.Debug("ID:%d, TextureModel:%s", getEntityId(), modelConfigCompound.getTextureName(0));
//		// モデルの初期化
//		((TextureBox)modelConfigCompound.getTextureBoxLittleMaid()).models[0].setCapsValue(IModelCaps.caps_changeModel, maidCaps);
//		// スタビの付け替え
////		for (Entry<String, MMM_EquippedStabilizer> le : pEntity.maidStabilizer.entrySet()) {
////			if (le.getValue() != null) {
////				le.getValue().updateEquippedPoint(pEntity.textureModel0);
////			}
////		}
//	}

	/**
	 * Client用
	 */
//	public void setTextureNames() {
//		modelConfigCompound.setTextureNames();
//		if (getEntityWorld().isRemote) {
//			dataManager.set(EntityLittleMaid.dataWatch_texture_LittleMaid, modelConfigCompound.getTextureName(0));
//			dataManager.set(EntityLittleMaid.dataWatch_texture_Armor, modelConfigCompound.getTextureName(1));
//		}
//	}

	/**
	 * 次のテクスチャを設定
	 * @param pTargetTexture
	 */
	@SideOnly(Side.CLIENT)
	public void setNextTexturePackege() {
		
		modelConfigCompound.setNextTexturePackege();
		
		this.syncModelNamesToServer();
	}
	
	/**
	 * 次のテクスチャを設定
	 * @param pTargetTexture
	 */
	@SideOnly(Side.CLIENT)
	public void setNextTextureArmorPackege() {
		
		modelConfigCompound.setNextTextureArmorPackege();
		
		this.syncModelNamesToServer();
	}

	/**
	 * 前のテクスチャを設定
	 * @param pTargetTexture
	 */
	@SideOnly(Side.CLIENT)
	public void setPrevTexturePackege() {
		
		modelConfigCompound.setPrevTexturePackege();
		
		this.syncModelNamesToServer();
	}
	
	/**
	 * 前のテクスチャを設定
	 * @param pTargetTexture
	 */
	@SideOnly(Side.CLIENT)
	public void setPrevTextureArmorPackege() {
		
		modelConfigCompound.setPrevTextureArmorPackege();
		
		this.syncModelNamesToServer();
	}


	// textureEntity
//	@Override
//	public void setTextureBox(TextureBoxBase[] pTextureBox) {
//		modelConfigCompound.setTextureBox(pTextureBox);
//	}

//	public String getModelNameMain() {
//		return dataManager.get(EntityLittleMaid.dataWatch_texture_LittleMaid);
//	}

//	public String getModelNameArmor() {
//		return dataManager.get(EntityLittleMaid.dataWatch_texture_Armor);
//	}
	
//	public void setTextureNameMain(String modelNameMain) {
//		dataManager.set(EntityLittleMaid.dataWatch_texture_LittleMaid, modelNameMain);
//		refreshModels();
//	}

//	public void setTextureNameArmor(String modelNameArmor) {
//		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor, modelNameArmor);
//		refreshModels();
//	}

	/**
	 * メイドさんのモデル情報をリフレッシュする
	 */
	protected void refreshModels() {
		this.modelConfigCompound.refreshModels(
				dataManager.get(EntityLittleMaid.dataWatch_texture_LittleMaid), 
				dataManager.get(EntityLittleMaid.dataWatch_Color), 
				dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_head), 
				dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_chest), 
				dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_legs), 
				dataManager.get(EntityLittleMaid.dataWatch_texture_Armor_feet), 
				this.isContract());
	}

//	private TextureBoxBase modelBoxAutoSelect(String pName) {
//		//return getEntityWorld().isRemote ? ModelManager.instance.getTextureBox(pName) : ModelManager.instance.getTextureBoxServer(pName);
//		return ModelManager.instance.getTextureBox(pName);
//	}

//	@Override
//	public TextureBoxBase[] getTextureBox() {
//		return modelConfigCompound.getTextureBox();
//	}

//	@Override
//	public void setTextures(int pIndex, ResourceLocation[] pNames) {
//		modelConfigCompound.setTextures(pIndex, pNames);
//	}

//	@Override
//	public ResourceLocation[] getTextures(int pIndex) {
//		ResourceLocation[] r = modelConfigCompound.getTextures(pIndex);
//		return r;
//	}

	@Override
	public ModelConfigCompound getModelConfigCompound() {
		return modelConfigCompound;
	}

	/**
	 * Can maid-model and texture be changed?
	 * NOTICE: modelChangeable will not be synchronized in default.
	 * To change this dynamically, you need to use packets.
	 */
	public boolean canChangeModel() {
		return true;
	}

	// Tile関係

	/**
	 * 使っているTileかどうか判定して返す。
	 */
	public boolean isUsingTile(TileEntity pTile) {
		if (this.jobController.isActiveModeClass()) {
			return this.jobController.getActiveModeClass().isUsingTile(pTile);
		}
		for (int li = 0; li < maidTiles.length; li++) {
			if (maidTiles[li] != null &&
					pTile.getPos().getX() == maidTiles[li][0] &&
					pTile.getPos().getY() == maidTiles[li][1] &&
					pTile.getPos().getZ() == maidTiles[li][2]) {
				return true;
			}
		}
		return false;
	}

	public boolean isEqualTile() {
		return getEntityWorld().getTileEntity(new BlockPos(maidTile[0], maidTile[1], maidTile[2])) == maidTileEntity;
	}

	public boolean isTilePos() {
		return maidTileEntity != null;
	}
	public boolean isTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
			return maidTiles[pIndex] != null;
		}
		return false;
	}

	/**
	 * ローカル変数にTileの位置を入れる。
	 */
	public boolean getTilePos(int pIndex) {
		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
			maidTile[0] = maidTiles[pIndex][0];
			maidTile[1] = maidTiles[pIndex][1];
			maidTile[2] = maidTiles[pIndex][2];
			return true;
		}
		return false;
	}

	public BlockPos getCurrentTilePos() {
		return new BlockPos(maidTile[0], maidTile[1], maidTile[2]);
	}

	public void setTilePos(int pX, int pY, int pZ) {
		maidTile[0] = pX;
		maidTile[1] = pY;
		maidTile[2] = pZ;
	}
	public void setTilePos(TileEntity pEntity) {
		maidTile[0] = pEntity.getPos().getX();
		maidTile[1] = pEntity.getPos().getY();
		maidTile[2] = pEntity.getPos().getZ();
		maidTileEntity = pEntity;
	}
	public void setTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
			if (maidTiles[pIndex] == null) {
				maidTiles[pIndex] = new int[3];
			}
			maidTiles[pIndex][0] = maidTile[0];
			maidTiles[pIndex][1] = maidTile[1];
			maidTiles[pIndex][2] = maidTile[2];
		}
	}
//	public void setTilePos(int pIndex, int pX, int pY, int pZ) {
//		if (pIndex < maidTiles.length) {
//			if (maidTiles[pIndex] == null) {
//				maidTiles[pIndex] = new int[3];
//			}
//			maidTiles[pIndex][0] = pX;
//			maidTiles[pIndex][1] = pY;
//			maidTiles[pIndex][2] = pZ;
//		}
//	}

	public TileEntity getTileEntity() {
		return maidTileEntity = getEntityWorld().getTileEntity(new BlockPos(maidTile[0], maidTile[1], maidTile[2]));
	}
	public TileEntity getTileEntity(int pIndex) {
		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
			TileEntity ltile = getEntityWorld().getTileEntity(new BlockPos(
					maidTiles[pIndex][0], maidTiles[pIndex][1], maidTiles[pIndex][2]));
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
		return getDistance(
				maidTile[0] + 0.5D,
				maidTile[1] + 0.5D,
				maidTile[2] + 0.5D);
	}
	public double getDistanceTilePosSq() {
		return getDistanceSq(
				maidTile[0] + 0.5D,
				maidTile[1] + 0.5D,
				maidTile[2] + 0.5D);
	}

	public double getDistanceTilePos(int pIndex) {
		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
			return getDistance(
					maidTiles[pIndex][0] + 0.5D,
					maidTiles[pIndex][1] + 0.5D,
					maidTiles[pIndex][2] + 0.5D);
		}
		return -1D;
	}
	public double getDistanceTilePosSq(int pIndex) {
		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
			return getDistanceSq(
					maidTiles[pIndex][0] + 0.5D,
					maidTiles[pIndex][1] + 0.5D,
					maidTiles[pIndex][2] + 0.5D);
		}
		return -1D;
	}
	public double getDistanceTilePos(TileEntity pTile) {
		if (pTile != null) {
			return getDistance(
					pTile.getPos().getX() + 0.5D,
					pTile.getPos().getY() + 0.5D,
					pTile.getPos().getZ() + 0.5D);
		}
		return -1D;
	}
	public double getDistanceTilePosSq(TileEntity pTile) {
		if (pTile != null) {
			return getDistanceSq(
					pTile.getPos().getX() + 0.5D,
					pTile.getPos().getY() + 0.5D,
					pTile.getPos().getZ() + 0.5D);
		}
		return -1D;
	}

	public void looksTilePos() {
		getLookHelper().setLookPosition(
				maidTile[0] + 0.5D, maidTile[1] + 0.5D, maidTile[2] + 0.5D,
				10F, getVerticalFaceSpeed());
	}
	public void looksTilePos(int pIndex) {
		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
			getLookHelper().setLookPosition(
					maidTiles[pIndex][0] + 0.5D,
					maidTiles[pIndex][1] + 0.5D,
					maidTiles[pIndex][2] + 0.5D,
					10F, getVerticalFaceSpeed());
		}
	}

	public boolean isUsingItem() {
		return dataManager.get(EntityLittleMaid.dataWatch_ItemUse) > 0;
	}

	public boolean isUsingItem(int pIndex) {
		return (dataManager.get(EntityLittleMaid.dataWatch_ItemUse) & (1 << pIndex)) > 0;
	}

	public void setExperienceValue(int val) {
//		experienceValue = val;
	}

	public void setFlag(int par1, boolean par2) {
		super.setFlag(par1, par2);
	}

	//1.8検討
	/*
	public void updateWanderPath()
	{
		super.updateWanderPath();
	}
	*/

	/**
	 * @intarface IModelEntity
	 */
	@Override
	public void setSizeMultiModel(float width, float height)
	{
		this.setSize(width, height);
	}

	public ModeTrigger getModeTrigger() {
		return modeTrigger;
	}
	
	/**
	 * メイドさんの契約期間を強制的にクリアする
	 */
	public void clearMaidContractLimit() {
		maidContractLimit = 0;
	}
	
	/**
	 * お座りモーション判断用
	 */
	private int tickisMotionSitting = 0;
	
	/**
	 * お座りモーション中か判断する
	 * @return
	 */
	public boolean isMotionSitting() {
		int maidWaitMotion = dataManager.get(EntityLittleMaid.dataWatch_WaitMotion);
		if (this.isMaidWait()) {
			if (maidWaitMotion == 1) {
				return true;
			}
		} else if (this.maidFreedom 
				&& EntityMode_Basic.mmode_Escort.equals(this.getMaidModeString())) {
			//自由モードでの判断
			this.tickisMotionSitting += 1;
			int coolTime = 20;
			//自由行動モード
			if (this.motionX == 0.0D && this.motionZ == 0.0D) {
				if (coolTime <= this.tickisMotionSitting) {
					return true;
				}
				return false;
			}
			this.tickisMotionSitting = 0;
		} else {
			this.tickisMotionSitting = 0;
		}
		return false;
	}
	
	/**
	 * お座りモーションテスト実装
	 * isRidingを直接の場合は影響が大きいので
	 * Render用のisRidingを実装
	 * @return
	 */
	public boolean isRidingRender() {
		if (this.clientRidingRender) return true;
		if (this.isMotionSitting()) return true;
		return this.isRiding();
	}
	
	
	/**
	 * 水中で息継ぎできるかの判定
	 * のはず
	 */
	@Override
	public boolean canBreatheUnderwater()
    {
		//水上歩行が有効化されている場合はtrueとする
        return LMRConfig.cfg_test_water_walking;
    }
	
	/**
	 * メイドさんの移動制御処理
	 * 
	 * 水上にいる場合も地上と同じ扱いにする処理を試験的に追加する
	 */
	@Override
    public void travel(float strafe, float vertical, float forward) {
		
		//水上歩行有効化（設定が有効かつ水中判定の場合）
		if (LMRConfig.cfg_test_water_walking && this.inWater) {
			//変数保存
			boolean onGround = this.onGround;
			boolean inWater = this.inWater;
			
			//状態の強制書き換え
			this.onGround = true;
			this.inWater = false;
			
			super.travel(strafe, vertical, forward);

			//状態の変数復元
			this.onGround = onGround;
			this.inWater = inWater;

			//浮力を与える
			this.motionY = 0.05D;
			
		//通常状態
		} else {
			super.travel(strafe, vertical, forward);
		}
	}
	
	
	/**
	 * クライアント側から設定するモーション値
	 */
	protected boolean clientRidingRender = false;
	
	/**
	 * クライアント側による強制設定
	 * 騎乗処理からのみ呼び出される想定
	 */
	@SideOnly(Side.CLIENT)
	public void setClientRidingRender(boolean clientRidingRender) {
		this.clientRidingRender = clientRidingRender;
	}
	
	/**
	 * クライアント側から設定する擬似騎乗処理用
	 */
	private EntityPlayer clinetRidingEntity = null;
	
	/**
	 * クライアント側からのみ擬似騎乗設定を行える
	 */
	@SideOnly(Side.CLIENT)
	public void setClinetRidingEntity(EntityPlayer clinetRidingEntity) {
		this.clinetRidingEntity = clinetRidingEntity;
	}
	
	@Override
	public Entity getRidingEntity() {
		if (clinetRidingEntity != null) return clinetRidingEntity;
        return super.getRidingEntity();
    }
	
	/**
	 * 擬似騎乗の場合はisRidingRenderで制御を行うためfalseを返す
	 * 通常騎乗扱いにすると向きによって体の方向がぶれるため
	 */
	@Override
	public boolean isRiding() {
		if (clinetRidingEntity != null) return false;
		return super.isRiding();
	}
	
	/**
	 * 指定ハンドのアイテムを消費してItemStackをプレイヤーにセットする
	 */
	private void setItemStackPlayerHand(ItemStack setStack, EnumHand hand, EntityPlayer player) {
        
		ItemStack handStack = player.getHeldItem(hand);
		
		//1つ消費
		handStack.shrink(1);
		
		//アイテムセット
		if (handStack.isEmpty()) {
			//空の場合
			player.setHeldItem(hand, setStack);
		} else {
			//在庫がある場合
			if (!player.inventory.addItemStackToInventory(setStack)) {
                player.dropItem(setStack, false);
            }
		}
    }
	
	
	/**
	 * メイドさん固定描画用
	 */
	private EnumMaidMotion maidMotion = EnumMaidMotion.NONE;
	public EnumMaidMotion getMaidMotion() {
		return this.maidMotion;
	}
	public void setMaidMotion(EnumMaidMotion motion) {
		this.maidMotion = motion;
	}
	
	/**
	 * SoundManagerで呼び出すためにアクセスレベルを変更
	 */
	@Override
	public float getSoundVolume()
    {
        return super.getSoundVolume();
    }
	
	/**
	 * EntityLiving.onInitialSpawnがスポーン時に一度のみ呼ばれる処理
	 * 
	 * onSpawnWithEggの処理をこちらに移植しこっちで
	 * テクスチャの設定を行う
	 */
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		
		IEntityLivingData ret = super.onInitialSpawn(difficulty, livingdata);
		
		String littleMaidTexture = LMTextureBoxManager.defaultTextureModelName;
		byte maidColor = (byte) EnumColor.BROWN.getColor();
		
		//デフォルト野良モデルを設定する
		if (!LMRConfig.cfg_isFixedWildMaid) {
			//String littleMaidTexture = ModelManager.instance.getRandomTextureString(rand);
			LMTextureBox littleMaidTextureBox = LMTextureBoxManager.instance.getRandomTexture(rand);
			maidColor = (byte) littleMaidTextureBox.getRandomWildColor(rand).getColor();
			littleMaidTexture = littleMaidTextureBox.getTextureModelName();
		}

		dataManager.set(EntityLittleMaid.dataWatch_texture_LittleMaid, littleMaidTexture);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_head, littleMaidTexture);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_chest, littleMaidTexture);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_legs, littleMaidTexture);
		dataManager.set(EntityLittleMaid.dataWatch_texture_Armor_feet, littleMaidTexture);
		dataManager.set(EntityLittleMaid.dataWatch_Color, maidColor);
		
		if(!isContract()) {
			setMaidMode(EntityMode_Basic.mmode_Wild);
			//onSpawnWild();
		}
		
		return ret;
		
	}
	
	
	
}
