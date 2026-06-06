package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import pantz.mod.core.PMConfig;
import pantz.mod.core.other.PMCriteriaTriggers;
import pantz.mod.core.other.tags.PMEntityTypeTags;
import pantz.mod.core.other.tags.PMItemTags;
import pantz.mod.core.registry.PMBlockEntityTypes;

import java.util.List;
import java.util.Optional;

public class EnderScannerBlockEntity extends BlockEntity {
    public EnderScannerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.ENDER_SCANNER.get(), pPos, pBlockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EnderScannerBlockEntity be) {
        int currentSignal = be.getPowerFromLooking(level, pos);
        int oldSignal = state.getValue(BlockStateProperties.POWER);;

        if (currentSignal != state.getValue(BlockStateProperties.POWER)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.POWER, currentSignal), 3);
            level.updateNeighborsAt(pos, state.getBlock());
        }

        if (currentSignal != oldSignal) {
            level.updateNeighborsAt(pos, state.getBlock());
        }

        if (currentSignal == 15 && oldSignal < 15) {
            List<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(8));
            for (ServerPlayer player : players) {
                PMCriteriaTriggers.LOOK_AT_SCANNER.trigger(player);
            }
        }
    }

    public int getPowerFromLooking(Level level, BlockPos pos) {
        AABB box = new AABB(pos).inflate(0.01);
        int distance = PMConfig.Common.COMMON.enderScannerDetectionRadius.get();
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box.inflate(distance));

        double bestPrecision = 0;

        for (LivingEntity entity : entities) {
            boolean isImmune = false;

            Vec3 eyePos = entity.getEyePosition(1.0f);
            Vec3 lookAngle = entity.getLookAngle().normalize();
            Vec3 endPoint = eyePos.add(lookAngle.scale(distance));

            Optional<Vec3> hitOptional = box.clip(eyePos, endPoint);
            if (hitOptional.isEmpty()) continue;

            Vec3 hit = hitOptional.get();

            for (ItemStack armor : entity.getArmorSlots()) {
                if (armor.is(PMItemTags.ENDER_SCANNER_IMMUNITIES)) {
                    isImmune = true;
                    break;
                }
            }

            if (entity.getType().is(PMEntityTypeTags.ENDER_SCANNER_IMMUNE_TYPES)) {
                isImmune = true;
            }

            if (isImmune) {
                continue;
            }

            Direction hitFace = null;
            for (Direction direction : Direction.values()) {
                Vec3 facePoint = Vec3.atCenterOf(pos).add(Vec3.atLowerCornerOf(direction.getNormal()).scale(0.5));
                if (Math.abs(facePoint.distanceTo(hit) - 0) < 1e-6) {
                    hitFace = direction;
                    break;
                }
            }

            if (hitFace == null) {
                hitFace = Direction.getNearest(hit.x - (pos.getX() + 0.5), hit.y - (pos.getY() + 0.5), hit.z - (pos.getZ() + 0.5));
            }

            Vec3 faceCenter = Vec3.atCenterOf(pos).add(Vec3.atLowerCornerOf(hitFace.getNormal()).scale(0.5));

            double faceDistance = faceCenter.distanceTo(hit);
            double maxDist = 0.65;

            double precision;
            if (faceDistance <= 0.05) {
                precision = 1.0;
            } else {
                double rawPrecision = 1.0 - (faceDistance / maxDist);
                precision = Math.pow(Mth.clamp(rawPrecision, 0, 1), 1.2);
            }
            precision = Mth.clamp(precision, 0, 1);
            bestPrecision = Math.max(bestPrecision, precision);
        }

        return bestPrecision > 0.05 ? Mth.clamp(Math.round((float)(bestPrecision * 15)), 0, 15) : 0;
    }
}
