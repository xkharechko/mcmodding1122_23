package com.enclave.enclavemod.entity;

import com.enclave.enclavemod.registers.ItemsRegistry;
import com.enclave.enclavemod.registers.SoundEventsRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityClaymore extends Entity
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntityClaymore.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase claymorePlacedBy;
    private int fuse;
    private boolean isTriggered;
    private final double triggerRadius;

    public EntityClaymore(World worldIn)
    {
        super(worldIn);
        this.fuse = 20;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(1.0F, 0.05F);
        this.triggerRadius = 1.5D;
        this.isTriggered = false;
    }

    public EntityClaymore(World worldIn, double x, double y, double z, EntityLivingBase igniter)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setFuse(20);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.claymorePlacedBy = igniter;
        this.isTriggered = false;
    }

    protected void entityInit()
    {
        this.dataManager.register(FUSE, Integer.valueOf(20));
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.world.isRemote && !this.isTriggered) {
            for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(5.0D, 0.0D, 5.0D))) {
                if (entity instanceof EntityLivingBase && entity != this.claymorePlacedBy) {
                    double angleX = entity.posX - this.posX;
                    double angleZ = entity.posZ - this.posZ;

                    double dx = entity.posX - this.posX;
                    double dy = entity.posY - this.posY;
                    double dz = entity.posZ - this.posZ;
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    double yawToTarget = Math.toDegrees(Math.atan2(angleZ, angleX));
                    double deltaYaw = net.minecraft.util.math.MathHelper.wrapDegrees(yawToTarget - this.rotationYaw);

                    if (deltaYaw >= 90 && deltaYaw <= 180) {
                        if (distance <= this.triggerRadius) {
                            this.isTriggered = true;
                            this.fuse = 20;
                            playSound(SoundEventsRegistry.CLAYMORE_TRIGGER, 1.0F, 1.0F);
                            break;
                        }
                    }
                }
            }
        }

        if (this.isTriggered) {
            --this.fuse;
            if (this.fuse <= 0) {
                this.setDead();

                if (!this.world.isRemote) {
                    this.explode();
                }
            }
        } else
        {
            this.handleWaterMovement();
        }
    }

    private void explode()
    {
        this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, 2.0F, true);
    }

    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Fuse", (short)this.getFuse());
    }

    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFuse(compound.getShort("Fuse"));
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void setFuse(int fuseIn)
    {
        this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
    }

    public int getFuseDataManager() {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }

    public double getTriggerRadius() {
        return this.triggerRadius;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) source.getTrueSource();

            if (attacker == this.claymorePlacedBy) {
                this.dropItem(ItemsRegistry.CLAYMORE, 1);
                this.setDead();
                return true;
            } else {
                this.isTriggered = true;
                this.fuse = 0;
            }
        } else if (source.isExplosion()) {
            this.isTriggered = true;
            this.fuse = 0;
        }

        return super.attackEntityFrom(source, amount);
    }
}