package com.enclave.enclavemod.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import java.util.Random;

public class WorldGenStructure extends WorldGenerator implements IStructure {
    public static String structureName;

    public WorldGenStructure(String structureName) {
        WorldGenStructure.structureName = structureName;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        generateStructure(worldIn, position);
        return true;
    }

    public static void generateStructure(World worldIn, BlockPos pos) {
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager manager = WORLDSERVER.getStructureTemplateManager();
        ResourceLocation structure = new ResourceLocation("enclavemod", WorldGenStructure.structureName);
        Template template = manager.get(server, structure);

        if (template != null) {
            IBlockState state = worldIn.getBlockState(pos);
            worldIn.notifyBlockUpdate(pos, state, state,3);
            template.addBlocksToWorld(worldIn, pos, PLACEMENT_SETTINGS);
        }
    }
}
