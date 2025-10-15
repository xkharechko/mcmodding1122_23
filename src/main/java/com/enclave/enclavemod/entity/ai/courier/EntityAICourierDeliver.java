package com.enclave.enclavemod.entity.ai.courier;

import com.enclave.enclavemod.entity.ai.courier.inventory.CourierInventory;
import com.enclave.enclavemod.entity.ai.courier.state.CourierState;
import com.enclave.enclavemod.entity.ai.courier.state.StateMachine;
import com.enclave.enclavemod.entity.ai.courier.world.DoorFinder;
import com.enclave.enclavemod.entity.ai.courier.world.FarmlandRowScanner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
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

public class EntityAICourierDeliver extends EntityAIMoveToBlock {
    private final EntityCreature entity;
    private final StateMachine stateMachine;
    private final DoorFinder doorFinder;
    private final CourierInventory inventory;

    public EntityAICourierDeliver(EntityCreature entity, double speedIn, StateMachine stateMachine, DoorFinder doorFinder, CourierInventory inventory) {
        super(entity, speedIn, 16);
        this.entity = entity;
        this.stateMachine = stateMachine;
        this.doorFinder = doorFinder;
        this.inventory = inventory;
    }

    public boolean shouldExecute() {
        if (stateMachine.getCourierState() == DELIVER) {
            BlockPos doorPos = doorFinder.findNearestDoor(entity);
            if (doorPos != null) {
                this.destinationBlock = doorPos;
                return true;
            } else {
                stateMachine.finishDeliverState();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return stateMachine.getCourierState() == DELIVER && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (doorFinder.isNearDoor(this.destinationBlock, entity) && stateMachine.getCourierState() == DELIVER) {
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
                    System.out.println("Finished delivery");
                    stateMachine.finishDeliverState();
                }
                this.runDelay = 1;
            } else {
                doorFinder.clearVisitedDoors();
                System.out.println("Finished delivery");
                stateMachine.finishDeliverState();
            }
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        return false;
    }
}