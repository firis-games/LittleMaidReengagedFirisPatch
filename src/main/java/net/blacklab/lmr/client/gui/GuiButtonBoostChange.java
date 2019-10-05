package net.blacklab.lmr.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonBoostChange extends GuiButton {

	/**
	 * trueで逆向き(-)，falseで正向き(+)
	 */
	public boolean inverse = false;

	private static final ResourceLocation widgetRL = new ResourceLocation("textures/gui/widgets.png");

	public GuiButtonBoostChange(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, 16, 16, buttonText);
	}

	protected void handleHovered(int mouseX, int mouseY) {
		hovered = enabled && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + height;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		handleHovered(mouseX, mouseY);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(widgetRL);
		GlStateManager.enableBlend();
		drawTexturedModalRect(this.x  , this.y  , 0  , 46 + 20*getHoverState(hovered), 8, 8);
		drawTexturedModalRect(this.x  , this.y+8, 0  , 58 + 20*getHoverState(hovered), 8, 8);
		drawTexturedModalRect(this.x+8, this.y  , 192, 46 + 20*getHoverState(hovered), 8, 8);
		drawTexturedModalRect(this.x+8, this.y+8, 192, 58 + 20*getHoverState(hovered), 8, 8);
		drawCenteredString(mc.fontRenderer, inverse ? "-" : "+", this.x + 8, this.y + 4, enabled ? 0xffffff : 0x404040);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
//		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public GuiButtonBoostChange setInverse(boolean v) {
		inverse = v;
		return this;
	}

	public GuiButtonBoostChange setEnabled(boolean v) {
		enabled = v;
		return this;
	}

}
