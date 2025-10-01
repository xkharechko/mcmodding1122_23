package com.enclave.enclavemod.hitmarker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;

public class FullClientHitmarker {
    public static final ResourceLocation ICONS_MOD = new ResourceLocation("enclavemod", "textures/gui/icons.png");
    private int hitTicks = 0;
    private int missTicks = 0;
    private float prevCooledAttackStrength = 1.0F;

    public void onHit() {
        hitTicks = 30;
    }

    public void onMiss() {
        missTicks = 10;
    }

    public void render(Minecraft mc, ScaledResolution res) {
        int l = res.getScaledWidth();
        int h = res.getScaledHeight();
        mc.getTextureManager().bindTexture(ICONS_MOD);
        RayTraceResult ray = mc.objectMouseOver;
        float currentCooledAttackStrength = mc.player.getCooledAttackStrength(0.0F);

        if (hitTicks > 0) {
            hitTicks--;
            GlStateManager.enableAlpha();
            mc.ingameGUI.drawTexturedModalRect(l / 2 - 7, h / 2 - 7, 0, 0, 16, 16);
        }

//        if (missTicks > 0) {
//            missTicks--;
//        }
//
//        if (ray != null && mc.player.getCooledAttackStrength(0.0F) == 0.0F && ray.typeOfHit != RayTraceResult.Type.ENTITY) {
//            onMiss();
//        }

        if (ray != null && ray.entityHit instanceof EntityLivingBase && currentCooledAttackStrength < prevCooledAttackStrength && ray.typeOfHit == RayTraceResult.Type.ENTITY) {
            onHit();
        }

        prevCooledAttackStrength = currentCooledAttackStrength;
    }
}
