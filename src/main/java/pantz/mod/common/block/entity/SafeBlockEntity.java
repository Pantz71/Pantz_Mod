package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.LockCode;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.SafeBlock;
import pantz.mod.common.utils.Lockable;
import pantz.mod.core.registry.PMBlockEntityTypes;
import pantz.mod.core.registry.PMSoundEvents;

import java.util.UUID;

public class SafeBlockEntity extends RandomizableContainerBlockEntity implements Lockable {
    private Item keyItem;
    private UUID owner;
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ContainerOpenersCounter openers = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            setOpen(true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            setOpen(false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState pState, int oldCount, int newCount) {
            if (oldCount == 0 && newCount > 0) {
                level.playSound(null, pos, PMSoundEvents.SAFE_OPEN.get(), SoundSource.BLOCKS);
            }
            if (oldCount > 0 && newCount == 0) {
                level.playSound(null, pos, PMSoundEvents.SAFE_CLOSE.get(), SoundSource.BLOCKS);
            }
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu) {
                return menu.getContainer() == SafeBlockEntity.this;
            }
            return false;
        }
    };

    public SafeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.SAFE.get(), pPos, pBlockState);
    }

    private void setOpen(boolean open) {
        if (this.level != null) {
            BlockState state = getBlockState();
            if (state.getValue(SafeBlock.OPEN) != open) {
                level.setBlock(this.worldPosition, state.setValue(SafeBlock.OPEN, open), 3);
            }
        }
    }

    public void startOpenContainer(Player player) {
        if (!player.isSpectator()) {
            if (this.level != null) {
                openers.incrementOpeners(player, this.level, worldPosition, getBlockState());
            }
        }
    }

    public void stopOpenContainer(Player player) {
        if (!player.isSpectator()) {
            if (this.level != null) {
                openers.decrementOpeners(player, this.level, worldPosition, getBlockState());
            }
        }
    }

    public void drops() {
        this.unpackLootTable(null);
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, this);
        }
    }

    @Override
    public boolean canOpen(Player player) {
        if (player.isSpectator() || this.keyItem == null) return true;
        ItemStack held = player.getMainHandItem();

        if (!held.is(this.keyItem)) {
            player.displayClientMessage(Component.translatable("container.isLocked", getDisplayName()), true);
            if (this.level != null) {
                this.level.playSound(null, this.worldPosition, PMSoundEvents.SAFE_LOCKED.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem() {
        if (keyItem == null) return ItemStack.EMPTY;
        return new ItemStack(keyItem);
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public void setItem(ItemStack stack, UUID owner) {

        this.keyItem = stack.getItem();
        Component component = stack.get(DataComponents.CUSTOM_NAME);

        if (component != null) {
            setLockCode(new LockCode(stack.getHoverName().getString()));
        } else {
            setLockCode(LockCode.NO_LOCK);
        }

        this.owner = owner;
        setChanged();
    }

    @Override
    public void removeItem(UUID owner) {
        if (this.owner != null && this.owner.equals(owner)) {
            keyItem = null;
            setLockCode(LockCode.NO_LOCK);
            this.owner = null;
            setChanged();
        }
    }

    @Override
    public LockCode getLockCode() {
        return LockCode.fromTag(this.saveWithoutMetadata(this.level.registryAccess()));
    }

    @Override
    public void setLockCode(LockCode code) {
        CompoundTag tag = new CompoundTag();
        code.addToTag(tag);
        this.loadAdditional(tag, this.level.registryAccess());
        setChanged();
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }


    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.pantz_mod.safe");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return ChestMenu.threeRows(id, inv, this);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public void startOpen(Player player) {
        startOpenContainer(player);
    }

    @Override
    public void stopOpen(Player player) {
        stopOpenContainer(player);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (keyItem != null) {
            tag.putString("KeyItem", BuiltInRegistries.ITEM.getKey(keyItem).toString());
        }
        if (owner != null) {
            tag.putUUID("Owner", owner);
        }

        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, registries);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("KeyItem")) {
            keyItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(tag.getString("KeyItem")));
        }
        if (tag.contains("Owner")) {
            owner = tag.getUUID("Owner");
        }

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, registries);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        loadAdditional(tag, lookupProvider);
    }
}
