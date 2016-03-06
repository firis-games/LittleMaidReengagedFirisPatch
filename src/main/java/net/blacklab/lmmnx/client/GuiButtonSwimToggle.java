package net.blacklab.lmmnx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonSwimToggle extends GuiButtonArmorToggle {

	public GuiButtonSwimToggle(int buttonId, int x, int y, String buttonText,
			boolean ison) {
		super(buttonId, x, y, buttonText, ison);
		// TODO 自動生成されたコンストラクター・スタブ
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
		if(hovered){
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		}else{
			GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
		}
		drawTexturedModalRect(xPosition, yPosition, 48, toggle?0:16, 16, 16);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
//		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	@Override
	protected String getTaleString() {
		// TODO 自動生成されたメソッド・スタブ
		return toggle?".disable":".enable";
	}

}
