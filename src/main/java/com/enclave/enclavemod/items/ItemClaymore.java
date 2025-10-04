package com.enclave.enclavemod.items;

import com.enclave.enclavemod.EnclaveMod;
import com.enclave.enclavemod.entity.EntityClaymore;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemClaymore extends Item {

    public ItemClaymore(String name) {
        this.setRegistryName(EnclaveMod.MODID, name);
        this.setUnlocalizedName(EnclaveMod.MODID + "." + name);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        Minecraft mc = Minecraft.getMinecraft();
        RayTraceResult traceResult = mc.objectMouseOver;

        if(!worldIn.isRemote && traceResult.typeOfHit == RayTraceResult.Type.BLOCK && traceResult.sideHit == EnumFacing.UP) {
            Vec3d lookVec = playerIn.getLookVec();

            Vec3d hit = traceResult.hitVec;
            double x = hit.x + lookVec.x;
            double y = hit.y + 0.05D;
            double z = hit.z + lookVec.z;

            EntityClaymore claymore = new EntityClaymore(worldIn, x, y, z, playerIn);
            claymore.rotationYaw = playerIn.rotationYaw - 45.0F;
            claymore.rotationPitch = 0.0F;
            worldIn.spawnEntity(claymore);

            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
