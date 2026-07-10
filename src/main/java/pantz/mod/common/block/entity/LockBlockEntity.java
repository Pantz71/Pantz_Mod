package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.LockCode;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.utils.Lockable;
import pantz.mod.core.registry.PMBlockEntityTypes;

import java.util.UUID;

public class LockBlockEntity extends BlockEntity implements Lockable {
    private Item keyItem;
    private LockCode lockCode = LockCode.NO_LOCK;
    private UUID owner;

    public LockBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.LOCK.get(), pPos, pBlockState);
    }

    @Override
    public ItemStack getItem() {
        if (keyItem == null) return ItemStack.EMPTY;
        return new ItemStack(keyItem);
    }

    @Override
    public LockCode getLockCode() {
        return lockCode;
    }

    @Override
    public void setLockCode(LockCode code) {
        this.lockCode = code == null ? LockCode.NO_LOCK : code;
        setChanged();
    }

    public Item getKeyItem() {
        return keyItem;
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

        setOwner(owner);
        setChanged();
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    @Override
    public void removeItem(UUID owner) {
        if (this.owner != null && this.owner.equals(owner)) {
            this.keyItem = null;
            this.lockCode = LockCode.NO_LOCK;
            this.owner = null;
            setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (keyItem != null) {
            tag.putString("KeyItem", BuiltInRegistries.ITEM.getKey(keyItem).toString());
        }
        lockCode.addToTag(tag);
        if (owner != null) {
            tag.putUUID("Owner", owner);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("KeyItem")) {
            keyItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(tag.getString("KeyItem")));
        }
        lockCode = LockCode.fromTag(tag);
        if (tag.contains("Owner")) {
            owner = tag.getUUID("Owner");
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
