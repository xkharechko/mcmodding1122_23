package com.enclave.enclavemod;

import com.enclave.enclavemod.configs.LootboxConfig;
import com.enclave.enclavemod.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;

@Mod(modid = EnclaveMod.MODID, name = EnclaveMod.NAME, version = EnclaveMod.VERSION)
public class EnclaveMod {
    public static final String MODID = "enclavemod";
    public static final String NAME = "Enclave Mod";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.enclave.enclavemod.proxy.ClientProxy", serverSide = "com.enclave.enclavemod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static EnclaveMod instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        proxy.preInit(event);
        File configDirectory = event.getModConfigurationDirectory();
        LootboxConfig.init(configDirectory);

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}