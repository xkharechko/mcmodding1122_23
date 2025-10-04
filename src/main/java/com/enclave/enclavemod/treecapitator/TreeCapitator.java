package com.enclave.enclavemod.treecapitator;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class TreeCapitator {
    public void checkAndDestroyWood(World world, BlockPos pos) {
        checkAndDestroyWood(world, pos.getX(), pos.getY(), pos.getZ());
    }

    private void checkAndDestroyWood(World world, int x, int y, int z) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        blockPos.setPos(x + 1, y, z);
        Block blockRight = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z);
        Block blockLeft = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z);
        Block blockUpper = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y + 1, z);
        Block blockUpperRight = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y + 1, z);
        Block blockUpperLeft = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z + 1);
        Block blockUpperFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z - 1);
        Block blockUpperBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y + 1, z + 1);
        Block blockUpperLeftFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y + 1, z - 1);
        Block blockUpperRightBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y + 1, z - 1);
        Block blockUpperRightFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y + 1, z + 1);
        Block blockUpperLeftBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z + 1);
        Block blockLeftFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y, z - 1);
        Block blockRightBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z - 1);
        Block blockRightFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y, z + 1);
        Block blockLeftBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y, z + 1);
        Block blockFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y, z - 1);
        Block blockBack = world.getBlockState(blockPos).getBlock();


        if (blockRight == Blocks.LOG || blockRight == Blocks.LOG2) {
            BlockPos.MutableBlockPos tmp = new BlockPos.MutableBlockPos(x + 1, y, z);
            world.destroyBlock(tmp, true);
            checkAndDestroyWood(world, x + 1, y, z);
        }
        if (blockLeft == Blocks.LOG || blockLeft == Blocks.LOG2) {
            BlockPos.MutableBlockPos tmp = new BlockPos.MutableBlockPos(x - 1, y, z);
            world.destroyBlock(tmp, true);
            checkAndDestroyWood(world, x - 1, y, z);
        }
        if (blockFront == Blocks.LOG || blockFront == Blocks.LOG2) {
            BlockPos.MutableBlockPos tmp = new BlockPos.MutableBlockPos(x, y, z + 1);
            world.destroyBlock(tmp, true);
            checkAndDestroyWood(world, x, y, z + 1);
        }
        if (blockBack == Blocks.LOG || blockBack == Blocks.LOG2) {
            BlockPos.MutableBlockPos tmp = new BlockPos.MutableBlockPos(x, y, z - 1);
            world.destroyBlock(tmp, true);
            checkAndDestroyWood(world, x, y, z - 1);
        }
        if (blockUpper == Blocks.LOG || blockUpper == Blocks.LOG2) {
            BlockPos.MutableBlockPos tmp = new BlockPos.MutableBlockPos(x, y + 1, z);
            world.destroyBlock(tmp, true);
            checkAndDestroyWood(world, x, y + 1, z);
        } else {
            checkAndDestroyLeaves(world, x, y + 1, z);
        }
        if (blockUpperLeftFront == Blocks.LOG || blockUpperLeftFront == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x - 1, y + 1, z + 1), true);
            checkAndDestroyWood(world, x - 1, y + 1, z + 1);
        } else checkAndDestroyLeaves(world, x - 1, y + 1, z + 1);

        if (blockUpperRightBack == Blocks.LOG || blockUpperRightBack == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x + 1, y + 1, z - 1), true);
            checkAndDestroyWood(world, x + 1, y + 1, z - 1);
        } else checkAndDestroyLeaves(world, x + 1, y + 1, z - 1);

        if (blockUpperRightFront == Blocks.LOG || blockUpperRightFront == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x - 1, y + 1, z - 1), true);
            checkAndDestroyWood(world, x - 1, y + 1, z - 1);
        } else checkAndDestroyLeaves(world, x - 1, y + 1, z - 1);

        if (blockUpperLeftBack == Blocks.LOG || blockUpperLeftBack == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x + 1, y + 1, z + 1), true);
            checkAndDestroyWood(world, x + 1, y + 1, z + 1);
        } else checkAndDestroyLeaves(world, x + 1, y + 1, z + 1);

        if (blockLeftFront == Blocks.LOG || blockLeftFront == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x - 1, y, z + 1), true);
            checkAndDestroyWood(world, x - 1, y, z + 1);
        } else checkAndDestroyLeaves(world, x - 1, y, z + 1);

        if (blockRightBack == Blocks.LOG || blockRightBack == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x + 1, y, z - 1), true);
            checkAndDestroyWood(world, x + 1, y, z - 1);
        } else checkAndDestroyLeaves(world, x + 1, y, z - 1);

        if (blockRightFront == Blocks.LOG || blockRightFront == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x - 1, y, z - 1), true);
            checkAndDestroyWood(world, x - 1, y, z - 1);
        } else checkAndDestroyLeaves(world, x - 1, y, z - 1);

        if (blockLeftBack == Blocks.LOG || blockLeftBack == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x + 1, y, z + 1), true);
            checkAndDestroyWood(world, x + 1, y, z + 1);
        } else checkAndDestroyLeaves(world, x + 1, y, z + 1);

        if (blockUpperRight == Blocks.LOG || blockUpperRight == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x + 1, y + 1, z), true);
            checkAndDestroyWood(world, x + 1, y + 1, z);
        } else checkAndDestroyLeaves(world, x + 1, y + 1, z);

        if (blockUpperLeft == Blocks.LOG || blockUpperLeft == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x - 1, y + 1, z), true);
            checkAndDestroyWood(world, x - 1, y + 1, z);
        } else checkAndDestroyLeaves(world, x - 1, y + 1, z);

        if (blockUpperFront == Blocks.LOG || blockUpperFront == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x, y + 1, z + 1), true);
            checkAndDestroyWood(world, x, y + 1, z + 1);
        } else checkAndDestroyLeaves(world, x, y + 1, z + 1);

        if (blockUpperBack == Blocks.LOG || blockUpperBack == Blocks.LOG2) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x, y + 1, z - 1), true);
            checkAndDestroyWood(world, x, y + 1, z - 1);
        } else checkAndDestroyLeaves(world, x, y + 1, z - 1);
    }

    public void checkAndDestroyLeaves(World world, BlockPos pos) {
        checkAndDestroyLeaves(world, pos.getX(), pos.getY(), pos.getZ());
    }

    private void checkAndDestroyLeaves(World world, int x, int y, int z) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        blockPos.setPos(x + 1, y, z);
        Block blockRight = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z);
        Block blockLeft = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y - 1, z);
        Block blockLower = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z);
        Block blockUpper = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y, z + 1);
        Block blockFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y, z - 1);
        Block blockBack = world.getBlockState(blockPos).getBlock();

        if ((blockRight == Blocks.LEAVES || blockRight == Blocks.LEAVES2) && !woodCheck(world, x + 1, y, z)) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x + 1, y, z), true);
            checkAndDestroyLeaves(world, x + 1, y, z);
        }
        if ((blockLeft == Blocks.LEAVES || blockLeft == Blocks.LEAVES2) && !woodCheck(world, x - 1, y, z)) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x - 1, y, z), true);
            checkAndDestroyLeaves(world, x - 1, y, z);
        }
        if ((blockLower == Blocks.LEAVES || blockLower == Blocks.LEAVES2) && !woodCheck(world, x, y - 1, z)) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x, y - 1, z), true);
            checkAndDestroyLeaves(world, x, y - 1, z);
        }
        if ((blockUpper == Blocks.LEAVES || blockUpper == Blocks.LEAVES2) && !woodCheck(world, x, y + 1, z)) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x, y + 1, z), true);
            checkAndDestroyLeaves(world, x, y + 1, z);
        }
        if ((blockFront == Blocks.LEAVES || blockFront == Blocks.LEAVES2) && !woodCheck(world, x, y, z + 1)) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x, y, z + 1), true);
            checkAndDestroyLeaves(world, x, y, z + 1);
        }
        if ((blockBack == Blocks.LEAVES || blockBack == Blocks.LEAVES2) && !woodCheck(world, x, y, z - 1)) {
            world.destroyBlock(new BlockPos.MutableBlockPos(x, y, z - 1), true);
            checkAndDestroyLeaves(world, x, y, z - 1);
        }
    }

    public boolean leavesCheck(World world, BlockPos pos) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        for (int i = 0; i <= 255; i++) {
            blockPos.setPos(x, y + i, z);
            Block leaves = world.getBlockState(blockPos).getBlock();
            if (leaves == Blocks.LEAVES || leaves == Blocks.LEAVES2) {
                return true;
            }
        }
        return false;
    }

    public boolean woodCheck(World world, BlockPos pos) {
        return woodCheck(world, pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean woodCheck(World world, int x, int y, int z) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i > -2; i--) {
            blockPos.setPos(x, y + i, z);
            Block wood = world.getBlockState(blockPos).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
        }
        for (int i = 0; i > -3; i--) {
            blockPos.setPos(x, y, z + i);
            Block wood = world.getBlockState(blockPos).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
        }
        for (int i = 0; i < 3; i++) {
            blockPos.setPos(x, y, z + i);
            Block wood = world.getBlockState(blockPos).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
        }
        for (int i = 0; i > -3; i--) {
            blockPos.setPos(x + i, y, z);
            Block wood = world.getBlockState(blockPos).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
        }
        for (int i = 0; i < 3; i++) {
            blockPos.setPos(x + i, y, z);
            Block wood = world.getBlockState(blockPos).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
        }
        for (int i = 0; i > -2; i--) {
            for (int j = 0; j > -2; j--) {
                blockPos.setPos(x + i, y, z + j);
                Block wood = world.getBlockState(blockPos).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
            }
        }
        for (int i = 0; i > -2; i--) {
            for (int j = 0; j < 2; j++) {
                blockPos.setPos(x + i, y, z + j);
                Block wood = world.getBlockState(blockPos).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j > -2; j--) {
                blockPos.setPos(x + i, y, z + j);
                Block wood = world.getBlockState(blockPos).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                blockPos.setPos(x + i, y, z + j);
                Block wood = world.getBlockState(blockPos).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) return true;
            }
        }
        return false;
    }

    public int countWood(World world, BlockPos pos, HashSet<BlockPos> checkedPos, int count) {
        return countWood(world, pos.getX(), pos.getY(), pos.getZ(), checkedPos, count);
    }


    private int countWood(World world, int x, int y, int z, HashSet<BlockPos> checkedPos, int count) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        blockPos.setPos(x + 1, y, z);
        Block blockRight = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z);
        Block blockLeft = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z);
        Block blockUpper = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y + 1, z);
        Block blockUpperRight = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y + 1, z);
        Block blockUpperLeft = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z + 1);
        Block blockUpperFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y + 1, z - 1);
        Block blockUpperBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y + 1, z + 1);
        Block blockUpperLeftFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y + 1, z - 1);
        Block blockUpperRightBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y + 1, z - 1);
        Block blockUpperRightFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y + 1, z + 1);
        Block blockUpperLeftBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z + 1);
        Block blockLeftFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y, z - 1);
        Block blockRightBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x - 1, y, z - 1);
        Block blockRightFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x + 1, y, z + 1);
        Block blockLeftBack = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y, z + 1);
        Block blockFront = world.getBlockState(blockPos).getBlock();
        blockPos.setPos(x, y, z - 1);
        Block blockBack = world.getBlockState(blockPos).getBlock();

        BlockPos keyPos = new BlockPos(x + 1, y, z);
        if ((blockRight == Blocks.LOG || blockRight == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x + 1, y, z, checkedPos, count);
        }

        keyPos = new BlockPos(x - 1, y, z);
        if ((blockLeft == Blocks.LOG || blockLeft == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x - 1, y, z, checkedPos, count);
        }

        keyPos = new BlockPos(x, y, z + 1);
        if ((blockFront == Blocks.LOG || blockFront == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x, y, z + 1, checkedPos, count);
        }

        keyPos = new BlockPos(x, y, z - 1);
        if ((blockBack == Blocks.LOG || blockBack == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x, y, z - 1, checkedPos, count);
        }

        keyPos = new BlockPos(x, y + 1, z);
        if ((blockUpper == Blocks.LOG || blockUpper == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x, y + 1, z, checkedPos, count);
        }

        keyPos = new BlockPos(x - 1, y + 1, z + 1);
        if ((blockUpperLeftFront == Blocks.LOG || blockUpperLeftFront == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x - 1, y + 1, z + 1, checkedPos, count);
        }

        keyPos = new BlockPos(x + 1, y + 1, z - 1);
        if ((blockUpperRightBack == Blocks.LOG || blockUpperRightBack == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x + 1, y + 1, z - 1, checkedPos, count);
        }

        keyPos = new BlockPos(x - 1, y + 1, z - 1);
        if ((blockUpperRightFront == Blocks.LOG || blockUpperRightFront == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x - 1, y + 1, z - 1, checkedPos, count);
        }

        keyPos = new BlockPos(x + 1, y + 1, z + 1);
        if ((blockUpperLeftBack == Blocks.LOG || blockUpperLeftBack == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x + 1, y + 1, z + 1, checkedPos, count);
        }

        keyPos = new BlockPos(x - 1, y, z + 1);
        if ((blockLeftFront == Blocks.LOG || blockLeftFront == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x - 1, y, z + 1, checkedPos, count);
        }

        keyPos = new BlockPos(x + 1, y, z - 1);
        if ((blockRightBack == Blocks.LOG || blockRightBack == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x + 1, y, z - 1, checkedPos, count);
        }

        keyPos = new BlockPos(x - 1, y, z - 1);
        if ((blockRightFront == Blocks.LOG || blockRightFront == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x - 1, y, z - 1, checkedPos, count);
        }

        keyPos = new BlockPos(x + 1, y, z + 1);
        if ((blockLeftBack == Blocks.LOG || blockLeftBack == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x + 1, y, z + 1, checkedPos, count);
        }

        keyPos = new BlockPos(x + 1, y + 1, z);
        if ((blockUpperRight == Blocks.LOG || blockUpperRight == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x + 1, y + 1, z, checkedPos, count);
        }

        keyPos = new BlockPos(x - 1, y + 1, z);
        if ((blockUpperLeft == Blocks.LOG || blockUpperLeft == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x - 1, y + 1, z, checkedPos, count);
        }

        keyPos = new BlockPos(x, y + 1, z + 1);
        if ((blockUpperFront == Blocks.LOG || blockUpperFront == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x, y + 1, z + 1, checkedPos, count);
        }

        keyPos = new BlockPos(x, y + 1, z - 1);
        if ((blockUpperBack == Blocks.LOG || blockUpperBack == Blocks.LOG2) && !checkedPos.contains(keyPos)) {
            count++;
            checkedPos.add(keyPos);
            count = countWood(world, x, y + 1, z - 1, checkedPos, count);
        }

        return count;
    }

    public void breakAxe(ItemStack stack, EntityLivingBase entityLiving, int count) {
        stack.damageItem(count, entityLiving);
    }
}
