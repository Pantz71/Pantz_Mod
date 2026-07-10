package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import pantz.mod.core.PantzMod;

public class PMEntityTypeTags {

    public static final TagKey<EntityType<?>> ENDER_SCANNER_IMMUNE_TYPES = entityTypeTag("ender_scanner_immune_types");

    public static TagKey<EntityType<?>> entityTypeTag(String name) {
        return TagUtil.entityTypeTag(PantzMod.MOD_ID, name);
    }

}
