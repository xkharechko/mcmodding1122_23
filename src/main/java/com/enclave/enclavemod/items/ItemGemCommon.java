package com.enclave.enclavemod.items;

import com.enclave.enclavemod.EnclaveMod;
import net.minecraft.item.Item;

public class ItemGemCommon extends Item {
    public ItemGemCommon(String name, int maxStackSize) {
        this.setRegistryName(EnclaveMod.MODID, name);
        this.setUnlocalizedName(EnclaveMod.MODID + "." + name);
        this.setMaxStackSize(maxStackSize);
    }
}
