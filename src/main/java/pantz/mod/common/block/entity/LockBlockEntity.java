package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.LockCode;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
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

        if (stack.hasCustomHoverName()) {
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
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (keyItem != null) {
            tag.putString("KeyItem", ForgeRegistries.ITEMS.getKey(keyItem).toString());
        }
        lockCode.addToTag(tag);
        if (owner != null) {
            tag.putUUID("Owner", owner);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("KeyItem")) {
            keyItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("KeyItem")));
        }
        lockCode = LockCode.fromTag(tag);
        if (tag.contains("Owner")) {
            owner = tag.getUUID("Owner");
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
