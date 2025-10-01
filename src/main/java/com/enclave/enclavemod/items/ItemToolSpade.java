package com.enclave.enclavemod.items;

import net.minecraft.item.ItemSpade;

public class ItemToolSpade extends ItemSpade {
    public ItemToolSpade(String name, ToolMaterial material) {
        super(material);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
    }
}
