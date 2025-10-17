package com.enclave.enclavemod.mixin.entity;

import com.enclave.enclavemod.entity.ai.courier.EntityAICourierDeliver;
import com.enclave.enclavemod.entity.ai.courier.EntityAICourierHarvest;
import com.enclave.enclavemod.entity.ai.courier.EntityAICourierMoveToRow;
import com.enclave.enclavemod.entity.ai.courier.inventory.CourierInventory;
import com.enclave.enclavemod.entity.ai.courier.state.StateMachine;
import com.enclave.enclavemod.entity.ai.courier.world.DoorFinder;
import com.enclave.enclavemod.entity.ai.courier.world.FarmlandRowScanner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityZombie.class)
public abstract class EntityZombieCourier extends EntityMob {
    private StateMachine stateMachine;
    private FarmlandRowScanner rowScanner;
    private CourierInventory inventory;
    private DoorFinder doorFinder;

    public EntityZombieCourier(World worldIn) {
        super(worldIn);
    }

    @Overwrite
    protected void initEntityAI() {
        this.stateMachine = new StateMachine();
        this.rowScanner = new FarmlandRowScanner();
        this.inventory = new CourierInventory();
        this.doorFinder = new DoorFinder();

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIOpenDoor(this, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    @Overwrite
    protected void applyEntityAI() {
        this.tasks.addTask(3, new EntityAICourierMoveToRow(this, 1.2D, stateMachine, rowScanner));
        this.tasks.addTask(4, new EntityAICourierHarvest(this, 1.2D, stateMachine, rowScanner, inventory));
        this.tasks.addTask(5, new EntityAICourierDeliver(this, 1.2D, stateMachine, doorFinder, inventory));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
    }

    @Overwrite
    public boolean attackEntityAsMob(Entity target) {
        return false;
    }

    @Overwrite
    public boolean attackEntityFrom(DamageSource source, float amount) {
        super.attackEntityFrom(source, amount);
        return false;
    }

    @Overwrite
    protected boolean shouldBurnInDay() {
        return false;
    }
}
