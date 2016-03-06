package net.blacklab.lmmnx.client;

import littleMaidMobX.LMM_EntityLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonFreedomToggle extends GuiButtonSwimToggle {

	protected LMM_EntityLittleMaid lmm;

	public GuiButtonFreedomToggle(int buttonId, int x, int y,
			String buttonText, boolean ison, LMM_EntityLittleMaid maid) {
		super(buttonId, x, y, buttonText, ison);
		// TODO 自動生成されたコンストラクター・スタブ
		lmm = maid;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		if(!visible) return;
		handleHovered(mouseX, mouseY);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		mc.getTextureManager().bindTexture(GUI_TOPBUTTON_RESOURCE);
		float colorb = toggle?1.0F:0.3F;
		if(hovered){
			GlStateManager.color(colorb, colorb, colorb, 1.0f);
		}else{
			GlStateManager.color(colorb, colorb, colorb, 0.5f);
		}
		drawTexturedModalRect(xPosition, yPosition, 64, 0, 16, 16);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
//		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

}
