package firis.lmlib.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import firis.lmlib.api.caps.IGuiTextureSelect;
import firis.lmlib.client.gui.parts.LMGuiSlotTextureSelect;
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

	/**
	 * 防具ボタン情報
	 * @author firis-games
	 */
	public enum EnumGuiArmorButton {
		ALL(0, "ALL", EntityEquipmentSlot.HEAD),
		HEAD(1, "HEAD", EntityEquipmentSlot.HEAD),
		CHEST(2, "CHEST", EntityEquipmentSlot.CHEST),
		LEGS(3, "LEGS", EntityEquipmentSlot.LEGS),
		FEET(4, "FEET", EntityEquipmentSlot.FEET);
		private EnumGuiArmorButton(int id, String name, EntityEquipmentSlot slot) {
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
		public static EnumGuiArmorButton next(String name) {
			EnumGuiArmorButton ret = ALL;
			boolean isCheck = false;
			for (EnumGuiArmorButton button : EnumGuiArmorButton.values()) {
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
		public static EnumGuiArmorButton get(String name) {
			EnumGuiArmorButton ret = ALL;
			for (EnumGuiArmorButton button : EnumGuiArmorButton.values()) {
				if (button.getName().equals(name)) {
					return button;
				}
			}
			return ret;
		}
	}
	
	//表示タイトル
	protected String screenTitle = "Texture Select";
	
	//親画面
	protected GuiScreen owner;
	
	//GUIパーツ
	protected LMGuiSlotTextureSelect selectPanel;
	protected GuiButton btnTexture;
	protected GuiButton btnArmor;
	protected GuiButton btnArmorParts;
	
	//テクスチャ変更ターゲット
	protected IGuiTextureSelect target;
	
	/**
	 * コンストラクタ
	 * @param owner
	 * @param target
	 */
	public LMGuiTextureSelect(GuiScreen owner, IGuiTextureSelect target) {
		this.owner = owner;
		this.target = target;
	}
	
	/**
	 * ボタン押下時の処理
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		switch (button.id) {
		//アーマーモードへ変更
		case 100:
			this.btnTexture.enabled = false;
			this.btnArmor.enabled = true;
			this.btnArmorParts.enabled = false;
			this.selectPanel.setArmorMode(false);
			break;
		//テクスチャモードへ変更
		case 101:
			this.btnTexture.enabled = true;
			this.btnArmor.enabled = false;
			this.btnArmorParts.enabled = true;
			this.selectPanel.setArmorMode(true);
			break;
		case 200:
			//メイドモデル選択モードのみ更新する
			if (this.btnArmorParts.enabled == false) {
				//同期処理
				this.target.syncTextureLittleMaid(this.selectPanel.getTextureLittleMaid(), 
						this.selectPanel.getTextureLittleMaidColor());
			}
			//防具モデルの更新
			//防具選択モードでのみ更新する
			if (this.btnArmorParts.enabled == true) {
				//同期処理
				this.target.syncTextureArmor(
						this.selectPanel.getTextureArmor(EntityEquipmentSlot.HEAD), 
						this.selectPanel.getTextureArmor(EntityEquipmentSlot.CHEST),
						this.selectPanel.getTextureArmor(EntityEquipmentSlot.LEGS),
						this.selectPanel.getTextureArmor(EntityEquipmentSlot.FEET));
			}

			//前の画面に戻る
			this.mc.displayGuiScreen(owner);
			break;
		case 300:
			//モード変更
			this.setChangeButtonArmorParts(EnumGuiArmorButton.next(this.btnArmorParts.displayString), true);
			break;
		}
	}
	
	/**
	 * Guiパーツ初期化
	 */
	@Override
	public void initGui() {
		
		//テクスチャ選択スロット
		this.selectPanel = new LMGuiSlotTextureSelect(this, this.target);
		this.selectPanel.registerScrollButtons(4, 5);
		
		//各ボタン登録
		this.buttonList.add(this.btnTexture = new GuiButton(100, width / 2 - 55, height - 55, 80, 20, "Texture"));
		this.buttonList.add(this.btnArmor = new GuiButton(101, width / 2 + 30, height - 55, 80, 20, "Armor"));
		this.buttonList.add(new GuiButton(200, width / 2 - 10 + 20, height - 30, 100, 20, "Select"));
		this.buttonList.add(this.btnArmorParts = new GuiButton(300, width / 2 - 55, height - 30, 60, 20, EnumGuiArmorButton.ALL.getName()));
		
		//ボタン有効無効設定
		this.btnTexture.enabled = false;
		this.btnArmor.enabled = true;
		this.btnArmorParts.enabled = false;
	}

	/**
	 * キー入力制御
	 */
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 && this.owner != null) {
			this.mc.displayGuiScreen(this.owner);
		}
	}
	
	/**
	 * 画面描画
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		//背景描画
		this.drawDefaultBackground();
		
		//テクスチャ選択画面描画
		this.selectPanel.drawScreen(mouseX, mouseY, partialTicks);
		
		//タイトル文字描画
		drawCenteredString(mc.fontRenderer, I18n.format(screenTitle), width / 2, 4, 0xffffff);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		//リトルメイド/アーマーモデル表示
		GL11.glPushMatrix();
		
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);

		RenderHelper.enableGUIStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

		GL11.glTranslatef(width / 2 - 115F, height - 5F, 100F);
		GL11.glScalef(60F, -60F, 60F);
		
		//メイドさんの位置調整
		selectPanel.entity.renderYawOffset = -25F;
		selectPanel.entity.rotationYawHead = -10F;
		
		//Entity描画
		this.selectPanel.setTextureSelectedBox();
		this.mc.getRenderManager().renderEntity(selectPanel.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		
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

	/**
	 * マウス制御
	 */
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.selectPanel.handleMouseInput();
	}
	
	/**
	 * 現在のアーマーモードを取得する
	 * @return
	 */
	public EnumGuiArmorButton getGuiArmorButtonMode() {
		return EnumGuiArmorButton.get(this.btnArmorParts.displayString);
	}
	
	/**
	 * 指定したアーマーパーツモードへ変更する
	 */
	public void setChangeButtonArmorParts(EnumGuiArmorButton guiArmorButton, boolean setScroll) {
		this.btnArmorParts.displayString = guiArmorButton.getName();
		if (setScroll) {
			this.selectPanel.setSelectedBoxArmor(guiArmorButton.getSlot());
		}
	}

}
