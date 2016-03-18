package mmmlibx.lib;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public abstract class MMM_GuiMobSelect extends GuiScreen {

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

	public MMM_GuiMobSelect(World pWorld) {
		super();
		entityMap = new TreeMap<String, Entity>();
		initEntitys(pWorld, true);
	}

	public MMM_GuiMobSelect(World pWorld, Map<String, Entity> pMap) {
		super();
		entityMap = pMap;
		initEntitys(pWorld, false);
	}


	public void initEntitys(World world, boolean pForce) {
		// 表示用EntityListの初期化
		if (entityMapClass.isEmpty()) {
			try {
				Map lmap = EntityList.classToStringMapping;// (Map)ModLoader.getPrivateValue(EntityList.class, null, 1);
				entityMapClass.putAll(lmap);
			}
			catch (Exception e) {
				MMMLib.Debug("EntityClassMap copy failed.");
			}
		}
		
		if (entityMap == null) return;
		if (!pForce && !entityMap.isEmpty()) return;
		
		for (Map.Entry<Class, String> le : entityMapClass.entrySet()) {
			if (Modifier.isAbstract(le.getKey().getModifiers())) continue;
			MMMLib.Debug("Add %s", le.getKey().getSimpleName());
			int li = 0;
			Entity lentity = null;
			try {
				// 表示用のEntityを作る
				do {
					lentity = (EntityLivingBase)le.getKey().getConstructor(World.class).newInstance(world);
//					lentity = (EntityLivingBase)EntityList.createEntityByName(le.getValue(), world);
				} while (lentity != null && checkEntity(le.getValue(), lentity, li++));
			} catch (Exception e) {
				MMMLib.Debug("Entity [" + le.getValue() + "] can't created.");
			}
		}
	}

	/**
	 * 渡されたEntityのチェック及び加工。
	 * trueを返すと同じクラスのエンティティを再度渡してくる、そのときpIndexはカウントアップされる
	 */
	protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
		entityMap.put(pName, pEntity);
		return false;
	}

	@Override
	public void initGui() {
		selectPanel = new MMM_GuiSlotMobSelect(mc, this);
		selectPanel.registerScrollButtons(3, 4);
	}

	@Override
	public void drawScreen(int px, int py, float pf) {
		float lhealthScale = BossStatus.healthScale;
		int lstatusBarLength = BossStatus.statusBarTime;
		String lbossName = BossStatus.bossName;
		boolean lfield_82825_d = BossStatus.hasColorModifier;
		
		drawDefaultBackground();
		selectPanel.drawScreen(px, py, pf);
		drawCenteredString(this.mc.fontRendererObj, I18n.translateToLocal(screenTitle), width / 2, 20, 0xffffff);
		super.drawScreen(px, py, pf);
		
		// GUIで表示した分のボスのステータスを表示しない
		BossStatus.healthScale = lhealthScale;
		BossStatus.statusBarTime = lstatusBarLength;
		BossStatus.bossName = lbossName;
		BossStatus.hasColorModifier = lfield_82825_d;
	}

	/**
	 *  スロットがクリックされた
	 */
	public abstract void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLivingBase pEntity);

	/**
	 *  スロットの描画
	 */
	public abstract void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight, String pName, Entity pEntity);
	
}
