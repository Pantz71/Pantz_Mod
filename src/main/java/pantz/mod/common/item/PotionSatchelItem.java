package pantz.mod.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import pantz.mod.common.inventory.PotionSatchelMenu;

public class PotionSatchelItem extends Item implements DyeableLeatherItem {
    public PotionSatchelItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            SimpleContainer container = new SimpleContainer(23) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    saveInventory(stack, this);
                }
            };
            loadInventory(stack, container);
            serverPlayer.openMenu(new SimpleMenuProvider((id, inv, p) -> new PotionSatchelMenu(id, inv, container), Component.translatable("item.pantz_mod.potion_satchel")));

        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    protected static void loadInventory(ItemStack satchel, SimpleContainer container) {
        CompoundTag tag = satchel.getTag();
        if (tag != null && tag.contains("Inventory", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Inventory", Tag.TAG_COMPOUND);
            container.clearContent(); // Clear old items before loading

            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                int slot = itemTag.getInt("Slot");
                if (slot >= 0 && slot < container.getContainerSize()) {
                    container.setItem(slot, ItemStack.of(itemTag));
                }
            }
        }
    }

    protected static void saveInventory(ItemStack satchel, SimpleContainer container) {
        CompoundTag tag = satchel.getOrCreateTag();
        ListTag list = new ListTag();

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", slot);
                stack.save(itemTag);
                list.add(itemTag);
            }
        }
        tag.put("Inventory", list);
    }

    public static void tryQuickDrink(Player player, ItemStack satchel) {
        SimpleContainer container = new SimpleContainer(23);
        Level level = player.level();

        loadInventory(satchel, container);

        for (int slot = 15; slot < 23; slot++) {
            ItemStack potion = container.getItem(slot);

            if (!potion.isEmpty() && potion.getItem() instanceof PotionItem) {
                ItemStack copy = potion.copyWithCount(1);
                ItemStack resultStack = copy.finishUsingItem(level, player);

                if (player.isCreative()) {
                    resultStack = new ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE);
                }

                potion.shrink(1);

                if (potion.isEmpty()) {
                    container.setItem(slot, resultStack);
                } else {
                    container.setItem(slot, potion);

                    if (!resultStack.isEmpty()) {
                        if (!player.getInventory().add(resultStack)) {
                            player.drop(resultStack, false);
                        }
                    }
                }

                saveInventory(satchel, container);

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        copy.getItem().getDrinkingSound(), SoundSource.PLAYERS, 1.0f, 1.0f);
                break;
            }
        }
    }

}
