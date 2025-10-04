package com.enclave.enclavemod.renderer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderClaymoreArea {

    public void drawPreview(WorldClient world, EntityPlayer player, RayTraceResult movingObjectPositionIn, float partialTicks)
    {
        if (movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK && movingObjectPositionIn.sideHit == EnumFacing.UP) {

            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (iblockstate.getMaterial() != Material.AIR && world.getWorldBorder().contains(blockpos)) {

                double viewerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
                double viewerY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
                double viewerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

                Vec3d hit = movingObjectPositionIn.hitVec;
                double cx = hit.x - viewerX;
                double cy = hit.y - viewerY;
                double cz = hit.z - viewerZ;

                Vec3d look = new Vec3d(player.getLookVec().x, 0.0D, player.getLookVec().z);
                if (look.lengthVector() < 1e-6) {
                    look = new Vec3d(1.0D, 0.0D, 1.0D);
                }
                look = look.normalize();

                Vec3d worldUp = new Vec3d(0.0D, 1.0D, 0.0D);
                Vec3d preview = look.crossProduct(worldUp).normalize();

                double length = 1.5D;
                double base = 1.0D;

                Vec3d center = look.scale(length);
                Vec3d left = look.scale(length - 0.15D).add(preview.scale(-base * 0.5));
                Vec3d right = look.scale(length - 0.15D).add(preview.scale(base * 0.5));

                double yOffset = 0.05D;
                double cyOffset = cy + yOffset;

                double ctxX = cx + center.x, ctyY = cyOffset + center.y, ctzZ = cz + center.z;
                double ltxX = cx + left.x, ltyY = cyOffset + left.y, ltzZ = cz + left.z;
                double rtxX = cx + right.x, rtyY = cyOffset + right.y, rtzZ = cz + right.z;

                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.depthMask(false);
                GlStateManager.glLineWidth(2.0F);
                GL11.glEnable(GL11.GL_LINE_STIPPLE);
                GL11.glLineStipple(3, (short) 0x3F3F);

                Tessellator tess = Tessellator.getInstance();
                BufferBuilder buf = tess.getBuffer();
                buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

                float r = 0.0f, g = 1.0f, b = 0.2f, a = 0.95f;

                buf.pos(cx, cyOffset, cz).color(r, g, b, a).endVertex();
                buf.pos(ctxX, ctyY, ctzZ).color(r, g, b, a).endVertex();

                buf.pos(cx, cyOffset, cz).color(r, g, b, a).endVertex();
                buf.pos(ltxX, ltyY, ltzZ).color(r, g, b, a).endVertex();

                buf.pos(cx, cyOffset, cz).color(r, g, b, a).endVertex();
                buf.pos(rtxX, rtyY, rtzZ).color(r, g, b, a).endVertex();

                tess.draw();

                GL11.glDisable(GL11.GL_LINE_STIPPLE);
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }
}
