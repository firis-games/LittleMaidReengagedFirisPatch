package net.blacklab.lmmnx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonArmorToggle extends GuiButton {

	protected String showText;

	/**
	 * 0: Inner
	 * 1: Outer
	 */
	private int toggleNode;

	/**
	 * 0: Normal
	 * 1: Light
	 */
	private int toggleLight;

	public boolean toggle = true;

	public static final ResourceLocation GUI_TOPBUTTON_RESOURCE = new ResourceLocation("lmmx:textures/gui/container/buttons/topbuttons.png");

	public GuiButtonArmorToggle(int buttonId, int x, int y, String buttonText, boolean ison) {
		super(buttonId, x, y, 16, 16, "");
		// TODO 自動生成されたコンストラクター・スタブ
		showText = buttonText;
		toggle = ison;
	}

	/**
	 * 0: Inner
	 * 1: Outer
	 */
	public GuiButtonArmorToggle setNode(int i){
		if(i>1) i = 1;
		if(i<0) i = 0;
		toggleNode = i;
		return this;
	}

	/**
	 * 0: Normal
	 * 1: Light
	 */
	public GuiButtonArmorToggle setLight(int i){
		if(i>1) i = 1;
		if(i<0) i = 0;
		toggleLight = i;
		return this;
	}

	public int toggleInt(){
		return toggle?1:0;
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
		drawTexturedModalRect(xPosition, yPosition, toggle?16*(toggleNode+1):0, 16*toggleLight, 16, 16);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
//		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	protected void handleHovered(int mouseX, int mouseY) {
		hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY) {
		// TODO 自動生成されたメソッド・スタブ
		super.drawButtonForegroundLayer(mouseX, mouseY);
		showHoverText(Minecraft.getMinecraft(), mouseX, mouseY);
	}

	protected void showHoverText(Minecraft mcMinecraft, int mx, int my){
		if(hovered){
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.colorMask(true, true, true, false);
			FontRenderer fRenderer = mcMinecraft.getRenderManager().getFontRenderer();
			int lcolor = 0xc0000000;
			String viewString = StatCollector.translateToLocal(showText+getTaleString());
			int fx = fRenderer.getStringWidth(viewString);
			drawGradientRect(mx+4, my+4, mx+4+fx+4, my+4+8+4, lcolor, lcolor);
			drawCenteredString(fRenderer, viewString, mx+fx/2+6, my+6, 0xffffffff);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.colorMask(true, true, true, true);
		}
	}

	protected String getTaleString() {
		return toggle?".hide":".show";
	}

}
