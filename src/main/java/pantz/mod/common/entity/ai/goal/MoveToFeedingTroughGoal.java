package pantz.mod.common.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import pantz.mod.common.block.entity.FeedingTroughBlockEntity;

public class MoveToFeedingTroughGoal extends MoveToBlockGoal {
    private final Animal animal;
    public static final String COOLDOWN_TAG = "FeedingTroughCooldown";
    public MoveToFeedingTroughGoal(Animal animal, double pSpeedModifier, int pSearchRange) {
        super(animal, pSpeedModifier, pSearchRange, 4);
        this.animal = animal;
    }
    @Override
    public boolean canUse() {
        if (this.animal.isInLove() || (!this.animal.isBaby() && this.animal.getAge() > 0)) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.animal.isInLove() || (!this.animal.isBaby() && this.animal.getAge() > 0)) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.blockPos != BlockPos.ZERO) {
            this.animal.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, this.blockPos.getY() + 0.5D, this.blockPos.getZ() + 0.5D, 10.0F, (float)this.animal.getMaxHeadXRot());
        }
        if (this.isReachedTarget()) {
            if (this.animal.level().getBlockEntity(this.blockPos) instanceof FeedingTroughBlockEntity trough) {
                boolean isAdult = !this.animal.isBaby();
                if (!isAdult || !this.animal.isInLove()) {
                    trough.tryFeedingAnimals(animal, isAdult);
                    this.animal.getPersistentData().putInt(COOLDOWN_TAG, 600);
                    this.nextStartTick = this.nextStartTick(this.mob);
                    this.stop();
                }
            }
        }
    }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            if (this.animal.getPersistentData().getInt(COOLDOWN_TAG) > 0) {
                return false;
            }

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FeedingTroughBlockEntity) {
                IItemHandler handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty() && this.animal.isFood(stack)) {
                        return true;
                    }
                }
            }
            return false;
        }

    @Override
    public double acceptedDistance() {
        return 1.5d;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(40 + mob.getRandom().nextInt(40));
    }
}
