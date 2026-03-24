package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import pantz.mod.common.utils.RandomizerOutput;

public class RandomizerBlock extends DiodeBlock {
    public static final EnumProperty<RandomizerOutput> OUTPUT = EnumProperty.create("output", RandomizerOutput.class);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RandomizerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(OUTPUT, RandomizerOutput.NONE));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.isLocked(level, pos, state)) return;

        boolean powered = state.getValue(POWERED);
        boolean shouldPower = this.shouldTurnOn(level, pos, state);

        if (!powered && shouldPower) {
            RandomizerOutput[] values = RandomizerOutput.values();
            RandomizerOutput randomOutput = values[1 + random.nextInt(values.length - 1)];

            level.setBlockAndUpdate(pos, state.setValue(POWERED, true).setValue(OUTPUT, randomOutput));

        } else if (powered && !shouldPower) {
            level.setBlockAndUpdate(pos, state.setValue(POWERED, false).setValue(OUTPUT, RandomizerOutput.NONE));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        RandomSource random = level.getRandom();

        boolean shouldPower = this.shouldTurnOn(level, pos, this.defaultBlockState());
        RandomizerOutput output = RandomizerOutput.NONE;

        if (shouldPower) {
            RandomizerOutput[] values = RandomizerOutput.values();
            output = values[1 + random.nextInt(values.length - 1)];
        }

        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(POWERED, shouldPower).setValue(OUTPUT, output);
    }

    @Override
    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos inputPos = pos.relative(direction);
        return level.getSignal(inputPos, direction);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        if (!state.getValue(POWERED)) return 0;

        Direction facing = state.getValue(FACING);
        RandomizerOutput output = state.getValue(OUTPUT);

        Direction outputDirection = switch (output) {
            case FRONT -> facing;
            case LEFT -> facing.getCounterClockWise();
            case RIGHT -> facing.getClockWise();
            case NONE -> null;
        };

        return side == outputDirection ? 15 : 0;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (!level.isClientSide()) {
            if (this.shouldTurnOn(level, pos, state)) {
                level.scheduleTick(pos, this, this.getDelay(state));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING, OUTPUT);
    }

    @Override
    protected int getDelay(BlockState pState) {
        return 2;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            Direction direction = state.getValue(FACING);
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double y = pos.getY() + 0.4 + (random.nextDouble() - 0.5) * 0.2;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;

            double offsetX = direction.getStepX() * 0.1;
            double offsetZ = direction.getStepZ() * 0.1;

            level.addParticle(DustParticleOptions.REDSTONE, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        }
    }
}
