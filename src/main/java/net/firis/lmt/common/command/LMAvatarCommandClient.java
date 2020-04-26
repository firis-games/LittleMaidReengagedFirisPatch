package net.firis.lmt.common.command;

import net.firis.lmt.config.FirisConfig;
import net.firis.lmt.config.custom.JConfigLMAvatarManager;
import net.firis.lmt.config.custom.JConfigLMAvatarModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Property;

/**
 * クライアント側でコマンド処理を行う用
 * @author firis-games
 */
public class LMAvatarCommandClient {

	/**
	 * コマンドを実行する
	 */
	public static void execute(String command, String param) {
		
		//各コマンドを実行する
		if ("save".equals(command)) executeSave(param);
		if ("load".equals(command)) executeLoad(param);
		if ("list".equals(command)) executeList(param);
		
	}
	
	/**
	 * LMAvatarの今の状態を保存する
	 * @param param
	 */
	private static void executeSave(String param) {
		
		//クライアント側のPlayer
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		boolean isNew = false;
		//パラメータが存在しない場合
		if (!JConfigLMAvatarManager.lmAvatarModels.containsKey(param)) {
			isNew = true;
		}
		
		JConfigLMAvatarModel configModel = new JConfigLMAvatarModel();
		
		//保存する
		configModel.maidModel = FirisConfig.cfg_maid_model;
		configModel.maidColor = FirisConfig.cfg_maid_color;
		configModel.armorHelmetModel = FirisConfig.cfg_armor_model_head;
		configModel.armorChestplateModel = FirisConfig.cfg_armor_model_body;
		configModel.armorLeggingsModel = FirisConfig.cfg_armor_model_leg;
		configModel.armorBootsModel = FirisConfig.cfg_armor_model_boots;
		
		JConfigLMAvatarManager.save(param, configModel);
		
		//終了後のメッセージ
		if (isNew) {
			//新規
			player.sendMessage(new TextComponentTranslation("commands.lmacommand.save.new", new Object[] {param}));
		} else {
			//更新
			player.sendMessage(new TextComponentTranslation("commands.lmacommand.save.update", new Object[] {param}));
		}
	}
	
	/**
	 * LMAvatarの状態を指定名で更新する
	 * @param param
	 */
	private static void executeLoad(String param) {
		
		//クライアント側のPlayer
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		//パラメータが存在しない場合
		if (!JConfigLMAvatarManager.lmAvatarModels.containsKey(param)) {
			//エラーメッセージ
			player.sendMessage(new TextComponentTranslation("commands.lmacommand.load.error", new Object[] {param}));
			return;
		}
		
		JConfigLMAvatarModel configModel = JConfigLMAvatarManager.lmAvatarModels.get(param);
		
		//メイドモデル名取得
		//Config操作用
		Property propModel = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "01.MaidModel", FirisConfig.cfg_maid_model);
		Property propModelColor = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "02.MaidColorNo", FirisConfig.cfg_maid_color);
		Property propModelArmorHelmet = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "03.ArmorHelmetModel", FirisConfig.cfg_armor_model_head);
		Property propModelArmorChest = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "04.ArmorChestplateModel", FirisConfig.cfg_armor_model_body);
		Property propModelArmorLegg = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "05.ArmorLeggingsModel", FirisConfig.cfg_armor_model_leg);
		Property propModelArmorBoots = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "06.ArmorBootsModel", FirisConfig.cfg_armor_model_boots);
		
		//各パラメータを上書き
		propModel.set(configModel.maidModel);
		propModelColor.set(configModel.maidColor);
		propModelArmorHelmet.set(configModel.armorHelmetModel);
		propModelArmorChest.set(configModel.armorChestplateModel);
		propModelArmorLegg.set(configModel.armorLeggingsModel);
		propModelArmorBoots.set(configModel.armorBootsModel);

		//設定ファイル同期
		FirisConfig.syncConfig();
		
		//メッセージ表示
		player.sendMessage(new TextComponentTranslation("commands.lmacommand.load", new Object[] {param}));
		
	}
	
	/**
	 * リスト表示
	 * @param param
	 */
	private static void executeList(String param) {
		
		//クライアント側のPlayer
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		if (param.equals("")) {
			//空の場合は登録名の一覧を表示する
			for (String key :JConfigLMAvatarManager.lmAvatarModels.lmavatar.keySet()) {
				player.sendMessage(new TextComponentTranslation(key, new Object[] {}));
			}
		} else {
			//指定名
			if (!JConfigLMAvatarManager.lmAvatarModels.containsKey(param)) {
				//エラーメッセージ
				player.sendMessage(new TextComponentTranslation("commands.lmacommand.list.error", new Object[] {param}));
				return;
			}
			
			//指定名の一覧を表示する
			JConfigLMAvatarModel configModel = JConfigLMAvatarManager.lmAvatarModels.get(param);
			player.sendMessage(new TextComponentTranslation("MaidModel:" + configModel.maidModel, new Object[] {}));
			player.sendMessage(new TextComponentTranslation("MaidColor:" + configModel.maidColor.toString(), new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorHelmetModel:" + configModel.armorHelmetModel, new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorChestplateModel:" + configModel.armorChestplateModel, new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorLeggingsModel:" + configModel.armorLeggingsModel, new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorBootsModel:" + configModel.armorBootsModel, new Object[] {}));
		}
	}
}
