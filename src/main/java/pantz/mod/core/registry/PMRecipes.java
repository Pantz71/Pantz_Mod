package pantz.mod.core.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.crafting.RenewablePotterySherdRecipe;
import pantz.mod.core.PantzMod;

public class PMRecipes {
    public static class PMRecipeTypes {
        public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, PantzMod.MOD_ID);

    }

    public static class PMRecipeSerializers {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PantzMod.MOD_ID);

        public static final RegistryObject<RecipeSerializer<RenewablePotterySherdRecipe>> POTTERY_SHERD_DUPLICATION =
                SERIALIZERS.register("pottery_sherd_duplication", RenewablePotterySherdRecipe.Serializer::new);
    }

    public static void register(IEventBus bus) {
        PMRecipeSerializers.SERIALIZERS.register(bus);
        PMRecipeTypes.TYPES.register(bus);
    }
}
