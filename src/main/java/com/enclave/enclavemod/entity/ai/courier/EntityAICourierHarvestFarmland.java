package com.enclave.enclavemod.entity.ai.courier;

import com.enclave.enclavemod.entity.ai.courier.inventory.CourierInventory;
import com.enclave.enclavemod.entity.ai.courier.state.CourierState;
import com.enclave.enclavemod.entity.ai.courier.world.DoorFinder;
import com.enclave.enclavemod.entity.ai.courier.world.FarmlandRowScanner;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static com.enclave.enclavemod.entity.ai.courier.state.CourierState.*;

public class EntityAICourierHarvestFarmland extends EntityAIMoveToBlock {
    private final EntityCreature entity;
    private CourierState currentTask = IDLE;
    private boolean isHarvestEnded = false;
    private final DoorFinder doorFinder = new DoorFinder();
    private final FarmlandRowScanner rowScanner = new FarmlandRowScanner();
    private final CourierInventory inventory = new CourierInventory();

    public EntityAICourierHarvestFarmland(EntityCreature entity, double speedIn) {
        super(entity, speedIn, 16);
        this.entity = entity;
    }

    public boolean shouldExecute()
    {
        if (this.runDelay <= 0 && this.isHarvestEnded) {
            this.currentTask = DELIVER;

            BlockPos doorPos = doorFinder.findNearestDoor(entity);
            if (doorPos != null) {
                this.destinationBlock = doorPos;
                return true;
            } else {
                this.currentTask = IDLE;
                this.isHarvestEnded = false;
                this.runDelay = 20;
                return false;
            }
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting()
    {
        return this.currentTask != IDLE && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (doorFinder.isNearDoor(this.destinationBlock, entity) && this.currentTask == DELIVER) {
            World world = this.entity.world;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = world.getBlockState(blockpos);
            BlockDoor door = (BlockDoor) iblockstate.getBlock();
            door.toggleDoor(world, blockpos, true);
            if (!inventory.isEmpty()) {
                ItemStack stack = inventory.takeOne();
                if (stack != null && !stack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, blockpos.getX() + 0.5, blockpos.getY() + 1.0, blockpos.getZ() + 0.5, stack.copy()));
                    System.out.println("Spawned " + stack + " at " + blockpos + ": " + world.getBlockState(blockpos).getBlock());
                }
                doorFinder.addVisitedDoor(blockpos);
                if (!inventory.isEmpty()) {
                    BlockPos doorPos = doorFinder.findNearestDoor(entity);
                    if (doorPos != null) {
                        this.destinationBlock = doorPos;
                        System.out.println("Found new door");
                        System.out.println("To delivery: " + inventory.getInventoryItems());
                    } else {
                        doorFinder.clearVisitedDoors();
                        doorPos = doorFinder.findNearestDoor(entity);
                        if (doorPos != null) {
                            this.destinationBlock = doorPos;
                            System.out.println("Found new door after visited doors reset");
                            System.out.println("To delivery: " + inventory.getInventoryItems());
                        }
                    }
                } else {
                    doorFinder.clearVisitedDoors();
                    this.currentTask = IDLE;
                    this.runDelay = 100;
                    this.isHarvestEnded = false;
                    System.out.println("Finished delivery");
                }
                this.runDelay = 1;
            } else {
                doorFinder.clearVisitedDoors();
                this.currentTask = IDLE;
                this.runDelay = 100;
                this.isHarvestEnded = false;
                System.out.println("Finished delivery");
            }
        }

        if (this.getIsAboveDestination() && this.currentTask == MOVE_TO_ROW) {
            rowScanner.defineRowCoordinates(this.entity.world, this.destinationBlock);
            this.currentTask = HARVEST;
            this.runDelay = 1;
        }

        if (this.getIsAboveDestination() && this.currentTask == HARVEST) {
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
                    if (item.getItem().getItem() instanceof ItemFood || item.getItem().getItem().equals(Items.WHEAT)) {
                        inventory.add(item.getItem());
                        item.setDead();
                    }
                }
            }

            for (int xi = rowScanner.getRowStartX(); xi <= rowScanner.getRowEndX() && !foundCrop; xi++) {
                for (int zi = rowScanner.getRowStartZ(); zi <= rowScanner.getRowEndZ(); zi++) {
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

        if (block == Blocks.FARMLAND && (this.currentTask == IDLE || this.currentTask == MOVE_TO_ROW)) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) && (this.currentTask == IDLE || this.currentTask == MOVE_TO_ROW))
            {
                this.currentTask = MOVE_TO_ROW;
                return true;
            }
        }

        if (block == Blocks.FARMLAND && this.currentTask == HARVEST) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            int x = pos.getX();
            int z = pos.getZ();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) {
                if (x <= rowScanner.getRowEndX() && x >= rowScanner.getRowStartX()
                        && z <= rowScanner.getRowEndZ() && z >= rowScanner.getRowStartZ()) {
                    this.currentTask = HARVEST;
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }
}