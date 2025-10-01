package com.enclave.enclavemod.blocks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCounter extends TileEntity {
    private int count;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("count", this.count);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.count = compound.getInteger("count");
        super.readFromNBT(compound);
    }

    public int getCount() {
        return this.count;
    }

    public void incrementCount() {
        this.count++;
        this.markDirty();
    }

    public void decrementCount() {
        this.count--;
        this.markDirty();
    }
}
