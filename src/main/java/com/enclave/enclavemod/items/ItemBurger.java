package com.enclave.enclavemod.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemBurger extends ItemFood {
    public ItemBurger(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player){
        super.onFoodEaten(stack, worldIn, player);

        if(player.getFoodStats().getFoodLevel() > 2){
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100));
        }
    }
}
