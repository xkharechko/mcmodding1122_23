package com.enclave.enclavemod.structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.enclave.enclavemod.registers.LootTableHandler.GEMS_CHEST;

public class WorldGenEnchantedEnchantmentTableStructure implements IWorldGenerator {
    public static final WorldGenStructure ENCHANTEDENCHANTMENTTABLESTRUCTURE = new WorldGenStructure("enchantedenchantmenttablestructure");

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateStructure(ENCHANTEDENCHANTMENTTABLESTRUCTURE, world, random, chunkX, chunkZ, 150, Blocks.GRASS, BiomePlains.class);
    }

    private void generateStructure(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int chance, Block spawnBlock, Class<?>... classes) {
        ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));

        int x = chunkX * 16 + random.nextInt(16);
        int z = chunkZ * 16 + random.nextInt(16);
        int y = calculateHeight(world, x, z, spawnBlock);
        BlockPos pos = new BlockPos(x, y, z);

        Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();

        if(classesList.contains(biome)) {
            if (random.nextInt(chance) == 0) {
                generator.generate(world, random, pos);

                BlockPos chestPos = pos.add(5, 2, 3);
                TileEntity tile = world.getTileEntity(chestPos);

                if (tile instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tile;
                    chest.setLootTable(GEMS_CHEST, random.nextLong());
                }
            }
        }
    }

    private static int calculateHeight(World world, int x, int z, Block spawnBlock) {
        int y = world.getHeight();
        boolean foundGround = false;

        while (y-- >= 0 && !foundGround) {
            Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            foundGround = block == spawnBlock;
        }

        return y;
    }
}
