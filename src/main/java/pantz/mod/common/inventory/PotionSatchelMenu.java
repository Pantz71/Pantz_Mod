package pantz.mod.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import pantz.mod.core.registry.PMMenuTypes;

public class PotionSatchelMenu extends AbstractContainerMenu {
    private final Container container;

    public PotionSatchelMenu(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(23));
    }

    public PotionSatchelMenu(int id, Inventory inv, Container container) {
        super(PMMenuTypes.POTION_SATCHEL.get(), id);
        this.container = container;
        checkContainerSize(container, 23);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 5; ++col) {
                this.addSlot(new PotionSlot(container, col + row * 5, 44 + col * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 8; ++row) {
            this.addSlot(new DrinkablePotionSlot(container, 15 + row, -24, 8 + row * 18));
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

            if (index < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (itemstack1.getItem() instanceof PotionItem) {
                    if (itemstack1.getUseAnimation() == UseAnim.DRINK) {
                        if (this.moveItemStackTo(itemstack1, 15, 23, false)) {
                            if (itemstack1.isEmpty()) {
                                slot.setByPlayer(ItemStack.EMPTY);
                            } else {
                                slot.setChanged();
                            }
                            return itemstack;
                        }
                    }

                    if (!this.moveItemStackTo(itemstack1, 0, 15, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    private static class PotionSlot extends Slot {
        public PotionSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof PotionItem;
        }
    }
    
    private static class DrinkablePotionSlot extends Slot {
        public DrinkablePotionSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(Items.POTION);
        }
    }
}
