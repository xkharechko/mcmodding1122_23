package com.enclave.enclavemod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHollow extends Container {
    private final IInventory hollowInventory;

    public ContainerHollow(InventoryPlayer playerInventory, IInventory hollowInventory, EntityPlayer player)
    {
        this.hollowInventory = hollowInventory;

        hollowInventory.openInventory(player);

        int slotIndex = 0;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlotToContainer(new Slot(hollowInventory, slotIndex++,
                        62 + col * 18, 24 + row * 18));
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInventory, col,
                    8 + col * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.hollowInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            int containerSlots = this.hollowInventory.getSizeInventory();

            if (index < containerSlots) {
                if (!this.mergeItemStack(stack, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.mergeItemStack(stack, 0, containerSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.hollowInventory.closeInventory(playerIn);
    }

    public IInventory getHollowInventory()
    {
        return this.hollowInventory;
    }
}
