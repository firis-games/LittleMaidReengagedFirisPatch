package net.blacklab.lmr.client.gui;


import mmmlibx.lib.MMM_GuiMobSelect;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.network.NetworkSync;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.NetworkHelper;
import net.blacklab.lmr.util.Statics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;

public class GuiIFF extends MMM_GuiMobSelect {

	public static final String IFFString[] = {
		"ENEMY", 
		"NEUTRAL", 
		"FRIENDLY" 
	};

	protected EntityLittleMaid target;
	protected EntityPlayer thePlayer;

	public GuiIFF(World world, EntityPlayer player, EntityLittleMaid pEntity) {
		super(world);
		screenTitle = StatCollector.translateToLocal("littleMaidMob.gui.iff.title");
		target = pEntity;
		thePlayer = player;
		
		// IFFをサーバーから取得
		if (!Minecraft.getMinecraft().isSingleplayer()) {
			int li = 0;
			for (String ls : IFF.DefaultIFF.keySet()) {
				byte ldata[] = new byte[5 + ls.length()];
				ldata[0] = Statics.LMN_Server_GetIFFValue;
				NetworkHelper.setIntToPacket(ldata, 1, li);
				NetworkHelper.setStrToPacket(ldata, 5, ls);
				LittleMaidReengaged.Debug("RequestIFF %s(%d)", ls, li);
				NetworkSync.sendToServer(ldata);
				li++;
			}
		}
	}

	@Override
	protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
		boolean lf = false;
		IFF.checkEntityStatic(pName, pEntity, pIndex, entityMap);
		if (pEntity instanceof EntityLivingBase) {
			if (pEntity instanceof EntityLittleMaid) {
				if (pIndex == 0 || pIndex == 1) {
					// 野生種、自分契約者
					lf = true;
				} else {
					// 他人の契約者
				}
			} else if (pEntity instanceof IEntityOwnable) {
				if (pIndex == 0 || pIndex == 1) {
					// 野生種、自分の
					lf = true;
				} else {
					// 他人の家畜
				}
			}
		}
		
		return lf;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		StringTranslate stringtranslate = new StringTranslate();
		
		buttonList.add(new GuiButton(200, width / 2 - 60, height - 40, 120, 20,
				stringtranslate.translateKey("gui.done")));
//		buttonList.add(new GuiButton(201, width / 2 + 10, height - 40, 120, 20,
//				"Trigger Select"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 200) {
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void onGuiClosed() {
		NetworkSync.saveIFF();
		super.onGuiClosed();
	}

	@Override
	public void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity) {
		if (pDoubleClick) {
			int tt = IFF.getIFF(null, pName, pEntity.worldObj);
			tt++;
			if (tt > 2) {
				tt = 0;
			}
			
			if (!mc.isSingleplayer()) {
				// サーバーへ変更値を送る。
				int li = 0;
				for (String ls : IFF.DefaultIFF.keySet()) {
					if (ls.contains(pName)) {
						byte[] ldata = new byte[pName.length() + 6];
						ldata[0] = Statics.LMN_Server_SetIFFValue;
						ldata[1] = (byte) tt;
						NetworkHelper.setIntToPacket(ldata, 2, li);
						NetworkHelper.setStrToPacket(ldata, 6, pName);
						LittleMaidReengaged.Debug("SendIFF %s(%d) = %d", pName, li, tt);
						NetworkSync.sendToServer(ldata);
					}
					li++;
				}
			} else {
				IFF.setIFFValue(null, pName, tt);
			}
			
			Entity player = mc.thePlayer;
			pEntity.worldObj.playSound(player.posX+0.5, player.posY+0.5, player.posZ+0.5, "random.click", 1, 1, false);
		}
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, String pName, Entity pEntity) {
		// 名前と敵味方識別の描画
		int tt = IFF.getIFF(null, pName, pEntity.worldObj);
		int c = 0xffffff;
		switch (tt) {
		case IFF.iff_Friendry:
			c = 0x3fff3f;
			break;
		case IFF.iff_Unknown:
			c = 0xffff00;
			break;
		case IFF.iff_Enemy:
			c = 0xff3f3f;
			break;
		}
		drawString(this.mc.fontRendererObj, GuiIFF.IFFString[tt],
				(width - this.mc.fontRendererObj.getStringWidth(GuiIFF.IFFString[tt])) / 2, pY + 18, c);
		drawString(this.mc.fontRendererObj, pName,
				(width - this.mc.fontRendererObj.getStringWidth(pName)) / 2, pY + 6, 0xffffff);
	}

}
