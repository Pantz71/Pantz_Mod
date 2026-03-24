package pantz.mod.core.data.server;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.DoubleOrnamentBlock;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMLootContextParamSets;
import pantz.mod.core.registry.PMItems;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMLootTableProvider extends LootTableProvider {
    public static final ResourceLocation ENVELOPE = PantzMod.location("red_envelope/envelope");
    public static final ResourceLocation COMMON = PantzMod.location("red_envelope/common");
    public static final ResourceLocation RARE = PantzMod.location("red_envelope/rare");

    public PMLootTableProvider(PackOutput pOutput) {
        super(pOutput, BuiltInLootTables.all(), ImmutableList.of(
                new SubProviderEntry(PMBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(PMRedEnvelopeLoot::new, PMLootContextParamSets.RED_ENVELOPE)
        ));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
    }

    private static class PMBlockLoot extends BlockLootSubProvider {
        private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(Collectors.toSet());

        protected PMBlockLoot() {
            super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            // dropSelf
            for (RegistryObject<?> block : new RegistryObject[]{
                    STEEL_BLOCK, STEEL_BARS, STEEL_TRAPDOOR, STEEL_LANTERN,
                    SULFUR_BLOCK, SULFUR_BRICKS, SULFUR_BRICK_STAIRS, SULFUR_BRICK_WALL, CHISELED_SULFUR_BRICKS, SULFUR_LAMP,
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
                    QUARTZ_GLASS, QUARTZ_GLASS_PANE, LAPIS_GLASS, LAPIS_GLASS_PANE, REDSTONE_GLASS, REDSTONE_GLASS_PANE,
                    SNOW_BRICKS, SNOW_BRICK_STAIRS, SNOW_BRICK_WALL,
                    PACKED_ICE_BRICKS, PACKED_ICE_BRICK_STAIRS, PACKED_ICE_BRICK_WALL, CHISELED_PACKED_ICE_BRICKS,
                    BLUE_ICE_BRICKS, BLUE_ICE_BRICK_STAIRS, BLUE_ICE_BRICK_WALL, CHISELED_BLUE_ICE_BRICKS,
                    PACKED_ICE_TRAPDOOR, BLUE_ICE_TRAPDOOR, ICE_LANTERN,
                    RED_PAPER_LANTERN, ORANGE_PAPER_LANTERN, YELLOW_PAPER_LANTERN, LIME_PAPER_LANTERN, GREEN_PAPER_LANTERN, BLUE_PAPER_LANTERN, CYAN_PAPER_LANTERN,
                    LIGHT_BLUE_PAPER_LANTERN, PURPLE_PAPER_LANTERN, MAGENTA_PAPER_LANTERN, PINK_PAPER_LANTERN, BROWN_PAPER_LANTERN, BLACK_PAPER_LANTERN, GRAY_PAPER_LANTERN, LIGHT_GRAY_PAPER_LANTERN, WHITE_PAPER_LANTERN,
                    LOCK, UNIVERSAL_LOCK, SAFE
            }) {
                this.dropSelf((Block) block.get());
            }

            // door
            for (RegistryObject<?> block : new RegistryObject[]{
                    STEEL_DOOR, PACKED_ICE_DOOR, BLUE_ICE_DOOR
            }) {
                this.add((Block) block.get(), this::createDoorTable);
            }

            // slab
            for (RegistryObject<?> block : new RegistryObject[]{
                    SULFUR_BRICK_SLAB, SNOW_BRICK_SLAB, PACKED_ICE_BRICK_SLAB, BLUE_ICE_BRICK_SLAB
            }) {
                this.add((Block) block.get(), this::createSlabItemTable);
            }

            // silk touch
            for (RegistryObject<?> block : new RegistryObject[]{
                    SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD
            }) {
                this.dropWhenSilkTouch((Block) block.get());
            }

            // ornament
            for (RegistryObject<?> block : new RegistryObject[]{
                    ORNAMENT_FIRECRACKERS, ORNAMENT_LUCKY_COINS
            }) {
                this.add((Block) block.get(), this.createSinglePropConditionTable((Block) block.get(), DoubleOrnamentBlock.HALF, DoubleBlockHalf.LOWER));
            }

            this.add(NETHER_SULFUR_ORE.get(), this.createDustOreDrop(NETHER_SULFUR_ORE.get(), PMItems.SULFUR_DUST.get(), 3, 7));
            
            this.add(SULFUR_CLUSTER.get(), createSilkTouchDispatchTable(SULFUR_CLUSTER.get(), LootItem.lootTableItem(PMItems.SULFUR_SHARD.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                    .otherwise(this.applyExplosionDecay(SULFUR_CLUSTER.get(), LootItem.lootTableItem(PMItems.SULFUR_SHARD.get())
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));

        }

        private LootTable.Builder createDustOreDrop(Block block, ItemLike item, int min, int max) {
            return createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(PantzMod.MOD_ID)).collect(Collectors.toSet());
        }
    }

    private static class PMRedEnvelopeLoot implements LootTableSubProvider {

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(COMMON,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0f))

                                            .add(LootItem.lootTableItem(Items.EMERALD).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 35))))
                                            .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))))
                                            .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 30))))
                            )
            );

            consumer.accept(RARE,
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
            consumer.accept(ENVELOPE,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .add(LootTableReference.lootTableReference(COMMON))
                            )
                            .withPool(
                                    LootPool.lootPool()
                                            .when(LootItemRandomChanceCondition.randomChance(0.25f))
                                            .add(LootTableReference.lootTableReference(RARE))
                            )
            );
        }
    }
}
