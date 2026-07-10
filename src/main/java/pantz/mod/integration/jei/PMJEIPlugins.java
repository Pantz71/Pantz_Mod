package pantz.mod.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import pantz.mod.common.crafting.PotterySherdDisplayRecipe;
import pantz.mod.common.crafting.RenewablePotterySherdRecipe;
import pantz.mod.common.inventory.PotionSatchelScreen;
import pantz.mod.common.inventory.TrashCanScreen;
import pantz.mod.core.PantzMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class PMJEIPlugins implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return PantzMod.location(PantzMod.MOD_ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager manager = level.getRecipeManager();

        RecipeHolder<CraftingRecipe> sherdRecipe = manager.getAllRecipesFor(RecipeType.CRAFTING)
                .stream()
                .filter(holder -> holder.value() instanceof RenewablePotterySherdRecipe)
                .findFirst()
                .orElse(null);

        if (sherdRecipe != null) {
            List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

            BuiltInRegistries.ITEM.getTag(ItemTags.DECORATED_POT_INGREDIENTS).ifPresent(tag -> tag.forEach(holder -> {
                ItemStack sherd = new ItemStack(holder.value());
                CraftingRecipe craftingRecipe = new PotterySherdDisplayRecipe(sherd);
                ResourceLocation id = sherdRecipe.id().withSuffix("/" + BuiltInRegistries.ITEM.getKey(holder.value()).getPath());
                recipes.add(new RecipeHolder<>(id, craftingRecipe));
            }));

            registration.addRecipes(RecipeTypes.CRAFTING, recipes);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(TrashCanScreen.class, new IGuiContainerHandler<>() {
            @Override
            public java.util.List<Rect2i> getGuiExtraAreas(TrashCanScreen containerScreen) {
                return Collections.singletonList(new Rect2i(containerScreen.getGuiLeft() + 176, containerScreen.getGuiTop(), 20, 20));
            }
        });
        registration.addGenericGuiContainerHandler(PotionSatchelScreen.class, new IGuiContainerHandler<>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(AbstractContainerScreen containerScreen) {
                return Collections.singletonList(new Rect2i(containerScreen.getGuiLeft() - 32, containerScreen.getGuiTop(), 32, containerScreen.getXSize()));
            }
        });
    }
}
