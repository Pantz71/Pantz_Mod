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

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        int currentSignal = getPowerFromLooking(level, pos);
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

    // Check vectors
    public int getPowerFromLooking(Level level, BlockPos pos) {
        AABB box = new AABB(pos);
        int distance = PMConfig.Common.COMMON.enderScannerDetectionRadius.get();
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box.inflate(distance));

        double maxPrecision = 0;

        for (LivingEntity entity : entities) {
            boolean isImmune = false;

            Vec3 eyePos = entity.getEyePosition(1.0f);
            Vec3 lookAngle = entity.getLookAngle().normalize();
            Vec3 endPoint = eyePos.add(lookAngle.scale(8.0));

            Optional<Vec3> hitOptional = box.clip(eyePos, endPoint);
            if (hitOptional.isEmpty()) continue;

            Vec3 hit = hitOptional.get();

            // Check if entity is wearing a piece of armor that are immune to scanner detection
            for (ItemStack armor : entity.getArmorSlots()) {
                if (armor.is(PMItemTags.ENDER_SCANNER_IMMUNITIES)) {
                    isImmune = true;
                    break;
                }
            }

            // or the entity is just immune to it
            if (entity.getType().is(PMEntityTypeTags.ENDER_SCANNER_IMMUNE_TYPES)) {
                isImmune = true;
            }

            if (isImmune) {
                continue;
            }

            // Looking at the center of each faces
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

            // Determine the power output
            double distance1 = faceCenter.distanceTo(hit);
            double maxDist = 0.65;

            double precision;
            if (distance1 <= 0.05) {
                precision = 1.0;
            } else {
                double raw = 1.0 - (distance1 / maxDist);
                precision = Math.pow(Mth.clamp(raw, 0, 1), 1.2);
            }
            precision = Mth.clamp(precision, 0, 1);

            // Log
            // System.out.println("✔ HitFace: " + hitFace);
            // System.out.println("✔ EyePos: " + eyePos + " → HitPos: " + hit);
            // System.out.println("✔ FaceCenter: " + faceCenter + " → Dist: " + distance1);
            // System.out.println("✔ Precision: " + precision);
            // System.out.println("✔ Power: " + (int)(precision * 15));

            maxPrecision = Math.max(maxPrecision, precision);
        }

        return maxPrecision > 0.05 ? (int)(maxPrecision * 15) : 0;
    }
}
