package pantz.mod.core.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import pantz.mod.core.PantzMod;

public class PMLootTables {
    public static final ResourceKey<LootTable> ENVELOPE = create("envelope");
    public static final ResourceKey<LootTable> ENVELOPE_COMMON = create("envelope_common");
    public static final ResourceKey<LootTable> ENVELOPE_RARE = create("envelope_rare");

    public static final ResourceKey<LootTable> POTION_SATCHEL = create("container_item/potion_satchel");
    public static final ResourceKey<LootTable> POTION_SATCHEL_BENEFICIAL_POTIONS = create("container_item/potion_satchel_beneficial_potions");
    public static final ResourceKey<LootTable> POTION_SATCHEL_HARMFUL_POTIONS = create("container_item/potion_satchel_harmful_potions");
    public static final ResourceKey<LootTable> POTION_SATCHEL_STRONGER_BENEFICIAL_POTIONS = create("container_item/potion_satchel_stronger_beneficial_potions");
    public static final ResourceKey<LootTable> POTION_SATCHEL_STRONGER_HARMFUL_POTIONS = create("container_item/potion_satchel_stronger_harmful_potions");

    private static ResourceKey<LootTable> create(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, PantzMod.location(name));
    }
}
