package littleMaidMobX;


import mmmlibx.lib.MMM_GuiMobSelect;
import mmmlibx.lib.MMM_Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;

public class LMM_GuiIFF extends MMM_GuiMobSelect {

	public static final String IFFString[] = {
		"ENEMY", 
		"NEUTRAL", 
		"FRIENDLY" 
	};

	protected LMM_EntityLittleMaid target;
	protected EntityPlayer thePlayer;

	public LMM_GuiIFF(World world, EntityPlayer player, LMM_EntityLittleMaid pEntity) {
		super(world);
		screenTitle = StatCollector.translateToLocal("littleMaidMob.gui.iff.title");
		target = pEntity;
		thePlayer = player;
		
		// IFFをサーバーから取得
		if (!Minecraft.getMinecraft().isSingleplayer()) {
			int li = 0;
			for (String ls : LMM_IFF.DefaultIFF.keySet()) {
				byte ldata[] = new byte[5 + ls.length()];
				ldata[0] = LMM_Statics.LMN_Server_GetIFFValue;
				MMM_Helper.setInt(ldata, 1, li);
				MMM_Helper.setStr(ldata, 5, ls);
				LMM_LittleMaidMobNX.Debug("RequestIFF %s(%d)", ls, li);
				LMM_Net.sendToServer(ldata);
				li++;
			}
		}
	}

	@Override
	protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
		boolean lf = false;
		LMM_IFF.checkEntityStatic(pName, pEntity, pIndex, entityMap);
		if (pEntity instanceof EntityLivingBase) {
			if (pEntity instanceof LMM_EntityLittleMaid) {
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
		if (guibutton.id == 201) {
			mc.displayGuiScreen(new LMM_GuiTriggerSelect(thePlayer, this));
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void onGuiClosed() {
		LMM_Net.saveIFF();
		super.onGuiClosed();
	}

	@Override
	public void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity) {
		if (pDoubleClick) {
			int tt = LMM_IFF.getIFF(null, pName, pEntity.worldObj);
			tt++;
			if (tt > 2) {
				tt = 0;
			}
			
			if (!mc.isSingleplayer()) {
				// サーバーへ変更値を送る。
				int li = 0;
				for (String ls : LMM_IFF.DefaultIFF.keySet()) {
					if (ls.contains(pName)) {
						byte[] ldata = new byte[pName.length() + 6];
						ldata[0] = LMM_Statics.LMN_Server_SetIFFValue;
						ldata[1] = (byte) tt;
						MMM_Helper.setInt(ldata, 2, li);
						MMM_Helper.setStr(ldata, 6, pName);
						LMM_LittleMaidMobNX.Debug("SendIFF %s(%d) = %d", pName, li, tt);
						LMM_Net.sendToServer(ldata);
					}
					li++;
				}
			} else {
				LMM_IFF.setIFFValue(null, pName, tt);
			}
			
			Entity player = mc.thePlayer;
			pEntity.worldObj.playSound(player.posX+0.5, player.posY+0.5, player.posZ+0.5, "random.click", 1, 1, false);
		}
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, String pName, Entity pEntity) {
		// 名前と敵味方識別の描画
		int tt = LMM_IFF.getIFF(null, pName, pEntity.worldObj);
		int c = 0xffffff;
		switch (tt) {
		case LMM_IFF.iff_Friendry:
			c = 0x3fff3f;
			break;
		case LMM_IFF.iff_Unknown:
			c = 0xffff00;
			break;
		case LMM_IFF.iff_Enemy:
			c = 0xff3f3f;
			break;
		}
		drawString(this.mc.fontRendererObj, LMM_GuiIFF.IFFString[tt],
				(width - this.mc.fontRendererObj.getStringWidth(LMM_GuiIFF.IFFString[tt])) / 2, pY + 18, c);
		drawString(this.mc.fontRendererObj, pName,
				(width - this.mc.fontRendererObj.getStringWidth(pName)) / 2, pY + 6, 0xffffff);
	}

}
