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

public class EntityAICourierMoveToRow extends EntityAIMoveToBlock {
    private final EntityCreature entity;
    private final StateMachine stateMachine;
    private final FarmlandRowScanner rowScanner;

    public EntityAICourierMoveToRow(EntityCreature entity, double speedIn, StateMachine stateMachine, FarmlandRowScanner rowScanner) {
        super(entity, speedIn, 16);
        this.entity = entity;
        this.stateMachine = stateMachine;
        this.rowScanner = rowScanner;
    }

    public boolean shouldExecute() {
        if (stateMachine.getCourierState() == IDLE || stateMachine.getCourierState() == MOVE_TO_ROW) {
            stateMachine.finishIdleState();
            return super.shouldExecute();
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return stateMachine.getCourierState() == MOVE_TO_ROW && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.entity.getVerticalFaceSpeed());

        if (this.getIsAboveDestination() && stateMachine.getCourierState() == MOVE_TO_ROW) {
            rowScanner.defineRowCoordinates(this.entity.world, this.destinationBlock);
            stateMachine.finishMoveState();
        }
    }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND && stateMachine.getCourierState() == MOVE_TO_ROW) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                return true;
            }
        }

        return false;
    }
}
