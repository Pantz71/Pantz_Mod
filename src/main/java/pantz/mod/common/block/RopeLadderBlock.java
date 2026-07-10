package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.utils.PMBlockStateProperties;

public class RopeLadderBlock extends LadderBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TOP = PMBlockStateProperties.TOP;
    public static final BooleanProperty BOTTOM = PMBlockStateProperties.BOTTOM;

    public RopeLadderBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(TOP, false)
                .setValue(BOTTOM, false));
    }

    private boolean isTop(BlockGetter level, BlockPos pos) {
        return !level.getBlockState(pos.above()).is(this);
    }

    private boolean isBottom(BlockGetter level, BlockPos pos) {
        return !level.getBlockState(pos.below()).is(this) && !this.isTop(level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TOP, BOTTOM);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState newState = state.setValue(TOP, isTop(level, pos)).setValue(BOTTOM, isBottom(level, pos));

        if (state != newState) {
            level.setBlockAndUpdate(pos, newState);
        }

        if (!newState.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return state.setValue(TOP, isTop(level, currentPos)).setValue(BOTTOM, isBottom(level, currentPos));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.replacingClickedOnBlock()) {
            BlockState state = context.getLevel()
                    .getBlockState(context.getClickedPos()
                            .relative(context.getClickedFace().getOpposite()));

            if (state.is(this) && state.getValue(FACING) == context.getClickedFace()) {
                return null;
            }
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluid = level.getFluidState(pos);

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                Direction facing = direction.getOpposite();

                boolean top = isTop(level, pos);
                boolean bottom = isBottom(level, pos);

                BlockState state = this.defaultBlockState()
                        .setValue(FACING, facing)
                        .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                        .setValue(TOP, top)
                        .setValue(BOTTOM, bottom);

                if (state.canSurvive(level, pos)) {
                    return state;
                }
            }
        }

        return null;
    }

    private boolean canAttachTo(BlockGetter pBlockReader, BlockPos pPos, Direction pDirection) {
        BlockState blockstate = pBlockReader.getBlockState(pPos);
        return blockstate.isFaceSturdy(pBlockReader, pPos, pDirection);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return this.canAttachTo(level, pos.relative(facing.getOpposite()), facing) || !isTop(level, pos);
    }

    private InteractionResult retractLadder(BlockPos pos, Level level, Player player) {
        BlockPos currentPos = pos;

        while (level.getBlockState(currentPos.below()).is(this)) {
            currentPos = currentPos.below();
        }

        level.levelEvent(2001, currentPos, Block.getId(this.defaultBlockState()));
        level.removeBlock(currentPos, false);
        ItemStack ladderItem = new ItemStack(this.asItem());

        if (!player.getInventory().add(ladderItem)) {
            player.drop(ladderItem, false);
        }

        BlockPos abovePos = currentPos.above();
        if (level.getBlockState(abovePos).is(this)) {
            boolean waterlogged = level.getBlockState(abovePos).getValue(WATERLOGGED);
            BlockState aboveState = level.getBlockState(abovePos)
                    .setValue(BOTTOM, true)
                    .setValue(TOP, isTop(level, abovePos))
                    .setValue(WATERLOGGED, waterlogged);
            level.setBlockAndUpdate(abovePos, aboveState);
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult extendLadder(BlockPos pos, Level level, Player player, BlockState state, ItemStack stack) {
        BlockPos currentPos = pos;

        while (level.getBlockState(currentPos.below()).is(this)) {
            currentPos = currentPos.below();
        }

        BlockPos belowPos = currentPos.below();

        if (level.getBlockState(belowPos).isAir()) {
            boolean waterlogged = level.getBlockState(currentPos).getValue(WATERLOGGED);

            BlockState belowState = this.defaultBlockState()
                    .setValue(FACING, state.getValue(FACING))
                    .setValue(BOTTOM, true)
                    .setValue(TOP, false)
                    .setValue(WATERLOGGED, level.getFluidState(belowPos).isSource());

            level.setBlockAndUpdate(belowPos, belowState);
            level.levelEvent(2001, currentPos, Block.getId(this.defaultBlockState()));

            BlockState aboveState = level.getBlockState(currentPos)
                    .setValue(BOTTOM, false)
                    .setValue(TOP, isTop(level, currentPos))
                    .setValue(WATERLOGGED, waterlogged);
            level.setBlockAndUpdate(currentPos, aboveState);

            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty() && player.isShiftKeyDown()) {
            if (isTop(level, pos)) {
                if (!level.isClientSide()) {
                    return retractLadder(pos, level, player);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        } else if (stack.is(this.asItem()) && !player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                return extendLadder(pos, level, player, state, stack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}
