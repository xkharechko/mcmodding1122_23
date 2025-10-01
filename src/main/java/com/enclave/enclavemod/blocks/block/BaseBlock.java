package com.enclave.enclavemod.blocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BaseBlock extends Block {
    public BaseBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(material);
        this.setRegistryName("enclavemod", name);
        this.setUnlocalizedName("enclavemod." + name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);
    }
}
