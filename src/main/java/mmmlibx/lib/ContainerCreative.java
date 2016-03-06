package mmmlibx.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCreative extends Container
{
	private static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
    /** the list of items in this container */
    public List<?> itemList = new ArrayList<Object>();
    public ContainerCreative(EntityPlayer par1EntityPlayer)
    {
        InventoryPlayer inventoryplayer = par1EntityPlayer.inventory;
        int i;

        for (i = 0; i < 5; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryplayer, i, 9 + i * 18, 112));
        }

        this.scrollTo(0.0F);
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Updates the gui slots ItemStack's based on scroll position.
     */
    public void scrollTo(float p_148329_1_)
    {
        int i = this.itemList.size() / 9 - 5 + 1;
        int j = (int)(p_148329_1_ * i + 0.5D);

        if (j < 0)
        {
            j = 0;
        }

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 9; ++l)
            {
                int i1 = l + (k + j) * 9;

                if (i1 >= 0 && i1 < this.itemList.size())
                {
                    field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i1));
                }
                else
                {
                    field_147060_v.setInventorySlotContents(l + k * 9, (ItemStack)null);
                }
            }
        }
    }

    public boolean func_148328_e()
    {
        return this.itemList.size() > 45;
    }

    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {}

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does
     * that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        if (par2 >= this.inventorySlots.size() - 9 && par2 < this.inventorySlots.size())
        {
            Slot slot = (Slot)this.inventorySlots.get(par2);

            if (slot != null && slot.getHasStack())
            {
                slot.putStack((ItemStack)null);
            }
        }

        return null;
    }

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return par2Slot.yDisplayPosition > 90;
    }

    /**
     * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to
     * check if the slot can be added to a list of Slots to split the held ItemStack across.
     */
    public boolean canDragIntoSlot(Slot par1Slot)
    {
        return par1Slot.inventory instanceof InventoryPlayer || par1Slot.yDisplayPosition > 90 && par1Slot.xDisplayPosition <= 162;
    }
}
