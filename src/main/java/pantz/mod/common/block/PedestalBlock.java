package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.entity.PedestalBlockEntity;
import pantz.mod.common.utils.PMBlockStateProperties;
import pantz.mod.common.utils.PMBlockStateProperties.CarpetColor;
import pantz.mod.common.utils.PedestalUtils;

@SuppressWarnings("deprecation")
public class PedestalBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty SPINNING = PMBlockStateProperties.SPINNING;
    public static final EnumProperty<CarpetColor> CARPET = PMBlockStateProperties.CARPET;
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(3, 2, 3, 13, 13, 13),
            Block.box(1, 13, 1, 15, 16, 15)
    );
    private static final VoxelShape TOP_SUPPORT = Block.box(0, 15, 0, 16, 16, 16);
    private static final VoxelShape BOTTOM_SUPPORT = Block.box(0, 0, 0, 16, 1, 16);
    private static final VoxelShape SUPPORT = Shapes.or(TOP_SUPPORT, BOTTOM_SUPPORT);

    public PedestalBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CARPET, CarpetColor.NONE).setValue(SPINNING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CARPET, SPINNING);
    }

    private InteractionResult putItemOn(Player player, InteractionHand hand, ItemStack stack, PedestalBlockEntity pedestal, Level level, BlockPos pos, BlockState state) {
        ItemStack onPedestal = pedestal.getItem();

        if (stack.isEmpty() && !onPedestal.isEmpty()) {
            player.setItemInHand(hand, onPedestal.copy());
            pedestal.setItem(ItemStack.EMPTY);
        }
        else if (!stack.isEmpty()) {
            ItemStack copy = stack.copy();
            copy.setCount(1);

            if (!onPedestal.isEmpty()) {
                if (!player.getInventory().add(onPedestal.copy())) {
                    player.drop(onPedestal.copy(), false);
                }
                stack.shrink(1);
            }

            pedestal.setItem(copy);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        } else {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal)) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            pedestal.setSpinning(!pedestal.isSpinning());
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        return putItemOn(player, hand, stack, pedestal, level, pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER).setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(CARPET, CarpetColor.NONE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PedestalBlockEntity pedestal) {
            return pedestal.getPower();
        }
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                pedestal.drops();
            }
            CarpetColor carpetColor = state.getValue(PedestalBlock.CARPET);
            if (carpetColor != CarpetColor.NONE) {
                ItemStack carpetItem = new ItemStack(PedestalUtils.getCarpetForColor(carpetColor));
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), carpetItem);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return SUPPORT;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PedestalBlockEntity(blockPos, blockState);
    }
}
