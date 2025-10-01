package com.enclave.enclavemod.items;

import com.enclave.enclavemod.configs.LootboxConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Random;

import static com.enclave.enclavemod.configs.LootboxConfig.lootContainer;

public class ItemLootbox extends Item {
    private static Random rand = new Random();

    public ItemLootbox(String name, int maxStackSize) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(maxStackSize);
    }

    private Item getLoot() {
        float totalWeight = 0;
        for (LootboxConfig.LootItemContainer i : lootContainer) {
            totalWeight += i.weight;
        }

        float r = rand.nextFloat() * totalWeight;

        float currentWeight = 0;
        for (LootboxConfig.LootItemContainer i : lootContainer) {
            currentWeight += i.weight;
            if (r < currentWeight) {
                return i.lootItem;
            }
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if(!worldIn.isRemote){
            ItemStack newStack = new ItemStack(getLoot());

            playerIn.setHeldItem(handIn, newStack);
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
