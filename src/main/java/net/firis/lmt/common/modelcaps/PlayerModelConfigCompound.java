package net.firis.lmt.common.modelcaps;

import java.util.UUID;

import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.common.data.IModelCapsData;
import firis.lmlib.common.data.ModelConfigCompoundBase;
import net.firis.lmt.config.FirisConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

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
	 * Player
	 */
	private EntityPlayer player;
	public void setPlayer(EntityPlayer player) {
		this.player = player;
		this.entityCaps = new PlayerModelCaps(player);
	}
	
	/**
	 * コンストラクタ
	 * @param entity
	 * @param caps
	 */
	public PlayerModelConfigCompound(EntityPlayer entity, IModelCapsData caps) {
		super(entity, caps);
		
		this.player = entity;
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
	
	/**
	 * NBTへ変換する
	 */
	public NBTTagCompound serializeToNBT(NBTTagCompound nbt) {
		
		String maid = this.getTextureBoxLittleMaid().getTextureModelName();
		String armorHead = this.getTextureBoxArmor(EntityEquipmentSlot.HEAD).getTextureModelName();
		String armorChest = this.getTextureBoxArmor(EntityEquipmentSlot.CHEST).getTextureModelName();
		String armorLegs = this.getTextureBoxArmor(EntityEquipmentSlot.LEGS).getTextureModelName();
		String armorFeet = this.getTextureBoxArmor(EntityEquipmentSlot.FEET).getTextureModelName();
		
		//必要な情報のみNBT化
		nbt.setUniqueId("uuid", this.player.getUniqueID());
		nbt.setString("name", this.player.getName());
		nbt.setString("maid", maid);
		nbt.setInteger("color", this.color);
		nbt.setString("head", armorHead);
		nbt.setString("chest", armorChest);
		nbt.setString("legs", armorLegs);
		nbt.setString("feet", armorFeet);
		
		//モーション系
		nbt.setBoolean("action", this.lmAvatarAction);

		return nbt;
	}
	
	/**
	 * NBTから復元する
	 */
	public void deserializeFromNBT(NBTTagCompound nbt) {
		
		String maid = nbt.getString("maid");
		String armorHead = nbt.getString("head");
		String armorChest = nbt.getString("chest");
		String armorLegs = nbt.getString("legs");
		String armorFeet = nbt.getString("feet");
		
		Integer color = nbt.getInteger("color");
		
		//展開
		this.setTextureBoxLittleMaid(LMLibraryAPI.instance().getTextureManager().getLMTextureBox(maid));
		this.setTextureBoxArmor(EntityEquipmentSlot.HEAD, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorHead));
		this.setTextureBoxArmor(EntityEquipmentSlot.CHEST, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorChest));
		this.setTextureBoxArmor(EntityEquipmentSlot.LEGS, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorLegs));
		this.setTextureBoxArmor(EntityEquipmentSlot.FEET, LMLibraryAPI.instance().getTextureManager().getLMTextureBox(armorFeet));
		this.setColor(color);
		
		//モーション系
		this.lmAvatarAction = nbt.getBoolean("action");
		
	}
	
	/**
	 * デフォルト状態のNBTを作成する
	 * @return
	 */
	public static NBTTagCompound createDefaultNBT(UUID uuid) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		LMTextureBox maidBox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(FirisConfig.cfg_maid_model);
		LMTextureBox headBox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(FirisConfig.cfg_armor_model_head);
		LMTextureBox bodyBox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(FirisConfig.cfg_armor_model_body);
		LMTextureBox legBox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(FirisConfig.cfg_armor_model_leg);
		LMTextureBox bootsBox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(FirisConfig.cfg_armor_model_boots);
		
		//必要な情報のみNBT化
		nbt.setUniqueId("uuid", uuid);
		nbt.setString("maid", maidBox.getTextureModelName());
		nbt.setInteger("color", FirisConfig.cfg_maid_color);
		nbt.setString("head", headBox.getTextureModelName());
		nbt.setString("chest", bodyBox.getTextureModelName());
		nbt.setString("legs", legBox.getTextureModelName());
		nbt.setString("feet", bootsBox.getTextureModelName());
		
		//モーション系
		nbt.setBoolean("action", false);

		return nbt;
	}
	
	/**
	 * インナー防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureInnerArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = player.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getTextureInnerArmor(stack);
		
	}
	
	/**
	 * インナー発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureInnerArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = player.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getLightTextureInnerArmor(stack);
	}
	
	/**
	 * アウター防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureOuterArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = player.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getTextureOuterArmor(stack);
	}
	
	/**
	 * アウター発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureOuterArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = player.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getLightTextureOuterArmor(stack);
	}
}
