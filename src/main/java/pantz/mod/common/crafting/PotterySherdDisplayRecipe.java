package pantz.mod.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

public class PotterySherdDisplayRecipe extends RenewablePotterySherdRecipe {
    private final ItemStack potterySherd;
    public PotterySherdDisplayRecipe(ResourceLocation id, ItemStack potterySherd) {
        super(id, CraftingBookCategory.MISC);
        this.potterySherd = potterySherd;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        Ingredient brick = Ingredient.of(Items.BRICK);
        Ingredient sherd = Ingredient.of(this.potterySherd);

        for (int i = 0; i < 9; i++) {
            ingredients.set(i, (i == 4) ? sherd : brick);
        }
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.potterySherd.copyWithCount(2);
    }

    @Override
    public boolean isSpecial() {
        return false;
    }
}
