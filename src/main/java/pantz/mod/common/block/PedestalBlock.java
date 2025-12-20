package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
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
import pantz.mod.common.utils.CarpetColor;
import pantz.mod.common.utils.PedestalUtils;

@SuppressWarnings("deprecation")
public class PedestalBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<CarpetColor> CARPET = EnumProperty.create("carpet", CarpetColor.class);
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(3, 2, 3, 13, 13, 13),
            Block.box(1, 13, 1, 15, 16, 15)
    );
    public PedestalBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CARPET, CarpetColor.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CARPET);
    }

    private InteractionResult decorateWithCarpet(BlockState state, Level level, BlockPos pos, Player player) {
        ItemStack offhand = player.getOffhandItem();
        ItemStack mainhand = player.getMainHandItem();
        if (offhand.is(ItemTags.WOOL_CARPETS) && state.getValue(CARPET) == CarpetColor.NONE) {
            CarpetColor selectedColor = PedestalUtils.getCarpetColor(offhand.getItem());
            if (selectedColor != CarpetColor.NONE) {
                if (!level.isClientSide()) {
                    if (state.getValue(CARPET) != CarpetColor.NONE) {
                        return InteractionResult.CONSUME;
                    }
                    level.setBlock(pos, state.setValue(CARPET, selectedColor), 3);
                    if (!player.isCreative()) {
                        offhand.shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }

        } else if (mainhand.isEmpty() && state.getValue(CARPET) != CarpetColor.NONE && !player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                CarpetColor carpetColor = state.getValue(CARPET);
                ItemStack carpetItem = new ItemStack(PedestalUtils.getCarpetForColor(carpetColor));
                if (!player.addItem(carpetItem)) {
                    player.drop(carpetItem, false);
                }

                level.setBlock(pos, state.setValue(CARPET, CarpetColor.NONE), 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    }

    private InteractionResult putItemOn(Player player, InteractionHand hand, ItemStack stack, PedestalBlockEntity pedestal, Level level, BlockPos pos, BlockState state) {
        ItemStack onPedestal = pedestal.getItem();
        boolean isSneaking = player.isShiftKeyDown();
        if (!isSneaking) {
            if (stack.isEmpty() && !onPedestal.isEmpty()) {
                player.setItemInHand(hand, onPedestal);
                pedestal.setItem(ItemStack.EMPTY);
                level.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.SUCCESS;
            }

            if (!stack.isEmpty() && !onPedestal.isEmpty()) {
                if (stack.getCount() > 1) {
                    if (player.getInventory().add(onPedestal)) {
                        ItemStack copy = stack.copy();
                        copy.setCount(1);
                        pedestal.setItem(copy);
                        level.sendBlockUpdated(pos, state, state, 3);
                        stack.shrink(1);
                    }
                } else {
                    pedestal.setItem(stack);
                    player.setItemInHand(hand, onPedestal);
                }
                return InteractionResult.SUCCESS;
            }

            if (!stack.isEmpty()) {
                ItemStack copy = stack.copy();
                copy.setCount(1);
                pedestal.setItem(copy);
                level.sendBlockUpdated(pos, state, state, 3);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal)) return InteractionResult.PASS;

        boolean isSneaking = player.isShiftKeyDown();
        ItemStack stack = player.getItemInHand(hand);

        InteractionResult decorateWithCarpet = decorateWithCarpet(state, level, pos, player);
        if (decorateWithCarpet.consumesAction()) {
            return decorateWithCarpet;
        }

        if (isSneaking) {
            pedestal.setSpinning(!pedestal.isSpinning());
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        InteractionResult putItemOn = putItemOn(player, hand, stack, pedestal, level, pos, state);
        if (putItemOn.consumesAction()) {
            return putItemOn;
        }

        return InteractionResult.PASS;
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
            return pedestal.getRedstoneSignal();
        }
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof PedestalBlockEntity pedestal) {
            return pedestal.getRedstoneSignal();
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                pedestal.drops();
                level.updateNeighbourForOutputSignal(pos, this);
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
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
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
