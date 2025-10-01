package com.enclave.enclavemod.inventory;

import com.enclave.enclavemod.registers.BlocksRegistry;
import com.enclave.enclavemod.registers.ItemsRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContainerEnchantedEnchantment extends Container {
    public IInventory tableInventory;
    private final World worldPointer;
    private final BlockPos position;
    private final Random rand;
    public int xpSeed;
    public int[] enchantLevels;
    public int[] enchantClue;
    public int[] worldClue;
    public int enchantmentsAmount;

    @SideOnly(Side.CLIENT)
    public ContainerEnchantedEnchantment(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerEnchantedEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos)
    {
        this.tableInventory = new InventoryBasic("Enchant", true, 2)
        {
            public int getInventoryStackLimit()
            {
                return 64;
            }
            public void markDirty()
            {
                super.markDirty();
                ContainerEnchantedEnchantment.this.onCraftMatrixChanged(this);
            }
        };
        this.rand = new Random();
        this.enchantLevels = new int[20];
        this.enchantClue = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        this.worldClue = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        this.worldPointer = worldIn;
        this.position = pos;
        this.xpSeed = playerInv.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47)
        {
            public boolean isItemValid(ItemStack stack)
            {
                return true;
            }
            public int getSlotStackLimit()
            {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47)
        {
            private final ArrayList<Item> gems = new ArrayList<Item>();
            {
                gems.add(ItemsRegistry.GEM_COMMON);
                gems.add(ItemsRegistry.GEM_RARE);
                gems.add(ItemsRegistry.GEM_MYTHIC);
                gems.add(ItemsRegistry.GEM_LEGENDARY);
            }

            public boolean isItemValid(ItemStack stack)
            {
                for (Item item : gems)
                    if (stack.getItem() == item) return true;
                return false;
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    protected void broadcastData(IContainerListener crafting)
    {
        for (int i = 0; i < 20; i++) {
            crafting.sendWindowProperty(this, i, this.enchantLevels[i]);
        }

        crafting.sendWindowProperty(this, 20, this.xpSeed & -16);

        for (int i = 0; i < 20; i++) {
            crafting.sendWindowProperty(this, 21 + i, this.enchantClue[i]);
        }

        for (int i = 0; i < 20; i++) {
            crafting.sendWindowProperty(this, 41 + i, this.worldClue[i]);
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        this.broadcastData(listener);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            this.broadcastData(icontainerlistener);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id >= 0 && id <= 19)
        {
            this.enchantLevels[id] = data;
        }
        else if (id == 20)
        {
            this.xpSeed = data;
        }
        else if (id >= 21 && id <= 40)
        {
            this.enchantClue[id - 21] = data;
        }
        else if (id >= 41 && id <= 60)
        {
            this.worldClue[id - 41] = data;
        }
        else
        {
            super.updateProgressBar(id, data);
        }
    }

    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        if (inventoryIn == this.tableInventory)
        {
            ItemStack itemstack = inventoryIn.getStackInSlot(0);

            if (!itemstack.isEmpty() && itemstack.isItemEnchantable())
            {
                if (!this.worldPointer.isRemote)
                {
                    int l = 0;
                    float power = 1;

                    for (int j = -1; j <= 1; ++j)
                    {
                        for (int k = -1; k <= 1; ++k)
                        {
                            if ((j != 0 || k != 0) && this.worldPointer.isAirBlock(this.position.add(k, 0, j)) && this.worldPointer.isAirBlock(this.position.add(k, 1, j)))
                            {
                                power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 0, j * 2));
                                power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 1, j * 2));
                                if (k != 0 && j != 0)
                                {
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 0, j));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k * 2, 1, j));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k, 0, j * 2));
                                    power += net.minecraftforge.common.ForgeHooks.getEnchantPower(worldPointer, position.add(k, 1, j * 2));
                                }
                            }
                        }
                    }

                    if (power > 10) power = 10;

                    this.enchantmentsAmount = 0;

                    ArrayList<EnchantmentData> availableEnchantment = new ArrayList<>();

                    for (Enchantment enchantment : Enchantment.REGISTRY) {
                        if (enchantment == null) continue;

                        if (enchantment.canApplyAtEnchantingTable(itemstack)) {
                            availableEnchantment.add(new EnchantmentData(enchantment, (int)power));
                            this.enchantmentsAmount++;
                        }
                    }

                    for (int id = 0; id < 20; id++) {
                        if (id < availableEnchantment.size()) {
                            EnchantmentData data = availableEnchantment.get(id);

                            this.enchantClue[id] = Enchantment.getEnchantmentID(data.enchantment);
                            this.worldClue[id] = data.enchantmentLevel;
                            this.enchantLevels[id] = 1;
                        } else {
                            this.enchantLevels[id] = 0;
                            this.enchantClue[id] = -1;
                            this.worldClue[id] = -1;
                        }
                    }

                    this.detectAndSendChanges();
                }
            }
            else {
                for (int i = 0; i < 20; ++i)
                {
                    this.enchantLevels[i] = 0;
                    this.enchantClue[i] = -1;
                    this.worldClue[i] = -1;
                }
            }
        }
    }

    public boolean enchantItem(EntityPlayer playerIn, int id)
    {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
        int i = id + 1;
        float n = 1;

        if ((itemstack1.isEmpty() || itemstack1.getCount() < i) && !playerIn.capabilities.isCreativeMode)
        {
            return false;
        }
        else if (itemstack1.isItemEqual(ItemsRegistry.GEM_COMMON.getDefaultInstance())
                || itemstack1.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance())
                || itemstack1.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())
                || itemstack1.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance())
                || playerIn.capabilities.isCreativeMode) {
            if (!this.worldPointer.isRemote)
            {
                List<EnchantmentData> list = this.getEnchantmentList(itemstack, this.worldClue[id]);

                if (itemstack1.isItemEqual(ItemsRegistry.GEM_COMMON.getDefaultInstance())) {
                    n = 0.1F;
                } else if (itemstack1.isItemEqual(ItemsRegistry.GEM_RARE.getDefaultInstance())) {
                    n = 0.4F;
                } else if (itemstack1.isItemEqual(ItemsRegistry.GEM_MYTHIC.getDefaultInstance())) {
                    n = 0.7F;
                } else if (itemstack1.isItemEqual(ItemsRegistry.GEM_LEGENDARY.getDefaultInstance())) {
                    n = 1.0F;
                }

                if (id < 0 || id >= list.size() * n) return false;

                EnchantmentData chosen = list.get(id);

                if (!list.isEmpty())
                {
                    playerIn.onEnchant(itemstack, i);
                    boolean flag = itemstack.getItem() == Items.BOOK;

                    if (flag) {
                        itemstack = new ItemStack(Items.ENCHANTED_BOOK);
                        this.tableInventory.setInventorySlotContents(0, itemstack);
                    }

                    if (flag) {
                            ItemEnchantedBook.addEnchantment(itemstack, chosen);
                    } else {
                            itemstack.addEnchantment(chosen.enchantment, chosen.enchantmentLevel);
                    }

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        itemstack1.shrink(1);

                        if (itemstack1.isEmpty())
                        {
                            this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                        }
                    }

                    playerIn.addStat(StatList.ITEM_ENCHANTED);

                    if (playerIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((EntityPlayerMP)playerIn, itemstack, i);
                    }

                    this.tableInventory.markDirty();
                    this.xpSeed = playerIn.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                    this.worldPointer.playSound((EntityPlayer)null, this.position, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.worldPointer.rand.nextFloat() * 0.1F + 0.9F);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private List<EnchantmentData> getEnchantmentList(ItemStack stack, int level)
    {
        ArrayList<EnchantmentData> availableEnchantment = new ArrayList<>();

        if(stack.isItemEnchanted()) {
            return availableEnchantment;
        }

        for (Enchantment enchantment : Enchantment.REGISTRY) {
            if (enchantment == null) break;

            if (enchantment.canApplyAtEnchantingTable(stack)) {
                availableEnchantment.add(new EnchantmentData(enchantment, level));
            }
        }

        return availableEnchantment;
    }

    public ItemStack getCurrentGem()
    {
        ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        return itemstack.isEmpty() ? Items.AIR.getDefaultInstance() : itemstack;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.worldPointer.isRemote)
        {
            this.clearContainer(playerIn, playerIn.world, this.tableInventory);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (this.worldPointer.getBlockState(this.position).getBlock() != BlocksRegistry.ENCHANTED_ENCHANTMENT_TABLE)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index == 1)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (itemstack1.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE)
            {
                if (!this.mergeItemStack(itemstack1, 1, 2, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1))
                {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.hasTagCompound())// Forge: Fix MC-17431
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(itemstack1.splitStack(1));
                }
                else if (!itemstack1.isEmpty())
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                    itemstack1.shrink(1);
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
