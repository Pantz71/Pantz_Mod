package pantz.mod.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import pantz.mod.common.crafting.PotterySherdDisplayRecipe;
import pantz.mod.common.crafting.RenewablePotterySherdRecipe;
import pantz.mod.core.PantzMod;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class PMJEIPlugins implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return PantzMod.location(PantzMod.MOD_ID);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        RenewablePotterySherdRecipe sherdRecipe = manager.getAllRecipesFor(RecipeType.CRAFTING)
                .stream().filter(recipe -> recipe instanceof RenewablePotterySherdRecipe)
                .map(recipe -> (RenewablePotterySherdRecipe) recipe)
                .findFirst().orElse(null);

        if (sherdRecipe != null) {
            List<CraftingRecipe> recipes = new ArrayList<>();

            BuiltInRegistries.ITEM.getTag(ItemTags.DECORATED_POT_INGREDIENTS).ifPresent(tag -> tag.forEach(holder -> {
                ItemStack sherd = new ItemStack(holder.value());
                recipes.add(new PotterySherdDisplayRecipe(sherdRecipe.getId(), sherd));
            }));
            registration.addRecipes(RecipeTypes.CRAFTING, recipes);
        }
    }
}
