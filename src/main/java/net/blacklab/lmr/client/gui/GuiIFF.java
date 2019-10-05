package net.blacklab.lmr.client.gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.network.LMRMessage;
import net.blacklab.lmr.network.LMRNetwork;
import net.blacklab.lmr.util.IFF;
import net.blacklab.lmr.util.helper.CommonHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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
	public static Set<ResourceLocation> entityMapClass = new HashSet<ResourceLocation>();
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
		entityMap = new TreeMap<>();
		initEntitys(world, true);

		screenTitle = I18n.format("littleMaidMob.gui.iff.title");
		target = pEntity;
		thePlayer = player;

		// IFFをサーバーから取得
		if (!Minecraft.getMinecraft().isSingleplayer()) {
			int li = 0;
			for (String ls : IFF.getUserIFF(player.getUniqueID()).keySet()) {
				// TODO Too much packet with many entities
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setInteger("Index", li);
				tagCompound.setString("Name", ls);

				LittleMaidReengaged.Debug("RequestIFF %s(%d)", ls, li);
				LMRNetwork.sendPacketToServer(LMRMessage.EnumPacketMode.SERVER_REQUEST_IFF, null, tagCompound);
				li++;
			}
		}
	}

	public void initEntitys(World world, boolean pForce) {
		// 表示用EntityListの初期化
		if (entityMapClass.isEmpty()) {
			try {
				//Map lmap = EntityList.CLASS_TO_NAME;// (Map)ModLoader.getPrivateValue(EntityList.class, null, 1);
				Set<ResourceLocation> lmap = EntityList.getEntityNameList();
				entityMapClass.addAll(lmap);
			}
			catch (Exception e) {
				LittleMaidReengaged.Debug("EntityClassMap copy failed.");
			}
		}

		if (entityMap == null) return;
		if (!pForce && !entityMap.isEmpty()) return;

		
		//IFFの表示処理はあとで見直す
		for (ResourceLocation mobrl : entityMapClass) {
			Entity lentity = null;
			int li = 0;
			try {
				// 表示用のEntityを作る
				do {
					lentity = EntityList.createEntityByIDFromName(mobrl, world);
				} while (lentity != null && checkEntity(mobrl.toString(), lentity, li++));
			} catch (Exception e) {
				LittleMaidReengaged.Debug("Entity [" + mobrl.toString() + "] can't created.");
			}
			
		}
		
		
		/*
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
		*/
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
		drawCenteredString(this.mc.fontRenderer, I18n.format(screenTitle), width / 2, 20, 0xffffff);
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
			byte tt = IFF.getIFF(CommonHelper.getPlayerUUID(thePlayer), pName, pEntity.getEntityWorld());
			tt++;
			if (tt > 2) {
				tt = 0;
			}

			IFF.setIFFValue(CommonHelper.getPlayerUUID(thePlayer), pName, tt);

			// サーバーへ変更値を送る。
			int li = 0;
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setByte("Value", tt);
			tagCompound.setString("Name", pName);
			LMRNetwork.sendPacketToServer(LMRMessage.EnumPacketMode.SERVER_CHANGE_IFF, -1, tagCompound);

			Entity player = mc.player;
			player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("ui.button.click")), 1, 1);
		}
	}

	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, String pName, Entity pEntity) {
		// 名前と敵味方識別の描画
		int tt = IFF.getIFF(CommonHelper.getPlayerUUID(thePlayer), pName, pEntity.world);
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
		drawString(this.mc.fontRenderer, GuiIFF.IFFString[tt],
				(width - this.mc.fontRenderer.getStringWidth(GuiIFF.IFFString[tt])) / 2, pY + 18, c);
		drawString(this.mc.fontRenderer, pName,
				(width - this.mc.fontRenderer.getStringWidth(pName)) / 2, pY + 6, 0xffffff);
	}

}
