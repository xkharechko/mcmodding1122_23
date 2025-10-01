package com.enclave.enclavemod.blocks.tile;

import com.enclave.enclavemod.blocks.block.BaseBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockTileEntity<T extends TileEntity> extends BaseBlock {
    public BlockTileEntity(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    public abstract Class<T> getTileEntityClass();

    public T getTileEntity(IBlockAccess world, BlockPos pos) {
        return (T) world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract T createTileEntity(World world, IBlockState state);
}
