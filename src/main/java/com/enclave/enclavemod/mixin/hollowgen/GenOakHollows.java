package com.enclave.enclavemod.mixin.hollowgen;

import com.enclave.enclavemod.blocks.tile.TileEntityHollow;
import com.enclave.enclavemod.registers.BlocksRegistry;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.storage.loot.LootTableList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.Random;

import static com.enclave.enclavemod.blocks.HollowBlock.FACING;

@Mixin(WorldGenCanopyTree.class)
public abstract class GenOakHollows extends WorldGenAbstractTree {
    @Shadow
    private static final IBlockState DARK_OAK_LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);

    @Shadow
    private static final IBlockState DARK_OAK_LEAVES = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    private static final IBlockState HOLLOW = BlocksRegistry.HOLLOW.getDefaultState();

    private static final IBlockState ACORN = BlocksRegistry.ACORN.getDefaultState();

    HashSet<BlockPos> hollowPos = new HashSet<>();

    public GenOakHollows(boolean notify) {
        super(notify);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        boolean hasHollow = false;
        int i = rand.nextInt(3) + rand.nextInt(2) + 6;
        int j = position.getX();
        int k = position.getY();
        int l = position.getZ();

        if (k >= 1 && k + i + 1 < 256)
        {
            BlockPos blockpos = position.down();
            IBlockState state = worldIn.getBlockState(blockpos);
            boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, blockpos, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.SAPLING));

            if (!(isSoil && position.getY() < worldIn.getHeight() - i - 1))
            {
                return false;
            }
            else if (!this.placeTreeOfHeight(worldIn, position, i))
            {
                return false;
            }
            else
            {
                this.onPlantGrow(worldIn, blockpos, position);
                this.onPlantGrow(worldIn, blockpos.east(), position);
                this.onPlantGrow(worldIn, blockpos.south(), position);
                this.onPlantGrow(worldIn, blockpos.south().east(), position);
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                int i1 = i - rand.nextInt(4);
                int j1 = 2 - rand.nextInt(3);
                int k1 = j;
                int l1 = l;
                int i2 = k + i - 1;

                for (int j2 = 0; j2 < i; ++j2)
                {
                    if (j2 >= i1 && j1 > 0)
                    {
                        k1 += enumfacing.getFrontOffsetX();
                        l1 += enumfacing.getFrontOffsetZ();
                        --j1;
                    }

                    int k2 = k + j2;
                    BlockPos blockpos1 = new BlockPos(k1, k2, l1);
                    state = worldIn.getBlockState(blockpos1);

                    if (state.getBlock().isAir(state, worldIn, blockpos1) || state.getBlock().isLeaves(state, worldIn, blockpos1))
                    {

                        if (rand.nextInt(10) == 0 && !hasHollow) {
                            this.placeHollow(worldIn, blockpos1);
                            hasHollow = true;
                        } else {
                            this.placeLogAt(worldIn, blockpos1);
                        }
                        if (rand.nextInt(10) == 0 && !hasHollow) {
                            this.placeHollow(worldIn, blockpos1.east());
                            hasHollow = true;
                        } else {
                            this.placeLogAt(worldIn, blockpos1.east());
                        }
                        if (rand.nextInt(10) == 0 && !hasHollow) {
                            this.placeHollow(worldIn, blockpos1.south());
                            hasHollow = true;
                        } else {
                            this.placeLogAt(worldIn, blockpos1.south());
                        }
                        if (rand.nextInt(10) == 0 && !hasHollow) {
                            this.placeHollow(worldIn, blockpos1.east().south());
                            hasHollow = true;
                        } else {
                            this.placeLogAt(worldIn, blockpos1.east().south());
                        }
                    }
                }

                for (int i3 = -2; i3 <= 0; ++i3)
                {
                    for (int l3 = -2; l3 <= 0; ++l3)
                    {
                        int k4 = -1;
                        this.placeLeafAt(worldIn, k1 + i3, i2 + k4, l1 + l3);
                        this.placeAcorn(worldIn, k1 + i3, i2 + k4, l1 + l3, rand);
                        this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
                        this.placeAcorn(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3, rand);
                        this.placeLeafAt(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
                        this.placeAcorn(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3, rand);
                        this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
                        this.placeAcorn(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3, rand);

                        if ((i3 > -2 || l3 > -1) && (i3 != -1 || l3 != -2))
                        {
                            k4 = 1;
                            this.placeLeafAt(worldIn, k1 + i3, i2 + k4, l1 + l3);
                            this.placeAcorn(worldIn, k1 + i3, i2 + k4, l1 + l3, rand);
                            this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3);
                            this.placeAcorn(worldIn, 1 + k1 - i3, i2 + k4, l1 + l3, rand);
                            this.placeLeafAt(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3);
                            this.placeAcorn(worldIn, k1 + i3, i2 + k4, 1 + l1 - l3, rand);
                            this.placeLeafAt(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
                            this.placeAcorn(worldIn, 1 + k1 - i3, i2 + k4, 1 + l1 - l3, rand);
                        }
                    }
                }

                if (rand.nextBoolean())
                {
                    this.placeLeafAt(worldIn, k1, i2 + 2, l1);
                    this.placeAcorn(worldIn, k1, i2 + 2, l1, rand);
                    this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1);
                    this.placeAcorn(worldIn, k1 + 1, i2 + 2, l1, rand);
                    this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1 + 1);
                    this.placeAcorn(worldIn, k1 + 1, i2 + 2, l1 + 1, rand);
                    this.placeLeafAt(worldIn, k1, i2 + 2, l1 + 1);
                    this.placeAcorn(worldIn, k1, i2 + 2, l1 + 1, rand);
                }

                for (int j3 = -3; j3 <= 4; ++j3)
                {
                    for (int i4 = -3; i4 <= 4; ++i4)
                    {
                        if ((j3 != -3 || i4 != -3) && (j3 != -3 || i4 != 4) && (j3 != 4 || i4 != -3) && (j3 != 4 || i4 != 4) && (Math.abs(j3) < 3 || Math.abs(i4) < 3))
                        {
                            this.placeLeafAt(worldIn, k1 + j3, i2, l1 + i4);
                            this.placeAcorn(worldIn, k1 + j3, i2, l1 + i4, rand);
                        }
                    }
                }

                for (int k3 = -1; k3 <= 2; ++k3)
                {
                    for (int j4 = -1; j4 <= 2; ++j4)
                    {
                        if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) <= 0)
                        {
                            int l4 = rand.nextInt(3) + 2;

                            for (int i5 = 0; i5 < l4; ++i5)
                            {
                                this.placeLogAt(worldIn, new BlockPos(j + k3, i2 - i5 - 1, l + j4));
                            }

                            for (int j5 = -1; j5 <= 1; ++j5)
                            {
                                for (int l2 = -1; l2 <= 1; ++l2)
                                {
                                    this.placeLeafAt(worldIn, k1 + k3 + j5, i2, l1 + j4 + l2);
                                    this.placeAcorn(worldIn, k1 + k3 + j5, i2, l1 + j4 + l2, rand);
                                }
                            }

                            for (int k5 = -2; k5 <= 2; ++k5)
                            {
                                for (int l5 = -2; l5 <= 2; ++l5)
                                {
                                    if (Math.abs(k5) != 2 || Math.abs(l5) != 2)
                                    {
                                        this.placeLeafAt(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
                                        this.placeAcorn(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5, rand);
                                    }
                                }
                            }
                        }
                    }
                }
                for (BlockPos pos : hollowPos) {
                    rotateHollows(worldIn, pos);
                    spawnLoot(worldIn, pos);
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Shadow
    private boolean placeTreeOfHeight(World worldIn, BlockPos pos, int height) {
        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void placeLogAt(World worldIn, BlockPos pos)
    {
        if (this.canGrowInto(worldIn.getBlockState(pos).getBlock()))
        {
            this.setBlockAndNotifyAdequately(worldIn, pos, DARK_OAK_LOG);
        }
    }

    @Shadow
    private void placeLeafAt(World worldIn, int x, int y, int z) {
    }

    private void onPlantGrow(World world, BlockPos pos, BlockPos source) {
    }

    public void placeHollow(World worldIn, BlockPos pos) {
        if (this.canGrowInto(worldIn.getBlockState(pos).getBlock())) {
            this.setBlockAndNotifyAdequately(worldIn, pos, HOLLOW);
            hollowPos.add(pos);
        }
    }

    public void placeAcorn(World worldIn, int x, int y, int z, Random rand) {
        BlockPos pos = new BlockPos(x, y, z);

        while (worldIn.isAirBlock(pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.LEAVES || worldIn.getBlockState(pos.down()).getBlock() == Blocks.LEAVES2) {
            pos = pos.down();
        }

        if (this.canGrowInto(worldIn.getBlockState(pos).getBlock())
                && worldIn.getBlockState(pos.down()).getBlock().isNormalCube(worldIn.getBlockState(pos.down()), worldIn, pos.down())
                && worldIn.getBlockState(pos.down()).getBlock() != Blocks.LEAVES && worldIn.getBlockState(pos.down()).getBlock() != Blocks.LEAVES2
                && worldIn.getBlockState(pos.down()).getBlock() != Blocks.LOG && worldIn.getBlockState(pos.down()).getBlock() != Blocks.LOG2) {
            if (rand.nextInt(15) == 0) {
                this.setBlockAndNotifyAdequately(worldIn, pos, ACORN);
            }
        }
    }

    public void rotateHollows(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        EnumFacing newFacing = EnumFacing.SOUTH;

        if (worldIn.isAirBlock(pos.north())
                || state.getCollisionBoundingBox(worldIn, pos.north()) == null
                || worldIn.getBlockState(pos.north()).getBlock() == BlocksRegistry.ACORN) {
            newFacing = EnumFacing.NORTH;
        } else if (worldIn.isAirBlock(pos.east())
                || state.getCollisionBoundingBox(worldIn, pos.east()) == null
                || worldIn.getBlockState(pos.east()).getBlock() == BlocksRegistry.ACORN) {
            newFacing = EnumFacing.EAST;
        } else if (worldIn.isAirBlock(pos.west())
                || state.getCollisionBoundingBox(worldIn, pos.west()) == null
                || worldIn.getBlockState(pos.west()).getBlock() == BlocksRegistry.ACORN) {
            newFacing = EnumFacing.WEST;
        } else if (worldIn.isAirBlock(pos.south())
                || state.getCollisionBoundingBox(worldIn, pos.south()) == null
                || worldIn.getBlockState(pos.south()).getBlock() == BlocksRegistry.ACORN) {
            newFacing = EnumFacing.SOUTH;
        }

        if (!worldIn.isAirBlock(pos)) {
            worldIn.setBlockState(pos, state.withProperty(FACING, newFacing), 2);
        }
    }

    public void spawnLoot(World worldIn, BlockPos pos) {
        ((TileEntityHollow)worldIn.getTileEntity(pos)).setLootTable(LootTableList.CHESTS_NETHER_BRIDGE, worldIn.rand.nextLong());
    }
}
