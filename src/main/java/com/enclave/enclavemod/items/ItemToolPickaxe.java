package com.enclave.enclavemod.items;

import net.minecraft.item.ItemPickaxe;

public class ItemToolPickaxe extends ItemPickaxe {
    public ItemToolPickaxe(String name, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
}
