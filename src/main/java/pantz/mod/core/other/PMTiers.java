package pantz.mod.core.other;

import com.teamabnormals.blueprint.core.api.BlueprintArmorMaterial;
import com.teamabnormals.blueprint.core.api.BlueprintItemTier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.tags.PMItemTags;
import pantz.mod.core.registry.PMItems;

public class PMTiers {
    public static class PMItemTiers {
        public static final BlueprintItemTier STEEL = new BlueprintItemTier(2, 780, 7.0f, 3f, 12, () -> Ingredient.of(PMItemTags.INGOTS_STEEL));

    }

    public static class PMArmorMaterials {
        public static final BlueprintArmorMaterial STEEL = new BlueprintArmorMaterial(PantzMod.location("steel"), 17, new int[]{2, 6, 7, 2}, 10, () -> SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.1f, () -> Ingredient.of(PMItemTags.INGOTS_STEEL));
    }
}
