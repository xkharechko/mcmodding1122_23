package com.enclave.enclavemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SuperStoneBlock extends Block {
    public static final PropertyBool UPPER = PropertyBool.create("upper");

    public SuperStoneBlock(String name) {
        super(Material.ROCK);
        this.setUnlocalizedName("enclavemod." + name);
        this.setRegistryName("enclavemod", name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UPPER, false));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return state.withProperty(UPPER, Boolean.valueOf(block != Blocks.AIR));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {UPPER});
    }
}
