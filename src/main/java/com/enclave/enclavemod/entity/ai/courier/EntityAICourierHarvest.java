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

public class EntityAICourierHarvest extends EntityAIMoveToBlock {
    private final EntityCreature entity;
    private final StateMachine stateMachine;
    private final FarmlandRowScanner rowScanner;
    private final CourierInventory inventory;

    public EntityAICourierHarvest(EntityCreature entity, double speedIn, StateMachine stateMachine, FarmlandRowScanner rowScanner, CourierInventory inventory) {
        super(entity, speedIn, 16);
        this.entity = entity;
        this.stateMachine = stateMachine;
        this.rowScanner = rowScanner;
        this.inventory = inventory;
    }

    public boolean shouldExecute() {
        if (stateMachine.getCourierState() == HARVEST) {
            return super.shouldExecute();
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return stateMachine.getCourierState() == HARVEST && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (this.getIsAboveDestination() && stateMachine.getCourierState() == HARVEST) {
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
                stateMachine.finishHarvestState();
            }
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND && stateMachine.getCourierState() == HARVEST) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            int x = pos.getX();
            int z = pos.getZ();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) {
                if (x <= rowScanner.getRowEndX() && x >= rowScanner.getRowStartX()
                        && z <= rowScanner.getRowEndZ() && z >= rowScanner.getRowStartZ()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }
}