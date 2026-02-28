package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.core.registry.PMBlockEntityTypes;

import java.util.UUID;

public class LockableBlockEntity extends BlockEntity {
    private ItemStack item = ItemStack.EMPTY;
    private UUID owner;

    public LockableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.LOCKABLE.get(), pPos, pBlockState);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item, UUID owner) {
        this.item = item.copy();
        this.owner = owner;
        setChanged();
    }

    public void removeItem(UUID owner) {
        if (this.owner != null && this.owner.equals(owner)) {
            this.item = ItemStack.EMPTY;
            this.owner = null;
            setChanged();
        }
    }

    public UUID getOwner() {
        return this.owner;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.item.isEmpty()) {
            tag.put("KeyItem", this.item.serializeNBT());
        }
        if (this.owner != null) {
            tag.putUUID("Owner", this.owner);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("KeyItem")) {
            this.item = ItemStack.of(tag.getCompound("KeyItem"));
        }
        if (tag.contains("Owner")) {
            this.owner = tag.getUUID("Owner");
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
}
