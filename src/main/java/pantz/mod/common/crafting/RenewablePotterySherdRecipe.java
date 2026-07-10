package pantz.mod.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import pantz.mod.core.registry.PMRecipes.*;

public class RenewablePotterySherdRecipe extends CustomRecipe {
    public RenewablePotterySherdRecipe(CraftingBookCategory category) {
        super(category);
    }


    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        int firstX = -1, lastX = -1, firstY = -1, lastY = -1;
        int count = 0;

        for (int y = 0; y < craftingInput.height(); ++y) {
            for (int x = 0; x < craftingInput.width(); ++x) {
                ItemStack stack = craftingInput.getItem(x + y * craftingInput.width());
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
                ItemStack stack = craftingInput.getItem((firstX + x) + (firstY + y) * craftingInput.width());
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
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack input = ItemStack.EMPTY;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
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

        private final MapCodec<RenewablePotterySherdRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CustomRecipe::category)
                ).apply(instance, RenewablePotterySherdRecipe::new)
        );

        private final StreamCodec<RegistryFriendlyByteBuf, RenewablePotterySherdRecipe> STREAM_CODEC = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC,
                CustomRecipe::category,
                RenewablePotterySherdRecipe::new
        );

        @Override
        public MapCodec<RenewablePotterySherdRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RenewablePotterySherdRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
