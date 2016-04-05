package net.blacklab.lmr.network;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.gui.GuiIFF;
import net.blacklab.lmr.client.gui.inventory.GuiMaidInventory;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.inventory.ContainerInventoryLittleMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	/* TODO 暫定、クリック対象を覚えておく
	 メイドさんを右クリックした時に呼ばれる interact はクライアントのほうが早いため、
	 1:クライアント：右クリック対象のメイドを覚える
	 2:サーバ：　　GUIを開く
	 3:クライアント：サーバからの通知で覚えておいたメイドのインベントリGUIを開く
	
	 課題：1～3までの間にクライアントは別のメイドを右クリックする可能性がある?
	 * 
	 */
	public static EntityLittleMaid maidClient = null;
	public static EntityLittleMaid maidServer = null;

	public static final int GUI_ID_INVVENTORY	= 0;
	public static final int GUI_ID_IFF			= 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		Object o = null;
		switch(ID)
		{
			case GUI_ID_INVVENTORY:
				if(maidServer!=null)
				{
					o = new ContainerInventoryLittleMaid(player.inventory, maidServer);
					maidServer = null;
				}
				break;

			case GUI_ID_IFF:
//				o = new LMM_ContainerTriggerSelect(player);
				break;
		}
		return o;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return LittleMaidReengaged.proxy.getClientGuiElement(ID, player, world, x, y, z);
	}
}
