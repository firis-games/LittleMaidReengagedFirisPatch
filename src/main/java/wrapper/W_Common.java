package wrapper;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.passive.EntityTameable;
import com.mojang.authlib.GameProfile;


// バージョン差分吸収をおこなう。
// JAVAで#ifdef って使えないの？

public class W_Common
{
	private static final W_ICommon instance = getInstance();
	
	private static W_ICommon getInstance()
	{
		/*
		final String VER = Loader.instance().getMCVersionString();
		if(VER.endsWith("1.7.2"))
		{
			return new wrapper.mc172.W_CCommon();
		}
		else if(VER.endsWith("1.7.10"))
		{
		*/
			return new wrapper.mc18.W_CCommon();
		//}
		//return null;
	}
	
	public static void setOwner(EntityTameable entity, String name)
	{
		instance.setOwner(entity, name);
	}
	public static String getOwnerName(IEntityOwnable entity)
	{
		String ownerName = instance.getOwnerName(entity);

		// メイドがターゲットを探す際に、狼などのテイム可能なモブのオーナー名を取得してチェックする
		// この時オーナー名が NULL だと NULL.isEmpty() と呼び出してしまいクラッシュする。
		// ここにNULLチェックを入れてクラッシュを防ぐ
		// http://forum.minecraftuser.jp/viewtopic.php?f=13&t=23347&p=212078#p212038
		return ownerName!=null? ownerName : "";
	}
	
	public static GameProfile newGameProfile(String UUIDid, String name)
	{
		return instance.newGameProfile(UUIDid, name);
	}
	
	public static void notifyAdmins(ICommandSender sender, ICommand cmd, int p_152374_2_, String s, Object ... p_152374_4_)
	{
		instance.notifyAdmins(sender, cmd, p_152374_2_, s, p_152374_4_);
	}
}
