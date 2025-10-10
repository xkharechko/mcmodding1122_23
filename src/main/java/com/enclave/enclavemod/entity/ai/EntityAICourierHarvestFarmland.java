package com.enclave.enclavemod.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAICourierHarvestFarmland extends EntityAIMoveToBlock {
    private final EntityCreature entity;
    private int currentTask; // 0 => move to row, 1 => harvest all food in row, -1 => none
    private int minX, minZ, maxX, maxZ;
    private boolean isHarvestEnded = false;

    public EntityAICourierHarvestFarmland(EntityCreature entity, double speedIn) {
        super(entity, speedIn, 16);
        this.entity = entity;
    }

    public boolean shouldExecute()
    {
        if (this.runDelay <= 0 && this.isHarvestEnded)
        {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity))
            {
                return false;
            }

            this.currentTask = -1;
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting()
    {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
    }

    public void updateTask()
    {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (this.getIsAboveDestination() && this.currentTask == 0) {
            defineRowCoordinates(this.entity.world, this.destinationBlock);
            this.currentTask = 1;
            this.runDelay = 1;
        }

        if (this.getIsAboveDestination() && this.currentTask == 1) {
            this.isHarvestEnded = false;
            World world = this.entity.world;
            BlockPos blockpos = this.destinationBlock.up();
            int x = blockpos.getX();
            int y = blockpos.getY();
            int z = blockpos.getZ();
            BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            boolean foundCrop = false;

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) {
                world.destroyBlock(blockpos, false);
            }

            for (int xi = this.minX; xi <= this.maxX && !foundCrop; xi++) {
                for (int zi = this.minZ; zi <= this.maxZ; zi++) {
                    if (xi == x && zi == z) continue;

                    checkPos.setPos(xi, y, zi);
                    iblockstate = world.getBlockState(checkPos);
                    block = iblockstate.getBlock();
                    if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) {
                        foundCrop = true;
                        this.runDelay = 1;
                        break;
                    }
                }
            }

            if (!foundCrop) {
                this.runDelay = 100;
                this.isHarvestEnded = true;
                System.out.println("goes in cooldown");
            }
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND && this.currentTask <= 0) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) && (this.currentTask == 0 || this.currentTask < 0))
            {
                this.currentTask = 0;
                return true;
            }
        }

        if (block == Blocks.FARMLAND && this.currentTask == 1) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            int x = pos.getX();
            int z = pos.getZ();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) {
                if (x <= this.maxX && x >= this.minX && z <= this.maxZ && z >= this.minZ) {
                    this.currentTask = 1;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isRow(World world, int x, int y, int z) {
        Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        return (block instanceof BlockFarmland) || (block instanceof BlockDirt);
    }

    private void defineRowCoordinates(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        this.minX = this.maxX = x;
        this.minZ = this.maxZ = z;

        for (int i = z - 1; i >= z - 10; i--) {
            if (isRow(world, x, y, i)) {
                this.minZ = i;
            } else {
                break;
            }
        }

        for (int i = z + 1; i <= z + 10; i++) {
            if (isRow(world, x, y, i)) {
                this.maxZ = i;
            } else {
                break;
            }
        }

        for (int i = x - 1; i >= x - 10; i--) {
            if (isRow(world, i, y, z)) {
                this.minX = i;
            } else {
                break;
            }
        }

        for (int i = x + 1; i <= x + 10; i++) {
            if (isRow(world, i, y, z)) {
                this.maxX = i;
            } else {
                break;
            }
        }

        System.out.println("minX: " + minX + ", maxX: " + maxX + ", minZ: " + minZ + ", maxZ: " + maxZ);
    }
}