package pantz.mod.core.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import pantz.mod.common.block.RopeLadderBlock;

public class PMDispenserBehaviors {
    public static DispenseItemBehavior EXTEND_ROPE_LADDER = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            Level level = source.level();
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos frontPos = source.pos().relative(direction);
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

    public static OptionalDispenseItemBehavior EQUIP_HORSE_ARMOR = new OptionalDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
            for (AbstractHorse abstractHorse : source.level().getEntitiesOfClass(AbstractHorse.class, new AABB(pos), horse -> horse.isAlive() && horse.canUseSlot(EquipmentSlot.BODY))) {
                if (abstractHorse.isBodyArmorItem(stack) && !abstractHorse.isWearingBodyArmor() && abstractHorse.isTamed()) {
                    abstractHorse.getSlot(401).set(stack.split(1));
                    this.setSuccess(true);
                    return stack;
                }
            }

            return super.execute(source, stack);
        }
    };
}
