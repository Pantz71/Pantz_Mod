package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import pantz.mod.common.block.entity.LockableBlockEntity;
import pantz.mod.core.other.tags.PMItemTags;

import java.util.UUID;

public class LockBlock extends BaseEntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public LockBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if (!(level.getBlockEntity(pos) instanceof LockableBlockEntity lock)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);
        ItemStack keyItem = lock.getItem();

        if (!keyItem.isEmpty() && isSameItem(keyItem, stack)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.cycle(POWERED), 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    public boolean isValidItem(ItemStack stack) {
        return stack.is(PMItemTags.KEYS);
    }

    protected boolean isSameItem(ItemStack stored, ItemStack held) {
        if (!stored.is(held.getItem())) return false;

        CompoundTag storedTag = stored.getTag();
        CompoundTag heldTag = held.getTag();

        if (storedTag == null && heldTag == null) return true;
        if (storedTag == null || heldTag == null) return false;

        CompoundTag cleanStored = storedTag.copy();
        CompoundTag cleanHeld = heldTag.copy();

        cleanStored.remove("Damage");
        cleanStored.remove("Unbreakable");
        cleanStored.remove("RepairCost");
        cleanStored.remove("HideFlags");

        cleanHeld.remove("Damage");
        cleanHeld.remove("Unbreakable");
        cleanHeld.remove("RepairCost");
        cleanHeld.remove("HideFlags");

        return cleanStored.equals(cleanHeld);
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
        return new LockableBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
