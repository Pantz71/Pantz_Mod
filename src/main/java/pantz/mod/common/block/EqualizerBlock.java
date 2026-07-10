package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class EqualizerBlock extends DiodeBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public EqualizerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(POWER, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            int current = state.getValue(POWER);
            int nextPower = (current + 1) % 16;
            BlockState nextState = state.setValue(POWER, nextPower);
            level.setBlockAndUpdate(pos, nextState);
            this.checkTickOnNeighbor(level, pos, nextState);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.isLocked(level, pos, state)) return;

        boolean shouldPower = this.shouldTurnOn(level, pos, state);
        boolean isPowered = state.getValue(POWERED);

        if (shouldPower != isPowered) {
            level.setBlockAndUpdate(pos, state.setValue(POWERED, shouldPower));
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        int input = this.getInputSignal(level, pos, state);
        int required = state.getValue(POWER);

        return input == required;
    }

    @Override
    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos frontPos = pos.relative(direction);

        int signal = level.getSignal(frontPos, direction);

        if (signal < 15) {
            BlockState frontState = level.getBlockState(frontPos);
            if (frontState.is(Blocks.REDSTONE_WIRE)) {
                signal = Math.max(signal, frontState.getValue(RedStoneWireBlock.POWER));
            }
        }

        return signal;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();

        BlockState state = this.defaultBlockState().setValue(FACING, facing).setValue(POWER, 0);

        boolean shouldPower = this.shouldTurnOn(level, pos, state);
        return state.setValue(POWERED, shouldPower);
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return super.getSignal(pBlockState, pBlockAccess, pPos, pSide);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, POWER);
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
