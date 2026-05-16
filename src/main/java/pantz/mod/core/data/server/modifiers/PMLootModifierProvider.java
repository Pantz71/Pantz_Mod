package pantz.mod.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolEntriesModifier;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import pantz.mod.common.loot.WardenDefeatedCondition;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMLootModifierProvider extends LootModifierProvider {
    private static final LootItemCondition.Builder WARDEN_DEFEATED = WardenDefeatedCondition.wardenDefeated();
    private static final LootItemCondition.Builder IN_DEEP_DARK = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.DEEP_DARK));

    public PMLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(PantzMod.MOD_ID, output, lookupProvider);
    }

    @Override
    protected void registerEntries(HolderLookup.Provider provider) {
        this.entry("simple_dungeon").selects(BuiltInLootTables.SIMPLE_DUNGEON)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 5, 1, 3)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(STEEL_HORSE_ARMOR.get(), 10)
                        )));

        this.entry("stronghold_library").selects(BuiltInLootTables.STRONGHOLD_LIBRARY)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(EARTH_GLOBE.get(), 2),
                                lootPool(CALLISTO_GLOBE.get(), 1),
                                lootPool(SUN_GLOBE.get(), 1)
                        )));
        this.entry("stronghold_corridor").selects(BuiltInLootTables.STRONGHOLD_CORRIDOR)
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 5, 2, 6),
                                lootPool(DYNAMITE.get(), 2, 1, 3),
                                lootPool(COMBAT_DYNAMITE.get(), 2, 1, 3),
                                lootPool(STEEL_HORSE_ARMOR.get(), 10)
                        )));
        this.entry("stronghold_crossing").selects(BuiltInLootTables.STRONGHOLD_CROSSING)
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 5, 2, 4),
                                lootPool(DYNAMITE.get(), 1)
                        )));

        this.entry("abandoned_mineshaft").selects(BuiltInLootTables.ABANDONED_MINESHAFT)
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(GANYMEDE_GLOBE.get(), 10),
                                lootPool(CERES_GLOBE.get(), 10)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 5, 1, 4),
                                lootPool(DYNAMITE.get(), 2)
                        )));

        this.entry("desert_pyramid").selects(BuiltInLootTables.DESERT_PYRAMID)
                .addModifier(new LootPoolEntriesModifier(false, 2,
                        List.of(
                                lootPool(MARS_GLOBE.get(), 1)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 5, 1, 4),
                                lootPool(DYNAMITE.get(), 2)
                        )));

        this.entry("jungle_temple").selects(BuiltInLootTables.JUNGLE_TEMPLE)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(MAKEMAKE_GLOBE.get(), 1)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 10, 1, 4),
                                lootPool(DYNAMITE.get(), 3)
                        )));

        this.entry("woodland_mansion").selects(BuiltInLootTables.WOODLAND_MANSION)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 3, 1, 5)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 3,
                        List.of(
                                lootPool(PLUTO_GLOBE.get(), 1),
                                lootPool(EUROPA_GLOBE.get(), 1)
                        )));

        this.entry("pillager_outpost").selects(BuiltInLootTables.PILLAGER_OUTPOST)
                .addModifier(new LootPoolEntriesModifier(false, 5,
                        List.of(
                                lootPool(GANYMEDE_GLOBE.get(), 1)
                        )));

        this.entry("igloo_chest").selects(BuiltInLootTables.IGLOO_CHEST)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(IO_GLOBE.get(), 5),
                                lootPool(CERES_GLOBE.get(), 5)
                        )));

        this.entry("shipwreck_treasure").selects(BuiltInLootTables.SHIPWRECK_TREASURE)
                .addModifier(new LootPoolEntriesModifier(false, 2,
                        List.of(
                                lootPool(IO_GLOBE.get(), 1),
                                lootPool(MAKEMAKE_GLOBE.get(), 1)
                        )));

        this.entry("bastion_treasure").selects(BuiltInLootTables.BASTION_TREASURE)
                .addModifier(new LootPoolEntriesModifier(false, 2,
                        List.of(
                                lootPool(URANUS_GLOBE.get(), 1),
                                lootPool(NEPTUNE_GLOBE.get(), 1)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(SULFUR_CRYSTAL.get(), 25, 2, 10),
                                lootPool(SULFUR_DUST.get(), 25, 4, 12),
                                lootPool(DYNAMITE.get(), 3, 2, 5),
                                damagedEnchantedPool(STEEL_CHESTPLATE.get(), 6, 0.8f, 1f),
                                damagedEnchantedPool(STEEL_HELMET.get(), 6, 0.8f, 1f),
                                damagedEnchantedPool(STEEL_SWORD.get(), 6, 0.8f, 1f),
                                damagedEnchantedPool(STEEL_PICKAXE.get(), 6, 0.8f, 1f),
                                damagedPool(STEEL_CHESTPLATE.get(), 8,0.8f, 1.0f),
                                damagedPool(STEEL_HELMET.get(), 8,0.8f, 1.0f)
                        )));

        this.entry("bastion_other").selects(BuiltInLootTables.BASTION_OTHER)
                .addModifier(new LootPoolEntriesModifier(false, 2,
                        List.of(
                                lootPool(SULFUR_CRYSTAL.get(), 20, 1, 4),
                                lootPool(SULFUR_DUST.get(), 25, 2, 7),
                                lootPool(DYNAMITE.get(), 3, 1, 4)
                        )));

        this.entry("bastion_bridge").selects(BuiltInLootTables.BASTION_BRIDGE)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(SULFUR_CRYSTAL.get(), 15, 3, 7),
                                lootPool(SULFUR_DUST.get(), 15, 6, 10),
                                lootPool(DYNAMITE.get(), 3, 1, 4)
                        )));

        this.entry("bastion_hoglin_stable").selects(BuiltInLootTables.BASTION_HOGLIN_STABLE)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(SULFUR_CRYSTAL.get(), 15, 1, 4),
                                lootPool(SULFUR_DUST.get(), 20, 2, 7),
                                lootPool(DYNAMITE.get(), 3, 1, 4)
                        )));

        this.entry("nether_bridge").selects(BuiltInLootTables.NETHER_BRIDGE)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(JUPITER_GLOBE.get(), 8),
                                lootPool(SATURN_GLOBE.get(), 8)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(SULFUR_CRYSTAL.get(), 15, 2, 10),
                                lootPool(SULFUR_DUST.get(), 15, 4, 12),
                                lootPool(DYNAMITE.get(), 3, 1, 2),
                                lootPool(STEEL_HORSE_ARMOR.get(), 10)
                        )));

        this.entry("end_city_treasure").selects(BuiltInLootTables.END_CITY_TREASURE)
                .addModifier(new LootPoolEntriesModifier(false, 1,
                        List.of(
                                lootPool(PLUTO_GLOBE.get(), 1),
                                lootPool(BLUE_SUN_GLOBE.get(), 1),
                                lootPool(IRIS_GLOBE.get(), 1)
                        )))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                enchantedPool(STEEL_CHESTPLATE.get(), 3, 20, 39),
                                enchantedPool(STEEL_BOOTS.get(), 3, 20, 39),
                                enchantedPool(STEEL_HELMET.get(), 3, 20, 39),
                                enchantedPool(STEEL_LEGGINGS.get(), 3, 20, 39),
                                enchantedPool(STEEL_SWORD.get(), 3, 20, 39),
                                enchantedPool(STEEL_SHOVEL.get(), 3, 20, 39),
                                enchantedPool(STEEL_PICKAXE.get(), 3, 20, 39)
                        )));

        this.entry("kousa_sanctum").selects(new ResourceLocation("atmospheric", "chests/kousa_sanctum"))
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(PMItems.STEEL_INGOT.get(), 15, 1, 7),
                                lootPool(STEEL_HORSE_ARMOR.get(), 3)
                        )));

        this.entry("librarian_gift").selects(BuiltInLootTables.LIBRARIAN_GIFT)
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(RED_ENVELOPE.get(), 1)
                        )));

        this.entry("piglin_bartering").selects(BuiltInLootTables.PIGLIN_BARTERING)
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                lootPool(Blocks.GILDED_BLACKSTONE, 40, 4, 10),
                                lootPool(Blocks.NETHERRACK, 40, 8, 16),
                                lootPool(Blocks.SOUL_SOIL, 40, 2, 8)
                        )));

        this.entry("fishing/treasure").selects(BuiltInLootTables.FISHING_TREASURE)
                .addModifier(new LootPoolEntriesModifier(false, 0,
                        List.of(
                                LootItem.lootTableItem(Items.ECHO_SHARD).when(WARDEN_DEFEATED.and(IN_DEEP_DARK)).build()
                        )));

    }

    private static LootPoolEntryContainer lootPool(ItemLike item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight).build();
    }

    private static LootPoolEntryContainer lootPool(ItemLike item, int weight, int min, int max) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))).build();
    }

    private static LootPoolEntryContainer damagedPool(ItemLike item, int weight, float minDamage, float maxDamage) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(minDamage, maxDamage))).build();
    }

    private static LootPoolEntryContainer enchantedPool(ItemLike item, float minLevel, float maxLevel, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(minLevel, maxLevel)).allowTreasure()).build();
    }

    private static LootPoolEntryContainer damagedEnchantedPool(ItemLike item, int weight, float minDamage, float maxDamage) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(minDamage, maxDamage))).apply(EnchantRandomlyFunction.randomApplicableEnchantment()).build();
    }
}
