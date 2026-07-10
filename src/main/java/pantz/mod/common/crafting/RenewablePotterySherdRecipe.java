package pantz.mod.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.registry.PMRecipes.*;

public class RenewablePotterySherdRecipe extends CustomRecipe {
    public RenewablePotterySherdRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int firstX = -1, lastX = -1, firstY = -1, lastY = -1;
        int count = 0;

        for (int y = 0; y < inv.getHeight(); ++y) {
            for (int x = 0; x < inv.getWidth(); ++x) {
                ItemStack stack = inv.getItem(x + y * inv.getWidth());
                if (!stack.isEmpty()) {
                    if (firstX == -1) firstX = x;
                    if (firstY == -1) firstY = y;
                    lastX = x;
                    lastY = y;
                    count++;
                }
            }
        }

        if (count != 9 || (lastX - firstX != 2) || (lastY - firstY != 2)) {
            return false;
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack stack = inv.getItem((firstX + x) + (firstY + y) * inv.getWidth());
                if (x == 1 && y == 1) {
                    if (!isPotterySherd(stack)) return false;
                } else {
                    if (!stack.is(Items.BRICK)) return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        ItemStack input = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && isPotterySherd(stack)) {
                input = stack;
                break;
            }
        }

        if (!input.isEmpty()) {
            return input.copyWithCount(2);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return ItemStack.EMPTY;
    }


    private boolean isPotterySherd(ItemStack stack) {
        return stack.is(ItemTags.DECORATED_POT_SHERDS);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PMRecipeSerializers.POTTERY_SHERD_DUPLICATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<RenewablePotterySherdRecipe> {
        @Override
        public RenewablePotterySherdRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            String categoryString = GsonHelper.getAsString(pSerializedRecipe, "category", "misc");
            CraftingBookCategory category = CraftingBookCategory.CODEC.byName(categoryString, CraftingBookCategory.MISC);
            return new RenewablePotterySherdRecipe(pRecipeId, category);
        }

        @Override
        public @Nullable RenewablePotterySherdRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            CraftingBookCategory category = pBuffer.readEnum(CraftingBookCategory.class);
            return new RenewablePotterySherdRecipe(pRecipeId, category);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RenewablePotterySherdRecipe pRecipe) {
            pBuffer.writeEnum(pRecipe.category());
        }
    }
}
