package com.enclave.enclavemod.items;

import net.minecraft.item.ItemHoe;

public class ItemToolHoe extends ItemHoe {
    public ItemToolHoe(String name, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
}
