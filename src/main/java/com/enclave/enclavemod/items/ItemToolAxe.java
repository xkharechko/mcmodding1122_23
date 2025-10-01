package com.enclave.enclavemod.items;

import net.minecraft.item.ItemAxe;

public class ItemToolAxe extends ItemAxe {
    public ItemToolAxe(String name, ToolMaterial material) {
        super(material, 15F, 28F);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
}
