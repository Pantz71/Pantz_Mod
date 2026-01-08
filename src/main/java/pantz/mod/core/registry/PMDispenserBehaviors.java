package pantz.mod.core.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.common.block.RopeLadderBlock;

public class PMDispenserBehaviors {
    public static DispenseItemBehavior EXTEND_ROPE_LADDER = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            Level level = source.getLevel();
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos frontPos = source.getPos().relative(direction);
            BlockState frontState = level.getBlockState(frontPos);

            if (!(frontState.getBlock() instanceof RopeLadderBlock ladder)) {
                return defaultDispenseItemBehavior.dispense(source, stack);
            }

            BlockPos currentPos = frontPos;
            while (level.getBlockState(currentPos.below()).is(ladder)) {
                currentPos = currentPos.below();
            }

            BlockPos placePos = currentPos.below();

            if (!level.getBlockState(placePos).canBeReplaced()) {
                return defaultDispenseItemBehavior.dispense(source, stack);
            }

            Direction ladderFacing = frontState.getValue(RopeLadderBlock.FACING);

            BlockState newState = ladder.defaultBlockState()
                    .setValue(RopeLadderBlock.FACING, ladderFacing)
                    .setValue(RopeLadderBlock.TOP, false)
                    .setValue(RopeLadderBlock.BOTTOM, true)
                    .setValue(RopeLadderBlock.WATERLOGGED,
                            level.getFluidState(placePos).isSource());

            if (!newState.canSurvive(level, placePos)) {
                return defaultDispenseItemBehavior.dispense(source, stack);
            }

            level.setBlock(placePos, newState, 3);

            BlockState aboveState = level.getBlockState(currentPos)
                    .setValue(RopeLadderBlock.BOTTOM, false)
                    .setValue(RopeLadderBlock.TOP,
                            !level.getBlockState(currentPos.above()).is(ladder));

            level.setBlock(currentPos, aboveState, 3);

            stack.shrink(1);
            return stack;
        }
    };
}
