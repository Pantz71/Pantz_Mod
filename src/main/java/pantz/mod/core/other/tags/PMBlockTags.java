package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import pantz.mod.core.PantzMod;

public class PMBlockTags {
    public static final TagKey<Block> STORAGE_BLOCKS_STEEL = forgeTag("storage_blocks/steel");

    public static final TagKey<Block> STORAGE_BLOCKS_SULFUR = forgeTag("storage_blocks/sulfur");
    public static final TagKey<Block> ORES_SULFUR = forgeTag("ores/sulfur");

    public static final TagKey<Block> NON_WAXED_BLOCKS = blockTag("non_waxed_blocks");
    public static final TagKey<Block> PEDESTALS = blockTag("pedestals");

    private static TagKey<Block> blockTag(String name) {
        return TagUtil.blockTag(PantzMod.MOD_ID, name);
    }

    private static TagKey<Block> forgeTag(String name) {
        return TagUtil.blockTag("forge", name);
    }
}
