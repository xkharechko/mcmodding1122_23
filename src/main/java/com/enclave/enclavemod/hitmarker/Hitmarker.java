package com.enclave.enclavemod.hitmarker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;

public class Hitmarker {
    public static final ResourceLocation ICONS_MOD = new ResourceLocation("enclavemod", "textures/gui/icons.png");
    private int hitTicks = 0;

    public void onHit() {
        hitTicks = 30;
    }

    public void render(Minecraft mc, ScaledResolution res) {
        if (hitTicks > 0) {
            int l = res.getScaledWidth();
            int h = res.getScaledHeight();
            mc.getTextureManager().bindTexture(ICONS_MOD);
            GlStateManager.enableAlpha();
            mc.ingameGUI.drawTexturedModalRect(l / 2 - 7, h / 2 - 7, 0, 0, 16, 16);
            hitTicks--;
        }
    }
}
