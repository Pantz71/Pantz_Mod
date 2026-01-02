package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.entity.ItemStandBlockEntity;

public class ItemStandBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty GLASS = BooleanProperty.create("glass");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(7, 2, 7, 9, 3, 9)
    );

    public ItemStandBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(GLASS, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLASS, FACING, WATERLOGGED);
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return state.getValue(GLASS) ? SoundType.GLASS : SoundType.STONE;
    }


    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return state.getValue(GLASS) ? Shapes.block() : SHAPE;
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ItemStandBlockEntity itemStand) {
                itemStand.drops();
            }
            if (state.getValue(GLASS)) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Blocks.GLASS.asItem().getDefaultInstance());
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ItemStandBlockEntity stand)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        ItemStack stack = stand.getItem();


        if (player.isShiftKeyDown()) {
            if (!level.isClientSide() && !stand.getItem().isEmpty()) {
                stand.setItem(ItemStack.EMPTY);
                ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.75, pos.getZ() + 0.5, stack);
                level.addFreshEntity(drop);
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (held.getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock().defaultBlockState().is(Blocks.GLASS)) {
            if (!state.getValue(GLASS)) {
                if (!level.isClientSide() && !player.isCreative()) held.shrink(1);
                level.setBlock(pos, state.setValue(GLASS, true), 3);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        if (stack.isEmpty() && !held.isEmpty()) {
            if (!level.isClientSide()) {
                stand.setItem(held.copyWithCount(1));
                if (!player.isCreative()) held.shrink(1);
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemStandBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0f;
    }
}
