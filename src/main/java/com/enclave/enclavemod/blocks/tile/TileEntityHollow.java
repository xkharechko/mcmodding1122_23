package com.enclave.enclavemod.blocks.tile;

import com.enclave.enclavemod.blocks.HollowBlock;
import com.enclave.enclavemod.inventory.ContainerHollow;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityHollow extends TileEntityLockableLoot implements ITickable {
    private NonNullList<ItemStack> hollowContents = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
    public int numPlayersUsing;
    private int ticksSinceSync;

    public TileEntityHollow()
    {
    }

    public int getSizeInventory()
    {
        return 6;
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.hollowContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public String getName()
    {
        return "container.hollow";
    }

    public static void registerFixesHollow(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityHollow.class, new String[] {"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.hollowContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.hollowContents);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.hollowContents);
        }

        return compound;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void update()
    {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)i - 5.0F), (double)((float)j - 5.0F), (double)((float)k - 5.0F), (double)((float)(i + 1) + 5.0F), (double)((float)(j + 1) + 5.0F), (double)((float)(k + 1) + 5.0F))))
            {
                if (entityplayer.openContainer instanceof ContainerHollow)
                {
                    IInventory iinventory = ((ContainerHollow)entityplayer.openContainer).getHollowInventory();

                    if (iinventory == this)
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.getBlockType() instanceof HollowBlock)
        {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return super.getCapability(capability, facing);
    }

    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
    }

    public String getGuiID()
    {
        return "enclavemod:hollow";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        this.fillWithLoot(playerIn);
        return new ContainerHollow(playerInventory, this, playerIn);
    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.hollowContents;
    }
}
