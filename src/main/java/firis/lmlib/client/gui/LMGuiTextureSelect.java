package firis.lmlib.client.gui;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.caps.IGuiTextureSelect;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.client.gui.parts.LMGuiPartsTextureSlot;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * リトルメイドテクスチャ選択画面
 */
@SideOnly(Side.CLIENT)
public class LMGuiTextureSelect extends GuiScreen {

	protected String screenTitle = "Texture Select";
	protected GuiScreen owner;
	protected LMGuiPartsTextureSlot selectPanel;
	protected GuiButton modeButton[] = new GuiButton[2];
	protected GuiButton armorButton;
	public IGuiTextureSelect target;
	public byte selectColor;
	
//	protected boolean toServer;
	
	/**
	 * 防具ボタン情報
	 * @author firis-games
	 */
	private enum EnumArmorButton {
		ALL(0, "ALL", null),
		HEAD(1, "HEAD", EntityEquipmentSlot.HEAD),
		CHEST(2, "CHEST", EntityEquipmentSlot.CHEST),
		LEGS(3, "LEGS", EntityEquipmentSlot.LEGS),
		FEET(4, "FEET", EntityEquipmentSlot.FEET);
		private EnumArmorButton(int id, String name, EntityEquipmentSlot slot) {
			this.name = name;
			this.slot = slot;
		}
		private String name;
		private EntityEquipmentSlot slot;
		public String getName() {
			return this.name;
		}
		public EntityEquipmentSlot getSlot() {
			return this.slot;
		}
		public static EnumArmorButton next(String name) {
			EnumArmorButton ret = ALL;
			boolean isCheck = false;
			for (EnumArmorButton button : EnumArmorButton.values()) {
				if (isCheck) {
					ret = button;
					break;
				}
				if (button.getName().equals(name)) {
					isCheck = true;
				}
			}
			return ret;
		}
		public static EnumArmorButton get(String name) {
			EnumArmorButton ret = ALL;
			for (EnumArmorButton button : EnumArmorButton.values()) {
				if (button.getName().equals(name)) {
					return button;
				}
			}
			return ret;
		}
	}

	public LMGuiTextureSelect(GuiScreen pOwner, IGuiTextureSelect pTarget, boolean pToServer) {
		owner = pOwner;
		target = pTarget;
		selectColor = (byte) pTarget.getTextureColor();
//		toServer = pToServer;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 100:
			modeButton[0].enabled = false;
			modeButton[1].enabled = true;
			selectPanel.setMode(false);
			armorButton.enabled = false;
			break;
		case 101:
			modeButton[0].enabled = true;
			modeButton[1].enabled = false;
			selectPanel.setMode(true);
			armorButton.enabled = true;
			break;
		case 200:
			//メイドモデル選択モードのみ更新する
			if (this.armorButton.enabled == false) {
				if (selectPanel.texsel[0] > -1) {
	//				target.setTextureNameMain(selectPanel.getSelectedBox(false).textureName);
//					target.setColor(selectColor);
//					target.getModelConfigCompound().refreshModelsLittleMaid(selectPanel.getSelectedBox(false).getTextureModelName(), selectColor);
					//同期処理
					target.syncTextureLittleMaid(selectPanel.getSelectedBox(false).getTextureModelName(), selectColor);
	//				target.getTextureBox()[0] = selectPanel.getSelectedBox(false);
				}
			}
			
			//防具モデルの更新
			//防具選択モードでのみ更新する
			if (this.armorButton.enabled == true) {
				if (selectPanel.texsel[1] > -1) {
	//				target.setTextureNameArmor(selectPanel.getSelectedBox(true).textureName);
	//				target.getModelConfigCompound().refreshModelsArmor(selectPanel.getSelectedBox(true).getTextureModelName());
	//				target.getTextureBox()[1] = selectPanel.getSelectedBox(true);
					
					//すべて
					if (this.armorButton.displayString.equals(EnumArmorButton.ALL.getName())) {
						
						String textureName = selectPanel.getSelectedBox(true).getTextureModelName();
						
//						target.getModelConfigCompound().refreshModelsArmor(EntityEquipmentSlot.HEAD, textureName);
//						target.getModelConfigCompound().refreshModelsArmor(EntityEquipmentSlot.CHEST, textureName);
//						target.getModelConfigCompound().refreshModelsArmor(EntityEquipmentSlot.LEGS, textureName);
//						target.getModelConfigCompound().refreshModelsArmor(EntityEquipmentSlot.FEET, textureName);
						
						//同期
						target.syncTextureArmor(textureName, textureName, textureName, textureName);
						
					//個別
					} else {

//						target.getModelConfigCompound().refreshModelsArmor(
//								EnumArmorButton.get(this.armorButton.displayString).getSlot(), 
//								selectPanel.getSelectedBox(true).getTextureModelName());
						
						//各パラメータ
						Map<EntityEquipmentSlot, String> textureNameHash = new EnumMap<>(EntityEquipmentSlot.class);
						textureNameHash.put(EntityEquipmentSlot.HEAD, target.getTextureArmor(EntityEquipmentSlot.HEAD));
						textureNameHash.put(EntityEquipmentSlot.CHEST, target.getTextureArmor(EntityEquipmentSlot.CHEST));
						textureNameHash.put(EntityEquipmentSlot.LEGS, target.getTextureArmor(EntityEquipmentSlot.LEGS));
						textureNameHash.put(EntityEquipmentSlot.FEET, target.getTextureArmor(EntityEquipmentSlot.FEET));
						
						//選択状態を設定する
						textureNameHash.put(EnumArmorButton.get(this.armorButton.displayString).getSlot(), 
								selectPanel.getSelectedBox(true).getTextureModelName());
						
						//同期
						target.syncTextureArmor(textureNameHash.get(EntityEquipmentSlot.HEAD), 
								textureNameHash.get(EntityEquipmentSlot.CHEST),
								textureNameHash.get(EntityEquipmentSlot.LEGS),
								textureNameHash.get(EntityEquipmentSlot.FEET));
					}
					
				}
			}
			
//			//サーバーへ情報送信
//			target.syncModelNamesToServer();
			
//			target.getModelConfigCompound().setTextureNames();
/*
			if (toServer) {
				MMM_TextureManager.instance.postSetTexturePack(target, selectColor, target.getTextureBox());
			} else {
				MMM_TextureBox lboxs[] = new MMM_TextureBox[2];
				lboxs[0] = (MMM_TextureBox)target.getTextureBox()[0];
				lboxs[1] = (MMM_TextureBox)target.getTextureBox()[1];
				target.setTexturePackName(lboxs);
			}
*/
//			System.out.println(String.format("select: %d(%s), %d(%s)",
//					selectPanel.texsel[0], target.getModelConfigCompound().getTextureBoxLittleMaid().getTextureModelName(),
//					selectPanel.texsel[1], target.getModelConfigCompound().getTextureBoxArmor().getTextureModelName()));
			mc.displayGuiScreen(owner);
			
//			if (toServer) {
//				MMM_TextureManager.instance.postSetTexturePack(target, selectColor, target.getTextureBox());
//				if (selectColor != selectPanel.color) {
//					// 色情報の設定
////					theMaid.maidColor = selectPanel.color | 0x010000 | (selectColor << 8);
//					NBTTagCompound tagCompound = new NBTTagCompound();
//					tagCompound.setByte("Color", selectColor);
//
//					target.syncNet(LMRMessage.EnumPacketMode.SERVER_DECREMENT_DYE, tagCompound);
//				}
//			}
			break;
		case 300:
			this.armorButton.displayString = EnumArmorButton.next(this.armorButton.displayString).getName();
			EnumArmorButton enumArmorButton = EnumArmorButton.get(this.armorButton.displayString);
			
			LMTextureBox texturebox;
			if (EnumArmorButton.ALL == enumArmorButton) {
//				texturebox = target.getModelConfigCompound().getTextureBoxArmorAll();
				texturebox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureArmor(EntityEquipmentSlot.HEAD));
			} else {
//				texturebox = target.getModelConfigCompound().getTextureBoxArmor(enumArmorButton.getSlot());
				texturebox = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureArmor(enumArmorButton.getSlot()));
			}
			selectPanel.setSelectedBoxArmor(texturebox.getTextureModelName());
			
			break;
		}
	}

	@Override
	public void initGui() {
		selectPanel = new LMGuiPartsTextureSlot(this);
		selectPanel.registerScrollButtons(4, 5);
		buttonList.add(modeButton[0] = new GuiButton(100, width / 2 - 55, height - 55, 80, 20, "Texture"));
		buttonList.add(modeButton[1] = new GuiButton(101, width / 2 + 30, height - 55, 80, 20, "Armor"));
		buttonList.add(new GuiButton(200, width / 2 - 10 + 20, height - 30, 100, 20, "Select"));
		modeButton[0].enabled = false;

		//防具別ボタン
		armorButton = new GuiButton(300, width / 2 - 55, height - 30, 60, 20, EnumArmorButton.ALL.getName());
		armorButton.enabled = false;
		buttonList.add(armorButton);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			mc.displayGuiScreen(owner);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
			throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY,
			int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		selectPanel.drawScreen(par1, par2, par3);
		drawCenteredString(mc.fontRenderer, I18n.format(screenTitle), width / 2, 4, 0xffffff);
		
		super.drawScreen(par1, par2, par3);
		
		GL11.glPushMatrix();
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);

		RenderHelper.enableGUIStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

		LMTextureBox lbox = selectPanel.getSelectedBox();
		GL11.glTranslatef(width / 2 - 115F, height - 5F, 100F);
		GL11.glScalef(60F, -60F, 60F);
		selectPanel.entity.renderYawOffset = -25F;
		selectPanel.entity.rotationYawHead = -10F;

		//ResourceLocation ltex[];
		if (selectPanel.mode) {
			selectPanel.entity.getModelConfigCompound().setTextureBoxLittleMaid(null);
			selectPanel.entity.getModelConfigCompound().setTextureBoxArmorAll(lbox);
//			selectPanel.entity.setTextureNames("default");			
		} else {
			selectPanel.entity.getModelConfigCompound().setTextureBoxLittleMaid(lbox);
			selectPanel.entity.getModelConfigCompound().setTextureBoxArmorAll(null);
			selectPanel.entity.getModelConfigCompound().setColor(selectColor);
//			selectPanel.entity.getModelConfigCompound().setTextureNames();
		}
		mc.getRenderManager().renderEntity(selectPanel.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		/*
		for (int li = 0; li < 16; li++) {
			if (lbox.hasColor(li)) {
				break;
			}
		}
		*/
		GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glPopMatrix();
	
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	@Override
	public void handleInput() throws IOException {
		super.handleInput();
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		super.handleKeyboardInput();
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		selectPanel.handleMouseInput();
	}

}
