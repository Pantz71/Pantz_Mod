package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import pantz.mod.core.PantzMod;

public class PMBlockTags {
    public static final TagKey<Block> STORAGE_BLOCKS_STEEL = commonTag("storage_blocks/steel");

    public static final TagKey<Block> STORAGE_BLOCKS_SULFUR = commonTag("storage_blocks/sulfur");
    public static final TagKey<Block> ORES_SULFUR = commonTag("ores/sulfur");
    public static final TagKey<Block> REDSTONE_LAMPS = commonTag("redstone_lamps");

    public static final TagKey<Block> GLASS_BLOCKS_CHORUS = commonTag("glass_blocks/chorus");
    public static final TagKey<Block> GLASS_PANES_CHORUS = commonTag("glass_panes/chorus");
    public static final TagKey<Block> GLASS_BLOCKS_SOUL = commonTag("glass_blocks/soul");
    public static final TagKey<Block> GLASS_PANES_SOUL = commonTag("glass_panes/soul");
    public static final TagKey<Block> GLASS_BLOCKS_ECHO = commonTag("glass_blocks/echo");
    public static final TagKey<Block> GLASS_PANES_ECHO = commonTag("glass_panes/echo");

    public static final TagKey<Block> STORAGE_BLOCKS_LEATHER = commonTag("storage_blocks/leather");
    public static final TagKey<Block> STORAGE_BLOCKS_RABBIT_HIDE = commonTag("storage_blocks/rabbit_hide");
    public static final TagKey<Block> STORAGE_BLOCKS_PHANTOM_MEMBRANE = commonTag("storage_blocks/phantom_membrane");
    public static final TagKey<Block> STORAGE_BLOCKS_FEATHER = commonTag("storage_blocks/feather");
    public static final TagKey<Block> STORAGE_BLOCKS_SUGAR = commonTag("storage_blocks/sugar");
    public static final TagKey<Block> STORAGE_BLOCKS_SUGAR_CANE = commonTag("storage_blocks/sugar_cane");
    public static final TagKey<Block> STORAGE_BLOCKS_BLAZE_POWDER = commonTag("storage_blocks/blaze_powder");

    public static final TagKey<Block> NON_WAXED_BLOCKS = blockTag("non_waxed_blocks");
    public static final TagKey<Block> PEDESTALS = blockTag("pedestals");
    public static final TagKey<Block> LOGIC_GATES = blockTag("logic_gates");
    public static final TagKey<Block> PAPER_LANTERNS = blockTag("paper_lanterns");

    private static TagKey<Block> blockTag(String name) {
        return TagUtil.blockTag(PantzMod.MOD_ID, name);
    }

    private static TagKey<Block> commonTag(String name) {
        return TagUtil.blockTag("c", name);
    }
}
