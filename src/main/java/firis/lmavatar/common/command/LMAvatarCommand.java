package firis.lmavatar.common.command;

import java.util.ArrayList;
import java.util.List;

import firis.lmavatar.common.network.NetworkHandler;
import firis.lmavatar.common.network.PacketSendNBTTagCompound;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

/**
 * LMアバター関連のコマンド
 * @author computer
 *
 */
public class LMAvatarCommand extends CommandBase {

	/**
	 * コマンド名
	 */
	@Override
	public String getName() {
		return "lmavatar";
	}
	
	/**
	 * コマンドのエイリアス
	 */
	@Override
	public List<String> getAliases() {
		List<String> commandAliases = new ArrayList<>();
		commandAliases.add("lma");
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
		
		//パラメータの簡易チェック
		String param = "";
		if (cmdMode.equals("save") && cmdArgs.size() == 1) {
			param = cmdArgs.get(0);
		} else if (cmdMode.equals("load") && cmdArgs.size() == 1) {
			param = cmdArgs.get(0);
		} else if (cmdMode.equals("list") && cmdArgs.size() <= 1) {
			if (cmdArgs.size() == 1) {
				param = cmdArgs.get(0);
			}
		} else {
			//コマンドエラーと判断sa
			throw new CommandException("commands.lmcommand.error", new Object[] {});
		}
		
		//コマンドを生成して実行する
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("command", cmdMode);
		nbt.setString("param", param);
		//LMRNetwork.sendPacketToPlayer(EnumPacketMode.CLIENT_COMMAND_EXECUTE, 0, nbt, entityplayer);
		NetworkHandler.sendPacketToClientPlayer(PacketSendNBTTagCompound.CLIENT_COMMAND_EXECUTE, nbt, entityplayer);
		
	}
	
}
