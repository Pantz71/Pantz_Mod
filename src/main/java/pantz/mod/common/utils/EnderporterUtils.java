package pantz.mod.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.common.block.EnderporterBlock;
import pantz.mod.core.PMConfig;
import pantz.mod.core.other.PMCriteriaTriggers;
import pantz.mod.core.registry.PMSoundEvents;

import java.util.Optional;

public class EnderporterUtils {
    public static boolean redirectTeleport(ThrownEnderpearl pearl, ServerLevel level, ServerPlayer player) {
        BlockPos pearlPos = pearl.blockPosition();
        Optional<BlockPos> nearestEnderporter = nearestChargedEnderporter(level, pearlPos);

        if (nearestEnderporter.isPresent()) {
            BlockPos targetPos = nearestEnderporter.get().above();

            player.teleportTo(level, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, player.getYRot(), player.getXRot());

            level.playSound(null, targetPos, PMSoundEvents.ENDERPORTER_DEPLETE.get(), SoundSource.BLOCKS);

            BlockState state = level.getBlockState(nearestEnderporter.get());
            if (state.getBlock() instanceof EnderporterBlock) {
                int currentCharge = state.getValue(EnderporterBlock.ENDERPORTER_CHARGE);
                if (currentCharge > 0) {
                    level.setBlock(nearestEnderporter.get(), state.setValue(EnderporterBlock.ENDERPORTER_CHARGE, currentCharge - 1), 3);
                }
            }
            pearl.discard();
            PMCriteriaTriggers.REDIRECT_TELEPORTATION.trigger(player);
            return true;
        }

        return false;
    }

    private static Optional<BlockPos> nearestChargedEnderporter(ServerLevel level, BlockPos fromPos) {
        int range = PMConfig.Common.COMMON.enderporterDetectionRadius.get();
        Optional<BlockPos> closest = Optional.empty();
        double minDistanceSquare = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.betweenClosed(fromPos.offset(-range, -range, -range), fromPos.offset(range, range, range))) {
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof EnderporterBlock && state.getValue(EnderporterBlock.ENDERPORTER_CHARGE) > 0) {
                double distance = pos.distSqr(fromPos);

                if (distance < minDistanceSquare) {
                    minDistanceSquare = distance;
                    closest = Optional.of(pos.immutable());
                }
            }
        }

        return closest;
    }
}
