package net.firis.lmt.common.manager;

import java.util.HashMap;
import java.util.Map;

import firis.lmlib.api.manager.LMTextureBoxManager;
import firis.lmlib.api.manager.pack.LMTextureBox;
import net.blacklab.lmr.LittleMaidReengaged;
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
	 * クライアント側のみ使う用にする
	 * 
	 */
	private static Map<String, PlayerModelConfigCompound> modelConfigCompoundMap = new HashMap<>();
	
	/**
	 * server側で保持するNBTリスト
	 * 同期のタイミングではインスタンス生成を行わない
	 * パラメータへの初回アクセスのタイミングで生成する
	 */
	protected static Map<String, NBTTagCompound> serverModelNbtMap = new HashMap<>();
	
	/**
	 * Client側で保持するNBTリスト
	 * 同期のタイミングではインスタンス生成を行わない
	 * パラメータへの初回アクセスのタイミングで生成する
	 */
	protected static Map<String, NBTTagCompound> clientModelNbtMap = new HashMap<>();
	
	/**
	 * EntityPlayerに紐づくModelConfigCompoundを取得する
	 * @return
	 */
	public static PlayerModelConfigCompound getModelConfigCompound(EntityPlayer player) {
		
		String key = player.getName();
		
		//存在していなければ初期化して作成する
		if (!modelConfigCompoundMap.containsKey(key)) {
			modelConfigCompoundMap.put(key, createModelConfigCompound(player));
		}
		PlayerModelConfigCompound modelConfig = modelConfigCompoundMap.get(key);
		
		//プレイヤーを最新化
		modelConfig.setPlayer(player);
		
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
		
		clientModelNbtMap.put(getClientPlayer().getName(), clientNBT);
		
		//LMRNetwork.sendPacketToServer(EnumPacketMode.SERVER_SYNC_CLIENT_LMAVATAR, -1, clientNBT);
		SyncPlayerModelClient.syncModel();
	}
	
	/**
	 * 設定から作成する
	 */
	private static PlayerModelConfigCompound createModelConfigCompound(EntityPlayer player) {
		
		PlayerModelConfigCompound playerModelConfig = new PlayerModelConfigCompound(player, new PlayerModelCaps(player));
		playerModelConfig = refreshConfig(playerModelConfig);
		
		String key = player.getName();
		
		//Ownerの場合
		if (player == getClientPlayer()) {
			//初回はローカル情報優先
			boolean isSend = false;
			if (!clientModelNbtMap.containsKey(key)) {
				//キーがない場合は同期
				isSend = true;
			} else {
				NBTTagCompound clientNBT = playerModelConfig.serializeToNBT(new NBTTagCompound());
				if (!clientNBT.equals(clientModelNbtMap.get(key))) {
					clientModelNbtMap.put(key, clientNBT);
					isSend = true;
				}
			}
			//同期する
			if (isSend) {
				SyncPlayerModelClient.syncModel();
			}
		} else if (clientModelNbtMap.containsKey(key)) {
			//サーバーから情報が来てる場合は適応する
			playerModelConfig.deserializeFromNBT(clientModelNbtMap.get(key));
		}
		return playerModelConfig;
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
	 *　・このタイミングではクライアントのPlayerが生成されていない可能性があるので何もしない
	 */
	public static void receiveLMAvatarDataFromServer(NBTTagCompound tagCompound) {

//		EntityPlayer ownerPlayer = getClientPlayer();
		NBTTagList tagList = tagCompound.getTagList("avatar", 10);
		
		//サーバーと同期するかの判断用
//		boolean isServerSync = false;
//		PlayerModelConfigCompound modelConfig = getModelConfigCompound(ownerPlayer);
//		NBTTagCompound clientNBT = modelConfig.serializeToNBT(new NBTTagCompound());
		
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound nbt = tagList.getCompoundTagAt(i);
			String key = nbt.getString("name");
			
			//クライアントのNBTキャッシュへ保存する
			clientModelNbtMap.put(key, nbt);
			
			////OwnerPlayerの場合
			//if (uuid.equals(getClientPlayer().getUniqueID())) {
			//	//サーバーのデフォルトと差異がある場合は同期する
			//	//差異がある場合はクライアントを優先する
			//	if (!clientNBT.equals(nbt)) {
			//		clientModelNbtMap.put(uuid, clientNBT);
			//		isServerSync = true;
			//	}
			////その他ユーザーかつインスタンス生成済みの場合
			//} else
			//既にオブジェクトが存在する場合は上書きする
			if (modelConfigCompoundMap.containsKey(key)){
				//その他のユーザーの場合
				PlayerModelConfigCompound playerModel = modelConfigCompoundMap.get(key);
				NBTTagCompound playerNbt = playerModel.serializeToNBT(new NBTTagCompound());
				if (!playerNbt.equals(nbt)) {
					//サーバーから来た情報が更新されている場合
					playerModel.deserializeFromNBT(nbt);
				}
			}
		}
		
//		if (isServerSync) {
//			//サーバー側へ再送信
//			//LMRNetwork.sendPacketToServer(EnumPacketMode.SERVER_SYNC_CLIENT_LMAVATAR, -1, clientNBT);
//			SyncPlayerModelClient.syncModel();
//		}
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
		
		String key = tagCompound.getString("name");
		
		if (!serverModelNbtMap.containsKey(key)) {
			serverModelNbtMap.put(key, tagCompound);
			isClientSync = true;
		} else {
			NBTTagCompound playerNbt = serverModelNbtMap.get(key);
			if (!playerNbt.equals(tagCompound)) {
				serverModelNbtMap.put(key, tagCompound);
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
			SyncPlayerModelServer.syncModel(key);
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
		
		String playerName = event.player.getName();
		
		//ログインのタイミングでサーバー側のNBTを作成する
		NBTTagCompound modelConfigNbt = PlayerModelConfigCompound.createDefaultNBT(event.player.getUniqueID());
		serverModelNbtMap.put(playerName, modelConfigNbt);

		//サーバー上で管理しているNBTリストをクライアントへ送る
		NBTTagCompound send = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for (String key : serverModelNbtMap.keySet()) {
			tagList.appendTag(serverModelNbtMap.get(key));
		}
		send.setTag("avatar", tagList);
		
		//送信
		//LMRNetwork.sendPacketToPlayer(EnumPacketMode.CLIENT_SYNC_SERVER_LMAVATAR, 0, send, event.player);
		SyncPlayerModelServer.syncModel(playerName);
	}
}
