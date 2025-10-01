package com.enclave.enclavemod.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHollow extends GuiContainer {
    private static final ResourceLocation HOLLOW_GUI_TEXTURES = new ResourceLocation("enclavemod:textures/gui/container/hollow.png");
    private final InventoryPlayer playerInventory;
    public IInventory hollowInventory;

    public GuiHollow(InventoryPlayer playerInv, IInventory hollowInv)
    {
        super(new ContainerHollow(playerInv, hollowInv, Minecraft.getMinecraft().player));
        this.playerInventory = playerInv;
        this.hollowInventory = hollowInv;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.hollowInventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(HOLLOW_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
