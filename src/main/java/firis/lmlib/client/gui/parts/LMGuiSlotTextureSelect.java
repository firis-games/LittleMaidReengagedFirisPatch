package firis.lmlib.client.gui.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.caps.IGuiTextureSelect;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.client.entity.EntityLittleMaidGui;
import firis.lmlib.client.gui.LMGuiTextureSelect;
import firis.lmlib.client.gui.LMGuiTextureSelect.EnumGuiArmorButton;
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
public class LMGuiSlotTextureSelect extends GuiSlot {

	public EntityLittleMaidGui entity;

	protected LMGuiTextureSelect owner;
	protected List<LMTextureBox> indexTexture;
	protected List<LMTextureBox> indexArmor;
	
	/**
	 * 選択モード
	 */
	protected boolean isArmorMode;
	
	/**
	 * マウスオーバー
	 */
	protected int mouseoverColor;
	
	/**
	 * 最後に選択した位置
	 */
	protected int nowSelected;
	
	/**
	 * 選択カラー
	 */
	protected int selectColor;
	
	/**
	 * テクスチャモデルGUI表示時の初期状態
	 */
	protected int targetColor;
	protected boolean targetContract;
	
	/**
	 * テクスチャ選択位置
	 */
	protected int selTextureLittleMaid = 0;
	protected Map<EntityEquipmentSlot, Integer> selTextureArmors = new HashMap<>();	
	
	/**
	 * コンストラクタ
	 * @param pOwner
	 */
	public LMGuiSlotTextureSelect(LMGuiTextureSelect owner, IGuiTextureSelect target) {
		super(owner.mc, owner.width, owner.height, 16, owner.height - 64, 36);
		
		this.owner = owner;
		this.entity = new EntityLittleMaidGui(owner.mc.world);
		
		//ターゲット初期値設定
		this.targetColor = target.getTextureColor();
		this.targetContract = target.getTextureContract();
		
		//選択位置初期化
		this.selTextureLittleMaid = 0;
		this.selTextureArmors.put(EntityEquipmentSlot.HEAD, 0);
		this.selTextureArmors.put(EntityEquipmentSlot.CHEST, 0);
		this.selTextureArmors.put(EntityEquipmentSlot.LEGS, 0);
		this.selTextureArmors.put(EntityEquipmentSlot.FEET, 0);
		this.mouseoverColor = -1;
		
		//ターゲットのテクスチャボックスを取得する
		LMTextureBox ltboxLittleMaid = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureLittleMaid());
		LMTextureBox ltboxArmorHead = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureArmor(EntityEquipmentSlot.HEAD));
		LMTextureBox ltboxArmorChest = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureArmor(EntityEquipmentSlot.CHEST));
		LMTextureBox ltboxArmorLegs = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureArmor(EntityEquipmentSlot.LEGS));
		LMTextureBox ltboxArmorFeet = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(target.getTextureArmor(EntityEquipmentSlot.FEET));
		
		//表示用キャッシュ初期化
		this.indexTexture = new ArrayList<LMTextureBox>();
		this.indexArmor = new ArrayList<LMTextureBox>();
		for (LMTextureBox lbox : LMLibraryAPI.instance().getTextureManager().getLMTextureBoxList()) {
			//リトルメイドモデル
			if (this.targetContract) {
				//通常種
				if (lbox.hasLittleMaid()) {
					this.indexTexture.add(lbox);
				}
			} else {
				//野生種
				if (lbox.hasWildLittleMaid()) {
					this.indexTexture.add(lbox);
				}
			}
			//アーマーモデル
			if (lbox.hasArmor()) {
				this.indexArmor.add(lbox);
			}
			
			//選択位置設定
			//リトルメイド
			if (lbox == ltboxLittleMaid) {
				this.selTextureLittleMaid = indexTexture.size() - 1;
			}
			//頭防具
			if (lbox == ltboxArmorHead) {
				this.selTextureArmors.put(EntityEquipmentSlot.HEAD, indexArmor.size() - 1);
			}
			//胴防具
			if (lbox == ltboxArmorChest) {
				this.selTextureArmors.put(EntityEquipmentSlot.CHEST, indexArmor.size() - 1);
			}
			//腰防具
			if (lbox == ltboxArmorLegs) {
				this.selTextureArmors.put(EntityEquipmentSlot.LEGS, indexArmor.size() - 1);
			}
			//足防具
			if (lbox == ltboxArmorFeet) {
				this.selTextureArmors.put(EntityEquipmentSlot.FEET, indexArmor.size() - 1);
			}
		}
		//初期選択状態設定
		this.setArmorMode(false);
	}

	/**
	 * スロット表示数
	 */
	@Override
	protected int getSize() {
		return isArmorMode ? indexArmor.size() : indexTexture.size();
	}
	
	/**
	 * クリック時の処理
	 * @param slotIndex
	 * @param isDoubleClick
	 * @param mouseX
	 * @param mouseY
	 */
	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		
		//防具モデル
		if (this.isArmorMode) {
			
			EnumGuiArmorButton clickArmorMode = null;
			clickArmorMode = this.mouseoverColor == 2 ? EnumGuiArmorButton.ALL : clickArmorMode;
			clickArmorMode = this.mouseoverColor == 5 ? EnumGuiArmorButton.HEAD : clickArmorMode;
			clickArmorMode = this.mouseoverColor == 7 ? EnumGuiArmorButton.CHEST : clickArmorMode;
			clickArmorMode = this.mouseoverColor == 9 ? EnumGuiArmorButton.LEGS : clickArmorMode;
			clickArmorMode = this.mouseoverColor == 11 ? EnumGuiArmorButton.FEET : clickArmorMode;
			
			if (clickArmorMode != null) {
				//個別選択
				this.owner.setChangeButtonArmorParts(clickArmorMode, false);
				
			}
			//通常選択
			this.nowSelected = slotIndex;
			EnumGuiArmorButton mode = this.owner.getGuiArmorButtonMode();
			if (mode == EnumGuiArmorButton.ALL) {
				//全部位
				this.selTextureArmors.put(EntityEquipmentSlot.HEAD, slotIndex);
				this.selTextureArmors.put(EntityEquipmentSlot.CHEST, slotIndex);
				this.selTextureArmors.put(EntityEquipmentSlot.LEGS, slotIndex);
				this.selTextureArmors.put(EntityEquipmentSlot.FEET, slotIndex);
			} else {
				//各部位
				this.selTextureArmors.put(mode.getSlot(), slotIndex);
			}
			
		//リトルメイドモデル
		} else {
			LMTextureBox lbox = getTextureBoxFromSlot(slotIndex);
			if (hasColorContract(lbox, this.mouseoverColor, this.targetContract)) {
				this.nowSelected = slotIndex;
				this.selTextureLittleMaid = slotIndex;
				this.selectColor = this.mouseoverColor;
			} else if (hasColorContract(lbox, this.targetColor, this.targetContract)) {
				this.nowSelected = slotIndex;
				this.selTextureLittleMaid = slotIndex;
				this.selectColor = this.targetColor;
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

	/**
	 * 選択状態の判定
	 */
	@Override
	protected boolean isSelected(int slotIndex) {
		return this.nowSelected == slotIndex;
	}
	
	/**
	 * 背景描画
	 */
	@Override
	protected void drawBackground() {}
	
	/**
	 * スロット描画
	 */
	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		
		//表示対象判断
		if (!isSlotView(slotIndex)) return;
		
		GL11.glPushMatrix();
		
		//マウスの位置を計算
		this.mouseoverColor = (byte)((this.mouseX - (xPos + 15)) / 12);
		if ((this.mouseoverColor < 0) && (this.mouseoverColor > 15)) {
			this.mouseoverColor = -1;
		}
		
		//テクスチャ取得
		LMTextureBox lbox = this.getTextureBoxFromSlot(slotIndex);
		
		if (!isArmorMode) {
			//リトルメイドスロット
			for (int li = 0; li < 16; li++) {
				int lx = xPos + 15 + 12 * li;
				//背景色描画
				if (targetColor == li) {
					//赤
					Gui.drawRect(lx, yPos, lx + 11, yPos + 36, 0x88882222);
				} else if (this.selectColor == li) {
					//緑
					Gui.drawRect(lx, yPos, lx + 11, yPos + 36, 0x88226622);
				} else if (lbox.hasColor(li)) {
					//灰
					Gui.drawRect(lx, yPos, lx + 11, yPos + 36, 0x66888888);
				}
			}
		} else {
			//防具スロット
			//ALL
			this.drawArmorSlotBackColor(xPos, yPos, 2, this.getArmorSlotBackColor(EnumGuiArmorButton.ALL, lbox));
			
			//各部位
			this.drawArmorSlotBackColor(xPos, yPos, 5, this.getArmorSlotBackColor(EnumGuiArmorButton.HEAD, lbox));
			this.drawArmorSlotBackColor(xPos, yPos, 7, this.getArmorSlotBackColor(EnumGuiArmorButton.CHEST, lbox));
			this.drawArmorSlotBackColor(xPos, yPos, 9, this.getArmorSlotBackColor(EnumGuiArmorButton.LEGS, lbox));
			this.drawArmorSlotBackColor(xPos, yPos, 11, this.getArmorSlotBackColor(EnumGuiArmorButton.FEET, lbox));
			
		}

		//アーマー選択画面の文字が2行目以降おかしくなる
		//enableAlpha担っている場合文字が正常に表示されないみたい
		GlStateManager.disableAlpha();
		GL11.glDisable(GL11.GL_BLEND);

		//文字表示
		this.owner.drawString(this.owner.mc.fontRenderer, lbox.getTextureModelName(),
				xPos + 207 - mc.fontRenderer.getStringWidth(lbox.getTextureModelName()), yPos + 25, -1);
		
		//位置調整
		GL11.glTranslatef(xPos + 8F, yPos + 25F, 50F);
		GL11.glScalef(12F, -12F, 12F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		
		//リトルメイドの向き調整
		this.entity.renderYawOffset = 30F;
		this.entity.rotationYawHead = 15F;
		
		if (!isArmorMode) {
			//リトルメイドスロット
			this.entity.setTextureLittleMaid(lbox);
			// テクスチャ表示
			for (int li = 0; li < 16; li++) {
				GL11.glTranslatef(1F, 0, 0);
				if (hasColorContract(lbox, li, this.targetContract)) {
					this.entity.setTextureLittleMaidColor(li, this.targetContract);
					Minecraft.getMinecraft().getRenderManager().renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
				}
			}
		} else {
			
			//防具スロット
			GL11.glTranslatef(1f, 0.25F, 0f);
			GL11.glTranslatef(2.0F, 0, 0);
			
			//アーマーALL
			this.entity.setTextureArmor(lbox, lbox, lbox, lbox);
			Minecraft.getMinecraft().getRenderManager().renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);

			GL11.glTranslatef(1.0F, 0, 0);
			
			//各パーツ
			GL11.glTranslatef(2.0F, 0, 0);
			this.entity.setTextureArmor(lbox, null, null, null);
			Minecraft.getMinecraft().getRenderManager().renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			GL11.glTranslatef(2.0F, 0, 0);
			this.entity.setTextureArmor(null, lbox, null, null);
			Minecraft.getMinecraft().getRenderManager().renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			GL11.glTranslatef(2.0F, 0, 0);
			this.entity.setTextureArmor(null, null, lbox, null);
			Minecraft.getMinecraft().getRenderManager().renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			GL11.glTranslatef(2.0F, 0, 0);
			this.entity.setTextureArmor(null, null, null, lbox);
			Minecraft.getMinecraft().getRenderManager().renderEntity(this.entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		}
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glPopMatrix();
	}
	
	/**
	 * 現在の選択防具と表示行のテクスチャをもとに背景色を判断する
	 * @param armorMode アーマー部位
	 * @param textureBox　現在描画予定のTextureBox
	 * @return
	 */
	private int getArmorSlotBackColor(EnumGuiArmorButton armorMode, LMTextureBox textureBox) {
		
		int colorType = 2;
		
		//現在のモードを取得する
		EnumGuiArmorButton guiArmorMode = this.owner.getGuiArmorButtonMode();
		
		//現在の選択防具かの判断
		if (EnumGuiArmorButton.ALL != armorMode
				&& this.getTextureBoxFromSlot(this.selTextureArmors.get(armorMode.getSlot())) == textureBox) {
			//赤色
			colorType = 0;

		//現在のモード選択判断
		} else if (armorMode == guiArmorMode) {
			//緑
			colorType = 1;
		
		//通常背景色
		} else {
			//灰色
			colorType = 2;
		}
		
		return colorType;
	}
	
	/**
	 * アーマースロットの背景色を塗る
	 * @param xPos
	 * @param yPos
	 * @param no 0-15
	 * @param colorType 0:赤色 1:緑 2:灰色
	 */
	private void drawArmorSlotBackColor(int xPos, int yPos, int no, int colorType) {
		int li = no;
		int lx = xPos + 15 + 12 * li;
		int ly = yPos - 2;
		if (colorType == 0) {
			//赤
			Gui.drawRect(lx, ly, lx + 11, ly + 36, 0x88882222);
		} else if (colorType == 1) {
			//緑
			Gui.drawRect(lx, ly, lx + 11, ly + 36, 0x88226622);
		} else if (colorType == 2) {
			//灰
			Gui.drawRect(lx, ly, lx + 11, ly + 36, 0x66888888);
		}
	}
	
	/**
	 * 指定されたスロット番号のTexutreBoxを取得する
	 * @param slotIndex
	 * @return
	 */
	private LMTextureBox getTextureBoxFromSlot(int slotIndex) {
		return this.isArmorMode ? 
				this.indexArmor.get(slotIndex) : this.indexTexture.get(slotIndex);
	}
	
	/**
	 * 選択しているリトルメイド/アーマーモデルを描画用Entityに設定する
	 */
	public void setTextureSelectedBox() {
		if (!this.isArmorMode) {
			//リトルメイドモデル
			this.entity.setTextureLittleMaid(this.getTextureBoxFromSlot(this.selTextureLittleMaid));
			this.entity.setTextureLittleMaidColor(this.selectColor, this.targetContract);
		} else {
			//アーマーモデル
			this.entity.setTextureArmor(this.getTextureBoxFromSlot(this.selTextureArmors.get(EntityEquipmentSlot.HEAD)),
					this.getTextureBoxFromSlot(this.selTextureArmors.get(EntityEquipmentSlot.CHEST)),
					this.getTextureBoxFromSlot(this.selTextureArmors.get(EntityEquipmentSlot.LEGS)),
					this.getTextureBoxFromSlot(this.selTextureArmors.get(EntityEquipmentSlot.FEET)));
		}
		
	}
	
	/**
	 * スクロール制御を含めたモード変更
	 * @param pFlag
	 */
	public void setArmorMode(boolean pFlag) {
		scrollBy(slotHeight * -getSize());
		if (pFlag) {
			this.nowSelected = this.selTextureArmors.get(this.owner.getGuiArmorButtonMode().getSlot());
			this.isArmorMode = true;
		} else {
			this.nowSelected = this.selTextureLittleMaid;
			this.isArmorMode = false;
		}
		scrollBy(slotHeight * nowSelected);
	}
	
	/**
	 * 選択位置を設定する
	 * @param pIndex
	 */
	public void setSelectedBoxArmor(EntityEquipmentSlot slot) {
		String modelName = this.getTextureBoxFromSlot(this.selTextureArmors.get(slot)).getTextureModelName();
		int idx = 0;
		for (LMTextureBox box : indexArmor) {
			if (box.getTextureModelName().equals(modelName)) {
				break;
			}
			idx++;
		}
		scrollBy(slotHeight * -getSize());
		nowSelected = idx;
		scrollBy(slotHeight * nowSelected);
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
	
	
	/**
	 * 選択中のリトルメイドモデル名を取得する
	 * @return
	 */
	public String getTextureLittleMaid() {
		return this.getTextureBoxFromSlot(this.selTextureLittleMaid).getTextureModelName();
	}
	
	/**
	 * 選択中のリトルメイドカラーを取得する
	 * @return
	 */
	public int getTextureLittleMaidColor() {
		return this.selectColor;
	}
	
	/**
	 * 選択中のアーマーモデルを取得する
	 * @return
	 */
	public String getTextureArmor(EntityEquipmentSlot slot) {
		return this.getTextureBoxFromSlot(this.selTextureArmors.get(slot)).getTextureModelName();
	} 

}
