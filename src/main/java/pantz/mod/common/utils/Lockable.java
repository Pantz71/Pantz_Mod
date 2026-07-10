package pantz.mod.common.utils;

import net.minecraft.world.LockCode;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Lockable {
    ItemStack getItem();

    void setItem(ItemStack Item, UUID owner);

    void removeItem(UUID owner);

    UUID getOwner();

    void setOwner(@Nullable UUID owner);

    LockCode getLockCode();

    void setLockCode(LockCode code);
}
