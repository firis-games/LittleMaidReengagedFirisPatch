package net.blacklab.lmr.client.gui.inventory;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.gui.GuiButtonArmorToggle;
import net.blacklab.lmr.client.gui.GuiButtonBoostChange;
import net.blacklab.lmr.client.gui.GuiButtonFreedomToggle;
import net.blacklab.lmr.client.gui.GuiButtonNextPage;
import net.blacklab.lmr.client.gui.GuiTextureSelect;
import net.blacklab.lmr.entity.experience.ExperienceUtil;
import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.blacklab.lmr.inventory.ContainerInventoryLittleMaid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiMaidInventory extends GuiContainer {
	// Field
	private Random rand;
	private IInventory playerInventory;
	private IInventory maidInventory;
	private int ySizebk;
	private int updateCounter;
	public EntityLittleMaid entitylittlemaid;

	public GuiButtonNextPage txbutton[] = new GuiButtonNextPage[4];
	public GuiButton selectbutton;
	public GuiButtonArmorToggle visarmorbutton[] = new GuiButtonArmorToggle[4];
	public GuiButtonFreedomToggle frdmbutton;
	public GuiButtonBoostChange boostMinus;
	public GuiButtonBoostChange boostPlus;
	public boolean isChangeTexture;

	private int topTicks = 0;

	private static class RenderInfoPart {
		private static boolean shiftLock;

		/**
		 * 0: Health, 1: Armor, 2: Mode, 3: Free
		 */
		private static boolean renderInfo[] = new boolean[] {
				true, false, true, false
		};

		private static int renderingPart = 0;

		public static int getEnabledCounts() {
			int count = 0;
			for (boolean s: renderInfo) {
				if (s) ++count;
			}
			return count;
		}

		public static void setEnabled(int index, boolean flag) {
			renderInfo[index] = flag;
			// 体力だけはtrueを維持
			renderInfo[0] = true;
			if (renderingPart == index && !flag) {
				shiftPart();
			}
		}

		public static boolean isEnabled(int index) {
			try {
				return renderInfo[index];
			} catch (IndexOutOfBoundsException exception) {
				return false;
			}
		}

		public static void shiftPart() {
			while (!isEnabled(++renderingPart)) {
				if (renderingPart >= renderInfo.length) {
					renderingPart = -1;
				}
			}
		}

		public static int getRenderingPart() {
			return renderingPart;
		}

		public static void lock() {
			shiftLock = true;
		}

		public static void unlock() {
			shiftLock = false;
		}

		public static boolean islocked() {
			return shiftLock;
		}
	}

	protected static final ResourceLocation fguiTex =
			new ResourceLocation(LittleMaidReengaged.DOMAIN, "textures/gui/container/littlemaidinventory2.png");

	// Method
	public GuiMaidInventory(EntityPlayer pPlayer, EntityLittleMaid elmaid) {
		super(new ContainerInventoryLittleMaid(pPlayer.inventory, elmaid));
		rand = new Random();
		playerInventory = pPlayer.inventory;
		maidInventory = elmaid.maidInventory;
		allowUserInput = false;
		updateCounter = 0;
		ySizebk = ySize;
		ySize = 207;
		isChangeTexture = true;

		entitylittlemaid = elmaid;
		// entitylittlemaid.setOpenInventory(true);

		topTicks = entitylittlemaid.ticksExisted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		if (!entitylittlemaid.getActivePotionEffects().isEmpty()) {
			guiLeft = 160 + (width - xSize - 200) / 2;
		}
		buttonList.add(txbutton[0] = new GuiButtonNextPage(100, guiLeft + 25, guiTop + 7, false));
		buttonList.add(txbutton[1] = new GuiButtonNextPage(101, guiLeft + 55, guiTop + 7, true));
		buttonList.add(txbutton[2] = new GuiButtonNextPage(110, guiLeft + 25, guiTop + 47, false));
		buttonList.add(txbutton[3] = new GuiButtonNextPage(111, guiLeft + 55, guiTop + 47, true));
		buttonList.add(selectbutton = new GuiButton(200, guiLeft + 26, guiTop + 25, 53, 18, "select"));
		buttonList.add(visarmorbutton[0] = new GuiButtonArmorToggle  (300, guiLeft     , guiTop - 14, "littleMaidMob.gui.toggle.inner"     , true).setNode(0).setLight(0));
		buttonList.add(visarmorbutton[1] = new GuiButtonArmorToggle  (301, guiLeft + 16, guiTop - 14, "littleMaidMob.gui.toggle.innerlight", true).setNode(0).setLight(1));
		buttonList.add(visarmorbutton[2] = new GuiButtonArmorToggle  (302, guiLeft + 32, guiTop - 14, "littleMaidMob.gui.toggle.outer"     , true).setNode(1).setLight(0));
		buttonList.add(visarmorbutton[3] = new GuiButtonArmorToggle  (303, guiLeft + 48, guiTop - 14, "littleMaidMob.gui.toggle.outerlight", true).setNode(1).setLight(1));
		buttonList.add(frdmbutton        = new GuiButtonFreedomToggle(311, guiLeft + 64, guiTop - 16, "littleMaidMob.gui.toggle.freedom"   , entitylittlemaid.isFreedom(), entitylittlemaid));
//		buttonList.add(swimbutton        = new GuiButtonSwimToggle   (310, guiLeft + 80, guiTop - 16, "littleMaidMob.gui.toggle.swim"      , entitylittlemaid.isSwimmingEnabled()));
		buttonList.add(boostMinus        = new GuiButtonBoostChange  (320, guiLeft + 96, guiTop - 16, "littleMaidMob.gui.button.minusboost").setInverse(true).setEnabled(false));
		buttonList.add(boostPlus         = new GuiButtonBoostChange  (321, guiLeft+xSize-16, guiTop - 16, "littleMaidMob.gui.button.plusboost"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String mInventoryString = I18n.format(maidInventory.getName());
		mc.fontRendererObj.drawString(mInventoryString, 168 - mc.fontRendererObj.getStringWidth(mInventoryString), 64, 0x404040);
		mc.fontRendererObj.drawString(I18n.format(playerInventory.getName()), 8, 114, 0x404040);

		//fontRenderer.drawString(StatCollector.translateToLocal("littleMaidMob.text.Health"), 86, 8, 0x404040);
		//fontRenderer.drawString(StatCollector.translateToLocal("littleMaidMob.text.AP"), 86, 32, 0x404040);

		if (RenderInfoPart.getRenderingPart() == 2) {
			mc.fontRendererObj.drawString(I18n.format(
					"littleMaidMob.mode.".concat(entitylittlemaid.getMaidModeStringForDisplay())), 7, 64, 0x404040);
		}

//	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

//		      GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
//	      GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//	      GL11.glPushMatrix();
//	      GL11.glTranslatef(lj + 51, lk + 57, 50F);
		// TODO use vanilla method at Background to render maid
/*
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(lj + 51, lk + 57, 50F);
		float f1 = 30F;
//	      GL11.glScalef(-f1, f1, f1);
//	      GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GlStateManager.scale(-f1, f1, f1);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = entitylittlemaid.renderYawOffset;
		float f3 = entitylittlemaid.rotationYaw;
		float f4 = entitylittlemaid.rotationYawHead;
		float f5 = entitylittlemaid.rotationPitch;
//	      float f8 = (float) (lj + 51) - xSize_lo;
//	      float f9 = (float) (lk + 75) - 50 - ySize_lo;
		float f8 = guiLeft + 51 - par1;
		float f9 = guiTop + 22 - par2;
//	      GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
//	      RenderHelper.enableStandardItemLighting();
//	      GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
//	      GL11.glRotatef(-(float) Math.atan(f9 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();

		GlStateManager.rotate(-135F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-(float) Math.atan(f9 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		entitylittlemaid.renderYawOffset = (float) Math.atan(f8 / 40F) * 20F;
		entitylittlemaid.rotationYawHead = entitylittlemaid.rotationYaw = (float) Math.atan(f8 / 40F) * 40F;
		entitylittlemaid.rotationPitch = -(float) Math.atan(f9 / 40F) * 20F;
//		GL11.glTranslatef(0.0F, entitylittlemaid.yOffset, 0.0F);
//		Minecraft.getMinecraft().getRenderManager().playerViewY = 180F;
		GlStateManager.translate(0.0F, entitylittlemaid.yOffset, 0.0F);
		Minecraft.getMinecraft().getRenderManager().setPlayerViewY(180F);
		Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
		// TODO この最後の引数もヨクワカンネ
		Minecraft.getMinecraft().getRenderManager().doRenderEntity(entitylittlemaid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);
		entitylittlemaid.renderYawOffset = f2;
		entitylittlemaid.rotationYaw = f3;
		entitylittlemaid.rotationYawHead = f4;
		entitylittlemaid.rotationPitch = f5;
//	      GL11.glPopMatrix();
//	      RenderHelper.disableStandardItemLighting();
//	      GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
//	      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F / 1.0F, 240F / 1.0F);
//	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();

		Iterator iterator = buttonList.iterator();
		while(iterator.hasNext()){
			GuiButton gButton = (GuiButton) iterator.next();
			gButton.drawButtonForegroundLayer(par1-guiLeft, par2-guiTop);
		}

//		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableColorMaterial();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
*/
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// 背景
		ResourceLocation lrl = entitylittlemaid.textureData.getGUITexture();
		if (lrl == null) {
			lrl = fguiTex;
		}
		mc.getTextureManager().bindTexture(lrl);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int lj = guiLeft;
		int lk = guiTop;
		drawTexturedModalRect(lj, lk, 0, 0, xSize, ySize);

		// PotionEffect
		displayDebuffEffects();

		// LP/AP
		drawHeathArmor(0, 0);

		// Mob
		net.minecraft.client.gui.inventory.GuiInventory.drawEntityOnScreen(
				lj + 51, lk + 57, 30,
				(float)(lj + 51) - i, (float)(lk + 57 - 50) - j, entitylittlemaid);
/*
		MMM_Client.setTexture(field_110324_m);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		boolean flag1 = (entitylittlemaid.hurtResistantTime / 3) % 2 == 1;
		if (entitylittlemaid.hurtResistantTime < 10) {
			flag1 = false;
		}
		int i1 = MathHelper.ceiling_float_int(entitylittlemaid.func_110138_aP());
		int j1 = MathHelper.ceiling_float_int(entitylittlemaid.prevHealth);
		rand.setSeed(updateCounter * 0x4c627);

		// AP
		int k1 = entitylittlemaid.getTotalArmorValue();
		for (int j2 = 0; j2 < 10; j2++) {
			int k3 = 43 + lk;
			if (k1 > 0) {
				// int j5 = j + 158 - j2 * 8;
				int j5 = lj + 86 + j2 * 8;
				if (j2 * 2 + 1 < k1) {
					drawTexturedModalRect(j5, k3, 34, 9, 9, 9);
				}
				if (j2 * 2 + 1 == k1) {
					drawTexturedModalRect(j5, k3, 25, 9, 9, 9);
				}
				if (j2 * 2 + 1 > k1) {
					drawTexturedModalRect(j5, k3, 16, 9, 9, 9);
				}
			}

			// LP
			int k5 = 0;
			if (flag1) {
				k5 = 1;
			}
			int i6 = lj + 86 + j2 * 8;
			k3 = 19 + lk;
			if (i1 <= 4) {
				k3 += rand.nextInt(2);
			}
			drawTexturedModalRect(i6, k3, 16 + k5 * 9, 0, 9, 9);
			if (flag1) {
				if (j2 * 2 + 1 < j1) {
					drawTexturedModalRect(i6, k3, 70, 0, 9, 9);
				}
				if (j2 * 2 + 1 == j1) {
					drawTexturedModalRect(i6, k3, 79, 0, 9, 9);
				}
			}
			if (j2 * 2 + 1 < i1) {
				drawTexturedModalRect(i6, k3, 52, 0, 9, 9);
			}
			if (j2 * 2 + 1 == i1) {
				drawTexturedModalRect(i6, k3, 61, 0, 9, 9);
			}
		}
*/
		int booster = entitylittlemaid.getExpBooster();
		if (booster >= ExperienceUtil.getBoosterLimit(entitylittlemaid.getMaidLevel()))
			boostPlus.setEnabled(false);
		else boostPlus.setEnabled(true);
		if (booster <= 1)
			boostMinus.setEnabled(false);
		else boostMinus.setEnabled(true);

		// EXPゲージ
		GlStateManager.colorMask(true, true, true, false);
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		int level = entitylittlemaid.getMaidLevel();
		if (level >= ExperienceUtil.EXP_FUNCTION_MAX) {
			level--;
		}
		float currentxp = entitylittlemaid.getMaidExperience() - ExperienceUtil.getRequiredExpToLevel(level);
		float nextxp = ExperienceUtil.getRequiredExpToLevel(level+1) - ExperienceUtil.getRequiredExpToLevel(level);
		drawGradientRect(guiLeft+97, guiTop+7, guiLeft+168, guiTop+18, 0x80202020, 0x80202020);
		drawGradientRect(guiLeft+98, guiTop+8, guiLeft+98+(int)(69*currentxp/nextxp), guiTop+17, 0xf0008000, 0xf000f000);

		//経験値ブースト
		drawGradientRect(guiLeft+112, guiTop-16, guiLeft+xSize-16, guiTop, 0x80202020, 0x80202020);
		drawCenteredString(fontRendererObj, String.format("x%d", booster), guiLeft+112+(xSize-128)/2, guiTop-12, 0xffffff);

		// LV数値
//		GlStateManager.pushMatrix();
		String lvString = String.format("Lv. %d", entitylittlemaid.getMaidLevel());
		mc.fontRendererObj.drawString(lvString, guiLeft + 108, guiTop + 13 - mc.fontRendererObj.FONT_HEIGHT/2, 0xff303030);
//		mc.fontRendererObj.drawString(lvString, guiLeft + 109, guiTop + 13 - mc.fontRendererObj.FONT_HEIGHT/2, 0xfff0f0f0);
//		GlStateManager.popMatrix();

		// HP stack
		// テキスト表示
		if (RenderInfoPart.getRenderingPart() == 0) {
			float lhealth = entitylittlemaid.getHealth();
			if (lhealth > 20) {
				mc.fontRendererObj.drawString(String.format("x%d", MathHelper.floor_float((lhealth-1) / 20)), guiLeft + 95, guiTop + 64, 0x404040);
			}
		}

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.colorMask(true, true, true, true);

	}

	protected void drawHeathArmor(int par1, int par2) {
		boolean var3 = entitylittlemaid.hurtResistantTime / 3 % 2 == 1;

		if (entitylittlemaid.hurtResistantTime < 10) {
			var3 = false;
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int orgnHealth = MathHelper.ceiling_float_int(entitylittlemaid.getHealth());
		int orgnLasthealth = orgnHealth + MathHelper.ceiling_float_int(entitylittlemaid.getLastDamage());
		this.rand.setSeed(updateCounter * 312871);
		//		FoodStats var7 = entitylittlemaid.getFoodStats();
//		int var8 = var7.getFoodLevel();
//		int var9 = var7.getPrevFoodLevel();
		IAttributeInstance var10 = entitylittlemaid.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float maxHealth = (float) var10.getAttributeValue();
		float origAbsorption = entitylittlemaid.getAbsorptionAmount();
		int maxHealthRows = MathHelper.ceiling_float_int((maxHealth + origAbsorption) / 20.0F);
		int healthRows = MathHelper.ceiling_float_int(orgnHealth / 20f);
		int var17 = Math.max(10 - (maxHealthRows - 2), 3);
		float absorption = origAbsorption;
		int var21 = -1;

		if (entitylittlemaid.isPotionActive(Potion.getPotionById(10))) {
			var21 = updateCounter % MathHelper.ceiling_float_int(maxHealth + 5.0F);
		}

		int ldrawx;
		int ldrawy;

		// AP
		int larmor = entitylittlemaid.getTotalArmorValue();
		if (larmor == 0) {
			RenderInfoPart.setEnabled(1, false);
		} else if (larmor > 0) {
			RenderInfoPart.setEnabled(1, true);
			if (RenderInfoPart.getRenderingPart() == 1) {
				ldrawy = guiTop + 64;
				for (int li = 0; li < 10; ++li) {
						ldrawx = guiLeft + li * 8 + 7;

						if (li * 2 + 1 < larmor) {
							this.drawTexturedModalRect(ldrawx, ldrawy, 34, 9, 9, 9);
						}
						if (li * 2 + 1 == larmor) {
							this.drawTexturedModalRect(ldrawx, ldrawy, 25, 9, 9, 9);
						}
						if (li * 2 + 1 > larmor) {
							this.drawTexturedModalRect(ldrawx, ldrawy, 16, 9, 9, 9);
						}
				}
			}
		}

		// LP
		if (RenderInfoPart.getRenderingPart() == 0) {
			for (int li = maxHealthRows > healthRows ? 9 : MathHelper.ceiling_float_int((maxHealth + origAbsorption - 2)/2f) % 10; li >= 0; --li) {
				int var23 = 16;
				if (entitylittlemaid.isPotionActive(Potion.getPotionById(19))) {
					var23 += 36;
				} else if (entitylittlemaid.isPotionActive(Potion.getPotionById(20))) {
					var23 += 72;
				}

//				int var25 = MathHelper.ceiling_float_int((li + 1) / 10.0F);
				ldrawx = guiLeft + li % 10 * 8 + 7;
				ldrawy = guiTop + 64;

				if (orgnHealth <= 4) {
					ldrawy += this.rand.nextInt(2);
				}
				if (li == var21) {
					ldrawy -= 2;
				}

				this.drawTexturedModalRect(ldrawx, ldrawy, var3 ? 25 : 16, 0, 9, 9);

				int lhealth = orgnHealth % 20;
				if (lhealth == 0 && orgnHealth > 0) lhealth = 20;
				int llasthealth = orgnLasthealth % 20;
				if (llasthealth == 0 && orgnLasthealth > 0) llasthealth = 20;

				if (var3) {
					if (li * 2 + 1 < llasthealth) {
						this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 54, 0, 9, 9);
					}
					if (li * 2 + 1 == llasthealth) {
						this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 63, 0, 9, 9);
					}
				}

				if (absorption > 0.0F) {
					if (absorption == origAbsorption && origAbsorption % 2.0F == 1.0F) {
						this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 153, 0, 9, 9);
					} else {
						this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 144, 0, 9, 9);
					}

					absorption -= 2.0F;
				} else {
					if (li * 2 + 1 < lhealth) {
						this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 36, 0, 9, 9);
					}
					if (li * 2 + 1 == lhealth) {
						this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 45, 0, 9, 9);
					}
				}
			}
		}

		// Air
		ldrawy = guiTop + 46;
		if (entitylittlemaid.isInsideOfMaterial(Material.WATER)) {
			int var23 = entitylittlemaid.getAir();
			int var35 = MathHelper.ceiling_double_int((var23 - 2) * 10.0D / 300.0D);
			int var25 = MathHelper.ceiling_double_int(var23 * 10.0D / 300.0D) - var35;

			for (int var26 = 0; var26 < var35 + var25; ++var26) {
				ldrawx = guiLeft + var26 * 8 + 86;
				if (var26 < var35) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 16, 18, 9, 9);
				} else {
					this.drawTexturedModalRect(ldrawx, ldrawy, 25, 18, 9, 9);
				}
			}
		}

	}

	@Override
	public void drawScreen(int i, int j, float f) {
		if ((entitylittlemaid.ticksExisted - topTicks) % 30 == 0) {
			if (!RenderInfoPart.islocked())RenderInfoPart.shiftPart();
			RenderInfoPart.lock();
		} else {
			RenderInfoPart.unlock();
		}

		super.drawScreen(i, j, f);

		// 事前処理
		for(int cnt=0;cnt<4;cnt++){
			visarmorbutton[cnt].visible = true;
			visarmorbutton[cnt].toggle = entitylittlemaid.isArmorVisible(cnt);
		}
		frdmbutton.visible = true;
		frdmbutton.toggle = entitylittlemaid.isFreedom();

		int ii = i - guiLeft;
		int jj = j - guiTop;

		if (entitylittlemaid.canChangeModel() && ii > 7 && ii < 96 && jj > 7 && jj < 60) {
			// ボタンの表示
			txbutton[0].visible = true;
			txbutton[1].visible = true;
			txbutton[2].visible = true;
			txbutton[3].visible = true;

			// テクスチャ名称の表示
			GL11.glPushMatrix();
			GL11.glTranslatef(i - ii, j - jj, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//			RenderHelper.disableStandardItemLighting();

			if (entitylittlemaid.textureData.textureBox[0] != null) {
				String ls1 = entitylittlemaid.textureData.getTextureName(0);
				String ls2 = entitylittlemaid.textureData.getTextureName(1);
				int ltw1 = this.mc.fontRendererObj.getStringWidth(ls1);
				int ltw2 = this.mc.fontRendererObj.getStringWidth(ls2);
				int ltwmax = (ltw1 > ltw2) ? ltw1 : ltw2;
				int lbx = 52 - ltwmax / 2;
				int lby = 68;
				int lcolor;
				lcolor = jj < 20 ? 0xc0882222 : 0xc0000000;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.colorMask(true, true, true, false);
				drawGradientRect(lbx - 3, lby - 4, lbx + ltwmax + 3, lby + 8, lcolor, lcolor);
				drawString(this.mc.fontRendererObj, ls1, 52 - ltw1 / 2, lby - 2, -1);
				lcolor = jj > 46 ? 0xc0882222 : 0xc0000000;
				drawGradientRect(lbx - 3, lby + 8, lbx + ltwmax + 3, lby + 16 + 4, lcolor, lcolor);
				drawString(this.mc.fontRendererObj, ls2, 52 - ltw2 / 2, lby + 10, -1);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.colorMask(true, true, true, true);
			}
			GL11.glPopMatrix();
//			RenderHelper.enableStandardItemLighting();
		} else {
			txbutton[0].visible = false;
			txbutton[1].visible = false;
			txbutton[2].visible = false;
			txbutton[3].visible = false;
		}
		if (ii > 25 && ii < 79 && jj > 24 && jj < 44) {
			selectbutton.visible = true;
		} else {
			selectbutton.visible = false;
		}
		if (ii > 96 && ii < xSize && jj > -16 && jj < 0) {
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.colorMask(true, true, true, false);
			String str = I18n.format("littleMaidMob.gui.text.expboost");
			int width = fontRendererObj.getStringWidth(str);
			int centerx = guiLeft + 48 + xSize/2;
			drawGradientRect(centerx - width/2 - 4, guiTop, centerx + width/2 + 4, guiTop + fontRendererObj.FONT_HEIGHT, 0xc0202020, 0xc0202020);
			drawCenteredString(fontRendererObj, str, centerx, guiTop, 0xffffff);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.colorMask(true, true, true, true);
		}

	}



	@Override
	public void updateScreen() {
		super.updateScreen();
		updateCounter++;
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		try {
			super.mouseClicked(i, j, k);
		} catch (IOException e) {
		}
/*
		// 26,8-77,59
		int ii = i - guiLeft;
		int jj = j - guiTop;

		// TODO:メイドアセンブル画面を作る
		if (ii > 25 && ii < 78 && jj > 7 && jj < 60) {
			// 伽羅表示領域
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				// Shift+で逆周り
				LMM_Client.setPrevTexturePackege(entitylittlemaid, k);
			} else {
				LMM_Client.setNextTexturePackege(entitylittlemaid, k);
			}
			LMM_Client.setTextureValue(entitylittlemaid);
		}
*/
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		int booster = entitylittlemaid.getExpBooster();
		switch (par1GuiButton.id) {
		case 100 :
			entitylittlemaid.setNextTexturePackege(0);
			entitylittlemaid.setTextureNames();
			entitylittlemaid.syncModelNames();
			break;
		case 101 :
			entitylittlemaid.setPrevTexturePackege(0);
			entitylittlemaid.setTextureNames();
			entitylittlemaid.syncModelNames();
			break;
		case 110 :
			entitylittlemaid.setNextTexturePackege(1);
			entitylittlemaid.setTextureNames();
			entitylittlemaid.syncModelNames();
			break;
		case 111 :
			entitylittlemaid.setPrevTexturePackege(1);
			entitylittlemaid.setTextureNames();
			entitylittlemaid.syncModelNames();
			break;
		case 200 :
			int ldye = 0;
			if (mc.thePlayer.capabilities.isCreativeMode) {
				ldye = 0xffff;
			} else {
				for (ItemStack lis : mc.thePlayer.inventory.mainInventory) {
					if (lis != null && lis.getItem() == Items.DYE) {
						ldye |= (1 << (15 - lis.getItemDamage()));
					}
				}
			}
			isChangeTexture = false;
			mc.displayGuiScreen(new GuiTextureSelect(this, entitylittlemaid, ldye, true));
			break;
		case 300 :
			visarmorbutton[0].toggle=!visarmorbutton[0].toggle;
			setArmorVisible();
			break;
		case 301 :
			visarmorbutton[1].toggle=!visarmorbutton[1].toggle;
			setArmorVisible();
			break;
		case 302 :
			visarmorbutton[2].toggle=!visarmorbutton[2].toggle;
			setArmorVisible();
			break;
		case 303 :
			visarmorbutton[3].toggle=!visarmorbutton[3].toggle;
			setArmorVisible();
			break;
		case 311 :
			frdmbutton.toggle=!frdmbutton.toggle;
			entitylittlemaid.setFreedom(frdmbutton.toggle);
			entitylittlemaid.handleStatusUpdate((byte) (frdmbutton.toggle?12:13));
			break;
		case 320:
			booster-=2;
		case 321:
			entitylittlemaid.setExpBooster(++booster);
			entitylittlemaid.syncExpBoost();
			break;
		}
	}

	protected void setArmorVisible() {
		entitylittlemaid.setMaidArmorVisible(visarmorbutton[0].toggle, visarmorbutton[1].toggle, visarmorbutton[2].toggle, visarmorbutton[3].toggle);
		entitylittlemaid.syncMaidArmorVisible();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		 entitylittlemaid.onGuiClosed();
		if (isChangeTexture) {
			entitylittlemaid.sendTextureToServer();
		}
	}

	private void displayDebuffEffects() {
		// ポーションエフェクトの表示
		int lx = guiLeft - 124;
		int ly = guiTop;
		Collection collection = entitylittlemaid.getActivePotionEffects();
		if (collection.isEmpty()) {
			return;
		}
		int lh = 33;
		if (collection.size() > 5) {
			lh = 132 / (collection.size() - 1);
		}
		for (Iterator iterator = entitylittlemaid.getActivePotionEffects().iterator(); iterator.hasNext();) {
			PotionEffect potioneffect = (PotionEffect) iterator.next();
			Potion potion = potioneffect.getPotion();//Potion.potionTypes[potioneffect.getPotionID()];
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
			drawTexturedModalRect(lx, ly, 0, ySizebk, 140, 32);

			if (potion.hasStatusIcon()) {
				int i1 = potion.getStatusIconIndex();
				drawTexturedModalRect(lx + 6, ly + 7, 0 + (i1 % 8) * 18,
						ySizebk + 32 + (i1 / 8) * 18, 18, 18);
			}
			String ls = I18n.format(potion.getName());
			if (potioneffect.getAmplifier() > 0) {
				ls = (new StringBuilder()).append(ls).append(" ")
						.append(I18n.format((new StringBuilder())
								.append("potion.potency.")
								.append(potioneffect.getAmplifier())
								.toString())).toString();
			}
			mc.fontRendererObj.drawString(ls, lx + 10 + 18, ly + 6, 0xffffff);
			// TODO ここもよく分からん
			String s1 = Potion.getPotionDurationString(potioneffect, 1);
			mc.fontRendererObj.drawString(s1, lx + 10 + 18, ly + 6 + 10, 0x7f7f7f);
			ly += lh;
		}
	}

}
