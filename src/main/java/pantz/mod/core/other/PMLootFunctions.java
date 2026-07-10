package pantz.mod.core.other;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.loot.SetItemLootTableFunction;
import pantz.mod.core.PantzMod;

public class PMLootFunctions {
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, PantzMod.MOD_ID);

    public static final RegistryObject<LootItemFunctionType> SET_ITEM_LOOT_TABLE = LOOT_FUNCTIONS.register("set_item_loot_table", () -> new LootItemFunctionType(new SetItemLootTableFunction.Serializer()));
}
