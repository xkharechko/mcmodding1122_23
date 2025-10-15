package com.enclave.enclavemod.entity.ai.courier.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FarmlandRowScanner {
    private int rowStartX, rowStartZ, rowEndX, rowEndZ;

    public void defineRowCoordinates(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        this.rowStartX = this.rowEndX = x;
        this.rowStartZ = this.rowEndZ = z;

        for (int i = z - 1; i >= z - 10; i--) {
            if (isRow(world, x, y, i)) {
                this.rowStartZ = i;
            } else {
                break;
            }
        }

        for (int i = z + 1; i <= z + 10; i++) {
            if (isRow(world, x, y, i)) {
                this.rowEndZ = i;
            } else {
                break;
            }
        }

        for (int i = x - 1; i >= x - 10; i--) {
            if (isRow(world, i, y, z)) {
                this.rowStartX = i;
            } else {
                break;
            }
        }

        for (int i = x + 1; i <= x + 10; i++) {
            if (isRow(world, i, y, z)) {
                this.rowEndX = i;
            } else {
                break;
            }
        }

        System.out.println("rowStartX: " + rowStartX + ", rowEndX: " + rowEndX + ", rowStartZ: " + rowStartZ + ", rowEndZ: " + rowEndZ);
    }

    private boolean isRow(World world, int x, int y, int z) {
        Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        return (block instanceof BlockFarmland) || (block instanceof BlockDirt);
    }

    public int getRowStartX() {
        return this.rowStartX;
    }

    public int getRowStartZ() {
        return this.rowStartZ;
    }

    public int getRowEndX() {
        return this.rowEndX;
    }

    public int getRowEndZ() {
        return this.rowEndZ;
    }
}
