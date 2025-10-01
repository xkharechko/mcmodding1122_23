package com.enclave.enclavemod;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ModMaterials {
    public static final Item.ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial("enclavemod:tool", 6, 256, 500.0F, 9F, 12).setRepairItem(new ItemStack(Blocks.COAL_BLOCK));
    public static final ItemArmor.ArmorMaterial ARMOR_MATERIAL = EnumHelper.addArmorMaterial("enclavemod:armor", "enclavemod:armor", 900, new int[]{2, 3, 4, 1}, 7, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 12.0F).setRepairItem(new ItemStack(Blocks.IRON_BLOCK));
}
