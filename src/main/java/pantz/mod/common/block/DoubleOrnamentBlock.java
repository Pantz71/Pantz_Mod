package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import pantz.mod.core.registry.PMSoundEvents;

public class DoubleOrnamentBlock extends DoublePlantBlock implements SimpleWaterloggedBlock {
    private static final VoxelShape UPPER_SHAPE = Block.box(5, 0, 5, 11, 16, 11);
    private static final VoxelShape LOWER_SHAPE = Block.box(5, 6, 5, 11, 16, 11);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DoubleOrnamentBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        if (pos.getY() <= level.getMinBuildHeight()) return null;

        BlockPos below = pos.below();
        BlockPos above = pos.above();

        if (!level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN)) {
            return null;
        }

        if (!level.getBlockState(below).canBeReplaced(ctx)) {
            return null;
        }

        return this.defaultBlockState()
                .setValue(HALF, DoubleBlockHalf.UPPER)
                .setValue(WATERLOGGED, level.getFluidState(pos).is(FluidTags.WATER))
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockPos below = pos.below();
        level.setBlock(below, state.setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, level.getFluidState(below).is(FluidTags.WATER)), 3);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return Block.canSupportCenter(level, pos.above(), Direction.DOWN);
        } else {
            BlockState above = level.getBlockState(pos.above());
            return above.is(this) && above.getValue(HALF) == DoubleBlockHalf.UPPER;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        Direction nextDirection = state.getValue(FACING).getClockWise();
        BlockPos otherPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(this)) {
            level.setBlockAndUpdate(pos, state.setValue(FACING, nextDirection));
            level.setBlockAndUpdate(otherPos, otherState.setValue(FACING, nextDirection));
            level.playSound(null, pos, PMSoundEvents.ORNAMENT_ADJUST.get(), SoundSource.BLOCKS);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HALF, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return UPPER_SHAPE;
        }
        return LOWER_SHAPE;
    }
}
