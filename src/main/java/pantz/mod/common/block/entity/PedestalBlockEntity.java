package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import pantz.mod.core.registry.PMBlockEntityTypes;

public class PedestalBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null) {
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
            }
        }
    };

    private boolean spinning = false;

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(PMBlockEntityTypes.PEDESTAL.get(), pos, state);
    }

    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        itemHandler.setStackInSlot(0, stack);
        setChanged();
        if (this.level != null) {
            BlockState state = this.getBlockState();
            this.level.updateNeighborsAt(this.worldPosition, state.getBlock());
            this.level.updateNeighbourForOutputSignal(this.worldPosition, state.getBlock());
            this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    public boolean isSpinning() {
        return spinning;
    }

    public void setSpinning(boolean spin) {
        this.spinning = spin;
        setChanged();
    }

    public int getPower() {
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            return 15;
        }
        return 0;
    }


    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        if (this.level == null) return;
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Item", itemHandler.serializeNBT(provider));
        tag.putBoolean("Spinning", spinning);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        itemHandler.deserializeNBT(provider, tag.getCompound("Item"));
        spinning = tag.getBoolean("Spinning");
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

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        loadAdditional(pkt.getTag(), lookupProvider);
    }
}