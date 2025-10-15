package com.enclave.enclavemod.entity.ai.courier.world;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class DoorFinder {
    private HashSet<BlockPos> visitedDoors = new HashSet<>();

    public boolean isNearDoor(BlockPos pos, EntityCreature entity) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int entityX = entity.getPosition().getX();
        int entityY = entity.getPosition().getY();
        int entityZ = entity.getPosition().getZ();

        return Math.abs(x - entityX) <= 1 && Math.abs(z - entityZ) <= 1 && Math.abs(y - entityY) <= 1;
    }

    public BlockPos findNearestDoor(EntityCreature entity) {
        BlockPos entityPos = entity.getPosition();
        int range = 32;
        BlockPos best = null;
        double bestDistSq = Double.MAX_VALUE;
        BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();

        int ex = entityPos.getX(), ey = entityPos.getY(), ez = entityPos.getZ();

        for (int dx = -range; dx <= range; dx++) {
            int x = ex + dx;
            for (int dy = -2; dy <= 2; dy++) {
                int y = ey + dy;
                for (int dz = -range; dz <= range; dz++) {
                    int z = ez + dz;
                    mPos.setPos(x, y, z);
                    IBlockState iBlockState = entity.world.getBlockState(mPos);
                    if (!(iBlockState.getBlock() instanceof BlockDoor)) continue;
                    BlockPos normalizedDoorPos = normalizeDoorPos(entity.world, mPos);
                    if (visitedDoors.contains(normalizedDoorPos)) continue;
                    double d = entityPos.distanceSq(normalizedDoorPos);
                    if (d < bestDistSq) {
                        bestDistSq = d;
                        best = normalizedDoorPos.toImmutable();
                    }
                }
            }
        }
        return best;
    }


    private BlockPos normalizeDoorPos(World world, BlockPos pos) {
        IBlockState iBlockState = world.getBlockState(pos);
        if (iBlockState.getBlock() instanceof BlockDoor) {
            BlockDoor.EnumDoorHalf half = iBlockState.getValue(BlockDoor.HALF);
            if (half.getName().equals("upper")) {
                return pos.down();
            }
        }
        return pos;
    }

    public void clearVisitedDoors() {
        visitedDoors.clear();
    }

    public void addVisitedDoor(BlockPos pos) {
        visitedDoors.add(pos);
    }
}
