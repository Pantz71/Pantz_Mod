package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import pantz.mod.core.PantzMod;

public class PMBiomeTags {
    public static final TagKey<Biome> IS_BASALT_DELTAS = commonTag("is_basalt_deltas");
    public static final TagKey<Biome> HAS_SULFUR_NETHER = biomeTag("has_sulfur_nether");

    private static TagKey<Biome> biomeTag(String name) {
        return TagUtil.biomeTag(PantzMod.MOD_ID, name);
    }

    private static TagKey<Biome> commonTag(String name) {
        return TagUtil.biomeTag("c", name);
    }

}
