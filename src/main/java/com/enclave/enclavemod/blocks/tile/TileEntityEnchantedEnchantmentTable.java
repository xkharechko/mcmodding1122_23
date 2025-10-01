package com.enclave.enclavemod.blocks.tile;

import com.enclave.enclavemod.inventory.ContainerEnchantedEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

public class TileEntityEnchantedEnchantmentTable extends TileEntityEnchantmentTable {

    private String customName;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.enchanted.enchant";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerEnchantedEnchantment(playerInventory, this.world, this.pos);
    }

    @Override
    public String getGuiID() {
        return "enclavemod:enchanted_enchanting_table";
    }
}
