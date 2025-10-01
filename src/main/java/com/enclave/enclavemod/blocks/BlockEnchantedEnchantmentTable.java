package com.enclave.enclavemod.blocks;
import com.enclave.enclavemod.EnclaveMod;
import com.enclave.enclavemod.blocks.tile.TileEntityCounter;
import com.enclave.enclavemod.blocks.tile.TileEntityEnchantedEnchantmentTable;
import com.enclave.enclavemod.inventory.GuiHandler;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEnchantedEnchantmentTable extends BlockEnchantmentTable {
    public BlockEnchantedEnchantmentTable(String name) {
        super();
        this.setUnlocalizedName("enclavemod." + name);
        this.setRegistryName("enclavemod", name);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEnchantedEnchantmentTable();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityEnchantedEnchantmentTable) {
                playerIn.openGui(EnclaveMod.instance, GuiHandler.ENCHANTED_ENCHANTMENT_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityEnchantedEnchantmentTable)
            {
                ((TileEntityEnchantedEnchantmentTable)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    public Class<TileEntityEnchantedEnchantmentTable> getTileEntityClass() {
        return TileEntityEnchantedEnchantmentTable.class;
    }
}
