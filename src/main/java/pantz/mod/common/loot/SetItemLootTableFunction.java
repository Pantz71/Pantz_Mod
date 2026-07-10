package pantz.mod.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SeededContainerLoot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import pantz.mod.core.other.PMLootFunctions;

import java.util.List;

public class SetItemLootTableFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetItemLootTableFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).and(
                    ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(f -> f.lootTable)
            ).apply(instance, SetItemLootTableFunction::new)
    );

    private final ResourceKey<LootTable> lootTable;

    public SetItemLootTableFunction(List<LootItemCondition> conditions, ResourceKey<LootTable> lootTable) {
        super(conditions);
        this.lootTable = lootTable;
    }

    @Override
    protected ItemStack run(ItemStack pStack, LootContext pContext) {
        if (!pStack.isEmpty()) {
            pStack.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(
                    this.lootTable,
                    pContext.getRandom().nextLong()
            ));
        }
        return pStack;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return PMLootFunctions.SET_ITEM_LOOT_TABLE.get();
    }

    public static LootItemConditionalFunction.Builder<?> setLootTable(ResourceKey<LootTable> lootTableKey) {
        return simpleBuilder((conditions) -> new SetItemLootTableFunction(conditions, lootTableKey));
    }
}
