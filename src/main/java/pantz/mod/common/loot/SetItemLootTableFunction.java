package pantz.mod.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import pantz.mod.core.other.PMLootFunctions;

public class SetItemLootTableFunction extends LootItemConditionalFunction {
    private final ResourceLocation lootTable;
    public SetItemLootTableFunction(LootItemCondition[] conditions, ResourceLocation lootTable) {
        super(conditions);
        this.lootTable = lootTable;
    }

    @Override
    protected ItemStack run(ItemStack pStack, LootContext pContext) {
        if (!pStack.isEmpty()) {
            CompoundTag tag = pStack.getOrCreateTag();
            tag.putString("LootTable", this.lootTable.toString());
            tag.putLong("LootTableSeed", pContext.getRandom().nextLong());
        }
        return pStack;
    }

    @Override
    public LootItemFunctionType getType() {
        return PMLootFunctions.SET_ITEM_LOOT_TABLE.get();
    }

    public static LootItemConditionalFunction.Builder<?> setLootTable(ResourceLocation lootTableRes) {
        return simpleBuilder((conditions) -> new SetItemLootTableFunction(conditions, lootTableRes));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetItemLootTableFunction> {
        @Override
        public void serialize(JsonObject json, SetItemLootTableFunction function, JsonSerializationContext context) {
            super.serialize(json, function, context);
            json.addProperty("loot_table", function.lootTable.toString());
        }

        @Override
        public SetItemLootTableFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            ResourceLocation res = new ResourceLocation(GsonHelper.getAsString(json, "loot_table"));
            return new SetItemLootTableFunction(conditions, res);
        }
    }
}
