package com.enclave.enclavemod.mixin.entity;

import com.enclave.enclavemod.entity.ai.EntityAICourierHarvestFarmland;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class EntityZombieCourier extends EntityMob {

    public EntityZombieCourier(World worldIn) {
        super(worldIn);
    }

    @Overwrite
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    @Overwrite
    protected void applyEntityAI() {
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAICourierHarvestFarmland(this, 1.2D));
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
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }
}
