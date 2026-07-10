package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import net.neoforged.neoforge.common.ticket.AABBTicket;
import pantz.mod.common.block.SprinklerBlock;
import pantz.mod.core.PMConfig;
import pantz.mod.core.registry.PMBlockEntityTypes;

public class SprinklerBlockEntity extends BlockEntity {
    private AABBTicket waterTicket;
    public static final int RANGE = 4;
    public static final int VERTICAL_RANGE = 5;
    public SprinklerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.SPRINKLER.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SprinklerBlockEntity be) {
        boolean isFilled = state.getValue(SprinklerBlock.FILLED);
        if (!level.isClientSide()) {
            if (!(level instanceof ServerLevel serverLevel)) return;

            if (isFilled) {
                if (be.waterTicket == null || !be.waterTicket.isValid()) {
                    be.refreshTicket();
                }
                be.accelerateCropArea(serverLevel, pos);
            } else if (be.waterTicket != null){
                be.removeTicket();
            }
        } else if (isFilled && level.getGameTime() % 2 == 0) {
            be.spawnParticles(level, pos, level.getRandom());
        }
    }

    private void spawnParticles(Level level, BlockPos pos, RandomSource random) {
        int count = 15 + random.nextInt(6);
        for (int i = 0; i < count; i++) {
            double offsetX = (random.nextDouble() - 0.5) * (RANGE * 2);
            double offsetZ = (random.nextDouble() - 0.5) * (RANGE * 2);

            double x = pos.getX() + 0.5 + offsetX;
            double z = pos.getZ() + 0.5 + offsetZ;
            double y = pos.getY() + 0.2;

            level.addParticle(ParticleTypes.RAIN, x, y, z, 0.0, -0.5, 0.0);
        }
    }

    private void refreshTicket() {
        if (this.level != null && !this.level.isClientSide()) {
            AABB area = new AABB(this.worldPosition).inflate(RANGE, 0, RANGE).expandTowards(0, -VERTICAL_RANGE, 0);
            this.waterTicket = FarmlandWaterManager.addAABBTicket(this.level, area);
        }
    }

    private void accelerateCropArea(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        double boostChance = PMConfig.Common.COMMON.sprinklerBoostChance.get();

        for (int i = 0; i < 3; i++) {
            if (random.nextDouble() < boostChance) {
                int dx = random.nextInt(RANGE * 2 + 1) - RANGE;
                int dz = random.nextInt(RANGE * 2 + 1) - RANGE;
                int dy = -random.nextInt(VERTICAL_RANGE + 1);

                BlockPos targetPos = pos.offset(dx, dy, dz);
                BlockState state = level.getBlockState(targetPos);

                if (state.getBlock() instanceof BonemealableBlock growable) {
                    if (growable.isValidBonemealTarget(level, targetPos, state)) {
                        state.randomTick(level, targetPos, random);
                    }
                }
            }
        }
    }

    public void removeTicket() {
        if (this.waterTicket != null) {
            this.waterTicket.invalidate();
            this.waterTicket = null;
        }
    }

    @Override
    public void setRemoved() {
        removeTicket();
        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded() {
        removeTicket();
        super.onChunkUnloaded();
    }
}
