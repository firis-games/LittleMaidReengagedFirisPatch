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
public class LMGuiPartsTextureSlot extends GuiSlot {

	public EntityLittleMaidGui entity;

	protected LMGuiTextureSelect owner;
	protected List<LMTextureBox> indexTexture;
	protected List<LMTextureBox> indexArmor;
	
	/**
	 * 選択モード
	 */
	protected boolean isArmorMode;
//	public int texsel[] = new int[2];
	
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
	
	
//	private ItemStack armors[] = new ItemStack[] {
//			new ItemStack(Items.LEATHER_BOOTS),
//			new ItemStack(Items.LEATHER_LEGGINGS),
//			new ItemStack(Items.LEATHER_CHESTPLATE),
//			new ItemStack(Items.LEATHER_HELMET)
//	};
//	protected boolean isContract;
//	protected static LMTextureBox blankBox;


	/**
	 * コンストラクタ
	 * @param pOwner
	 */
	public LMGuiPartsTextureSlot(LMGuiTextureSelect owner, IGuiTextureSelect target) {
		super(owner.mc, owner.width, owner.height, 16, owner.height - 64, 36);
		
		this.owner = owner;
		this.entity = new EntityLittleMaidGui(owner.mc.world);
//		color = owner.target.getColor();
		
		//ターゲット初期値設定
		this.targetColor = target.getTextureColor();
		this.targetContract = target.getTextureContract();
		
//		blankBox = new LMTextureBox();
//		blankBox.models = new ModelMultiBase[] {null, null, null};

//		texsel[0] = 0;//-1;
//		texsel[1] = 0;//-1;
		
		//選択位置初期化
		this.selTextureLittleMaid = 0;
		this.selTextureArmors.put(EntityEquipmentSlot.HEAD, 0);
		this.selTextureArmors.put(EntityEquipmentSlot.CHEST, 0);
		this.selTextureArmors.put(EntityEquipmentSlot.LEGS, 0);
		this.selTextureArmors.put(EntityEquipmentSlot.FEET, 0);
		this.mouseoverColor = -1;
		
//		isContract = owner.target.isContract();
//		entity.getModelConfigCompound().setContract(isContract);
//		LMTextureBox ltbox[] = owner.target.getModelConfigCompound().getLMTextureBox();
//		LMTextureBox ltboxLittleMaid = owner.target.getModelConfigCompound().getTextureBoxLittleMaid();
//		LMTextureBox ltboxLittleMaid = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(owner.target.getTextureLittleMaid());
//		LMTextureBox ltboxArmor = owner.target.getModelConfigCompound().getTextureBoxArmorAll();
//		LMTextureBox ltboxArmor = LMLibraryAPI.instance().getTextureManager().getLMTextureBox(owner.target.getTextureArmor(EntityEquipmentSlot.HEAD));
		
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

//	public static LMTextureBox getBlankBox() {
//		return blankBox;
//	}

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
			LMTextureBox lbox = getSlotTextureBox(slotIndex);
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

	@Override
	protected boolean isSelected(int slotIndex) {
		return this.nowSelected == slotIndex;
	}

	@Override
	protected void drawBackground() {
	}
	
	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		
		//表示対象判断
		if (!isSlotView(slotIndex)) return;
		
		GL11.glPushMatrix();
		
		//テクスチャ取得
		LMTextureBox lbox = this.getSlotTextureBox(slotIndex);
		if (isArmorMode) {
//			lbox = indexArmor.get(slotIndex);
//			entity.getModelConfigCompound().setTextureBoxLittleMaid(null);
//			entity.getModelConfigCompound().setTextureBoxArmorAll(lbox);
			entity.setTextureArmor(lbox, lbox, lbox, lbox);
		} else {
//			lbox = indexTexture.get(slotIndex);
//			entity.getModelConfigCompound().setTextureBoxLittleMaid(lbox);
//			entity.getModelConfigCompound().setTextureBoxArmorAll(null);
			entity.setTextureLittleMaid(lbox);
		}

		//マウスの位置を計算
		this.mouseoverColor = (byte)((this.mouseX - (xPos + 15)) / 12);
		if ((this.mouseoverColor < 0) && (this.mouseoverColor > 15)) {
			this.mouseoverColor = -1;
		}
		
		if (!isArmorMode) {
			for (int li = 0; li < 16; li++) {
				int lx = xPos + 15 + 12 * li;
//				mouseoverColor = (byte)((mouseX - (xPos + 15)) / 12);
//				if ((mouseoverColor < 0) && (mouseoverColor > 15)) {
//					mouseoverColor = -1;
//				}
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
		//		MMM_TextureManager.instance.checkLMTextureBoxServer(lbox);
		GL11.glDisable(GL11.GL_BLEND);

		//文字表示
		this.owner.drawString(this.owner.mc.fontRenderer, lbox.getTextureModelName(),
				xPos + 207 - mc.fontRenderer.getStringWidth(lbox.getTextureModelName()), yPos + 25, -1);
		
		//位置調整
		GL11.glTranslatef(xPos + 8F, yPos + 25F, 50F);
		GL11.glScalef(12F, -12F, 12F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
//		entity.modeArmor = mode;

		//リトルメイドの向き調整
		this.entity.renderYawOffset = 30F;
		this.entity.rotationYawHead = 15F;

		if (isArmorMode) {
			//デフォルトアーマー
			GL11.glTranslatef(1f, 0.25F, 0f);
			
			GL11.glTranslatef(2.0F, 0, 0);
			
//			entity.setTextureNames("default");
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//			RendererHelper.setLightmapTextureCoords(0x00f0);//61680

			//GL11.glScalef(0.8F, 0.8F, 0.8F);
			
			GL11.glTranslatef(1.0F, 0, 0);
			
			//各パーツ
			entity.setTextureArmor(lbox, null, null, null);
			GL11.glTranslatef(2.0F, 0, 0);
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			entity.setTextureArmor(null, lbox, null, null);
			GL11.glTranslatef(2.0F, 0, 0);
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			entity.setTextureArmor(null, null, lbox, null);
			GL11.glTranslatef(2.0F, 0, 0);
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			entity.setTextureArmor(null, null, null, lbox);
			GL11.glTranslatef(2.0F, 0, 0);
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
			
			
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
				
				if (hasColorContract(lbox, li, this.targetContract)) {
//					entity.getModelConfigCompound().setColor(li);
//					entity.getModelConfigCompound().setContract(isContract);
					entity.setTextureLittleMaidColor(li, this.targetContract);
					
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

//	public LMTextureBox getSelectedBox() {
//		return getSelectedBox(nowSelected);
//	}

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
				&& this.getSlotTextureBox(this.selTextureArmors.get(armorMode.getSlot())) == textureBox) {
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
	protected LMTextureBox getSlotTextureBox(int slotIndex) {
		return this.isArmorMode ? 
				this.indexArmor.get(slotIndex) : this.indexTexture.get(slotIndex);
	}
	
	/**
	 * 選択しているリトルメイド/アーマーモデルを描画用Entityに設定する
	 */
	public void setTextureSelectedBox() {
		if (!this.isArmorMode) {
			//リトルメイドモデル
			this.entity.setTextureLittleMaid(this.getSlotTextureBox(this.selTextureLittleMaid));
			this.entity.setTextureLittleMaidColor(this.selectColor, this.targetContract);
		} else {
			//アーマーモデル
			this.entity.setTextureArmor(this.getSlotTextureBox(this.selTextureArmors.get(EntityEquipmentSlot.HEAD)),
					this.getSlotTextureBox(this.selTextureArmors.get(EntityEquipmentSlot.CHEST)),
					this.getSlotTextureBox(this.selTextureArmors.get(EntityEquipmentSlot.LEGS)),
					this.getSlotTextureBox(this.selTextureArmors.get(EntityEquipmentSlot.FEET)));
		}
		
	}

//	public LMTextureBox getSelectedBox(boolean pMode) {
//		return pMode ? indexArmor.get(texsel[1]) : indexTexture.get(texsel[0]);
//	}

	/**
	 * スクロール制御を含めたモード変更
	 * @param pFlag
	 */
	public void setArmorMode(boolean pFlag) {
		scrollBy(slotHeight * -getSize());
//		entity.modeArmor = pFlag;
		if (pFlag) {
			this.nowSelected = this.selTextureArmors.get(this.owner.getGuiArmorButtonMode().getSlot());
			this.isArmorMode = true;
//			entity.setItemStackToSlot(EntityEquipmentSlot.FEET,  armors[0]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS,  armors[1]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, armors[2]);
//			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD,  armors[3]);
		} else {
			this.nowSelected = this.selTextureLittleMaid;
			this.isArmorMode = false;
//			entity.setItemStackToSlot(EntityEquipmentSlot.FEET,  ItemStack.EMPTY);
//			entity.setItemStackToSlot(EntityEquipmentSlot.LEGS,  ItemStack.EMPTY);
//			entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
//			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD,  ItemStack.EMPTY);
		}
		scrollBy(slotHeight * nowSelected);
	}
	
	/**
	 * 選択位置を設定する
	 * @param pIndex
	 */
	public void setSelectedBoxArmor(EntityEquipmentSlot slot) {
		
		String modelName = this.getSlotTextureBox(this.selTextureArmors.get(slot)).getTextureModelName();
		
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
		return this.getSlotTextureBox(this.selTextureLittleMaid).getTextureModelName();
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
		return this.getSlotTextureBox(this.selTextureArmors.get(slot)).getTextureModelName();
	} 

}
