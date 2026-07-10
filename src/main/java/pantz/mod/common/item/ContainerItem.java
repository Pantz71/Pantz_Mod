package pantz.mod.common.item;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.other.PMLootContextParamSets;

public abstract class ContainerItem extends Item {
    public ContainerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            int size = this.getContainerSize(stack);
            NonNullList<ItemStack> itemStorage = NonNullList.withSize(size, ItemStack.EMPTY);

            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
            if (contents != null) {
                contents.copyInto(itemStorage);
            }

            SimpleContainer container = new SimpleContainer(itemStorage.toArray(new ItemStack[0])) {
                @Override
                public boolean canPlaceItem(int index, ItemStack itemStack) {
                    return mayPlace(index, itemStack);
                }

                @Override
                public void setChanged() {
                    super.setChanged();
                    stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
                }
            };

            if (!this.trySetLootTable(stack, container, serverPlayer)) {
                loadInventory(stack, container);
            }

            serverPlayer.openMenu(new SimpleMenuProvider((id, inv, pl) -> createMenu(id, inv, container), stack.getHoverName()));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    protected abstract boolean mayPlace(int index, ItemStack stack);

    protected abstract int getContainerSize(ItemStack stack);

    public abstract AbstractContainerMenu createMenu(int containerId, Inventory inv, SimpleContainer container);

    protected boolean trySetLootTable(ItemStack stack, SimpleContainer container, ServerPlayer serverPlayer) {
        SeededContainerLoot containerLoot = stack.get(DataComponents.CONTAINER_LOOT);
        if (containerLoot != null) {
            ResourceKey<LootTable> lootTableKey = containerLoot.lootTable();
            long seed = containerLoot.seed();

            stack.remove(DataComponents.CONTAINER_LOOT);
            if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                LootTable loot = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);
                LootParams lootParams = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, serverPlayer.position())
                        .withParameter(LootContextParams.THIS_ENTITY, serverPlayer)
                        .create(PMLootContextParamSets.CONTAINER_ITEM);

                SimpleContainer tempContainer = new SimpleContainer(container.getContainerSize());
                loot.fill(tempContainer, lootParams, seed);
                container.clearContent();

                for (int i = 0; i < tempContainer.getContainerSize(); i++) {
                    ItemStack lootStack = tempContainer.getItem(i);
                    if (lootStack.isEmpty()) continue;

                    if (this.mayPlace(i, lootStack)) {
                        container.setItem(i, lootStack);
                    } else {
                        ItemStack remainder = lootStack;
                        for (int targetSlot = 0; targetSlot < container.getContainerSize(); targetSlot++) {
                            if (this.mayPlace(targetSlot, remainder)) {
                                ItemStack existing = container.getItem(targetSlot);
                                if (existing.isEmpty()) {
                                    container.setItem(targetSlot, remainder);
                                    remainder = ItemStack.EMPTY;
                                    break;
                                }
                            }
                        }

                        if (!remainder.isEmpty()) {
                            serverPlayer.drop(remainder, false);
                        }
                    }
                }

                saveInventory(stack, container);
                return true;
            }
        }
        return false;
    }

    protected static void loadInventory(ItemStack stack, SimpleContainer container) {
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        container.clearContent();
        for (int i = 0; i < Math.min(contents.getSlots(), container.getContainerSize()); i++) {
            ItemStack item = contents.getStackInSlot(i);
            if (!item.isEmpty()) {
                container.setItem(i, item);
            }
        }
    }

    protected static void saveInventory(ItemStack stack, SimpleContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            list.set(slot, container.getItem(slot));
        }
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(list));
    }
}
