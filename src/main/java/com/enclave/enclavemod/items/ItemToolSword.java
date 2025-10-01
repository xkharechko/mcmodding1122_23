package com.enclave.enclavemod.items;

import net.minecraft.item.ItemSword;

public class ItemToolSword extends ItemSword {
    public ItemToolSword(String name, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
}
