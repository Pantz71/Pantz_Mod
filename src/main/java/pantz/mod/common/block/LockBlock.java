package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.entity.LockBlockEntity;
import pantz.mod.common.utils.ILockableBlock;
import pantz.mod.core.other.tags.PMItemTags;
import pantz.mod.core.registry.PMSoundEvents;

public class LockBlock extends BaseEntityBlock implements ILockableBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public LockBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if (!(level.getBlockEntity(pos) instanceof LockBlockEntity lock)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (lock.getKeyItem() == null) {
            return InteractionResult.PASS;
        }

        boolean item = held.is(lock.getKeyItem());
        boolean key = item && lock.getLockCode().unlocksWith(held);

        if (!key) {
            if (!level.isClientSide()) {
                player.displayClientMessage(Component.translatable("container.isLocked", state.getBlock().getName()), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        level.playSound(null, pos, PMSoundEvents.KEY_LOCK.get(), SoundSource.BLOCKS);
        if (!level.isClientSide()) {
            level.setBlock(pos, state.cycle(POWERED), 3);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public boolean isValidItem(ItemStack stack) {
        return stack.is(PMItemTags.KEYS);
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LockBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
