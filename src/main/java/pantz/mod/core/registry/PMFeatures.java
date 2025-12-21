package pantz.mod.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.Tags;
import pantz.mod.core.PantzMod;

import java.util.List;

public class PMFeatures {

    public static class PMConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SULFUR = createKey("ore_sulfur");
        public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SULFUR_BLOCK = createKey("ore_sulfur_block");

        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
            RuleTest netherrack = new TagMatchTest(Tags.Blocks.NETHERRACK);
            register(context, ORE_SULFUR, Feature.ORE, new OreConfiguration(netherrack, PMBlocks.NETHER_SULFUR_ORE.get().defaultBlockState(), 16));
            register(context, ORE_SULFUR_BLOCK, Feature.ORE, new OreConfiguration(netherrack, PMBlocks.SULFUR_BLOCK.get().defaultBlockState(), 20, 0f));

        }

        public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
            return ResourceKey.create(Registries.CONFIGURED_FEATURE, PantzMod.location(name));
        }

        public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
            context.register(key, new ConfiguredFeature<>(feature, config));
        }
    }

    public static class PMPlacedFeatures {
        public static final ResourceKey<PlacedFeature> ORE_SULFUR_NETHER = createKey("ore_sulfur_nether");
        public static final ResourceKey<PlacedFeature> ORE_SULFUR_DELTAS = createKey("ore_sulfur_deltas");
        public static final ResourceKey<PlacedFeature> ORE_SULFUR_BLOCK = createKey("ore_sulfur_block");

        public static void bootstrap(BootstapContext<PlacedFeature> context) {
            register(context, ORE_SULFUR_NETHER, PMConfiguredFeatures.ORE_SULFUR, commonOrePlacement(16, PlacementUtils.RANGE_10_10));
            register(context, ORE_SULFUR_DELTAS, PMConfiguredFeatures.ORE_SULFUR, commonOrePlacement(32, PlacementUtils.RANGE_10_10));
            register(context, ORE_SULFUR_BLOCK, PMConfiguredFeatures.ORE_SULFUR_BLOCK, commonOrePlacement(20, PlacementUtils.HEIGHTMAP_WORLD_SURFACE));
        }

        private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
            return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
        }

        private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
            return orePlacement(CountPlacement.of(pCount), pHeightRange);
        }

        private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
            return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
            context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
        }

        public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
            register(context, key, feature, List.of(modifiers));
        }

        public static ResourceKey<PlacedFeature> createKey(String name) {
            return ResourceKey.create(Registries.PLACED_FEATURE, PantzMod.location(name));
        }
    }
}
