package com.enclave.enclavemod.registers;

import com.enclave.enclavemod.EnclaveMod;
import com.enclave.enclavemod.entity.EntityBoar;
import com.enclave.enclavemod.entity.EntityClaymore;
import com.enclave.enclavemod.entity.RenderBoar;
import com.enclave.enclavemod.entity.RenderClaymore;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRegistryHandler {

    public static void register() {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(EnclaveMod.MODID, "boar"), EntityBoar.class, "Boar", id++, EnclaveMod.instance, 64, 3, true, 0x8B4513, 0x654321);
        EntityRegistry.registerModEntity(new ResourceLocation(EnclaveMod.MODID, "claymore"), EntityClaymore.class, "Claymore", id++, EnclaveMod.instance, 64, 10, false);
        for (Biome biome : net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES) {
            biome.getSpawnableList(EnumCreatureType.MONSTER)
                    .add(new Biome.SpawnListEntry(EntityBoar.class, 10, 3, 5));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        RenderingRegistry.registerEntityRenderingHandler(EntityBoar.class, RenderBoar.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityClaymore.class, RenderClaymore.FACTORY);
    }
}
