package pantz.mod.common.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import pantz.mod.common.inventory.PotionSatchelMenu;

public class PotionSatchelItem extends ContainerItem {
    public PotionSatchelItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected boolean mayPlace(int index, ItemStack stack) {
        if (index >= 15 && index < 23) {
            return stack.is(Items.POTION);
        }
        return stack.getItem() instanceof PotionItem;
    }

    @Override
    protected int getContainerSize(ItemStack stack) {
        return 23;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, SimpleContainer container) {
        return new PotionSatchelMenu(id, inv, container);
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
                    resultStack = new ItemStack(Items.GLASS_BOTTLE);
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
