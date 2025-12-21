package pantz.mod.core.data.server;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMItems;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMLootTableProvider extends LootTableProvider {
    public PMLootTableProvider(PackOutput pOutput) {
        super(pOutput, BuiltInLootTables.all(), ImmutableList.of(
            new SubProviderEntry(PMBlockLoot::new, LootContextParamSets.BLOCK)
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
            this.dropSelf(STEEL_BLOCK.get());
            this.dropSelf(STEEL_BARS.get());
            this.add(STEEL_DOOR.get(), this::createDoorTable);
            this.dropSelf(STEEL_TRAPDOOR.get());

            this.add(NETHER_SULFUR_ORE.get(), this.createDustOreDrop(NETHER_SULFUR_ORE.get(), PMItems.SULFUR_CRYSTAL.get(), 3, 7));
            this.dropSelf(SULFUR_BLOCK.get());
            this.dropSelf(SULFUR_BRICKS.get());
            this.dropSelf(SULFUR_BRICK_STAIRS.get());
            this.add(SULFUR_BRICK_SLAB.get(), this::createSlabItemTable);
            this.dropSelf(SULFUR_BRICK_WALL.get());
            this.dropSelf(CHISELED_SULFUR_BRICKS.get());

            this.add(SULFUR_CLUSTER.get(), createSilkTouchDispatchTable(SULFUR_CLUSTER.get(), LootItem.lootTableItem(PMItems.SULFUR_CRYSTAL.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                    .otherwise(this.applyExplosionDecay(SULFUR_CLUSTER.get(), LootItem.lootTableItem(PMItems.SULFUR_CRYSTAL.get())
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));

            this.dropWhenSilkTouch(SMALL_SULFUR_BUD.get());
            this.dropWhenSilkTouch(MEDIUM_SULFUR_BUD.get());
            this.dropWhenSilkTouch(LARGE_SULFUR_BUD.get());

            this.dropSelf(STONE_PEDESTAL.get());
            this.dropSelf(DEEPSLATE_PEDESTAL.get());
            this.dropSelf(BLACKSTONE_PEDESTAL.get());
            this.dropSelf(QUARTZ_PEDESTAL.get());
            this.dropSelf(PURPUR_PEDESTAL.get());
            this.dropSelf(PRISMARINE_PEDESTAL.get());

            this.dropSelf(ENDER_SCANNER.get());


        }

        private LootTable.Builder createDustOreDrop(Block block, ItemLike item, int min, int max) {
            return createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(PantzMod.MOD_ID)).collect(Collectors.toSet());
        }
    }
}
