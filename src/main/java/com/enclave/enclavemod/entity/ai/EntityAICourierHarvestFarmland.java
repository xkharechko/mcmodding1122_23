package com.enclave.enclavemod.entity.ai;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EntityAICourierHarvestFarmland extends EntityAIMoveToBlock {
    private final EntityCreature entity;
    private int currentTask; // 0 => move to row, 1 => harvest all food in row, 2 => delivery food, -1 => none
    private int minX, minZ, maxX, maxZ;
    private boolean isHarvestEnded = false;
    private ArrayList<ItemStack> collectedFood = new ArrayList<>();
    private HashSet<BlockPos> visitedDoors = new HashSet<>();

    public EntityAICourierHarvestFarmland(EntityCreature entity, double speedIn) {
        super(entity, speedIn, 16);
        this.entity = entity;
    }

    public boolean shouldExecute()
    {
        if (this.runDelay <= 0 && this.isHarvestEnded) {
            this.currentTask = 2;

            BlockPos doorPos = findNearestDoor();
            if (doorPos != null) {
                this.destinationBlock = doorPos;
                return true;
            } else {
                this.currentTask = -1;
                this.isHarvestEnded = false;
                this.runDelay = 20;
                return false;
            }
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting()
    {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (this.isNearDoor(this.destinationBlock) && this.currentTask == 2) {
            World world = this.entity.world;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = world.getBlockState(blockpos);
            BlockDoor door = (BlockDoor) iblockstate.getBlock();
            door.toggleDoor(world, blockpos, true);
            if (!collectedFood.isEmpty()) {
                ItemStack stack = collectedFood.remove(0);
                if (stack != null && !stack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, blockpos.getX() + 0.5, blockpos.getY() + 1.0, blockpos.getZ() + 0.5, stack.copy()));
                    System.out.println("Spawned " + stack + " at " + blockpos + ": " + world.getBlockState(blockpos).getBlock());
                }
                visitedDoors.add(blockpos);
                if (!collectedFood.isEmpty()) {
                    BlockPos doorPos = findNearestDoor();
                    if (doorPos != null) {
                        this.destinationBlock = doorPos;
                        System.out.println("Found new door");
                        System.out.println("To delivery: " + collectedFood);
                    } else {
                        visitedDoors.clear();
                        doorPos = findNearestDoor();
                        if (doorPos != null) {
                            this.destinationBlock = doorPos;
                            System.out.println("Found new door after visited doors reset");
                            System.out.println("To delivery: " + collectedFood);
                        }
                    }
                }
                this.runDelay = 1;
            } else {
                visitedDoors.clear();
                this.currentTask = -1;
                this.runDelay = 100;
                this.isHarvestEnded = false;
                System.out.println("Finished delivery");
            }
        }

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
                world.destroyBlock(blockpos, true);
                List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox().grow(1.5D));

                for (EntityItem item : items) {
                    collectedFood.add(item.getItem());
                    item.setDead();
                }
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
                this.isHarvestEnded = true;
                this.runDelay = 10;
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

    private boolean isNearDoor(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int entityX = entity.getPosition().getX();
        int entityY = entity.getPosition().getY();
        int entityZ = entity.getPosition().getZ();

        return Math.abs(x - entityX) <= 1 && Math.abs(z - entityZ) <= 1 && Math.abs(y - entityY) <= 1;
    }

    private BlockPos findNearestDoor() {
        BlockPos entity = this.entity.getPosition();
        int range = 32;
        BlockPos best = null;
        double bestDistSq = Double.MAX_VALUE;

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                zcheck:
                for (int dz = -range; dz <= range; dz++) {
                    BlockPos p = entity.add(dx, dy, dz);
                    if (this.entity.world.getBlockState(p).getBlock() instanceof BlockDoor) {
                        p = normalizeDoorPos(this.entity.world, p);
                        for (BlockPos pos : visitedDoors) {
                            if (pos.equals(p)) {
                                continue zcheck;
                            }
                        }
                        double d = entity.distanceSq(p);
                        if (d < bestDistSq) {
                            bestDistSq = d;
                            best = p;
                        }
                    }
                }
            }
        }
        return best;
    }

    private BlockPos normalizeDoorPos(World world, BlockPos pos) {
        IBlockState iBlockState = world.getBlockState(pos);
        if (iBlockState.getBlock() instanceof BlockDoor) {
            BlockDoor.EnumDoorHalf half = iBlockState.getValue(BlockDoor.HALF);
            if (half.getName().equals("upper")) {
                return pos.down();
            }
        }
        return pos;
    }
}