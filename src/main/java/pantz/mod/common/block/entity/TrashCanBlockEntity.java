package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.TrashCanBlock;
import pantz.mod.core.registry.PMBlockEntityTypes;
import pantz.mod.core.registry.PMSoundEvents;

public class TrashCanBlockEntity extends BlockEntity implements MenuProvider, Nameable {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final Container container = new SimpleContainer(27) {
        @Override
        public int getContainerSize() {
            return itemStackHandler.getSlots();
        }

        @Override
        public boolean isEmpty() {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
               if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                   return false;
               }
            }
            return true;
        }

        @Override
        public @NotNull ItemStack getItem(int index) {
            return itemStackHandler.getStackInSlot(index);
        }

        @Override
        public void setItem(int index, ItemStack stack) {
            itemStackHandler.setStackInSlot(index, stack);
            setChanged();
        }

        @Override
        public void clearContent() {
            clearContentInside();
        }

        @Override
        public void startOpen(Player pPlayer) {
            startOpenContainer(pPlayer);
        }

        @Override
        public void stopOpen(Player pPlayer) {
            stopOpenContainer(pPlayer);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack stack = itemStackHandler.extractItem(slot, amount, false);
            if (!stack.isEmpty()) {
                setChanged();
            }
            return stack;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack stack = itemStackHandler.getStackInSlot(slot);
            itemStackHandler.setStackInSlot(slot, ItemStack.EMPTY);
            return stack;
        }
    };

    private final ContainerOpenersCounter openers = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            setOpen(true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            setOpen(false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
            if (oldCount == 0 && newCount > 0) {
                level.playSound(null, pos, PMSoundEvents.TRASH_CAN_OPEN.get(),
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            if (oldCount > 0 && newCount == 0) {
                level.playSound(null, pos, PMSoundEvents.TRASH_CAN_CLOSE.get(),
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu) {
                return menu.getContainer() == container;
            }
            return false;
        }
    };

    private final LazyOptional<IItemHandler> items = LazyOptional.of(() -> itemStackHandler);
    private Component customName;

    public TrashCanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.TRASH_CAN.get(), pPos, pBlockState);
    }

    private void setOpen(boolean open) {
        if (this.level != null) {
            BlockState state = getBlockState();
            if (state.getValue(TrashCanBlock.OPEN) != open) {
                level.setBlock(this.worldPosition, state.setValue(TrashCanBlock.OPEN, open), 3);
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

    public void clearContentInside() {
        if (level == null) return;
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStackHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public Container getContainer() {
        return container;
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
        for(int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        if (this.level == null) return;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : Component.translatable("block.pantz_mod.trash_can");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return ChestMenu.threeRows(id, inventory, container);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return items.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        items.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", itemStackHandler.serializeNBT());
        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemStackHandler.deserializeNBT(tag.getCompound("Inventory"));
        if (tag.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }
}
