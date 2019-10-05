package net.blacklab.lmr.client.gui;

import net.blacklab.lmr.entity.littlemaid.EntityLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonFreedomToggle extends GuiButtonSwimToggle {

	protected EntityLittleMaid lmm;

	public GuiButtonFreedomToggle(int buttonId, int x, int y,
			String buttonText, boolean ison, EntityLittleMaid maid) {
		super(buttonId, x, y, buttonText, ison);
		// TODO 自動生成されたコンストラクター・スタブ
		lmm = maid;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
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
		drawTexturedModalRect(this.x, this.y, 64, 0, 16, 16);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
//		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

}
