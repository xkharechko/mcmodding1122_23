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
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

        Block blockRight = world.getBlockState(blockpos.setPos(blockpos.getX() + 1, blockpos.getY() + 0, blockpos.getZ() + 0)).getBlock();
        Block blockLeft = world.getBlockState(pos.add(-1, 0, 0)).getBlock();
        Block blockUpper = world.getBlockState(pos.add(0, 1, 0)).getBlock();
        Block blockUpperRight = world.getBlockState(pos.add(1, 1, 0)).getBlock();
        Block blockUpperLeft = world.getBlockState(pos.add(-1, 1, 0)).getBlock();
        Block blockUpperFront = world.getBlockState(pos.add(0, 1, 1)).getBlock();
        Block blockUpperBack = world.getBlockState(pos.add(0, 1, -1)).getBlock();
        Block blockUpperLeftFront = world.getBlockState(pos.add(-1, 1, 1)).getBlock();
        Block blockUpperRightBack = world.getBlockState(pos.add(1, 1, -1)).getBlock();
        Block blockUpperRightFront = world.getBlockState(pos.add(-1, 1, -1)).getBlock();
        Block blockUpperLeftBack = world.getBlockState(pos.add(1, 1, 1)).getBlock();
        Block blockLeftFront = world.getBlockState(pos.add(-1, 0, 1)).getBlock();
        Block blockRightBack = world.getBlockState(pos.add(1, 0, -1)).getBlock();
        Block blockRightFront = world.getBlockState(pos.add(-1, 0, -1)).getBlock();
        Block blockLeftBack = world.getBlockState(pos.add(1, 0, 1)).getBlock();
        Block blockFront = world.getBlockState(pos.add(0, 0, 1)).getBlock();
        Block blockBack = world.getBlockState(pos.add(0, 0, -1)).getBlock();
        if (blockRight == Blocks.LOG || blockRight == Blocks.LOG2) {

            world.destroyBlock(pos.add(1, 0, 0), true);
            checkAndDestroyWood(world, pos.add(1, 0, 0));
        }
        if (blockLeft == Blocks.LOG || blockLeft == Blocks.LOG2) {
            world.destroyBlock(pos.add(-1, 0, 0), true);
            checkAndDestroyWood(world, pos.add(-1, 0, 0));
        }
        if (blockFront == Blocks.LOG || blockFront == Blocks.LOG2) {
            world.destroyBlock(pos.add(0, 0, 1), true);
            checkAndDestroyWood(world, pos.add(0, 0, 1));
        }
        if (blockBack == Blocks.LOG || blockBack == Blocks.LOG2) {
            world.destroyBlock(pos.add(0, 0, -1), true);
            checkAndDestroyWood(world, pos.add(0, 0, -1));
        }
        if (blockUpper == Blocks.LOG || blockUpper == Blocks.LOG2) {
            world.destroyBlock(pos.add(0, 1, 0), true);
            checkAndDestroyWood(world, pos.add(0, 1, 0));
        } else {
            checkAndDestroyLeaves(world, pos.add(0, 1, 0));
        }
        if (blockUpperLeftFront == Blocks.LOG || blockUpperLeftFront == Blocks.LOG2) {
            world.destroyBlock(pos.add(-1, 1, 1), true);
            checkAndDestroyWood(world, pos.add(-1, 1, 1));
        } else {
            checkAndDestroyLeaves(world, pos.add(-1, 1, 1));
        }
        if (blockUpperRightBack == Blocks.LOG || blockUpperRightBack == Blocks.LOG2) {
            world.destroyBlock(pos.add(1, 1, -1), true);
            checkAndDestroyWood(world, pos.add(1, 1, -1));
        } else {
            checkAndDestroyLeaves(world, pos.add(1, 1, -1));
        }
        if (blockUpperRightFront == Blocks.LOG || blockUpperRightFront == Blocks.LOG2) {
            world.destroyBlock(pos.add(-1, 1, -1), true);
            checkAndDestroyWood(world, pos.add(-1, 1, -1));
        } else {
            checkAndDestroyLeaves(world, pos.add(-1, 1, -1));
        }
        if (blockUpperLeftBack == Blocks.LOG || blockUpperLeftBack == Blocks.LOG2) {
            world.destroyBlock(pos.add(1, 1, 1), true);
            checkAndDestroyWood(world, pos.add(1, 1, 1));
        } else {
            checkAndDestroyLeaves(world, pos.add(1, 1, 1));
        }
        if (blockLeftFront == Blocks.LOG || blockLeftFront == Blocks.LOG2) {
            world.destroyBlock(pos.add(-1, 0, 1), true);
            checkAndDestroyWood(world, pos.add(-1, 0, 1));
        } else {
            checkAndDestroyLeaves(world, pos.add(-1, 0, 1));
        }
        if (blockRightBack == Blocks.LOG || blockRightBack == Blocks.LOG2) {
            world.destroyBlock(pos.add(1, 0, -1), true);
            checkAndDestroyWood(world, pos.add(1, 0, -1));
        } else {
            checkAndDestroyLeaves(world, pos.add(1, 0, -1));
        }
        if (blockRightFront == Blocks.LOG || blockRightFront == Blocks.LOG2) {
            world.destroyBlock(pos.add(-1, 0, -1), true);
            checkAndDestroyWood(world, pos.add(-1, 0, -1));
        } else {
            checkAndDestroyLeaves(world, pos.add(-1, 0, -1));
        }
        if (blockLeftBack == Blocks.LOG || blockLeftBack == Blocks.LOG2) {
            world.destroyBlock(pos.add(1, 0, 1), true);
            checkAndDestroyWood(world, pos.add(1, 0, 1));
        } else {
            checkAndDestroyLeaves(world, pos.add(1, 0, 1));
        }
        if (blockUpperRight == Blocks.LOG || blockUpper == Blocks.LOG2) {
            world.destroyBlock(pos.add(1, 1, 0), true);
            checkAndDestroyWood(world, pos.add(1, 1, 0));
        } else {
            checkAndDestroyLeaves(world, pos.add(1, 1, 0));
        }
        if (blockUpperLeft == Blocks.LOG || blockUpper == Blocks.LOG2) {
            world.destroyBlock(pos.add(-1, 1, 0), true);
            checkAndDestroyWood(world, pos.add(-1, 1, 0));
        } else {
            checkAndDestroyLeaves(world, pos.add(-1, 1, 0));
        }
        if (blockUpperFront == Blocks.LOG || blockUpper == Blocks.LOG2) {
            world.destroyBlock(pos.add(0, 1, 1), true);
            checkAndDestroyWood(world, pos.add(0, 1, 1));
        } else {
            checkAndDestroyLeaves(world, pos.add(0, 1, 1));
        }
        if (blockUpperBack == Blocks.LOG || blockUpper == Blocks.LOG2) {
            world.destroyBlock(pos.add(0, 1, -1), true);
            checkAndDestroyWood(world, pos.add(0, 1, -1));
        } else {
            checkAndDestroyLeaves(world, pos.add(0, 1, -1));
        }
    }

    public void checkAndDestroyLeaves(World world, BlockPos pos) {
        Block blockRight = world.getBlockState(pos.add(1, 0, 0)).getBlock();
        Block blockLeft = world.getBlockState(pos.add(-1, 0, 0)).getBlock();
        Block blockLower = world.getBlockState(pos.add(0, -1, 0)).getBlock();
        Block blockUpper = world.getBlockState(pos.add(0, 1, 0)).getBlock();
        Block blockFront = world.getBlockState(pos.add(0, 0, 1)).getBlock();
        Block blockBack = world.getBlockState(pos.add(0, 0, -1)).getBlock();
        if ((blockRight == Blocks.LEAVES || blockRight == Blocks.LEAVES2) && !woodCheck(world, pos.add(1, 0, 0))) {
            world.destroyBlock(pos.add(1, 0, 0), true);
            checkAndDestroyLeaves(world, pos.add(1, 0, 0));
        }
        if ((blockLeft == Blocks.LEAVES || blockLeft == Blocks.LEAVES2) && !woodCheck(world, pos.add(-1, 0, 0))) {
            world.destroyBlock(pos.add(-1, 0, 0), true);
            checkAndDestroyLeaves(world, pos.add(-1, 0, 0));
        }
        if ((blockLower == Blocks.LEAVES || blockLower == Blocks.LEAVES2) && !woodCheck(world, pos.add(0, -1, 0))) {
            world.destroyBlock(pos.add(0, -1, 0), true);
            checkAndDestroyLeaves(world, pos.add(0, -1, 0));
        }
        if ((blockUpper == Blocks.LEAVES || blockUpper == Blocks.LEAVES2) && !woodCheck(world, pos.add(0, 1, 0))) {
            world.destroyBlock(pos.add(0, 1, 0), true);
            checkAndDestroyLeaves(world, pos.add(0, 1, 0));
        }
        if ((blockFront == Blocks.LEAVES || blockFront == Blocks.LEAVES2) && !woodCheck(world, pos.add(0, 0, 1))) {
            world.destroyBlock(pos.add(0, 0, 1), true);
            checkAndDestroyLeaves(world, pos.add(0, 0, 1));
        }
        if ((blockBack == Blocks.LEAVES || blockBack == Blocks.LEAVES2) && !woodCheck(world, pos.add(0, 0, -1))) {
            world.destroyBlock(pos.add(0, 0, -1), true);
            checkAndDestroyLeaves(world, pos.add(0, 0, -1));
        }
    }

    public boolean leavesCheck (World world, BlockPos pos) {
        for (int i = 0; i <= 255; i++) {
            Block leaves = world.getBlockState(pos.add(0, i, 0)).getBlock();
            if (leaves == Blocks.LEAVES || leaves == Blocks.LEAVES2) {
                return true;
            }
        }
        return false;
    }

    public boolean woodCheck (World world, BlockPos pos) {
        for (int i = 0; i > -2; i--) {
            Block wood = world.getBlockState(pos.add(0, i, 0)).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                return true;
            }
        }
        for (int i = 0; i > -3; i--) {
            Block wood = world.getBlockState(pos.add(0, 0, i)).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            Block wood = world.getBlockState(pos.add(0, 0, i)).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                return true;
            }
        }
        for (int i = 0; i > -3; i--) {
            Block wood = world.getBlockState(pos.add(i, 0, 0)).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            Block wood = world.getBlockState(pos.add(i, 0, 0)).getBlock();
            if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                return true;
            }
        }
        for (int i = 0; i > -2; i--) {
            for (int j = 0; j > -2; j--) {
                Block wood = world.getBlockState(pos.add(i, 0, j)).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                    return true;
                }
            }
        }
        for (int i = 0; i > -2; i--) {
            for (int j = 0; j < 2; j++) {
                Block wood = world.getBlockState(pos.add(i, 0, j)).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                    return true;
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j > -2; j--) {
                Block wood = world.getBlockState(pos.add(i, 0, j)).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                    return true;
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Block wood = world.getBlockState(pos.add(i, 0, j)).getBlock();
                if (wood == Blocks.LOG || wood == Blocks.LOG2) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countWood(World world, BlockPos pos, HashSet<BlockPos> checkedPos, int count) {
        Block blockRight = world.getBlockState(pos.add(1, 0, 0)).getBlock();
        Block blockLeft = world.getBlockState(pos.add(-1, 0, 0)).getBlock();
        Block blockUpper = world.getBlockState(pos.add(0, 1, 0)).getBlock();
        Block blockUpperRight = world.getBlockState(pos.add(1, 1, 0)).getBlock();
        Block blockUpperLeft = world.getBlockState(pos.add(-1, 1, 0)).getBlock();
        Block blockUpperFront = world.getBlockState(pos.add(0, 1, 1)).getBlock();
        Block blockUpperBack = world.getBlockState(pos.add(0, 1, -1)).getBlock();
        Block blockUpperLeftFront = world.getBlockState(pos.add(-1, 1, 1)).getBlock();
        Block blockUpperRightBack = world.getBlockState(pos.add(1, 1, -1)).getBlock();
        Block blockUpperRightFront = world.getBlockState(pos.add(-1, 1, -1)).getBlock();
        Block blockUpperLeftBack = world.getBlockState(pos.add(1, 1, 1)).getBlock();
        Block blockLeftFront = world.getBlockState(pos.add(-1, 0, 1)).getBlock();
        Block blockRightBack = world.getBlockState(pos.add(1, 0, -1)).getBlock();
        Block blockRightFront = world.getBlockState(pos.add(-1, 0, -1)).getBlock();
        Block blockLeftBack = world.getBlockState(pos.add(1, 0, 1)).getBlock();
        Block blockFront = world.getBlockState(pos.add(0, 0, 1)).getBlock();
        Block blockBack = world.getBlockState(pos.add(0, 0, -1)).getBlock();
        if ((blockRight == Blocks.LOG || blockRight == Blocks.LOG2) && !checkedPos.contains(pos.add(1, 0, 0))) {
            count++;
            checkedPos.add(pos.add(1, 0, 0));
            count = countWood(world, pos.add(1, 0, 0), checkedPos, count);
        }

        if ((blockLeft == Blocks.LOG || blockLeft == Blocks.LOG2) && !checkedPos.contains(pos.add(-1, 0, 0))) {
            count++;
            checkedPos.add(pos.add(-1, 0, 0));
            count = countWood(world, pos.add(-1, 0, 0), checkedPos, count);
        }

        if ((blockFront == Blocks.LOG || blockFront == Blocks.LOG2) && !checkedPos.contains(pos.add(0, 0, 1))) {
            count++;
            checkedPos.add(pos.add(0, 0, 1));
            count = countWood(world, pos.add(0, 0, 1), checkedPos, count);
        }

        if ((blockBack == Blocks.LOG || blockBack == Blocks.LOG2) && !checkedPos.contains(pos.add(0, 0, -1))) {
            count++;
            checkedPos.add(pos.add(0, 0, -1));
            count = countWood(world, pos.add(0, 0, -1), checkedPos, count);
        }

        if ((blockUpper == Blocks.LOG || blockUpper == Blocks.LOG2) && !checkedPos.contains(pos.add(0, 1, 0))) {
            count++;
            checkedPos.add(pos.add(0, 1, 0));
            count = countWood(world, pos.add(0, 1, 0), checkedPos, count);
        }

        if ((blockUpperLeftFront == Blocks.LOG || blockUpperLeftFront == Blocks.LOG2) && !checkedPos.contains(pos.add(-1, 1, 1))) {
            count++;
            checkedPos.add(pos.add(-1, 1, 1));
            count = countWood(world, pos.add(-1, 1, 1), checkedPos, count);

        }
        if ((blockUpperRightBack == Blocks.LOG || blockUpperRightBack == Blocks.LOG2) && !checkedPos.contains(pos.add(1, 1, -1))) {
            count++;
            checkedPos.add(pos.add(1, 1, -1));
            count = countWood(world, pos.add(1, 1, -1), checkedPos, count);
        }

        if ((blockUpperRightFront == Blocks.LOG || blockUpperRightFront == Blocks.LOG2) && !checkedPos.contains(pos.add(-1, 1, -1))) {
            count++;
            checkedPos.add(pos.add(-1, 1, -1));
            count = countWood(world, pos.add(-1, 1, -1), checkedPos, count);
        }

        if ((blockUpperLeftBack == Blocks.LOG || blockUpperLeftBack == Blocks.LOG2) && !checkedPos.contains(pos.add(1, 1, 1))) {
            count++;
            checkedPos.add(pos.add(1, 1, 1));
            count = countWood(world, pos.add(1, 1, 1), checkedPos, count);
        }

        if ((blockLeftFront == Blocks.LOG || blockLeftFront == Blocks.LOG2) && !checkedPos.contains(pos.add(-1, 0, 1))) {
            count++;
            checkedPos.add(pos.add(-1, 0, 1));
            count = countWood(world, pos.add(-1, 0, 1), checkedPos, count);
        }

        if ((blockRightBack == Blocks.LOG || blockRightBack == Blocks.LOG2) && !checkedPos.contains(pos.add(1, 0, -1))) {
            count++;
            checkedPos.add(pos.add(1, 0, -1));
            count = countWood(world, pos.add(1, 0, -1), checkedPos, count);
        }

        if ((blockRightFront == Blocks.LOG || blockRightFront == Blocks.LOG2) && !checkedPos.contains(pos.add(-1, 0, -1))) {
            count++;
            checkedPos.add(pos.add(-1, 0, -1));
            count = countWood(world, pos.add(-1, 0, -1), checkedPos, count);
        }

        if ((blockLeftBack == Blocks.LOG || blockLeftBack == Blocks.LOG2) && !checkedPos.contains(pos.add(1, 0, 1))) {
            count++;
            checkedPos.add(pos.add(1, 0, 1));
            count = countWood(world, pos.add(1, 0, 1), checkedPos, count);
        }

        if ((blockUpperRight == Blocks.LOG || blockUpperRight == Blocks.LOG2) && !checkedPos.contains(pos.add(1, 1, 0))) {
            count++;
            checkedPos.add(pos.add(1, 1, 0));
            count = countWood(world, pos.add(1, 1, 0), checkedPos, count);
        }

        if ((blockUpperLeft == Blocks.LOG || blockUpperLeft == Blocks.LOG2) && !checkedPos.contains(pos.add(-1, 1, 0))) {
            count++;
            checkedPos.add(pos.add(-1, 1, 0));
            count = countWood(world, pos.add(-1, 1, 0), checkedPos, count);
        }

        if ((blockUpperFront == Blocks.LOG || blockUpperFront == Blocks.LOG2) && !checkedPos.contains(pos.add(0, 1, 1))) {
            count++;
            checkedPos.add(pos.add(0, 1, 1));
            count = countWood(world, pos.add(0, 1, 1), checkedPos, count);
        }

        if ((blockUpperBack == Blocks.LOG || blockUpperBack == Blocks.LOG2) && !checkedPos.contains(pos.add(0, 1, -1))) {
            count++;
            checkedPos.add(pos.add(0, 1, -1));
            count = countWood(world, pos.add(0, 1, -1), checkedPos, count);
        }

        return count;
    }

    public void breakAxe(ItemStack stack, EntityLivingBase entityLiving, int count) {
        stack.damageItem(count, entityLiving);
    }
}
