package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import pantz.mod.common.utils.LogicGateConditions;

public class AdvancedLogicGateBlock extends LogicGateBlock {
    public static final BooleanProperty INPUT_LEFT = BooleanProperty.create("input_left");
    public static final BooleanProperty INPUT_RIGHT = BooleanProperty.create("input_right");
    public static final BooleanProperty INPUT_BACK = BooleanProperty.create("input_back");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;


    public AdvancedLogicGateBlock(Properties props, LogicGateConditions logic) {
        super(props, logic);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(INPUT_LEFT, false)
                .setValue(INPUT_RIGHT, false)
                .setValue(INPUT_BACK, false)
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState updateInputs(LevelAccessor level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);

        BlockPos rightPos = pos.relative(facing.getCounterClockWise());
        BlockPos leftPos = pos.relative(facing.getClockWise());
        BlockPos backPos = pos.relative(facing);

        boolean left = level.hasSignal(leftPos, facing.getClockWise());
        boolean right = level.hasSignal(rightPos, facing.getCounterClockWise());
        boolean back = level.hasSignal(backPos, facing);

        boolean powered = this.logic.apply(back, left, right);

        return state.setValue(INPUT_LEFT, left).setValue(INPUT_RIGHT, right).setValue(INPUT_BACK, back).setValue(POWERED, powered);
    }

    @Override
    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        Direction back = state.getValue(FACING);
        Direction right = state.getValue(FACING).getCounterClockWise();
        Direction left = state.getValue(FACING).getClockWise();

        boolean inputBack = getPowerFromInputs(level, pos, back) > 0;
        boolean inputLeft = getPowerFromInputs(level, pos, left) > 0;
        boolean inputRight = getPowerFromInputs(level, pos, right) > 0;

        return logic.apply(inputBack, inputLeft, inputRight);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, INPUT_LEFT, INPUT_RIGHT, INPUT_BACK);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED) || state.getValue(INPUT_BACK) || state.getValue(INPUT_LEFT) || state.getValue(INPUT_RIGHT)) {
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
