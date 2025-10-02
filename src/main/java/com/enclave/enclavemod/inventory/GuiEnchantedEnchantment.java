package com.enclave.enclavemod.inventory;

import com.enclave.enclavemod.packets.MessageSyncSliderPos;
import com.enclave.enclavemod.registers.ItemsRegistry;
import com.enclave.enclavemod.registers.NetworkPacketsRegistry;
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
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiEnchantedEnchantment extends GuiContainer
{
    private static final ResourceLocation ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("enclavemod:textures/gui/container/enchanted_enchanting_table_extended.png");
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
    public boolean needsScroll = false;
    public boolean needsSlider = false;
    public int scrollPos = 0;
    public float scrollCorrectPos = 0.00F;
    public int sliderPos = 0;
    public float sliderCorrectPos = 0.00F;
    private ItemStack last = ItemStack.EMPTY;
    private final IWorldNameable nameable;
    private final List<String> generatedNames = new ArrayList<>();
    private boolean isDraggingScroll = false;
    private boolean isDraggingSlider = false;
    private boolean movedToMaxLevel = false;
    private int maxEnchantmentLevel = 0;

    public GuiEnchantedEnchantment(InventoryPlayer inventory, World worldIn, IWorldNameable nameable)
    {
        super(new ContainerEnchantedEnchantment(inventory, worldIn, ((TileEntity) nameable).getPos()));
        this.playerInventory = inventory;
        this.container = (ContainerEnchantedEnchantment)this.inventorySlots;
        this.nameable = nameable;
        this.ySize = 166;
        this.xSize = 308;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.nameable.getDisplayName().getUnformattedText(), 8, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
        this.fontRenderer.drawString((this.container.worldClue[0] > 0 ? "Enchantment level: " + (this.container.worldClue[0] - (maxEnchantmentLevel - sliderPos)) : "Select an item to enchant"),
                                     (this.container.worldClue[0] > 0 ? 75 : 67), 48, 4210752);
    }

    public void updateScreen()
    {
        super.updateScreen();
        this.tickBook();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int scrollBarX = mouseX - (i + 288);
        int scrollBarY = mouseY - (j + 14);

        int sliderBarX = mouseX - (i + 60);
        int sliderBarY = mouseY - (j + 59);

        if (mouseButton == 0 && scrollBarX >= 0 && scrollBarY >= 0 && scrollBarX < 12 && scrollBarY < 133 && !isDraggingSlider) {
            isDraggingScroll = true;
        }

        if (mouseButton == 0 && sliderBarX >= 0 && sliderBarY >= 0 && sliderBarX < 108 && sliderBarY < 12 && !isDraggingScroll) {
            isDraggingSlider = true;
        }

        for (int k = scrollPos; k < 20; ++k) {
            if (k - scrollPos > 6) {
                break;
            }
            int l = mouseX - (i + 174);
            int i1 = mouseY - (j + 14 + 19 * (k - scrollPos));

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(this.mc.player, k))
            {
                this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDraggingScroll = false;
        isDraggingSlider = false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int d = 0;
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int scaledX = Mouse.getX() * this.width / this.mc.displayWidth;
        int scaledY = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;


        int scrollZoneX = scaledX - (i + 174);
        int scrollZoneY = scaledY - (j + 14);

        int sliderZoneX = scaledX - (i + 60);
        int sliderZoneY = scaledY - (j + 59);

        d = Mouse.getEventDWheel();

        if (d != 0 && needsScroll && scrollZoneX >= 0 && scrollZoneY >= 0 && scrollZoneX < 126 && scrollZoneY < 133) {
            int dir = d > 0 ? -1 : 1;
            scrollCorrectPos = 0;
            scrollPos += dir;
        }
        if (scrollPos < 0) {
            scrollPos = 0;
        }

        if (d != 0 && needsSlider && sliderZoneX >= 0 && sliderZoneY >= 0 && sliderZoneX < 108 && sliderZoneY < 12) {
            int dir = d > 0 ? 1 : -1;
            sliderCorrectPos = 0;
            sliderPos += dir;
        }
        if (sliderPos < 0) {
            sliderPos = 0;
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        drawModalRectWithCustomSizedTexture(i, j, 0, 0, this.xSize, this.ySize, 392.0F, 256.0F);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 453) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
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
        f4 = (f4 - (float) MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;

        if (!this.inventorySlots.getSlot(0).getStack().isEmpty() && this.last != this.inventorySlots.getSlot(0).getStack()) {
            maxEnchantmentLevel = this.container.worldClue[0] - 1;
        }

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
        float scrollMultiplier;
        float sliderMultiplier;

        for (int n : this.container.enchantClue) {
            if (n == -1) break;
            enchantmentsAmount++;
        }

        scrollMultiplier = 118.00F / Math.max(enchantmentsAmount - 7, 1);
        sliderMultiplier = 93.00F / Math.max(maxEnchantmentLevel, 1);

        if (isDraggingScroll) {
            if (mouseY < (int) (j + 14 + (1 * scrollMultiplier))) {
                scrollPos = 0;
            } else if (mouseY < (int) (j + 14 + (2 * scrollMultiplier))) {
                scrollPos = 1;
            } else if (mouseY < (int) (j + 14 + (3 * scrollMultiplier))) {
                scrollPos = 2;
            } else if (mouseY < (int) (j + 14 + (4 * scrollMultiplier))) {
                scrollPos = 3;
            } else if (mouseY < (int) (j + 14 + (5 * scrollMultiplier))) {
                scrollPos = 4;
            } else if (mouseY < (int) (j + 14 + (6 * scrollMultiplier))) {
                scrollPos = 5;
            }
            scrollCorrectPos = mouseY;
        }

        if (isDraggingSlider) {
            if (mouseX < (int) (i + 60 + 1 * sliderMultiplier)) {
                sliderPos = 0;
            } else if (mouseX < (int) (i + 60 + 2 * sliderMultiplier)) {
                sliderPos = 1;
            } else if (mouseX < (int) (i + 60 + 3 * sliderMultiplier)) {
                sliderPos = 2;
            } else if (mouseX < (int) (i + 60 + 4 * sliderMultiplier)) {
                sliderPos = 3;
            } else if (mouseX < (int) (i + 60 + 5 * sliderMultiplier)) {
                sliderPos = 4;
            } else if (mouseX < (int) (i + 60 + 6 * sliderMultiplier)) {
                sliderPos = 5;
            } else if (mouseX < (int) (i + 60 + 7 * sliderMultiplier)) {
                sliderPos = 6;
            } else if (mouseX < (int) (i + 60 + 8 * sliderMultiplier)) {
                sliderPos = 7;
            } else if (mouseX < (int) (i + 60 + 9 * sliderMultiplier)) {
                sliderPos = 8;
            } else if (mouseX < (int) (i + 60 + 10 * sliderMultiplier)) {
                sliderPos = 9;
            }
            sliderCorrectPos = mouseX;
        }

        if (scrollPos > enchantmentsAmount - 7) {
            scrollPos = (Math.max(enchantmentsAmount - 7, 0));
        }

        if (sliderPos > maxEnchantmentLevel) {
            sliderPos = (Math.max(maxEnchantmentLevel, 0));
        }

        if (!this.inventorySlots.getSlot(0).getStack().isEmpty() && maxEnchantmentLevel > 0 && !movedToMaxLevel) {
            this.sliderPos = Math.max(maxEnchantmentLevel, 0);
            this.movedToMaxLevel = true;
        }

        boolean isVisible;
        int reduceL = 0;

        for (int l = 0; l < 7 + (enchantmentsAmount >= 7 ? enchantmentsAmount - 7 : 0); ++l)
        {
            isVisible = 7 - l + scrollPos <= 7 && 7 - l + scrollPos > 0;
            if(!isVisible) {
                reduceL--;
                continue;
            }

            int i1 = i + 174;
            int j1 = i1 + 20;
            this.zLevel = 0.0F;
            this.mc.getTextureManager().bindTexture(ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE);
            int k1 = this.container.worldClue[l];
            int gemtype;

            if (l < enchantmentsAmount * 0.1F) {
                gemtype = 0;
            } else if (l < enchantmentsAmount * 0.4F) {
                gemtype = 1;
            } else if (l < enchantmentsAmount * 0.7F) {
                gemtype = 2;
            } else {
                gemtype = 3;
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (k1 <= 0)
            {
                drawModalRectWithCustomSizedTexture(i1, j + 14 + 19 * (l + reduceL), 0, 185, 108, 19, 392.0F, 256.0F);
            }
            else
            {
                String s = Enchantment.getEnchantmentByID(this.container.enchantClue[l]).getTranslatedName(k1 - (maxEnchantmentLevel - sliderPos)).replace("§c", "");
                int l1 = 85;
                FontRenderer fontrenderer = this.mc.fontRenderer;

                if (this.last != this.inventorySlots.getSlot(0).getStack()) {
                    generatedNames.clear();
                    this.last = this.inventorySlots.getSlot(0).getStack();
                }

                while (generatedNames.size() <= 6 + (enchantmentsAmount >= 7 ? enchantmentsAmount - 7 : 0)) {
                    generatedNames.add(EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l1));
                }

                int i2 = 6839882;
                if (this.container.enchantClue[l] == -1) {
                    drawModalRectWithCustomSizedTexture(i1, j + 14 + 19 * (l + reduceL), 0, 185, 108, 19, 392.0F, 256.0F);
                    drawModalRectWithCustomSizedTexture(i1 + 1, j + 15 + 19 * (l + reduceL), 16 * gemtype, 239, 16, 16, 392.0F, 256.0F);
                    fontrenderer.drawSplitString(s, j1, j + 16 + 19 * (l + reduceL), l1, (i2 & 16711422) >> 1);
                    i2 = 5985349;
                } else if ((gem.isItemEqual(ItemsRegistry.GEM_COMMON.getDefaultInstance()) && l < enchantmentsAmount * 0.1F)
                        || (gem.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance()) && l < enchantmentsAmount * 0.4F)
                        || (gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance()) && l < enchantmentsAmount * 0.7F)
                        || gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()) || this.mc.player.capabilities.isCreativeMode) {
                    int j2 = mouseX - (i + 174);
                    int k2 = mouseY - (j + 14 + 19 * (l + reduceL));

                    if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19)
                    {
                        drawModalRectWithCustomSizedTexture(i1, j + 14 + 19 * (l + reduceL), 0, 204, 108, 19, 392.0F, 256.0F);
                        i2 = 16777088;
                        drawModalRectWithCustomSizedTexture(i1 + 1, j + 15 + 19 * (l + reduceL), 16 * gemtype, 223, 16, 16, 392.0F, 256.0F);
                        fontrenderer.drawSplitString(s, j1, j + 16 + 19 * (l + reduceL), l1, i2);
                        i2 = 13409475;
                    }
                    else
                    {
                        drawModalRectWithCustomSizedTexture(i1, j + 14 + 19 * (l + reduceL), 0, 166, 108, 19, 392.0F, 256.0F);
                        drawModalRectWithCustomSizedTexture(i1 + 1, j + 15 + 19 * (l + reduceL), 16 * gemtype, 223, 16, 16, 392.0F, 256.0F);
                        fontrenderer.drawSplitString(s, j1, j + 16 + 19 * (l + reduceL), l1, i2);
                        i2 = 11575681;
                    }
                } else {
                    drawModalRectWithCustomSizedTexture(i1, j + 14 + 19 * (l + reduceL), 0, 185, 108, 19, 392.0F, 256.0F);
                    drawModalRectWithCustomSizedTexture(i1 + 1, j + 15 + 19 * (l + reduceL), 16 * gemtype, 239, 16, 16, 392.0F, 256.0F);
                    fontrenderer.drawSplitString(s, j1, j + 16 + 19 * (l + reduceL), l1, (i2 & 16711422) >> 1);
                    i2 = 5985349;
                }

                fontrenderer = this.mc.standardGalacticFontRenderer;
                int stringSize = s.length();
                String enchantMagicName = stringSize > 7 ? s.substring(0, 7) : s;
                fontrenderer.drawString(enchantMagicName, j1 + 86 - fontrenderer.getStringWidth(enchantMagicName), j + 17 + 19 * (l + reduceL) + 7, i2);
            }
        }

        needsScroll = enchantmentsAmount > 7;

        this.mc.getTextureManager().bindTexture(ENCHANTED_ENCHANTMENT_TABLE_GUI_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if(needsScroll) {
            drawModalRectWithCustomSizedTexture(i + 288, (int) ((isDraggingScroll ? (mouseY - 7 > 14 + j ? (Math.min(mouseY - 7, 132 + j)) : 14 + j) : (scrollCorrectPos - 7 > 0 ? (scrollCorrectPos - 7 < j + 14 ? j + 14 : Math.min(scrollCorrectPos - 7, j + 14 + 118)) : j + 14 + (scrollPos * scrollMultiplier)))), 368, 0, 12, 15, 392.0F, 256.0F);
        } else {
            drawModalRectWithCustomSizedTexture(i + 288, j + 14, 380, 0, 12, 15, 392.0F, 256.0F);
        }

        needsSlider = this.container.worldClue[0] > 1;

        if(needsSlider) {
            drawModalRectWithCustomSizedTexture((int) (isDraggingSlider ? (mouseX - 7 > 60 + i ? (Math.min(mouseX - 7, 153 + i)) : 60 + i) : (sliderCorrectPos - 7 > 0 ? ((sliderCorrectPos - 7 < i + 60 ? i + 60 : Math.min(sliderCorrectPos - 7, i + 60 + 93))) : (i + 60 + sliderPos * sliderMultiplier))), j + 59, 338, 0, 15, 12, 392.0F, 256.0F);
        } else {
            drawModalRectWithCustomSizedTexture(i + 60, j + 59, 353, 0, 15, 12, 392.0F, 256.0F);
        }

        NetworkPacketsRegistry.INSTANCE.sendToServer(new MessageSyncSliderPos(maxEnchantmentLevel - sliderPos));
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

        for (int j = scrollPos; j < 20; ++j)
        {
            if (j - scrollPos > 6) {
                break;
            }
            int k = this.container.enchantLevels[j];
            Enchantment enchantment = Enchantment.getEnchantmentByID(this.container.enchantClue[j]);
            int l = this.container.worldClue[j] - (maxEnchantmentLevel - sliderPos);

            if (this.isPointInRegion(174, 14 + 19 * (j - scrollPos), 108, 17, mouseX, mouseY) && k > 0)
            {
                List<String> list = Lists.<String>newArrayList();
                list.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format(enchantment == null ? "" : enchantment.getTranslatedName(l)));

                if(enchantment == null) java.util.Collections.addAll(list, "", TextFormatting.RED + I18n.format("forge.container.enchant.limitedEnchantability")); else
                if (!flag) {
                    if (j < enchantmentsAmount * 0.1F && (!gem.isItemEqual(ItemsRegistry.GEM_COMMON.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()))) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires at least common gem"));
                    } else if (j < enchantmentsAmount * 0.4F && j >= enchantmentsAmount * 0.1F && (!gem.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()))) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires at least rare gem"));
                    } else if (j < enchantmentsAmount * 0.7F && j >= enchantmentsAmount * 0.4F && (!gem.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                            && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance()))) {
                        list.add("");
                        list.add(TextFormatting.RED + I18n.format("Requires at least mythic gem"));
                    } else if (j >= enchantmentsAmount * 0.7F && !gem.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance())) {
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
            needsScroll = false;
            scrollPos = 0;
            generatedNames.clear();
            movedToMaxLevel = false;
            scrollCorrectPos = 0;
            sliderCorrectPos = 0;

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
