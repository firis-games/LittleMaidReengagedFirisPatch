package net.blacklab.lmc.common.command;

import java.util.ArrayList;
import java.util.List;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

/**
 * メイドさん関連のコマンド
 * @author computer
 *
 */
public class LMCommand extends CommandBase {

	/**
	 * コマンド名
	 */
	@Override
	public String getName() {
		return "lmcommand";
	}
	
	/**
	 * コマンドのエイリアス
	 */
	@Override
	public List<String> getAliases() {
		List<String> commandAliases = new ArrayList<>();
		commandAliases.add("lmc");
		commandAliases.add("maid");
        return commandAliases;
    }

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.lmcommand.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		//プレイヤーが実行時のみ処理を行う
		EntityPlayer entityplayer = getCommandSenderAsPlayer(sender);

		//引数なし
		if (args.length == 0) {
			//エラーの表示
            throw new CommandException("commands.lmcommand.error", new Object[] {});
		}
		
		//パラメータ分解
		String cmdMode = args[0];
		List<String> cmdArgs = new ArrayList<>();
		for (int i = 1; i < args.length; i++) {
			cmdArgs.add(args[i]);
		}
		
		//コマンドを実行する
		boolean isCommand = false;
		
		//call
		//メイドさんを呼び寄せる
		if (!isCommand) isCommand = commandCall(server, sender, entityplayer, cmdMode, cmdArgs);

		//list
		//メイドさんのリストを表示する
		if (!isCommand) isCommand = commandList(server, sender, entityplayer, cmdMode, cmdArgs);
		
		//コマンドエラー
		if (!isCommand) {
			throw new CommandException("commands.lmcommand.error", new Object[] {});
		}
		
	}
	
	/**
	 * メイドさん呼び出しコマンド
	 * @return
	 */
	public boolean commandCall(MinecraftServer server, ICommandSender sender, EntityPlayer player, String commandMode, List<String> commandArgs) {
		
		if (!"call".equals(commandMode)) {
			return false;
		}
		//引数が1以外の場合はエラー
		if (commandArgs.size() != 1) {
			return false;
		}
		
		//引数チェック
		String callType = commandArgs.get(0);
		boolean isCallTypeAll = "all".equals(callType);
		
		List<Entity> entityList = server.getEntityWorld().loadedEntityList;
		
		boolean isSearchMaid = false;
		
		//メイドさんを探す
		for (Entity entity : entityList) {
			if (entity instanceof EntityLittleMaid) {
				//メイドさんの場合
				EntityLittleMaid maid = (EntityLittleMaid) entity;
				//メイドさんのご主人様が自分であること
				if (maid.isContract() && maid.isMaidContractOwner(player)) {
					//メイドさんの名前が一致 or all
					if (isCallTypeAll || callType.equals(maid.getName())) {
						isSearchMaid = true;
						//メイドさんをワープ
						maid.setPosition(sender.getPosition().getX(), 
								sender.getPosition().getY(), 
								sender.getPosition().getZ());
					}
				}
			}
		}
		
		//呼び出し成否のメッセージ
		if (isSearchMaid) {
			notifyCommandListener(sender, this, "commands.lmcommand.call.success", new Object[] {});
		} else {
			if (isCallTypeAll) {
				//全員
				notifyCommandListener(sender, this, "commands.lmcommand.call.failure", new Object[] {});
			} else {
				//個人指名
				notifyCommandListener(sender, this, "commands.lmcommand.call.failure.maid", new Object[] {callType});				
			}
		}
		
		return true;
	}
	
	/**
	 * メイドさん呼び出しコマンド
	 * @return
	 */
	public boolean commandList(MinecraftServer server, ICommandSender sender, EntityPlayer player, String commandMode, List<String> commandArgs) {
		
		if (!"list".equals(commandMode)) {
			return false;
		}
		//引数が0以外の場合はエラー
		if (commandArgs.size() != 0) {
			return false;
		}
		
		List<Entity> entityList = server.getEntityWorld().loadedEntityList;

		boolean isSearchMaid = false;
		
		//メイドさんを探す
		for (Entity entity : entityList) {
			if (entity instanceof EntityLittleMaid) {

				//メイドさんの場合
				EntityLittleMaid maid = (EntityLittleMaid) entity;
				//メイドさんのご主人様が自分であること
				if (maid.isContract() && maid.isMaidContractOwner(player)) {
					
					isSearchMaid = true;
					
					//チャット欄に表示
					String msg = maid.getName();
					msg += String.format("[%.2f, %.2f, %.2f]", maid.posX, maid.posY, maid.posZ);
					
					//sender.sendMessage(new TextComponentString(msg));
					notifyCommandListener(sender, this, msg, new Object[] {});
				}
			}	
		}
		
		//メイドさんがいない場合のメッセージ
		if (!isSearchMaid) {
			notifyCommandListener(sender, this, "commands.lmcommand.list.failure", new Object[] {});
		}
		
		return true;
	}

}
