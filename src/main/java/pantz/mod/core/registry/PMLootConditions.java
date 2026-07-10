package pantz.mod.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.common.loot.WardenDefeatedCondition;
import pantz.mod.core.PantzMod;

public class PMLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, PantzMod.MOD_ID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> WARDEN_DEFEATED = LOOT_CONDITION_TYPES.register("warden_defeated", () -> new LootItemConditionType(WardenDefeatedCondition.CODEC));
}
