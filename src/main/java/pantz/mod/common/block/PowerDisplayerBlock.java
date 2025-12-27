package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class PowerDisplayerBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;    
    public static final IntegerProperty POWER = BlockStateProperties.POWER;       
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_UP  = Block.box(0, 0, 0, 16, 3, 16);
    private static final VoxelShape SHAPE_DOWN    = Block.box(0, 13, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 0, 16, 16, 3);
    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 13, 16, 16, 16);
    private static final VoxelShape SHAPE_EAST  = Block.box(0, 0, 0, 3, 16, 16);
    private static final VoxelShape SHAPE_WEST  = Block.box(13, 0, 0, 16, 16, 16);

    public PowerDisplayerBlock(Properties props) {
        super(props.lightLevel(state -> 2)); 
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWER, 0)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getClickedFace(); 
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        boolean waterlogged = level.getFluidState(pos).getType() == Fluids.WATER;
        int power = getDisplayedPower(level, pos, facing);
        return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection()).setValue(WATERLOGGED, waterlogged)
                .setValue(POWER, power);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case DOWN -> SHAPE_DOWN;
            case UP -> SHAPE_UP;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
        };
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }

        if (!accessor.isClientSide()) {
            Level level = (Level) accessor;
            int newPower = getDisplayedPower(level, pos, state.getValue(FACING));
            if (state.getValue(POWER) != newPower) {
                return state.setValue(POWER, newPower);
            }
        }

        return super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int newPower = getDisplayedPower(level, pos, state.getValue(FACING));
        if (state.getValue(POWER) != newPower) {
            level.setBlock(pos, state.setValue(POWER, newPower), 3);
        }
    }

    private int getDisplayedPower(Level level, BlockPos pos, Direction facing) {
        return getPowerFromBehind(level, pos, facing);
    }

    private int getPowerFromBehind(Level level, BlockPos pos, Direction direction) {
        BlockPos behind = pos.relative(direction, 1);
        BlockState state = level.getBlockState(behind);

        if (!state.isAir()) {
            if (state.is(Blocks.REDSTONE_WIRE)) {
                int dustPower = state.getValue(BlockStateProperties.POWER);
                if (dustPower > 0) {
                    return clampPower(dustPower);
                }
            }
            if (state.hasAnalogOutputSignal()) {
                int power = state.getAnalogOutputSignal(level, behind);
                if (power > 0) {
                    return clampPower(power);
                }
            }
            int signal = level.getSignal(behind, direction);
            if (signal > 0) {
                return clampPower(signal);
            }

            if (state.is(Blocks.REDSTONE_BLOCK)) {
                return 15;
            }
        }
        return 0;
    }

    private static int clampPower(int value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, 15);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(POWER);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide()) {
            int newPower = getDisplayedPower(level, pos, state.getValue(FACING));
            if (state.getValue(POWER) != newPower) {
                level.setBlock(pos, state.setValue(POWER, newPower), 3);
            }
        }
    }
}
