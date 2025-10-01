package com.enclave.enclavemod.inventory;

import com.enclave.enclavemod.registers.ItemsRegistry;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiEnchantedEnchantment extends GuiContainer
{
    private static final ResourceLocation ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("enclavemod:textures/gui/container/enchanted_enchanting_table.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final ModelBook MODEL_BOOK = new ModelBook();
    private final InventoryPlayer playerInventory;
    private final Random random = new Random();
    private final ContainerEnchantedEnchantment container;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private final IWorldNameable nameable;

    public GuiEnchantedEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable nameable)
    {
        super(new ContainerEnchantedEnchantment(inventory, worldIn, ((TileEntity) nameable).getPos()));
        this.playerInventory = inventory;
        this.container = (ContainerEnchantedEnchantment)this.inventorySlots;
        this.nameable = nameable;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.nameable.getDisplayName().getUnformattedText(), 8, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    public void updateScreen()
    {
        super.updateScreen();
        this.tickBook();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        for (int k = 0; k < 20; ++k)
        {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(this.mc.player, k))
            {
                this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
            }
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize + 18, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float f = 1.0F;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        float f1 = 5.0F;
        GlStateManager.scale(5.0F, 5.0F, 5.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        float f2 = this.oOpen + (this.open - this.oOpen) * partialTicks;
        GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
        GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float f3 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.25F;
        float f4 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.75F;
        f3 = (f3 - (float) MathHelper.fastFloor((double)f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f3 > 1.0F)
        {
            f3 = 1.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render((Entity)null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNameParts.getInstance().reseedRandomGenerator((long)this.container.xpSeed);
        ItemStack gem = this.container.getCurrentGem();
        int enchantmentsAmount = 0;

        for (int n : this.container.enchantClue) {
            if (n == -1) break;
            enchantmentsAmount++;
        }

        for (int l = 0; l < 3 + (enchantmentsAmount >= 3 ? enchantmentsAmount - 3 : 0); ++l)
        {
            int i1 = i + 60;
            int j1 = i1 + 20;
            this.zLevel = 0.0F;
            this.mc.getTextureManager().bindTexture(ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE);
            int k1 = this.container.enchantLevels[l];
            int gemtype;

            if (l < enchantmentsAmount/10F) {
                gemtype = 0;
            } else if (l < enchantmentsAmount/3.34F) {
                gemtype = 1;
            } else if (l < enchantmentsAmount/1.43F) {
                gemtype = 2;
            } else {
                gemtype = 3;
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (k1 == 0)
            {
                this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
            }
            else
            {
                String s = "" + k1;
                int l1 = 86 - this.fontRenderer.getStringWidth(s);
                String s1 = EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l1);
                FontRenderer fontrenderer = this.mc.standardGalacticFontRenderer;
                int i2 = 6839882;

                if (this.container.enchantClue[l] == -1) {
                    this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                    this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * gemtype, 239, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, (i2 & 16711422) >> 1);
                    i2 = 4226832;
                } else if ((gem.isItemEqual(ItemsRegistry.GEM_COMMON.getDefaultInstance()) && l < enchantmentsAmount/10F)
                        || (gem.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance()) && l < enchantmentsAmount/3.34F)
                        || (gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance()) && l < enchantmentsAmount/1.43F)
                        || gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()) || this.mc.player.capabilities.isCreativeMode) {
                    int j2 = mouseX - (i + 60);
                    int k2 = mouseY - (j + 14 + 19 * l);

                    if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19)
                    {
                        this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 204, 108, 19);
                        i2 = 16777088;
                    }
                    else
                    {
                        this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 166, 108, 19);
                    }

                    this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * gemtype, 223, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, i2);
                    i2 = 8453920;
                } else {
                    this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                    this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * gemtype, 239, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, (i2 & 16711422) >> 1);
                    i2 = 4226832;
                }

                fontrenderer = this.mc.fontRenderer;
                fontrenderer.drawStringWithShadow(s, (float)(j1 + 86 - fontrenderer.getStringWidth(s)), (float)(j + 16 + 19 * l + 7), i2);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        partialTicks = this.mc.getTickLength();
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        boolean flag = this.mc.player.capabilities.isCreativeMode;
        ItemStack gem = this.container.getCurrentGem();
        int enchantmentsAmount = 0;

        for (int n : this.container.enchantClue) {
            if (n == -1) break;
            enchantmentsAmount++;
        }

        for (int j = 0; j < 20; ++j)
        {
            int k = this.container.enchantLevels[j];
            Enchantment enchantment = Enchantment.getEnchantmentByID(this.container.enchantClue[j]);
            int l = this.container.worldClue[j];

            if (this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0)
            {
                List<String> list = Lists.<String>newArrayList();
                list.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format(enchantment == null ? "" : enchantment.getTranslatedName(l)));

                if(enchantment == null) java.util.Collections.addAll(list, "", TextFormatting.RED + I18n.format("forge.container.enchant.limitedEnchantability")); else
                if (!flag) {
                    if (j < enchantmentsAmount/10F && (!gem.isItemEqual(ItemsRegistry.GEM_COMMON.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()))) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires at least common gem"));
                    } else if (j < enchantmentsAmount/3.34F && j >= enchantmentsAmount/10F && (!gem.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()))) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires at least rare gem"));
                    } else if (j < enchantmentsAmount/1.43F && j >= enchantmentsAmount/3.34F && (!gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()))) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires at least mythic gem"));
                    } else if (j >= enchantmentsAmount/1.43F && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance())) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires legendary gem"));
                    }
                }

                this.drawHoveringText(list, mouseX, mouseY);
                break;
            }
        }
    }

    public void tickBook()
    {
        ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(itemstack, this.last))
        {
            this.last = itemstack;

            while (true)
            {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));

                if (this.flip > this.flipT + 1.0F || this.flip < this.flipT - 1.0F)
                {
                    break;
                }
            }
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for (int i = 0; i < 20; ++i)
        {
            if (this.container.enchantLevels[i] != 0)
            {
                flag = true;
            }
        }

        if (flag)
        {
            this.open += 0.2F;
        }
        else
        {
            this.open -= 0.2F;
        }

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}
