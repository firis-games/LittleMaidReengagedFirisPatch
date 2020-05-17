package net.firis.lmt.common.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.manager.LMTextureBoxManager;
import net.blacklab.lmr.util.manager.pack.LMTextureBox;
import net.firis.lmt.common.modelcaps.PlayerModelCaps;
import net.firis.lmt.common.modelcaps.PlayerModelConfigCompound;
import net.firis.lmt.config.FirisConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * LMアバターの管理クラス
 * @author firis-games
 *
 */
public class PlayerModelManager {

	/**
	 * 内部保持用
	 * 
	 */
	private static Map<UUID, PlayerModelConfigCompound> modelConfigCompoundMap = new HashMap<>();
	
	/**
	 * server側で保持するNBTリスト
	 * 同期のタイミングではインスタンス生成を行わない
	 * パラメータへの初回アクセスのタイミングで生成する
	 */
	protected static Map<UUID, NBTTagCompound> serverModelNbtMap = new HashMap<>();
	
	/**
	 * Client側で保持するNBTリスト
	 * 同期のタイミングではインスタンス生成を行わない
	 * パラメータへの初回アクセスのタイミングで生成する
	 */
	protected static Map<UUID, NBTTagCompound> clientModelNbtMap = new HashMap<>();
	
	/**
	 * EntityPlayerに紐づくModelConfigCompoundを取得する
	 * @return
	 */
	public static PlayerModelConfigCompound getModelConfigCompound(EntityPlayer player) {
		UUID uuid = player.getUniqueID();
		//存在していなければ初期化して作成する
		if (!modelConfigCompoundMap.containsKey(uuid)) {
			modelConfigCompoundMap.put(uuid, createModelConfigCompound(player));
		}
		PlayerModelConfigCompound modelConfig = modelConfigCompoundMap.get(uuid);
		modelConfig.player = player;
		return modelConfig; 
	}
	
	/**
	 * 設定ファイルの内容を反映する
	 * 
	 * 設定を変更した際にServerへ情報を送る
	 */
	public static void syncConfig() {
		
		PlayerModelConfigCompound modelConfig = getModelConfigCompound(getClientPlayer());
		
		//設定値へ更新
		refreshConfig(modelConfig);
		
		//変更値をサーバーへ送信
		NBTTagCompound clientNBT = modelConfig.serializeToNBT(new NBTTagCompound());
		
		clientModelNbtMap.put(getClientPlayer().getUniqueID(), clientNBT);
		
		//LMRNetwork.sendPacketToServer(EnumPacketMode.SERVER_SYNC_CLIENT_LMAVATAR, -1, clientNBT);
		SyncPlayerModelClient.syncModel();
	}
	
	/**
	 * 設定から作成する
	 */
	private static PlayerModelConfigCompound createModelConfigCompound(EntityPlayer player) {
		PlayerModelConfigCompound playerModelConfig = new PlayerModelConfigCompound(player, new PlayerModelCaps(player));
		return refreshConfig(playerModelConfig);
	}
	
	/**
	 * 設定内容をオブジェクトに反映する
	 * @param playerModelConfig
	 */
	private static PlayerModelConfigCompound refreshConfig(PlayerModelConfigCompound playerModelConfig) {
		//モデル情報を設定する
		LMTextureBox maidBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_maid_model);
		LMTextureBox headBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_head);
		LMTextureBox bodyBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_body);
		LMTextureBox legBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_leg);
		LMTextureBox bootsBox = LMTextureBoxManager.instance.getLMTextureBox(FirisConfig.cfg_armor_model_boots);
		
		playerModelConfig.setTextureBoxLittleMaid(maidBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.HEAD, headBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.CHEST, bodyBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.LEGS, legBox);
		playerModelConfig.setTextureBoxArmor(EntityEquipmentSlot.FEET, bootsBox);
		
		playerModelConfig.setColor(FirisConfig.cfg_maid_color);
		playerModelConfig.setContract(true);
		
		playerModelConfig.setEnableLMAvatar(FirisConfig.cfg_enable_lmavatar);
		
		return playerModelConfig;
	}
	
	/**
	 * サーバーから送信されたLMアバターの情報を受け取って反映する
	 * 
	 * 呼ばれるパターン
	 *　・ログイン時
	 *　・クライアント側でLMアバター切り替えたタイミング
	 */
	public static void receiveLMAvatarDataFromServer(NBTTagCompound tagCompound) {

		EntityPlayer ownerPlayer = getClientPlayer();
		
		NBTTagList tagList = tagCompound.getTagList("avatar", 10);
		
		//サーバーと同期するかの判断用
		boolean isServerSync = false;
		PlayerModelConfigCompound modelConfig = getModelConfigCompound(ownerPlayer);
		NBTTagCompound clientNBT = modelConfig.serializeToNBT(new NBTTagCompound());
		
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound nbt = tagList.getCompoundTagAt(i);
			UUID uuid = nbt.getUniqueId("uuid");
			
			//クライアントのNBTキャッシュへ保存する
			clientModelNbtMap.put(uuid, nbt);
			
			//OwnerPlayerの場合
			if (uuid.equals(getClientPlayer().getUniqueID())) {
				//サーバーのデフォルトと差異がある場合は同期する
				//差異がある場合はクライアントを優先する
				if (!clientNBT.equals(nbt)) {
					clientModelNbtMap.put(uuid, clientNBT);
					isServerSync = true;
				}
			//その他ユーザーかつインスタンス生成済みの場合
			} else if (modelConfigCompoundMap.containsKey(uuid)){
				//その他のユーザーの場合
				PlayerModelConfigCompound playerModel = modelConfigCompoundMap.get(uuid);
				NBTTagCompound playerNbt = playerModel.serializeToNBT(new NBTTagCompound());
				if (!playerNbt.equals(nbt)) {
					//サーバーから来た情報が更新されている場合
					playerModel.deserializeFromNBT(nbt);
				}
			}
		}
		
		if (isServerSync) {
			//サーバー側へ再送信
			//LMRNetwork.sendPacketToServer(EnumPacketMode.SERVER_SYNC_CLIENT_LMAVATAR, -1, clientNBT);
			SyncPlayerModelClient.syncModel();
		}
	}
	
	/**
	 * クライアントから送信されたLMアバターの情報を受け取って反映する
	 * 
	 * クライアントから飛んでくる場合は1つのみを想定
	 * 
	 */
	public static void reciveLMAvatarDataFromClient(NBTTagCompound tagCompound) {
		
		//サーバーと同期するかの判断用
		boolean isClientSync = false;
		
		UUID uuid = tagCompound.getUniqueId("uuid");
		
		if (!serverModelNbtMap.containsKey(uuid)) {
			serverModelNbtMap.put(uuid, tagCompound);
			isClientSync = true;
		} else {
			NBTTagCompound playerNbt = serverModelNbtMap.get(uuid);
			if (!playerNbt.equals(tagCompound)) {
				serverModelNbtMap.put(uuid, tagCompound);
				isClientSync = true;
			}
		}
		
		//変更する
		if (isClientSync) {
			//NBTTagCompound send = new NBTTagCompound();
			//NBTTagList tagList = new NBTTagList();
			//tagList.appendTag(tagCompound);
			//send.setTag("avatar", tagList);
			
			//全クライアントへ送信する
			//LMRNetwork.sendPacketToAllPlayer(EnumPacketMode.CLIENT_SYNC_SERVER_LMAVATAR, -1, send);
			SyncPlayerModelServer.syncModel(uuid);
		}
	}
	
	/**
	 * クライアントのEntityPlayerを取得する
	 * 
	 * Minecraft.getMinecraft().playerから直接取得すると
	 * マルチ環境のサーバーで落ちてしまうのでproxy経由で取得する
	 * @return
	 */
	private static EntityPlayer getClientPlayer() {
		return LittleMaidReengaged.proxy.getClientPlayer();
	}
	
	/**
	 * ログイン時にアバターを同期する
	 * アバターの情報をプレイヤーへ送信する
	 */
	@SubscribeEvent
	public void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {
		
		//ログインのタイミングでサーバー側のNBTを作成する
		PlayerModelConfigCompound modelConfig = PlayerModelManager.getModelConfigCompound(event.player);
		PlayerModelManager.serverModelNbtMap.put(event.player.getUniqueID(), modelConfig.serializeToNBT(new NBTTagCompound()));

		//サーバー上で管理しているNBTリストをクライアントへ送る
		NBTTagCompound send = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for (UUID key : PlayerModelManager.serverModelNbtMap.keySet()) {
			tagList.appendTag(PlayerModelManager.serverModelNbtMap.get(key));
		}
		send.setTag("avatar", tagList);
		
		//送信
		//LMRNetwork.sendPacketToPlayer(EnumPacketMode.CLIENT_SYNC_SERVER_LMAVATAR, 0, send, event.player);
		SyncPlayerModelServer.syncModel(event.player.getUniqueID());
	}
}
