package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IceLanternBlock extends LanternBlock {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(5, 0, 5, 11, 7, 11),
            Block.box(6, 7, 6, 10, 8, 10)
    );
    private static final VoxelShape HANGING_SHAPE = Shapes.or(
            Block.box(5, 2, 5, 11, 9, 11),
            Block.box(6, 9, 6, 10, 10, 10)
    );
    public IceLanternBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(HANGING, false)
                .setValue(LIT, false)
                .setValue(WATERLOGGED, false));

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HANGING, LIT, WATERLOGGED);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        if (random.nextInt(5) != 0) return;

        Direction direction = Direction.getRandom(random);
        if (direction != Direction.UP) {
            BlockPos blockPos = pos.relative(direction);
            BlockState blockState = level.getBlockState(blockPos);
            if (!blockState.canOcclude()) {
                double x = direction.getStepX() == 0 ? random.nextDouble() : 0.25 * (double) direction.getStepX() * 0.6D;
                double y = direction.getStepY() == 0 ? random.nextDouble() : 0.25 * (double) direction.getStepY() * 0.6D;
                double z = direction.getStepZ() == 0 ? random.nextDouble() : 0.25 * (double) direction.getStepZ() * 0.6D;
                level.addParticle(ParticleTypes.SNOWFLAKE, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, 0.0D, 0.0D, 0.0D);
                }
            }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            level.setBlock(pos, state.cycle(LIT), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

}
