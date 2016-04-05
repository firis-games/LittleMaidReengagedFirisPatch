package net.blacklab.lmr.client.gui;


import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mmmlibx.lib.MMM_GuiMobSelect;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.network.EnumPacketMode;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiIFF extends GuiScreen {

	public static final String IFFString[] = {
		"ENEMY",
		"NEUTRAL",
		"FRIENDLY"
	};

	protected EntityLittleMaid target;
	protected EntityPlayer thePlayer;

	public Map<String, Entity> entityMap;
	public static Map<Class, String> entityMapClass = new HashMap<Class, String>();
	public static List<String> exclusionList = new ArrayList<String>();

	protected String screenTitle;
	protected GuiSlot selectPanel;

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		selectPanel.handleMouseInput();
	}
	
	public GuiIFF(World world, EntityPlayer player, EntityLittleMaid pEntity) {
		super();
		entityMap = new TreeMap<String, Entity>();
		initEntitys(world, true);

		screenTitle = I18n.translateToLocal("littleMaidMob.gui.iff.title");
		target = pEntity;
		thePlayer = player;
	
		// IFFをサーバーから取得
		if (!Minecraft.getMinecraft().isSingleplayer()) {
			int li = 0;
			for (String ls : IFF.DefaultIFF.keySet()) {
				byte ldata[] = new byte[4 + ls.length()];
				NetworkHelper.setIntToPacket(ldata, 0, li);
				NetworkHelper.setStrToPacket(ldata, 4, ls);
				LittleMaidReengaged.Debug("RequestIFF %s(%d)", ls, li);
				LMRNetwork.sendToServer(EnumPacketMode.SERVER_REQUEST_IFF, ldata);
				li++;
			}
		}
	}

	public void initEntitys(World world, boolean pForce) {
		// 表示用EntityListの初期化
		if (entityMapClass.isEmpty()) {
			try {
				Map lmap = EntityList.classToStringMapping;// (Map)ModLoader.getPrivateValue(EntityList.class, null, 1);
				entityMapClass.putAll(lmap);
			}
			catch (Exception e) {
				LittleMaidReengaged.Debug("EntityClassMap copy failed.");
			}
		}

		if (entityMap == null) return;
		if (!pForce && !entityMap.isEmpty()) return;

		for (Map.Entry<Class, String> le : entityMapClass.entrySet()) {
			if (Modifier.isAbstract(le.getKey().getModifiers())) continue;
			LittleMaidReengaged.Debug("Add %s", le.getKey().getSimpleName());
			int li = 0;
			Entity lentity = null;
			try {
				// 表示用のEntityを作る
				do {
					lentity = (EntityLivingBase)le.getKey().getConstructor(World.class).newInstance(world);
//					lentity = (EntityLivingBase)EntityList.createEntityByName(le.getValue(), world);
				} while (lentity != null && checkEntity(le.getValue(), lentity, li++));
			} catch (Exception e) {
				LittleMaidReengaged.Debug("Entity [" + le.getValue() + "] can't created.");
			}
		}
	}

	@Override
	public void drawScreen(int px, int py, float pf) {
/*
		float lhealthScale = BossStatus.healthScale;
		int lstatusBarLength = BossStatus.statusBarTime;
		String lbossName = BossStatus.bossName;
		boolean lfield_82825_d = BossStatus.hasColorModifier;
*/

		drawDefaultBackground();
		selectPanel.drawScreen(px, py, pf);
		drawCenteredString(this.mc.fontRendererObj, I18n.translateToLocal(screenTitle), width / 2, 20, 0xffffff);
		super.drawScreen(px, py, pf);

/*
		// GUIで表示した分のボスのステータスを表示しない
		BossStatus.healthScale = lhealthScale;
		BossStatus.statusBarTime = lstatusBarLength;
		BossStatus.bossName = lbossName;
		BossStatus.hasColorModifier = lfield_82825_d;
*/
	}

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
		selectPanel = new GuiSlotMobSelect(mc, this);
		selectPanel.registerScrollButtons(3, 4);

		//		StringTranslate stringtranslate = new StringTranslate();

		buttonList.add(new GuiButton(200, width / 2 - 60, height - 40, 120, 20, "Done"));
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
		LMRNetwork.requestSavingIFF();
		super.onGuiClosed();
	}

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
						byte[] ldata = new byte[pName.length() + 5];
						ldata[0] = (byte) tt;
						NetworkHelper.setIntToPacket(ldata, 1, li);
						NetworkHelper.setStrToPacket(ldata, 5, pName);
						LittleMaidReengaged.Debug("SendIFF %s(%d) = %d", pName, li, tt);
						LMRNetwork.sendToServer(EnumPacketMode.SERVER_CHANGE_IFF, ldata);
					}
					li++;
				}
			} else {
				IFF.setIFFValue(null, pName, tt);
			}

			Entity player = mc.thePlayer;
			player.playSound(SoundEvent.soundEventRegistry.getObject(new ResourceLocation("ui.button.click")), 1, 1);
		}
	}

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
