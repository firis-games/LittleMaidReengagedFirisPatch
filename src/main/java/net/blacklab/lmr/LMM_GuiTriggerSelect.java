package net.blacklab.lmr;

import java.io.IOException;
import java.util.List;

import mmmlibx.lib.Client;
import mmmlibx.lib.MMM_Helper;
import net.blacklab.lmr.client.gui.GuiIFF;
import net.blacklab.lmr.entity.EntityLittleMaid;
import net.blacklab.lmr.util.TriggerSelect;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class LMM_GuiTriggerSelect extends GuiContainer {

	protected float scrolleWeaponset;
	protected float scrolleContainer;
	private static InventoryBasic inventory1 = new InventoryBasic("tmpsel", false, 40);
	private static InventoryBasic inventory2 = new InventoryBasic("tmpwep", false, 32);
	private int lastY;
	private boolean ismousePress;
	private int isScrolled;
	public GuiIFF owner;
	private GuiButton[] guiButton = new GuiButton[3];
	private LMM_ContainerTriggerSelect inventoryTrigger;
	private int selectPage;
	protected EntityPlayer target;
	protected static final ResourceLocation fguiTex =
			new ResourceLocation(LittleMaidReengaged.DOMAIN, "textures/gui/container/littlemaidtrigger.png");


	public LMM_GuiTriggerSelect(EntityPlayer entityplayer, GuiIFF guiowner) {
		super(new LMM_ContainerTriggerSelect(entityplayer));
		ySize = 216;
		owner = guiowner;
		inventoryTrigger = (LMM_ContainerTriggerSelect) inventorySlots;
		target = entityplayer;
	}
	
	public LMM_GuiTriggerSelect(World world, EntityPlayer entityPlayer, EntityLittleMaid maid) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(entityPlayer, null);
		owner = new GuiIFF(world, entityPlayer, maid);
	}

	@Override
	public void initGui() {
		super.initGui();

		guiButton[0] = new GuiButton(100, guiLeft + 7, guiTop + 193, 20, 20, "<");
		guiButton[1] = new GuiButton(101, guiLeft + 35, guiTop + 193, 106, 20, TriggerSelect.selector.get(0));
		guiButton[2] = new GuiButton(102, guiLeft + 149, guiTop + 193, 20, 20, ">");
		buttonList.add(guiButton[0]);
		buttonList.add(guiButton[1]);
		buttonList.add(guiButton[2]);
		guiButton[1].enabled = false;
		selectPage = 0;
	}

//	@Override
//	protected void keyTyped(char c, int i) {
//		if (i == 1) {
//			mc.displayGuiScreen(owner);
//		}
//	}

	@Override
	public void onGuiClosed() {
		// 設定値のデコード
		setItemList();

		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		setItemList();
		if (guibutton.id == 100) {
			if (--selectPage < 0) {
				selectPage = TriggerSelect.selector.size() - 1;
			}
		}
		if (guibutton.id == 101) {
			// Sword Select
		}
		if (guibutton.id == 102) {
			if (++selectPage >= TriggerSelect.selector.size()) {
				selectPage = 0;
			}
		}
		String ls = TriggerSelect.selector.get(selectPage);
		guiButton[1].displayString = ls;
		inventoryTrigger.setWeaponSelect(MMM_Helper.getPlayerName(target), ls);
		inventoryTrigger.setWeaponlist(0.0F);
	}

	@Override
	protected void handleMouseClick(Slot slot, int i, int j, int flag) {
		flag = i == -999 && flag == 0 ? 4 : flag;
		if (slot != null) {
			if (slot.inventory == inventory1 && flag == 0) {
				InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
				ItemStack itemstack1 = inventoryplayer.getItemStack();
				ItemStack itemstack4 = slot.getStack();
				if (itemstack1 != null && itemstack4 != null
						&& itemstack1.getItem() == itemstack4.getItem()) {
					// 選択アイテムが空ではない時
					if (j != 0) {
						inventoryplayer.setItemStack(null);
					}
				} else if (itemstack1 != null) {
					inventoryplayer.setItemStack(null);
				} else if (itemstack4 == null) {
					inventoryplayer.setItemStack(null);
				} else {
					inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack4));
				}
			} else {
				inventorySlots.slotClick(slot.slotNumber, j, flag, mc.thePlayer);
				ItemStack itemstack = inventorySlots.getSlot(slot.slotNumber).getStack();
				LittleMaidReengaged.Debug("SLOT ITEM %d/%s", slot.slotNumber, itemstack!=null?itemstack.getItem().getUnlocalizedName():"null");
				mc.playerController.sendSlotPacket(itemstack,(slot.slotNumber - inventorySlots.inventorySlots.size()) + 9 + 36);
			}
		} else {
			// Slot以外のところは捨てる
			InventoryPlayer inventoryplayer1 = mc.thePlayer.inventory;
			inventoryplayer1.setItemStack(null);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		mc.fontRendererObj.drawString("Item selection", 8, 6, 0x404040);
		mc.fontRendererObj.drawString("Trigger Items", 8, 110, 0x404040);
	}

	@Override
	public void handleMouseInput() {
		try {
			super.handleMouseInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (lastY < height / 2) {
				int j = (inventoryTrigger.itemList.size() / 8 - 5) + 1;
				if (i > 0) {
					i = 1;
				}
				if (i < 0) {
					i = -1;
				}
				scrolleContainer -= (double) i / (double) j;
				if (scrolleContainer < 0.0F) {
					scrolleContainer = 0.0F;
				}
				if (scrolleContainer > 1.0F) {
					scrolleContainer = 1.0F;
				}
				inventoryTrigger.scrollTo(scrolleContainer);
			} else {
				int j = (inventoryTrigger.weaponSelect.size() / 8 - 4) + 1;
				if (i > 0) {
					i = 1;
				}
				if (i < 0) {
					i = -1;
				}
				if (j > 0) {
					scrolleWeaponset -= (double) i / (double) j;
				} else {
					scrolleWeaponset = 0.0F;
				}
				if (scrolleWeaponset < 0.0F) {
					scrolleWeaponset = 0.0F;
				}
				if (scrolleWeaponset > 1.0F) {
					scrolleWeaponset = 1.0F;
				}
				inventoryTrigger.setWeaponlist(scrolleWeaponset);
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		lastY = j;
		boolean flag = Mouse.isButtonDown(0);
		int k = guiLeft;
		int l = guiTop;
		int i1 = k + 155;
		int j1 = l + 17;
		int k1 = i1 + 14;
		int l1 = j1 + 90;
		if (!flag) {
			isScrolled = 0;
		}
		if (!ismousePress && flag && i >= i1 && j >= j1 && i < k1 && j < l1) {
			isScrolled = 1;
		}
		if (isScrolled == 1) {
			scrolleContainer = (j - (j1 + 8)) / (l1 - j1 - 16F);
			if (scrolleContainer < 0.0F) {
				scrolleContainer = 0.0F;
			}
			if (scrolleContainer > 1.0F) {
				scrolleContainer = 1.0F;
			}
			inventoryTrigger.scrollTo(scrolleContainer);
		}
		j1 = l + 120;
		l1 = j1 + 72;
		if (!ismousePress && flag && i >= i1 && j >= j1 && i < k1 && j < l1) {
			isScrolled = 2;
		}
		if (isScrolled == 2) {
			scrolleWeaponset = (j - (j1 + 8)) / (l1 - j1 - 16F);
			if (scrolleWeaponset < 0.0F) {
				scrolleWeaponset = 0.0F;
			}
			if (scrolleWeaponset > 1.0F) {
				scrolleWeaponset = 1.0F;
			}
			inventoryTrigger.setWeaponlist(scrolleWeaponset);
		}
		ismousePress = flag;
		super.drawScreen(i, j, f);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(2896 /* GL_LIGHTING */);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Client.setTexture(fguiTex);
		int l = guiLeft;
		int i1 = guiTop;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		int k1 = i1 + 17;
		int l1 = k1 + 88 + 2;
		// scrolleWeaponset = 1.0F;
		// scrolleContainer = 0.5F;
		drawTexturedModalRect(l + 154,
				i1 + 17 + (int) ((l1 - k1 - 17) * scrolleContainer),
				176, 0, 16, 16);
		drawTexturedModalRect(l + 154,
				i1 + 120 + (int) ((l1 - k1 - 35) * scrolleWeaponset),
				176, 0, 16, 16);
	}

	private void setItemList() {
		List list1 = inventoryTrigger.getItemList();
		list1.clear();
		for (int i = 0; i < inventoryTrigger.weaponSelect.size(); i++) {
			ItemStack is = inventoryTrigger.weaponSelect.get(i);
			if (is != null && !list1.contains(is.getItem())) {
				list1.add(is.getItem());
			}
		}
	}

	public static InventoryBasic getInventory1() {
		return inventory1;
	}

	public static InventoryBasic getInventory2() {
		return inventory2;
	}

}
