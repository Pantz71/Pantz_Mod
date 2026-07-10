package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import pantz.mod.common.entity.projectile.Dynamite;
import pantz.mod.core.PantzMod;

public class PMEntityTypes {
    public static final EntitySubRegistryHelper ENTITY_TYPES = PantzMod.REGISTRY_HELPER.getEntitySubHelper();

    public static final DeferredHolder<EntityType<?>, EntityType<Dynamite>> DYNAMITE = ENTITY_TYPES.createEntity("dynamite", Dynamite::new, MobCategory.MISC, 0.25f, 0.25f);

}
