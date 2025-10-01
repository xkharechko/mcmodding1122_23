package com.enclave.enclavemod.entity;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBoar extends RenderLiving<EntityBoar>
{
    private static final ResourceLocation BOAR_TEXTURES = new ResourceLocation("enclavemod:textures/entity/boar.png");

    public static final Factory FACTORY = new Factory();

    public RenderBoar(RenderManager p_i47198_1_)
    {
        super(p_i47198_1_, new ModelPig(), 0.7F);
    }

    protected ResourceLocation getEntityTexture(EntityBoar entity)
    {
        return BOAR_TEXTURES;
    }

    public static class Factory implements IRenderFactory<EntityBoar> {

        @Override
        public Render<? super EntityBoar> createRenderFor(RenderManager manager) {
            return new RenderBoar(manager);
        }
    }
}
