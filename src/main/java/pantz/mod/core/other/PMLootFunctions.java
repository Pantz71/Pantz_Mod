package pantz.mod.core.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.common.loot.SetItemLootTableFunction;
import pantz.mod.core.PantzMod;

public class PMLootFunctions {
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, PantzMod.MOD_ID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetItemLootTableFunction>> SET_ITEM_LOOT_TABLE = LOOT_FUNCTIONS.register("set_item_loot_table", () -> new LootItemFunctionType<>(SetItemLootTableFunction.CODEC));
}
