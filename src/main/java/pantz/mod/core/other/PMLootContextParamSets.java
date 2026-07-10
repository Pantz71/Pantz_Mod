package pantz.mod.core.other;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import pantz.mod.core.PantzMod;

import java.util.function.Consumer;

public class PMLootContextParamSets {

    public static LootContextParamSet ENVELOPE = register("envelope", PMLootContextParamSets::defaultContextParams);
    public static LootContextParamSet CONTAINER_ITEM = register("container_item", PMLootContextParamSets::defaultContextParams);

    public static void register() {}

    public static void defaultContextParams(LootContextParamSet.Builder builder) {
        builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY);
    }

    private static LootContextParamSet register(String name, Consumer<LootContextParamSet.Builder> builderConsumer) {
        LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
        builderConsumer.accept(builder);
        LootContextParamSet paramSet = builder.build();

        ResourceLocation id = PantzMod.location(name);
        if (LootContextParamSets.REGISTRY.containsKey(id)) {
            throw new IllegalStateException("Loot table parameter set " + id + " is already registered");
        }

        LootContextParamSets.REGISTRY.put(id, paramSet);
        return paramSet;
    }
}
