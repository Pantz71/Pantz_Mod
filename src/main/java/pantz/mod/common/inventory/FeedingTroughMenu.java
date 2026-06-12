package pantz.mod.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import pantz.mod.core.registry.PMMenuTypes;

public class FeedingTroughMenu extends AbstractContainerMenu {
    private final Container container;

    public FeedingTroughMenu(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(24));
    }

    public FeedingTroughMenu(int pContainerId, Inventory inv, Container container) {
        super(PMMenuTypes.FEEDING_TROUGH.get(), pContainerId);
        this.container = container;
        checkContainerSize(container, 24);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 8; ++col) {
                this.addSlot(new Slot(container, col + row * 8, 17 + col * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 24) {
                if (!this.moveItemStackTo(itemstack1, 24, 60, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, 24, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
