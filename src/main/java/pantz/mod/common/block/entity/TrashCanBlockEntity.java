package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.TrashCanBlock;
import pantz.mod.core.registry.PMBlockEntityTypes;
import pantz.mod.core.registry.PMSoundEvents;

public class TrashCanBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
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
                level.playSound(null, pos, PMSoundEvents.TRASH_CAN_OPEN.get(), SoundSource.BLOCKS);
            }
            if (oldCount > 0 && newCount == 0) {
                level.playSound(null, pos, PMSoundEvents.TRASH_CAN_CLOSE.get(), SoundSource.BLOCKS);
            }
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu) {
                return menu.getContainer() == TrashCanBlockEntity.this;
            }
            return false;
        }
    };

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
        items.replaceAll(ignored -> ItemStack.EMPTY);
        setChanged();
        if (level != null) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(items.size());
        for(int i = 0; i < items.size(); i++) {
            inv.setItem(i, items.get(i));
        }
        if (this.level == null) return;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public void clearContent() {
        clearContentInside();
    }

    @Override
    public boolean stillValid(Player player) {
        return getBlockState().getBlock() instanceof TrashCanBlock;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.pantz_mod.trash_can");
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.threeRows(id, inventory, this);
    }

    @Override
    public void startOpen(Player player) {
        startOpenContainer(player);
    }

    @Override
    public void stopOpen(Player player) {
        stopOpenContainer(player);
    }
}
