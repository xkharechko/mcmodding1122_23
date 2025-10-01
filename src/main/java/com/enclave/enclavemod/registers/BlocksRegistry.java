package com.enclave.enclavemod.registers;

import com.enclave.enclavemod.blocks.*;
import com.enclave.enclavemod.blocks.tile.TileEntityHollow;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlocksRegistry {
    public static Block SUPERSTONE = new SuperStoneBlock("superstone").setCreativeTab(CreativeTabs.MISC);
    public static Block BLOCK_COUNTER = new BlockCounter("block_counter", Material.ROCK, 10F, 10F, SoundType.STONE).setCreativeTab(CreativeTabs.MISC);
    public static Block HOLLOW = new HollowBlock("hollow");
    public static Block ACORN = new BlockAcorn("acorn").setCreativeTab(CreativeTabs.MISC);
    public static Block ENCHANTED_ENCHANTMENT_TABLE = new BlockEnchantedEnchantmentTable("enchanted_enchanting_table");

    public static void register() {
        setRegister(SUPERSTONE);
        setRegister(BLOCK_COUNTER);
        setRegister(HOLLOW);
        setRegister(ACORN);
        setRegister(ENCHANTED_ENCHANTMENT_TABLE);
        GameRegistry.registerTileEntity(((BlockCounter) BLOCK_COUNTER).getTileEntityClass(), BLOCK_COUNTER.getRegistryName().toString());
        GameRegistry.registerTileEntity(((HollowBlock) HOLLOW).getTileEntityClass(), HOLLOW.getRegistryName().toString());
        GameRegistry.registerTileEntity(((BlockEnchantedEnchantmentTable) ENCHANTED_ENCHANTMENT_TABLE).getTileEntityClass(), ENCHANTED_ENCHANTMENT_TABLE.getRegistryName().toString());
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender() {
        setRender(SUPERSTONE);
        setRender(BLOCK_COUNTER);
        setRender(HOLLOW);
        setRender(ACORN);
        setRender(ENCHANTED_ENCHANTMENT_TABLE);
    }

    private static void setRegister(Block block) {
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    private static void setRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ItemBlock.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
