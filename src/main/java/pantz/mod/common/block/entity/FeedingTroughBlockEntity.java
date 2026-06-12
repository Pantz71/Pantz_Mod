package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.common.block.FeedingTroughBlock;
import pantz.mod.common.inventory.FeedingTroughMenu;
import pantz.mod.core.registry.PMBlockEntityTypes;

public class FeedingTroughBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(24, ItemStack.EMPTY);

    public FeedingTroughBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.FEEDING_TROUGH.get(), pPos, pBlockState);
    }

    private void updateLayers() {
        if (this.level == null || level.isClientSide()) return;
        int layers = getLayers();

        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.hasProperty(FeedingTroughBlock.LAYERS) && state.getValue(FeedingTroughBlock.LAYERS) != layers) {
            this.level.setBlockAndUpdate(this.worldPosition, state.setValue(FeedingTroughBlock.LAYERS, layers));
        }

    }

    private int getLayers() {
        if (this.getContainerSize() == 0) return 0;

        int occupiedSlots = 0;
        float fillPercentage = 0.0f;

        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                int maxStack = Math.min(this.getMaxStackSize(), itemstack.getMaxStackSize());
                fillPercentage += (float) itemstack.getCount() / (float) maxStack;
                occupiedSlots++;
            }
        }

        fillPercentage /= (float) this.getContainerSize();
        if (occupiedSlots > 0) {
            return Mth.clamp(Mth.ceil(fillPercentage * 4.0F), 1, 4);
        }

        return 0;
    }

    public void tryFeedingAnimals(Animal animal, boolean isAdult) {
        this.unpackLootTable(null);
        boolean itemConsumed = false;
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty() && animal.isFood(stack)) {
                stack.shrink(1);
                this.setItem(i, stack);
                itemConsumed = true;
                if (this.level != null) {
                    this.level.broadcastEntityEvent(animal, (byte)18);
                    animal.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                }
                if (isAdult) {
                    animal.setInLove(null);
                } else {
                    animal.ageUp((int) ((float)(-animal.getAge() / 20) * 0.1f), true);
                }
                break;
            }
        }
        if (itemConsumed) {
            this.updateLayers();
        }
    }

    public void drops() {
        this.unpackLootTable(null);
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, this);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = super.removeItem(slot, amount);
        this.updateLayers();
        return result;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
        setChanged();
        updateLayers();
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.unpackLootTable(null);
        this.getItems().set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
        this.updateLayers();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.pantz_mod.feeding_trough");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new FeedingTroughMenu(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 24;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide()) {
            this.unpackLootTable(null);
        }
        updateLayers();
    }
}
