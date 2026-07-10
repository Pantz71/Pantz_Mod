package pantz.mod.common.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import pantz.mod.common.inventory.PotionSatchelMenu;
import pantz.mod.core.other.PMLootContextParamSets;

public abstract class ContainerItem extends Item {
    public static final String LOOT_TABLE_TAG = "LootTable";
    public static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    public ContainerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            int containerSize = this.getContainerSize(stack);

            SimpleContainer container = new SimpleContainer(containerSize) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    saveInventory(stack, this);
                }

                @Override
                public boolean canPlaceItem(int index, ItemStack itemStack) {
                    return mayPlace(index, itemStack);
                }

                @Override
                public void startOpen(Player player) {
                    super.startOpen(player);
                    SoundEvent openSound = openSound();
                    if (openSound != null) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), openSound, SoundSource.PLAYERS, 0.5f, 1.0f);
                    }
                }

                @Override
                public void stopOpen(Player player) {
                    super.stopOpen(player);
                    SoundEvent closeSound = closeSound();
                    if (closeSound != null) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), closeSound, SoundSource.PLAYERS, 0.5f, 1.0f);
                    }
                }
            };

            if (!this.trySetLootTable(stack, container, serverPlayer)) {
                loadInventory(stack, container);
            }

            serverPlayer.openMenu(new SimpleMenuProvider((id, inv, p) -> this.createMenu(id, inv, container), stack.getHoverName()));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    protected abstract boolean mayPlace(int index, ItemStack stack);

    protected abstract int getContainerSize(ItemStack stack);

    protected abstract AbstractContainerMenu createMenu(int id, Inventory inv, SimpleContainer container);

    protected abstract SoundEvent openSound();

    protected abstract SoundEvent closeSound();

    protected boolean trySetLootTable(ItemStack stack, SimpleContainer container, ServerPlayer serverPlayer) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LOOT_TABLE_TAG, Tag.TAG_STRING)) {
            String lootTable = tag.getString(LOOT_TABLE_TAG);
            long seed = tag.contains(LOOT_TABLE_SEED_TAG, Tag.TAG_LONG) ? tag.getLong(LOOT_TABLE_SEED_TAG) : 0L;

            tag.remove(LOOT_TABLE_TAG);
            tag.remove(LOOT_TABLE_SEED_TAG);

            if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                ResourceLocation lootTableLoc = ResourceLocation.tryParse(lootTable);

                if (lootTableLoc != null) {
                    LootTable loot = serverLevel.getServer().getLootData().getLootTable(lootTableLoc);
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
        }
        return false;
    }

    protected static void loadInventory(ItemStack stack, SimpleContainer container) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Inventory", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Inventory", Tag.TAG_COMPOUND);
            container.clearContent();

            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                int slot = itemTag.getInt("Slot");
                if (slot >= 0 && slot < container.getContainerSize()) {
                    container.setItem(slot, ItemStack.of(itemTag));
                }
            }
        }
    }

    protected static void saveInventory(ItemStack stack, SimpleContainer container) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = new ListTag();

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack item = container.getItem(slot);
            if (!item.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", slot);
                item.save(itemTag);
                list.add(itemTag);
            }
        }
        tag.put("Inventory", list);
    }
}
