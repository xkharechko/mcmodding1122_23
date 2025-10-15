package com.enclave.enclavemod.entity.ai.courier.inventory;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class CourierInventory {
    private final ArrayList<ItemStack> items = new ArrayList<>();

    public void add(ItemStack item) {
        if (item != null && !item.isEmpty()) items.add(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ItemStack takeOne() {
        return items.isEmpty() ? ItemStack.EMPTY : items.remove(0);
    }

    public ArrayList<ItemStack> getInventoryItems() {
        return new ArrayList<>(items);
    }
}
