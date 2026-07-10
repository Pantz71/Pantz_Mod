package pantz.mod.common.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import pantz.mod.common.world.WardenWorldData;
import pantz.mod.core.registry.PMLootConditions;

public class WardenDefeatedCondition implements LootItemCondition {
    private static final WardenDefeatedCondition INSTANCE = new WardenDefeatedCondition();
    public static final MapCodec<WardenDefeatedCondition> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public LootItemConditionType getType() {
        return PMLootConditions.WARDEN_DEFEATED.get();
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity killer = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        Entity directKiller = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);

        Player player = null;
        if (entity instanceof Player playerEntity) {
            player = playerEntity;
        } else if (killer instanceof Player playerKiller) {
            player = playerKiller;
        } else if (directKiller instanceof Player playerDirect) {
            player = playerDirect;
        }

        if (player != null) {
            System.out.println("Condition triggered for player: " + player.getName().getString());
            return WardenWorldData.get(player.level()).hasPlayerKilled(player.getUUID());
        }

        System.out.println("Condition checked, but no player context could be determined.");
        return false;
    }

    public static Builder wardenDefeated() {
        return () -> INSTANCE;
    }
}
