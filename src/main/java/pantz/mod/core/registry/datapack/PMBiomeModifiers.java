package pantz.mod.core.registry.datapack;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.tags.PMBiomeTags;
import pantz.mod.core.registry.PMFeatures.*;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PMBiomeModifiers {
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        addFeature(context, "ore_sulfur", BiomeTags.IS_OVERWORLD, Decoration.UNDERGROUND_ORES, PMPlacedFeatures.ORE_SULFUR);
        addFeature(context, "ore_sulfur_nether", PMBiomeTags.HAS_SULFUR_NETHER, Decoration.UNDERGROUND_ORES, PMPlacedFeatures.ORE_SULFUR_NETHER);
        addFeature(context, "ore_sulfur_delta", PMBiomeTags.IS_BASALT_DELTAS, Decoration.UNDERGROUND_ORES, PMPlacedFeatures.ORE_SULFUR_DELTAS);
        addFeature(context, "ore_sulfur_block", PMBiomeTags.HAS_SULFUR_NETHER, Decoration.UNDERGROUND_ORES, PMPlacedFeatures.ORE_SULFUR_BLOCK);
    }

    @SafeVarargs
    private static void addFeature(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, Decoration step, ResourceKey<PlacedFeature>... features) {
        register(context, "add_feature/" + name, () -> new ForgeBiomeModifiers.AddFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), featureSet(context, features), step));
    }

    private static void register(BootstapContext<BiomeModifier> context, String name, Supplier<? extends BiomeModifier> modifier) {
        context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, PantzMod.location(name)), modifier.get());
    }

    @SafeVarargs
    private static HolderSet<PlacedFeature> featureSet(BootstapContext<?> context, ResourceKey<PlacedFeature>... features) {
        return HolderSet.direct(Stream.of(features).map(placedFeatureKey -> context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeatureKey)).collect(Collectors.toList()));
    }
}
