package pantz.mod.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import pantz.mod.common.world.WardenWorldData;
import pantz.mod.core.registry.PMLootConditions;

public class WardenDefeatedCondition implements LootItemCondition {
    @Override
    public LootItemConditionType getType() {
        return PMLootConditions.WARDEN_DEFEATED.get();
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);

        Player player = null;
        if (entity instanceof Player playerEntity) {
            player = playerEntity;
        } else if (killer instanceof Player playerKiller) {
            player = playerKiller;
        }

        if (player != null) {
            return WardenWorldData.get(player.level()).hasPlayerKilled(player.getUUID());
        }

        return false;
    }

    public static LootItemCondition.Builder wardenDefeated() {
        return WardenDefeatedCondition::new;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<WardenDefeatedCondition> {

        @Override
        public void serialize(JsonObject pJson, WardenDefeatedCondition pValue, JsonSerializationContext pSerializationContext) {

        }

        @Override
        public WardenDefeatedCondition deserialize(JsonObject pJson, JsonDeserializationContext pSerializationContext) {
            return new WardenDefeatedCondition();
        }
    }
}
