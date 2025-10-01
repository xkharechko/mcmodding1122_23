package com.enclave.enclavemod.inventory;

import com.enclave.enclavemod.blocks.tile.TileEntityEnchantedEnchantmentTable;
import com.enclave.enclavemod.blocks.tile.TileEntityHollow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int HOLLOW_GUI = 0;
    public static final int ENCHANTED_ENCHANTMENT_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == HOLLOW_GUI) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityHollow) {
                return new ContainerHollow(player.inventory, (TileEntityHollow) tileEntity, player);
            }
        }
        if (ID == ENCHANTED_ENCHANTMENT_GUI) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityEnchantedEnchantmentTable) {
                return new ContainerEnchantedEnchantment(player.inventory, world, new BlockPos(x, y, z));
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == HOLLOW_GUI) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityHollow) {
                return new GuiHollow(player.inventory, (TileEntityHollow) tileEntity);
            }
        }
        if (ID == ENCHANTED_ENCHANTMENT_GUI) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityEnchantedEnchantmentTable) {
                return new GuiEnchantedEnchantment(player.inventory, world, (IWorldNameable) tileEntity);
            }
        }
        return null;
    }
}
