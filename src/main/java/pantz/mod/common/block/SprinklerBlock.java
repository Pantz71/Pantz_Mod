package pantz.mod.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.entity.SprinklerBlockEntity;
import pantz.mod.common.utils.PMBlockStateProperties;
import pantz.mod.core.registry.PMBlockEntityTypes;

public class SprinklerBlock extends BaseEntityBlock {
    private static final MapCodec<SprinklerBlock> CODEC = simpleCodec(SprinklerBlock::new);
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    public static final BooleanProperty FILLED = PMBlockStateProperties.FILLED;

    public SprinklerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FILLED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) return ItemInteractionResult.FAIL;
        if (stack.is(Items.WATER_BUCKET) || !state.getValue(FILLED)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, state.setValue(FILLED, true));
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        } else if (stack.is(Items.BUCKET) || state.getValue(FILLED)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(pos, state.setValue(FILLED, false));
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, PMBlockEntityTypes.SPRINKLER.get(), SprinklerBlockEntity::tick);
    }
}
