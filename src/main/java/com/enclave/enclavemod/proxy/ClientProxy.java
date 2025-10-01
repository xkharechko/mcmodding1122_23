package com.enclave.enclavemod.proxy;

import com.enclave.enclavemod.hitmarker.FullClientHitmarker;
import com.enclave.enclavemod.hitmarker.Hitmarker;
import com.enclave.enclavemod.registers.BlocksRegistry;
import com.enclave.enclavemod.registers.EntityRegistryHandler;
import com.enclave.enclavemod.treecapitator.TreeCapitator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

public class ClientProxy extends CommonProxy {
    private FullClientHitmarker fullClientHitmarker;
    private Hitmarker hitmarker = new Hitmarker();
    private final TreeCapitator treeCapitator = new TreeCapitator();

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            if (Minecraft.getMinecraft().ingameGUI != null) {
                hitmarker.render(Minecraft.getMinecraft(), res);
                //fullClientHitmarker.render(Minecraft.getMinecraft(), res);
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if ((event.getState().getBlock() == Blocks.LOG || event.getState().getBlock() == Blocks.LOG2) && treeCapitator.leavesCheck(event.getEntityPlayer().world, event.getPos())
                && (event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.WOODEN_AXE || event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.GOLDEN_AXE
                || event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.IRON_AXE || event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.DIAMOND_AXE
                || event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.STONE_AXE)) {
            int count = 1;
            HashSet<BlockPos> checkedPos = new HashSet<>();
            int treeSize = treeCapitator.countWood(event.getEntityPlayer().world, event.getPos(), checkedPos, count);

            float baseSpeed = event.getOriginalSpeed();
            float newSpeed = baseSpeed/treeSize;

            event.setNewSpeed(newSpeed);
        }
    }

    @SubscribeEvent
    public void onEntityHurt(AttackEntityEvent event) {
        if (event.getEntityPlayer() == Minecraft.getMinecraft().player) {
            hitmarker.onHit();
        }
    }


    @Override
    public void preInit(FMLPreInitializationEvent event) {
        EntityRegistryHandler.initModels();
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        //fullClientHitmarker = new FullClientHitmarker();

        hitmarker = new Hitmarker();

        BlocksRegistry.registerRender();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
