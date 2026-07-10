package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import pantz.mod.core.registry.PMParticleTypes;

public class FeatherBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

    public FeatherBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState p_221566_, BlockGetter p_221567_, BlockPos p_221568_) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_221556_, BlockGetter p_221557_, BlockPos p_221558_, CollisionContext p_221559_) {
        return Shapes.block();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return true;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        if (level.isClientSide) {
            RandomSource random = level.getRandom();

            int count = 8 + random.nextInt(5);

            for (int i = 0; i < count; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double radius = 0.6 + random.nextDouble();

                double x = pos.getX() + 0.4 + Math.cos(angle) * radius;
                double y = pos.getY() + SHAPE.max(Direction.Axis.Y) + 0.1;
                double z = pos.getZ() + 0.4 + Math.sin(angle) * radius;

                double motionX = (random.nextDouble() - 0.5) * 0.02;
                double motionY = 0.025 + random.nextDouble() * 0.02;
                double motionZ = (random.nextDouble() - 0.5) * 0.02;

                level.addParticle(PMParticleTypes.FEATHER.get(), x, y, z, motionX, motionY, motionZ);
            }
        }
    }

}
