package pantz.mod.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import com.teamabnormals.caverns_and_chasms.common.block.IngotLayer;
import com.teamabnormals.caverns_and_chasms.core.registry.CCItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredBlock;
import pantz.mod.common.block.DoubleOrnamentBlock;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMLootContextParamSets;
import pantz.mod.core.other.PMLootTables;
import pantz.mod.core.registry.PMItems;
import pantz.mod.core.registry.PMMobEffects;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMLootTableProvider extends LootTableProvider {
    public PMLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, BuiltInLootTables.all(), ImmutableList.of(
                new SubProviderEntry(PMBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(PMRedEnvelopeLoot::new, PMLootContextParamSets.ENVELOPE),
                new SubProviderEntry(PMContainerItemLoot::new, PMLootContextParamSets.CONTAINER_ITEM)
                ), registries);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }

    private static class PMBlockLoot extends BlockLootSubProvider {
        private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

        protected PMBlockLoot(HolderLookup.Provider registries) {
            super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

            // dropSelf
            for (DeferredBlock<?> block : new DeferredBlock[]{
                    STEEL_BLOCK, STEEL_BARS, STEEL_TRAPDOOR, STEEL_LANTERN, STEEL_BRICKS, STEEL_BRICK_STAIRS, STEEL_BRICK_WALL, CHISELED_STEEL_BRICKS, STEEL_CHAIN,
                    SULFUR, SULFUR_BLOCK, POLISHED_SULFUR, POLISHED_SULFUR_STAIRS, POLISHED_SULFUR_WALL,
                    SULFUR_BRICKS, SULFUR_BRICK_STAIRS, SULFUR_BRICK_WALL, CHISELED_SULFUR_BRICKS, SULFUR_LAMP,
                    STONE_PEDESTAL, DEEPSLATE_PEDESTAL, BLACKSTONE_PEDESTAL, QUARTZ_PEDESTAL, PURPUR_PEDESTAL, PRISMARINE_PEDESTAL,
                    ENDER_SCANNER, REDSTONE_CONFIGURATOR, WEATHER_DETECTOR, ENTITY_DETECTOR, POWER_DISPLAYER,
                    RANDOMIZER, EQUALIZER,
                    NOT_GATE, AND_GATE, OR_GATE, NAND_GATE, NOR_GATE, XOR_GATE, XNOR_GATE,
                    ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NAND_GATE, ADVANCED_NOR_GATE, ADVANCED_XOR_GATE, ADVANCED_XNOR_GATE, MAJORITY_GATE, MINORITY_GATE,
                    MERCURY_GLOBE, VENUS_GLOBE, EARTH_GLOBE, MARS_GLOBE, JUPITER_GLOBE, SATURN_GLOBE, URANUS_GLOBE, NEPTUNE_GLOBE,
                    PLUTO_GLOBE, CERES_GLOBE, MAKEMAKE_GLOBE, MOON_GLOBE, IO_GLOBE, EUROPA_GLOBE, CALLISTO_GLOBE, GANYMEDE_GLOBE,
                    SUN_GLOBE, BLUE_SUN_GLOBE, IRIS_GLOBE,
                    RED_REDSTONE_LAMP, ORANGE_REDSTONE_LAMP, YELLOW_REDSTONE_LAMP, LIME_REDSTONE_LAMP, GREEN_REDSTONE_LAMP, BLUE_REDSTONE_LAMP, CYAN_REDSTONE_LAMP,
                    LIGHT_BLUE_REDSTONE_LAMP, PURPLE_REDSTONE_LAMP, MAGENTA_REDSTONE_LAMP, PINK_REDSTONE_LAMP, BROWN_REDSTONE_LAMP, BLACK_REDSTONE_LAMP, GRAY_REDSTONE_LAMP, LIGHT_GRAY_REDSTONE_LAMP, WHITE_REDSTONE_LAMP,
                    ITEM_STAND, GLOW_ITEM_STAND, TRASH_CAN, ENDERPORTER, ROPE_LADDER,
                    CHORUS_GLASS, CHORUS_GLASS_PANE, SOUL_GLASS, SOUL_GLASS_PANE, ECHO_GLASS, ECHO_GLASS_PANE,
                    RED_PAPER_LANTERN, ORANGE_PAPER_LANTERN, YELLOW_PAPER_LANTERN, LIME_PAPER_LANTERN, GREEN_PAPER_LANTERN, BLUE_PAPER_LANTERN, CYAN_PAPER_LANTERN,
                    LIGHT_BLUE_PAPER_LANTERN, PURPLE_PAPER_LANTERN, MAGENTA_PAPER_LANTERN, PINK_PAPER_LANTERN, BROWN_PAPER_LANTERN, BLACK_PAPER_LANTERN, GRAY_PAPER_LANTERN, LIGHT_GRAY_PAPER_LANTERN, WHITE_PAPER_LANTERN,
                    LOCK, UNIVERSAL_LOCK, SAFE, SPIKE, SPRINKLER,
                    LEATHER_BLOCK, RABBIT_HIDE_BLOCK, PHANTOM_MEMBRANE_BLOCK, SUGAR_CANE_BLOCK, SUGAR_BLOCK, FEATHER_BLOCK, FIERY_LAMP, FEEDING_TROUGH
            }) {
                this.dropSelf(block.get());
            }

            // door
            for (DeferredBlock<?> block : new DeferredBlock[]{
                    STEEL_DOOR
            }) {
                this.add(block.get(), this::createDoorTable);
            }

            // slab
            for (DeferredBlock<?> block : new DeferredBlock[]{
                    POLISHED_SULFUR_SLAB, SULFUR_BRICK_SLAB, STEEL_BRICK_SLAB
            }) {
                this.add(block.get(), this::createSlabItemTable);
            }

            // silk touch
            for (DeferredBlock<?> block : new DeferredBlock[]{
                    SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD
            }) {
                this.dropWhenSilkTouch(block.get());
            }

            // ornament
            for (DeferredBlock<?> block : new DeferredBlock[]{
                    ORNAMENT_FIRECRACKERS, ORNAMENT_LUCKY_COINS
            }) {
                this.add(block.get(), this.createSinglePropConditionTable(block.get(), DoubleOrnamentBlock.HALF, DoubleBlockHalf.LOWER));
            }

            this.add(STEEL_INGOT.get(), this::createIngotDrops);

            this.add(SULFUR_ORE.get(), this.createSulfurOreDrop(SULFUR_ORE.get(), PMItems.SULFUR_CRYSTAL.get(), 3, 7));
            this.add(DEEPSLATE_SULFUR_ORE.get(), this.createSulfurOreDrop(DEEPSLATE_SULFUR_ORE.get(), PMItems.SULFUR_CRYSTAL.get(), 3, 7));
            this.add(NETHER_SULFUR_ORE.get(), this.createSulfurOreDrop(NETHER_SULFUR_ORE.get(), PMItems.SULFUR_CRYSTAL.get(), 1, 4));

            this.add(SULFUR_CLUSTER.get(), createSilkTouchDispatchTable(SULFUR_CLUSTER.get(), LootItem.lootTableItem(PMItems.SULFUR_CRYSTAL.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                    .otherwise(this.applyExplosionDecay(SULFUR_CLUSTER.get(), LootItem.lootTableItem(PMItems.SULFUR_CRYSTAL.get())
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));

        }

        protected LootTable.Builder createIngotDrops(Block block) {
            return createIngotDrops(block, block.asItem());
        }

        protected LootTable.Builder createIngotDrops(Block block, Item ingot) {
            return LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(this.applyExplosionDecay(ingot, LootItem.lootTableItem(ingot)
                                    .apply(List.of(1, 2, 3), i -> SetItemCountFunction.setCount(ConstantValue.exactly(i * 2))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(IngotBlock.LAYERS, i)
                                                    )
                                            ))
                            )).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(IngotBlock.LAYERS, 0)).invert())
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(this.applyExplosionDecay(ingot, LootItem.lootTableItem(ingot)
                                    .apply(List.of(IngotLayer.BOTH), layer -> SetItemCountFunction.setCount(ConstantValue.exactly(2))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(IngotBlock.TOP_INGOT, layer)
                                                    )
                                            ))
                            ))
                    );
        }

        private LootTable.Builder createSulfurOreDrop(Block block, ItemLike item, int min, int max) {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
            return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return BuiltInRegistries.BLOCK.stream().filter(block -> PantzMod.MOD_ID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace())).collect(Collectors.toSet());
        }
    }

    private record PMContainerItemLoot(HolderLookup.Provider provider) implements LootTableSubProvider {

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(PMLootTables.POTION_SATCHEL_BENEFICIAL_POTIONS,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(3.0f, 7.0f))
                                            .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.REGENERATION, Potions.STRENGTH, Potions.SWIFTNESS, Potions.LEAPING, PMMobEffects.NORMAL_PRECISION)))))
                                            .add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(10).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.REGENERATION, Potions.STRENGTH, Potions.SWIFTNESS, Potions.LEAPING, PMMobEffects.NORMAL_PRECISION)))))
                                            .add(LootItem.lootTableItem(CCItems.TETHER_POTION.get()).setWeight(4).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.REGENERATION, Potions.STRENGTH, Potions.SWIFTNESS, Potions.LEAPING, PMMobEffects.NORMAL_PRECISION)))))
                                            .add(LootItem.lootTableItem(CCItems.IMPACT_POTION.get()).setWeight(4).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.REGENERATION, Potions.STRENGTH, Potions.SWIFTNESS, Potions.LEAPING, PMMobEffects.NORMAL_PRECISION)))))

                            )
            );

            consumer.accept(PMLootTables.POTION_SATCHEL_HARMFUL_POTIONS,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(3.0f, 7.0f))
                                            .add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(2).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.SLOWNESS, Potions.WEAKNESS, Potions.POISON, PMMobEffects.NORMAL_CLUMSINESS)))))

                            )
            );

            consumer.accept(PMLootTables.POTION_SATCHEL_STRONGER_BENEFICIAL_POTIONS,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(3.0f, 7.0f))
                                            .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS, Potions.LONG_LEAPING, Potions.STRONG_LEAPING, PMMobEffects.LONG_PRECISION, PMMobEffects.STRONG_PRECISION)))))
                                            .add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(10).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS, Potions.LONG_LEAPING, Potions.STRONG_LEAPING, PMMobEffects.LONG_PRECISION, PMMobEffects.STRONG_PRECISION)))))
                                            .add(LootItem.lootTableItem(CCItems.TETHER_POTION.get()).setWeight(4).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS, Potions.LONG_LEAPING, Potions.STRONG_LEAPING, PMMobEffects.LONG_PRECISION, PMMobEffects.STRONG_PRECISION)))))
                                            .add(LootItem.lootTableItem(CCItems.IMPACT_POTION.get()).setWeight(4).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS, Potions.LONG_LEAPING, Potions.STRONG_LEAPING, PMMobEffects.LONG_PRECISION, PMMobEffects.STRONG_PRECISION)))))

                            )
            );

            consumer.accept(PMLootTables.POTION_SATCHEL_STRONGER_HARMFUL_POTIONS,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(3.0f, 7.0f))
                                            .add(LootItem.lootTableItem(Items.SPLASH_POTION).setWeight(2).apply(SetPotionFunction.setPotion(getRandomPotion(List.of(Potions.LONG_SLOWNESS, Potions.STRONG_SLOWNESS, Potions.LONG_WEAKNESS, Potions.LONG_POISON, Potions.STRONG_POISON, PMMobEffects.LONG_CLUMSINESS, PMMobEffects.STRONG_CLUMSINESS)))))

                            )
            );

            consumer.accept(PMLootTables.POTION_SATCHEL,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
                                            .add(NestedLootTable.lootTableReference(PMLootTables.POTION_SATCHEL_BENEFICIAL_POTIONS))
                                            .add(NestedLootTable.lootTableReference(PMLootTables.POTION_SATCHEL_STRONGER_BENEFICIAL_POTIONS).when(LootItemRandomChanceCondition.randomChance(0.5f)))
                                            .add(NestedLootTable.lootTableReference(PMLootTables.POTION_SATCHEL_HARMFUL_POTIONS))
                                            .add(NestedLootTable.lootTableReference(PMLootTables.POTION_SATCHEL_STRONGER_HARMFUL_POTIONS).when(LootItemRandomChanceCondition.randomChance(0.5f)))
                            ));

        }

        private static Holder<Potion> getRandomPotion(List<Holder<Potion>> list) {
            if (list == null || list.isEmpty()) {
                return null;
            }
            RandomSource random = RandomSource.create();
            int index = random.nextInt(list.size());
            return list.get(index);
        }
    }

    private record PMRedEnvelopeLoot(HolderLookup.Provider provider) implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(PMLootTables.ENVELOPE_COMMON,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0f))

                                            .add(LootItem.lootTableItem(Items.EMERALD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 35))))
                                            .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))))
                                            .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 30))))
                            )
            );

            consumer.accept(PMLootTables.ENVELOPE_RARE,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0f))

                                            .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                            .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
                                            .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10))))
                                            .add(LootItem.lootTableItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                            )
            );
            consumer.accept(PMLootTables.ENVELOPE,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .add(NestedLootTable.lootTableReference(PMLootTables.ENVELOPE_COMMON))
                            )
                            .withPool(
                                    LootPool.lootPool()
                                            .when(LootItemRandomChanceCondition.randomChance(0.25f))
                                            .add(NestedLootTable.lootTableReference(PMLootTables.ENVELOPE_RARE))
                            )
            );
        }
    }
}
