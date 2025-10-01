package com.enclave.enclavemod.blocks;

import com.enclave.enclavemod.EnclaveMod;
import com.enclave.enclavemod.blocks.tile.TileEntityCounter;
import com.enclave.enclavemod.blocks.tile.TileEntityHollow;
import com.enclave.enclavemod.inventory.GuiHandler;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class HollowBlock extends Block implements ITileEntityProvider {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public HollowBlock(String name)
    {
        super(Material.WOOD);
        this.setRegistryName("enclavemod", name);
        this.setUnlocalizedName("enclavemod." + name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(FACING, enumfacing);
        BlockPos blockpos = pos.north();
        BlockPos blockpos1 = pos.south();
        BlockPos blockpos2 = pos.west();
        BlockPos blockpos3 = pos.east();
        boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
        boolean flag1 = this == worldIn.getBlockState(blockpos1).getBlock();
        boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
        boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();

        if (!flag && !flag1 && !flag2 && !flag3)
        {
            worldIn.setBlockState(pos, state, 3);
        }
        else if (enumfacing.getAxis() != EnumFacing.Axis.X || !flag && !flag1)
        {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3))
            {
                if (flag2)
                {
                    worldIn.setBlockState(blockpos2, state, 3);
                }
                else
                {
                    worldIn.setBlockState(blockpos3, state, 3);
                }

                worldIn.setBlockState(pos, state, 3);
            }
        }
        else
        {
            if (flag)
            {
                worldIn.setBlockState(blockpos, state, 3);
            }
            else
            {
                worldIn.setBlockState(blockpos1, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        }

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityHollow)
            {
                ((TileEntityHollow)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);

            if (ilockablecontainer != null)
            {
                playerIn.openGui(EnclaveMod.instance, GuiHandler.HOLLOW_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
                playerIn.addStat(StatList.CHEST_OPENED);
            }
            return true;
        }
    }

    @Nullable
    public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
    {
        return this.getContainer(worldIn, pos, false);
    }

    @Nullable
    public ILockableContainer getContainer(World worldIn, BlockPos pos, boolean allowBlocking)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityHollow)
        {
            return (TileEntityHollow) tileentity;
        }
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityHollow();
    }

    public Class<TileEntityHollow> getTileEntityClass() {
        return TileEntityHollow.class;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        return super.rotateBlock(world, pos, axis);
    }
}
