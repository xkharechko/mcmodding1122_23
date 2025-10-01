package com.enclave.enclavemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAcorn extends Block {
    protected static final AxisAlignedBB ACORN_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.37D, 0.565D, 0.19D, 0.56D);

    public BlockAcorn(String name) {
        super(Material.WOOD);
        this.setUnlocalizedName("enclavemod." + name);
        this.setRegistryName("enclavemod", name);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return ACORN_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
