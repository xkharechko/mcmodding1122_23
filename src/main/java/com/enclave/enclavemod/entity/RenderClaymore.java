package com.enclave.enclavemod.entity;

import com.enclave.enclavemod.entity.model.ModelClaymore;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.enclave.enclavemod.entity.model.ModelClaymore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderClaymore extends Render<EntityClaymore> {

    private static final ResourceLocation CLAYMORE_TEXTURES = new ResourceLocation("enclavemod:textures/entity/claymore_512.png");
    public static final RenderClaymore.Factory FACTORY = new RenderClaymore.Factory();
    private final ModelClaymore model;

    public RenderClaymore(RenderManager manager) {
        super(manager);
        this.model = new ModelClaymore();
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityClaymore entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

        GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -1.36F, 0.0F);
        this.bindEntityTexture(entity);
        GlStateManager.enableRescaleNormal();

        float scale = 0.0625F;
        this.model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, scale);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityClaymore entity) {
        return CLAYMORE_TEXTURES;
    }

    public static class Factory implements IRenderFactory<EntityClaymore> {
        @Override
        public Render<? super EntityClaymore> createRenderFor(RenderManager manager) {
            return new RenderClaymore(manager);
        }
    }
}

