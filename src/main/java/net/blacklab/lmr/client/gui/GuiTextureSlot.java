package net.blacklab.lmr.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.ibm.icu.text.Normalizer.Mode;

import mmmlibx.lib.multiModel.model.mc162.ModelMultiBase;
import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.client.entity.EntityLittleMaidForTexSelect;
import net.blacklab.lmr.entity.maidmodel.TextureBox;
import net.blacklab.lmr.entity.maidmodel.TextureBoxBase;
import net.blacklab.lmr.util.helper.RendererHelper;
import net.blacklab.lmr.util.manager.ModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiTextureSlot extends GuiSlot {

	public GuiTextureSelect owner;
	public int selected;
	public EntityLittleMaidForTexSelect entity;
	public List<TextureBox> indexTexture;
	public List<TextureBox> indexArmor;
	public boolean mode;
	public int texsel[] = new int[2];
	public int color;
	public int selectColor;
	private ItemStack armors[] = new ItemStack[] {
			new ItemStack(Items.leather_boots),
			new ItemStack(Items.leather_leggings),
			new ItemStack(Items.leather_chestplate),
			new ItemStack(Items.leather_helmet)
	};
	protected boolean isContract;
	protected static TextureBox blankBox;


	public GuiTextureSlot(GuiTextureSelect pOwner) {
		super(pOwner.mc, pOwner.width, pOwner.height, 16, pOwner.height - 64, 36);
		owner = pOwner;
		entity = new EntityLittleMaidForTexSelect(owner.mc.theWorld);
		color = owner.target.getColor();
		selectColor = -1;
		blankBox = new TextureBox();
		blankBox.models = new ModelMultiBase[] {null, null, null};
		
		texsel[0] = 0;//-1;
		texsel[1] = 0;//-1;
		indexTexture = new ArrayList<TextureBox>();
		indexArmor = new ArrayList<TextureBox>();
		isContract = owner.target.isContract();
		entity.setContract(isContract);
		TextureBoxBase ltbox[] = owner.target.getTextureBox();
		for (int li = 0; li < ModelManager.instance.getTextureCount(); li++) {
			TextureBox lbox = ModelManager.getTextureList().get(li);
			if (isContract) {
				if (lbox.getContractColorBits() > 0) {
					indexTexture.add(lbox);
				}
			} else {
				if (lbox.getWildColorBits() > 0) {
					indexTexture.add(lbox);
				}
			}
			if (lbox.hasArmor()) {
				indexArmor.add(lbox);
			}
			if (lbox == ltbox[0]) {
				texsel[0] = indexTexture.size() - 1;
			}
			if (lbox == ltbox[1]) {
				texsel[1] = indexArmor.size() - 1;
			}
		}
		setMode(false);
	}

	@Override
	protected int getSize() {
		return mode ? indexArmor.size() : indexTexture.size();
	}
	
	public static TextureBox getBlankBox() {
		return blankBox;
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		if (mode) {
			selected = var1;
			texsel[1] = var1;
		} else {
			TextureBox lbox = getSelectedBox(var1);
			if (lbox.hasColor(selectColor, isContract) && (owner.canSelectColor & (1 << selectColor)) > 0) {
				selected = var1;
				texsel[0] = var1;
				owner.selectColor = selectColor;
			} else if (lbox.hasColor(color, isContract)) {
				selected = var1;
				texsel[0] = var1;
				owner.selectColor = color;
			}
		}
	}

	@Override
	protected boolean isSelected(int var1) {
		return selected == var1;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int var1, int var2, int var3, int var4, int var6, int var7) {
		GL11.glPushMatrix();
		
		if (!mode) {
			for (int li = 0; li < 16; li++) {
				int lx = var2 + 15 + 12 * li;
				selectColor = (mouseX - (var2 + 15)) / 12;
				if ((selectColor < 0) && (selectColor > 15)) {
					selectColor = -1;
				}
				if (color == li) {
					Gui.drawRect(lx, var3, lx + 11, var3 + 36, 0x88882222);
				} else if (owner.selectColor == li) {
					Gui.drawRect(lx, var3, lx + 11, var3 + 36, 0x88226622);
				} else if ((owner.canSelectColor & (1 << li)) > 0) {
					Gui.drawRect(lx, var3, lx + 11, var3 + 36, 0x88222288);
				}
			}
		}
		
		TextureBox lbox;
		if (mode) {
			lbox = indexArmor.get(var1);
			entity.textureData.textureBox[0] = blankBox;
			entity.textureData.textureBox[1] = lbox;
		} else {
			lbox = indexTexture.get(var1);
			entity.textureData.textureBox[0] = lbox;
			entity.textureData.textureBox[1] = blankBox;
		}
//		MMM_TextureManager.instance.checkTextureBoxServer(lbox);
		GL11.glDisable(GL11.GL_BLEND);
		
		owner.drawString(this.owner.mc.fontRendererObj, lbox.textureName,
				var2 + 207 - mc.fontRendererObj.getStringWidth(lbox.textureName), var3 + 25, -1);
		GL11.glTranslatef(var2 + 8F, var3 + 25F, 50F);
		GL11.glScalef(12F, -12F, 12F);
		entity.renderYawOffset = 30F;
		entity.rotationYawHead = 15F;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		entity.modeArmor = mode;
		if (mode) {
			//デフォルトアーマー
			GL11.glTranslatef(1f, 0.25F, 0f);
			entity.setTextureNames("default");
			Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			RendererHelper.setLightmapTextureCoords(0x00f0);//61680

			// 素材別アーマー
			for (int li = 0; li < ModelManager.armorFilenamePrefix.length; li++) {
				GL11.glTranslatef(1F, 0, 0);
				if (lbox.armors.containsKey(ModelManager.armorFilenamePrefix[li])) {
//					ltxname = entity.getTextures(1);
//					ltxname[0] = ltxname[1] = ltxname[2] = ltxname[3] =
//							lbox.getArmorTextureName(MMM_TextureManager.tx_armor1, "default", 0);
//					ltxname = entity.getTextures(2);
//					ltxname[0] = ltxname[1] = ltxname[2] = ltxname[3] =
//							lbox.getArmorTextureName(MMM_TextureManager.tx_armor2, "default", 0);
					entity.setTextureNames(ModelManager.armorFilenamePrefix[li]);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
					RendererHelper.setLightmapTextureCoords(0x00f0);//61680
				}
			}
		} else {
			// テクスチャ表示
			for (int li = 0; li < 16; li++) {
				GL11.glTranslatef(1F, 0, 0);
				if (lbox.hasColor(li, isContract)) {
					entity.setColor(li);
					entity.setContract(isContract);
					entity.setTextureNames();
//					entity.getTextures(0)[0] = lbox.getTextureName(li + (isContract ? 0 : MMM_TextureManager.tx_wild));
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
					RendererHelper.setLightmapTextureCoords(0x00f0);//61680
				}
			}
		}
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glPopMatrix();
	}

	public TextureBox getSelectedBox() {
		return getSelectedBox(selected);
	}

	public TextureBox getSelectedBox(int pIndex) {
		return mode ? indexArmor.get(pIndex) : indexTexture.get(pIndex);
	}

	public TextureBox getSelectedBox(boolean pMode) {
		return pMode ? indexArmor.get(texsel[1]) : indexTexture.get(texsel[0]);
	}

	public void setMode(boolean pFlag) {
		scrollBy(slotHeight * -getSize());
		entity.modeArmor = pFlag;
		if (pFlag) {
			selected = texsel[1];
			mode = true;
			entity.setCurrentItemOrArmor(1, armors[0]);
			entity.setCurrentItemOrArmor(2, armors[1]);
			entity.setCurrentItemOrArmor(3, armors[2]);
			entity.setCurrentItemOrArmor(4, armors[3]);
		} else {
			selected = texsel[0];
			mode = false;
			entity.setCurrentItemOrArmor(1, null);
			entity.setCurrentItemOrArmor(2, null);
			entity.setCurrentItemOrArmor(3, null);
			entity.setCurrentItemOrArmor(4, null);
		}
		scrollBy(slotHeight * selected);
	}

}
