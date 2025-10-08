package com.enclave.enclavemod.entity;

import com.enclave.enclavemod.entity.model.ModelClaymore;
import com.enclave.enclavemod.entity.model.ModelClaymoreMoved;
import io.netty.util.internal.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
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
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderClaymore extends Render<EntityClaymore> {

    private static final ResourceLocation CLAYMORE_TEXTURES = new ResourceLocation("enclavemod:textures/entity/claymore_nolaser_512.png");
    public static final RenderClaymore.Factory FACTORY = new RenderClaymore.Factory();
    private final ModelClaymoreMoved model;

    public RenderClaymore(RenderManager manager) {
        super(manager);
        this.model = new ModelClaymoreMoved();
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityClaymore entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        double dx = player.posX - entity.posX;
        double dy = player.posY - entity.posY;
        double dz = player.posZ - entity.posZ;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        double minDist = 1.0D;
        double maxDist = 40.0D;
        float minWidth = 4.00F;
        float maxWidth = 0.40F;

        float t = (float) ((distance - minDist) / (maxDist - minDist));
        t = Math.max(0.00F, Math.min(1.00F, t));
        float lineWidth = minWidth + (maxWidth - minWidth) * t;

        GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -1.36F, 0.0F);
        this.bindEntityTexture(entity);
        GlStateManager.enableRescaleNormal();

        float scale = 0.0625F;
        this.model.render(entity, 0.0F, 0.0F, entity.ticksExisted + partialTicks, 0.0F, 0.0F, scale);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.08D, z);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(lineWidth);

        double angle = Math.toRadians(45.0D);
        double length = entity.getTriggerRadius();
        double yawRad = Math.toRadians(yaw + 45);

        Vec3d look = new Vec3d(-Math.sin(yawRad), 0, Math.cos(yawRad)).normalize();
        Vec3d leftDir = new Vec3d(look.x * Math.cos(angle) - look.z * Math.sin(angle), 0, look.x * Math.sin(angle) + look.z * Math.cos(angle));
        Vec3d rightDir = new Vec3d(look.x * Math.cos(-angle) - look.z * Math.sin(-angle), 0, look.x * Math.sin(-angle) + look.z * Math.cos(-angle));

        Vec3d center = look.scale(length);
        Vec3d left = leftDir.scale(length);
        Vec3d right = rightDir.scale(length);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        float r = 1.0f, g = 0.0f, b = 0.2f, a = 1.0f;

        buf.pos(0, 0, 0).color(r, g, b, a).endVertex();
        buf.pos(center.x, 0, center.z).color(r, g, b, a).endVertex();

        buf.pos(0, 0, 0).color(r, g, b, a).endVertex();
        buf.pos(left.x, 0, left.z).color(r, g, b, a).endVertex();

        buf.pos(0, 0, 0).color(r, g, b, a).endVertex();
        buf.pos(right.x, 0, right.z).color(r, g, b, a).endVertex();

        tess.draw();

        GlStateManager.glLineWidth(1.0F);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
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

