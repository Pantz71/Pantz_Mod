package pantz.mod.core.data.server;

import com.teamabnormals.blueprint.common.remolder.data.RemolderProvider;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import pantz.mod.common.loot.SetItemLootTableFunction;
import pantz.mod.common.loot.WardenDefeatedCondition;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMLootTables;
import pantz.mod.core.registry.PMItems;

import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;
import static com.teamabnormals.blueprint.common.remolder.RemolderTypes.sequence;
import static com.teamabnormals.blueprint.common.remolder.util.LootRemolders.addEntry;

public class PMDataRemolderProvider extends RemolderProvider {
    public PMDataRemolderProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(PantzMod.MOD_ID, PackOutput.Target.DATA_PACK, packOutput, lookupProvider);
    }

    @Override
    protected void registerEntries(HolderLookup.Provider provider) {
        registerLootRemolders(provider);
    }

    public void registerLootRemolders(HolderLookup.Provider provider) {
        HolderLookup.RegistryLookup<Biome> biome = provider.lookupOrThrow(Registries.BIOME);
        LootItemCondition.Builder wardenDefeated = WardenDefeatedCondition.wardenDefeated();
        LootItemCondition.Builder inDeepDark = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(biome.getOrThrow(Biomes.DEEP_DARK))));

        this.lootRemolder(BuiltInLootTables.SIMPLE_DUNGEON)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 5, 1, 3)),
                        addEntry(1, lootPool(STEEL_HORSE_ARMOR.get(), 10))
                ));

        this.lootRemolder(BuiltInLootTables.STRONGHOLD_LIBRARY)
                .remolder(sequence(
                        addEntry(1, lootPool(EARTH_GLOBE.get(), 2)),
                        addEntry(1, lootPool(CALLISTO_GLOBE.get(), 1)),
                        addEntry(1, lootPool(SUN_GLOBE.get(), 1)),
                        addEntry(1, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())

                ));

        this.lootRemolder(BuiltInLootTables.STRONGHOLD_CORRIDOR)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 5, 2, 6)),
                        addEntry(0, lootPool(DYNAMITE.get(), 2, 1, 3)),
                        addEntry(0, lootPool(COMBAT_DYNAMITE.get(), 2, 1, 3)),
                        addEntry(0, lootPool(STEEL_HORSE_ARMOR.get(), 10)),
                        addEntry(0, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())

                ));

        this.lootRemolder(BuiltInLootTables.STRONGHOLD_CROSSING)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 5, 2, 4)),
                        addEntry(0, lootPool(DYNAMITE.get(), 1)),
                        addEntry(1, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())
                ));

        this.lootRemolder(BuiltInLootTables.ABANDONED_MINESHAFT)
                .remolder(sequence(
                        addEntry(0, lootPool(GANYMEDE_GLOBE.get(), 10)),
                        addEntry(0, lootPool(CERES_GLOBE.get(), 10)),
                        addEntry(1, lootPool(PMItems.STEEL_INGOT.get(), 5, 1, 4)),
                        addEntry(1, lootPool(DYNAMITE.get(), 2)),
                        addEntry(0, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())
                ));

        this.lootRemolder(BuiltInLootTables.DESERT_PYRAMID)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 5, 1, 4)),
                        addEntry(0, lootPool(DYNAMITE.get(), 2)),
                        addEntry(2, lootPool(MARS_GLOBE.get(), 1))
                ));

        this.lootRemolder(BuiltInLootTables.JUNGLE_TEMPLE)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 10, 1, 4)),
                        addEntry(0, lootPool(DYNAMITE.get(), 3)),
                        addEntry(1, lootPool(MAKEMAKE_GLOBE.get(), 1))
                ));

        this.lootRemolder(BuiltInLootTables.WOODLAND_MANSION)
                .remolder(sequence(
                        addEntry(3, lootPool(PLUTO_GLOBE.get(), 1)),
                        addEntry(3, lootPool(EUROPA_GLOBE.get(), 1)),
                        addEntry(1, lootPool(PMItems.STEEL_INGOT.get(), 3, 1, 5)),
                        addEntry(1, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())
                ));

        this.lootRemolder(BuiltInLootTables.PILLAGER_OUTPOST)
                .remolder(sequence(
                        addEntry(5, lootPool(GANYMEDE_GLOBE.get(), 1))
                ));

        this.lootRemolder(BuiltInLootTables.IGLOO_CHEST)
                .remolder(sequence(
                        addEntry(1, lootPool(IO_GLOBE.get(), 5)),
                        addEntry(1, lootPool(CERES_GLOBE.get(), 5))
                ));

        this.lootRemolder(BuiltInLootTables.SHIPWRECK_TREASURE)
                .remolder(sequence(
                        addEntry(1, lootPool(IO_GLOBE.get(), 1)),
                        addEntry(1, lootPool(MAKEMAKE_GLOBE.get(), 1))
                ));

        this.lootRemolder(BuiltInLootTables.BASTION_TREASURE)
                .remolder(sequence(
                        addEntry(0, lootPool(SULFUR_CRYSTAL.get(), 25, 2, 10)),
                        addEntry(0, lootPool(SULFUR_DUST.get(), 25, 4, 12)),
                        addEntry(0, lootPool(DYNAMITE.get(), 3, 2, 5)),
                        addEntry(0, damagedEnchantedPool(STEEL_CHESTPLATE.get(), 6, 0.8f, 1f, provider)),
                        addEntry(0, damagedEnchantedPool(STEEL_HELMET.get(), 6, 0.8f, 1f, provider)),
                        addEntry(0, damagedEnchantedPool(STEEL_SWORD.get(), 6, 0.8f, 1f, provider)),
                        addEntry(0, damagedEnchantedPool(STEEL_PICKAXE.get(), 6, 0.8f, 1f, provider)),
                        addEntry(0, damagedPool(STEEL_CHESTPLATE.get(), 8, 0.8f, 1f)),
                        addEntry(0, damagedPool(STEEL_HELMET.get(), 8, 0.8f, 1f)),
                        addEntry(2, lootPool(URANUS_GLOBE.get(), 1)),
                        addEntry(2, lootPool(NEPTUNE_GLOBE.get(), 1))
                ));

        this.lootRemolder(BuiltInLootTables.BASTION_OTHER)
                .remolder(sequence(
                        addEntry(2, lootPool(SULFUR_CRYSTAL.get(), 20, 1, 4)),
                        addEntry(2, lootPool(SULFUR_DUST.get(), 25, 2, 7)),
                        addEntry(2, lootPool(DYNAMITE.get(), 3, 1, 4))
                ));

        this.lootRemolder(BuiltInLootTables.BASTION_BRIDGE)
                .remolder(sequence(
                        addEntry(1, lootPool(SULFUR_CRYSTAL.get(), 15, 3, 7)),
                        addEntry(1, lootPool(SULFUR_DUST.get(), 15, 3, 10)),
                        addEntry(1, lootPool(DYNAMITE.get(), 3, 1, 4))
                ));

        this.lootRemolder(BuiltInLootTables.BASTION_HOGLIN_STABLE)
                .remolder(sequence(
                        addEntry(1, lootPool(SULFUR_CRYSTAL.get(), 15, 1, 4)),
                        addEntry(1, lootPool(SULFUR_DUST.get(), 20, 2, 7)),
                        addEntry(1, lootPool(DYNAMITE.get(), 3, 1, 4))
                ));

        this.lootRemolder(BuiltInLootTables.NETHER_BRIDGE)
                .remolder(sequence(
                        addEntry(0, lootPool(SULFUR_CRYSTAL.get(), 15, 2, 10)),
                        addEntry(0, lootPool(SULFUR_DUST.get(), 15, 4, 12)),
                        addEntry(0, lootPool(DYNAMITE.get(), 3, 1, 2)),
                        addEntry(1, lootPool(JUPITER_GLOBE.get(), 8)),
                        addEntry(1, lootPool(SATURN_GLOBE.get(), 8)),
                        addEntry(0, lootPool(STEEL_HORSE_ARMOR.get(), 10)),
                        addEntry(1, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())
                ));

        this.lootRemolder(BuiltInLootTables.END_CITY_TREASURE)
                .remolder(sequence(
                        addEntry(0, enchantedPool(STEEL_CHESTPLATE.get(), 3, 20, 39, provider)),
                        addEntry(0, enchantedPool(STEEL_BOOTS.get(), 3, 20, 39,  provider)),
                        addEntry(0, enchantedPool(STEEL_HELMET.get(), 3, 20, 39,  provider)),
                        addEntry(0, enchantedPool(STEEL_LEGGINGS.get(), 3, 20, 39,  provider)),
                        addEntry(0, enchantedPool(STEEL_SWORD.get(), 3, 20, 39,  provider)),
                        addEntry(0, enchantedPool(STEEL_SHOVEL.get(), 3, 20, 39,  provider)),
                        addEntry(0, enchantedPool(STEEL_PICKAXE.get(), 3, 20, 39,  provider)),
                        addEntry(1, lootPool(PLUTO_GLOBE.get(), 1)),
                        addEntry(1, lootPool(IRIS_GLOBE.get(), 1)),
                        addEntry(1, LootItem.lootTableItem(POTION_SATCHEL.get()).setWeight(1).apply(SetItemLootTableFunction.setLootTable(PMLootTables.POTION_SATCHEL)).build())
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_POT)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 100, 1, 2))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY)
                .remolder(sequence(
                        addEntry(0, lootPool(COMBAT_DYNAMITE.get(), 2, 1, 2))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION)
                .remolder(sequence(
                        addEntry(0, lootPool(STEEL_BLOCK.get(), 20, 1, 2)),
                        addEntry(0, damagedPool(STEEL_PICKAXE.get(), 5, 0.1f, 0.5f)),
                        addEntry(0, damagedPool(STEEL_AXE.get(), 5, 0.1f, 0.5f)),
                        addEntry(0, damagedPool(EXCAVATOR.get(), 5, 0.1f, 0.5f)),
                        addEntry(0, damagedPool(HAMMER.get(), 5, 0.1f, 0.5f))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION_BARREL)
                .remolder(sequence(
                        addEntry(0, damagedPool(STEEL_PICKAXE.get(), 5, 0.4f, 0.9f)),
                        addEntry(0, damagedPool(STEEL_AXE.get(), 5, 0.15f, 0.8f)),
                        addEntry(0, damagedPool(EXCAVATOR.get(), 5, 0.15f, 0.8f)),
                        addEntry(0, damagedPool(HAMMER.get(), 5, 0.4f, 0.9f))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 4, 2, 4)),
                        addEntry(0, lootPool(COMBAT_DYNAMITE.get(), 2, 1, 2))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE)
                .remolder(sequence(
                        addEntry(0, enchantedPool(STEEL_CHESTPLATE.get(), 2, 1, 10, provider)),
                        addEntry(0, enchantedPool(STEEL_PICKAXE.get(), 2, 5, 10, provider)),
                        addEntry(0, enchantedPool(EXCAVATOR.get(), 2, 1, 10, provider)),
                        addEntry(0, enchantedPool(HAMMER.get(), 2, 1, 10, provider)),
                        addEntry(0, lootPool(COMBAT_DYNAMITE.get(), 4, 2, 3))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE)
                .remolder(sequence(
                        addEntry(0, lootPool(CERES_GLOBE.get(), 1)),
                        addEntry(0, lootPool(SUN_GLOBE.get(), 1)),
                        addEntry(0, lootPool(GANYMEDE_GLOBE.get(), 1)),
                        addEntry(0, lootPool(IO_GLOBE.get(), 1)),
                        addEntry(0, lootPool(CALLISTO_GLOBE.get(), 1)),
                        addEntry(0, enchantedPool(DIAMOND_EXCAVATOR.get(), 2, 5, 15, provider)),
                        addEntry(0, enchantedPool(DIAMOND_HAMMER.get(), 2, 5, 15, provider))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON)
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 4, 2, 4)),
                        addEntry(0, lootPool(COMBAT_DYNAMITE.get(), 3, 2, 5))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE)
                .remolder(sequence(
                        addEntry(0, lootPool(STEEL_BLOCK.get(), 4)),
                        addEntry(0, enchantedPool(DIAMOND_EXCAVATOR.get(), 3, 5, 15, provider)),
                        addEntry(0, enchantedPool(DIAMOND_HAMMER.get(), 3, 5, 15, provider))
                ));

        this.lootRemolder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE)
                .remolder(sequence(
                        addEntry(0, lootPool(BLUE_SUN_GLOBE.get(), 1)),
                        addEntry(0, enchantedPool(DIAMOND_EXCAVATOR.get(), 3, 20, 39, provider)),
                        addEntry(0, enchantedPool(DIAMOND_HAMMER.get(), 3, 20, 39, provider))
                ));

        this.lootRemolder(moddedChest("atmospheric", "kousa_sanctum"))
                .remolder(sequence(
                        addEntry(0, lootPool(PMItems.STEEL_INGOT.get(), 15, 1, 7)),
                        addEntry(0, lootPool(STEEL_HORSE_ARMOR.get(), 3))
                ));

        this.lootRemolder(BuiltInLootTables.LIBRARIAN_GIFT)
                .path()
                .remolder(sequence(
                        addEntry(0, lootPool(RED_ENVELOPE.get(), 1))
                ));

        this.lootRemolder(BuiltInLootTables.PIGLIN_BARTERING)
                .remolder(sequence(
                        addEntry(0, lootPool(Blocks.GILDED_BLACKSTONE, 40, 4, 10)),
                        addEntry(0, lootPool(Blocks.NETHERRACK, 40, 8, 16)),
                        addEntry(0, lootPool(Blocks.SOUL_SOIL, 40, 2, 8))
                ));

        this.lootRemolder(BuiltInLootTables.FISHING_TREASURE)
                .remolder(sequence(
                        addEntry(0, LootItem.lootTableItem(Items.ECHO_SHARD).when(wardenDefeated.and(inDeepDark)).build())
                ));
    }

    public Entry lootRemolder(ResourceKey<LootTable> key) {
        return this.lootRemolder(key.location());
    }

    public Entry lootRemolder(ResourceLocation location) {
        String name = "loot_table/" + location.getPath();
        return this.entry(name).path(name);
    }

    private ResourceLocation moddedChest(String modId, String name) {
        return ResourceLocation.fromNamespaceAndPath(modId, "loot_table/chests/" + name);
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

    private static LootPoolEntryContainer enchantedPool(ItemLike item, int weight, float minLevel, float maxLevel, HolderLookup.Provider provider) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(EnchantWithLevelsFunction.enchantWithLevels(provider, UniformGenerator.between(minLevel, maxLevel))).build();
    }

    private static LootPoolEntryContainer damagedEnchantedPool(ItemLike item, int weight, float minDamage, float maxDamage, HolderLookup.Provider provider) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(minDamage, maxDamage))).apply(EnchantRandomlyFunction.randomApplicableEnchantment(provider)).build();
    }
}
