package com.enclave.enclavemod.proxy;

import com.enclave.enclavemod.EnclaveMod;
import com.enclave.enclavemod.entity.EntityBoar;
import com.enclave.enclavemod.inventory.GuiHandler;
import com.enclave.enclavemod.registers.BlocksRegistry;
import com.enclave.enclavemod.registers.EntityRegistryHandler;
import com.enclave.enclavemod.registers.NetworkPacketsRegistry;
import com.enclave.enclavemod.treecapitator.TreeCapitator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.HashSet;

public class CommonProxy {
    private final TreeCapitator treeCapitator = new TreeCapitator();

    @SubscribeEvent
    public void onBlockBreak (BlockEvent.BreakEvent event) {
        if ((event.getState().getBlock() == Blocks.LOG || event.getState().getBlock() == Blocks.LOG2) && treeCapitator.leavesCheck(event.getWorld(), event.getPos())
                && (event.getPlayer().getHeldItemMainhand().getItem() == Items.WOODEN_AXE || event.getPlayer().getHeldItemMainhand().getItem() == Items.GOLDEN_AXE
                || event.getPlayer().getHeldItemMainhand().getItem() == Items.IRON_AXE || event.getPlayer().getHeldItemMainhand().getItem() == Items.DIAMOND_AXE
                || event.getPlayer().getHeldItemMainhand().getItem() == Items.STONE_AXE)) {
            if (event.getWorld().isRemote) return;
            int count = 0;
            HashSet<BlockPos> checkedPos = new HashSet<>();
            int treeSize = treeCapitator.countWood(event.getPlayer().world, event.getPos(), checkedPos, count);
            treeCapitator.checkAndDestroyWood(event.getWorld(), event.getPos());
            treeCapitator.breakAxe(event.getPlayer().getHeldItemMainhand(), event.getPlayer(), treeSize);
        }
    }

    public void preInit(FMLPreInitializationEvent event) {
        NetworkPacketsRegistry.registerMessages();
        EntityRegistryHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
        BlocksRegistry.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(EnclaveMod.instance, new GuiHandler());
    }
    public void init(FMLInitializationEvent event) {

    }
    public void postInit(FMLPostInitializationEvent event) {

    }
}
