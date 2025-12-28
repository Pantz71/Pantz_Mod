package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import pantz.mod.common.utils.LogicGateConditions;

public class LogicGateBlock extends DiodeBlock {
    public static final BooleanProperty INPUT_LEFT = BooleanProperty.create("input_left");
    public static final BooleanProperty INPUT_RIGHT = BooleanProperty.create("input_right");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public final LogicGateConditions logic;

    public LogicGateBlock(Properties props, LogicGateConditions logic) {
        super(props);
        this.logic = logic;
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(INPUT_LEFT, false)
                .setValue(INPUT_RIGHT, false)
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide()) {
            BlockState updated = updateInputs(level, pos, state);
            level.setBlock(pos, updated, 2);
        }
    }

    @Override
    protected int getDelay(BlockState blockState) {
        return 1;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!level.isClientSide()) {
            BlockState updatedInputs = updateInputs(level, pos, state);

            if (!updatedInputs.equals(state)) {
                level.scheduleTick(pos, this, 1);
            }
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState updatedInputs = updateInputs(level, pos, state);

        if (!updatedInputs.equals(state)) {
            level.setBlock(pos, updatedInputs, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
        }
    }

    public BlockState updateInputs(LevelAccessor level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);

        BlockPos rightPos = pos.relative(facing.getCounterClockWise());
        BlockPos leftPos = pos.relative(facing.getClockWise());

        boolean left = level.hasSignal(leftPos, facing.getClockWise());
        boolean right = level.hasSignal(rightPos, facing.getCounterClockWise());

        boolean powered = this.logic.apply(left, right);

        return state.setValue(INPUT_LEFT, left).setValue(INPUT_RIGHT, right).setValue(POWERED, powered);
    }

    @Override
    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        Direction right = state.getValue(FACING).getCounterClockWise();
        Direction left = state.getValue(FACING).getClockWise();

        boolean inputLeft = getPowerFromInputs(level, pos, left) > 0;
        boolean inputRight = getPowerFromInputs(level, pos, right) > 0;

        return logic.apply(inputLeft, inputRight);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, INPUT_LEFT, INPUT_RIGHT);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED) || state.getValue(INPUT_LEFT) || state.getValue(INPUT_RIGHT)) {
            Direction direction = state.getValue(FACING);
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double y = pos.getY() + 0.4 + (random.nextDouble() - 0.5) * 0.2;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;

            double offsetX = direction.getStepX() * 0.1;
            double offsetZ = direction.getStepZ() * 0.1;

            level.addParticle(DustParticleOptions.REDSTONE, x + offsetX, y, z + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    public static int getPowerFromInputs(Level level, BlockPos pos, Direction direction) {
        BlockPos side = pos.relative(direction);
        int signal = level.getSignal(side, direction);
        if (signal >= 15) {
            return signal;
        } else {
            BlockState state = level.getBlockState(side);
            return Math.max(signal, state.getBlock() instanceof RedStoneWireBlock ? state.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }
}
