package com.enclave.enclavemod.entity;

import com.enclave.enclavemod.blocks.BlockAcorn;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityBoar extends EntityMob {
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.<Boolean>createKey(com.enclave.enclavemod.entity.EntityBoar.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> BOOST_TIME = EntityDataManager.<Integer>createKey(com.enclave.enclavemod.entity.EntityBoar.class, DataSerializers.VARINT);
    private boolean boosting;
    private boolean isFollowingAcorn = false;
    public void setIsFollowingAcorn(boolean isFollowingAcorn) {
        this.isFollowingAcorn = isFollowingAcorn;
        System.out.println("setRaiding = " + isFollowingAcorn);
    }
    public boolean isFollowingAcorn() {
        return this.isFollowingAcorn;
    }
    private int boostTime;
    private int totalBoostTime;
    private int acornTicks;

    public EntityBoar(World worldIn)
    {
        super(worldIn);
        this.setSize(1.3F, 1.3F);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new AIBoarAttack(this));
        this.targetTasks.addTask(2, new AITargetAggressor(this));
        this.tasks.addTask(3, new EntityBoar.AIRaidAcorn(this));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (BOOST_TIME.equals(key) && this.world.isRemote)
        {
            this.boosting = true;
            this.boostTime = 0;
            this.totalBoostTime = ((Integer)this.dataManager.get(BOOST_TIME)).intValue();
        }

        super.notifyDataManagerChange(key);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(SADDLED, Boolean.valueOf(false));
        this.dataManager.register(BOOST_TIME, Integer.valueOf(0));
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (!super.processInteract(player, hand))
        {
            ItemStack itemstack = player.getHeldItem(hand);

            if (itemstack.getItem() == Items.NAME_TAG)
            {
                itemstack.interactWithEntity(player, this, hand);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_PIG;
    }

    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!this.world.isRemote && !this.isDead)
        {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);
            entitypigzombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitypigzombie.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                entitypigzombie.setCustomNameTag(this.getCustomNameTag());
                entitypigzombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }

            this.world.spawnEntity(entitypigzombie);
            this.setDead();
        }
    }

    public void travel(float strafe, float vertical, float forward)
    {
        Entity entity = this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);

        if (this.isBeingRidden() && this.canBeSteered())
        {
            this.rotationYaw = entity.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (this.boosting && this.boostTime++ > this.totalBoostTime)
            {
                this.boosting = false;
            }

            if (this.canPassengerSteer())
            {
                float f = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.225F;

                if (this.boosting)
                {
                    f += f * 1.15F * MathHelper.sin((float)this.boostTime / (float)this.totalBoostTime * (float)Math.PI);
                }

                this.setAIMoveSpeed(f);
                super.travel(0.0F, 0.0F, 1.0F);
            }
            else
            {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f1 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            this.limbSwingAmount += (f1 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }

    public void updateAITasks() {
        if (this.acornTicks > 0)
        {
            this.acornTicks--;
        } else if (isFollowingAcorn) {
            this.setIsFollowingAcorn(false);
        }
    }

    static class AIRaidAcorn extends EntityAIMoveToBlock {
        private final EntityBoar boar;
        private boolean canRaid;
        private int wait = 2;

        public AIRaidAcorn(EntityBoar boarIn)
        {
            super(boarIn, 1D, 10);
            this.boar = boarIn;
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.boar.world, this.boar)) {
                return false;
            }

            this.canRaid = false;
            if (this.runDelay > 20) {
                this.runDelay = 20;
            }
            if (this.runDelay == 0 && wait < 0) {
                wait = 2;
            }

            return super.shouldExecute();
        }

        public boolean shouldContinueExecuting()
        {
            return this.canRaid && !this.boar.getNavigator().noPath();
        }

        public void updateTask()
        {
            super.updateTask();
            this.boar.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.boar.getVerticalFaceSpeed());

            if (this.boar.getDistanceSqToCenter(this.destinationBlock) < 6.0D) {
                World world = this.boar.world;
                BlockPos blockpos = this.destinationBlock;
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (this.canRaid && block instanceof BlockAcorn)
                {
                    this.boar.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.8F, 0.8F);
                    if (wait == 0) {
                        world.destroyBlock(blockpos, false);
                        this.boar.setIsFollowingAcorn(false);
                    }
                    wait--;
                }

                this.canRaid = false;
                this.runDelay = 10;
            }
        }

        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            Block block = worldIn.getBlockState(pos).getBlock();

            if (block instanceof BlockAcorn && !this.canRaid) {
                this.canRaid = true;
                this.boar.setIsFollowingAcorn(true);
                this.boar.acornTicks = 200;
                return true;
            }

            return false;
        }
    }

    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AITargetAggressor(EntityBoar boar)
        {
            super(boar, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return ((EntityBoar)this.taskOwner).isFollowingAcorn() && super.shouldExecute();
        }
    }

    static class AIBoarAttack extends EntityAIAttackMelee
    {
        public AIBoarAttack(EntityBoar boar)
        {
            super(boar, 1.0D, false);
        }

        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        {
            return (double)(4.0F + attackTarget.width);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        IBlockState down = world.getBlockState(pos.down());
        return down.getMaterial() == Material.GRASS || down.getMaterial() == Material.GROUND;
    }
}
