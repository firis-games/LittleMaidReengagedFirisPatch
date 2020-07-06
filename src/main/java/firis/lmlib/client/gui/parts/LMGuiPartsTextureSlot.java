package firis.lmlib.client.gui.parts;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.client.entity.EntityLittleMaidGui;
import firis.lmlib.client.gui.LMGuiTextureSelect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * リトルメイド/アーマーモデルの描画スロット
 * @author firis-games
 *
 */
@SideOnly(Side.CLIENT)
public class LMGuiPartsTextureSlot extends GuiSlot {

	public EntityLittleMaidGui entity;

	protected LMGuiTextureSelect owner;
	protected int selected;
	protected List<LMTextureBox> indexTexture;
	protected List<LMTextureBox> indexArmor;
	
	public boolean mode;
	public int texsel[] = new int[2];
	protected byte color;
	protected byte selectColor;
	
//	private ItemStack armors[] = new ItemStack[] {
//			new ItemStack(Items.LEATHER_BOOTS),
//			new ItemStack(Items.LEATHER_LEGGINGS),
//			new ItemStack(Items.LEATHER_CHESTPLATE),
//			new ItemStack(Items.LEATHER_HELMET)
//	};
	protected boolean isContract;
//	protected static LMTextureBox blankBox;


	public LMGuiPartsTextureSlot(LMGuiTextureSelect pOwner) {
		super(pOwner.mc, pOwner.width, pOwner.height, 16, pOwner.height - 64, 36);
		owner = pOwner;
		entity = new EntityLittleMaidGui(owner.mc.world);
//		color = owner.target.getColor();
		color = (byte) owner.target.getTextureColor();
		selectColor = -1;
//		blankBox = new LMTextureBox();
//		blankBox.models = new ModelMultiBase[] {null, null, null};

		texsel[0] = 0;//-1;
		texsel[1] = 0;//-1;
		indexTexture = new ArrayList<LMTextureBox>();
		indexArmor = new ArrayList<LMTextureBox>();
//		isContract = owner.target.isContract();
		isContract = true;
//		entity.getModelConfigCompound().setContract(isContract);
//		LMTextureBox ltbox[] = owner.target.getModelConfigCompound().getLMTextureBox();
//		LMTextureBox ltboxLittleMaid = owner.target.getModelConfigCompound().getTextureBoxLittleMaid();
		LMTextureBox ltboxLittleMaid = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(owner.target.getTextureLittleMaid());
//		LMTextureBox ltboxArmor = owner.target.getModelConfigCompound().getTextureBoxArmorAll();
		LMTextureBox ltboxArmor = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(owner.target.getTextureArmor(EntityEquipmentSlot.HEAD));
		
		
		
		for (LMTextureBox lbox : LMLibraryAPI.instance().getTextureManager().getLMTextureBoxList()) {
			if (isContract) {
				if (lbox.hasLittleMaid()) {
					indexTexture.add(lbox);
				}
			} else {
				if (lbox.hasWildLittleMaid()) {
					indexTexture.add(lbox);
				}
			}
			if (lbox.hasArmor()) {
				indexArmor.add(lbox);
			}
			if (lbox == ltboxLittleMaid) {
				texsel[0] = indexTexture.size() - 1;
			}
			if (lbox == ltboxArmor) {
				texsel[1] = indexArmor.size() - 1;
			}
		}
		
		setMode(false);
		
//		for (int li = 0; li < ModelManager.instance.getTextureCount(); li++) {
//			LMTextureBox lbox = ModelManager.getTextureList().get(li);
//			if (isContract) {
//				if (lbox.getContractColorBits() > 0) {
//					indexTexture.add(lbox);
//				}
//			} else {
//				if (lbox.getWildColorBits() > 0) {
//					indexTexture.add(lbox);
//				}
//			}
//			if (lbox.hasArmor()) {
//				indexArmor.add(lbox);
//			}
//			if (lbox == ltboxLittleMaid) {
//				texsel[0] = indexTexture.size() - 1;
//			}
//			if (lbox == ltboxArmor) {
//				texsel[1] = indexArmor.size() - 1;
//			}
//		}
	}

	@Override
	protected int getSize() {
		return mode ? indexArmor.size() : indexTexture.size();
	}

//	public static LMTextureBox getBlankBox() {
//		return blankBox;
//	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		if (mode) {
			selected = var1;
			texsel[1] = var1;
		} else {
			LMTextureBox lbox = getSelectedBox(var1);
			if (hasColorContract(lbox, selectColor, isContract)) {
				selected = var1;
				texsel[0] = var1;
				owner.selectColor = selectColor;
			} else if (hasColorContract(lbox, color, isContract)) {
				selected = var1;
				texsel[0] = var1;
				owner.selectColor = color;
			}
		}
	}
	
	/**
	 * メイドさんの色判定
	 * @param lbox
	 * @param color
	 * @param contract
	 * @return
	 */
	private boolean hasColorContract(LMTextureBox lbox, int color, boolean contract) {
		if (contract) {
			return lbox.hasColor(color);
		} else {
			return lbox.hasWildColor(color);
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
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		
		//表示対象判断
		if (!isSlotView(slotIndex)) return;
		
		GL11.glPushMatrix();

		LMTextureBox lbox;
		if (mode) {
			lbox = indexArmor.get(slotIndex);
//			entity.getModelConfigCompound().setTextureBoxLittleMaid(null);
//			entity.getModelConfigCompound().setTextureBoxArmorAll(lbox);
			entity.setTextureArmor(lbox);
		} else {
			lbox = indexTexture.get(slotIndex);
//			entity.getModelConfigCompound().setTextureBoxLittleMaid(lbox);
//			entity.getModelConfigCompound().setTextureBoxArmorAll(null);
			entity.setTextureLittleMaid(lbox);
		}

		if (!mode) {
			for (int li = 0; li < 16; li++) {
				int lx = xPos + 15 + 12 * li;
				selectColor = (byte)((mouseX - (xPos + 15)) / 12);
				if ((selectColor < 0) && (selectColor > 15)) {
					selectColor = -1;
				}
				if (color == li) {
					Gui.drawRect(lx, yPos, lx + 11, yPos + 36, 0x88882222);
				} else if (owner.selectColor == li) {
					Gui.drawRect(lx, yPos, lx + 11, yPos + 36, 0x88226622);
				} else if (lbox.hasColor(li)) {
					Gui.drawRect(lx, yPos, lx + 11, yPos + 36, 0x66888888);
				}
			}
		}

		//アーマー選択画面の文字が2行目以降おかしくなる
		//enableAlpha担っている場合文字が正常に表示されないみたい
		GlStateManager.disableAlpha();
		//		MMM_TextureManager.instance.checkLMTextureBoxServer(lbox);
		GL11.glDisable(GL11.GL_BLEND);

		owner.drawString(this.owner.mc.fontRenderer, lbox.getTextureModelName(),
				xPos + 207 - mc.fontRenderer.getStringWidth(lbox.getTextureModelName()), yPos + 25, -1);
		GL11.glTranslatef(xPos + 8F, yPos + 25F, 50F);
		GL11.glScalef(12F, -12F, 12F);
		entity.renderYawOffset = 30F;
		entity.rotationYawHead = 15F;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
//		entity.modeArmor = mode;
		
		if (mode) {
			//デフォルトアーマー
			GL11.glTranslatef(1f, 0.25F, 0f);
//			entity.setTextureNames("default");
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//			RendererHelper.setLightmapTextureCoords(0x00f0);//61680

//			// 素材別アーマー
//			for (int li = 0; li < ModelManager.armorFilenamePrefix.length; li++) {
//				GL11.glTranslatef(1F, 0, 0);
//				if (lbox.armors.containsKey(ModelManager.armorFilenamePrefix[li])) {
////					ltxname = entity.getTextures(1);
////					ltxname[0] = ltxname[1] = ltxname[2] = ltxname[3] =
////							lbox.getArmorTextureName(MMM_TextureManager.tx_armor1, "default", 0);
////					ltxname = entity.getTextures(2);
////					ltxname[0] = ltxname[1] = ltxname[2] = ltxname[3] =
////							lbox.getArmorTextureName(MMM_TextureManager.tx_armor2, "default", 0);
//					entity.setTextureNames(ModelManager.armorFilenamePrefix[li]);
//					Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//					RendererHelper.setLightmapTextureCoords(0x00f0);//61680
//				}
//			}
		} else {
			
			// テクスチャ表示
			for (byte li = 0; li < 16; li++) {
				GL11.glTranslatef(1F, 0, 0);
				
				if (hasColorContract(lbox, li, isContract)) {
//					entity.getModelConfigCompound().setColor(li);
//					entity.getModelConfigCompound().setContract(isContract);
					entity.setTextureLittleMaidColor(li, isContract);
					
//					entity.setTextureNames();
//					entity.getTextures(0)[0] = lbox.getTextureName(li + (isContract ? 0 : MMM_TextureManager.tx_wild));
					Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//					RendererHelper.setLightmapTextureCoords(0x00f0);//61680
				}
			}
		}
		
//		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glPopMatrix();
	}

	public LMTextureBox getSelectedBox() {
		return getSelectedBox(selected);
	}

	protected LMTextureBox getSelectedBox(int pIndex) {
		return mode ? indexArmor.get(pIndex) : indexTexture.get(pIndex);
	}

	public LMTextureBox getSelectedBox(boolean pMode) {
		return pMode ? indexArmor.get(texsel[1]) : indexTexture.get(texsel[0]);
	}

	public void setMode(boolean pFlag) {
		scrollBy(slotHeight * -getSize());
//		entity.modeArmor = pFlag;
		if (pFlag) {
			selected = texsel[1];
			mode = true;
//			entity.setItemStackToSlot(EntityEquipmentSlot.FEET,  armors[0]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS,  armors[1]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, armors[2]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD,  armors[3]);
		} else {
			selected = texsel[0];
			mode = false;
//			entity.setItemStackToSlot(EntityEquipmentSlot.FEET,  ItemStack.EMPTY);
//			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS,  ItemStack.EMPTY);
//			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
//			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD,  ItemStack.EMPTY);
		}
		scrollBy(slotHeight * selected);
	}
	
	/**
	 * 選択位置を設定する
	 * @param pIndex
	 */
	public void setSelectedBoxArmor(String modelName) {
		
		int idx = 0;
		for (LMTextureBox box : indexArmor) {
			if (box.getTextureModelName().equals(modelName)) {
				break;
			}
			idx++;
		}
		
		scrollBy(slotHeight * -getSize());
		selected = idx;
		scrollBy(slotHeight * selected);
	}
	
	/**
	 * 対象スロットが描画位置かどうか判断する
	 * @param slot
	 * @return
	 */
	private boolean isSlotView(int slot) {
		
		//スロットの開始位置と終了位置
		float slotHeightStart = this.slotHeight * slot;
		float slotHeightEnd = this.slotHeight + slotHeightStart;
		
		float nowHeightStart = this.amountScrolled; 
		float nowHeightEnd = this.height + nowHeightStart;
		
		boolean slotView = false;
		
		if (nowHeightStart <= slotHeightStart &&  slotHeightStart <= nowHeightEnd) {
			slotView = true;
		} else if (nowHeightStart <= slotHeightEnd &&  slotHeightEnd <= nowHeightEnd) {
			slotView = true;
		}
		return slotView;
	}

}
