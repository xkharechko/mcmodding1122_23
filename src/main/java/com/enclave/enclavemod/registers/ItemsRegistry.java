package com.enclave.enclavemod.registers;

import com.enclave.enclavemod.*;
import com.enclave.enclavemod.items.*;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ObjectHolder("enclavemod")
@Mod.EventBusSubscriber
public class ItemsRegistry {
    @ObjectHolder("mysterious")
    public static final Item MYSTERIOUS = null;

    @ObjectHolder("burger")
    public static final Item BURGER = null;

    @ObjectHolder("axe")
    public static final Item AXE = null;

    @ObjectHolder("hoe")
    public static final Item HOE = null;

    @ObjectHolder("pickaxe")
    public static final Item PICKAXE = null;

    @ObjectHolder("spade")
    public static final Item SPADE = null;

    @ObjectHolder("sword")
    public static final Item SWORD = null;

    @ObjectHolder("boots")
    public static final Item BOOTS = null;

    @ObjectHolder("legs")
    public static final Item LEGS = null;

    @ObjectHolder("chestplate")
    public static final Item CHESTPLATE = null;

    @ObjectHolder("helmet")
    public static final Item HELMET = null;

    @ObjectHolder("gem_common")
    public static final Item GEM_COMMON = null;

    @ObjectHolder("gem_rare")
    public static final Item GEM_RARE = null;

    @ObjectHolder("gem_mythic")
    public static final Item GEM_MYTHIC = null;

    @ObjectHolder("gem_legendary")
    public static final Item GEM_LEGENDARY = null;


    @SubscribeEvent
    public static void onRegistryItem(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new ItemMysterious().setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemBurger("burger", 3, 20, true).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolAxe("axe", ModMaterials.TOOL_MATERIAL).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolHoe("hoe", ModMaterials.TOOL_MATERIAL).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolPickaxe("pickaxe", ModMaterials.TOOL_MATERIAL).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolSpade("spade", ModMaterials.TOOL_MATERIAL).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolSword("sword", ModMaterials.TOOL_MATERIAL).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolArmor("boots", ModMaterials.ARMOR_MATERIAL, 1, EntityEquipmentSlot.FEET).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolArmor("legs", ModMaterials.ARMOR_MATERIAL, 2, EntityEquipmentSlot.LEGS).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolArmor("chestplate", ModMaterials.ARMOR_MATERIAL, 1, EntityEquipmentSlot.CHEST).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemToolArmor("helmet", ModMaterials.ARMOR_MATERIAL, 1, EntityEquipmentSlot.HEAD).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemLootbox("lootbox", 1).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemGemCommon("gem_common", 16).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemGemRare("gem_rare", 16).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemGemMythic("gem_mythic", 16).setCreativeTab(CreativeTabs.MISC));
        e.getRegistry().register(new ItemGemLegendary("gem_legendary", 16).setCreativeTab(CreativeTabs.MISC));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRegistryModel(ModelRegistryEvent e) {
        registryModel(MYSTERIOUS);
        registryModel(BURGER);
        registryModel(BOOTS);
        registryModel(LEGS);
        registryModel(CHESTPLATE);
        registryModel(HELMET);
        registryModel(GEM_COMMON);
        registryModel(GEM_RARE);
        registryModel(GEM_MYTHIC);
        registryModel(GEM_LEGENDARY);
    }

    @SideOnly(Side.CLIENT)
    private static void registryModel(Item item) {
        final ResourceLocation regName = item.getRegistryName();
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(regName, "inventory");
        ModelBakery.registerItemVariants(item, modelResourceLocation);
        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    }
}
