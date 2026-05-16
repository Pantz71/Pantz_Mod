package pantz.mod.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.loot.WardenDefeatedCondition;
import pantz.mod.common.loot.WardenDefeatedCondition.*;
import pantz.mod.core.PantzMod;

public class PMLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, PantzMod.MOD_ID);

    public static final RegistryObject<LootItemConditionType> WARDEN_DEFEATED = LOOT_CONDITION_TYPES.register("warden_defeated", () -> new LootItemConditionType(new WardenDefeatedCondition.Serializer()));
}
