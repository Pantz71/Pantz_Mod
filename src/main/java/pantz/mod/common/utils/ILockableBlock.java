package pantz.mod.common.utils;

import net.minecraft.world.item.ItemStack;

public interface ILockableBlock {
    boolean isValidItem(ItemStack stack);
}
